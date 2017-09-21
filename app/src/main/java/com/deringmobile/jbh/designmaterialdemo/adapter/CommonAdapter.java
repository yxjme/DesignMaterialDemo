package com.deringmobile.jbh.designmaterialdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zbsdata on 2017/8/7.
 */

public abstract class CommonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int HEAD_VIEW_ID=0;
    public static final int FOOTER_VIEW_ID=1;
    public static final int ITEM_VIEW_ID=2;

    private Context context;
    private int itemView;
    private int itemCount;
    private LayoutInflater inflater;
    private int headView=0;
    private int footerView=0;

    public CommonAdapter(Context context, int itemView, int itemCount, int headView, int footerView){
        this.context=context;
        this.itemView=itemView;
        this.itemCount=itemCount;
        this.headView=headView;
        this.footerView=footerView;
        inflater=LayoutInflater.from(context);
        setCount();
    }

    public int getHeadView() {
        return headView;
    }

    public void setHeadView(int headView) {
        this.headView = headView;
    }

    public int getFooterView() {
        return footerView;
    }

    public void setFooterView(int footerView) {
        this.footerView = footerView;
    }

    public void setCount(){
        if(footerView!=0){
            itemCount=itemCount+1;
        }
        if(headView!=0){
            itemCount=itemCount+1;
        }
        Log.v("========itemCount=",String.valueOf(itemCount));
    }



    @Override
    public int getItemViewType(int position) {
        if(headView!=0 && position==0){
            return HEAD_VIEW_ID;
        }else if (footerView!=0&&(position+1)==itemCount){
            return FOOTER_VIEW_ID;
        }else {
            return ITEM_VIEW_ID;
        }
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if(viewType==HEAD_VIEW_ID){
            view=inflater.inflate(headView,parent,false);
            return new HeadViewHolder(view);
        }else if (viewType==FOOTER_VIEW_ID){
            view=inflater.inflate(footerView,parent,false);
            return new FooterViewHolder(view);
        }else {
            view=inflater.inflate(itemView,parent,false);
            return new MyViewHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(headView!=0 && position==0){
            headContent(holder,position);
        }else if (footerView!=0&&(position+1)==itemCount){
            footerContent(holder,position);
        }else {
            content(holder,position);
        }
    }


    @Override
    public int getItemCount() {
        return  itemCount;
    }

    public abstract void content(RecyclerView.ViewHolder holder, int position);
    public abstract void headContent(RecyclerView.ViewHolder holder, int position);
    public abstract void footerContent(RecyclerView.ViewHolder holder, int position);

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class HeadViewHolder extends RecyclerView.ViewHolder{
        public HeadViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder{
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
