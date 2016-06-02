package com.example.polina.meethere.network;

import android.content.SharedPreferences;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ko3a4ok on 5/7/16.
 */
public class ServerApi {
    public static final String HOST = "https://meethere-dev.herokuapp.com/";
    public static final String AUTH = "myauth";
    public static final String EVENT = "event/";
    public static final String AUTH_HEADER = "Authorization";
    private String accessToken;


    public JSONObject auth(String fbToken) {
        System.err.println("FB TOKEN: " + fbToken);
        HttpConnector connector = new HttpConnector(HOST + AUTH);
        connector.setHeader(AUTH_HEADER, "Bearer facebook " + fbToken);
        return connector.response();
    }

    public JSONObject createEvent(String data) {
        HttpConnector connector = new HttpConnector(HOST + EVENT);
        connector.setHeader(AUTH_HEADER, "Token " + accessToken);
        connector.setData(data);
        return connector.response();
    }

    public void setAuthToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void uploadImage(String id, byte[] imageByteArray) {
        try {
        URL url = new URL("https://meethere-dev.herokuapp.com/event/"+id+"/image_upload");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty (AUTH_HEADER, "Token " + accessToken);
        sendRequest(connection, imageByteArray);

        InputStream is = null;
        int len = 0xffff;
        connection.connect();
        int response = connection.getResponseCode();
        try {
            is = connection.getInputStream();
        }catch (FileNotFoundException ex) {
            is = connection.getErrorStream();
        }
        String contentAsString = readIt(is, len);
        System.err.println("RESPONSE CODE: " + response);
        System.err.println("RESPONSE CONTENT: " + contentAsString);
    } catch (Exception e) {
        e.printStackTrace();
    }
    }

    public static void sendRequest(HttpURLConnection connection, byte[] byteArray) throws IOException {
        String attachmentName = "file";
        String crlf = "\r\n";
        String twoHyphens = "--";
        String boundary =  "*****";

        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        connection.setRequestProperty(
                "Content-Type", "multipart/form-data;boundary=" + boundary);
        DataOutputStream request = new DataOutputStream(
                connection.getOutputStream());
        request.writeBytes(twoHyphens + boundary + crlf);
        request.writeBytes("Content-Disposition: form-data; name=\"" +
                attachmentName +
                "\";filename=\"" + attachmentName +
                "\"" + crlf);
        request.writeBytes(crlf);
        request.write(byteArray);
        request.writeBytes(crlf);
        request.writeBytes(twoHyphens + boundary +
                twoHyphens + crlf);
        request.flush();
        request.close();


    }

    public String readIt(InputStream stream, int len) throws IOException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
}
