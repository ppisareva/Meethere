package com.example.polina.meethere.network;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by ko3a4ok on 5/7/16.
 */
public class HttpConnector {
    private HttpURLConnection conn;


    public HttpConnector(String url) {
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
        } catch (IOException e) {
            e.printStackTrace();
        }
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setDoInput(true);
        // Starts the query

    }
    public HttpConnector(String url, boolean b) {
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
        } catch (IOException e) {
            e.printStackTrace();
        }
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setDoInput(true);
        // Starts the query

    }

    public void deleteData(){

        try {
            conn.setRequestMethod("DELETE");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void postData(){

        try {
            conn.setRequestMethod("POST");
        } catch (Exception e) {
            e.printStackTrace();
        }}


    public void setData(String data) {
        System.err.println("DATA: " + data);
        setHeader("Content-Type", "application/json");
        try {
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(data);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void patchData(String data) {
        System.err.println("DATA: " + data);
        setHeader("Content-Type", "application/json");
        try {
            conn.setRequestMethod("PATCH");
            conn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(data);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setHeader(String key, String value) {
        System.err.println("HEADER: "  + key + ":" + value);
        conn.setRequestProperty(key, value);
    }

    public JSONObject response() {
        InputStream is = null;
        int len = 0xfffff;
        try {
            conn.connect();
            int response = conn.getResponseCode();
            System.err.println("REQUEST URL: " + conn.getURL());
            System.err.println("RESPONSE CODE: " + response);

            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is);
            System.err.println("RESPONSE CONTENT: " + contentAsString);
            return new JSONObject(contentAsString);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                System.err.println("RESPONSE CONTENT: " + readIt(conn.getErrorStream()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }  finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String readIt(InputStream stream) {
        BufferedReader r = new BufferedReader(new InputStreamReader(stream));
        StringBuilder total = new StringBuilder();
        String line;
        try {
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return total.toString();

    }


}