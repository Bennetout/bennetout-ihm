package com.monier.bennetout.ihmclient;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.monier.bennetout.ihmclient.configuration.ConfigManager;

import static com.monier.bennetout.ihmclient.utils.Utils.formatDouble;

public class RemorquePainter extends SurfaceView implements SurfaceHolder.Callback {

    public static final int CANVAS_SIZE_REF   = 160;

    private int MY_RED    = Color.parseColor("#800000");
    private int MY_BLACK    = Color.parseColor("#000000");
    private int MY_WHITE    = Color.parseColor("#ffffff");
    private int MY_GRAY    = Color.parseColor("#cccccc");
    private int MY_BROWN    = Color.parseColor("#663300");
    private double angle = 0;
    private double angleBenne = 0;

    private Paint myRedPaint = new Paint(),
            myBlackPaint = new Paint(),
            myWhitePaint = new Paint(),
            myGrayPaint = new Paint(),
            myBorderPaint = new Paint();

    private Paint myBlackStroke = new Paint(),
            myRedStroke = new Paint(),
            myBrownStroke = new Paint();
//    private RectF canvasBorder = new RectF();

    Path remorqueDraw = new Path();
    Path benneDraw = new Path();

    private float canvasSize;

    private SurfaceHolder mySurfaceHolder;

    public RemorquePainter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RemorquePainter(Context context) {
        super(context);
        init();
    }

    void init() {

        mySurfaceHolder = getHolder();
        mySurfaceHolder.addCallback(this);

        canvasSize = getResources().getDimension(R.dimen._130sdp);

        remorqueDraw.moveTo(canvasSize*40/CANVAS_SIZE_REF, canvasSize*110/CANVAS_SIZE_REF);
        remorqueDraw.lineTo(canvasSize*120/CANVAS_SIZE_REF, canvasSize*110/CANVAS_SIZE_REF);
        remorqueDraw.lineTo(canvasSize*100/CANVAS_SIZE_REF, canvasSize*145/CANVAS_SIZE_REF);
        remorqueDraw.lineTo(canvasSize*40/CANVAS_SIZE_REF, canvasSize*145/CANVAS_SIZE_REF);
        remorqueDraw.lineTo(canvasSize*40/CANVAS_SIZE_REF, canvasSize*110/CANVAS_SIZE_REF);

        benneDraw.moveTo(canvasSize*120/CANVAS_SIZE_REF, canvasSize*110/CANVAS_SIZE_REF);
        benneDraw.lineTo(canvasSize*120/CANVAS_SIZE_REF, canvasSize*145/CANVAS_SIZE_REF);
        benneDraw.lineTo(canvasSize*100/CANVAS_SIZE_REF, canvasSize*145/CANVAS_SIZE_REF);
        benneDraw.lineTo(canvasSize*120/CANVAS_SIZE_REF, canvasSize*110/CANVAS_SIZE_REF);

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

        myBlackPaint.setTextSize(getResources().getDimension(R.dimen._15sdp));
        myBlackPaint.setTextAlign(Paint.Align.CENTER);

        myWhitePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        myWhitePaint.setColor(MY_WHITE);

        myWhitePaint.setTextSize(getResources().getDimension(R.dimen._15sdp));
        myWhitePaint.setTextAlign(Paint.Align.CENTER);

        myGrayPaint.setStyle(Paint.Style.FILL);
        myGrayPaint.setColor(MY_GRAY);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        Canvas canvas = mySurfaceHolder.lockCanvas();
        if (canvas == null) {
            return;
        }

        // Remorque
        canvas.drawPath(remorqueDraw, myRedPaint);
        canvas.drawPath(remorqueDraw, myBlackStroke);

        // Text remorque
        canvas.drawText(" " + formatDouble(angle) + "°",canvasSize*65/CANVAS_SIZE_REF, canvasSize*135/CANVAS_SIZE_REF, myWhitePaint);

        // Text benne
        canvas.drawText(" " + formatDouble(angleBenne) + "°",canvasSize*122/CANVAS_SIZE_REF, canvasSize*105/CANVAS_SIZE_REF, myBlackPaint);

        // Fleche
        canvas.drawRect(canvasSize*5/CANVAS_SIZE_REF, canvasSize*145/CANVAS_SIZE_REF, canvasSize*52.5f/CANVAS_SIZE_REF, canvasSize*150/CANVAS_SIZE_REF, myBlackPaint);

        // Benne
        canvas.drawPath(benneDraw, myBlackPaint);
        canvas.drawPath(benneDraw, myBlackStroke);

        // Fleche côté roue
        canvas.drawRect(canvasSize*50/CANVAS_SIZE_REF, canvasSize*145/CANVAS_SIZE_REF, canvasSize*95/CANVAS_SIZE_REF, canvasSize*150/CANVAS_SIZE_REF, myBlackPaint);

        // Roue
        canvas.drawCircle(canvasSize*90/CANVAS_SIZE_REF, canvasSize*147.5f/CANVAS_SIZE_REF, canvasSize*10/CANVAS_SIZE_REF, myBlackPaint);
        canvas.drawCircle(canvasSize*90/CANVAS_SIZE_REF, canvasSize*147.5f/CANVAS_SIZE_REF, canvasSize*2.5f/CANVAS_SIZE_REF, myWhitePaint);
        canvas.drawCircle(canvasSize*90/CANVAS_SIZE_REF, canvasSize*147.5f/CANVAS_SIZE_REF, canvasSize*10/CANVAS_SIZE_REF, myBrownStroke);

        mySurfaceHolder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    public void drawRemorque() {

        Canvas canvas = mySurfaceHolder.lockCanvas();
        if (canvas == null) {
            return;
        }

        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        canvas.drawColor(Color.WHITE);

        canvas.rotate((float) -this.angle, canvasSize*7.5f/CANVAS_SIZE_REF, canvasSize*147.5f/CANVAS_SIZE_REF);

        // Remorque
        canvas.drawPath(remorqueDraw, myRedPaint);
        canvas.drawPath(remorqueDraw, myBlackStroke);

        // Text remorque
        canvas.drawText(" " + formatDouble(angle) + "°",canvasSize*65/CANVAS_SIZE_REF, canvasSize*135/CANVAS_SIZE_REF, myWhitePaint);

        // Text benne
        canvas.drawText(" " + formatDouble(angleBenne) + "°",canvasSize*122/CANVAS_SIZE_REF, canvasSize*105/CANVAS_SIZE_REF, myBlackPaint);

        // Fleche
        canvas.drawRect(canvasSize*5/CANVAS_SIZE_REF, canvasSize*145/CANVAS_SIZE_REF, canvasSize*52.5f/CANVAS_SIZE_REF, canvasSize*150/CANVAS_SIZE_REF, myBlackPaint);

        canvas.rotate((float) -angleBenne, canvasSize*120/CANVAS_SIZE_REF, canvasSize*110/CANVAS_SIZE_REF);

        // Benne
        canvas.drawPath(benneDraw, myBlackPaint);
        canvas.drawPath(benneDraw, myBlackStroke);

        canvas.rotate((float) angleBenne, canvasSize*120/CANVAS_SIZE_REF, canvasSize*110/CANVAS_SIZE_REF);

        canvas.rotate((float) (this.angle*2), canvasSize*52.5f/CANVAS_SIZE_REF, canvasSize*147.5f/CANVAS_SIZE_REF);

        // Fleche côté roue
        canvas.drawRect(canvasSize*50/CANVAS_SIZE_REF, canvasSize*145/CANVAS_SIZE_REF, canvasSize*95/CANVAS_SIZE_REF, canvasSize*150/CANVAS_SIZE_REF, myBlackPaint);

        // Roue
        canvas.drawCircle(canvasSize*90/CANVAS_SIZE_REF, canvasSize*147.5f/CANVAS_SIZE_REF, canvasSize*10/CANVAS_SIZE_REF, myBlackPaint);
        canvas.drawCircle(canvasSize*90/CANVAS_SIZE_REF, canvasSize*147.5f/CANVAS_SIZE_REF, canvasSize*2.5f/CANVAS_SIZE_REF, myWhitePaint);
        canvas.drawCircle(canvasSize*90/CANVAS_SIZE_REF, canvasSize*147.5f/CANVAS_SIZE_REF, canvasSize*10/CANVAS_SIZE_REF, myBrownStroke);

        mySurfaceHolder.unlockCanvasAndPost(canvas);
    }

    public void setAngle(double angle, double angleBenne) {

        this.angle = angle;

        if (this.angle > ConfigManager.model.BORNE_MAX_LEVAGE)
            this.angle = ConfigManager.model.BORNE_MAX_LEVAGE;

        if (this.angle < ConfigManager.model.BORNE_MIN_LEVAGE)
            this.angle = ConfigManager.model.BORNE_MIN_LEVAGE;

        this.angleBenne = angleBenne;

        if (this.angleBenne > ConfigManager.model.BORNE_MAX_PORTE)
            this.angleBenne = ConfigManager.model.BORNE_MAX_PORTE;

        if (this.angleBenne < ConfigManager.model.BORNE_MIN_PORTE)
            this.angleBenne = ConfigManager.model.BORNE_MIN_PORTE;


        drawRemorque();
    }
}
