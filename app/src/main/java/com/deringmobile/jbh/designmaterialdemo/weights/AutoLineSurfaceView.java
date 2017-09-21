package com.deringmobile.jbh.designmaterialdemo.weights;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.deringmobile.jbh.designmaterialdemo.util.LogUtil;

/**
 * Created by zbsdata on 2017/9/20.
 */

public class AutoLineSurfaceView extends SurfaceView implements SurfaceHolder.Callback ,Runnable  {

    /***/
    private SurfaceHolder holder;
    /***/
    private Canvas canvas;

    /**控制开关*/
    private Boolean isRunning;

    private Paint nomalPaint;
    private Paint autoPaint;


    private Thread t;


    public AutoLineSurfaceView(Context context) {
        this(context,null);
    }

    public AutoLineSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoLineSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        holder=getHolder();
        holder.addCallback(this);

        //设置可获得焦点
        setFocusable(true);
        setFocusableInTouchMode(true);
        //设置常亮
        this.setKeepScreenOn(true);

    }

    private void initPaint() {

        nomalPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        nomalPaint.setAntiAlias(true);
        nomalPaint.setDither(true);
        nomalPaint.setStrokeCap(Paint.Cap.ROUND);
        nomalPaint.setStrokeJoin(Paint.Join.ROUND);
        nomalPaint.setColor(Color.BLUE);
        nomalPaint.setStyle(Paint.Style.STROKE);
        nomalPaint.setStrokeWidth(10);


        autoPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        autoPaint.setAntiAlias(true);
        autoPaint.setDither(true);
        autoPaint.setStrokeCap(Paint.Cap.ROUND);
        autoPaint.setStrokeJoin(Paint.Join.ROUND);
        autoPaint.setColor(Color.YELLOW);
        autoPaint.setStyle(Paint.Style.STROKE);
        autoPaint.setStrokeWidth(10);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isRunning=true;
        t =new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        while (isRunning){
            drawCanvas();
        }
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRunning=false;
    }


    private void drawCanvas() {
        try {
            canvas = holder.lockCanvas();
            if(canvas!=null){
                canvasNormal();


                canvasAuto();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                holder.unlockCanvasAndPost(canvas);
            }catch (IllegalStateException e){
                e.printStackTrace();
            }
        }
    }




    /**绘制默认的边框*/
    private void canvasNormal() {
        if (canvas!=null){
            Path p=new Path();
            p.moveTo(100,100);
            p.lineTo(400,100);
            p.lineTo(450,150);
            p.lineTo(450,300);
            p.lineTo(450,300);
            p.lineTo(100,300);
            p.lineTo(100,100);
            canvas.drawPath(p,nomalPaint);

            Path  p1=new Path();
            p1.moveTo(450,200);
            p1.lineTo(500,200);
            p1.lineTo(500,400);
            canvas.drawPath(p1,nomalPaint);

            canvas.drawCircle(500,400,10,nomalPaint);
        }
    }


    /**绘制可以流动的上边*/
    Path path=new Path();
    private int progress = 100;
    private void canvasAuto() {
        path.moveTo(100,100);
        progress+=7;
        if(progress>400){
            progress=400;
            canvasAuto1();
        }else {
            path.lineTo(progress,100);
            canvas.drawPath(path,autoPaint);
        }
    }


    /**绘制右边的斜边*/
    int progressH=400;
    int progressVertical=100;
    private void canvasAuto1() {
//        path.moveTo(400,100);
        progressH+=7;
        progressVertical+=7;

        if(progressH > 150 ){
            canvasAuto2();
        }else {
            path.lineTo(progressH,progressVertical);
            canvas.drawPath(path,autoPaint);
        }
    }


    private void canvasAuto2() {


    }
}
