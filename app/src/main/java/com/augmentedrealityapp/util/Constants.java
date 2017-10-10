package com.augmentedrealityapp.util;

/**
 * Created by prasharma on 12/24/2016.
 */

public class Constants {

    public static String BASE_URL_TEST = "http://www.synapse.asia/android_app9636/users/";
    public static String BASE_URL_LIVE = "";

    public static String DEVICE_TYPE = "Android";
    public static String DEVICE_TOKEN = "";
    public static String USER_ID = "";
    public static String CAT_ID = "";
    public static String EMAIL_ID = "";
    public static int CART_COUNT =0;
    public static String ORDER_ID ="";


    public static enum API_TYPE {
        LOGIN("login"),
        SIGN_UP("signup");

        private String url;

        API_TYPE(String url) {
            this.url = url;
        }

        public String url() {
            return url;
        }
    }

    //DEVICE ID RETURN BY THE SERVER
    public static final String DEVICE_ID = "device_id";

}
