package com.deringmobile.jbh.designmaterialdemo.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.PointerIcon;
import android.view.View;
import android.widget.Toast;

import com.deringmobile.jbh.designmaterialdemo.R;
import com.deringmobile.jbh.designmaterialdemo.adapter.MyPagerAdapter;
import com.deringmobile.jbh.designmaterialdemo.fragment.ItemFragment;
import com.deringmobile.jbh.designmaterialdemo.fragment.YouHuiQuanFragment;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zbsdata on 2017/9/8.
 */

public class UserCenterActivity extends AppCompatActivity {


    @Bind(R.id.tl_title)
    Toolbar tlTitle;
    @Bind(R.id.ctl_title)
    CollapsingToolbarLayout ctlTitle;
    @Bind(R.id.mTabLayout)
    TabLayout mTabLayout;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    @Bind(R.id.cdl_content)
    CoordinatorLayout cdlContent;


    private MyPagerAdapter adapter;
    private List<String> title=new ArrayList<>();
    private List<Fragment> list=new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);
        ButterKnife.bind(this);
        setSupportActionBar(tlTitle);
        tlTitle.setNavigationIcon(R.drawable.back);
        tlTitle.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /**标题显示的位置*/
        ctlTitle.setCollapsedTitleGravity(Gravity.LEFT);
        ctlTitle.setExpandedTitleGravity(Gravity.LEFT);
        /**设置标题*/
        ctlTitle.setTitle("手机安全卫士");
        /**标题的字体颜色*/
        ctlTitle.setExpandedTitleColor(Color.TRANSPARENT);
        ctlTitle.setCollapsedTitleTextColor(Color.WHITE);
        /**过程变化的颜色*/
        ctlTitle.setContentScrimColor(Color.GREEN);
        initView();
    }


    private void initView() {
        Toast.makeText(UserCenterActivity.this, "dfasdfS", Toast.LENGTH_SHORT).show();
        title.add("我的积分");
        title.add("优惠券");
        list.clear();
        for (int i=0;i<title.size();i++){
            YouHuiQuanFragment item=new YouHuiQuanFragment(title.get(i));
            list.add(item);
            mTabLayout.addTab(mTabLayout.newTab().setText(title.get(i)));
        }
        viewpager.setOffscreenPageLimit(0);
        adapter=new MyPagerAdapter(getSupportFragmentManager(),list,title);
        viewpager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(viewpager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_settings:
                Log.v("======","设置");
                return true;
            case R.id.action_search:
                Log.v("======","搜索");
                return true;
            case R.id.action_camera:
                Log.v("======","相机");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

