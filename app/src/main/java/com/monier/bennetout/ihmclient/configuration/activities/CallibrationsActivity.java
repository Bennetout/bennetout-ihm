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
    }

    private void btnCallibFlecheInit() {
        final FancyButton fancyButton = findViewById(R.id.btnCallibFleche);
        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CallibFlecheActivity.class);
                startActivity(intent);
            }
        });
    }
}
