package com.monier.bennetout.ihmclient.communication;

import android.util.Log;

import java.io.IOException;
import java.net.Socket;

import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.*;

public class Lvl2ClientSocket extends Lvl1SocketCommunications {

    private static final String TAG = Lvl2ClientSocket.class.getCanonicalName();

    private static final int SOCK_TIMEOUT           = 2000;

    private static final int SENSOR_PORTE       = 1;
    private static final int SENSOR_LEVAGE      = 2;
    private static final int SENSOR_FLECHE      = 3;

    private final String ipAddr;
    private final int port;
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

    public void setActuatorState(byte actionNb, byte state) {
        byte[] headers = new byte[]{ID_ACTION, actionNb, state};
        write(headers);
    }

    public void stopSetSensorValue(int sensorType) {

        byte[] headers;

        switch (sensorType) {
            case SENSOR_PORTE:
                headers = new byte[]{ID_STOP_SET_SENSOR_VALUE, ARG_PORTE};
                break;

            case SENSOR_LEVAGE:
                headers = new byte[]{ID_STOP_SET_SENSOR_VALUE, ARG_LEVAGE};
                break;

            case SENSOR_FLECHE:
                headers = new byte[]{ID_STOP_SET_SENSOR_VALUE, ARG_FLECHE};
                break;

            default:
                Log.e(TAG, "stopSetSensorValue: unknown sensorType");
                return;
        }

        write(headers);
    }

    public void setSensorValue(int sensorType, double value) {

        byte[] headers;

        if (value < -127 || value > 127) {
            Log.e(TAG, "setSensorValue: value must be in range [0-255]");
            return;
        }

        byte charValue = (byte) value;

        switch (sensorType) {
            case SENSOR_PORTE:
                headers = new byte[]{ID_SET_SENSOR_VALUE, ARG_PORTE, charValue};
                break;

            case SENSOR_LEVAGE:
                headers = new byte[]{ID_SET_SENSOR_VALUE, ARG_LEVAGE, charValue};
                break;

            case SENSOR_FLECHE:
                headers = new byte[]{ID_SET_SENSOR_VALUE, ARG_FLECHE, charValue};
                break;

            default:
                Log.e(TAG, "setSensorValue: unknown sensorType");
                return;
        }

        write(headers);
    }

    @Override
    protected void onSensorsValuesReceived(byte[] data) {

        String values = new String(data);
        double fleche = 0, levage = 0, porte = 0, inclinoX = 0, inclinoY = 0, tamis = 0;

        String[] splitValues = values.split("/");

        try {
            if (splitValues.length > 1) {
                if (!splitValues[1].isEmpty())
                    fleche = Double.valueOf(splitValues[1]);
            }

            if (splitValues.length > 2) {
                if (!splitValues[2].isEmpty())
                    levage = Double.valueOf(splitValues[2]);
            }

            if (splitValues.length > 3) {
                if (!splitValues[3].isEmpty())
                    porte = Double.valueOf(splitValues[3]);
            }

            if (splitValues.length > 4) {
                if (!splitValues[4].isEmpty())
                    inclinoX = Double.valueOf(splitValues[4]);
            }

            if (splitValues.length > 5) {
                if (!splitValues[5].isEmpty())
                    inclinoY = Double.valueOf(splitValues[5]);
            }

            if (splitValues.length > 6) {
                if (!splitValues[6].isEmpty())
                    tamis = Double.valueOf(splitValues[6]);
            }

            if (myListener != null)
                myListener.onPositionsReceivedFromServer(fleche, levage, porte, inclinoX, inclinoY, tamis);

        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }
    }

    @Override
    public void onSocketStatusUpdate(int status) {
        if (myListener != null)
            myListener.onSocketStatusUpdate(status);
    }

    @Override
    protected void onSetSensorValueFinish(byte sensorArg) {
        if (myListener != null)
            myListener.onSetSensorValueFinish(sensorArg);
    }

    public interface SocketClientListener {
        void onSocketStatusUpdate(int status);
        void onPositionsReceivedFromServer(double flechePos, double levagePos, double portePos, double niveauX, double niveauY, double tamisPos);
        void onSetSensorValueFinish(byte sensorArg);
    }
}