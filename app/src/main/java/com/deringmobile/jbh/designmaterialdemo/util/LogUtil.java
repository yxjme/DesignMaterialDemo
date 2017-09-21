package com.deringmobile.jbh.designmaterialdemo.util;

import android.util.Log;

/**
 * Created by zbsdata on 2017/8/30.
 */

public class LogUtil {

    private static Boolean isLog;

    public static void setDeBug(Boolean isLog0){
        isLog=isLog0;
    }


    /**
     *
     * @param tag
     * @param msg
     */
    public static void showLogV(String tag,String msg){
        if(isLog){
            Log.v(tag,msg);
        }
    }


}
