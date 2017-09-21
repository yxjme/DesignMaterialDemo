package com.deringmobile.jbh.designmaterialdemo.net;

/**
 * Created by zbsdata on 2017/9/11.
 */

public class Api {

    /**外测试*/
    public static final String BaseUrl_0="http://api2.zbs6.com/";
    /**内测*/
    public static final String BaseUrl_1="http://192.168.1.5:9011/";


    public interface method{

        /**获取服务器时间*/
        String serverTime="Common.svc/Time";

        /**获取订单数据*/
        String ORDER_LIST="Client.svc/GetOrder";

        /**文件上传*/
        String FileUpload="Common.svc/FileUpload";

        /**基本信息*/
        String FileBack="Common.svc/FileBack";

        /**省市区全局参数获取*/
        String GetListArea="Common.svc/GetListArea";

        /**手机号短信验证发送*/
        String SendMobileVerify="Common.svc/SendMobileVerify";

        /**手机号短信验证检查*/
        String CheckMobileVerify="Common.svc/CheckMobileVerify";

        /**字典所有数据*/
        String GetListBaseItem="Common.svc/GetListBaseItem";


    }
}
