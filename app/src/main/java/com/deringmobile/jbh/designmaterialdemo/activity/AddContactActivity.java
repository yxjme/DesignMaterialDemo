package com.deringmobile.jbh.designmaterialdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.deringmobile.jbh.designmaterialdemo.R;
import com.deringmobile.jbh.designmaterialdemo.mvp.model.bean;
import com.deringmobile.jbh.designmaterialdemo.util.LogUtil;
import com.deringmobile.jbh.designmaterialdemo.weights.TextViewScrollerVertical;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddContactActivity extends AppCompatActivity {

    @Bind(R.id.ed_add_contact)
    EditText edAddContact;
    @Bind(R.id.btn_add_contact)
    Button btnAddContact;
    @Bind(R.id.ed_add_contact_reason)
    EditText edAddContactReason;
    ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        ButterKnife.bind(this);
        executorService= Executors.newSingleThreadExecutor();


        TextViewScrollerVertical  aa=(TextViewScrollerVertical)findViewById(R.id.aaa);
        aa.set();


        LogUtil.showLogV("tag" , "time:"+System.currentTimeMillis());
    }

    @OnClick(R.id.btn_add_contact)
    public void onViewClicked() {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                String toAddUsername = edAddContact.getText().toString();
                String reason = edAddContactReason.getText().toString();
                if(toAddUsername.length()>0){
                    //参数为要添加的好友的username和添加理由
                    try {
                        EMClient.getInstance().contactManager().addContact(toAddUsername, reason);
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
