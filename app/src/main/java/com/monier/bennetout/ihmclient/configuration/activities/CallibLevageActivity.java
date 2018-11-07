package com.monier.bennetout.ihmclient.configuration.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.monier.bennetout.ihmclient.CaptorValuesSingleton;
import com.monier.bennetout.ihmclient.R;
import com.monier.bennetout.ihmclient.communication.Lvl2ClientSocket;
import com.monier.bennetout.ihmclient.configuration.ConfigManager;

import mehdi.sakout.fancybuttons.FancyButton;

import static com.monier.bennetout.ihmclient.utils.Utils.formatDouble;

public class CallibLevageActivity extends Activity {

    private double levageZero = 0, levageCent = 100;

    private Handler myHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.callib_levage);

        btnCallibLevageZeroInit();
        btnCallibLevageCentInit();
        myHandler = new Handler();
        myHandler.postDelayed(majIhm, 0);
    }

    private void btnCallibLevageCentInit() {
        FancyButton fancyButton = findViewById(R.id.btnCallibLevageCent);
        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfigManager.model.LEVAGE_CALLIB_CENT = levageCent;
                ConfigManager.model2ConfigFile(getApplicationContext());
            }
        });
    }

    private void btnCallibLevageZeroInit() {
        FancyButton fancyButton = findViewById(R.id.btnCallibLevageZero);
        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfigManager.model.LEVAGE_CALLIB_ZERO = levageZero;
                ConfigManager.model2ConfigFile(getApplicationContext());
            }
        });
    }

    private Runnable majIhm = new Runnable() {
        @Override
        public void run() {

            double levagePos = CaptorValuesSingleton.getAngleLevage();
            TextView zero = findViewById(R.id.textViewCallibLevageZero);
            TextView cent = findViewById(R.id.textViewCallibLevageCent);
            zero.setText(formatDouble(levagePos));
            cent.setText(formatDouble(levagePos));
            levageZero = levagePos;
            levageCent = levagePos;

            myHandler.postDelayed(this, 500);
        }
    };
}
