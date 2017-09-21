package com.deringmobile.jbh.designmaterialdemo.server;

/**
 * Created by zbsdata on 2017/9/6.
 */

public class DownFileManager {

    public static DownFileManager manager;


    public static DownFileManager newInstence(){

        if(manager==null){
            manager=new DownFileManager();
        }
        return manager;
    }
}
