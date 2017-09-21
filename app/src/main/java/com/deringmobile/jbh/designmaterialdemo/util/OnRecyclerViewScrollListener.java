package com.deringmobile.jbh.designmaterialdemo.util;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by zbsdata on 2017/9/12.
 */



public abstract class OnRecyclerViewScrollListener extends RecyclerView.OnScrollListener {


    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        RecyclerView.LayoutManager layoutManager=recyclerView.getLayoutManager();
        int visibleChildCount=layoutManager.getChildCount();

        boolean triggerCondition = visibleChildCount>0
                && newState==RecyclerView.SCROLL_STATE_IDLE
                &&  scrollPosition(recyclerView,layoutManager) ;
        if(triggerCondition){
            onLoadMore(recyclerView);
        }
    }

    /**
     * recyclerView 滚动监听  是否滑动到了底部
     * @param recyclerView
     * @param layoutManager
     * @return
     */
    private boolean scrollPosition(RecyclerView recyclerView,RecyclerView.LayoutManager layoutManager) {
        View child=layoutManager.getChildAt(layoutManager.getChildCount()-1);
        int position=recyclerView.getChildLayoutPosition(child);
        int totalItemCount = layoutManager.getItemCount();
        return totalItemCount - 1 == position;
    }


    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        offset(recyclerView, dx, dy);
    }

    /**
     * 执行加载更多的操作
     * @param recyclerView
     *
     */
    public abstract void onLoadMore(RecyclerView recyclerView);


    public abstract void offset(RecyclerView recyclerView, int dx, int dy);

}
