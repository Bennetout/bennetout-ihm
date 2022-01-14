package com.monier.bennetout.ihmclient;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;

import com.monier.bennetout.ihmclient.communication.Lvl2ClientSocket;
import com.monier.bennetout.ihmclient.configuration.ConfigManager;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

import mehdi.sakout.fancybuttons.FancyButton;

import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.ARG_STATE_HIGH;
import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.ARG_STATE_LOW;

public class ActuatorStateManager {

    private boolean statusButtonON, statusButtonOFF;

    @SuppressLint("ClickableViewAccessibility")
    public ActuatorStateManager(double buttonType, Lvl2ClientSocket myLvl2ClientSocket, byte actionON, byte actionOFF,
                                FancyButton buttonON, FancyButton buttonOFF, int colorStatusON,
                                MyListViewAdapter listView) {
        Drawable drawable = buttonON.getBackground();
        // On travaille avec des clones
        final Drawable drawableInit = Objects.requireNonNull(drawable.getConstantState()).newDrawable();

        if (buttonType == ConfigManager.TYPE_BOUTON_IMPULSION) {
            buttonON.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            myLvl2ClientSocket.setActuatorState(actionON, ARG_STATE_HIGH);
                            break;

                        case MotionEvent.ACTION_UP:
                            myLvl2ClientSocket.setActuatorState(actionON, ARG_STATE_LOW);
                            break;
                    }
                    return false;
                }
            });

            buttonOFF.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            myLvl2ClientSocket.setActuatorState(actionOFF, ARG_STATE_HIGH);
                            break;

                        case MotionEvent.ACTION_UP:
                            myLvl2ClientSocket.setActuatorState(actionOFF, ARG_STATE_LOW);
                            break;
                    }
                    return false;
                }
            });
        }

        if (buttonType == ConfigManager.TYPE_BOUTON_AUTOMAINTIEN) {
            buttonON.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (statusButtonOFF)
                        return;

                    statusButtonON = !statusButtonON;

                    if (statusButtonON) {
                        Drawable drawable = buttonON.getBackground();
                        drawable.setColorFilter(colorStatusON, PorterDuff.Mode.MULTIPLY);
                        buttonON.setBackground(drawable);
                        myLvl2ClientSocket.setActuatorState(actionON, ARG_STATE_HIGH);
                    } else {
                        buttonON.setBackground(Objects.requireNonNull(drawableInit.getConstantState()).newDrawable());
                        myLvl2ClientSocket.setActuatorState(actionON, ARG_STATE_LOW);
                    }
                }
            });

            buttonOFF.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (statusButtonON)
                        return;

                    statusButtonOFF = !statusButtonOFF;

                    if (statusButtonOFF) {
                        Drawable drawable = buttonOFF.getBackground();
                        drawable.setColorFilter(colorStatusON, PorterDuff.Mode.MULTIPLY);
                        buttonOFF.setBackground(drawable);
                        myLvl2ClientSocket.setActuatorState(actionOFF, ARG_STATE_HIGH);
                    } else {
                        buttonOFF.setBackground(Objects.requireNonNull(drawableInit.getConstantState()).newDrawable());
                        myLvl2ClientSocket.setActuatorState(actionOFF, ARG_STATE_LOW);
                    }
                }
            });
        }

        if (buttonType == ConfigManager.TYPE_BOUTON_BANDEAU) {
            buttonON.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    double actualTamisValue = MainActivity.calculPosTamis(CaptorValuesSingleton.getAngleTamis());
                    try {
                        actualTamisValue = listView.getValueSelected();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    double[] possibleTamisValues = listView.getAllValues();
                    try {
                        int nextValueIndex = getNextValue(actualTamisValue +0.5, possibleTamisValues);
                        listView.switchActiveItem(nextValueIndex);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            buttonOFF.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    double actualTamisValue = MainActivity.calculPosTamis(CaptorValuesSingleton.getAngleTamis());
                    try {
                        actualTamisValue = listView.getValueSelected();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    double[] possibleTamisValues = listView.getAllValues();
                    try {
                        int previousValueIndex = getPreviousValue(actualTamisValue -0.5, possibleTamisValues);
                        listView.switchActiveItem(previousValueIndex);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private int getNextValue(double actualValue, double[] allValues) throws Exception {
        int result = -1;
        double ecart = Double.MAX_VALUE;
        boolean findOneValue = false;

        for (int i = 0; i < allValues.length; i++) {
            if (allValues[i] <= actualValue)
                continue;

            if (allValues[i] - actualValue < ecart) {
                result = i;
                ecart = allValues[i] - actualValue;
                findOneValue = true;
            }
        }

        if (!findOneValue)
            throw new Exception("No value found");

        return result;
    }

    private int getPreviousValue(double actualValue, double[] allValues) throws Exception {
        int result = -1;
        double ecart = Double.MAX_VALUE;
        boolean findOneValue = false;

        for (int i = 0; i < allValues.length; i++) {
            if (allValues[i] >= actualValue)
                continue;

            if (actualValue - allValues[i] < ecart) {
                result = i;
                ecart = actualValue - allValues[i];
                findOneValue = true;
            }
        }

        if (!findOneValue)
            throw new Exception("No value found");

        return result;
    }
}
