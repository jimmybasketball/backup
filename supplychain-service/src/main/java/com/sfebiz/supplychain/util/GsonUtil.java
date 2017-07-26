package com.sfebiz.supplychain.util;

import com.google.gson.Gson;

/**
 * Created by wang_cl on 2015/7/22.
 */
public class GsonUtil {
    private static Gson gson = new Gson();

    public static Gson getGsonInstance(){
        return gson;
    }
}
