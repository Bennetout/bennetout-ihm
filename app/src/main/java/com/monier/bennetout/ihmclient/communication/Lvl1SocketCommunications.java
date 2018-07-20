package com.monier.bennetout.ihmclient.communication;

import android.os.AsyncTask;

import java.net.Socket;
import java.util.Arrays;

import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.*;

public class Lvl1SocketCommunications implements Lvl0ProtocolThread.Lvl0ProtocolListener {

    private static final String TAG = Lvl1SocketCommunications.class.getCanonicalName();

    // Useless at this time
    private byte idCounter = 1;

    private Lvl0ProtocolThread myLvl0ProtocolThread;

    public enum fileType{PLANNING, CONFIG, LOG_AUDIO, LOG_MOVE, LOG_ERROR}
    private fileType fileType;

    Lvl1SocketCommunications() {
    }

    protected void logOnConsole(byte[] log) {

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
            logOnConsole("Socket not connected".getBytes());
            return -1;
        }

        if (myLvl0ProtocolThread.sendToLvl0(idCounter, toWrite) == ERROR_EPIPE) {
            myLvl0ProtocolThread.stopThread();
            logOnConsole("Socket not yet connected, rx thread will close automatically".getBytes());
            return -1;
        }

//        logOnConsole("write on socket successful".getBytes());
        return 0;
    }

    public void sendUselessMessage(byte[] message) {
        byte[] headers = new byte[]{ID_USELESS};

        byte[] toSend = new byte[headers.length + message.length];
        System.arraycopy(headers,0,toSend,0         ,headers.length);
        System.arraycopy(message,0,toSend,headers.length,message.length);

        write(toSend);
    }

    private void sendAck() {
        byte[] headers = new byte[]{ID_ACK};
        write(headers);
        logOnConsole("ACK sent".getBytes());
    }

    private boolean ackReceived = false;
    @Override
    public void onDataReceivedFromLvl0(byte[] data) {

        if (data.length < 1) {
            logOnConsole("Empty data received from Socket".getBytes());
            return;
        }

        byte id;
        byte arg;

        id = data[0];
        switch (id) {
            case ID_USELESS:
                logOnConsole(Arrays.copyOfRange(data, 6, data.length));
                break;

            case ID_ACK:
                ackReceived = true;
                break;
        }
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

    private void write(byte[] message) {
        new SendOnSocket(message).execute();
    }
}
