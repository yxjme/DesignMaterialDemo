package com.deringmobile.jbh.designmaterialdemo.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.deringmobile.jbh.designmaterialdemo.R;
import com.deringmobile.jbh.designmaterialdemo.adapter.CommonAdapter;
import com.google.gson.Gson;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.adapter.message.EMAMessageBody;

import java.util.Dictionary;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

public class ChatActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.ed_message)
    TextInputEditText edMessage;
    private String toChatUsername="aaaaaa";
    private EMMessage.ChatType chatType = EMMessage.ChatType.Chat;
    private CommonAdapter adapter;
    private LinearLayoutManager mLinearLayoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toChatUsername=getIntent().getStringExtra("contactName");
        toolbar.setTitle(toChatUsername);
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }


    private void dictionaryChatMessage(final List<EMMessage> msgList) {
        if(!(msgList.size()>0)){
            Toast.makeText(ChatActivity.this,"sdfsad",Toast.LENGTH_SHORT).show();
            return;
        }
        mLinearLayoutManager=new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        adapter=new CommonAdapter(this,R.layout.item_right_layout,msgList.size(),0,0) {

            @Override
            public void content(RecyclerView.ViewHolder holder, int position) {

                EMMessage msg = msgList.get(position);
                TextView tvTime=(TextView)holder.itemView.findViewById(R.id.tv_time);
                tvTime.setText(String.valueOf(msg.getMsgTime()));

                TextView tvName=(TextView)holder.itemView.findViewById(R.id.tv_user_name);
                tvName.setText(msg.getFrom());

                TextView tvMessage=(TextView)holder.itemView.findViewById(R.id.tv_message);
                tvMessage.setText(msg.getBody().toString());
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

    @OnClick(R.id.btn_send_message)
    public void onViewClicked() {
        String content=edMessage.getText().toString();
        if(content.length()>0){
            /**
             * 创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
             *            EMMessage.ChatType.Chat      单聊
             *            EMMessage.ChatType.ChatRoom  聊天室
             *            EMMessage.ChatType.GroupChat 群主聊天
             */
            EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
            /**如果是群聊，设置chattype，默认是单聊*/
            if (chatType == EMMessage.ChatType.GroupChat){
                message.setChatType(EMMessage.ChatType.GroupChat);
            }
            /**发送消息*/
            EMClient.getInstance().chatManager().sendMessage(message);
        }
    }


    EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {

            //收到消息
            EMConversation conversation = EMClient.getInstance().chatManager().getConversation(toChatUsername);
            //获取此会话的所有消息
            final List<EMMessage> mess = conversation.getAllMessages();
            Observable.empty().subscribeOn(AndroidSchedulers.mainThread())
                    .doOnCompleted(new Action0() {
                        @Override
                        public void call() {
                            dictionaryChatMessage(mess);
                        }
                    }).subscribe();
//            //SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多
//          //获取startMsgId之前的pagesize条消息，此方法获取的messages SDK会自动存入到此会话中，APP中无需再次把获取到的messages添加到会话中
//            List<EMMessage> mess = conversation.loadMoreMsgFromDB(messages.get(0).getMsgId(), 1000);
            StringBuilder mBuilder=new StringBuilder();
            for (EMMessage body:mess){
                mBuilder.append("name:"+body.getUserName()+"\n");
                mBuilder.append("MsgTime:"+body.getMsgTime()+"\n");
                mBuilder.append("Body:"+body.getBody().toString()+"\n");
                mBuilder.append("From:"+body.getFrom()+"\n");
                mBuilder.append("MsgId:"+body.getMsgId()+"\n");
                mBuilder.append("ChatType:"+body.getChatType()+"\n");
            }
            Log.d("=======收到消息=",mBuilder.toString());
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            //收到已读回执
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
            //收到已送达回执
        }


        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
            Log.d("=======消息状态变动=",new Gson().toJson(message));
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }
}
