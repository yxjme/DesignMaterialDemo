package com.deringmobile.jbh.designmaterialdemo.util;

import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by zbsdata on 2017/9/4.
 */

public class RestHttpManager {


    public static RestHttpManager manager;
    /**设置超时时间*/
    public static int TIMEOUT=10 * 1000;
    public static RestHttpManager newInstence(){
        if(manager == null){
            manager=new RestHttpManager();
        }
        return manager;
    }

    public RestHttpManager(){}


    /**
     * @param _url
     */
    public void get(String _url,CallBack c){
        HttpURLConnection connection=null;
        try {
            URL url=new URL(_url);
            connection = (HttpURLConnection) url.openConnection();
            connection.getDoInput();
            connection.setRequestMethod("GET");
            connection.setDefaultUseCaches(true);
            //可以添加请求头
            connection.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
//            connection.setRequestProperty("","");
            connection.setConnectTimeout(TIMEOUT);
            /**链接长度*/
//            int length=connection.getContentLength();
            InputStream is=connection.getInputStream();
            inputStream(is,c);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(connection!=null){
                connection.disconnect();
            }
        }
    }

    /**
     *
     * @param is
     */
    private void inputStream(InputStream is,CallBack callBack) {
        BufferedReader buffer;
        byte[] result=null;
        try {
            buffer=new BufferedReader(new InputStreamReader(is));
            result = buffer.readLine().getBytes("utf-8");
            callBack.result(new String(result));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * post请求
     * @param _url
     * @param map
     * @param callBack
     */
    public void post(String _url, Map<String,String> map,CallBack callBack){
        HttpURLConnection conn=null;
        try {
            URL url=new URL(_url);
            conn= (HttpURLConnection) url.openConnection();
            conn.getDoOutput();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(TIMEOUT);
            conn.setDefaultUseCaches(true);
            conn.setRequestProperty("Content-Type", "text/plain; charset=utf-8");

            OutputStream out=conn.getOutputStream();
            outputStream(out,map);

            InputStream is=conn.getInputStream();
            postInputStream(is,callBack);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(conn!=null){
                conn.disconnect();
            }
        }
    }


    /**
     * post上传的数据
     * @param out
     * @param map
     */
    private void outputStream(OutputStream out, Map<String, String> map) {
        try {
            out.write(new Gson().toJson(map).getBytes());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * post请求的结果数据
     * @param is
     * @param callBack
     */
    private void postInputStream(InputStream is, CallBack callBack) {
        byte[] b=new byte[1024];
        int len = 0;
        try {
            while ((len = is.read())!= 0 ){
                is.read(b,0,len);
            }
            callBack.result(new String(b));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
