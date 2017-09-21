package com.deringmobile.jbh.designmaterialdemo.weights;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Picture;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zbsdata on 2017/9/6.
 */

public class MyView  extends View{

    /**限制滑动的最大距离*/
    private static final int MOVE_MAX=500;
    /**用于记录上一次滑动Y距离*/
    private float lastMoveY = 0;
    /**记录上一次滑动的X距离*/
    private float lastMoveX = 0;
    /**滑动的进度*/
    private float progress = 0;
    /**滑动的的变化率*/
    private float moveScale=1;
    /**手指移动的轨迹*/
    private Path path;
    /**画笔*/
    private  Paint paint;

    float x,y;

    private List<PointF> pts=new ArrayList<>();


    public MyView(Context context) {
        this(context ,null);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setDither(true);
        paint.setAntiAlias(true);
        paint.setColor(Color.YELLOW);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(10);
        path=new Path();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:


                lastMoveX = event.getX();
                lastMoveY = event.getY();

                path.moveTo(lastMoveX,lastMoveY);
                break;
            case MotionEvent.ACTION_MOVE:
                float tempMoveY=event.getY();
                float tempMoveX=event.getX();
                 x=  (tempMoveX-lastMoveX);
                 y=  (tempMoveY-lastMoveY);
                /**如果滑动的最小距离大于20*/
                if(y > 20){
                    if(y>MOVE_MAX){
                        moveScale = 1 ;
                    }else {
                        moveScale = (float) ((y*1.0)/MOVE_MAX);
                    }
                    progress=moveScale*MOVE_MAX;
                    Log.v("==progress=",String.valueOf(progress));
                    Log.v("==moveScale=",String.valueOf(moveScale));
                }
                path.lineTo(tempMoveX,tempMoveY);
//                lastMoveX=tempMoveX;
//                lastMoveY=tempMoveY;
                pts.add(new PointF((int)lastMoveX,(int)lastMoveY));
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                progress = 0;
                moveScale = 0;
                Log.v("==progress=",String.valueOf(progress));
                Log.v("==moveScale=",String.valueOf(moveScale));
//                path=null;
                lastMoveX=0;
                lastMoveY=0;
                x=0;
                y=0;
                pts.clear();
                break;

            default:
                break;
        }
        postInvalidate();
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        switch (type){
            case ARC:
                drawArc(canvas);
                break;

            case Bitmap:

                break;

            case Rect:

                break;

            case RoundRect:
                canvasRoundRect(canvas);
                break;

            case Text:

                break;

            case TextOnPath:

                break;

            case TextRun:

                break;

            case Line:
                canvasLine(canvas);
                break;
            case Path:
                canvasPath(canvas);
                break;
            case BitmapMesh:

                break;
            case Oval:
                canvasOval(canvas);
                break;
            case Picture:
                canvasPicture(canvas);
                break;
            case Point:
                canvasPoint(canvas);
                canvasRoundRect(canvas);
                break;
            case Vertices:

                break;
        }

//        canvas.drawArc();
//        canvas.drawBitmap();
//        canvas.drawRect();
//        canvas.drawCircle();
//        canvas.drawText();
//        canvas.drawARGB();
//        canvas.drawLine();
//        canvas.drawPath();
//        canvas.drawTextOnPath();
//        canvas.drawBitmapMesh();
//        canvas.drawOval();
//        canvas.drawPicture();
//        canvas.drawPoint();
//        canvas.drawRoundRect();
//        canvas.drawTextRun();
//        canvas.drawVertices();
    }


    /**
     * 绘制圆角矩形
     * @param canvas
     */
    private void canvasRoundRect(Canvas canvas) {
        RectF rf=new RectF(0,0,300,500);
        canvas.drawRoundRect(rf,getMeasuredWidth()/2,getMeasuredHeight()/2,paint);
    }


    /**
     * 二阶贝塞尔曲线
     * @param canvas
     */
    private void canvasLine(Canvas canvas) {
        float startX = 0,  startY = 0,  stopX = 0,  stopY = 0;
        canvas.drawLine(startX,  startY,  stopX,  stopY,paint);
    }


    /**
     *
     * 绘制椭圆
     * @param canvas
     */
    private void canvasOval(Canvas canvas) {


    }


    /**
     *  绘制图片
     * @param canvas
     */
    private void canvasPicture(Canvas canvas) {


    }

    /**
     *
     * @param canvas
     */
    private void canvasPoint(Canvas canvas) {
        PointF p0 = new PointF(0 ,100);
        PointF p2 = new PointF(getMeasuredWidth(), 100);
        canvas.drawPoint(p0.x,p0.y,paint);
        canvas.drawPoint(p2.x,p2.y,paint);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        Path p=new Path();
        p.moveTo(p0.x,p0.y);
        p.cubicTo(p0.x,p0.y,x,y,p2.x,p2.y);
        canvas.drawPath(p,paint);
    }

    /**
     * @param t
     * @param p0
     * @param p1
     * @param p2
     * @return
     */
    public PointF getBezierPoint(float t , PointF p0,PointF p1,PointF p2){
        PointF p=new PointF();
        p.x=(1-t)*(1-t)*p0.x + 2*t*(1-t)*p1.x + t*t*p2.x;
        p.y=(1-t)*(1-t)*p0.y + 2*t*(1-t)*p1.y + t*t*p2.y;
        return p;
    }

    /**
     * @param canvas
     */
    private void canvasPath(Canvas canvas) {
        if(path!=null)
            canvas.drawPath(path,paint);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void drawArc(Canvas canvas) {
        try {
            canvas.drawArc(0,-progress,getMeasuredWidth(),progress,0,180,true,paint);
        }catch (NoSuchMethodError error){
            error.printStackTrace();
        }
    }

    Type type = Type.Point;
    public void setType(Type type) {
        this.type = type;
    }

    public enum Type{
        ARC,Bitmap,Rect,RoundRect,Text,TextOnPath,TextRun,Line,Path,BitmapMesh,Oval,Picture,Point,Vertices
    }
}
