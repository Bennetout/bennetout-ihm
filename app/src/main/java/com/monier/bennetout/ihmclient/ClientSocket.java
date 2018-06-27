package com.monier.bennetout.ihmclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientSocket implements Runnable {

    private static final int SOCKET_PORT = 65000;
    private Socket mySocket;
    private boolean isRunning = false;

    private InputStream myInputStream;
    private OutputStream myOutputStream;

    public interface ClientSocketListener {
        void onPositionsReceivedFromServer(double flechePos, double levagePos, double portePos, double niveauX, double niveauY);

        void onSocketStatusUpdate(int status);
    }

    private static final ArrayList<ClientSocketListener> myListeners = new ArrayList<>();
    public static void addListener(ClientSocketListener listener) {
        myListeners.add(listener);
    }

    public static void removeListener(ClientSocketListener listener) {
        if (myListeners.contains(listener))
            myListeners.remove(listener);
    }

    public void connect(String ipAddr) throws IOException {
        mySocket = new Socket(ipAddr, SOCKET_PORT);
        myInputStream = mySocket.getInputStream();
        myOutputStream = mySocket.getOutputStream();

        isRunning = true;
        Thread myThread = new Thread(this);
        myThread.start();
    }

    public void writeToSocket(String texte) throws IOException {
        myOutputStream.write(texte.getBytes());
    }

    public void stopRxThread() {
        isRunning = false;
    }

    private int readNextByte() throws IOException {
        int value;
        value = myInputStream.read();
        if (value == -1) {
            stopRxThread();
        }
        return value;
    }

    private double flecheTest = 0;
    private double levageTest = 0;
    private double porteTest = 0;
    private double niveauXTest = 0;
    private double niveauYTest = 0;
    private void fireData(byte[] data) {
        // TODO: retrieve all fields
        for (ClientSocketListener listener:myListeners) {
            listener.onPositionsReceivedFromServer(flecheTest, levageTest, porteTest, niveauXTest, niveauYTest);
        }
        flecheTest++;
        levageTest++;
        porteTest++;
        niveauXTest++;
        niveauYTest++;
    }

    @Override
    public void run() {

        int value;

        while (isRunning) {
//            try {
//                value = readNextByte();
//                // TODO: STX LENGTH DATA
//                fireData("/26.3/12.9/32.6/5.4/3.2/".getBytes());
//            } catch (IOException e) {
//                stopRxThread();
//                e.printStackTrace();
//            }

            fireData("".getBytes());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
