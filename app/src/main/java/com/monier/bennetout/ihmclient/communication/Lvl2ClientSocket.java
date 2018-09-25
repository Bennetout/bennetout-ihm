package com.monier.bennetout.ihmclient.communication;

import java.io.IOException;
import java.net.Socket;

import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.ID_GET_SENSORS_VALUES;
import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.STATUS_CONNECTED;
import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.STATUS_NOT_CONNECTED;

public class Lvl2ClientSocket extends Lvl1SocketCommunications {

    private static final String TAG = Lvl2ClientSocket.class.getCanonicalName();

    private static final int SOCK_TIMEOUT           = 2000;

    private String ipAddr;
    private int port;
    private Socket mySocket = null;
    private SocketClientListener myListener;

    public Lvl2ClientSocket(String ipAddr, int port) {
        super();
        this.ipAddr = ipAddr;
        this.port = port;
    }

    public void connect() throws IOException {

        mySocket = new Socket(ipAddr, port);
        mySocket.setSoTimeout(SOCK_TIMEOUT);
        setSocketCommunication(mySocket);

        if (myListener != null)
            myListener.onSocketStatusUpdate(STATUS_CONNECTED);
    }

    public void deconnect() {
        setSocketCommunication(null);
        if (myListener != null)
            myListener.onSocketStatusUpdate(STATUS_NOT_CONNECTED);
    }

    public void setListener(SocketClientListener myListener) {
        this.myListener = myListener;
    }

    public void getSensorsValues() {
        byte[] headers = new byte[]{ID_GET_SENSORS_VALUES};
        write(headers);
    }

    @Override
    protected void onSensorsValuesReceived(byte[] data) {

        String values = new String(data);

        String[] splitValues = values.split("/");
        if (splitValues.length < 6)
            return;

        if (splitValues[1].isEmpty())
            return;
        if (splitValues[2].isEmpty())
            return;
        if (splitValues[3].isEmpty())
            return;
        if (splitValues[4].isEmpty())
            return;
        if (splitValues[5].isEmpty())
            return;

        try {
            double fleche = Double.valueOf(splitValues[1]);
            double levage = Double.valueOf(splitValues[2]);
            double porte = Double.valueOf(splitValues[3]);
            double inclinoX = Double.valueOf(splitValues[4]);
            double inclinoY = Double.valueOf(splitValues[5]);

            if (myListener != null)
                myListener.onPositionsReceivedFromServer(fleche, levage, porte, inclinoX, inclinoY);

        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
    }

    @Override
    public void onSocketStatusUpdate(int status) {
        if (myListener != null)
            myListener.onSocketStatusUpdate(status);
    }

    public interface SocketClientListener {
        void onSocketStatusUpdate(int status);
        void onPositionsReceivedFromServer(double flechePos, double levagePos, double portePos, double niveauX, double niveauY);
    }
}