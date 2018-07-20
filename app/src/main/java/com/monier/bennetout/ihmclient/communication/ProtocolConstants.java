package com.monier.bennetout.ihmclient.communication;

class ProtocolConstants {

    // 8 KiloBytes = 8 Ko =  64000 bits = 64 kbits
    static final int TRAME_LENGTH_MAX_SIZE          = 8000;

    static final int ERROR_EPIPE                    = -2;
    static final int ERROR_UNKNOW                   = -1;
    static final int NO_ERROR                       = 0;

    static final byte ID_ACK                        = (byte) 0xFD;
    static final byte ID_NACK                       = (byte) 0xFE;

    static final byte ID_USELESS                    = (byte) 0xD0;
}
