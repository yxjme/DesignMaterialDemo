package com.deringmobile.jbh.designmaterialdemo.mvp.view;

/**
 * Created by zbsdata on 2017/9/11.
 */

public interface BaseInterface {
    void loading(int type);
    void dismiss();
    void error(String s);
    void result(Object result);
}
