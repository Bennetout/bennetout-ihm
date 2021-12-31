package com.monier.bennetout.ihmclient.configuration.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.monier.bennetout.ihmclient.R;
import com.monier.bennetout.ihmclient.configuration.ConfigManager;

import mehdi.sakout.fancybuttons.FancyButton;

public class TypesBoutonsActivity extends Activity implements View.OnClickListener {

    private double typeBoutonsTapis;
    private double typeBoutonsPorte;
    private double typeBoutonsFleche;
    private double typeBoutonsLevage;

    private FancyButton tapisAutoMaintienButton, tapisImpulsionButton;
    private FancyButton porteAutoMaintienButton, porteImpulsionButton;
    private FancyButton flecheAutoMaintienButton, flecheImpulsionButton;
    private FancyButton levageAutoMaintienButton, levageImpulsionButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.types_boutons);

        tapisAutoMaintienButton = findViewById(R.id.btnTypeBoutonTapisAutoMaintient);
        tapisAutoMaintienButton.setOnClickListener(this);
        tapisImpulsionButton = findViewById(R.id.btnTypeBoutonTapisImpulsion);
        tapisImpulsionButton.setOnClickListener(this);

        porteAutoMaintienButton = findViewById(R.id.btnTypeBoutonPorteAutoMaintient);
        porteAutoMaintienButton.setOnClickListener(this);
        porteImpulsionButton = findViewById(R.id.btnTypeBoutonPorteImpulsion);
        porteImpulsionButton.setOnClickListener(this);

        flecheAutoMaintienButton = findViewById(R.id.btnTypeBoutonFlecheAutoMaintient);
        flecheAutoMaintienButton.setOnClickListener(this);
        flecheImpulsionButton = findViewById(R.id.btnTypeBoutonFlecheImpulsion);
        flecheImpulsionButton.setOnClickListener(this);

        levageAutoMaintienButton = findViewById(R.id.btnTypeBoutonLevageAutoMaintient);
        levageAutoMaintienButton.setOnClickListener(this);
        levageImpulsionButton = findViewById(R.id.btnTypeBoutonLevageImpulsion);
        levageImpulsionButton.setOnClickListener(this);

        majButtonsFromConfig();
    }

    private void majButtonsFromConfig() {
        typeBoutonsTapis = ConfigManager.model.TYPE_BOUTON_TAPIS;
        typeBoutonsPorte = ConfigManager.model.TYPE_BOUTON_PORTE;
        typeBoutonsFleche = ConfigManager.model.TYPE_BOUTON_FLECHE;
        typeBoutonsLevage = ConfigManager.model.TYPE_BOUTON_LEVAGE;

        if (typeBoutonsTapis == ConfigManager.TYPE_BOUTON_AUTOMAINTIEN) {
            tapisAutoMaintienButton.setBackgroundColor(getResources().getColor(R.color.myGreen));
            tapisImpulsionButton.setBackgroundColor(getResources().getColor(R.color.myGray));
        } else {
            tapisAutoMaintienButton.setBackgroundColor(getResources().getColor(R.color.myGray));
            tapisImpulsionButton.setBackgroundColor(getResources().getColor(R.color.myGreen));
        }

        if (typeBoutonsPorte == ConfigManager.TYPE_BOUTON_AUTOMAINTIEN) {
            porteAutoMaintienButton.setBackgroundColor(getResources().getColor(R.color.myGreen));
            porteImpulsionButton.setBackgroundColor(getResources().getColor(R.color.myGray));
        } else {
            porteAutoMaintienButton.setBackgroundColor(getResources().getColor(R.color.myGray));
            porteImpulsionButton.setBackgroundColor(getResources().getColor(R.color.myGreen));
        }

        if (typeBoutonsFleche == ConfigManager.TYPE_BOUTON_AUTOMAINTIEN) {
            flecheAutoMaintienButton.setBackgroundColor(getResources().getColor(R.color.myGreen));
            flecheImpulsionButton.setBackgroundColor(getResources().getColor(R.color.myGray));
        } else {
            flecheAutoMaintienButton.setBackgroundColor(getResources().getColor(R.color.myGray));
            flecheImpulsionButton.setBackgroundColor(getResources().getColor(R.color.myGreen));
        }

        if (typeBoutonsLevage == ConfigManager.TYPE_BOUTON_AUTOMAINTIEN) {
            levageAutoMaintienButton.setBackgroundColor(getResources().getColor(R.color.myGreen));
            levageImpulsionButton.setBackgroundColor(getResources().getColor(R.color.myGray));
        } else {
            levageAutoMaintienButton.setBackgroundColor(getResources().getColor(R.color.myGray));
            levageImpulsionButton.setBackgroundColor(getResources().getColor(R.color.myGreen));
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnTypeBoutonTapisAutoMaintient) {
            if (typeBoutonsTapis == ConfigManager.TYPE_BOUTON_AUTOMAINTIEN)
                return;

            typeBoutonsTapis = ConfigManager.TYPE_BOUTON_AUTOMAINTIEN;
            ConfigManager.model.TYPE_BOUTON_TAPIS = typeBoutonsTapis;
            ConfigManager.model2ConfigFile(getApplicationContext());
        }

        if (view.getId() == R.id.btnTypeBoutonTapisImpulsion) {
            if (typeBoutonsTapis == ConfigManager.TYPE_BOUTON_IMPULSION)
                return;

            typeBoutonsTapis = ConfigManager.TYPE_BOUTON_IMPULSION;
            ConfigManager.model.TYPE_BOUTON_TAPIS = typeBoutonsTapis;
            ConfigManager.model2ConfigFile(getApplicationContext());
        }

        if (view.getId() == R.id.btnTypeBoutonPorteAutoMaintient) {
            if (typeBoutonsPorte == ConfigManager.TYPE_BOUTON_AUTOMAINTIEN)
                return;

            typeBoutonsPorte = ConfigManager.TYPE_BOUTON_AUTOMAINTIEN;
            ConfigManager.model.TYPE_BOUTON_PORTE = typeBoutonsPorte;
            ConfigManager.model2ConfigFile(getApplicationContext());
        }

        if (view.getId() == R.id.btnTypeBoutonPorteImpulsion) {
            if (typeBoutonsPorte == ConfigManager.TYPE_BOUTON_IMPULSION)
                return;

            typeBoutonsPorte = ConfigManager.TYPE_BOUTON_IMPULSION;
            ConfigManager.model.TYPE_BOUTON_PORTE = typeBoutonsPorte;
            ConfigManager.model2ConfigFile(getApplicationContext());
        }

        if (view.getId() == R.id.btnTypeBoutonFlecheAutoMaintient) {
            if (typeBoutonsFleche == ConfigManager.TYPE_BOUTON_AUTOMAINTIEN)
                return;

            typeBoutonsFleche = ConfigManager.TYPE_BOUTON_AUTOMAINTIEN;
            ConfigManager.model.TYPE_BOUTON_FLECHE = typeBoutonsFleche;
            ConfigManager.model2ConfigFile(getApplicationContext());
        }

        if (view.getId() == R.id.btnTypeBoutonFlecheImpulsion) {
            if (typeBoutonsFleche == ConfigManager.TYPE_BOUTON_IMPULSION)
                return;

            typeBoutonsFleche = ConfigManager.TYPE_BOUTON_IMPULSION;
            ConfigManager.model.TYPE_BOUTON_FLECHE = typeBoutonsFleche;
            ConfigManager.model2ConfigFile(getApplicationContext());
        }

        if (view.getId() == R.id.btnTypeBoutonLevageAutoMaintient) {
            if (typeBoutonsLevage == ConfigManager.TYPE_BOUTON_AUTOMAINTIEN)
                return;

            typeBoutonsLevage = ConfigManager.TYPE_BOUTON_AUTOMAINTIEN;
            ConfigManager.model.TYPE_BOUTON_LEVAGE = typeBoutonsLevage;
            ConfigManager.model2ConfigFile(getApplicationContext());
        }

        if (view.getId() == R.id.btnTypeBoutonLevageImpulsion) {
            if (typeBoutonsLevage == ConfigManager.TYPE_BOUTON_IMPULSION)
                return;

            typeBoutonsLevage = ConfigManager.TYPE_BOUTON_IMPULSION;
            ConfigManager.model.TYPE_BOUTON_LEVAGE = typeBoutonsLevage;
            ConfigManager.model2ConfigFile(getApplicationContext());
        }

        majButtonsFromConfig();
    }
}
