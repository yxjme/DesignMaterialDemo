package com.deringmobile.jbh.designmaterialdemo.mvp.model;

/**
 * Created by zbsdata on 2017/9/11.
 */

public class beanResponse {

    B d;
    public B getB() {
        return d;
    }

    public class B{
//            "__type": "WcfResultEntityOfint:#LeaRun.Application.WCF.Entity",
//            "code": 0,
//            "data": 1505109995,
//            "msg": "请求成功。"
        String msg;
        long data;
        int code;

        public String getMsg() {
            return msg;
        }

        public long getData() {
            return data;
        }

        public int getCode() {
            return code;
        }
    }
}
