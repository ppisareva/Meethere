package com.example.polina.meethere.data;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.example.polina.meethere.Utils;
import com.example.polina.meethere.fragments.FeedFragment;
import com.example.polina.meethere.model.App;
import com.example.polina.meethere.model.Event;
import com.example.polina.meethere.model.Feed;
import com.example.polina.meethere.model.User;
import com.example.polina.meethere.model.UserProfile;
import com.example.polina.meethere.network.ServerApi;

import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by polina on 03.06.16.
 */
public class EventProvider extends android.content.ContentProvider {

    public static final String AUTHORITY = "com.example.polina.meethere.data.data";
    private static final long REFRESH_TIMEOUT = 10000;

    EventsSQLiteHelper database;

    private static final int MY_EVENTS = 11;
    private static final int USER_EVENTS = 12;
    private static final int CATEGORY_ID = 1;
    private static final int SEARCH = 13;
    private static final int SEARCH_LOW_PRICE = 10;
    private static final int CATEGORY_LOW_PRICE = 9;
    private static final int SEARCH_BY_HIGH = 8;
    private static final int CATEGORY_BY_HIGH = 7;
    private static final int SEARCH_BY_DISTANCE = 4;
    private static final int CATEGORY_BY_DISTANCE = 3;
    private static final int CATEGORY_FEED =2 ;
    private static final int SEARCH_FREINDS = 6;
    private static final int FEED = 5;

    private List<Object[]> feedData = new ArrayList<>();
    private List<Object[]> eventsData = new ArrayList<>();
    private List<Object[]> eventsMy = new ArrayList<>();
    private List<Object[]> eventsPast = new ArrayList<>();
    private List<Object[]> eventsFuture = new ArrayList<>();

    public static final int PAST_EVENTS = 4343430;
    public static final int FUTURE_EVENTS = 4343431;
    public static final int CREATED_BY_ME_EVENTS = 4343432;

    private static final UriMatcher URI_MATCHER;
    private SQLiteDatabase db;


    // prepare the UriMatcher
    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

        URI_MATCHER.addURI(AUTHORITY,  "category",  CATEGORY_ID);
        URI_MATCHER.addURI(AUTHORITY,  "category_feed",  CATEGORY_FEED);
        URI_MATCHER.addURI(AUTHORITY, "distance_category", CATEGORY_BY_DISTANCE);
        URI_MATCHER.addURI(AUTHORITY, "distance_search", SEARCH_BY_DISTANCE);
        URI_MATCHER.addURI(AUTHORITY, "feed", FEED);
        URI_MATCHER.addURI(AUTHORITY, "friends_search/*", SEARCH_FREINDS);
        URI_MATCHER.addURI(AUTHORITY, "high_price_category",  CATEGORY_BY_HIGH);
        URI_MATCHER.addURI(AUTHORITY, "high_price_search",  SEARCH_BY_HIGH);
        URI_MATCHER.addURI(AUTHORITY, "low_price_category", CATEGORY_LOW_PRICE);
        URI_MATCHER.addURI(AUTHORITY, "low_price_search", SEARCH_LOW_PRICE);
        URI_MATCHER.addURI(AUTHORITY, "myevents", MY_EVENTS);
        URI_MATCHER.addURI(AUTHORITY, "userevents", USER_EVENTS);
        URI_MATCHER.addURI(AUTHORITY, "words_search", SEARCH);

    }

    private Map<String, Long> LAST_UPDATE = new HashMap<>();
    @Override
    public boolean onCreate() {
         database = new EventsSQLiteHelper(getContext());

        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return list(uri);
    }

    private Cursor list( Uri uri){
        List<Event> events = null;
        ServerApi serverApi = ((App) getContext().getApplicationContext()).getServerApi();
        String search = "";
        JSONObject jsonObject = null;
        String offset ="";
        String categoryId = "";
        String lon ="";
        String lat = "";





        Uri previousUri = uri;
        switch (URI_MATCHER.match(uri)) {  /// done
            case CATEGORY_FEED:
                categoryId = uri.getQueryParameter("category");
                if(categoryId.equals(Utils.POPULAR+"")){
                    jsonObject = serverApi.loadEventsByCategory(categoryId, offset);
                    MatrixCursor  cursor =  new MatrixCursor(new String[]{"_id",  Event.ID, Event.NAME, Event.DESCRIPTION, Event.START,
                            Event.TAGS, Event.JOINED, Event.ADDRESS,  Event.BUDGET_MIN, Event.LAT, Event.LNG, Event.ATTENDANCES});
                    events = Utils.parseEventList(jsonObject);
                        eventsData.clear();
                    if(events==null) return null;
                    for (Event event : events) {
                        eventsData.add(new Object[]{event.getId(), event.getId(), event.getName(), event.getDescription(), event.getStart(),
                                event.getTag(), event.getJoin(),  event.getAddress(), event.getBudgetMin(), event.getLat(), event.getLng(), event.getAttendances()});
                    }
                    for (Object[] row : eventsData) {
                        cursor.addRow(row);
                    }
                    return cursor;
                }
                if (!LAST_UPDATE.containsKey(categoryId) || LAST_UPDATE.get(categoryId) + REFRESH_TIMEOUT < System.currentTimeMillis()) {
                    LAST_UPDATE.put(categoryId, System.currentTimeMillis());
                    new LoadEvents().execute(categoryId, uri.toString());
                }
                Cursor cursor = database.getCursor(categoryId);
                cursor.setNotificationUri(getContext().getContentResolver(), previousUri);
                return  cursor;

            case CATEGORY_ID:
                categoryId = uri.getQueryParameter("category");
                offset = uri.getQueryParameter("offset");
                jsonObject = serverApi.loadEventsByCategory(categoryId, offset);
                break;


            case MY_EVENTS: //// done
                offset = uri.getQueryParameter("offset");
                String timeStamp = uri.getQueryParameter("id");
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("pref", getContext().MODE_PRIVATE);
                int id = sharedPreferences.getInt(UserProfile.USER_ID, -1);
                jsonObject= serverApi.loadMyEvents(timeStamp, id, offset);
                events = Utils.parseEventList(jsonObject);

                    switch (Integer.parseInt(timeStamp)){
                        case PAST_EVENTS:
                          return getEventCursor(eventsPast, offset, events);

                        case FUTURE_EVENTS:
                           return getEventCursor(eventsFuture, offset, events);

                        case CREATED_BY_ME_EVENTS:
                          return getEventCursor(eventsMy, offset, events);

                    }

            case SEARCH: //// done
                offset = uri.getQueryParameter("offset");
                search = uri.getQueryParameter("search");
                jsonObject = serverApi.loadEventsByWords(search, offset);

                break;
            case SEARCH_BY_HIGH: ///// done
                offset = uri.getQueryParameter("offset");
                search = uri.getQueryParameter("search");
                jsonObject = serverApi.loadEventsByHighPrice(search, offset);

                break;
            case CATEGORY_BY_HIGH: ///// //done
                offset = uri.getQueryParameter("offset");
                categoryId = uri.getQueryParameter("category");
                jsonObject = serverApi.loadEventsByHighPriceAndCategory(categoryId, offset);

                break;
            case SEARCH_LOW_PRICE: /////done
                offset = uri.getQueryParameter("offset");
                search = uri.getQueryParameter("search");
                jsonObject = serverApi.loadEventsByLowPrice(search, offset);

                break;
            case CATEGORY_LOW_PRICE: ///////done
                offset = uri.getQueryParameter("offset");
                categoryId = uri.getQueryParameter("category");
                jsonObject = serverApi.loadEventsByLowPriceAndCategory(categoryId, offset);

                break;
            case SEARCH_BY_DISTANCE: ///// done
                lon = uri.getQueryParameter("lon");
                lat = uri.getQueryParameter("lat");
                if(uri.getQueryParameter("search")==null)
                {
                    eventsData.clear();
                    jsonObject = serverApi.loadEventsByDistance(lon, lat);
                } else {
                    search = uri.getQueryParameter("search");
                    offset = uri.getQueryParameter("offset");
                    jsonObject = serverApi.loadEventsByDistance(lon, lat, search, offset);
                }
                break;
            case CATEGORY_BY_DISTANCE: ///// todo
                lon = uri.getQueryParameter("lon");
                lat = uri.getQueryParameter("lat");
                categoryId = uri.getQueryParameter("category");
                offset = uri.getQueryParameter("offset");
                jsonObject = serverApi.loadEventsByDistanceAndCategory(lon, lat, categoryId, offset);
                break;
            case USER_EVENTS: //// done
                offset = uri.getQueryParameter("offset");
                String userId =uri.getQueryParameter("user_id");
                jsonObject = serverApi.loadUserEvents(userId, offset);
                break;
            case SEARCH_FREINDS:
                String  name = uri.getLastPathSegment();
                JSONObject w = serverApi.searchFriends(name);
               List <UserProfile> userlist = Utils.parseUsersProfile(w) ;
               MatrixCursor c = new MatrixCursor(new String[]{"_id", User.FIRST_NAME, User.LAST_NAME, User.IMAGE});
                for (UserProfile user : userlist ) {
                    c.addRow(new Object[]{user.getId(), user.getFirstName(), user.getLastName(), user.getMiniProfileUrl()});
                }
                return c;
            case FEED:
                offset = uri.getQueryParameter("offset");
                jsonObject = serverApi.loadFeed(offset);
                List<Feed> feed = Feed.parseFeed(jsonObject);
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




       MatrixCursor  cursor = getEventCursor();
                events = Utils.parseEventList(jsonObject);
        if("0".equals(offset)) {
            eventsData.clear();
        }
            if(events==null) return null;
            for (Event event : events) {
                eventsData.add(new Object[]{event.getId(), event.getName(), event.getDescription(), event.getStart(),
                        event.getTag(), event.getJoin(), event.getAddress(), event.getBudgetMin(), event.getLat(), event.getLng(), event.getAttendances()});
            }
        for (Object[] row : eventsData) {
            cursor.addRow(row);
        }
        return cursor;
    }

    private MatrixCursor getEventCursor(){
        return  new MatrixCursor(new String[]{"_id", Event.NAME, Event.DESCRIPTION, Event.START,
               Event.TAGS, Event.JOINED, Event.ADDRESS, Event.BUDGET_MIN, Event.LAT, Event.LNG, Event.ATTENDANCES});
    }

    private MatrixCursor getEventCursor(List<Object[]> list, String offset, List<Event> events){
        MatrixCursor  cursor = getEventCursor();
        if ("0".equals(offset))
            list.clear();
        if(events==null) return null;
        for (Event event : events) {
            list.add(new Object[]{event.getId(), event.getName(), event.getDescription(), event.getStart(),
                    event.getTag(), event.getJoin(), event.getAddress(), event.getBudgetMin(), event.getLat(), event.getLng(), event.getAttendances()});
        }
        for (Object[] row : list) {
            cursor.addRow(row);
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
      db=database.getWritableDatabase();

        int uriType = URI_MATCHER.match(uri);

        long id = 0;
        switch (uriType) {
            case CATEGORY_FEED:
                id = db.insert(EventsSQLiteHelper.TABLE_EVENTS, null, values);
                System.out.println(id);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return Uri.parse(AUTHORITY + "/" + id);

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = URI_MATCHER.match(uri);
        db=database.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case CATEGORY_FEED:
                        rowsDeleted = db.delete(EventsSQLiteHelper.TABLE_EVENTS, selection,
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    class LoadEvents extends AsyncTask<String,Void, Void >{

        @Override
        protected Void doInBackground(String... params) {
            ServerApi serverApi = ((App) getContext().getApplicationContext()).getServerApi();

            String categoryId = params[0];
            Uri uri = Uri.parse(params[1]);

            System.err.println("LoadEvents:: " + uri);
            JSONObject  jsonObject = serverApi.loadEventsByCategory(categoryId, "0");
            List<Event> events = Utils.parseEventList(jsonObject);


            if(events==null) return null;
            getContext().getContentResolver().delete(uri, EventsSQLiteHelper.COLUMN_TAGS +" = ?" , new String[]{categoryId});
            ContentValues[] cvs = new ContentValues[events.size()];
            for (int i = 0; i < events.size(); i++) {
                Event event = events.get(i);
                ContentValues contentValues = new ContentValues();
                contentValues.put(EventsSQLiteHelper.COLUMN_ID, event.getId());
                contentValues.put(EventsSQLiteHelper.COLUMN_NAME, event.getName());
                contentValues.put(EventsSQLiteHelper.COLUMN_DESCRIPTION, event.getDescription());
                contentValues.put(EventsSQLiteHelper.COLUMN_START, event.getStart());
                contentValues.put(EventsSQLiteHelper.COLUMN_TAGS, categoryId);
                contentValues.put(EventsSQLiteHelper.COLUMN_JOIND, event.getJoin());
                contentValues.put(EventsSQLiteHelper.COLUMN_ADDRESS, event.getAddress());
                contentValues.put(EventsSQLiteHelper.COLUMN_BUDGET_MIN, event.getBudgetMin());
                contentValues.put(EventsSQLiteHelper.COLUMN_LAT, event.getLat());
                contentValues.put(EventsSQLiteHelper.COLUMN_LNG, event.getLng());
                contentValues.put(EventsSQLiteHelper.ATTENDANCE, event.getAttendances());
                cvs[i]=contentValues;
            }
            System.out.println(getContext().getContentResolver().bulkInsert(uri, cvs));
            LAST_UPDATE.put(categoryId, System.currentTimeMillis());
            getContext().getContentResolver().notifyChange(uri, null);
            return null;
        }

    }


}

