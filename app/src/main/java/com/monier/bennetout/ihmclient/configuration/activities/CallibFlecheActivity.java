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

public class CallibFlecheActivity extends Activity {

    private double flecheZero = 0, flecheCent = 100;

    private Handler myHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.callib_fleche);

        btnCallibFlecheZeroInit();
        btnCallibFlecheCentInit();

        myHandler = new Handler();
        myHandler.postDelayed(majIhm, 0);
    }

    private void btnCallibFlecheCentInit() {
        FancyButton fancyButton = findViewById(R.id.btnCallibFlecheCent);
        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfigManager.model.FLECHE_CALLIB_CENT = flecheCent;
                ConfigManager.model2ConfigFile(getApplicationContext());
            }
        });
    }

    private void btnCallibFlecheZeroInit() {
        FancyButton fancyButton = findViewById(R.id.btnCallibFlecheZero);
        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfigManager.model.FLECHE_CALLIB_ZERO = flecheZero;
                ConfigManager.model2ConfigFile(getApplicationContext());
            }
        });
    }

    private Runnable majIhm = new Runnable() {
        @Override
        public void run() {

            double flechePos = CaptorValuesSingleton.getAngleFleche();
            TextView zero = findViewById(R.id.textViewCallibFlecheZero);
            TextView cent = findViewById(R.id.textViewCallibFlecheCent);
            zero.setText(formatDouble(flechePos));
            cent.setText(formatDouble(flechePos));
            flecheZero = flechePos;
            flecheCent = flechePos;

            myHandler.postDelayed(this, 500);
        }
    };
}
