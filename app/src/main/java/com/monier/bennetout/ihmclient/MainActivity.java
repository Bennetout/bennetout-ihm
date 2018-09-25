package com.monier.bennetout.ihmclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.monier.bennetout.ihmclient.communication.Lvl2ClientSocket;
import com.monier.bennetout.ihmclient.configuration.ConfigManager;
import com.monier.bennetout.ihmclient.configuration.activities.ConfigActivity;

import java.io.IOException;
import java.util.ArrayList;

import mehdi.sakout.fancybuttons.FancyButton;

import static com.monier.bennetout.ihmclient.utils.Utils.formatDouble;

public class MainActivity extends Activity implements Lvl2ClientSocket.SocketClientListener {

    private static final String TAG = MainActivity.class.getCanonicalName();

    private double angleFleche = 0, angleLevage = 0, anglePorte = 0;
    private double niveau = 0;
    private final Handler handler = new Handler();
    Lvl2ClientSocket myLvl2ClientSocket = new Lvl2ClientSocket("10.3.141.1", 65000);

    private TextView textViewFleche;
    private TextView textViewLevage;
    private TextView textViewPorte;

//    private FlecheDesigner flecheDesigner;
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

        listViewPorteInit();
        listViewFlecheInit();
        listViewLevageInit();

        myHandler = new Handler();
        myHandler.postDelayed(majIhm, 0);
    }

    private boolean isBtnPorteArretPressed = false;
    private void btnPorteArretInit() {

        Button button = findViewById(R.id.buttonPorteArret);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isBtnPorteArretPressed = false;
                handler.removeCallbacksAndMessages(null);
                anglePorte -= 1;
                if (anglePorte < ConfigManager.model.BORNE_MIN_PORTE)
                    anglePorte = ConfigManager.model.BORNE_MIN_PORTE;
            }
        });

        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                isBtnPorteArretPressed = true;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        anglePorte -= 1;
                        if (anglePorte < ConfigManager.model.BORNE_MIN_PORTE)
                            anglePorte = ConfigManager.model.BORNE_MIN_PORTE;

                        if (isBtnPorteArretPressed)
                            handler.postDelayed(this, 20);
                        else
                            handler.removeCallbacksAndMessages(null);
                    }
                }, 100);
                return false;
            }
        });
    }
    private boolean isBtnPorteMarchePressed = false;
    private void btnPorteMarcheInit() {

        Button button = findViewById(R.id.buttonPorteMarche);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isBtnPorteMarchePressed = false;
                handler.removeCallbacksAndMessages(null);
                anglePorte += 1;
                if (anglePorte > ConfigManager.model.BORNE_MAX_PORTE)
                    anglePorte = ConfigManager.model.BORNE_MAX_PORTE;
            }
        });

        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                isBtnPorteMarchePressed = true;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        anglePorte += 1;
                        if (anglePorte > ConfigManager.model.BORNE_MAX_PORTE)
                            anglePorte = ConfigManager.model.BORNE_MAX_PORTE;

                        if (isBtnPorteMarchePressed)
                            handler.postDelayed(this, 20);
                        else
                            handler.removeCallbacksAndMessages(null);
                    }
                }, 100);
                return false;
            }
        });
    }

    private boolean isBtnLevageArretPressed = false;
    private void btnLevageArretInit() {

        Button button = findViewById(R.id.buttonLevageArret);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isBtnLevageArretPressed = false;
                handler.removeCallbacksAndMessages(null);
                angleLevage -= 1;
                if (angleLevage < ConfigManager.model.BORNE_MIN_LEVAGE)
                    angleLevage = ConfigManager.model.BORNE_MIN_LEVAGE;
            }
        });

        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                isBtnLevageArretPressed = true;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        angleLevage -= 1;
                        if (angleLevage < ConfigManager.model.BORNE_MIN_LEVAGE)
                            angleLevage = ConfigManager.model.BORNE_MIN_LEVAGE;

                        if (isBtnLevageArretPressed)
                            handler.postDelayed(this, 20);
                        else
                            handler.removeCallbacksAndMessages(null);
                    }
                }, 100);
                return false;
            }
        });
    }
    private boolean isBtnLevageMarchePressed = false;
    private void btnLevageMarcheInit() {

        Button button = findViewById(R.id.buttonLevageMarche);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isBtnLevageMarchePressed = false;
                handler.removeCallbacksAndMessages(null);
                angleLevage += 1;
                if (angleLevage > ConfigManager.model.BORNE_MAX_LEVAGE)
                    angleLevage = ConfigManager.model.BORNE_MAX_LEVAGE;
            }
        });

        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                isBtnLevageMarchePressed = true;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        angleLevage += 1;
                        if (angleLevage > ConfigManager.model.BORNE_MAX_LEVAGE)
                            angleLevage = ConfigManager.model.BORNE_MAX_LEVAGE;

                        if (isBtnLevageMarchePressed)
                            handler.postDelayed(this, 20);
                        else
                            handler.removeCallbacksAndMessages(null);
                    }
                }, 100);
                return false;
            }
        });
    }

    private boolean isBtnFlecheArretPressed = false;
    private void btnFlecheArretInit() {

        Button button = findViewById(R.id.buttonFlecheArret);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isBtnFlecheArretPressed = false;
                handler.removeCallbacksAndMessages(null);
                angleFleche -= 1;
            }
        });

        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                isBtnFlecheArretPressed = true;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        angleFleche -= 1;
                        if (isBtnFlecheArretPressed)
                            handler.postDelayed(this, 20);
                        else
                            handler.removeCallbacksAndMessages(null);
                    }
                }, 100);
                return false;
            }
        });
    }
    private boolean isBtnFlecheMarchePressed = false;
    private void btnFlecheMarcheInit() {

        Button button = findViewById(R.id.buttonFlecheMarche);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isBtnFlecheMarchePressed = false;
                handler.removeCallbacksAndMessages(null);
                angleFleche += 1;
            }
        });

        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                isBtnFlecheMarchePressed = true;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        angleFleche += 1;

                        if (isBtnFlecheMarchePressed)
                            handler.postDelayed(this, 20);
                        else
                            handler.removeCallbacksAndMessages(null);
                    }
                }, 100);
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

        // specify an adapter (see also next example)

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("0°");
        arrayList.add("10°");
        arrayList.add("30°");
        arrayList.add("100°");
        arrayList.add("150°");
        arrayList.add("175°");
        MyListViewAdapter myListViewAdapter = new MyListViewAdapter(arrayList);
        mRecyclerView.setAdapter(myListViewAdapter);
    }

    private void listViewFlecheInit() {
        RecyclerView mRecyclerView = findViewById(R.id.listViewFleche);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("0°");
        arrayList.add("10°");
        arrayList.add("30°");
        arrayList.add("100°");
        arrayList.add("150°");
        arrayList.add("175°");
        MyListViewAdapter myListViewAdapter = new MyListViewAdapter(arrayList);
        mRecyclerView.setAdapter(myListViewAdapter);
    }

    private void listViewPorteInit() {
        RecyclerView mRecyclerView = findViewById(R.id.listViewPorte);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("0°");
        arrayList.add("10°");
        arrayList.add("30°");
        arrayList.add("100°");
        arrayList.add("150°");
        arrayList.add("175°");
        MyListViewAdapter myListViewAdapter = new MyListViewAdapter(arrayList);
        mRecyclerView.setAdapter(myListViewAdapter);
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

        double droiteX = angleInitial;

        double droiteA = (droiteYb - droiteYa) / (droiteXb - droiteXa);
        double droiteB = - (droiteA * droiteXa);

        double droiteY = droiteA * droiteX + droiteB;

        return droiteY *((ConfigManager.model.BORNE_MAX_LEVAGE - ConfigManager.model.BORNE_MIN_LEVAGE)*0.01);
//        return droiteY *0.45;
    }

    public static double calculPosNiveau(double angleInitial) {
        double niveauCallibZero = ConfigManager.model.NIVEAU_CALLIB_ZERO;
        double niveauCallibCent = ConfigManager.model.NIVEAU_CALLIB_CENT;

        double droiteXa = niveauCallibZero;
        double droiteXb = niveauCallibCent;
        double droiteYa = 0;
        double droiteYb = 100;

        double droiteX = angleInitial;

        double droiteA = (droiteYb - droiteYa) / (droiteXb - droiteXa);
        double droiteB = - (droiteA * droiteXa);

        double droiteY = droiteA * droiteX + droiteB;

        return droiteY *0.2 - 10;
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

            myLvl2ClientSocket.getSensorsValues();

//            remorquePainter.setAngle(calculPosLevage(angleLevage), calculPosPorte(anglePorte));
//            flechePainter.setAngle(calculPosFleche(angleFleche));
//            niveauPainter.setNiveau(calculPosNiveau(niveau));

            remorquePainter.setAngle(angleLevage, anglePorte);
            flechePainter.setAngle(angleFleche);
            niveauPainter.setNiveau(niveau);

            textViewFleche.setText(formatDouble(angleFleche));
            textViewLevage.setText(formatDouble(angleLevage));
            textViewPorte.setText(formatDouble(anglePorte));

            myHandler.postDelayed(this, 100);
        }
    };

    @Override
    public void onPositionsReceivedFromServer(final double flechePos, final double levagePos, final double portePos, final double niveauX, double niveauY) {
        angleFleche = flechePos;
        angleLevage = levagePos;
        anglePorte = portePos;
        niveau = niveauX;
    }

    @Override
    public void onSocketStatusUpdate(final int status) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FancyButton fancyButton = findViewById(R.id.btnRefresh);
                switch (status) {
                    case ClientSocket.STATUS_CONNECTED:
                        fancyButton.setBorderColor(R.color.myGreen);
                        fancyButton.setBackgroundColor(getResources().getColor(R.color.myGreen));
                        break;

                    case ClientSocket.STATUS_NOT_CONNECTED:
                        fancyButton.setBorderColor(R.color.myRed);
                        fancyButton.setBackgroundColor(getResources().getColor(R.color.myRed));
                        break;
                }
            }
        });
    }
}
