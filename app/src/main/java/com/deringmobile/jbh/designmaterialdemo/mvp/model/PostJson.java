package com.deringmobile.jbh.designmaterialdemo.mvp.model;

import android.util.Log;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zbsdata on 2017/9/11.
 */

public class PostJson {

    /**
     * @param map
     * @return
     */
    public static String getPostContent(Map<String , Object> map){
        Map<String , Object> jsonContent=new HashMap<>();
        jsonContent.put("data",map);
        String content=new Gson().toJson(jsonContent);
        Log.d("tag","content:"+content);
        if(content!=null&&content.length()>0){
            return content;
        }else {
            return null;
        }
    }
}
