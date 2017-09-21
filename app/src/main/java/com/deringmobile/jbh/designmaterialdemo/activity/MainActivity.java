package com.deringmobile.jbh.designmaterialdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import com.deringmobile.jbh.designmaterialdemo.R;
import com.deringmobile.jbh.designmaterialdemo.fragment.MainFragment;
import com.deringmobile.jbh.designmaterialdemo.mvp.model.bean;
import com.deringmobile.jbh.designmaterialdemo.mvp.presenter.ServerTimePresenter;
import com.deringmobile.jbh.designmaterialdemo.mvp.view.ServerTimeView;
import com.deringmobile.jbh.designmaterialdemo.util.CallBack;
import com.deringmobile.jbh.designmaterialdemo.util.LogUtil;
import com.deringmobile.jbh.designmaterialdemo.util.RestHttpManager;
import com.google.gson.Gson;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.util.NetUtils;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener ,ServerTimeView {

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private DrawerLayout drawer ;
    private NavigationView navigationView;
    private FrameLayout mFrameLayout;
    private FragmentManager manager;
    private ImageView headImg;
    private ExecutorService executorService;
    private ServerTimePresenter presenter=new ServerTimePresenter(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        executorService= Executors.newSingleThreadExecutor();
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                RestHttpManager.newInstence().get("http://www.weather.com.cn/data/sk/101010100.html", new CallBack() {
                    @Override
                    public void result(Object result) {
                        if(result instanceof String) {
                            LogUtil.showLogV("=======",String.valueOf(result));
                        }
                    }
                });
            }
        });
        initData();
    }


    private void initData() {
        presenter.serverTimeTask();
    }



    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**浮动的按钮*/
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        mFrameLayout=(FrameLayout)findViewById(R.id.mFrameLayout);
        mFrameLayout.removeAllViews();
        manager=getSupportFragmentManager();
        FragmentTransaction ft=manager.beginTransaction();
        MainFragment mainFragment=new MainFragment();
        ft.replace(R.id.mFrameLayout,mainFragment);
        ft.commit();

       //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());
    }

    @Override
    public void loading(int type) {

    }

    @Override
    public void dismiss() {

    }

    @Override
    public void error(String s) {
        Log.d("tag","s:"+s);
    }

    @Override
    public void result(Object result) {
        Log.d("tag",new Gson().toJson(result));
    }

    @Override
    public Map postContent() {
        return new bean().map();
    }


    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }
        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(error == EMError.USER_REMOVED){
                        Log.d("=====main=","显示帐号已经被移除");
                        Toast.makeText(MainActivity.this,"显示帐号已经被移除",Toast.LENGTH_SHORT).show();
                    }else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        Log.d("=====main=","显示帐号在其他设备登录");
                        Toast.makeText(MainActivity.this,"显示帐号在其他设备登录,你的账号即将被退出登录",Toast.LENGTH_SHORT).show();
                    } else {
                        if (NetUtils.hasNetwork(MainActivity.this)){
                            Log.d("=====main=","连接不到聊天服务器");
                            Toast.makeText(MainActivity.this,"连接不到聊天服务器",Toast.LENGTH_SHORT).show();
                        } else{
                            Log.d("=====main=","当前网络不可用，请检查网络设置");
                            Toast.makeText(MainActivity.this,"连接不到聊天服务器",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                return true;

            case R.id.action_search:
                startActivity(new Intent(MainActivity.this,SearchActivity.class));
                return true;

            case R.id.action_camera:
                startActivity(new Intent(MainActivity.this,ContantActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(MainActivity.this,UserCenterActivity.class));
        } else if (id == R.id.nav_slideshow) {
            startActivity(new Intent(MainActivity.this,UserCenterActivity.class));
        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(MainActivity.this,UserCenterActivity.class));
        } else if (id == R.id.nav_share) {
            startActivity(new Intent(MainActivity.this,UserCenterActivity.class));
        } else if (id == R.id.nav_send) {
            startActivity(new Intent(MainActivity.this,UserCenterActivity.class));
        }
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
