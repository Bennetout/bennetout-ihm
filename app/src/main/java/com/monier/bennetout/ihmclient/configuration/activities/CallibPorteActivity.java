package com.monier.bennetout.ihmclient.configuration.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.monier.bennetout.ihmclient.CaptorValuesSingleton;
import com.monier.bennetout.ihmclient.R;
import com.monier.bennetout.ihmclient.configuration.ConfigManager;

import mehdi.sakout.fancybuttons.FancyButton;

import static com.monier.bennetout.ihmclient.utils.Utils.formatDouble;

public class CallibPorteActivity extends Activity {

    private double porteZero = 0, porteCent = 100;

    private Handler myHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.callib_porte);

        btnCallibPorteZeroInit();
        btnCallibPorteCentInit();

        myHandler = new Handler();
        myHandler.postDelayed(majIhm, 0);
    }

    private void btnCallibPorteCentInit() {
        FancyButton fancyButton = findViewById(R.id.btnCallibPorteCent);
        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfigManager.model.PORTE_CALLIB_CENT = porteCent;
                ConfigManager.model2ConfigFile(getApplicationContext());
            }
        });
    }

    private void btnCallibPorteZeroInit() {
        FancyButton fancyButton = findViewById(R.id.btnCallibPorteZero);
        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfigManager.model.PORTE_CALLIB_ZERO = porteZero;
                ConfigManager.model2ConfigFile(getApplicationContext());
            }
        });
    }

    private Runnable majIhm = new Runnable() {
        @Override
        public void run() {

            double portePos = CaptorValuesSingleton.getAnglePorte();
            TextView zero = findViewById(R.id.textViewCallibPorteZero);
            TextView cent = findViewById(R.id.textViewCallibPorteCent);
            zero.setText(formatDouble(portePos));
            cent.setText(formatDouble(portePos));
            porteZero = portePos;
            porteCent = portePos;

            myHandler.postDelayed(this, 500);
        }
    };
}
