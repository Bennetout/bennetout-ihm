package com.monier.bennetout.ihmclient.configuration.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.monier.bennetout.ihmclient.MainActivity;
import com.monier.bennetout.ihmclient.R;

import mehdi.sakout.fancybuttons.FancyButton;

public class ConfigActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.configurations);
        btnCallibrationsInit();
        btnActionneursInit();
        btnAffichageInit();
        btnParamsInit();
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

        super.onDestroy();
    }

    private void btnCallibrationsInit() {
        final FancyButton fancyButton = findViewById(R.id.btnCallibrations);
        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CallibrationsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void btnActionneursInit() {
        final FancyButton fancyButton = findViewById(R.id.btnActionneurs);
        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActionneursActivity.class);
                startActivity(intent);
            }
        });
    }

    private void btnAffichageInit() {
        final FancyButton fancyButton = findViewById(R.id.btnAffichages);
        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AffichagesActivity.class);
                startActivity(intent);
            }
        });
    }

    private void btnParamsInit() {
        final FancyButton fancyButton = findViewById(R.id.btnParams);
        fancyButton.setVisibility(View.INVISIBLE);
        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ParamsActivity.class);
                startActivity(intent);
            }
        });
    }
}
