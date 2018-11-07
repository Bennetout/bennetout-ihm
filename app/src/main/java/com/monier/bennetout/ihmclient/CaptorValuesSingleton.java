package com.monier.bennetout.ihmclient;

public class CaptorValuesSingleton {

    private static double angleFleche = 0, angleLevage = 0, anglePorte = 0;
    private static double niveau = 0;

    public static double getAngleFleche() {
        return angleFleche;
    }

    public static void setAngleFleche(double newAngle) {
        angleFleche = newAngle;
    }

    public static double getAngleLevage() {
        return angleLevage;
    }

    public static void setAngleLevage(double newAngle) {
        angleLevage = newAngle;
    }

    public static double getAnglePorte() {
        return anglePorte;
    }

    public static void setAnglePorte(double newAngle) {
        anglePorte = newAngle;
    }

    public static double getNiveau() {
        return niveau;
    }

    public static void setNiveau(double newPos) {
        niveau = newPos;
    }
}
