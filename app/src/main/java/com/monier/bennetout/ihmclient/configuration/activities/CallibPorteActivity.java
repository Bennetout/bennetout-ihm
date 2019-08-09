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

    private double porteZero = 0, porteCent = 100, porteZeroActual, porteCentActual;
    private TextView textViewActualZero, textViewActualCent;

    private Handler myHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.callib_porte);

        porteZeroActual = ConfigManager.model.PORTE_CALLIB_ZERO;
        porteCentActual = ConfigManager.model.PORTE_CALLIB_CENT;

        textViewActualZero = findViewById(R.id.textViewCallibPorteZeroActual);
        textViewActualCent = findViewById(R.id.textViewCallibPorteCentActual);
        majTextViewActuals();

        btnCallibPorteZeroInit();
        btnCallibPorteCentInit();
        btnCallibPorteSaveInit();

        myHandler = new Handler();
        myHandler.postDelayed(majIhm, 0);
    }

    private void majTextViewActuals() {
        textViewActualZero.setText(formatDouble(porteZeroActual));
        textViewActualCent.setText(formatDouble(porteCentActual));
    }

    private void btnCallibPorteCentInit() {
        FancyButton fancyButton = findViewById(R.id.btnCallibPorteCent);
        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                porteCentActual = porteCent;
                majTextViewActuals();
            }
        });
    }

    private void btnCallibPorteZeroInit() {
        FancyButton fancyButton = findViewById(R.id.btnCallibPorteZero);
        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                porteZeroActual = porteZero;
                majTextViewActuals();
            }
        });
    }

    private void btnCallibPorteSaveInit() {
        FancyButton fancyButton = findViewById(R.id.btnCallibPorteSave);
        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfigManager.model.PORTE_CALLIB_ZERO = porteZeroActual;
                ConfigManager.model.PORTE_CALLIB_CENT = porteCentActual;
                ConfigManager.model2ConfigFile(getApplicationContext());

                finish();
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
