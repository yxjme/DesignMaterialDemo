package com.deringmobile.jbh.designmaterialdemo.net;

import com.deringmobile.jbh.designmaterialdemo.mvp.model.beanResponse;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by zbsdata on 2017/9/11.
 */

public interface ApiService{


    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST(Api.method.serverTime)
    Observable<beanResponse>  request(@Body RequestBody requestBody);


    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST(Api.method.ORDER_LIST)
    Observable<beanResponse>  requestOrderList(@Body RequestBody requestBody);

    /**实现文件下载*/
    @Streaming
    @GET
    Observable<RequestBody> downFileSyncTask(@Url String fileUrl);
}
