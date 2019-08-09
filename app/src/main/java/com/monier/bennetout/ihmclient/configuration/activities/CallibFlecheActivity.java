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

    private double flecheZero = 0, flecheCent = 100, flecheZeroActual, flecheCentActual;
    private TextView textViewActualZero, textViewActualCent;

    private Handler myHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.callib_fleche);

        flecheZeroActual = ConfigManager.model.FLECHE_CALLIB_ZERO;
        flecheCentActual = ConfigManager.model.FLECHE_CALLIB_CENT;

        textViewActualZero = findViewById(R.id.textViewCallibFlecheZeroActual);
        textViewActualCent = findViewById(R.id.textViewCallibFlecheCentActual);
        majTextViewActuals();

        btnCallibFlecheZeroInit();
        btnCallibFlecheCentInit();
        btnCallibFlecheSaveInit();

        myHandler = new Handler();
        myHandler.postDelayed(majIhm, 0);
    }

    private void majTextViewActuals() {
        textViewActualZero.setText(formatDouble(flecheZeroActual));
        textViewActualCent.setText(formatDouble(flecheCentActual));
    }

    private void btnCallibFlecheCentInit() {
        FancyButton fancyButton = findViewById(R.id.btnCallibFlecheCent);
        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flecheCentActual = flecheCent;
                majTextViewActuals();
            }
        });
    }

    private void btnCallibFlecheZeroInit() {
        FancyButton fancyButton = findViewById(R.id.btnCallibFlecheZero);
        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flecheZeroActual = flecheZero;
                majTextViewActuals();
            }
        });
    }

    private void btnCallibFlecheSaveInit() {
        FancyButton fancyButton = findViewById(R.id.btnCallibFlecheSave);
        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfigManager.model.FLECHE_CALLIB_ZERO = flecheZeroActual;
                ConfigManager.model.FLECHE_CALLIB_CENT = flecheCentActual;
                ConfigManager.model2ConfigFile(getApplicationContext());

                finish();
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
