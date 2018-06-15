package com.monier.bennetout.ihmclient;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import static com.monier.bennetout.ihmclient.utils.Utils.formatDouble;

public class FlecheDesigner extends View {

    private int MY_RED    = Color.parseColor("#800000");
    private int MY_BLACK    = Color.parseColor("#000000");
    private int MY_WHITE    = Color.parseColor("#ffffff");

    private Paint myRedPaint = new Paint(), myBlackPaint = new Paint(), myWhitePaint = new Paint();
    private Paint myBlackStroke = new Paint();
    private double angle = 0;

    public FlecheDesigner(Context context) {
        super(context);
        init();
    }

    public FlecheDesigner(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        myRedPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        myRedPaint.setColor(MY_RED);

        myBlackStroke.setStyle(Paint.Style.STROKE);
        myBlackStroke.setColor(MY_BLACK);
        myBlackStroke.setStrokeWidth(4);

        myBlackPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        myBlackPaint.setColor(MY_BLACK);
        myBlackPaint.setStrokeWidth(10);

        myWhitePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        myWhitePaint.setColor(MY_WHITE);

        myWhitePaint.setTextSize(getResources().getDimension(R.dimen._15sdp));
        myWhitePaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int canvasSize = canvas.getHeight();

        float longueurFleche = canvasSize / (float)4;
        float flecheXFixe = canvasSize / (float)2;
        float flecheYFixe = canvasSize;

        float flecheXMobile =  (longueurFleche * (float) Math.cos(degree2radian(angle - 90))) + flecheXFixe;
        float flecheYMobile =  (longueurFleche * (float) Math.sin(degree2radian(angle - 90))) + flecheYFixe;

        float longueurRemorque =  (float) canvasSize * (float) 0.6;
        float largeurRemorque =  canvasSize / (float)2.5;
        float remorqueXCointHautGauche = flecheXMobile - largeurRemorque / (float)2;
        float remorqueYCointHautGauche = flecheYMobile - longueurRemorque;

        float remorqueXCointBasDroite = flecheXMobile + largeurRemorque / (float)2;
        float remorqueYCointBasDroite = flecheYMobile;

        canvas.drawLine(flecheXFixe, flecheYFixe, flecheXMobile, flecheYMobile, myBlackPaint);
        canvas.drawRect(remorqueXCointHautGauche, remorqueYCointHautGauche, remorqueXCointBasDroite, remorqueYCointBasDroite, myRedPaint);
        canvas.drawRect(remorqueXCointHautGauche, remorqueYCointHautGauche, remorqueXCointBasDroite, remorqueYCointBasDroite, myBlackStroke);

        canvas.drawText(" " + formatDouble(angle) + "°", remorqueXCointBasDroite - largeurRemorque/2, remorqueYCointBasDroite - getResources().getDimension(R.dimen._5sdp), myWhitePaint);
    }

    private double degree2radian(double degree) {
        return (Math.PI / (double) 180) * degree;
    }

    public void setAngle(double angle) {

        this.angle = angle;

        if (angle > 45)
            this.angle = 45;

        if (angle < -45)
            this.angle = -45;

        postInvalidate();
    }
}
