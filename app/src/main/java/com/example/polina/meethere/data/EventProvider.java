package com.example.polina.meethere.data;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.polina.meethere.Utils;
import com.example.polina.meethere.model.App;
import com.example.polina.meethere.model.Event;
import com.example.polina.meethere.model.UserProfile;
import com.example.polina.meethere.network.ServerApi;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by polina on 03.06.16.
 */
public class EventProvider extends android.content.ContentProvider {

    public static final String AUTHORITY = "com.example.polina.meethere.data.data";

    private static final int MY_EVENTS = 4;
    private static final int CATEGORY_ID = 1;
    private static final int SEARCH = 5;
    private static final int SEARCH_LOW_PRICE = 3;
    private static final int SEARCH_BY_HIGH = 2;
    private static final int ENTITY_ID = 11;

    private static final UriMatcher URI_MATCHER;

    // prepare the UriMatcher
    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

        URI_MATCHER.addURI(AUTHORITY,  "category/#",  CATEGORY_ID);
        URI_MATCHER.addURI(AUTHORITY, "high_price_search/*",  SEARCH_BY_HIGH);

        URI_MATCHER.addURI(AUTHORITY, "low_price_search/*", SEARCH_LOW_PRICE);
        URI_MATCHER.addURI(AUTHORITY, "myevents/*", MY_EVENTS);
        URI_MATCHER.addURI(AUTHORITY, "words_search/*", SEARCH);

//        URI_MATCHER.addURI(AUTHORITY,
//                "entities/#",
//                ENTITY_ID);
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        return list(uri);

    }

    private Cursor list(Uri uri){
        List<Event> events = null;
        ServerApi serverApi = ((App) getContext().getApplicationContext()).getServerApi();
        String search = "";
        String padr = uri.getLastPathSegment();
        System.out.println(padr) ;
        String fff = uri.getQueryParameter("words_search");
        System.out.println(fff);

        switch (URI_MATCHER.match(uri)) {
            case CATEGORY_ID:
                String par = uri.getLastPathSegment();
                int categoryId = Integer.parseInt(par);
                JSONObject o = serverApi.loadEventsByCategory(categoryId);
                events = Utils.parseEventList(o);
                break;
            case MY_EVENTS:
                String timeStamp =uri.getLastPathSegment();
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("pref", getContext().MODE_PRIVATE);
                int id = sharedPreferences.getInt(UserProfile.USER_ID, -1);
                JSONObject h = serverApi.loadMyEvents(Integer.parseInt(timeStamp), id);
                events = Utils.parseEventList(h);
                break;
            case SEARCH:
               String searcha =uri.getLastPathSegment();
                JSONObject d = serverApi.loadEventsByWords(searcha);
                events = Utils.parseEventList(d);
                break;
            case SEARCH_BY_HIGH:
               String high =uri.getLastPathSegment();
                JSONObject f = serverApi.loadEventsByHighPrice(high);
                events = Utils.parseEventList(f);
                break;
            case SEARCH_LOW_PRICE:
                String low =uri.getLastPathSegment();
                JSONObject k = serverApi.loadEventsByLowPrice(low);
                events = Utils.parseEventList(k);
                break;

        }
        MatrixCursor cursor = new MatrixCursor(new String[]{"_id", Event.NAME, Event.DESCRIPTION, Event.START,
                Event.END, Event.TAGS, Event.PLACE, Event.ADDRESS, Event.AGE_MAX, Event.AGE_MIN, Event.BUDGET_MAX, Event.BUDGET_MIN});
        for (Event event : events) {
            cursor.addRow(new Object[]{event.getId(), event.getName(), event.getDescription(), event.getStart(), event.getEnd(),
                    event.getTag(), event.getPlace(), event.getAddress(), event.getAgeMax(), event.getAgeMin(), event.getBudgetMax(), event.getBudgetMin()});
        }
        return cursor;
    }

//    private Cursor categoryCursor(Uri uri) {
//
//        String par = uri.getLastPathSegment();
//        String timeStamp = uri.getQueryParameter("timestamp");
//        String search = uri.getQueryParameter("search");
//
//
//        if (par != null) {
//            int categoryId = Integer.parseInt(par);
//            JSONObject o = serverApi.loadEventsByCategory(categoryId);
//            events = Utils.parseEventList(o);
//        } else {
//            if (timeStamp != null) {
//                SharedPreferences sharedPreferences = getContext().getSharedPreferences("pref", getContext().MODE_PRIVATE);
//                int id = sharedPreferences.getInt(UserProfile.USER_ID, -1);
//                JSONObject o = serverApi.loadMyEvents(Integer.parseInt(timeStamp), id);
//                events = Utils.parseEventList(o);
//            } else {
//                JSONObject o = serverApi.loadEventsByWords(search);
//                events = Utils.parseEventList(o);
//            }
//        }
//
//
//        MatrixCursor cursor = new MatrixCursor(new String[]{"_id", Event.NAME, Event.DESCRIPTION, Event.START,
//                Event.END, Event.TAGS, Event.PLACE, Event.ADDRESS, Event.AGE_MAX, Event.AGE_MIN, Event.BUDGET_MAX, Event.BUDGET_MIN});
//        for (Event event : events) {
//            cursor.addRow(new Object[]{event.getId(), event.getName(), event.getDescription(), event.getStart(), event.getEnd(),
//                    event.getTag(), event.getPlace(), event.getAddress(), event.getAgeMax(), event.getAgeMin(), event.getBudgetMax(), event.getBudgetMin()});
//        }
//        return cursor;
//
//    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
