package com.deringmobile.jbh.designmaterialdemo.weights;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.deringmobile.jbh.designmaterialdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zbsdata on 2017/9/7.
 */

public class FlowLayout extends ViewGroup {

    /**所有行的view的集合*/
    public List<List<State>> allViews=new ArrayList<>();
    /**行高*/
    private List<Integer> mlineHeight=new ArrayList<>();

    public Drawable normalBackgroundResource ;
    public Drawable pressBackgroundResource;


    public FlowLayout(Context context) {
        this(context,null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array=context.obtainStyledAttributes(attrs,R.styleable.FlowLayout,0,defStyleAttr);
        int count=array.getIndexCount();

        for (int i = 0 ; i<count ; i++){
            int attr=array.getIndex(i);
            switch (attr){
                case R.styleable.FlowLayout_normalBackgroundResource:
                    normalBackgroundResource = array.getDrawable(attr);
                    break;
                case R.styleable.FlowLayout_pressBackgroundResource:
                    pressBackgroundResource = array.getDrawable(attr);
                    break;
                default: break;
            }
        }
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeWidthMode=MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight=MeasureSpec.getSize(heightMeasureSpec);
        int sizeHeightMode=MeasureSpec.getMode(heightMeasureSpec);


        int width = 0;
        int height = 0;

        /**行宽度*/
        int lineWidth = 0;
        /**行高度*/
        int lineHeight = 0;

        int count=getChildCount();
        if(count>0){
            for (int i=0;i<count;i++){
                View child=getChildAt(i);

                /**测量子view*/
                measureChild(child,widthMeasureSpec,heightMeasureSpec);
                MarginLayoutParams params= (MarginLayoutParams) child.getLayoutParams();

                /**子view所占的宽度*/
                int childWidth=child.getMeasuredWidth()+params.leftMargin+params.rightMargin;
                /**子view所占有的高度*/
                int childHeight=child.getMeasuredHeight()+params.topMargin+params.bottomMargin;


                //换行的情况
                if( lineWidth + childWidth > sizeWidth){
                    /**取最大的作为行宽*/
                    width=Math.max(lineWidth,childWidth);
                    /**重置下一行的宽*/
                    lineWidth=childWidth;
                    /**累计行高度*/
                    height+=childHeight;
                    /**重置行高度*/
                    lineHeight=childHeight;
                }else {
                    //未换行时候操作
                    /**累加控件的宽度*/
                    lineWidth+=childWidth;
                    /**比较当前行中个最高的view*/
                    lineHeight=Math.max(childHeight,lineHeight);
                }

                /**最后一个控件*/
                if(i==(count-1)){
                    lineWidth=Math.max(lineWidth ,width);
                    height+=lineHeight;
                }
            }
        }

//        LogUtil.showLogV(" ==w= ","width : "+sizeWidth);
//        LogUtil.showLogV(" ==h= ","hight : "+sizeHeight);
        setMeasuredDimension(sizeWidthMode == MeasureSpec.EXACTLY  ? sizeWidth : width ,
                sizeHeightMode==MeasureSpec.EXACTLY  ? sizeHeight : height);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        allViews.clear();
        mlineHeight.clear();

        /**获取控件的高度*/
        int width=getWidth();
        /**单行的宽度*/
        int lineWidth = 0;
        /**单行的高度*/
        int lineHeight = 0;
        /**所有的自view的个数*/
        int count=getChildCount();
        /**初始化第一行的view集合*/
        List<State> lineView=new ArrayList<>();


        for (int i=0; i < count ; i++){

            View child=getChildAt(i);
            MarginLayoutParams lp= (MarginLayoutParams) child.getLayoutParams();
            int childWidth=child.getMeasuredWidth();
            int childHeight=child.getMeasuredHeight();

            /**需要换行*/
            if(lineWidth + childWidth + lp.leftMargin + lp.rightMargin > width){

                mlineHeight.add(lineHeight);
                allViews.add(lineView);

                lineWidth = 0;
                lineHeight=childHeight + lp.topMargin +lp.bottomMargin;

                /**重置到下一行的view*/
                lineView=new ArrayList<>();
            }

            lineWidth+=childWidth+lp.leftMargin+lp.rightMargin;
            lineHeight=Math.max(childHeight+lp.topMargin+lp.bottomMargin,lineHeight);
            lineView.add(new State(child,false));
        }

        mlineHeight.add(lineHeight);
        allViews.add(lineView);


        int left= 0;
        int top = 0;
        int lineNums=allViews.size();

        Log.v("=====lineNums=",String.valueOf(lineNums));


        for (int i=0;i<lineNums;i++){
            lineView=allViews.get(i);
            lineHeight=mlineHeight.get(i);

            for (int j = 0;j<lineView.size();j++){
                View child=lineView.get(j).getV();
                if(child.getVisibility()==View.GONE){
                    continue;
                }

                MarginLayoutParams  lp= (MarginLayoutParams) child.getLayoutParams();
                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();
                /**自view的位置*/
                child.layout(lc,tc,rc,bc);
                left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            }
            left= 0;
            top+=lineHeight;
            Log.v("=====top=",String.valueOf(top));
        }
        setClick();
    }

    int position=0;
    int linePosition=0;
    int childAtPosition = 0;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setClick() {
        int allLineNum=allViews.size();
        for (int i=0;i<allLineNum;i++){
            this.linePosition=i;
            List<State> lineView = allViews.get(i);
            int lineNum=lineView.size();
            for (int j=0;j<lineNum;j++){
                this.childAtPosition=j;
                final State child=lineView.get(j);
                child.getV().setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("==========","linePosition:"+linePosition);
                        Log.d("==========","childAtPosition:"+childAtPosition);
                        getPosition(linePosition,childAtPosition);
                        if(child.isTag){
                            child.setTag(false);
                            v.setBackground(normalBackgroundResource);
                        }else {
                            child.setTag(true);
                            v.setBackground(pressBackgroundResource);
                        }
                        onItemClickListener.onItemClick(child.getV(),child.getTag(),position);
                    }
                });
            }
            position+=lineNum;
        }
    }


    /**
     *
     * @param linePosition
     * @param childAtPosition
     */
    private void getPosition(int linePosition, int childAtPosition) {
        position=0;
        if(linePosition==0){
            position=childAtPosition;
        }else {
            int count=linePosition;
            for (int i=0;i<linePosition;i++){
                position+=allViews.get(i).size();
            }
            position+=childAtPosition;
        }
        Log.d("==========","position:"+position);
    }



    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }


    /**
     * @param list
     */
    public void setData(List<String> list){
        for (String s:list){
            Button button=new Button(getContext());
            button.setText(s);
            ViewGroup.MarginLayoutParams params= new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.bottomMargin=5;
            params.leftMargin=5;
            params.rightMargin=5;
            params.topMargin=5;
            button.setLayoutParams(params);
            addView(button);
        }
    }



    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View v,Boolean tag,int position);
    }


    public class State{

        View v;
        Boolean isTag;

        public State(View v, Boolean isTag) {
            this.v = v;
            this.isTag = isTag;
        }

        public View getV() {
            return v;
        }

        public void setV(View v) {
            this.v = v;
        }

        public Boolean getTag() {
            return isTag;
        }

        public void setTag(Boolean tag) {
            isTag = tag;
        }
    }
}


