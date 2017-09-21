package com.deringmobile.jbh.designmaterialdemo.weights;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.deringmobile.jbh.designmaterialdemo.R;

/**
 * Created by zbsdata on 2017/9/4.
 */

public class NavBar extends ViewGroup implements View.OnClickListener{

    private TypedArray array;
    private Context context;
    private TextView tv_back,tv_Title,tv_right;
    private String leftText;
    private String rightText;
    private String titleText;

    private int textColor;
    private int textSize=20;
    private int backGroundColor;

    public void setLeftText(String leftText) {
        this.leftText = leftText;
    }

    public void setRightText(String rightText) {
        this.rightText = rightText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public void setBackGroundColor(int backGroundColor) {
        this.backGroundColor = backGroundColor;
    }

    public NavBar(Context context) {
        this(context,null);
    }
    public NavBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public NavBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
         array=context.obtainStyledAttributes(attrs, R.styleable.NavBar,0,defStyleAttr);
        int count=array.getIndexCount();
        for (int i=0 ; i<count; i++){
            int attr=array.getIndex(i);
            switch (attr){
                case R.styleable.NavBar_leftText:
                    leftText=array.getString(attr);
                    break;
                case R.styleable.NavBar_backGroundColor:
                    backGroundColor=array.getColor(attr,Color.BLACK);
                    break;
                case R.styleable.NavBar_rightText:
                    rightText=array.getString(attr);
                    break;
                case R.styleable.NavBar_titleText:
                    titleText=array.getString(attr);
                    break;
                case R.styleable.NavBar_textSize:
                    textSize= (int) array.getDimension(attr, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, context.getResources().getDisplayMetrics()));
                    break;
                default:
                    break;
            }
        }
        /**释放内存*/
        array.recycle();
        setBackgroundColor(backGroundColor);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec,heightMeasureSpec);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        tv_back=new TextView(context);
        tv_back.setText(leftText);
//        tv_back.setBackgroundColor(Color.YELLOW);
        tv_back.setHeight(getMeasuredHeight());
        tv_back.setGravity(Gravity.CENTER);
        tv_back.setTextColor(Color.BLACK);
        tv_back.setPadding(10,10,10,10);
        tv_back.setTextSize(textSize);
        tv_back.setMinimumWidth(110);
        tv_back.setOnClickListener(this);
        addView(tv_back);

        tv_Title=new TextView(context);
        tv_Title.setText(titleText);
        tv_Title.setBackgroundColor(Color.YELLOW);
        tv_Title.setHeight(getMeasuredHeight());
        tv_Title.setGravity(Gravity.CENTER);
        tv_Title.setTextColor(Color.BLACK);
        tv_Title.setPadding(10,10,10,10);
        tv_Title.setTextSize(textSize);
        tv_Title.setOnClickListener(this);
        addView(tv_Title);

        tv_right= new TextView(context);
        tv_right.setText(rightText);
//        tv_right.setBackgroundColor(Color.YELLOW);
        tv_right.setHeight(getMeasuredHeight());
        tv_right.setGravity(Gravity.CENTER);
        tv_right.setTextColor(Color.BLACK);
        tv_right.setPadding(10,10,10,10);
        tv_right.setTextSize(textSize);
        tv_right.setMinimumWidth(110);
        tv_right.setOnClickListener(this);
        addView(tv_right);


        int count1=getChildCount();
        if(count1<0){
            throw new IllegalArgumentException("sdf asdfsa ");
        } else {
            for (int i = 0 ; i < count1 ; i++){
                View child=getChildAt(i);
                child.measure(0,0);
                if(child!=null&&i==0){
                    child.layout(0,0,child.getMeasuredWidth(),child.getMeasuredHeight());
                }
                if(child!=null&&i==1){
                    int left=getMeasuredWidth()/2-child.getMeasuredWidth()/2;
                    int right=getMeasuredWidth()/2+child.getMeasuredWidth()/2;
                    child.layout(left,0,right,child.getMeasuredHeight());
                }
                if(child!=null&&i==2){
                    child.layout(getMeasuredWidth()-child.getMeasuredWidth(),0,getMeasuredWidth(),child.getMeasuredHeight());
                }
            }
        }
    }


    @Override
    public void onClick(View v) {
        if(v==tv_back){
            onLeftClickListener.onClick(tv_back);
        }else if (v==tv_Title){
            Toast.makeText(context,"sdfasdf",Toast.LENGTH_SHORT).show();
        }else if (v==tv_right){
            onRightClickListener.onClick(tv_right);
        }
    }


    public OnLeftClickListener onLeftClickListener;
    public interface OnLeftClickListener{
        void onClick(View v);
    }


    public void setOnLeftClickListener(OnLeftClickListener onLeftClickListener) {
        this.onLeftClickListener = onLeftClickListener;
    }

    public OnRightClickListener onRightClickListener;
   public interface OnRightClickListener{
        void onClick(View v);
    }

    public void setOnRightClickListener(OnRightClickListener onRightClickListener) {
        this.onRightClickListener = onRightClickListener;
    }
}
