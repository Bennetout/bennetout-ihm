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

import java.util.ArrayList;
import java.util.Locale;

import mehdi.sakout.fancybuttons.FancyButton;

import static com.monier.bennetout.ihmclient.utils.Utils.formatDouble;

public class MainActivity extends Activity {

    private double angleFleche = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        btnReglageInit();
        btnRefreshInit();

        FlecheDesigner flecheDesigner = findViewById(R.id.flecheView);
        flecheDesigner.setAngle(angleFleche);

        btnFlecheMarcheInit();
        btnFlecheArretInit();
        listViewPorteInit();
        listViewFlecheInit();
        listViewLevageInit();
    }

    private boolean isBtnFlecheArretPressed = false;
    private void btnFlecheArretInit() {

        Button button = findViewById(R.id.buttonFlecheArret);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isBtnFlecheArretPressed = false;
                angleFleche -= 1;
                TextView textView = findViewById(R.id.textViewFlecheValue);
                textView.setText(String.format(Locale.FRANCE, "%s°", formatDouble(angleFleche)));
                FlecheDesigner flecheDesigner = findViewById(R.id.flecheView);
                flecheDesigner.setAngle(angleFleche);
            }
        });

        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                isBtnFlecheArretPressed = true;
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        angleFleche -= 1;
                        TextView textView = findViewById(R.id.textViewFlecheValue);
                        textView.setText(String.format(Locale.FRANCE, "%s°", formatDouble(angleFleche)));
                        FlecheDesigner flecheDesigner = findViewById(R.id.flecheView);
                        flecheDesigner.setAngle(angleFleche);

                        if (isBtnFlecheArretPressed)
                            handler.postDelayed(this, 100);
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
                angleFleche += 1;
                FlecheDesigner flecheDesigner = findViewById(R.id.flecheView);
                flecheDesigner.setAngle(angleFleche);
                TextView textView = findViewById(R.id.textViewFlecheValue);
                textView.setText(String.format(Locale.FRANCE, "%s°", formatDouble(angleFleche)));
            }
        });

        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                isBtnFlecheMarchePressed = true;
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        angleFleche += 1;
                        TextView textView = findViewById(R.id.textViewFlecheValue);
                        textView.setText(String.format(Locale.FRANCE, "%s°", formatDouble(angleFleche)));
                        FlecheDesigner flecheDesigner = findViewById(R.id.flecheView);
                        flecheDesigner.setAngle(angleFleche);

                        if (isBtnFlecheMarchePressed)
                            handler.postDelayed(this, 100);
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
