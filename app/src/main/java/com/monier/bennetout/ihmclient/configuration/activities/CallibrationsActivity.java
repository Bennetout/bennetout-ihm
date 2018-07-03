package com.monier.bennetout.ihmclient.configuration.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.monier.bennetout.ihmclient.R;

import mehdi.sakout.fancybuttons.FancyButton;

public class CallibrationsActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.callibrations);

        btnCallibFlecheInit();
        btnCallibLevageInit();
        btnCallibPorteInit();
        btnCallibNiveauInit();
    }

    private void btnCallibNiveauInit() {
        FancyButton fancyButton = findViewById(R.id.btnCallibNiveau);
        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CallibNiveauActivity.class);
                startActivity(intent);
            }
        });
    }

    private void btnCallibPorteInit() {
        FancyButton fancyButton = findViewById(R.id.btnCallibPorte);
        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CallibPorteActivity.class);
                startActivity(intent);
            }
        });
    }

    private void btnCallibLevageInit() {
        FancyButton fancyButton = findViewById(R.id.btnCallibLevage);
        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CallibLevageActivity.class);
                startActivity(intent);
            }
        });
    }

    private void btnCallibFlecheInit() {
        FancyButton fancyButton = findViewById(R.id.btnCallibFleche);
        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CallibFlecheActivity.class);
                startActivity(intent);
            }
        });
    }
}
