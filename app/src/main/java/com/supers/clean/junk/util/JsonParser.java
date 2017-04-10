package com.supers.clean.junk.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * IVY (2016) All Rights Reserved.
 * Created by renqingyou on 11/08/16.
 */
public class JsonParser {
    public static JsonParser jsonParser;

    public static JsonParser getInstance() {
        if (jsonParser == null) {
            jsonParser = new JsonParser();
        }
        return jsonParser;
    }

    public <T extends Object> T fromJson(byte[] response, Class<T> tClass) {
        return fromJson(getStrFromByte(response), tClass);
    }

    public <T extends Object> T fromJson(String response, Class<T> tClass) {
        String jsonStr = response.replace("\"data\":[]", "\"data\":null");
        Gson gson = new Gson();
        T obj = null;
        try {
            obj = gson.fromJson(jsonStr, tClass);
        } catch (NumberFormatException e) {
            Log.d("JsonParser", e.toString() + " json = " + jsonStr);
        } catch (JsonSyntaxException e) {
            Log.d("JsonParser", e.toString() + " json = " + jsonStr);
        } catch (IllegalStateException e) {
            Log.d("JsonParser", e.toString() + " json = " + jsonStr);
        }

        // try to parse as SimpleResult
        if (obj == null) {

            Object result1 = null;
            try {
                result1 = gson.fromJson(jsonStr, Object.class);
            } catch (NumberFormatException e) {
                Log.d("JsonParser", e.toString() + " json = " + jsonStr);
            } catch (JsonSyntaxException e) {
                Log.d("JsonParser", e.toString() + " json = " + jsonStr);
            } catch (IllegalStateException e) {
                Log.d("JsonParser", e.toString() + " json = " + jsonStr);
            }
            Log.d("JsonParser", "simpleresult = " + result1);
            if (result1 == null) {
                return null;
            }

            try {
                Constructor<T> constructor = tClass.getConstructor(new Class[0]);
                obj = constructor.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return obj;
    }

    public static String getStrFromByte(byte[] responseBody) {
        String response = null;
        if (responseBody != null) {
            try {
                response = new String(responseBody, "utf-8");
            } catch (UnsupportedEncodingException var3) {
                var3.printStackTrace();
            }
        }

        return response;
    }
}
