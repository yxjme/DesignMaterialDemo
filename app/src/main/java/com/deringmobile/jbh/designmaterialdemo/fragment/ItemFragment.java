package com.deringmobile.jbh.designmaterialdemo.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.deringmobile.jbh.designmaterialdemo.R;
import com.deringmobile.jbh.designmaterialdemo.activity.DetailActivity;
import com.deringmobile.jbh.designmaterialdemo.adapter.CommonAdapter;
import com.deringmobile.jbh.designmaterialdemo.net.LoadImageManager;
import com.deringmobile.jbh.designmaterialdemo.util.OnRecyclerViewScrollListener;
import com.deringmobile.jbh.designmaterialdemo.util.Type;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by zbsdata on 2017/9/2.
 */

public class ItemFragment extends Fragment  implements SwipeRefreshLayout.OnRefreshListener{


    Type type = Type.GLIDE;

    private String[] imgs={
            "https://img10.360buyimg.com/mobilecms/s110x110_jfs/t1588/138/434981161/280119/a5895e83/55815d4dNd883af38.jpg",
            "https://img14.360buyimg.com/n7/s140x140_jfs/t3724/347/1016217917/255875/c5380734/581ae701N723b098d.jpg!q90",
            "https://img10.360buyimg.com/mobilecms/s200x140_jfs/t8596/65/71786488/76878/1ef95ed6/599fee55Nff18c3bf.jpg!cc_200x140",
            "https://img12.360buyimg.com/babel/s400x170_jfs/t5938/90/535830027/35925/30c06ab1/5928f90dN664d55ef.jpg!q90",
            "https://img10.360buyimg.com/n4/s130x130_jfs/t6925/75/2382158459/437865/f3931d24/598be5b1N24d949fe.jpg",
            "https://img12.360buyimg.com/babel/s193x260_jfs/t6871/130/2611878755/49912/9ab9a4a9/598d575cN55decfdf.jpg!q90",
            "https://img14.360buyimg.com/babel/s100x100_jfs/t8860/279/60943576/9177/7b4f784b/599fc0f1N06d987eb.jpg!q90",
            "https://img11.360buyimg.com/mobilecms/s180x225_jfs/t8392/335/71937118/36941/c7549b27/59a00160Nb4136ffb.jpg",
            "https://img11.360buyimg.com/da/jfs/t8221/293/233373328/100778/3faac736/59a3a41aNad8e4521.jpg",
            "https://img1.360buyimg.com/da/jfs/t7363/213/1432700762/196454/fdae1d3d/599d4b39N5af6118d.jpg"};

    private String[] str={
            "的更多",
            "苟富贵",
            "了泼辣的买尬的么 阿德",
            "阿哥大噶 ",
            "的，风格说到底",
            "发的贵定师范的",
            "https://img14.360buyimg.com/babel/s100x100_jfs/t8860/279/4f784b/599fc0f1N06d987eb.jpg!q90",
            "https://img11.360buyimg.com/mob27/59a00160Nb4136ffb.jpg",
            "https://img11.360buyimg.com/da/jfs/t8221/293/233373328/100778/3faac736/59a3a41aNad8e4521.jpg",
            "https://img1.360buyimg.com/da/jfs/t7363/213/1432700762/196454/fdae1d3d/599d4b39N5af6118d.jpg"};

    private CommonAdapter adapter;
    private RecyclerView mRecyclerView;
    private String name;
    private StaggeredGridLayoutManager layoutManager ;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    int mDistanceY=0;


    public ItemFragment(String name){
        this.name=name;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView=inflater.inflate(R.layout.item_fragment_layout,container,false);
        initView(contentView);
        return contentView;
    }



    /**
     * 初始化 控件
     *
     * @param contentView
     *
     */
    private void initView(View contentView) {
//        layoutManager=new StaggeredGridLayoutManager(StaggeredGridLayoutManager.VERTICAL,2);
        mSwipeRefreshLayout=(SwipeRefreshLayout)contentView.findViewById(R.id.mSwipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE,Color.RED,Color.RED,Color.BLUE);
        mSwipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
        refresh();

        mRecyclerView=(RecyclerView) contentView.findViewById(R.id.mRecyclerView);

        mRecyclerView.addOnScrollListener(new OnRecyclerViewScrollListener() {

            @Override
            public void onLoadMore(RecyclerView recyclerView) {
                Log.d("tag","more:"+recyclerView.getMeasuredHeight());
                TextView textView=new TextView(getActivity());
                textView.setText("加载更多。。。");
                textView.setPadding(10,10,10,10);
                int footerView=R.layout.item_contant_layout;
                adapter.setFooterView(footerView);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void offset(RecyclerView recyclerView, int dx, int dy) {
                //滑动的距离
                mDistanceY += dy;
                //toolbar的高度
                int toolbarHeight = 200;
                //当滑动的距离 <= toolbar高度的时候，改变Toolbar背景色的透明度，达到渐变的效果
                if (mDistanceY <= toolbarHeight) {
                    float scale = (float) mDistanceY / toolbarHeight;
                    float alpha = scale * 255;
                    recyclerView.setBackgroundColor(Color.argb((int) alpha, 128, 0, 0));
                } else {
                    //上述虽然判断了滑动距离与toolbar高度相等的情况，但是实际测试时发现，标题栏的背景色
                    //很少能达到完全不透明的情况，所以这里又判断了滑动距离大于toolbar高度的情况，
                    //将标题栏的颜色设置为完全不透明状态
                    recyclerView.setBackgroundResource(R.color.colorPrimary);
                }
                Log.d("tag","dy:"+mDistanceY);
            }
        });


        GridLayoutManager layoutManager=new GridLayoutManager(getActivity(), 2 , GridLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(layoutManager);

        adapter=new CommonAdapter(getActivity(),R.layout.item_layout,imgs.length,0,0) {

            @Override
            public void content(RecyclerView.ViewHolder holder, int position) {
                TextView text=(TextView)holder.itemView.findViewById(R.id.text);
                text.setText(str[position]);
                final ImageView imge=(ImageView)holder.itemView.findViewById(R.id.img);
                switch (type){
                    case GLIDE:
                        LoadImageManager.newStanence().loadImage(getActivity(),imgs[position],0,0,imge);
                        break;
                    case IMAGELOADER:
                        ImageLoader mImageLoader = ImageLoader.getInstance();
                        mImageLoader .displayImage(imgs[position],imge);
                        break;
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), DetailActivity.class));
                    }
                });
            }

            @Override
            public void headContent(RecyclerView.ViewHolder holder, int position) {
            }


            @Override
            public void footerContent(RecyclerView.ViewHolder holder, int position) {

            }
        };
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        refresh();
    }


    public void refresh(){
         new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        },2000);

    }

}
