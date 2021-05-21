package com.monier.bennetout.ihmclient;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.monier.bennetout.ihmclient.configuration.ConfigManager;

import static com.monier.bennetout.ihmclient.utils.Utils.degree2radian;
import static com.monier.bennetout.ihmclient.utils.Utils.formatDouble;

public class FlechePainter extends SurfaceView implements SurfaceHolder.Callback {

    private int MY_RED    = Color.parseColor("#800000");
    private int MY_BLACK    = Color.parseColor("#000000");
    private int MY_WHITE    = Color.parseColor("#ffffff");
    private int MY_BROWN    = Color.parseColor("#663300");

    private Paint myRedPaint = new Paint(),
            myBlackPaint = new Paint(),
            mySecondBlackPaint = new Paint(),
            myWhitePaint = new Paint();

    private Paint myBlackStroke = new Paint(),
            myBrownStroke = new Paint();

    private double angle = 0;

    private float canvasSize;
    private float longueurFleche;
    private float flecheXFixe;
    private float flecheYFixe;
    private float longueurRemorque;
    private float largeurRemorque;

    private SurfaceHolder mySurfaceHolder;

    public FlechePainter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FlechePainter(Context context) {
        super(context);
        init();
    }

    private void init() {

        mySurfaceHolder = getHolder();
        mySurfaceHolder.addCallback(this);

        canvasSize = getResources().getDimension(R.dimen._130sdp);

        longueurFleche = canvasSize / (float)4;
        flecheXFixe = canvasSize / (float)2;
        flecheYFixe = canvasSize;
        longueurRemorque =  canvasSize * (float) 0.6;
        largeurRemorque =  canvasSize / (float)2.5;

        myRedPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        myRedPaint.setColor(MY_RED);

        myBlackStroke.setStyle(Paint.Style.STROKE);
        myBlackStroke.setColor(MY_BLACK);
        myBlackStroke.setStrokeWidth(4);

        myBlackPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        myBlackPaint.setColor(MY_BLACK);
        myBlackPaint.setStrokeWidth(10);

        mySecondBlackPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mySecondBlackPaint.setColor(MY_BLACK);
        mySecondBlackPaint.setStrokeWidth(4);

        myWhitePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        myWhitePaint.setColor(MY_WHITE);

        myWhitePaint.setTextSize(getResources().getDimension(R.dimen._15sdp));
        myWhitePaint.setTextAlign(Paint.Align.CENTER);

        myBrownStroke.setStyle(Paint.Style.STROKE);
        myBrownStroke.setColor(MY_BROWN);
        myBrownStroke.setStrokeWidth(4);

        drawFleche();
    }

    public void drawFleche() {

        float flecheXMobile = (longueurFleche * (float) Math.cos(degree2radian(angle - 90))) + flecheXFixe;
        float flecheYMobile = (longueurFleche * (float) Math.sin(degree2radian(angle - 90))) + flecheYFixe;

        float remorqueXCointHautGauche = flecheXMobile - largeurRemorque / (float) 2;
        float remorqueYCointHautGauche = flecheYMobile - longueurRemorque;

        float remorqueXCointBasDroite = flecheXMobile + largeurRemorque / (float) 2;
        float remorqueYCointBasDroite = flecheYMobile;

        Canvas canvas = mySurfaceHolder.lockCanvas();
        if (canvas == null) {
            return;
        }

        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        canvas.drawColor(Color.WHITE);

        canvas.rotate(180, canvasSize*(float)0.5, canvasSize*(float)0.5);

        // Fleche
        canvas.drawLine(flecheXFixe, flecheYFixe, flecheXMobile, flecheYMobile, myBlackPaint);

        // Roue gauche
        canvas.drawRect(remorqueXCointHautGauche - canvasSize*5/100, remorqueYCointHautGauche + longueurRemorque*0.4f - canvasSize*10/100, remorqueXCointHautGauche,  remorqueYCointBasDroite - longueurRemorque*0.7f + canvasSize*10/100, mySecondBlackPaint);
        canvas.drawRect(remorqueXCointHautGauche - canvasSize*5/100, remorqueYCointHautGauche + longueurRemorque*0.4f - canvasSize*10/100, remorqueXCointHautGauche,  remorqueYCointBasDroite - longueurRemorque*0.7f + canvasSize*10/100, myBrownStroke);

        // Roue droite
        canvas.drawRect(remorqueXCointBasDroite, remorqueYCointBasDroite - longueurRemorque*0.7f + canvasSize*10/100, remorqueXCointBasDroite + canvasSize*5/100, remorqueYCointHautGauche + longueurRemorque*0.4f - canvasSize*10/100, mySecondBlackPaint);
        canvas.drawRect(remorqueXCointBasDroite, remorqueYCointBasDroite - longueurRemorque*0.7f + canvasSize*10/100, remorqueXCointBasDroite + canvasSize*5/100, remorqueYCointHautGauche + longueurRemorque*0.4f - canvasSize*10/100, myBrownStroke);

        // Remorque
        canvas.drawRect(remorqueXCointHautGauche, remorqueYCointHautGauche, remorqueXCointBasDroite, remorqueYCointBasDroite, myRedPaint);
        canvas.drawRect(remorqueXCointHautGauche, remorqueYCointHautGauche, remorqueXCointBasDroite, remorqueYCointBasDroite, myBlackStroke);

        canvas.rotate(-180, canvasSize*(float)0.5, canvasSize*(float)0.5);

        canvas.drawText(" " + formatDouble(angle) + "°", remorqueXCointBasDroite - largeurRemorque/2, remorqueYCointBasDroite - getResources().getDimension(R.dimen._50sdp), myWhitePaint);

        mySurfaceHolder.unlockCanvasAndPost(canvas);
    }

    public void setAngle(double angle) {

        this.angle = angle;

        if (angle > ConfigManager.model.BORNE_MAX_FLECHE)
            this.angle = ConfigManager.model.BORNE_MAX_FLECHE;

        if (angle < ConfigManager.model.BORNE_MIN_FLECHE)
            this.angle = ConfigManager.model.BORNE_MIN_FLECHE;

        drawFleche();
    }

    public double getAngle() {
        return this.angle;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawFleche();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
