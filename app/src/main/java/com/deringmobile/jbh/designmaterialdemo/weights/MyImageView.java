package com.deringmobile.jbh.designmaterialdemo.weights;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.deringmobile.jbh.designmaterialdemo.R;

/**
 * Created by zbsdata on 2017/9/12.
 */

public class MyImageView extends View {


    private Bitmap sourceBitmap;
    private int sourceBitmapWidth;
    private int sourceBitmapheight;

    private Matrix matrix;


    /**获取屏幕的宽高*/
    int withParent;
    int heightParent;



    /**记录控件的中心点*/
    private PointF mPointF=new PointF();


    private Paint  mPaint;

    public MyImageView(Context context) {
        this(context , null);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs) {
        this (context, attrs , 0);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        matrix=new Matrix();
        sourceBitmap= BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        sourceBitmapWidth = sourceBitmap.getWidth();
        sourceBitmapheight = sourceBitmap.getHeight();

        DisplayMetrics dis = context.getResources().getDisplayMetrics();
        withParent=dis.widthPixels;
        heightParent=dis.heightPixels;

        initPaint();


        setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                matrix.postScale(3,3);
                return true;
            }
        });
    }

    private void initPaint() {
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(20);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int heightSize=MeasureSpec.getSize(widthMeasureSpec);
        int heightMode=MeasureSpec.getMode(widthMeasureSpec);

        int width = 0;
        int height = 0;

        if(widthMode==MeasureSpec.EXACTLY){
            width=widthSize;
        }else if(widthMode==MeasureSpec.AT_MOST){
            width = Math.min(withParent,sourceBitmapWidth);
        }

        if(heightMode==MeasureSpec.EXACTLY){
            height=heightSize;
        }else if(heightMode==MeasureSpec.AT_MOST){
            height = Math.min(heightParent,sourceBitmapheight) ;
        }

        mPointF.x=getMeasuredWidth()/2 - sourceBitmapWidth/2;
        mPointF.y=getMeasuredHeight()/2 - sourceBitmapheight/2;
        /**初始化显示图片显示的位置*/
        matrix.postTranslate(mPointF.x/2,mPointF.y/2);

        setMeasuredDimension(width,height);
    }

    double oldRotation = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int dx= 0;
        int dy = 0;
        int action=event.getActionMasked();
        int pointCount=event.getPointerCount();

        switch (action){

            case MotionEvent.ACTION_POINTER_DOWN:
                oldRotation= (int) getAngle(event);
                break;

            case MotionEvent.ACTION_DOWN:
                dx= (int) event.getX();
                dy= (int) event.getY();
            case MotionEvent.ACTION_MOVE:
                int tempX= (int) (event.getX()-dx);
                int tempY= (int) (event.getY()-dy);
                mPointF.x=event.getX();
                mPointF.y=event.getY();
                if(pointCount>1){
                    matrix.postTranslate( mPointF.x, mPointF.y);
//                    matrix.postRotate((float) (getAngle(event)-oldRotation)) ;
                }
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mPointF.x=getMeasuredWidth()/2 - sourceBitmapWidth/2;
                mPointF.y=getMeasuredHeight()/2 - sourceBitmapheight/2;
                break;
            default:
                break;
        }

        postInvalidate();
        return true;

    }

    private double getAngle(MotionEvent event) {
        float dx= (event.getX(0)-event.getX(1));
        float dy= (event.getY(0) - event.getY(1));
        double angle = Math.toDegrees(Math.atan2(dy, dx));
        return angle;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(sourceBitmap==null)
            return;
//        canvas.drawBitmap(sourceBitmap,mPointF.x ,mPointF.y,null);
        canvas.drawBitmap(sourceBitmap,matrix,null);
    }
}
