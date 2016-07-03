package com.example.polina.meethere.network;

import android.content.SharedPreferences;

import com.example.polina.meethere.Utils;
import com.example.polina.meethere.data.Comment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ko3a4ok on 5/7/16.
 */
public class ServerApi {
    public static final String HOST = "https://meethere-dev.herokuapp.com/";
    public static final String AUTH = "myauth";
    public static final String COMMENT = "/comment/";
    public static final String EVENT = "event/";
    public static final String MANAGE = "/manage";
    public static final String SEARCH = "search/?q=";
    public static final String SORT_LOW_PRICE = "&sort_by=+budget_min";
    public static final String SORT_HIGH_PRICE = "&sort_by=-budget_max";

    public static final String EVENTS_BY_CATEGORY = "find-event/tags/all/";
    public static final String USER = "user/";
    public static final String ATTEND = "/attenders";
    public static final String EVENTS_PAST = "/events/past";
    public static final String EVENTS_FUTURE = "/events/future";
    public static final String EVENTS_MY = "find-event?created_by=";


    public final int FRAGMENT_PAST_EVENTS = 4343430;
    public final int FRAGMENT_FUTURE_EVENTS = 4343431;
    public final int FRAGMENT_CREATED_BY_ME_EVENTS = 4343432;

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

    public JSONObject loadEvent(String id){
        HttpConnector connector = new HttpConnector(HOST + EVENT+id);
        connector.setHeader(AUTH_HEADER, "Token " + accessToken);
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

    public JSONObject loadEventsByCategory(int category) {
        HttpConnector connector = new HttpConnector(HOST + EVENTS_BY_CATEGORY+category);
        connector.setHeader(AUTH_HEADER, "Token " + accessToken);
        return connector.response();
    }

    public JSONObject loadMyEvents(int timeStamp, int id) {
        if(id==-1) return null;
        String host = "";

        switch (timeStamp){
            case FRAGMENT_PAST_EVENTS:
               host = (HOST + USER + id+ EVENTS_PAST);
                break;
            case FRAGMENT_FUTURE_EVENTS:
                host =(HOST + USER +id+ EVENTS_FUTURE);
                break;
            case FRAGMENT_CREATED_BY_ME_EVENTS:
                host =(HOST + EVENTS_MY+id);
                break;
        }
        HttpConnector connector = new HttpConnector(host);
        connector.setHeader(AUTH_HEADER, "Token " + accessToken);
        return connector.response();
    }

    public JSONObject joinEvent(String id) {
        HttpConnector connector = new HttpConnector(HOST + EVENT+id+MANAGE);
        connector.setHeader(AUTH_HEADER, "Token " + accessToken);
        connector.postData();
        return connector.response();

    }

    public JSONObject unjoinEvent(String id) {
        HttpConnector connector = new HttpConnector(HOST + EVENT+id+MANAGE);
        connector.setHeader(AUTH_HEADER, "Token " + accessToken);
        connector.deleteData();
        return connector.response();
    }

    public JSONObject loadJoiners(String id) {
        HttpConnector connector = new HttpConnector(HOST + EVENT+id+ATTEND);
        connector.setHeader(AUTH_HEADER, "Token " + accessToken);
        return connector.response();
    }

    public JSONObject loadEventsByWords(String search) {
        HttpConnector connector = new HttpConnector(HOST + SEARCH+ search);
        connector.setHeader(AUTH_HEADER, "Token " + accessToken);
        return connector.response();
    }

    public JSONObject loadEventsByHighPrice(String search) {
        HttpConnector connector = new HttpConnector(HOST + SEARCH+ search+SORT_HIGH_PRICE);
        connector.setHeader(AUTH_HEADER, "Token " + accessToken);
        return connector.response();
    }


    public JSONObject loadEventsByLowPrice(String search) {
        HttpConnector connector = new HttpConnector(HOST + SEARCH+ search +SORT_LOW_PRICE);
        connector.setHeader(AUTH_HEADER, "Token " + accessToken);
        return connector.response();
    }

    public Comment sendComment(String eventId, String str) {
        HttpConnector connector = new HttpConnector(HOST + EVENT+ eventId +COMMENT);
        connector.setHeader(AUTH_HEADER, "Token " + accessToken);
        JSONObject o = new JSONObject();
        try {
            o.put("text", str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        connector.setData(o.toString());
        o = connector.response();
        if (o == null) return null;
        return new Comment(o);

    }

    public List<Comment> getComments(String eventId, int offset) {
        HttpConnector connector = new HttpConnector(HOST + EVENT+ eventId +COMMENT + "?offset="+offset + "&limit=5");
        connector.setHeader(AUTH_HEADER, "Token " + accessToken);
        JSONObject o = connector.response();
        if (o == null) return null;
        JSONArray arr = o.optJSONArray(Utils.RESULTS);
        List<Comment> comments = new ArrayList();
        for (int i = 0; i < arr.length(); i++)
            comments.add(new Comment(arr.optJSONObject(i)));
        return comments;
    }
}
