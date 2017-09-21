package com.deringmobile.jbh.designmaterialdemo.net;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by zbsdata on 2017/9/8.
 */

public class RestHttpRequest  {

    /**
     * 首先实例化okhttp
     * @return
     */
    private static OkHttpClient.Builder okHttp=new OkHttpClient.Builder()
            .connectTimeout(3, TimeUnit.MINUTES);


    /**
     * 获取的Retrofit的对象
     */
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .addConverterFactory(new NullOnEmptyConverterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .baseUrl(Api.BaseUrl_1);


    /**
     * @param serviceClass
     * @param <S>
     * @return
     */
    public synchronized static <S> S createService(Class<S> serviceClass) {
        okHttp.interceptors().add(new HttpInterceptor());
        Retrofit retrofit = builder.client(okHttp.build()).build();
        return retrofit.create(serviceClass);
    }



    /**
     *
     */
    public static class HttpInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            Request request = builder.addHeader("Content-type", "application/json").build();
            okhttp3.Response response = chain.proceed(request);
            return response;
        }
    }



    /**
     *
     */
    public static class NullOnEmptyConverterFactory extends Converter.Factory {

        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            final Converter<ResponseBody, ?> delegate = retrofit.nextResponseBodyConverter(this, type, annotations);

            return new Converter<ResponseBody,Object>() {
                @Override
                public Object convert(ResponseBody body) throws IOException {
                    if (body.contentLength() == 0) return null;
                    return delegate.convert(body);
                }
            };
        }
    }
}
