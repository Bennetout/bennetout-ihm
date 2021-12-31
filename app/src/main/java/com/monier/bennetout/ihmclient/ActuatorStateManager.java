package com.monier.bennetout.ihmclient;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;

import com.monier.bennetout.ihmclient.communication.Lvl2ClientSocket;
import com.monier.bennetout.ihmclient.configuration.ConfigManager;

import java.util.Objects;

import mehdi.sakout.fancybuttons.FancyButton;

import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.ARG_STATE_HIGH;
import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.ARG_STATE_LOW;

public class ActuatorStateManager {

    private boolean statusButtonON, statusButtonOFF;

    @SuppressLint("ClickableViewAccessibility")
    public ActuatorStateManager(double buttonType, Lvl2ClientSocket myLvl2ClientSocket, byte actionON, byte actionOFF, FancyButton buttonON, FancyButton buttonOFF, int colorStatusON) {
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
        } else {
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
    }
}
