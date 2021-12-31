package com.monier.bennetout.ihmclient.configuration;

class ConfigDefaults {

    static final double FLECHE_CALLIB_ZERO_D = 0;
    static final double FLECHE_CALLIB_CENT_D = 100;

    static final double LEVAGE_CALLIB_ZERO_D = 0;
    static final double LEVAGE_CALLIB_CENT_D = 100;

    static final double PORTE_CALLIB_ZERO_D = 0;
    static final double PORTE_CALLIB_CENT_D = 100;

    static final double NIVEAU_CALLIB_ZERO_D = 0;

    static final double TAMIS_CALLIB_ZERO_D = 0;
    static final double TAMIS_CALLIB_CENT_D = 100;

    static final double BORNE_MAX_FLECHE_D   = 30;
    static final double BORNE_MIN_FLECHE_D   = -30;

    static final double BORNE_MAX_LEVAGE_D   = 45;
    static final double BORNE_MIN_LEVAGE_D   = 0;

    static final double BORNE_MAX_PORTE_D   = 90;
    static final double BORNE_MIN_PORTE_D   = 0;

    static final double BORNE_MAX_TAMIS_D   = 7;
    static final double BORNE_MIN_TAMIS_D   = 0;

    static final double[] PORTE_CONFIGS_D = {0, 50, 75, 100};
    static final double[] FLECHE_CONFIGS_D = {-5, -10, -15, -20, 20, 15, 10, 5};
    static final double[] LEVAGE_CONFIGS_D = {0, 25, 50, 75, 100};

    static final double TYPE_BOUTON_TAPIS_D = ConfigManager.TYPE_BOUTON_AUTOMAINTIEN;
    static final double TYPE_BOUTON_PORTE_D = ConfigManager.TYPE_BOUTON_IMPULSION;
    static final double TYPE_BOUTON_FLECHE_D = ConfigManager.TYPE_BOUTON_IMPULSION;
    static final double TYPE_BOUTON_LEVAGE_D = ConfigManager.TYPE_BOUTON_IMPULSION;
}