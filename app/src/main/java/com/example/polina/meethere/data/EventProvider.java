package com.example.polina.meethere.data;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.polina.meethere.Utils;
import com.example.polina.meethere.model.App;
import com.example.polina.meethere.model.Event;
import com.example.polina.meethere.model.Feed;
import com.example.polina.meethere.model.User;
import com.example.polina.meethere.model.UserProfile;
import com.example.polina.meethere.network.ServerApi;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by polina on 03.06.16.
 */
public class EventProvider extends android.content.ContentProvider {

    public static final String AUTHORITY = "com.example.polina.meethere.data.data";

    private static final int MY_EVENTS = 7;
    private static final int USER_EVENTS = 8;
    private static final int CATEGORY_ID = 1;
    private static final int SEARCH = 9;
    private static final int SEARCH_LOW_PRICE = 6;
    private static final int SEARCH_BY_HIGH = 5;
    private static final int SEARCH_BY_DISTANCE = 2;
    private List<Object[]> feedData = new ArrayList<>();


    private static final UriMatcher URI_MATCHER;

    private static final int SEARCH_FREINDS = 4 ;

    private static final int FEED = 3;

    // prepare the UriMatcher
    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

        URI_MATCHER.addURI(AUTHORITY,  "category/#",  CATEGORY_ID);
        URI_MATCHER.addURI(AUTHORITY, "distance_search", SEARCH_BY_DISTANCE);
        URI_MATCHER.addURI(AUTHORITY, "feed", FEED);
        URI_MATCHER.addURI(AUTHORITY, "friends_search/*", SEARCH_FREINDS);
        URI_MATCHER.addURI(AUTHORITY, "high_price_search/*",  SEARCH_BY_HIGH);
        URI_MATCHER.addURI(AUTHORITY, "low_price_search/*", SEARCH_LOW_PRICE);
        URI_MATCHER.addURI(AUTHORITY, "myevents/*", MY_EVENTS);
        URI_MATCHER.addURI(AUTHORITY, "userevents/*", USER_EVENTS);
        URI_MATCHER.addURI(AUTHORITY, "words_search/*", SEARCH);

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
               search =uri.getLastPathSegment();
                JSONObject d = serverApi.loadEventsByWords(search, 0);
                events = Utils.parseEventList(d);
                break;
            case SEARCH_BY_HIGH:
               String high =uri.getLastPathSegment();
                JSONObject f = serverApi.loadEventsByHighPrice(high, 0);
                events = Utils.parseEventList(f);
                break;
            case SEARCH_LOW_PRICE:
                String low =uri.getLastPathSegment();
                JSONObject k = serverApi.loadEventsByLowPrice(low, 0);
                events = Utils.parseEventList(k);
                break;
            case SEARCH_BY_DISTANCE:
                String lon = uri.getQueryParameter("lon");
               String lat = uri.getQueryParameter("lat");
              String  s = uri.getQueryParameter("search");
                JSONObject g = serverApi.loadEventsByDistance(lon,lat, s, 0);
                events = Utils.parseEventList(g);
                break;
            case USER_EVENTS:
                String userId =uri.getLastPathSegment();
                JSONObject hd = serverApi.loadUserEvents(userId);
                events = Utils.parseEventList(hd);
                break;
            case SEARCH_FREINDS:
                String  name = uri.getLastPathSegment();
                JSONObject w = serverApi.searchFriends(name);
               List <UserProfile> userlist = Utils.parseUsersProfile(w) ;
              MatrixCursor cursor = new MatrixCursor(new String[]{"_id", User.FIRST_NAME,User.LAST_NAME, User.IMAGE});
                for (UserProfile user : userlist ) {
                    cursor.addRow(new Object[]{user.getId(), user.getFirstName(), user.getLastName(), user.getMiniProfileUrl()});
                }
                return cursor;
            case FEED:
                String offset = uri.getQueryParameter("offset");
                JSONObject hh = serverApi.loadFeed(offset);
                List<Feed> feed = Feed.parseFeed(hh);
                MatrixCursor feedCursor = new MatrixCursor(new String[]{"_id", User.LAST_NAME,User.FIRST_NAME, User.IMAGE, User.ID, Feed.TYPE, Feed.TIME,
                        com.example.polina.meethere.adapters.Event.START, com.example.polina.meethere.adapters.Event.BUDGET,
                        com.example.polina.meethere.adapters.Event.ID, com.example.polina.meethere.adapters.Event.NAME, com.example.polina.meethere.adapters.Event.DESCRIPTION});
                if ("0".equals(offset))
                    feedData.clear();
                int id_cursor=feedData.size();
                for(int i =0; i<feed.size(); i++){
                    feedData.add(new Object[]{i+id_cursor, feed.get(i).getUser().getLastName(), feed.get(i).getUser().getFirstName(), feed.get(i).getUser().getImage(), feed.get(i).getUser().getId(),
                    feed.get(i).getType(), feed.get(i).getTime(), feed.get(i).getEvent().getStart(), feed.get(i).getEvent().getBudget_min(), feed.get(i).getEvent().getId(), feed.get(i).getEvent().getName()
                    , feed.get(i).getEvent().getDescription()});
                }
                for (Object[] row : feedData) {
                    feedCursor.addRow(row);
                }
                System.err.println("onLoaderFinished === " + feedCursor.getCount());
                return feedCursor;

        }

        MatrixCursor cursor = new MatrixCursor(new String[]{"_id", Event.NAME, Event.DESCRIPTION, Event.START,
                Event.END, Event.TAGS, Event.PLACE, Event.ADDRESS, Event.AGE_MAX, Event.AGE_MIN, Event.BUDGET_MAX, Event.BUDGET_MIN});


        if(events==null) return null;
        for (Event event : events) {
            cursor.addRow(new Object[]{event.getId(), event.getName(), event.getDescription(), event.getStart(), event.getEnd(),
                    event.getTag(), event.getPlace(), event.getAddress(), event.getAgeMax(), event.getAgeMin(), event.getBudgetMax(), event.getBudgetMin()});
        }
        return cursor;
    }


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
