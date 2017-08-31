package com.alipay.api.conf;

import java.util.ResourceBundle;

import com.alipay.api.conf.kit.EncryptUtil;

public class AlipayConfigure {

	static ResourceBundle resource = ResourceBundle.getBundle("alipay");
	
    private static String API_URL = resource.getString("API_URL");
    
    private static String APP_ID = resource.getString("APP_ID");
    private static String PID = resource.getString("PID");
	
	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    private static String notify_url = resource.getString("notify_url");
	
	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问	
    private static String return_url = resource.getString("return_url");

//  private static String APP_PRIVATE_KEY = EncryptUtil.getInstance().AESdecode(resource.getString("APP_PRIVATE_KEY"), "cscc#");
    private static String APP_PRIVATE_KEY = resource.getString("APP_PRIVATE_KEY");
    private static String ALIPAY_PUBLIC_KEY  = resource.getString("ALIPAY_PUBLIC_KEY");
    private static String SIGN_TYPE = "RSA2";
    private static String CHARSET = "utf-8";
    private static String FORMAT = "json";

    public static String getAPI_URL() {
        return API_URL;
    }

    public static void setAPI_URL(String aPI_URL) {
        API_URL = aPI_URL;
    }

    public static String getAPP_ID() {
        return APP_ID;
    }

    public static void setAPP_ID(String aPP_ID) {
        APP_ID = aPP_ID;
    }

    public static String getPID() {
        return PID;
    }

    public static void setPID(String pID) {
        PID = pID;
    }

    public static String getSIGN_TYPE() {
        return SIGN_TYPE;
    }

    public static void setSIGN_TYPE(String sIGN_TYPE) {
        SIGN_TYPE = sIGN_TYPE;
    }

    public static String getCHARSET() {
        return CHARSET;
    }

    public static void setCHARSET(String cHARSET) {
        CHARSET = cHARSET;
    }

    public static String getFORMAT() {
        return FORMAT;
    }

    public static void setFORMAT(String fORMAT) {
        FORMAT = fORMAT;
    }

    public static String getNotify_url() {
        return notify_url;
    }

    public static void setNotify_url(String notify_url) {
        AlipayConfigure.notify_url = notify_url;
    }

    public static String getReturn_url() {
        return return_url;
    }

    public static void setReturn_url(String return_url) {
        AlipayConfigure.return_url = return_url;
    }

    public static String getAPP_PRIVATE_KEY() {
        return APP_PRIVATE_KEY;
    }

    public static void setAPP_PRIVATE_KEY(String aPP_PRIVATE_KEY) {
        APP_PRIVATE_KEY = aPP_PRIVATE_KEY;
    }

    public static String getALIPAY_PUBLIC_KEY() {
        return ALIPAY_PUBLIC_KEY;
    }

    public static void setALIPAY_PUBLIC_KEY(String aLIPAY_PUBLIC_KEY) {
        ALIPAY_PUBLIC_KEY = aLIPAY_PUBLIC_KEY;
    }

}

