package com.monier.bennetout.ihmclient.configuration.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.monier.bennetout.ihmclient.ClientSocket;
import com.monier.bennetout.ihmclient.R;
import com.monier.bennetout.ihmclient.configuration.ConfigManager;

import mehdi.sakout.fancybuttons.FancyButton;

import static com.monier.bennetout.ihmclient.utils.Utils.formatDouble;

public class CallibFlecheActivity extends Activity implements ClientSocket.ClientSocketListener {

    private double flecheZero = 0, flecheCent = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.callib_fleche);

        btnCallibFlecheZeroInit();
        btnCallibFlecheCentInit();
        ClientSocket.addListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ClientSocket.removeListener(this);
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

    @Override
    public void onPositionsReceivedFromServer(final double flechePos, double levagePos, double portePos, double niveauX, double niveauY) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView zero = findViewById(R.id.textViewCallibFlecheZero);
                TextView cent = findViewById(R.id.textViewCallibFlecheCent);
                zero.setText(formatDouble(flechePos));
                cent.setText(formatDouble(flechePos));
                flecheZero = flechePos;
                flecheCent = flechePos;
            }
        });
    }
}
