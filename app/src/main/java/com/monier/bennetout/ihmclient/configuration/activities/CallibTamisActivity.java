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

public class CallibTamisActivity extends Activity {

    private double tamisZero = 0, tamisCent = 100, tamisZeroActual, tamisCentActual;
    private TextView textViewActualZero, textViewActualCent;

    private Handler myHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.callib_tamis);

        tamisZeroActual = ConfigManager.model.TAMIS_CALLIB_ZERO;
        tamisCentActual = ConfigManager.model.TAMIS_CALLIB_CENT;

        textViewActualZero = findViewById(R.id.textViewCallibTamisZeroActual);
        textViewActualCent = findViewById(R.id.textViewCallibTamisCentActual);
        majTextViewActuals();

        btnCallibTamisZeroInit();
        btnCallibTamisCentInit();
        btnCallibTamisSaveInit();

        myHandler = new Handler();
        myHandler.postDelayed(majIhm, 0);
    }

    private void majTextViewActuals() {
        textViewActualZero.setText(formatDouble(tamisZeroActual));
        textViewActualCent.setText(formatDouble(tamisCentActual));
    }

    private void btnCallibTamisCentInit() {
        FancyButton fancyButton = findViewById(R.id.btnCallibTamisCent);
        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tamisCentActual = tamisCent;
                majTextViewActuals();
            }
        });
    }

    private void btnCallibTamisZeroInit() {
        FancyButton fancyButton = findViewById(R.id.btnCallibTamisZero);
        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tamisZeroActual = tamisZero;
                majTextViewActuals();
            }
        });
    }

    private void btnCallibTamisSaveInit() {
        FancyButton fancyButton = findViewById(R.id.btnCallibTamisSave);
        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfigManager.model.TAMIS_CALLIB_ZERO = tamisZeroActual;
                ConfigManager.model.TAMIS_CALLIB_CENT = tamisCentActual;
                ConfigManager.model2ConfigFile(getApplicationContext());

                finish();
            }
        });
    }

    private Runnable majIhm = new Runnable() {
        @Override
        public void run() {

            double tamisPos = CaptorValuesSingleton.getAngleTamis();
            TextView zero = findViewById(R.id.textViewCallibTamisZero);
            TextView cent = findViewById(R.id.textViewCallibTamisCent);
            zero.setText(formatDouble(tamisPos));
            cent.setText(formatDouble(tamisPos));
            tamisZero = tamisPos;
            tamisCent = tamisPos;

            myHandler.postDelayed(this, 500);
        }
    };
}