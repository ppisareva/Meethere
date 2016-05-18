package com.example.polina.meethere.network;

import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by ko3a4ok on 5/7/16.
 */
public class ServerApi {
    public static final String HOST = "https://meethere-dev.herokuapp.com/";
    public static final String AUTH = "myauth";
    public static final String AUTH_HEADER = "Authorization";

    public JSONObject auth(String fbToken) {
        System.err.println("FB TOKEN: " + fbToken);
        HttpConnector connector = new HttpConnector(HOST + AUTH);
        connector.setHeader(AUTH_HEADER, "Bearer facebook " + fbToken);
        return connector.response();
    }

}
