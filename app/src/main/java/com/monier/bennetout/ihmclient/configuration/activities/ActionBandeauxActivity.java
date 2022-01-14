package com.monier.bennetout.ihmclient.configuration.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.monier.bennetout.ihmclient.MyCustomHolder;
import com.monier.bennetout.ihmclient.MyListViewAdapter;
import com.monier.bennetout.ihmclient.R;
import com.monier.bennetout.ihmclient.configuration.ConfigManager;

import java.text.NumberFormat;
import java.util.ArrayList;

public class ActionBandeauxActivity extends Activity {

    private MyListViewAdapter myListViewAdapterPorte;
    private MyListViewAdapter myListViewAdapterLevage;
    private MyListViewAdapter myListViewAdapterFleche;
    private MyListViewAdapter myListViewAdapterTamis;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.action_bandeaux);

        listViewPorteInit();
        buttonActionPortePlusInit();

        listViewFlecheInit();
        buttonActionFlechePlusInit();

        listViewLevageInit();
        buttonActionLevagePlusInit();

        listViewTamisInit();
        buttonActionTamisPlusInit();

        btnActionBandeauxSaveInit();
    }

    private void btnActionBandeauxSaveInit() {
        findViewById(R.id.btnActionBandeauxSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfigManager.model.PORTE_CONFIGS = myListViewAdapterPorte.getAllValues();
                ConfigManager.model.LEVAGE_CONFIGS = myListViewAdapterLevage.getAllValues();
                ConfigManager.model.FLECHE_CONFIGS = myListViewAdapterFleche.getAllValues();
                ConfigManager.model.TAMIS_CONFIGS = myListViewAdapterTamis.getAllValues();
                ConfigManager.model2ConfigFile(getApplicationContext());

                Toast.makeText(getApplicationContext(), "Bandeaux enregistrés avec succès", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void buttonActionLevagePlusInit() {
        findViewById(R.id.buttonActionLevagePlus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ActionBandeauxActivity.this);
                alertDialog.setTitle("Ajouter une valeur");
                alertDialog.setMessage("Indiquez la valeur à ajouter :");

                final EditText input = new EditText(ActionBandeauxActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input); // uncomment this line
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int colorId = getResources().getColor(R.color.myGreen);
                        myListViewAdapterLevage.addItem(new MyCustomHolder(Double.parseDouble(input.getText().toString()), false, colorId));
                    }
                });
                alertDialog.setNegativeButton("Annuler", null);
                alertDialog.show();
            }
        });
    }

    private void buttonActionFlechePlusInit() {
        findViewById(R.id.buttonActionFlechePlus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ActionBandeauxActivity.this);
                alertDialog.setTitle("Ajouter une valeur");
                alertDialog.setMessage("Indiquez la valeur à ajouter :");

                final EditText input = new EditText(ActionBandeauxActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input); // uncomment this line
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int colorId = getResources().getColor(R.color.myGreen);
                        myListViewAdapterFleche.addItem(new MyCustomHolder(Double.parseDouble(input.getText().toString()), false, colorId));
                    }
                });
                alertDialog.setNegativeButton("Annuler", null);
                alertDialog.show();
            }
        });
    }

    private void buttonActionPortePlusInit() {
        findViewById(R.id.buttonActionPortePlus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ActionBandeauxActivity.this);
                alertDialog.setTitle("Ajouter une valeur");
                alertDialog.setMessage("Indiquez la valeur à ajouter :");

                final EditText input = new EditText(ActionBandeauxActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input); // uncomment this line
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int colorId = getResources().getColor(R.color.myGreen);
                        myListViewAdapterPorte.addItem(new MyCustomHolder(Double.parseDouble(input.getText().toString()), false, colorId));
                    }
                });
                alertDialog.setNegativeButton("Annuler", null);
                alertDialog.show();
            }
        });
    }

    private void buttonActionTamisPlusInit() {
        findViewById(R.id.buttonActionTamisPlus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ActionBandeauxActivity.this);
                alertDialog.setTitle("Ajouter une valeur");
                alertDialog.setMessage("Indiquez la valeur à ajouter :");

                final EditText input = new EditText(ActionBandeauxActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input); // uncomment this line
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int colorId = getResources().getColor(R.color.myGreen);
                        myListViewAdapterTamis.addItem(new MyCustomHolder(Double.parseDouble(input.getText().toString()), false, colorId));
                    }
                });
                alertDialog.setNegativeButton("Annuler", null);
                alertDialog.show();
            }
        });
    }

    private void listViewLevageInit() {
        RecyclerView mRecyclerView = findViewById(R.id.listViewActionLevage);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<MyCustomHolder> configs = new ArrayList<>();
        double[] userConfig = ConfigManager.model.LEVAGE_CONFIGS;
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(0);

        int colorId = getResources().getColor(R.color.myGreen);
        for (double anUserConfig : userConfig) {
            configs.add(new MyCustomHolder(anUserConfig, false, colorId));
        }

        myListViewAdapterLevage = new MyListViewAdapter(configs, null);
        myListViewAdapterLevage.setCustomClickEnabled(false);
        myListViewAdapterLevage.setCustomLongClickEnabled(true);
        mRecyclerView.setAdapter(myListViewAdapterLevage);
    }

    private void listViewFlecheInit() {
        RecyclerView mRecyclerView = findViewById(R.id.listViewActionFleche);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<MyCustomHolder> configs = new ArrayList<>();
        double[] userConfig = ConfigManager.model.FLECHE_CONFIGS;
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(0);

        int colorId = getResources().getColor(R.color.myGreen);
        for (double anUserConfig : userConfig) {
            configs.add(new MyCustomHolder(anUserConfig, false, colorId));
        }

        myListViewAdapterFleche = new MyListViewAdapter(configs, null);
        myListViewAdapterFleche.setCustomClickEnabled(false);
        myListViewAdapterFleche.setCustomLongClickEnabled(true);
        mRecyclerView.setAdapter(myListViewAdapterFleche);
    }

    private void listViewPorteInit() {
        RecyclerView mRecyclerView = findViewById(R.id.listViewActionPorte);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<MyCustomHolder> configs = new ArrayList<>();
        double[] userConfig = ConfigManager.model.PORTE_CONFIGS;
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(0);

        int colorId = getResources().getColor(R.color.myGreen);
        for (double anUserConfig : userConfig) {
            configs.add(new MyCustomHolder(anUserConfig, false, colorId));
        }

        myListViewAdapterPorte = new MyListViewAdapter(configs, null);
        myListViewAdapterPorte.setCustomClickEnabled(false);
        myListViewAdapterPorte.setCustomLongClickEnabled(true);
        mRecyclerView.setAdapter(myListViewAdapterPorte);
    }

    private void listViewTamisInit() {
        RecyclerView mRecyclerView = findViewById(R.id.listViewActionTamis);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<MyCustomHolder> configs = new ArrayList<>();
        double[] userConfig = ConfigManager.model.TAMIS_CONFIGS;
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(0);

        int colorId = getResources().getColor(R.color.myGreen);
        for (double anUserConfig : userConfig) {
            configs.add(new MyCustomHolder(anUserConfig, false, colorId));
        }

        myListViewAdapterTamis = new MyListViewAdapter(configs, null);
        myListViewAdapterTamis.setCustomClickEnabled(false);
        myListViewAdapterTamis.setCustomLongClickEnabled(true);
        mRecyclerView.setAdapter(myListViewAdapterTamis);
    }
}
