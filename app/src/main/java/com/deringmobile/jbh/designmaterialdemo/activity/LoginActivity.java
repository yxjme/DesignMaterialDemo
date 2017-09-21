package com.deringmobile.jbh.designmaterialdemo.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.deringmobile.jbh.designmaterialdemo.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.ed_name)
    TextInputEditText edName;
    @Bind(R.id.ed_password)
    TextInputEditText edPassword;
    @Bind(R.id.btn_register)
    Button btnRegister;
    @Bind(R.id.btn_login)
    Button btnLogin;
    @Bind(R.id.btn_login_out)
    Button btnLoginOut;

    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        executorService= Executors.newSingleThreadExecutor();


        Log.d("===tag=","connected:"+EMClient.getInstance().isConnected());
        if(EMClient.getInstance().isConnected()){
            btnLoginOut.setText("退出登录");
        }else {
            btnLoginOut.setText("还没登录");
        }
    }

    @OnClick({R.id.btn_register, R.id.btn_login,R.id.btn_login_out})
    public void onViewClicked(View view) {
        final String name=edName.getText().toString();
        final String password=edPassword.getText().toString();
        switch (view.getId()) {

            /**
             * 注册
             */
            case R.id.btn_register:
                if(name==null&&edName.length()==0){
                    Toast.makeText(LoginActivity.this,"请输入账号",Toast.LENGTH_SHORT).show();
                }else {
                    if(password!=null&&password.length()>0){
                        executorService.submit(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    EMClient.getInstance().createAccount(name, password);
                                } catch (HyphenateException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }else {
                        Toast.makeText(LoginActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            /**
             * 登录
             */
            case R.id.btn_login:

                if(EMClient.getInstance().isConnected()){
                    Toast.makeText(LoginActivity.this,"你已经登录了！",Toast.LENGTH_SHORT).show();
                }else {
                    EMClient.getInstance().login(name,password,new EMCallBack() {//回调
                        @Override
                        public void onSuccess() {
                            /**为了保证回话加载完毕*/
                            EMClient.getInstance().chatManager().loadAllConversations();
                            EMClient.getInstance().groupManager().loadAllGroups();
                            Log.d("main", "登录聊天服务器成功！");
                            finish();
                        }

                        @Override
                        public void onProgress(int progress, String status) {
                            Log.d("main", "progress:"+progress +"\n status:"+status);
                        }

                        @Override
                        public void onError(int code, String message) {
                            Log.d("main", "登录聊天服务器失败！");
                        }
                    });
                }
                break;


            /**
             * 退出登录
             */
            case R.id.btn_login_out:
//                同步方法
//                EMClient.getInstance().logout(true);
//                异步方法
                EMClient.getInstance().logout(true, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
                        Log.d("main", "退出聊天登录服务器成功！");
                        finish();
                    }
                    @Override
                    public void onProgress(int progress, String status) {
                        // TODO Auto-generated method stub
                        Log.d("main", "progress："+progress);
                    }

                    @Override
                    public void onError(int code, String message) {
                        // TODO Auto-generated method stub
                        Log.d("main", "code："+code+"\n message:"+message);
                    }
                });
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_search){

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
