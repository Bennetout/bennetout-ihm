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
import android.widget.Toast;

import com.monier.bennetout.ihmclient.configuration.ConfigManager;
import com.monier.bennetout.ihmclient.configuration.activities.ConfigActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import mehdi.sakout.fancybuttons.FancyButton;

import static com.monier.bennetout.ihmclient.utils.Utils.formatDouble;

public class MainActivity extends Activity implements ClientSocket.ClientSocketListener {

    private double angleFleche = 0, angleLevage = 0, anglePorte = 0;
    private double niveau = 0;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // On charge la configuration sur chaque changement d'activité pour appliquer
        // les modifications utilisateurs si besoin
        ConfigManager.initConfig(this);

        ClientSocket.addListener(this);


        setContentView(R.layout.main);
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
                if (anglePorte < RemorqueDesigner.BORNE_MIN_BENNE)
                    anglePorte = RemorqueDesigner.BORNE_MIN_BENNE;

                TextView textView = findViewById(R.id.textViewPorteValue);
                textView.setText(String.format(Locale.FRANCE, "%s°", formatDouble(anglePorte)));
                RemorqueDesigner remorqueDesigner = findViewById(R.id.remorqueView);
                remorqueDesigner.setAngleBenne(anglePorte);
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
                        if (anglePorte < RemorqueDesigner.BORNE_MIN_BENNE)
                            anglePorte = RemorqueDesigner.BORNE_MIN_BENNE;

                        TextView textView = findViewById(R.id.textViewPorteValue);
                        textView.setText(String.format(Locale.FRANCE, "%s°", formatDouble(anglePorte)));
                        RemorqueDesigner remorqueDesigner = findViewById(R.id.remorqueView);
                        remorqueDesigner.setAngleBenne(anglePorte);

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
                if (anglePorte > RemorqueDesigner.BORNE_MAX_BENNE)
                    anglePorte = RemorqueDesigner.BORNE_MAX_BENNE;

                RemorqueDesigner remorqueDesigner = findViewById(R.id.remorqueView);
                remorqueDesigner.setAngleBenne(anglePorte);
                TextView textView = findViewById(R.id.textViewPorteValue);
                textView.setText(String.format(Locale.FRANCE, "%s°", formatDouble(anglePorte)));
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
                        if (anglePorte > RemorqueDesigner.BORNE_MAX_BENNE)
                            anglePorte = RemorqueDesigner.BORNE_MAX_BENNE;

                        TextView textView = findViewById(R.id.textViewPorteValue);
                        textView.setText(String.format(Locale.FRANCE, "%s°", formatDouble(anglePorte)));
                        RemorqueDesigner remorqueDesigner = findViewById(R.id.remorqueView);
                        remorqueDesigner.setAngleBenne(anglePorte);

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
                if (angleLevage < RemorqueDesigner.BORNE_MIN)
                    angleLevage = RemorqueDesigner.BORNE_MIN;

                TextView textView = findViewById(R.id.textViewLevageValue);
                textView.setText(String.format(Locale.FRANCE, "%s°", formatDouble(angleLevage)));
                RemorqueDesigner remorqueDesigner = findViewById(R.id.remorqueView);
                remorqueDesigner.setAngle(angleLevage);
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
                        if (angleLevage < RemorqueDesigner.BORNE_MIN)
                            angleLevage = RemorqueDesigner.BORNE_MIN;

                        TextView textView = findViewById(R.id.textViewLevageValue);
                        textView.setText(String.format(Locale.FRANCE, "%s°", formatDouble(angleLevage)));
                        RemorqueDesigner remorqueDesigner = findViewById(R.id.remorqueView);
                        remorqueDesigner.setAngle(angleLevage);

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
                if (angleLevage > RemorqueDesigner.BORNE_MAX)
                    angleLevage = RemorqueDesigner.BORNE_MAX;

                RemorqueDesigner remorqueDesigner = findViewById(R.id.remorqueView);
                remorqueDesigner.setAngle(angleLevage);
                TextView textView = findViewById(R.id.textViewLevageValue);
                textView.setText(String.format(Locale.FRANCE, "%s°", formatDouble(angleLevage)));
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
                        if (angleLevage > RemorqueDesigner.BORNE_MAX)
                            angleLevage = RemorqueDesigner.BORNE_MAX;

                        TextView textView = findViewById(R.id.textViewLevageValue);
                        textView.setText(String.format(Locale.FRANCE, "%s°", formatDouble(angleLevage)));
                        RemorqueDesigner remorqueDesigner = findViewById(R.id.remorqueView);
                        remorqueDesigner.setAngle(angleLevage);

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
                TextView textView = findViewById(R.id.textViewFlecheValue);
                textView.setText(String.format(Locale.FRANCE, "%s°", formatDouble(angleFleche)));
                FlecheDesigner flecheDesigner = findViewById(R.id.flecheView);
                flecheDesigner.setAngle(angleFleche);

//                niveau -= 1;
//                NiveauDesigner niveauDesigner = findViewById(R.id.niveauView);
//                niveauDesigner.setNiveau(niveau);

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
                        TextView textView = findViewById(R.id.textViewFlecheValue);
                        textView.setText(String.format(Locale.FRANCE, "%s°", formatDouble(angleFleche)));
                        FlecheDesigner flecheDesigner = findViewById(R.id.flecheView);
                        flecheDesigner.setAngle(angleFleche);

//                        niveau -= 1;
//                        NiveauDesigner niveauDesigner = findViewById(R.id.niveauView);
//                        niveauDesigner.setNiveau(niveau);

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
                FlecheDesigner flecheDesigner = findViewById(R.id.flecheView);
                flecheDesigner.setAngle(angleFleche);
                TextView textView = findViewById(R.id.textViewFlecheValue);
                textView.setText(String.format(Locale.FRANCE, "%s°", formatDouble(angleFleche)));

//                niveau += 1;
//                NiveauDesigner niveauDesigner = findViewById(R.id.niveauView);
//                niveauDesigner.setNiveau(niveau);
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
                        TextView textView = findViewById(R.id.textViewFlecheValue);
                        textView.setText(String.format(Locale.FRANCE, "%s°", formatDouble(angleFleche)));
                        FlecheDesigner flecheDesigner = findViewById(R.id.flecheView);
                        flecheDesigner.setAngle(angleFleche);

//                        niveau += 1;
//                        NiveauDesigner niveauDesigner = findViewById(R.id.niveauView);
//                        niveauDesigner.setNiveau(niveau);
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
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.listViewLevage);

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
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.listViewFleche);

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
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.listViewPorte);

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

    private ClientSocket clientSocket = new ClientSocket();
    private void btnRefreshInit() {
        final FancyButton fancyButton = findViewById(R.id.btnRefresh);
        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
//                            clientSocket.connect("192.168.42.1");
                            clientSocket.deconnect();
                            clientSocket.connect("127.0.0.1");
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

        return droiteY *0.45;
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

        return droiteY *0.9;
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

        return droiteY*0.9 -45;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onPositionsReceivedFromServer(final double flechePos, final double levagePos, final double portePos, final double niveauX, double niveauY) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                angleFleche = flechePos;
                angleLevage = levagePos;
                anglePorte = portePos;
                niveau = niveauX;

                TextView textViewFleche = findViewById(R.id.textViewFlecheValue);
                TextView textViewLevage = findViewById(R.id.textViewLevageValue);
                TextView textViewPorte = findViewById(R.id.textViewPorteValue);

                FlecheDesigner flecheDesigner = findViewById(R.id.flecheView);
                RemorqueDesigner remorqueDesigner = findViewById(R.id.remorqueView);
                NiveauDesigner niveauDesigner = findViewById(R.id.niveauView);

                flecheDesigner.setAngle(calculPosFleche(flechePos));
                remorqueDesigner.setAngle(calculPosLevage(levagePos));
                remorqueDesigner.setAngleBenne(calculPosPorte(portePos));
                niveauDesigner.setNiveau(niveauX);

                textViewFleche.setText(formatDouble(flechePos));
                textViewLevage.setText(formatDouble(levagePos));
                textViewPorte.setText(formatDouble(portePos));
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
