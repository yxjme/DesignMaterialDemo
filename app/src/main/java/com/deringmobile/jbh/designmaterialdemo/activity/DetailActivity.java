package com.deringmobile.jbh.designmaterialdemo.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.deringmobile.jbh.designmaterialdemo.R;
import com.deringmobile.jbh.designmaterialdemo.server.DownServer;
import com.deringmobile.jbh.designmaterialdemo.weights.FlowLayout;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {



    DownServer server;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent=new Intent(DetailActivity.this,DownServer.class);
        intent.setAction("com.deringmobile.jbh.designmaterialdemo.active");
        bindService(intent,connection, Context.BIND_AUTO_CREATE);

        FlowLayout mFlowLayout= (FlowLayout) findViewById(R.id.mFlowLaout);
        Button button=new Button(this);
        button.setText("自定义的");
        ViewGroup.MarginLayoutParams params= new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomMargin=10;
        button.setLayoutParams(params);
        mFlowLayout.addView(button);

        List<String> list=new ArrayList<>();
        list.add("klsdaskldfn");
        list.add("porekt");
        list.add("android");
        list.add("java");
        list.add("java web");
        mFlowLayout.setData(list);
        mFlowLayout.setOnItemClickListener(new FlowLayout.OnItemClickListener() {
            @Override
            public void onItemClick(View v, Boolean tag, int position) {
                Button b= (Button) v;
                Log.v("============","position:"+position+"  : "+tag+":"+b.getText().toString());
            }
        });
        int lineNum=mFlowLayout.allViews.size();
        if(lineNum>0){
            Log.d("==========","lineNum:"+lineNum);
        }
    }

    private ServiceConnection connection=new ServiceConnection() {

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            server = ((DownServer.MyBinder)service).getServerContext();
            server.notifyState();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            server=null;
        }
    };
}
