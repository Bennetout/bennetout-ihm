package com.monier.bennetout.ihmclient.configuration.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.monier.bennetout.ihmclient.R;

import mehdi.sakout.fancybuttons.FancyButton;

public class ActionneursActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.actionneurs);
        btnActionBandeauxInit();
        btnTypesBoutonsInit();
    }

    private void btnActionBandeauxInit() {
        final FancyButton fancyButton = findViewById(R.id.btnActionBandeaux);
        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActionBandeauxActivity.class);
                startActivity(intent);
            }
        });
    }

    private void btnTypesBoutonsInit() {
        final FancyButton fancyButton = findViewById(R.id.btnTypesBoutons);
        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TypesBoutonsActivity.class);
                startActivity(intent);
            }
        });
    }
}
