package com.monier.bennetout.ihmclient;

import com.monier.bennetout.ihmclient.utils.Utils;

public class MyCustomHolder {
    String textToShow;
    double value;
    boolean isActive;
    int activeColor;

    public MyCustomHolder(double value, boolean isActive, int activeColor) {
        this.value = value;
        this.textToShow = Utils.formatDouble(value) + "°";
        this.isActive = isActive;
        this.activeColor = activeColor;
    }
}
