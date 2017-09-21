package com.deringmobile.jbh.designmaterialdemo.mvp.presenter;

import android.os.Handler;
import android.util.Log;
import com.deringmobile.jbh.designmaterialdemo.mvp.model.PostJson;
import com.deringmobile.jbh.designmaterialdemo.mvp.model.beanResponse;
import com.deringmobile.jbh.designmaterialdemo.mvp.view.ServerTimeView;
import com.deringmobile.jbh.designmaterialdemo.net.ApiService;
import com.deringmobile.jbh.designmaterialdemo.net.RestHttpRequest;
import com.google.gson.Gson;
import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by zbsdata on 2017/9/11.
 */

public class ServerTimePresenter implements ServerTimeMethod {


    private Handler handler = new Handler();
    private ServerTimeView serverTimeView;


    public ServerTimePresenter(ServerTimeView serverTimeView){
        this.serverTimeView=serverTimeView;
    }


    @Override
    public void serverTimeTask() {
        serverTimeView.loading(0);
        ApiService apiService= RestHttpRequest.createService(ApiService.class);
        String  content= PostJson.getPostContent(serverTimeView.postContent());
        Log.d("tag" , "request:"+content);
        RequestBody body=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),content);
        apiService.request(body)
                .subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<beanResponse>() {
                    @Override
                    public void onCompleted() {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("tag" , "e:finish");
                                serverTimeView.dismiss();
                            }
                        });
                    }

                    @Override
                    public void onError(final Throwable e) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("tag" , "e:"+e.getMessage());
                                serverTimeView.error(e.getMessage());
                            }
                        });
                    }

                    @Override
                    public void onNext(final beanResponse responseBody) {
                       handler.post(new Runnable() {
                           @Override
                           public void run() {
                               serverTimeView.result(responseBody);
                               Log.d("tag" , "response:"+new Gson().toJson(responseBody));
                           }
                       });
                    }
                });
    }
}
