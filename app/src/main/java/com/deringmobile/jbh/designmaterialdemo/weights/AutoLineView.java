package com.deringmobile.jbh.designmaterialdemo.weights;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.os.MessageQueue;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.deringmobile.jbh.designmaterialdemo.util.LogUtil;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * Created by zbsdata on 2017/9/19.
 */

public class AutoLineView extends View {


    private Paint nomalPaint;
    private Paint autoPaint;


    public AutoLineView(Context context) {
        this(context , null);
    }

    public AutoLineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public AutoLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initPaint();
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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvasNomal(canvas);
        canvasAuto(canvas);
    }

    /**绘制默认的边框*/
    private void canvasNomal(Canvas canvas) {
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



    float autoProgess = 100;
    /**绘制可以流动的边框*/
    Path path=new Path();
    private void canvasAuto(final Canvas canvas) {
        path.moveTo(100,100);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (autoProgess < 400){
                    autoProgess+=0.1;
                    Observable.empty().subscribeOn(AndroidSchedulers.mainThread())
                           .doOnCompleted(new Action0() {
                               @Override
                               public void call() {
                                   path.lineTo(autoProgess,100);
                                   canvas.drawPath(path,autoPaint);
                                   LogUtil.showLogV("==tag=","aFloat:"+autoProgess);
                                   invalidate();
                                   try {
                                       Thread.sleep(10);
                                   } catch (InterruptedException e) {
                                       e.printStackTrace();
                                   }
                               }
                           }).subscribe();
                }
            }
        }).start();
    }
}
