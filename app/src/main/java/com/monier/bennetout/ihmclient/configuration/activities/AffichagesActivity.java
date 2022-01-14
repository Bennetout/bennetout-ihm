package com.monier.bennetout.ihmclient.configuration.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.monier.bennetout.ihmclient.R;
import com.monier.bennetout.ihmclient.configuration.ConfigManager;

import mehdi.sakout.fancybuttons.FancyButton;

public class AffichagesActivity extends Activity {

    private boolean withDrawingValue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.affichage);

        FancyButton withDrawingBtn = findViewById(R.id.btnWithDrawingYesNo);

        withDrawingValue = (ConfigManager.model.SHOW_DRAWING > 0);
        setBtnOnOff(withDrawingBtn, withDrawingValue);

        withDrawingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                withDrawingValue = !withDrawingValue;
                setBtnOnOff(withDrawingBtn, withDrawingValue);
                if (withDrawingValue) {
                    ConfigManager.model.SHOW_DRAWING = 1;
                } else {
                    ConfigManager.model.SHOW_DRAWING = 0;
                }
                ConfigManager.model2ConfigFile(getApplicationContext());
            }
        });
    }

    private void setBtnOnOff(FancyButton button, boolean state) {
        if (state) {
            button.setBackgroundColor(getResources().getColor(R.color.myGreen));
            button.setText(getResources().getString(R.string.oui));
        } else {
            button.setBackgroundColor(getResources().getColor(R.color.myGray));
            button.setText(getResources().getString(R.string.non));
        }
    }
}
