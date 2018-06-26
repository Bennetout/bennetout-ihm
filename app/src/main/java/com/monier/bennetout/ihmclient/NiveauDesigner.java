package com.monier.bennetout.ihmclient;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import static com.monier.bennetout.ihmclient.utils.Utils.formatDouble;

public class NiveauDesigner extends View {

    private int MY_RED    = Color.parseColor("#800000");
    private int MY_BLACK    = Color.parseColor("#000000");
    private int MY_WHITE    = Color.parseColor("#ffffff");
    private int MY_GRAY    = Color.parseColor("#cccccc");
    private int MY_BROWN    = Color.parseColor("#663300");

    private Paint myRedPaint = new Paint(),
            myBlackPaint = new Paint(),
            myWhitePaint = new Paint(),
            myGrayPaint = new Paint(),
            myBorderPaint = new Paint();

    private Paint myBlackStroke = new Paint(),
            myRedStroke = new Paint(),
            myBrownStroke = new Paint();

    private double niveau = 0;
    RectF canvasBorder = new RectF();

    public NiveauDesigner(Context context) {
        super(context);
        init();
    }

    public NiveauDesigner(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        myBorderPaint.setStyle(Paint.Style.STROKE);
        myBorderPaint.setColor(MY_RED);
        myBorderPaint.setStrokeWidth(1);

        myRedPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        myRedPaint.setColor(MY_RED);

        myBlackStroke.setStyle(Paint.Style.STROKE);
        myBlackStroke.setColor(MY_BLACK);
        myBlackStroke.setStrokeWidth(4);

        myRedStroke.setStyle(Paint.Style.STROKE);
        myRedStroke.setColor(MY_RED);
        myRedStroke.setStrokeWidth(4);

        myBrownStroke.setStyle(Paint.Style.STROKE);
        myBrownStroke.setColor(MY_BROWN);
        myBrownStroke.setStrokeWidth(4);

        myBlackPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        myBlackPaint.setColor(MY_BLACK);

        myWhitePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        myWhitePaint.setColor(MY_WHITE);

        myWhitePaint.setTextSize(getResources().getDimension(R.dimen._15sdp));
        myWhitePaint.setTextAlign(Paint.Align.CENTER);

        myGrayPaint.setStyle(Paint.Style.FILL);
        myGrayPaint.setColor(MY_GRAY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float canvasSize = canvas.getWidth();

//        canvasBorder.set(1,1,canvasSize -1,canvasSize -1);
//        canvas.drawRect(canvasBorder, myBorderPaint);

        canvas.rotate((float) this.niveau, canvasSize*50/100, canvasSize*50/100);

        // Sol
        canvasBorder.set(canvasSize*10/100,canvasSize*35/100,canvasSize*90/100, canvasSize*90/100);
        canvas.drawArc(canvasBorder, 0, 180, true, myBlackStroke);
        canvas.drawArc(canvasBorder, 0, 180, true, myGrayPaint);

        // Porte
        canvas.drawRect(canvasSize*30/100, canvasSize*20/100, canvasSize*70/100, canvasSize*70/100, myBlackStroke);
        canvas.drawRect(canvasSize*30/100, canvasSize*20/100, canvasSize*70/100, canvasSize*70/100, myRedPaint);

        // Axes roues
        canvas.drawLine(canvasSize*30/100, canvasSize*72.5f/100, canvasSize*70/100, canvasSize*72.5f/100, myRedStroke);
        canvas.drawLine(canvasSize*50/100, canvasSize*70/100, canvasSize*50/100, canvasSize*72.5f/100, myRedStroke);

        // Roue Gauche
        canvas.drawRect(canvasSize*20/100, canvasSize*65/100, canvasSize*30/100, canvasSize*80/100, myBlackPaint);
        canvas.drawRect(canvasSize*20/100, canvasSize*65/100, canvasSize*30/100, canvasSize*80/100, myBrownStroke);

        // Roue Droite
        canvas.drawRect(canvasSize*70/100, canvasSize*65/100, canvasSize*80/100, canvasSize*80/100, myBlackPaint);
        canvas.drawRect(canvasSize*70/100, canvasSize*65/100, canvasSize*80/100, canvasSize*80/100, myBrownStroke);

        canvas.drawText(" " + formatDouble(niveau) + "°", canvasSize*50/100, canvasSize*60/100, myWhitePaint);
    }

    private double degree2radian(double degree) {
        return (Math.PI / (double) 180) * degree;
    }

    public void setNiveau(double niveau) {

        this.niveau = niveau;

        if (niveau > 10)
            this.niveau = 10;

        if (niveau < -10)
            this.niveau = -10;

        postInvalidate();
    }
}
