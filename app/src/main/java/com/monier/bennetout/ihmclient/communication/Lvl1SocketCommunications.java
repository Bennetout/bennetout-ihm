package com.monier.bennetout.ihmclient.communication;

import android.os.AsyncTask;
import android.util.Log;

import java.net.Socket;
import java.util.Arrays;

import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.*;

public class Lvl1SocketCommunications implements Lvl0ProtocolThread.Lvl0ProtocolListener {

    private static final String TAG = Lvl1SocketCommunications.class.getCanonicalName();

    // Useless at this time
    private byte idCounter = 1;

    private Lvl0ProtocolThread myLvl0ProtocolThread;

    Lvl1SocketCommunications() {
    }

    protected void onLogReceived(byte[] log) {
        Log.i(TAG, new String(log));
    }


    void setSocketCommunication(Socket socketCommunication) {

        if (myLvl0ProtocolThread != null)
            myLvl0ProtocolThread.stopThread();

        if (socketCommunication == null)
            return;

        myLvl0ProtocolThread = new Lvl0ProtocolThread(socketCommunication);
        myLvl0ProtocolThread.setObserver(this);
        idCounter = 1;
        new Thread(myLvl0ProtocolThread).start();
    }

    private int writeToSocket(byte[] toWrite) {
        if (myLvl0ProtocolThread == null) {
            onLogReceived("Socket not connected".getBytes());
            return -1;
        }

        if (myLvl0ProtocolThread.sendToLvl0(idCounter, toWrite) == ERROR_EPIPE) {
            myLvl0ProtocolThread.stopThread();
            onLogReceived("Socket not yet connected, rx thread will close automatically".getBytes());
            return -1;
        }

//        onLogReceived("write on socket successful".getBytes());
        return 0;
    }

    private void sendAck() {
        byte[] headers = new byte[]{ID_ACK};
        write(headers);
        onLogReceived("ACK sent".getBytes());
    }

    private boolean ackReceived = false;
    @Override
    public void onDataReceivedFromLvl0(byte[] data) {

        if (data.length < 1) {
            onLogReceived("Empty data received from Socket".getBytes());
            return;
        }

        byte id;
        byte arg;

        id = data[0];
        switch (id) {
            case ID_USELESS:
                onLogReceived(Arrays.copyOfRange(data, 6, data.length));
                break;

            case ID_ACK:
                ackReceived = true;
                break;

            case ID_SEND_SENSORS_VALUES:
                onSensorsValuesReceived(Arrays.copyOfRange(data, 1, data.length));
                break;

            case ID_SET_SENSOR_VALUE_FINISH:
                arg = data[1];
                onSetSensorValueFinish(arg);
                break;
        }
    }

    protected void onSensorsValuesReceived(byte[] data) {

    }

    protected void onSetSensorValueFinish(byte sensorArg) {

    }

    @Override
    public void onSocketStatusUpdate(int status) {
    }

    private class SendOnSocket extends AsyncTask<Void, Void, Void> {

        private byte[] header = null;

        SendOnSocket(byte[] header) {
            this.header = header;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            writeToSocket(header);

            return null;
        }
    }

    void write(byte[] message) {
//        new SendOnSocket(message).execute();
//        new SendOnSocket(message).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        writeToSocket(message);
    }
}
