package com.monier.bennetout.ihmclient;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import static com.monier.bennetout.ihmclient.utils.Utils.formatDouble;

public class NiveauPainter extends SurfaceView implements SurfaceHolder.Callback {

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

    private float canvasSize;

    private SurfaceHolder mySurfaceHolder;

    public NiveauPainter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NiveauPainter(Context context) {
        super(context);
        init();
    }

    private void init() {

        mySurfaceHolder = getHolder();
        mySurfaceHolder.addCallback(this);

        canvasSize = getResources().getDimension(R.dimen._130sdp);
        canvasBorder.set(canvasSize*10/100,canvasSize*35/100,canvasSize*90/100, canvasSize*90/100);

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

    public void drawNiveau() {

        Canvas canvas = mySurfaceHolder.lockCanvas();
        if (canvas == null) {
            return;
        }

        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        canvas.drawColor(Color.WHITE);

        canvas.rotate((float) this.niveau, canvasSize*50/100, canvasSize*50/100);

        // Sol
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

        mySurfaceHolder.unlockCanvasAndPost(canvas);
    }

    public void setNiveau(double niveau) {

        this.niveau = niveau;

        if (niveau > 10)
            this.niveau = 10;

        if (niveau < -10)
            this.niveau = -10;

        drawNiveau();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawNiveau();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
