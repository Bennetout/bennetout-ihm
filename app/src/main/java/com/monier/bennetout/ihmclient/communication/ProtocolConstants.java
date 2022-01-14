package com.monier.bennetout.ihmclient.communication;

public class ProtocolConstants {

    // 8 KiloBytes = 8 Ko =  64000 bits = 64 kbits
    static final int TRAME_LENGTH_MAX_SIZE          = 8000;

    static final int ERROR_EPIPE                    = -2;
    static final int ERROR_UNKNOW                   = -1;
    static final int NO_ERROR                       = 0;

    public static final int STATUS_CONNECTED               = 1;
    public static final int STATUS_NOT_CONNECTED           = 2;

    static final byte ID_ACK                        = (byte) 0xFD;
    static final byte ID_NACK                       = (byte) 0xFE;

    static final byte ID_GET_SENSORS_VALUES         = (byte) 0xE0;
    static final byte ID_SEND_SENSORS_VALUES        = (byte) 0xE1;
    static final byte ID_ACTION                     = (byte) 0xE2;

    static final byte ID_USELESS                    = (byte) 0xD0;

    static final byte RELAY_1                  = (byte) 0x00;
    static final byte RELAY_2                  = (byte) 0x01;
    static final byte RELAY_3                  = (byte) 0x02;
    static final byte RELAY_4                  = (byte) 0x03;
    static final byte RELAY_5                  = (byte) 0x04;
    static final byte RELAY_6                  = (byte) 0x05;
    static final byte RELAY_7                  = (byte) 0x06;
    static final byte RELAY_8                  = (byte) 0x07;
    static final byte RELAY_9                  = (byte) 0x08;
    static final byte RELAY_10                 = (byte) 0x09;
    static final byte RELAY_11                 = (byte) 0x0A;
    static final byte RELAY_12                 = (byte) 0x0B;
    static final byte RELAY_13                 = (byte) 0x0C;
    static final byte RELAY_14                 = (byte) 0x0D;
    static final byte RELAY_15                 = (byte) 0x0E;
    static final byte RELAY_16                 = (byte) 0x0F;

    public static final byte ARG_STATE_HIGH                = (byte) 0x00;
    public static final byte ARG_STATE_LOW                 = (byte) 0x01;

    public static final byte ARG_PORTE              = (byte) 0x01;
    public static final byte ARG_FLECHE             = (byte) 0x02;
    public static final byte ARG_LEVAGE             = (byte) 0x03;
    public static final byte ARG_TAMIS              = (byte) 0x04;

    public static final byte ARG_ACTION_PORTE_ON    = RELAY_1;
    public static final byte ARG_ACTION_PORTE_OFF   = RELAY_2;

    public static final byte ARG_ACTION_FLECHE_ON   = RELAY_3;
    public static final byte ARG_ACTION_FLECHE_OFF  = RELAY_4;

    public static final byte ARG_ACTION_LEVAGE_ON   = RELAY_5;
    public static final byte ARG_ACTION_LEVAGE_OFF  = RELAY_6;

    public static final byte ARG_ACTION_TAPIS_ON    = RELAY_7;
    public static final byte ARG_ACTION_TAPIS_OFF   = RELAY_8;

    public static final byte ARG_ACTION_TAMIS_ON    = RELAY_9;
    public static final byte ARG_ACTION_TAMIS_OFF   = RELAY_10;
}
