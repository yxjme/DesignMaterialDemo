package com.deringmobile.jbh.designmaterialdemo.weights;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * Created by zbsdata on 2017/9/18.
 */

public class TextViewScrollerVertical extends ViewGroup{


    /**记录所有的子视图*/
    private List<View> views=new ArrayList<>();
    /**周期性的循环展示*/
    private ExecutorService executorService;

    /**记录当前显示的index*/
    private int index=0;

    public TextViewScrollerVertical(Context context) {
        this(context,null);
    }

    public TextViewScrollerVertical(Context context, AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public TextViewScrollerVertical(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);

        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);


        int width= 0 ;
        int height = 0;

        int countChild=getChildCount();

        if(countChild>0){
            for (int i=0;i<countChild;i++){
                View child=getChildAt(i);
                measureChild(child,widthMeasureSpec,widthMeasureSpec);
                if(widthMode == MeasureSpec.EXACTLY){ //具体值 或者 控件宽
                    width=widthSize;
                }else if (widthMode==MeasureSpec.AT_MOST){ //wrap_parent
                    width=Math.min(child.getMeasuredWidth(),widthSize);
                }

                if(heightMode == MeasureSpec.EXACTLY){
                    height = heightSize;
                }else if (heightMode==MeasureSpec.AT_MOST){
                    height = Math.min(child.getMeasuredHeight() , heightSize);
                }else {
                }
            }
        }
        setMeasuredDimension(width,height);
    }




    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int childCount=getChildCount();
        views.clear();
        if(childCount>0){
            for (int i=0 ; i<childCount ; i++){
                View child=getChildAt(i);
                int childWidth=child.getMeasuredWidth();
                int childHeight=child.getMeasuredHeight();
                child.layout(0,0,childWidth,childHeight);
                views.add(child);
            }
        }
    }



    Timer timer=new Timer();
    TimerTask timerTask=new TimerTask() {
        @Override
        public void run() {
            try {
                Observable.empty().subscribeOn(AndroidSchedulers.mainThread())
                        .doOnCompleted(new Action0() {
                            @Override
                            public void call() {
                                for (int  i = 0 ; i<views.size();i++ ){
                                    views.get(i).setVisibility(GONE);
                                }
                            }
                        }).subscribe();
                if (++index>= views.size()){
                    index = 0;
                }
                final View child = views.get(index);
                int childWidth=child.getMeasuredWidth();
                int childHeight=child.getMeasuredHeight();
                measureChild(child,childWidth,childHeight);
                child.layout(0,0,childWidth,childHeight);
                Observable.empty()
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .doOnCompleted(new Action0() {

                    @Override
                    public void call() {
                        child.setVisibility(VISIBLE);
                        ObjectAnimator.ofFloat(child,"scaleX" , 0 ,1f).setDuration(500).start();
                        ObjectAnimator.ofFloat(child, "scaleY" , 0 ,1f).setDuration(500).start();
                    }
                }).subscribe();

            }catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }
        }
    };

    public void set(){
        timer.schedule(timerTask,10,2000);
    }
}
