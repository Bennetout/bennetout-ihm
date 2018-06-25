package com.monier.bennetout.ihmclient;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.monier.bennetout.ihmclient.configuration.ConfigManager;

import java.util.ArrayList;
import java.util.Locale;

import mehdi.sakout.fancybuttons.FancyButton;

import static com.monier.bennetout.ihmclient.utils.Utils.formatDouble;

public class MainActivity extends Activity {

    private double angleFleche = 0, angleLevage = 0, anglePorte = 0;
    private double niveau = 0;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // On charge la configuration sur chaque changement d'activité pour appliquer
        // les modifications utilisateurs si besoin
        ConfigManager.initConfig(this);

        setContentView(R.layout.main);
        btnReglageInit();
        btnRefreshInit();

        FlecheDesigner flecheDesigner = findViewById(R.id.flecheView);
        flecheDesigner.setAngle(angleFleche);

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

                niveau -= 1;
                NiveauDesigner niveauDesigner = findViewById(R.id.niveauView);
                niveauDesigner.setNiveau(niveau);

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

                        niveau -= 1;
                        NiveauDesigner niveauDesigner = findViewById(R.id.niveauView);
                        niveauDesigner.setNiveau(niveau);

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

                niveau += 1;
                NiveauDesigner niveauDesigner = findViewById(R.id.niveauView);
                niveauDesigner.setNiveau(niveau);
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

                        niveau += 1;
                        NiveauDesigner niveauDesigner = findViewById(R.id.niveauView);
                        niveauDesigner.setNiveau(niveau);
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

    private void btnRefreshInit() {
        final FancyButton fancyButton = findViewById(R.id.btnRefresh);
        fancyButton.setBorderColor(R.color.myRed);
        fancyButton.setBackgroundColor(getResources().getColor(R.color.myRed));
        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fancyButton.setBorderColor(R.color.myGreen);
                fancyButton.setBackgroundColor(getResources().getColor(R.color.myGreen));
            }
        });
    }

    private void btnReglageInit() {
        final FancyButton fancyButton = findViewById(R.id.btnReglage);
        fancyButton.setBorderColor(R.color.myRed);
        fancyButton.setBackgroundColor(getResources().getColor(R.color.myRed));
        fancyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
