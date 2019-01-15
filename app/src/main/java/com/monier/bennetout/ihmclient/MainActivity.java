package com.monier.bennetout.ihmclient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import com.monier.bennetout.ihmclient.communication.Lvl2ClientSocket;
import com.monier.bennetout.ihmclient.communication.ProtocolConstants;
import com.monier.bennetout.ihmclient.configuration.ConfigManager;
import com.monier.bennetout.ihmclient.configuration.activities.ConfigActivity;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Objects;

import mehdi.sakout.fancybuttons.FancyButton;

import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.ARG_ACTION_FLECHE_OFF;
import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.ARG_ACTION_FLECHE_ON;
import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.ARG_ACTION_LEVAGE_OFF;
import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.ARG_ACTION_LEVAGE_ON;
import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.ARG_ACTION_PORTE_OFF;
import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.ARG_ACTION_PORTE_ON;
import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.ARG_ACTION_TAPIS_OFF;
import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.ARG_ACTION_TAPIS_ON;
import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.ARG_FLECHE;
import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.ARG_LEVAGE;
import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.ARG_PORTE;
import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.ARG_STATE_HIGH;
import static com.monier.bennetout.ihmclient.communication.ProtocolConstants.ARG_STATE_LOW;
import static com.monier.bennetout.ihmclient.utils.Utils.formatDouble;

public class MainActivity extends Activity implements Lvl2ClientSocket.SocketClientListener {

    private static final String TAG = MainActivity.class.getCanonicalName();

    private double angleFleche = 0, angleLevage = 0, anglePorte = 0;
    private double niveau = 0;
    Lvl2ClientSocket myLvl2ClientSocket = new Lvl2ClientSocket("10.3.141.1", 65000);

    private TextView textViewFleche;
    private TextView textViewLevage;
    private TextView textViewPorte;

    private MyListViewAdapter myListViewAdapterPorte;
    private MyListViewAdapter myListViewAdapterLevage;
    private MyListViewAdapter myListViewAdapterFleche;

//    private FlecheDesigner flecheDesigner;ArrayList
//    private RemorqueDesigner remorqueDesigner;
//    private NiveauDesigner niveauDesigner;

    private RemorquePainter remorquePainter;
    private FlechePainter flechePainter;
    private NiveauPainter niveauPainter;

    private Handler myHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // On charge la configuration sur chaque changement d'activité pour appliquer
        // les modifications utilisateurs si besoin
        ConfigManager.initConfig(this);

        myLvl2ClientSocket.setListener(this);

        setContentView(R.layout.main);

        // Get a random id into the main View
        View randomView = findViewById(R.id.btnRefresh);
        View root = randomView.getRootView();
        root.setBackgroundColor(Color.WHITE);

        textViewFleche = findViewById(R.id.textViewFlecheValue);
        textViewLevage = findViewById(R.id.textViewLevageValue);
        textViewPorte = findViewById(R.id.textViewPorteValue);

        remorquePainter = findViewById(R.id.remorqueView);
        flechePainter = findViewById(R.id.flecheView);
        niveauPainter = findViewById(R.id.niveauView);

        btnReglageInit();
        btnRefreshInit();

        btnFlecheMarcheInit();
        btnFlecheArretInit();

        btnLevageMarcheInit();
        btnLevageArretInit();

        btnPorteMarcheInit();
        btnPorteArretInit();

        btnTapisMarcheInit();
        btnTapisArretInit();

        listViewPorteInit();
        listViewFlecheInit();
        listViewLevageInit();

        myHandler = new Handler();
    }

    boolean tapisArretStatus = false;
    private void btnTapisArretInit() {

        final FancyButton button = findViewById(R.id.buttonTapisArret);
        Drawable drawable = button.getBackground();

        // On travaille avec des clones
        final Drawable drawableInit = Objects.requireNonNull(drawable.getConstantState()).newDrawable();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tapisMarcheStatus)
                    return;

                tapisArretStatus = !tapisArretStatus;

                if (tapisArretStatus) {
                    Drawable drawable = button.getBackground();
                    drawable.setColorFilter(getResources().getColor(R.color.myOrange), PorterDuff.Mode.MULTIPLY);
                    button.setBackground(drawable);
                    myLvl2ClientSocket.setActuatorState(ARG_ACTION_TAPIS_OFF, ARG_STATE_HIGH);
                } else {
                    button.setBackground(Objects.requireNonNull(drawableInit.getConstantState()).newDrawable());
                    myLvl2ClientSocket.setActuatorState(ARG_ACTION_TAPIS_OFF, ARG_STATE_LOW);
                }
            }
        });
    }

    boolean tapisMarcheStatus = false;
    private void btnTapisMarcheInit() {

        final FancyButton button = findViewById(R.id.buttonTapisMarche);
        Drawable drawable = button.getBackground();

        // On travaille avec des clones
        final Drawable drawableInit = Objects.requireNonNull(drawable.getConstantState()).newDrawable();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tapisArretStatus)
                    return;

                tapisMarcheStatus = !tapisMarcheStatus;

                if (tapisMarcheStatus) {
                    Drawable drawable = button.getBackground();
                    drawable.setColorFilter(getResources().getColor(R.color.myOrange), PorterDuff.Mode.MULTIPLY);
                    button.setBackground(drawable);
                    myLvl2ClientSocket.setActuatorState(ARG_ACTION_TAPIS_ON, ARG_STATE_HIGH);
                } else {
                    button.setBackground(Objects.requireNonNull(drawableInit.getConstantState()).newDrawable());
                    myLvl2ClientSocket.setActuatorState(ARG_ACTION_TAPIS_ON, ARG_STATE_LOW);
                }
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void btnPorteArretInit() {

        FancyButton button = findViewById(R.id.buttonPorteArret);
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        myLvl2ClientSocket.setActuatorState(ARG_ACTION_PORTE_OFF, ARG_STATE_HIGH);
                        break;

                    case MotionEvent.ACTION_UP:
                        myLvl2ClientSocket.setActuatorState(ARG_ACTION_PORTE_OFF, ARG_STATE_LOW);
                        break;
                }

                return false;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void btnPorteMarcheInit() {

        FancyButton button = findViewById(R.id.buttonPorteMarche);
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        myLvl2ClientSocket.setActuatorState(ARG_ACTION_PORTE_ON, ARG_STATE_HIGH);
                        break;

                    case MotionEvent.ACTION_UP:
                        myLvl2ClientSocket.setActuatorState(ARG_ACTION_PORTE_ON, ARG_STATE_LOW);
                        break;
                }

                return false;
            }

        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void btnLevageArretInit() {

        FancyButton button = findViewById(R.id.buttonLevageArret);
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        myLvl2ClientSocket.setActuatorState(ARG_ACTION_LEVAGE_OFF, ARG_STATE_HIGH);
                        break;

                    case MotionEvent.ACTION_UP:
                        myLvl2ClientSocket.setActuatorState(ARG_ACTION_LEVAGE_OFF, ARG_STATE_LOW);
                        break;
                }

                return false;
            }

        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void btnLevageMarcheInit() {

        FancyButton button = findViewById(R.id.buttonLevageMarche);
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        myLvl2ClientSocket.setActuatorState(ARG_ACTION_LEVAGE_ON, ARG_STATE_HIGH);
                        break;

                    case MotionEvent.ACTION_UP:
                        myLvl2ClientSocket.setActuatorState(ARG_ACTION_LEVAGE_ON, ARG_STATE_LOW);
                        break;
                }

                return false;
            }

        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void btnFlecheArretInit() {

        FancyButton button = findViewById(R.id.buttonFlecheArret);
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        myLvl2ClientSocket.setActuatorState(ARG_ACTION_FLECHE_OFF, ARG_STATE_HIGH);
                        break;

                    case MotionEvent.ACTION_UP:
                        myLvl2ClientSocket.setActuatorState(ARG_ACTION_FLECHE_OFF, ARG_STATE_LOW);
                        break;
                }

                return false;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void btnFlecheMarcheInit() {

        FancyButton button = findViewById(R.id.buttonFlecheMarche);
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        myLvl2ClientSocket.setActuatorState(ARG_ACTION_FLECHE_ON, ARG_STATE_HIGH);
                        break;

                    case MotionEvent.ACTION_UP:
                        myLvl2ClientSocket.setActuatorState(ARG_ACTION_FLECHE_ON, ARG_STATE_LOW);
                        break;
                }

                return false;
            }
        });
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
            configs.add(new MyCustomHolder(numberFormat.format(anUserConfig) + "°", false, colorId));
        }

        final GestionLevage[] gestionLevage = new GestionLevage[1];

        myListViewAdapterLevage = new MyListViewAdapter(configs, getResources().getDimension(R.dimen._11sdp),
                new MyListViewAdapter.MyListViewListener() {
                    @Override
                    public void onNewPositionClicked(boolean state, final double value) {

                        if (gestionLevage[0] != null)
                            gestionLevage[0].stopAll();

                        if (!state) {
                            return;
                        }

                        gestionLevage[0] = new GestionLevage();
                        gestionLevage[0].value = value;
                        gestionLevage[0].start();
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
            configs.add(new MyCustomHolder(numberFormat.format(anUserConfig) + "°", false, colorId));
        }

        myListViewAdapterFleche = new MyListViewAdapter(configs, getResources().getDimension(R.dimen._11sdp),
                new MyListViewAdapter.MyListViewListener() {
                    @Override
                    public void onNewPositionClicked(boolean state, double value) {
//                        if (state)
//                            myLvl2ClientSocket.setSensorValue(Lvl2ClientSocket.SENSOR_FLECHE, value);
//                        else
//                            myLvl2ClientSocket.stopSetSensorValue(Lvl2ClientSocket.SENSOR_FLECHE);
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
            configs.add(new MyCustomHolder(numberFormat.format(anUserConfig) + "°", false, colorId));
        }

        myListViewAdapterPorte = new MyListViewAdapter(configs, getResources().getDimension(R.dimen._11sdp),
                new MyListViewAdapter.MyListViewListener() {
                    @Override
                    public void onNewPositionClicked(boolean state, double value) {
//                        if (state)
//                            myLvl2ClientSocket.setSensorValue(Lvl2ClientSocket.SENSOR_PORTE, value);
//                        else
//                            myLvl2ClientSocket.stopSetSensorValue(Lvl2ClientSocket.SENSOR_PORTE);
                    }
                });
        mRecyclerView.setAdapter(myListViewAdapterPorte);
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

        double droiteX = angleInitial;

        double droiteA = (droiteYb - droiteYa) / (droiteXb - droiteXa);
        double droiteB = - (droiteA * droiteXa);

        double droiteY = droiteA * droiteX + droiteB;

        return droiteY *((ConfigManager.model.BORNE_MAX_LEVAGE - ConfigManager.model.BORNE_MIN_LEVAGE)*0.01);
//        return droiteY *0.45;
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

        double droiteX = angleInitial;

        double droiteA = (droiteYb - droiteYa) / (droiteXb - droiteXa);
        double droiteB = - (droiteA * droiteXa);

        double droiteY = droiteA * droiteX + droiteB;

        return droiteY *((ConfigManager.model.BORNE_MAX_PORTE - ConfigManager.model.BORNE_MIN_PORTE)*0.01);
//        return droiteY *0.9;
    }

    public static double calculPosFleche(double angleInitial) {
        double flecheCallibZero = ConfigManager.model.FLECHE_CALLIB_ZERO;
        double flecheCallibCent = ConfigManager.model.FLECHE_CALLIB_CENT;

        double droiteXa = flecheCallibZero;
        double droiteXb = flecheCallibCent;
        double droiteYa = 0;
        double droiteYb = 100;

        double droiteX = angleInitial;

        double droiteA = (droiteYb - droiteYa) / (droiteXb - droiteXa);
        double droiteB = - (droiteA * droiteXa);

        double droiteY = droiteA * droiteX + droiteB;

        return droiteY * (ConfigManager.model.BORNE_MAX_FLECHE - ConfigManager.model.BORNE_MIN_FLECHE)*0.01 - ConfigManager.model.BORNE_MAX_FLECHE;
//        return droiteY*0.9 -45;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private Runnable majIhm = new Runnable() {
        @Override
        public void run() {

            remorquePainter.setAngle(calculPosLevage(angleLevage), calculPosPorte(anglePorte));
            flechePainter.setAngle(calculPosFleche(angleFleche));
            niveauPainter.setNiveau(calculPosNiveau(niveau));

//            remorquePainter.setAngle(angleLevage, anglePorte);
//            flechePainter.setAngle(angleFleche);
//            niveauPainter.setNiveau(niveau);

            textViewFleche.setText(formatDouble(angleFleche));
            textViewLevage.setText(formatDouble(angleLevage));
            textViewPorte.setText(formatDouble(anglePorte));

            myLvl2ClientSocket.getSensorsValues();

            myHandler.postDelayed(this, 500);
        }
    };

    @Override
    public void onPositionsReceivedFromServer(final double flechePos, final double levagePos, final double portePos, final double niveauX, double niveauY) {
        angleFleche = flechePos;
        angleLevage = levagePos;
        anglePorte = portePos;
        niveau = niveauX;

        CaptorValuesSingleton.setAngleFleche(angleFleche);
        CaptorValuesSingleton.setAngleLevage(angleLevage);
        CaptorValuesSingleton.setAnglePorte(anglePorte);
        CaptorValuesSingleton.setNiveau(niveau);
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
    public void onSocketStatusUpdate(final int status) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FancyButton fancyButton = findViewById(R.id.btnRefresh);
                switch (status) {
                    case ProtocolConstants.STATUS_CONNECTED:
                        fancyButton.setBorderColor(R.color.myGreen);
                        fancyButton.setBackgroundColor(getResources().getColor(R.color.myGreen));
                        break;

                    case ProtocolConstants.STATUS_NOT_CONNECTED:
                        fancyButton.setBorderColor(R.color.myRed);
                        fancyButton.setBackgroundColor(getResources().getColor(R.color.myRed));
                        break;
                }
            }
        });
    }

    private class GestionLevage extends Thread {

        double value;
        boolean isOk = false;

        void stopAll() {
            this.interrupt();
            isOk = true;
        }

        @Override
        public void run() {

            isOk = false;

            while (!isOk) {
                if (angleLevage > value) {
                    myLvl2ClientSocket.setActuatorState(ARG_ACTION_LEVAGE_OFF, ARG_STATE_LOW);
                    myLvl2ClientSocket.setActuatorState(ARG_ACTION_LEVAGE_ON, ARG_STATE_HIGH);
                }

                if (angleLevage < value) {
                    myLvl2ClientSocket.setActuatorState(ARG_ACTION_LEVAGE_ON, ARG_STATE_LOW);
                    myLvl2ClientSocket.setActuatorState(ARG_ACTION_LEVAGE_OFF, ARG_STATE_HIGH);
                }

                if (angleLevage < value+3 &&
                        angleLevage > value-3) {
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

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            myLvl2ClientSocket.setActuatorState(ARG_ACTION_LEVAGE_OFF, ARG_STATE_LOW);
            myLvl2ClientSocket.setActuatorState(ARG_ACTION_LEVAGE_ON, ARG_STATE_LOW);
        }
    }
}
