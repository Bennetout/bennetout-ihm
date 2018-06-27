package com.monier.bennetout.ihmclient.configuration.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.monier.bennetout.ihmclient.ClientSocket;
import com.monier.bennetout.ihmclient.R;
import com.monier.bennetout.ihmclient.configuration.ConfigManager;

import mehdi.sakout.fancybuttons.FancyButton;

import static com.monier.bennetout.ihmclient.utils.Utils.formatDouble;

public class CallibLevageActivity extends Activity implements ClientSocket.ClientSocketListener {

    private double levageZero = 0, levageCent = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.callib_levage);

        btnCallibLevageZeroInit();
        btnCallibLevageCentInit();
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

    @Override
    public void onPositionsReceivedFromServer(double flechePos, final double levagePos, double portePos, double niveauX, double niveauY) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView zero = findViewById(R.id.textViewCallibLevageZero);
                TextView cent = findViewById(R.id.textViewCallibLevageCent);
                zero.setText(formatDouble(levagePos));
                cent.setText(formatDouble(levagePos));
                levageZero = levagePos;
                levageCent = levagePos;
            }
        });
    }

}
