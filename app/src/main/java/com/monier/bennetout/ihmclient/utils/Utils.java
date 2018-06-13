package com.monier.bennetout.ihmclient.utils;

import java.util.Locale;

public class Utils {

    public static String formatDouble(double number) {
        return String.format(Locale.FRANCE,"%.1f", number);
    }
}
