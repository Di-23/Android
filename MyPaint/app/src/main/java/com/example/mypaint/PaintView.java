package com.example.mypaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;

public class PaintView extends View {
    public static int BRUSH_SIZE = 1;
    public static final int DEFAULT_COLOR = Color.BLACK;
    public static final int DEFAULT_BG_COLOR = Color.WHITE;
    private static final float TOUCH_TOLERANCE = 4;

    private int prevSelectedColor = DEFAULT_COLOR;

    private float mX, mY;

    private int currentColor;
    private int backgroundColor = DEFAULT_BG_COLOR;
    private int strokeWidth;

    private Path mPath;
    private Paint mPaint;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);

    private ArrayList<Draw> pathList = new ArrayList<>();
    private ArrayList<Draw> undo = new ArrayList<>();

    public PaintView(Context context) {
        super(context, null);
    }

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(DEFAULT_COLOR);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setXfermode(null);
        mPaint.setAlpha(0xff);
    }

    public void initialise(DisplayMetrics displayMetrics) {

        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        currentColor = DEFAULT_COLOR;
        strokeWidth = BRUSH_SIZE;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mCanvas.drawColor(backgroundColor); // WRONG

        for (Draw draw : pathList) {

            mPaint.setColor(draw.color); // WRONG
            mPaint.setStrokeWidth(draw.strokeWidth);
            mPaint.setMaskFilter(null);

            mCanvas.drawPath(draw.path, mPaint);
        }
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
    }

    private void touchStart(float x, float y) {

        mPath = new Path();
        mPath.moveTo(x, y);

        Draw draw = new Draw(currentColor, strokeWidth, mPath);
        pathList.add(draw);

        mX = x;
        mY = y;

    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, x, y);
            mX = x;
            mY = y;
        }
    }

    private void touchUp() {
        mPath.lineTo(mX, mY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                Log.d("TOUCH EVENT:", "ACTION_DOWN");
                touchStart(x, y);
                invalidate();
                break;
//            case MotionEvent.ACTION_UP:
//                Log.d("TOUCH EVENT:", "ACTION_UP");
//                touchUp();
//                invalidate();
//                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("TOUCH EVENT:", "ACTION_MOVE");
                touchMove(x, y);
                invalidate();
                break;
        }
        return true;
    }

    public void clear() {
        backgroundColor = DEFAULT_BG_COLOR;

        pathList.clear();
        invalidate();

    }

    public void undo() {
        if (pathList.size() > 0) {
            undo.add(pathList.remove(pathList.size() - 1));
            invalidate(); // add
        }
    }

    public void redo() {
        if (undo.size() > 0) {
            pathList.add(undo.remove(undo.size() - 1));
            invalidate(); // add
        }
    }

    public void setStrokeWidth(int width) {
        strokeWidth = width;
    }

    public void setColor(int color) {
        prevSelectedColor = currentColor;
        currentColor = color;
    }

    public void setPrevBrushColor() {
        currentColor = prevSelectedColor;
    }


    public Bitmap getmBitmap() {
        return mBitmap;
    }
}