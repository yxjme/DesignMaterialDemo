package com.deringmobile.jbh.designmaterialdemo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.deringmobile.jbh.designmaterialdemo.R;
import com.deringmobile.jbh.designmaterialdemo.adapter.CommonAdapter;
import com.deringmobile.jbh.designmaterialdemo.util.AppBarStateChangeListener;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ContantActivity extends AppCompatActivity {

    @Bind(R.id.tl_title)
    Toolbar tlTitle;
    @Bind(R.id.ctl_title)
    CollapsingToolbarLayout ctlTitle;
    @Bind(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.mAppBarLayout)
    AppBarLayout mAppBarLayout;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.img)
    ImageView img;
    @Bind(R.id.edSearchKey)
    EditText edSearchKey;
    private CommonAdapter adapter ;
    private ExecutorService executorService;
    private List<String> usernames;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contant);
        ButterKnife.bind(this);
        setSupportActionBar(tlTitle);


        /**标题显示的位置*/
        ctlTitle.setCollapsedTitleGravity(Gravity.LEFT);
        ctlTitle.setExpandedTitleGravity(Gravity.LEFT);
        /**设置标题*/
        ctlTitle.setTitle("联系人列表");

        /**标题的字体颜色*/
        ctlTitle.setExpandedTitleColor(Color.TRANSPARENT);//展开时候颜色
        ctlTitle.setCollapsedTitleTextColor(Color.WHITE); //收起来的字体颜色

        /**过程变化的颜色*/
        ctlTitle.setContentScrimColor(Color.TRANSPARENT);


        executorService= Executors.newSingleThreadExecutor();


        initData();
    }

    /**初始化获取联系人数据*/
    private void initData() {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    Log.d("====tag=",new Gson().toJson(usernames));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initView();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        });


        mAppBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {

            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {


            }

            @Override
            public void onoffsetChanged(AppBarLayout appBarLayout, int offset) {
                /**AppBar的高度*/
                int appLayoutHeight=appBarLayout.getTotalScrollRange();
                /**变化率*/
                float scale= (float) (1-(Math.abs(offset)*1.0)/appLayoutHeight);
                Log.v("======tag=","scale:"+scale);
                int color=Color.argb((int) ((1-scale)*255),255,255,0);
                tvTitle.setTextColor(Color.argb((int) ((scale)*255),180,150,0));
                tlTitle.setBackgroundColor(color);
                img.setScaleX(scale);
                img.setScaleY(scale);

                edSearchKey.setPivotY(appLayoutHeight*scale);
                edSearchKey.setPivotX(edSearchKey.getWidth());
                edSearchKey.setScaleX(scale);
                edSearchKey.setScaleY(scale);
            }
        });
    }



    private void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new CommonAdapter(this,R.layout.item_contant_layout,usernames.size(),0,0) {
            @Override
            public void content(RecyclerView.ViewHolder holder, final int position) {
                TextView tv_user_name=(TextView)holder.itemView.findViewById(R.id.tv_user_name);
                tv_user_name.setText(usernames.get(position));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(ContantActivity.this,ChatActivity.class)
                                .putExtra("contactName" , usernames.get(position)));
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.search:
                startActivity(new Intent(ContantActivity.this,SearchActivity.class));
                break;
            case R.id.add_contact:
                startActivity(new Intent(ContantActivity.this,AddContactActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
