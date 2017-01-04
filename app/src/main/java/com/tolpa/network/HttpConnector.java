package com.tolpa.network;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ko3a4ok on 5/7/16.
 */
public class HttpConnector {
    final static private OkHttpClient conn = new OkHttpClient();
    private final Request.Builder builder;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");


    public HttpConnector(String url) {
         builder = new Request.Builder().url(url);
    }

    public void deleteData(){
        builder.delete();

    }

    public void postData(){
        builder.post(RequestBody.create(null, new byte[0]));
    }

    public void setData(String data) {
        RequestBody body = RequestBody.create(JSON, data);
        builder.post(body);
    }
    public void setData(String data, String contentType) {
        System.err.println("DATA: " + data);
        setHeader("Content-Type", contentType);
        builder.post(RequestBody.create(MediaType.parse(contentType + "; charset=utf-8"), data));
    }

    public void patchData(String data) {
        System.err.println("DATA: " + data);
        setHeader("Content-Type", "application/json");
        builder.patch(RequestBody.create(JSON, data));
    }

    public void setHeader(String key, String value) {
        builder.addHeader(key, value);
    }

    public JSONObject response() {
        Response response = null;
        try {
            response = conn.newCall(builder.build()).execute();
            String jsonData = response.body().string();
            System.out.println("RESPONSE: " + jsonData);
            return new JSONObject(jsonData);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}