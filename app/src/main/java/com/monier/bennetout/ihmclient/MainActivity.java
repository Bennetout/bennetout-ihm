package com.monier.bennetout.ihmclient;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.monier.bennetout.ihmclient.communication.Lvl2ClientSocket;
import com.monier.bennetout.ihmclient.communication.ProtocolConstants;
import com.monier.bennetout.ihmclient.configuration.ConfigManager;
import com.monier.bennetout.ihmclient.configuration.activities.ConfigActivity;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;

import mehdi.sakout.fancybuttons.FancyButton;

import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.ARG_ACTION_FLECHE_OFF;
import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.ARG_ACTION_FLECHE_ON;
import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.ARG_ACTION_LEVAGE_OFF;
import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.ARG_ACTION_LEVAGE_ON;
import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.ARG_ACTION_PORTE_OFF;
import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.ARG_ACTION_PORTE_ON;
import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.ARG_ACTION_TAMIS_OFF;
import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.ARG_ACTION_TAMIS_ON;
import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.ARG_ACTION_TAPIS_OFF;
import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.ARG_ACTION_TAPIS_ON;
import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.ARG_FLECHE;
import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.ARG_LEVAGE;
import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.ARG_PORTE;
import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.ARG_STATE_HIGH;
import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.ARG_STATE_LOW;
import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.ARG_TAMIS;
import static com.monier.bennetout.ihmclient.utils.Utils.formatDouble;

public class MainActivity extends Activity implements Lvl2ClientSocket.SocketClientListener {

    private static final String VERSION = "V4";

    private static final String TAG = MainActivity.class.getCanonicalName();
    private static final int PORTE      = 0;
    private static final int FLECHE     = 1;
    private static final int LEVAGE     = 2;
    private static final int TAMIS      = 3;

    private final String[] permissionsNeeded = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private double angleFleche = 0, angleLevage = 0, anglePorte = 0;
    private double niveau = 0;
    private double angleTamis = 0;
    Lvl2ClientSocket myLvl2ClientSocket = new Lvl2ClientSocket("10.3.141.1", 65000);

    private TextView textViewFleche;
    private TextView textViewLevage;
    private TextView textViewPorte;
    private TextView textViewTamis;

    private MyListViewAdapter myListViewAdapterPorte;
    private MyListViewAdapter myListViewAdapterLevage;
    private MyListViewAdapter myListViewAdapterFleche;
    private MyListViewAdapter myListViewAdapterTamis;

    private RemorquePainter myRemorquePainter;
    private FlechePainter myFlechePainter;
    private NiveauPainter myNiveauPainter;

    private Handler myHandler;
    private final GestionActionneur[] gestionActionneur = new GestionActionneur[4];

    private FancyButton tapisMarcheButton;
    private FancyButton tapisArretButton;
    private FancyButton porteMarcheButton;
    private FancyButton porteArretButton;
    private FancyButton flecheMarcheButton;
    private FancyButton flecheArretButton;
    private FancyButton levageMarcheButton;
    private FancyButton levageArretButton;
    private FancyButton tamisMarcheButton;
    private FancyButton tamisArretButton;

    private double angleFlecheRounded = 0;
    private double angleLevageRounded = 0;
    private double anglePorteRounded = 0;
    private double niveauRounded = 0;
    private double angleTamisRounded = 0;

    private boolean withDrawing;
    private String serverVersion = "??";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        grantPermissions(permissionsNeeded);

        while (!isPermissionsGranted(permissionsNeeded)) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // On charge la configuration sur chaque changement d'activité pour appliquer
        // les modifications utilisateurs si besoin
        ConfigManager.initConfig(this);

        myLvl2ClientSocket.setListener(this);

        if (ConfigManager.model.SHOW_DRAWING > 0) {
            setContentView(R.layout.main);
            withDrawing = true;
            myRemorquePainter = findViewById(R.id.remorqueView);
            myFlechePainter = findViewById(R.id.flecheView);
            myNiveauPainter = findViewById(R.id.niveauView);
        } else {
            setContentView(R.layout.main_without_drawing);
            withDrawing = false;
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Get a random id into the main View
        View randomView = findViewById(R.id.btnRefresh);
        View root = randomView.getRootView();
        root.setBackgroundColor(Color.WHITE);

        majVersions();

        textViewFleche = findViewById(R.id.textViewFlecheValue);
        textViewLevage = findViewById(R.id.textViewLevageValue);
        textViewPorte = findViewById(R.id.textViewPorteValue);
        textViewTamis = findViewById(R.id.textViewTamisValue);

        btnReglageInit();
        btnRefreshInit();

        textViewsInit();

        myHandler = new Handler();
    }

    private class myTextViewOnLongClickListener implements View.OnLongClickListener {

        TextView textView;
        Thread specificFunc;

        public myTextViewOnLongClickListener(TextView textView, Thread specificFunc) {
            this.textView = textView;
            this.specificFunc = specificFunc;
        }

        @Override
        public boolean onLongClick(View view) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle("Titre à afficher");
            final EditText input = new EditText(MainActivity.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
            alertDialog.setView(input); // uncomment this line
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String newTitle = input.getText().toString();
                    if (newTitle.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Erreur, titre invalide", Toast.LENGTH_LONG).show();
                        return;
                    }
                    textView.setText(newTitle + " :");
                    specificFunc.start();
                }
            });
            alertDialog.show();
            return false;
        }
    }

    private void textViewsInit() {
        TextView textViewTapis = findViewById(R.id.textViewTapis);
        textViewTapis.setText(ConfigManager.model.TEXT_TAPIS);
        textViewTapis.setOnLongClickListener(new myTextViewOnLongClickListener(textViewTapis, new Thread(() -> {
            ConfigManager.model.TEXT_TAPIS = textViewTapis.getText().toString();
            ConfigManager.model2ConfigFile(getApplicationContext());
        })));

        TextView textViewPorte = findViewById(R.id.textViewPorte);
        textViewPorte.setText(ConfigManager.model.TEXT_PORTE);
        textViewPorte.setOnLongClickListener(new myTextViewOnLongClickListener(textViewPorte, new Thread(() -> {
            ConfigManager.model.TEXT_PORTE = textViewPorte.getText().toString();
            ConfigManager.model2ConfigFile(getApplicationContext());
        })));

        TextView textViewFleche = findViewById(R.id.textViewFleche);
        textViewFleche.setText(ConfigManager.model.TEXT_FLECHE);
        textViewFleche.setOnLongClickListener(new myTextViewOnLongClickListener(textViewFleche, new Thread(() -> {
            ConfigManager.model.TEXT_FLECHE = textViewFleche.getText().toString();
            ConfigManager.model2ConfigFile(getApplicationContext());
        })));

        TextView textViewLevage = findViewById(R.id.textViewLevage);
        textViewLevage.setText(ConfigManager.model.TEXT_LEVAGE);
        textViewLevage.setOnLongClickListener(new myTextViewOnLongClickListener(textViewLevage, new Thread(() -> {
            ConfigManager.model.TEXT_LEVAGE = textViewLevage.getText().toString();
            ConfigManager.model2ConfigFile(getApplicationContext());
        })));

        TextView textViewTamis = findViewById(R.id.textViewTamis);
        textViewTamis.setText(ConfigManager.model.TEXT_TAMIS);
        textViewTamis.setOnLongClickListener(new myTextViewOnLongClickListener(textViewTamis, new Thread(() -> {
            ConfigManager.model.TEXT_TAMIS = textViewTamis.getText().toString();
            ConfigManager.model2ConfigFile(getApplicationContext());
        })));
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        Log.e(TAG, "code touche up = " + keyCode);

        // Obtain MotionEvent object
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis() + 100;
        float x = 0.0f;
        float y = 0.0f;
        int metaState = 0;
        MotionEvent motionEvent = MotionEvent.obtain(
                downTime,
                eventTime,
                MotionEvent.ACTION_UP,
                x,
                y,
                metaState
        );

        switch (keyCode) {
            case KeyEvent.KEYCODE_MEDIA_NEXT:
                flecheArretButton.dispatchTouchEvent(motionEvent);
                break;

            case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                flecheMarcheButton.dispatchTouchEvent(motionEvent);
                break;

            case KeyEvent.KEYCODE_VOLUME_UP:
                tamisMarcheButton.dispatchTouchEvent(motionEvent);
                return true;

            case KeyEvent.KEYCODE_VOLUME_DOWN:
                tamisArretButton.dispatchTouchEvent(motionEvent);
                return true;
        }

        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        Log.e(TAG, "code touche down = " + keyCode);

        // Obtain MotionEvent object
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis() + 100;
        float x = 0.0f;
        float y = 0.0f;
        int metaState = 0;
        MotionEvent motionEvent = MotionEvent.obtain(
                downTime,
                eventTime,
                MotionEvent.ACTION_DOWN,
                x,
                y,
                metaState
        );

        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                tamisMarcheButton.dispatchTouchEvent(motionEvent);
                return true;

            case KeyEvent.KEYCODE_VOLUME_DOWN:
                tamisArretButton.dispatchTouchEvent(motionEvent);
                return true;

            case KeyEvent.KEYCODE_MEDIA_NEXT:
                // Flèche droite
                flecheArretButton.dispatchTouchEvent(motionEvent);
                return true;

            case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                // Flèche gauche
                flecheMarcheButton.dispatchTouchEvent(motionEvent);
                return true;

            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                // Première valeur bandeau flèche
                myListViewAdapterFleche.switchActiveItem(0);
                return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();

        listViewPorteInit();
        listViewFlecheInit();
        listViewLevageInit();
        listViewTamisInit();

        btnsActuatorsInit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    private boolean isPermissionsGranted(String... permissions) {

        for (String permission: permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }

    private void grantPermissions(String... permissions) {

        final int PERMISSIONS_SIMPLE_REQUEST      = 1;
        final int PERMISSIONS_MULTIPLE_REQUEST    = 123;

        if (permissions == null)
            return;

        ArrayList<String> notGranted = new ArrayList<>();

        for (String permission: permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                notGranted.add(permission);
            }
        }

        if (notGranted.size() > 0) {
            String[] permissionsToRequest = new String[notGranted.size()];
            int index = 0;
            for (String permission: notGranted) {
                permissionsToRequest[index] = permission;
                index++;
            }

            if (permissionsToRequest.length > 1)
                ActivityCompat.requestPermissions(this, permissionsToRequest, PERMISSIONS_MULTIPLE_REQUEST);
            else
                ActivityCompat.requestPermissions(this, permissionsToRequest, PERMISSIONS_SIMPLE_REQUEST);
        }
    }

    private void btnsActuatorsInit() {
        double typeBoutonsTapis = ConfigManager.model.TYPE_BOUTON_TAPIS;
        double typeBoutonsPorte = ConfigManager.model.TYPE_BOUTON_PORTE;
        double typeBoutonsFleche = ConfigManager.model.TYPE_BOUTON_FLECHE;
        double typeBoutonsLevage = ConfigManager.model.TYPE_BOUTON_LEVAGE;
        double typeBoutonsTamis = ConfigManager.model.TYPE_BOUTON_TAMIS;

        //Tapis
        tapisMarcheButton = findViewById(R.id.buttonTapisMarche);
        tapisArretButton = findViewById(R.id.buttonTapisArret);

        new ActuatorStateManager(typeBoutonsTapis, myLvl2ClientSocket, ARG_ACTION_TAPIS_ON, ARG_ACTION_TAPIS_OFF,
                tapisMarcheButton, tapisArretButton, getResources().getColor(R.color.myOrange), null);

        // Porte
        porteMarcheButton = findViewById(R.id.buttonPorteMarche);
        porteArretButton = findViewById(R.id.buttonPorteArret);

        new ActuatorStateManager(typeBoutonsPorte, myLvl2ClientSocket, ARG_ACTION_PORTE_ON, ARG_ACTION_PORTE_OFF,
                porteMarcheButton, porteArretButton, getResources().getColor(R.color.myOrange), null);

        // Flèche
        flecheMarcheButton = findViewById(R.id.buttonFlecheMarche);
        flecheArretButton = findViewById(R.id.buttonFlecheArret);

        new ActuatorStateManager(typeBoutonsFleche, myLvl2ClientSocket, ARG_ACTION_FLECHE_ON, ARG_ACTION_FLECHE_OFF,
                flecheMarcheButton, flecheArretButton, getResources().getColor(R.color.myOrange), null);

        // Levage
        levageMarcheButton = findViewById(R.id.buttonLevageMarche);
        levageArretButton = findViewById(R.id.buttonLevageArret);

        new ActuatorStateManager(typeBoutonsLevage, myLvl2ClientSocket, ARG_ACTION_LEVAGE_ON, ARG_ACTION_LEVAGE_OFF,
                levageMarcheButton, levageArretButton, getResources().getColor(R.color.myOrange), null);

        // Tamis
        tamisMarcheButton = findViewById(R.id.buttonTamisMarche);
        tamisArretButton = findViewById(R.id.buttonTamisArret);

        new ActuatorStateManager(typeBoutonsTamis, myLvl2ClientSocket, ARG_ACTION_TAMIS_ON, ARG_ACTION_TAMIS_OFF,
                tamisMarcheButton, tamisArretButton, getResources().getColor(R.color.myOrange), myListViewAdapterTamis);
    }

    private void listViewLevageInit() {
        RecyclerView mRecyclerView = findViewById(R.id.listViewLevage);

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

        myListViewAdapterLevage = new MyListViewAdapter(configs,
                new MyListViewAdapter.MyListViewListener() {
                    @Override
                    public void onNewPositionClicked(boolean state, final double value) {

                        if (gestionActionneur[LEVAGE] != null)
                            gestionActionneur[LEVAGE].stopAll();

                        if (!state) {
                            return;
                        }

                        gestionActionneur[LEVAGE] = new GestionActionneur(ARG_LEVAGE, value);
                        gestionActionneur[LEVAGE].start();
                    }
                });
        mRecyclerView.setAdapter(myListViewAdapterLevage);
    }

    private void listViewFlecheInit() {
        RecyclerView mRecyclerView = findViewById(R.id.listViewFleche);

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

        myListViewAdapterFleche = new MyListViewAdapter(configs,
                new MyListViewAdapter.MyListViewListener() {
                    @Override
                    public void onNewPositionClicked(boolean state, double value) {
                        if (gestionActionneur[FLECHE] != null)
                            gestionActionneur[FLECHE].stopAll();

                        if (!state) {
                            return;
                        }

                        gestionActionneur[FLECHE] = new GestionActionneur(ARG_FLECHE, value);
                        gestionActionneur[FLECHE].start();
                    }
                });
        mRecyclerView.setAdapter(myListViewAdapterFleche);
    }

    private void listViewPorteInit() {
        RecyclerView mRecyclerView = findViewById(R.id.listViewPorte);

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

        myListViewAdapterPorte = new MyListViewAdapter(configs,
                new MyListViewAdapter.MyListViewListener() {
                    @Override
                    public void onNewPositionClicked(boolean state, double value) {
                        if (gestionActionneur[PORTE] != null)
                            gestionActionneur[PORTE].stopAll();

                        if (!state) {
                            return;
                        }

                        gestionActionneur[PORTE] = new GestionActionneur(ARG_PORTE, value);
                        gestionActionneur[PORTE].start();
                    }
                });
        mRecyclerView.setAdapter(myListViewAdapterPorte);
    }

    private void listViewTamisInit() {
        RecyclerView mRecyclerView = findViewById(R.id.listViewTamis);

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

        myListViewAdapterTamis = new MyListViewAdapter(configs,
                new MyListViewAdapter.MyListViewListener() {
                    @Override
                    public void onNewPositionClicked(boolean state, double value) {
                        if (gestionActionneur[TAMIS] != null)
                            gestionActionneur[TAMIS].stopAll();

                        if (!state) {
                            return;
                        }

                        gestionActionneur[TAMIS] = new GestionActionneur(ARG_TAMIS, value);
                        gestionActionneur[TAMIS].start();
                    }
                });
        mRecyclerView.setAdapter(myListViewAdapterTamis);
    }

    private void btnRefreshInit() {
        final FancyButton fancyButton = findViewById(R.id.btnRefresh);
        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            myLvl2ClientSocket.deconnect();
//                            clientSocket.connect("10.42.0.1");
                            myLvl2ClientSocket.connect();
                            myHandler.postDelayed(majIhm, 0);
                            myLvl2ClientSocket.getServerVersion();
                            myLvl2ClientSocket.getSensorsValues();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
    }

    private void btnReglageInit() {
        final FancyButton fancyButton = findViewById(R.id.btnReglage);
        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ConfigActivity.class);
                startActivity(intent);
            }
        });
    }

    public static double calculPosLevage(double angleInitial) {
        double levageCallibZero = ConfigManager.model.LEVAGE_CALLIB_ZERO;
        double levageCallibCent = ConfigManager.model.LEVAGE_CALLIB_CENT;

        double droiteXa = levageCallibZero;
        double droiteXb = levageCallibCent;
        double droiteYa = 0;
        double droiteYb = 100;
        double resultat;

        double droiteX = angleInitial;

        double droiteA = (droiteYb - droiteYa) / (droiteXb - droiteXa);
        double droiteB = - (droiteA * droiteXa);

        double droiteY = droiteA * droiteX + droiteB;

        resultat = droiteY *((ConfigManager.model.BORNE_MAX_LEVAGE - ConfigManager.model.BORNE_MIN_LEVAGE)*0.01);

        if (resultat > ConfigManager.model.BORNE_MAX_LEVAGE)
            return ConfigManager.model.BORNE_MAX_LEVAGE;

        if (resultat < ConfigManager.model.BORNE_MIN_LEVAGE)
            return ConfigManager.model.BORNE_MIN_LEVAGE;

        return resultat;
    }

    public static double calculPosNiveau(double angleInitial) {
        double niveauCallibZero = ConfigManager.model.NIVEAU_CALLIB_ZERO;

        return angleInitial - niveauCallibZero;
    }

    public static double calculPosPorte(double angleInitial) {
        double porteCallibZero = ConfigManager.model.PORTE_CALLIB_ZERO;
        double porteCallibCent = ConfigManager.model.PORTE_CALLIB_CENT;

        double droiteXa = porteCallibZero;
        double droiteXb = porteCallibCent;
        double droiteYa = 0;
        double droiteYb = 100;
        double resultat;

        double droiteX = angleInitial;

        double droiteA = (droiteYb - droiteYa) / (droiteXb - droiteXa);
        double droiteB = - (droiteA * droiteXa);

        double droiteY = droiteA * droiteX + droiteB;

        resultat = droiteY *((ConfigManager.model.BORNE_MAX_PORTE - ConfigManager.model.BORNE_MIN_PORTE)*0.01);

        if (resultat > ConfigManager.model.BORNE_MAX_PORTE)
            return ConfigManager.model.BORNE_MAX_PORTE;

        if (resultat < ConfigManager.model.BORNE_MIN_PORTE)
            return ConfigManager.model.BORNE_MIN_PORTE;

        return resultat;
    }

    public static double calculPosFleche(double angleInitial) {
        double flecheCallibZero = ConfigManager.model.FLECHE_CALLIB_ZERO;
        double resultat;

        resultat = angleInitial - flecheCallibZero;

        if (resultat > ConfigManager.model.BORNE_MAX_FLECHE)
            return ConfigManager.model.BORNE_MAX_FLECHE;

        if (resultat < ConfigManager.model.BORNE_MIN_FLECHE)
            return ConfigManager.model.BORNE_MIN_FLECHE;

        return resultat;
    }

    public static double calculPosTamis(double angleInitial) {
        double tamisCallibZero = ConfigManager.model.TAMIS_CALLIB_ZERO;
        double tamisCallibCent = ConfigManager.model.TAMIS_CALLIB_CENT;

        double droiteXa = tamisCallibZero;
        double droiteXb = tamisCallibCent;
        double droiteYa = 0;
        double droiteYb = 100;
        double resultat;

        double droiteX = angleInitial;

        double droiteA = (droiteYb - droiteYa) / (droiteXb - droiteXa);
        double droiteB = - (droiteA * droiteXa);

        double droiteY = droiteA * droiteX + droiteB;

        resultat = droiteY *((ConfigManager.model.BORNE_MAX_TAMIS - ConfigManager.model.BORNE_MIN_TAMIS)*0.01);

        if (resultat > ConfigManager.model.BORNE_MAX_TAMIS)
            return ConfigManager.model.BORNE_MAX_TAMIS;

        if (resultat < ConfigManager.model.BORNE_MIN_TAMIS)
            return ConfigManager.model.BORNE_MIN_TAMIS;

        return resultat;
    }

//    public static double calculPosFleche(double angleInitial) {
//        double flecheCallibZero = ConfigManager.model.FLECHE_CALLIB_ZERO;
//        double flecheCallibCent = ConfigManager.model.FLECHE_CALLIB_CENT;
//
//        double droiteXa = flecheCallibZero;
//        double droiteXb = flecheCallibCent;
//        double droiteYa = 0;
//        double droiteYb = 100;
//
//        double droiteX = angleInitial;
//
//        double droiteA = (droiteYb - droiteYa) / (droiteXb - droiteXa);
//        double droiteB = - (droiteA * droiteXa);
//
//        double droiteY = droiteA * droiteX + droiteB;
//
//        return droiteY * (ConfigManager.model.BORNE_MAX_FLECHE - ConfigManager.model.BORNE_MIN_FLECHE)*0.01 - ConfigManager.model.BORNE_MAX_FLECHE;
////        return droiteY*0.9 -45;
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    private Runnable majIhm = new Runnable() {

        private double angleFlecheRound = 0;
        private double angleLevageRound = 0;
        private double anglePorteRound = 0;
        private double niveauRound = 0;
        private double angleTamisRound = 0;

        private int indexRound = 0;

        @Override
        public void run() {

            double nbRound = 4;
            if (indexRound == nbRound) {

                // Affichage du devers
                niveauRounded = niveauRound/ nbRound;
                if (withDrawing)
                    myNiveauPainter.setNiveau(calculPosNiveau(niveauRounded));

                // Affichage de la position du tamis
                angleTamisRounded = calculPosTamis(angleTamisRound/ nbRound);
                textViewTamis.setText(formatDouble(angleTamisRounded));

                // Affichage de l'angle de la flèche
                angleFlecheRounded = calculPosFleche(angleFlecheRound/ nbRound);
                textViewFleche.setText(formatDouble(angleFlecheRounded));
                if (withDrawing)
                    myFlechePainter.setAngle(angleFlecheRounded);

                // Affichage des angles levage + porte
                angleLevageRounded = calculPosLevage(angleLevageRound/ nbRound);
                anglePorteRounded = calculPosPorte(anglePorteRound/ nbRound);
                if (withDrawing)
                    myRemorquePainter.setAngle(angleLevageRounded, anglePorteRounded);
                textViewLevage.setText(formatDouble(angleLevageRounded));
                textViewPorte.setText(formatDouble(anglePorteRounded));

                angleFlecheRound = 0;
                angleLevageRound = 0;
                anglePorteRound = 0;
                niveauRound = 0;
                angleTamisRound = 0;
                indexRound = 0;
            } else {
                angleFlecheRound += angleFleche;
                angleLevageRound += angleLevage;
                anglePorteRound += anglePorte;
                niveauRound += niveau;
                angleTamisRound += angleTamis;
                indexRound ++;
            }

//            myLvl2ClientSocket.getSensorsValues();

            myHandler.postDelayed(this, 30);
        }
    };

    @Override
    public void onPositionsReceivedFromServer(final double flechePos, final double levagePos, final double portePos, final double niveauX, double niveauY, final double tamisPos) {
        angleFleche = flechePos;
        angleLevage = levagePos;
        anglePorte = portePos;
        niveau = niveauX;
        angleTamis = tamisPos;

        CaptorValuesSingleton.setAngleFleche(angleFleche);
        CaptorValuesSingleton.setAngleLevage(angleLevage);
        CaptorValuesSingleton.setAnglePorte(anglePorte);
        CaptorValuesSingleton.setNiveau(niveau);
        CaptorValuesSingleton.setAngleTamis(angleTamis);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        myLvl2ClientSocket.getSensorsValues();
    }

    @Override
    public void onSetSensorValueFinish(final byte sensorArg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (sensorArg) {
                    case ARG_PORTE:
                        myListViewAdapterPorte.removeSelectedValue();
                        break;

                    case ARG_FLECHE:
                        myListViewAdapterFleche.removeSelectedValue();
                        break;

                    case ARG_LEVAGE:
                        myListViewAdapterLevage.removeSelectedValue();
                        break;
                }
            }
        });
    }

    @Override
    public void onServerVersionReceived(byte[] versionName) {
        serverVersion = new String(versionName);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                majVersions();
            }
        });
    }

    @Override
    public void onSocketStatusUpdate(final int status) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FancyButton fancyButton = findViewById(R.id.btnRefresh);
                switch (status) {
                    case ProtocolConstants.STATUS_CONNECTED:
                        fancyButton.setBorderColor(R.color.myGreen);
                        fancyButton.setBackgroundColor(getResources().getColor(R.color.myGreen));
                        fancyButton.setClickable(false);
                        break;

                    case ProtocolConstants.STATUS_NOT_CONNECTED:
                        fancyButton.setBorderColor(R.color.myRed);
                        fancyButton.setBackgroundColor(getResources().getColor(R.color.myRed));
                        fancyButton.setClickable(true);
                        break;
                }
            }
        });
    }

    private void majVersions() {
        TextView textViewVersion = findViewById(R.id.textViewVersion);
        textViewVersion.setText("Serveur: " + serverVersion + "/IHM: " + VERSION + " ");
    }

    private class GestionActionneur extends Thread {

        private double value, actualValue, initialValue;
        private boolean isOk = false;
        private int type;

        GestionActionneur(int type, double valueToSet) {
            this.type = type;
            this.value = valueToSet;

            switch (type) {
                case ARG_LEVAGE:
                    this.initialValue = angleLevageRounded;
                    break;

                case ARG_FLECHE:
                    this.initialValue = angleFlecheRounded;
                    break;

                case ARG_PORTE:
                    this.initialValue = anglePorteRounded;
                    break;

                case ARG_TAMIS:
                    this.initialValue = angleTamisRounded;
                    break;
            }
        }

        void stopAll() {
            this.interrupt();
            isOk = true;
        }

        @Override
        public void run() {

            isOk = false;

            while (!isOk) {
                switch (type) {
                    case ARG_LEVAGE:
                        actualValue = angleLevageRounded;
                        if (actualValue > value) {
                            myLvl2ClientSocket.setActuatorState(ARG_ACTION_LEVAGE_OFF, ARG_STATE_LOW);
                            myLvl2ClientSocket.setActuatorState(ARG_ACTION_LEVAGE_ON, ARG_STATE_HIGH);
                        }

                        if (actualValue < value) {
                            myLvl2ClientSocket.setActuatorState(ARG_ACTION_LEVAGE_ON, ARG_STATE_LOW);
                            myLvl2ClientSocket.setActuatorState(ARG_ACTION_LEVAGE_OFF, ARG_STATE_HIGH);
                        }

                        if ((actualValue < (value+3) && actualValue > (value-3)) ||
                                (actualValue > (initialValue+3) && actualValue > value) ||
                                (actualValue < (initialValue-3) && actualValue < value)) {

                            myLvl2ClientSocket.setActuatorState(ARG_ACTION_LEVAGE_OFF, ARG_STATE_LOW);
                            myLvl2ClientSocket.setActuatorState(ARG_ACTION_LEVAGE_ON, ARG_STATE_LOW);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    myListViewAdapterLevage.removeSelectedValue();
                                }
                            });
                            isOk = true;
                        }
                        break;

                    case ARG_FLECHE:
                        actualValue = angleFlecheRounded;
                        if (actualValue > value) {
                            myLvl2ClientSocket.setActuatorState(ARG_ACTION_FLECHE_OFF, ARG_STATE_LOW);
                            myLvl2ClientSocket.setActuatorState(ARG_ACTION_FLECHE_ON, ARG_STATE_HIGH);
                        }

                        if (actualValue < value) {
                            myLvl2ClientSocket.setActuatorState(ARG_ACTION_FLECHE_ON, ARG_STATE_LOW);
                            myLvl2ClientSocket.setActuatorState(ARG_ACTION_FLECHE_OFF, ARG_STATE_HIGH);
                        }

                        if ((actualValue < (value+3) && actualValue > (value-3)) ||
                                (actualValue > (initialValue+3) && actualValue > value) ||
                                (actualValue < (initialValue-3) && actualValue < value)) {
                            myLvl2ClientSocket.setActuatorState(ARG_ACTION_FLECHE_OFF, ARG_STATE_LOW);
                            myLvl2ClientSocket.setActuatorState(ARG_ACTION_FLECHE_ON, ARG_STATE_LOW);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    myListViewAdapterFleche.removeSelectedValue();
                                }
                            });
                            isOk = true;
                        }
                        break;

                    case ARG_PORTE:
                        actualValue = anglePorteRounded;
                        if (actualValue > value) {
                            myLvl2ClientSocket.setActuatorState(ARG_ACTION_PORTE_OFF, ARG_STATE_LOW);
                            myLvl2ClientSocket.setActuatorState(ARG_ACTION_PORTE_ON, ARG_STATE_HIGH);
                        }

                        if (actualValue < value) {
                            myLvl2ClientSocket.setActuatorState(ARG_ACTION_PORTE_ON, ARG_STATE_LOW);
                            myLvl2ClientSocket.setActuatorState(ARG_ACTION_PORTE_OFF, ARG_STATE_HIGH);
                        }

                        if ((actualValue < (value+3) && actualValue > (value-3)) ||
                                (actualValue > (initialValue+3) && actualValue > value) ||
                                (actualValue < (initialValue-3) && actualValue < value)) {
                            myLvl2ClientSocket.setActuatorState(ARG_ACTION_PORTE_OFF, ARG_STATE_LOW);
                            myLvl2ClientSocket.setActuatorState(ARG_ACTION_PORTE_ON, ARG_STATE_LOW);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    myListViewAdapterPorte.removeSelectedValue();
                                }
                            });
                            isOk = true;
                        }
                        break;

                    case ARG_TAMIS:
                        actualValue = angleTamisRounded;
                        if (actualValue > value) {
                            myLvl2ClientSocket.setActuatorState(ARG_ACTION_TAMIS_OFF, ARG_STATE_LOW);
                            myLvl2ClientSocket.setActuatorState(ARG_ACTION_TAMIS_ON, ARG_STATE_HIGH);
                        }

                        if (actualValue < value) {
                            myLvl2ClientSocket.setActuatorState(ARG_ACTION_TAMIS_ON, ARG_STATE_LOW);
                            myLvl2ClientSocket.setActuatorState(ARG_ACTION_TAMIS_OFF, ARG_STATE_HIGH);
                        }

                        if ((actualValue < (value+0.2) && actualValue > (value-0.2)) ||
                                (actualValue > (initialValue+0.2) && actualValue > value) ||
                                (actualValue < (initialValue-0.2) && actualValue < value)) {
                            myLvl2ClientSocket.setActuatorState(ARG_ACTION_TAMIS_OFF, ARG_STATE_LOW);
                            myLvl2ClientSocket.setActuatorState(ARG_ACTION_TAMIS_ON, ARG_STATE_LOW);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    myListViewAdapterTamis.removeSelectedValue();
                                }
                            });
                            isOk = true;
                        }
                        break;
                }


                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            switch (type) {
                case ARG_LEVAGE:
                    myLvl2ClientSocket.setActuatorState(ARG_ACTION_LEVAGE_OFF, ARG_STATE_LOW);
                    myLvl2ClientSocket.setActuatorState(ARG_ACTION_LEVAGE_ON, ARG_STATE_LOW);
                    break;

                case ARG_FLECHE:
                    myLvl2ClientSocket.setActuatorState(ARG_ACTION_FLECHE_OFF, ARG_STATE_LOW);
                    myLvl2ClientSocket.setActuatorState(ARG_ACTION_FLECHE_ON, ARG_STATE_LOW);
                    break;

                case ARG_PORTE:
                    myLvl2ClientSocket.setActuatorState(ARG_ACTION_PORTE_OFF, ARG_STATE_LOW);
                    myLvl2ClientSocket.setActuatorState(ARG_ACTION_PORTE_ON, ARG_STATE_LOW);
                    break;
            }
        }
    }
}
