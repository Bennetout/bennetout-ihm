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

public class CallibNiveauActivity extends Activity implements ClientSocket.ClientSocketListener {

    private double niveauZero = 0, niveauCent = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.callib_niveau);

        btnCallibNiveauZeroInit();
        btnCallibNiveauCentInit();
        ClientSocket.addListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ClientSocket.removeListener(this);
    }

    private void btnCallibNiveauCentInit() {
        FancyButton fancyButton = findViewById(R.id.btnCallibNiveauCent);
        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfigManager.model.NIVEAU_CALLIB_CENT = niveauCent;
                ConfigManager.model2ConfigFile(getApplicationContext());
            }
        });
    }

    private void btnCallibNiveauZeroInit() {
        FancyButton fancyButton = findViewById(R.id.btnCallibNiveauZero);
        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfigManager.model.NIVEAU_CALLIB_ZERO = niveauZero;
                ConfigManager.model2ConfigFile(getApplicationContext());
            }
        });
    }

    @Override
    public void onPositionsReceivedFromServer(final double flechePos, double levagePos, double portePos, final double niveauX, double niveauY) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView zero = findViewById(R.id.textViewCallibNiveauZero);
                TextView cent = findViewById(R.id.textViewCallibNiveauCent);
                zero.setText(formatDouble(niveauX));
                cent.setText(formatDouble(niveauX));
                niveauZero = niveauX;
                niveauCent = niveauX;
            }
        });
    }

    @Override
    public void onSocketStatusUpdate(int status) {

    }
}
