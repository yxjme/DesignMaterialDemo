package com.deringmobile.jbh.designmaterialdemo.mvp.model;

import java.util.Map;

/**
 * Created by zbsdata on 2017/9/11.
 */

public class bean extends BaseBean {


    public bean() {
        super("5a4d2ff3-2f11-4ae4-921b-4df5076876ff");
//        map.put("OrderId","a848e277-e1f1-4515-999c-54cfa67487b0");
    }

    @Override
    public Map map() {
        return  setSign(md5Sign().toString()).requestBody();
    }
}
