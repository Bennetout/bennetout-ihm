package com.monier.bennetout.ihmclient.communication;

import java.io.IOException;
import java.net.Socket;

public class Lvl2ClientSocket extends Lvl1SocketCommunications {

    private static final String TAG = Lvl2ClientSocket.class.getCanonicalName();

    private String ipAddr;
    private int port;

    public Lvl2ClientSocket(String ipAddr, int port) {
        super();
        this.ipAddr = ipAddr;
        this.port = port;
    }

    public void connect() throws IOException {
        setSocketCommunication(new Socket(ipAddr, port));
        if (myListener != null)
            myListener.onLogReceiveFromClient("Connection success");
    }

    public void deconnect() {
        setSocketCommunication(null);
    }

    private SocketClientListener myListener;
    public void setListener(SocketClientListener myListener) {
        this.myListener = myListener;
    }

    @Override
    protected void logOnConsole(byte[] log) {
        if (myListener != null)
            myListener.onLogReceiveFromClient(new String(log));
    }

    public interface SocketClientListener {
        void onLogReceiveFromClient(CharSequence log);
        void onMessageReceivedFromClient(byte[] message);
    }
}