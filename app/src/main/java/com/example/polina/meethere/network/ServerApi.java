package com.example.polina.meethere.network;

import android.content.SharedPreferences;

import com.example.polina.meethere.Utils;
import com.example.polina.meethere.data.Comment;
import com.example.polina.meethere.fragments.ProfileFragment;
import com.example.polina.meethere.model.App;

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

    public static final String AUTH = "/auth/facebook";
    public static final String LOGIN = "auth/email/login";
    public static final String CHECKUSER = "/auth/email/check?email=";
    public static final String SIGNUP = "auth/register";

    public static final String COMMENT = "/comment/";
    public static final String COMMENT_EDIT = "/event/%s/comment/%s/";
    public static final String EVENT = "event/";
    public static final String MANAGE = "/manage";
    public static final String SEARCH = "search/?q=";
    public static final String SEARCH_WORDS = "&q=";
    public static final String SEARCH_LON = "search?lon=";
    public static final String LAT = "&lat=";
    public static final String LOG = "lon=";
    public static final String SORT_LOW_PRICE = "sort_by=+budget_min";
    public static final String SORT_UP = "sorted_by=budget_min";
    public static final String SORT_DOWN = "sorted_by=-budget_min";
    public static final String SORT_HIGH_PRICE = "sort_by=-budget_max";


    public static final String EVENTS_BY_CATEGORY = "find-event/tags/all/";
    public static final String USER = "user/";
    public static final String PROFILE = "/profile/";
    public static final String PRO = "profile/";

    public static final String POPULAR = "popular";
    public static final String ATTEND = "/attenders";
    public static final String EVENTS_PAST = "/events/past";
    public static final String EVENTS_FUTURE = "/events/future";
    public static final String EVENTS_MY = "find-event?created_by=";
    public static final String FOLLOW = "/follow";
    public static final String FOLLOWERS = "/followers";
    public static final String FOLLOWINGS = "/followings";
    private static final int POPULAR_EVENTS = 445445;
    private static final String FEED = "feed/";

    private static final String OFFSET = "?limit=10&offset=";
    private static final String OFFSET_ = "&limit=10&offset=";
    private static final String INVITE ="/invite/" ;


    public final int FRAGMENT_PAST_EVENTS = 4343430;
    public final int FRAGMENT_FUTURE_EVENTS = 4343431;
    public final int FRAGMENT_CREATED_BY_ME_EVENTS = 4343432;

    public static final String AUTH_HEADER = "Authorization";
    private String accessToken;
    App a;


    public JSONObject auth(String fbToken, String pnToken) {
        System.err.println("FB TOKEN: " + fbToken);
        System.err.println("Notification TOKEN: " + pnToken);
        String url = HOST + AUTH;
        if (pnToken != null)
            url += "?notification_token="+pnToken;
        HttpConnector connector = new HttpConnector(url);
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

    public void uploadImage(String id, byte[] imageByteArray, int direct) {
        URL url = null;
        try {
            if(direct==Utils.EVENT){
                url = new URL(HOST + EVENT +id+"/image_upload");
            } else{
                if(direct==Utils.PROFILE){
                    url = new URL(HOST+USER+ PRO+ "photo");
                }
            }

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

    public JSONObject loadEventsByCategory(String category, String offset) {
        HttpConnector connector = new HttpConnector(HOST + EVENTS_BY_CATEGORY+category + OFFSET + offset );
        if(Integer.parseInt(category)==POPULAR_EVENTS){
           connector = new HttpConnector(HOST + EVENTS_BY_CATEGORY+POPULAR + OFFSET + offset);
        }

        connector.setHeader(AUTH_HEADER, "Token " + accessToken);
        return connector.response();
    }

    public JSONObject loadMyEvents(String timeStamp, int id, String offset) {
        if(id==-1) return null;
        String host = "";

        switch (Integer.parseInt(timeStamp)){
            case FRAGMENT_PAST_EVENTS:
               host = (HOST + USER + id+ EVENTS_PAST +"/" + OFFSET +offset );
                break;
            case FRAGMENT_FUTURE_EVENTS:
                host =(HOST + USER +id+ EVENTS_FUTURE +"/" + OFFSET +offset );
                break;
            case FRAGMENT_CREATED_BY_ME_EVENTS:
                host =(HOST + EVENTS_MY+id + OFFSET_ +offset );
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

    public JSONObject loadEventsByWords(String search, String offset) {
        HttpConnector connector = new HttpConnector(HOST + SEARCH+ search + OFFSET_+offset);
        connector.setHeader(AUTH_HEADER, "Token " + accessToken);
        return connector.response();
    }

    public JSONObject loadEventsByHighPrice(String search, String offset) {
        HttpConnector connector = new HttpConnector(HOST + SEARCH+ search+ "&"+SORT_HIGH_PRICE + OFFSET_+offset);
        connector.setHeader(AUTH_HEADER, "Token " + accessToken);
        return connector.response();
    }


    public JSONObject loadEventsByLowPrice(String search, String offset) {
        HttpConnector connector = new HttpConnector(HOST + SEARCH+ search + "&"+SORT_LOW_PRICE + OFFSET_+offset);
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

    public JSONObject loadEventsByDistance(String longitude, String latitude, String search, String offset) {
        HttpConnector connector = new HttpConnector(HOST + SEARCH_LON+ longitude +LAT+latitude + SEARCH_WORDS +search+OFFSET_+offset) ;
        connector.setHeader(AUTH_HEADER, "Token " + accessToken);
        return connector.response();

    }

    public JSONObject searchFriends(String name) {
        HttpConnector connector = new HttpConnector(HOST + USER+ SEARCH + name);
        connector.setHeader(AUTH_HEADER, "Token " + accessToken);
        return connector.response();
    }

    public JSONObject loadUserEvents(String userId, String offset) {
        HttpConnector connector = new HttpConnector(HOST + EVENTS_MY+userId + OFFSET_ + offset);
        connector.setHeader(AUTH_HEADER, "Token " + accessToken);
        return connector.response();
    }

    public JSONObject loadUserProfile(String param) {
        HttpConnector connector = new HttpConnector(HOST + PROFILE+param);
        connector.setHeader(AUTH_HEADER, "Token " + accessToken);
        return connector.response();
    }

    public JSONObject follow(String userId) {
        HttpConnector connector = new HttpConnector(HOST + USER+userId+FOLLOW);
        connector.setHeader(AUTH_HEADER, "Token " + accessToken);
        connector.postData();
        return connector.response();
    }

    public JSONObject unfollow(String userId) {
        HttpConnector connector = new HttpConnector(HOST +USER+userId+FOLLOW);
        connector.setHeader(AUTH_HEADER, "Token " + accessToken);
        connector.deleteData();
        return connector.response();
    }

    public JSONObject updateProfile(JSONObject param, String id) {
        HttpConnector connector = new HttpConnector(HOST + PRO+id +"/");
        connector.setHeader(AUTH_HEADER, "Token " + accessToken);
        connector.patchData(param.toString());

        return connector.response();
    }

    public JSONObject loadFeed( String offset) {
        HttpConnector connector = new HttpConnector(HOST + FEED + OFFSET+ offset);
        connector.setHeader(AUTH_HEADER, "Token " + accessToken);
        return connector.response();
    }

    public JSONObject updateEvent( String data, String id) {
        HttpConnector connector = new HttpConnector(HOST + EVENT + id +"/");
        connector.setHeader(AUTH_HEADER, "Token " + accessToken);
        connector.patchData(data);
        return connector.response();
    }
    public void deleteComment(String eventId, String commentId) {
        HttpConnector connector = new HttpConnector(HOST + String.format(COMMENT_EDIT, eventId, commentId));
        connector.setHeader(AUTH_HEADER, "Token " + accessToken);
        connector.deleteData();
        connector.response();
    }

    public JSONObject loadEventsByDistance(String lon, String lat) {
        HttpConnector connector = new HttpConnector(HOST + SEARCH_LON+ lon +LAT+lat + "&limit=1000&offset=0");
        connector.setHeader(AUTH_HEADER, "Token " + accessToken);
        return  connector.response();
    }

    public JSONObject loadFollowing(int id) {
        HttpConnector connector = new HttpConnector(HOST + USER + id +FOLLOWINGS );
        connector.setHeader(AUTH_HEADER, "Token " + accessToken);
        return  connector.response();
    }

    public JSONObject sendInvite(String eventId, String user) {
        HttpConnector connector = new HttpConnector(HOST + EVENT + eventId +INVITE+user );
        connector.setHeader(AUTH_HEADER, "Token " + accessToken);
        connector.postData();
        return  connector.response();
    }

    //DONE

    public JSONObject loadEventsByHighPriceAndCategory(String categoryId, String offset) {
        HttpConnector connector = new HttpConnector(HOST + EVENTS_BY_CATEGORY+categoryId + "?"+SORT_DOWN + OFFSET_+ offset );
        connector.setHeader(AUTH_HEADER, "Token " + accessToken);
        return  connector.response();
    }

    // DONE
    public JSONObject loadEventsByLowPriceAndCategory(String categoryId, String offset) {
        HttpConnector connector = new HttpConnector(HOST + EVENTS_BY_CATEGORY+categoryId +"?"+SORT_UP+ OFFSET_ + offset );
        connector.setHeader(AUTH_HEADER, "Token " + accessToken);
        return  connector.response();
    }

    // DANE
    public JSONObject loadEventsByDistanceAndCategory(String lon, String lat, String categoryId, String offset) {
        HttpConnector connector = new HttpConnector(HOST + EVENTS_BY_CATEGORY+categoryId +"?"+LOG+ lon +LAT+lat +  OFFSET_ + offset);
        connector.setHeader(AUTH_HEADER, "Token " + accessToken);
        return  connector.response();
    }

    public JSONObject logIn (String email, String password){
        HttpConnector connector = new HttpConnector(HOST + LOGIN);
        String data = String.format("email=%s&password=%s", email, password);
        connector.setData(data, "application/x-www-form-urlencoded");
        return connector.response();
    }

    public boolean checkUser(String s) {
        HttpConnector connector = new HttpConnector(HOST + CHECKUSER + s);
        JSONObject o = connector.response();
        if (o == null) return false;
        return o.optBoolean("registered");

    }

    public JSONObject signUp(String fn, String ln, String e, String p) {
        HttpConnector connector = new HttpConnector(HOST + SIGNUP);
        String data = String.format("first_name=%s&last_name=%s&email=%s&username=%s&password=%s", fn, ln, e, e, p);
        connector.setData(data, "application/x-www-form-urlencoded");
        return connector.response();
    }
}
