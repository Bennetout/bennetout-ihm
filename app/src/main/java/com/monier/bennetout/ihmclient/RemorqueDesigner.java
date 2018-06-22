package com.monier.bennetout.ihmclient;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class RemorqueDesigner extends View {

    public static final int BORNE_MAX   = 45;
    public static final int BORNE_MIN   = 0;

    public static final int BORNE_MAX_BENNE   = 90;
    public static final int BORNE_MIN_BENNE   = 0;

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
    private RectF canvasBorder = new RectF();

    public RemorqueDesigner(Context context) {
        super(context);
        init();
    }

    public RemorqueDesigner(Context context, @Nullable AttributeSet attrs) {
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

        myWhitePaint.setTextSize(getResources().getDimension(R.dimen._10sdp));
        myWhitePaint.setTextAlign(Paint.Align.CENTER);

        myGrayPaint.setStyle(Paint.Style.FILL);
        myGrayPaint.setColor(MY_GRAY);
    }

    Path remorqueDraw = new Path();
    Path benneDraw = new Path();
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float canvasSize = canvas.getWidth();

        canvasBorder.set(1,1,canvasSize -1,canvasSize -1);
        canvas.drawRect(canvasBorder, myBorderPaint);

        canvas.rotate((float) -this.angle, canvasSize*22.5f/150, canvasSize*127.5f/150);

        // Remorque
//        canvas.drawRect(canvasSize*55/150, canvasSize*85/150, canvasSize*135/150, canvasSize*125/150, myRedPaint);
//        canvas.drawRect(canvasSize*55/150, canvasSize*85/150, canvasSize*135/150, canvasSize*125/150, myBlackStroke);
        remorqueDraw.moveTo(canvasSize*55/150, canvasSize*85/150);
        remorqueDraw.lineTo(canvasSize*135/150, canvasSize*85/150);
        remorqueDraw.lineTo(canvasSize*115/150, canvasSize*125/150);
        remorqueDraw.lineTo(canvasSize*55/150, canvasSize*125/150);
        remorqueDraw.lineTo(canvasSize*55/150, canvasSize*85/150);
        canvas.drawPath(remorqueDraw, myRedPaint);
        canvas.drawPath(remorqueDraw, myBlackStroke);

        // Fleche
        canvas.drawRect(canvasSize*20/150, canvasSize*125/150, canvasSize*67.5f/150, canvasSize*130/150, myBlackPaint);

        canvas.rotate((float) -angleBenne, canvasSize*135/150, canvasSize*85/150);
        // Benne
        benneDraw.moveTo(canvasSize*135/150, canvasSize*85/150);
        benneDraw.lineTo(canvasSize*135/150, canvasSize*125/150);
        benneDraw.lineTo(canvasSize*115/150, canvasSize*125/150);
        benneDraw.lineTo(canvasSize*135/150, canvasSize*85/150);
        canvas.drawPath(benneDraw, myBlackPaint);
        canvas.drawPath(benneDraw, myBlackStroke);
        canvas.rotate((float) angleBenne, canvasSize*135/150, canvasSize*85/150);

        canvas.rotate((float) (this.angle*2), canvasSize*67.5f/150, canvasSize*127.5f/150);

        // Fleche côté roue
        canvas.drawRect(canvasSize*65/150, canvasSize*125/150, canvasSize*110/150, canvasSize*130/150, myBlackPaint);

        // Roue
        canvas.drawCircle(canvasSize*105/150, canvasSize*127.5f/150, canvasSize*10/150, myBlackPaint);
        canvas.drawCircle(canvasSize*105/150, canvasSize*127.5f/150, canvasSize*2.5f/150, myWhitePaint);
        canvas.drawCircle(canvasSize*105/150, canvasSize*127.5f/150, canvasSize*10/150, myBrownStroke);
    }

    public void setAngle(double angle) {

        this.angle = angle;

        if (this.angle > BORNE_MAX)
            this.angle = BORNE_MAX;

        if (this.angle < BORNE_MIN)
            this.angle = BORNE_MIN;

        postInvalidate();
    }

    public void setAngleBenne(double angle) {

        this.angleBenne = angle;

        if (this.angleBenne > BORNE_MAX_BENNE)
            this.angleBenne = BORNE_MAX_BENNE;

        if (this.angleBenne < BORNE_MIN_BENNE)
            this.angleBenne = BORNE_MIN_BENNE;

        postInvalidate();
    }
}
