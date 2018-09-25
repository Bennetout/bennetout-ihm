package com.monier.bennetout.ihmclient.utils;

import java.util.Locale;

public class Utils {

    public static String formatDouble(double number) {
        return String.format(Locale.FRANCE,"%.1f", number);
    }

    public static double degree2radian(double degree) {
        return (Math.PI / (double) 180) * degree;
    }
}
