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

public class CallibNiveauActivity extends Activity {

    private double niveauZero = 0, niveauZeroActual;
    private TextView textViewActualZero;

    private Handler myHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.callib_niveau);

        niveauZeroActual = ConfigManager.model.NIVEAU_CALLIB_ZERO;

        textViewActualZero = findViewById(R.id.textViewCallibNiveauZeroActual);
        majTextViewActuals();

        btnCallibNiveauZeroInit();
        btnCallibNiveauSaveInit();

        myHandler = new Handler();
        myHandler.postDelayed(majIhm, 0);
    }

    private void majTextViewActuals() {
        textViewActualZero.setText(formatDouble(niveauZeroActual));
    }

    private void btnCallibNiveauZeroInit() {
        FancyButton fancyButton = findViewById(R.id.btnCallibNiveauZero);
        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                niveauZeroActual = niveauZero;
                majTextViewActuals();
            }
        });
    }

    private void btnCallibNiveauSaveInit() {
        FancyButton fancyButton = findViewById(R.id.btnCallibNiveauSave);
        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfigManager.model.NIVEAU_CALLIB_ZERO = niveauZeroActual;
                ConfigManager.model2ConfigFile(getApplicationContext());

                finish();
            }
        });
    }

    private Runnable majIhm = new Runnable() {
        @Override
        public void run() {

            double niveauX = CaptorValuesSingleton.getNiveau();
            TextView zero = findViewById(R.id.textViewCallibNiveauZero);
            zero.setText(formatDouble(niveauX));
            niveauZero = niveauX;

            myHandler.postDelayed(this, 500);
        }
    };
}
