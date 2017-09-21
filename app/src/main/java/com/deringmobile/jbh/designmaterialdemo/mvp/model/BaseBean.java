package com.deringmobile.jbh.designmaterialdemo.mvp.model;

import com.deringmobile.jbh.designmaterialdemo.util.Constant;
import com.deringmobile.jbh.designmaterialdemo.util.MD5Util;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zbsdata on 2017/9/11.
 */

public abstract class BaseBean{

    public String userId = "";
    public String manageId = "d0158ac6-fc5c-452b-b78b-a7686f823714";
    public int device = 2;
    public String time="";
    public String verId = "1";
    public String onlineMark="";
    public String sign;// sign=MD5(userId|manageId|device|time|verId|onlineMark|key)
    public BaseBean(String userId){
        this.userId=userId;
    }


    public StringBuilder md5Sign(){
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(userId+"|"+manageId+"|"+device+"|"+time+"|"+verId+"|"+onlineMark);
        return stringBuilder;
    }

    /**
     *
     * @param sign
     * @return
     */
    public BaseBean setSign(String sign) {
        this.sign = sign;
        return this;
    }


    /**
     *
     * @return
     */
    public Map<String,Object> requestBody(){
        Map<String,Object> map=new HashMap<>();
        map.put(Constant.USERID,userId);
        map.put(Constant.MANAGERID,manageId);
        map.put(Constant.DEVICE,device);
        map.put(Constant.VERID,verId);
        map.put(Constant.ONLINEMARK,onlineMark);
        map.put(Constant.SIGN, MD5Util.getMD5(sign));
        return map;
    }


    /**
     *
     * @return
     *
     */
    public abstract Map map();
}
