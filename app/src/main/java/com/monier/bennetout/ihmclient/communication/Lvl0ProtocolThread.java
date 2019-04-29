package com.monier.bennetout.ihmclient.communication;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import static com.monier.bennetout.ihmclient.communication.Lvl0ProtocolThread.machine_state.*;
import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.ERROR_EPIPE;
import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.ERROR_UNKNOW;
import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.NO_ERROR;
import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.STATUS_NOT_CONNECTED;
import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.TRAME_LENGTH_MAX_SIZE;

public class Lvl0ProtocolThread implements Runnable {

    private static final String TAG = Lvl0ProtocolThread.class.getCanonicalName();

    private static final int INDEX_STX = 0;
    private static final int INDEX_LENGTH = 1;
    private static final int INDEX_ID = 3;
    private static final int INDEX_DATA = 4;

    // STX + length + id + checksum
    private static final int WITHOUT_DATA_LENGTH = 1 +2 +1 +4;
    private static final int CHECKSUM_LENGTH = 4;

    private static final int STX = 0x02;

    private InputStream inputStream;
    private OutputStream outputStream;
    private byte[] dataIn;
    private machine_state state = WAIT_STX;
    enum machine_state {WAIT_STX, GET_LENGTH, GET_ID, GET_DATA, VERIFY_CHECKSUM, FIRE_DATA}
    private boolean running = false;

    Lvl0ProtocolThread(Socket clientSocket) {
        Socket socket = clientSocket;
        try {
            this.inputStream = socket.getInputStream();
            this.outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Lvl0ProtocolListener myListener;
    public interface Lvl0ProtocolListener {
        void onDataReceivedFromLvl0(byte[] data);
        void onSocketStatusUpdate(int status);
    }

    public void setObserver(Lvl0ProtocolListener mObserver) {
        myListener = mObserver;
    }

    private int readNextByte() throws IOException {
        int value;
        value = inputStream.read();
        if (value == -1) {
            stopThread();
        }
        return value;
    }

    @Override
    public void run() {
        int value;
        int indexData = 0;
        int lengthLsb, lengthMsb;
        int length = 0;
        running = true;

        while (running) {
            try {
                    switch (state) {
                        case WAIT_STX:
                            value = readNextByte();
                            if (value == STX) {
                                state = GET_LENGTH;
                            }
                            break;

                        case GET_LENGTH:
                            value = readNextByte();
                            lengthLsb= value;
                            value = readNextByte();
                            lengthMsb = value;
                            length = lengthMsb * 256 + lengthLsb;
                            if (length > TRAME_LENGTH_MAX_SIZE) {
                                Log.e(TAG, ("Error, length is larger than TRAME_LENGTH_MAX_SIZE = " + TRAME_LENGTH_MAX_SIZE + "bytes"));
                                state = WAIT_STX;
                                continue;
                            }

                            dataIn = new byte[length + WITHOUT_DATA_LENGTH];
                            dataIn[INDEX_STX] = STX;
                            dataIn[INDEX_LENGTH] = (byte) lengthLsb;
                            dataIn[INDEX_LENGTH +1] = (byte) lengthMsb;
                            state = GET_ID;
                            break;

                        case GET_ID:
                            int id = readNextByte();
                            dataIn[INDEX_ID] = (byte) id;
                            indexData = INDEX_DATA;
                            state = GET_DATA;
                            break;

                        case GET_DATA:
                            if (indexData >= (length + WITHOUT_DATA_LENGTH - CHECKSUM_LENGTH)) {
                                state = VERIFY_CHECKSUM;
                                continue;
                            }

                            value = readNextByte();
                            dataIn[indexData++] = (byte) value;

                            break;

                        case VERIFY_CHECKSUM:
                            byte[] checksumByteArray = new byte[4];
                            int checksum = readNextByte();
                            checksumByteArray[0] = (byte) checksum;
                            checksum = readNextByte();
                            checksumByteArray[1] = (byte) checksum;
                            checksum = readNextByte();
                            checksumByteArray[2] = (byte) checksum;
                            checksum = readNextByte();
                            checksumByteArray[3] = (byte) checksum;
                            long checksumReceived = (long)(checksumByteArray[3]&0xff)*(long)256*(long)256*(long)256 +
                                                    (long)(checksumByteArray[2]&0xff)*(long)256*(long)256 +
                                                    (long)(checksumByteArray[1]&0xff)*(long)256 +
                                                    (long)(checksumByteArray[0]&0xff);

                            final int INDEX_CHECKSUM = indexData;
                            if(!verifyChecksum(Arrays.copyOfRange(dataIn, INDEX_STX, INDEX_CHECKSUM), checksumReceived)) {
                                // Just not follow the corrupted message
                                // the timeout of the high level will retry
                                state = WAIT_STX;
                            } else {
                                state = FIRE_DATA;
                            }
                            break;

                        case FIRE_DATA:
                            byte[] data = new byte[length];
                            System.arraycopy(dataIn, INDEX_DATA, data, 0, length);
                            myListener.onDataReceivedFromLvl0(data);
                            dataIn = null;
                            state = WAIT_STX;
                            break;

                        default:
                            break;
                    }

            } catch (SocketTimeoutException ste) {
                state = WAIT_STX;
            } catch (IOException e) {
                if (e.getMessage().contains("EPIPE") || e.getMessage().contains("ECONNRESET")) {
                    stopThread();
                } else {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean verifyChecksum(byte[] data, long checksum) {

        Checksum checksumExpected = new CRC32();
        checksumExpected.update(data, 0, data.length);

        boolean isSameChecksum = checksumExpected.getValue() == checksum;
        if (!isSameChecksum)
            Log.e(TAG, "checksum = " + checksum + "\nchecksumExpected.getValue() = " + checksumExpected.getValue());

        return isSameChecksum;
    }

    private long getChecksum(byte[] data) {

        Checksum checksum = new CRC32();
        checksum.update(data, 0, data.length);

        return checksum.getValue();
    }

    public void stopThread() {
        running = false;
        if (myListener != null)
            myListener.onSocketStatusUpdate(STATUS_NOT_CONNECTED);
    }

    public int sendToLvl0(byte idTrame, byte[] data) {

        int ret = ERROR_UNKNOW;

        int length = data.length;
        byte lengthLsb = (byte) (length & 0xFF);
        byte lengthMsb = (byte) ((length >> 8) & 0xFF);

        byte[] toSend = new byte[data.length + WITHOUT_DATA_LENGTH];
        toSend[INDEX_STX] = STX;
        toSend[INDEX_LENGTH] = lengthLsb;
        toSend[INDEX_LENGTH +1] = lengthMsb;
        toSend[INDEX_ID] = idTrame;
        System.arraycopy(data, 0, toSend, INDEX_DATA, length);

        final int INDEX_CHECKSUM = INDEX_DATA + data.length;
        long checksum = getChecksum(Arrays.copyOfRange(toSend, INDEX_STX, INDEX_CHECKSUM));
        ByteBuffer byteBufferChecksum = ByteBuffer.allocate(8);
        byteBufferChecksum.order(ByteOrder.LITTLE_ENDIAN);
        byteBufferChecksum.putLong(checksum);

        byte[] checksumByteArray = byteBufferChecksum.array();
        System.arraycopy(checksumByteArray, 0, toSend, INDEX_CHECKSUM, 4);

        try {
            outputStream.write(toSend);
            outputStream.flush();
            ret = NO_ERROR;
        } catch (IOException e) {
            if (e.getMessage().contains("EPIPE")) {
                ret = ERROR_EPIPE;
            } else {
                e.printStackTrace();
            }
        }
        return ret;
    }
}