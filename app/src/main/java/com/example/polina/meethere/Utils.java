package com.example.polina.meethere;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.polina.meethere.adapters.SimpleItem;
import com.example.polina.meethere.model.Event;
import com.example.polina.meethere.model.User;
import com.example.polina.meethere.model.UserProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by polina on 10.03.16.
 */
public class Utils {

    private final static int WIDTH = 100;
    private final static int HEIGHT = 100;
    public static final String RESULTS = "results";
    public static final String CATEGORY = "category";
    public static final String EVENT_ID = "event_id";
    public static final String TIME_TAG = "tag";
    public static final String SEARCH = "search";
    public static final String EVENT_NAME = "name";
    public static final SimpleDateFormat INPUT_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    public static final String LON = "lon";
    public static final String LAT = "lat";
    public static final String USER_ID = "user_id" ;
    public static final java.lang.String OFFSET = "offset";
    public static final int PROFILE = 1;
    public static final int EVENT = 0;

    static {
        INPUT_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static String getCurrentTime() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Calendar c = Calendar.getInstance();

        return dateFormat.format(c.getTime());

    }

    public static String[] getCurrentTimePlusHour() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, 1);

        return new String[]{new SimpleDateFormat("yyyy-MM-dd").format(c.getTime()), new SimpleDateFormat("HH:mm").format(c.getTime())};

    }

    public static String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();

        return dateFormat.format(c.getTime());
    }


    public static List<SimpleItem> getAllCategory(Context context) {
        List<SimpleItem> allCategory = new ArrayList<>();
        String arr[] = context.getResources().getStringArray(R.array.category);
        TypedArray image = context.getResources().obtainTypedArray(R.array.category_images);
        for (int i = 0; i < arr.length; i++) {
            allCategory.add(new SimpleItem(image.getResourceId(i, R.drawable.ic_android), arr[i]));
        }
        return allCategory;
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] prof = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, prof, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static InputStream getThumbnailImage(Uri uri, Context context) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            System.out.println(bitmap.toString());
            Bitmap resizedBitmap = getResizedBitmap(bitmap, HEIGHT, WIDTH);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            byte[] bitmapdata = bos.toByteArray();
            InputStream inputStream = new ByteArrayInputStream(bitmapdata);
            return inputStream;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



    public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        Bitmap resizedBitmap;


        if (width >= height) {
            matrix.postScale(scaleHeight, scaleHeight);
            resizedBitmap = Bitmap.createBitmap(bm, width / 2 - height / 2, 0, height, height, matrix, false);
        } else {
            matrix.postScale(scaleWidth, scaleWidth);
            resizedBitmap = Bitmap.createBitmap(bm, 0, height / 2 - width / 2, width, width, matrix, false);
        }

        return resizedBitmap;

    }

    public static String convertDateToISO(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:00.000'Z'");
        df.setTimeZone(tz);
        try {
            return df.format(dateFormat.parseObject(date));
        } catch (ParseException e) {
            return null;
        }

    }


    public static List<Event> parseEventList(JSONObject o) {

        List<Event> list = new ArrayList<>();
        JSONArray arr = new JSONArray();
        try {
            if(o!=null) {
                arr = o.getJSONArray(RESULTS);

                for (int i = 0; i < arr.length(); i++) {
                    Event event = new Event();
                    final JSONObject eventJSON = arr.getJSONObject(i);


                    list.add(parseEvent(eventJSON));
                    System.out.println("List of Category: event " + i + "________ " + event.toString());
                }


                return list;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static List<User> parseUsersList(JSONObject o) {

        List<User> list = new ArrayList<>();
        JSONArray arr = new JSONArray();
        try {
            arr = o.getJSONArray(RESULTS);

            for (int i = 0; i < arr.length(); i++) {
                User event = new User();
                final JSONObject eventJSON = arr.getJSONObject(i);


                list.add(parseUser(eventJSON));
                System.out.println("List of Category: event " + i + "________ " + event.toString());
            }


            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static List<UserProfile> parseUsersProfile(JSONObject o) {

        List<UserProfile> list = new ArrayList<>();
        JSONArray arr = new JSONArray();
        try {
            arr = o.getJSONArray(RESULTS);

            for (int i = 0; i < arr.length(); i++) {
                final JSONObject eventJSON = arr.getJSONObject(i);
                list.add(UserProfile.parseUserProfile(eventJSON));
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    private static User parseUser(JSONObject userJSON) {
        User user = new User();
        user.setId(userJSON.optString(user.ID));
        user.setFirstName(userJSON.optString(user.FIRST_NAME));
        user.setLastName(userJSON.optString(user.LAST_NAME));
        user.setImage(userJSON.optString(user.IMAGE));
        return user;
    }


    public static Event parseEvent(JSONObject eventJSON) {

            Event event = new Event();
            event.setId(eventJSON.optString(Event.ID));
            event.setName(eventJSON.optString(Event.NAME));
            event.setDescription(eventJSON.optString(Event.DESCRIPTION));
            event.setStart(eventJSON.optString(Event.START));
            event.setEnd(eventJSON.optString(Event.END));
            event.setJoin(eventJSON.optBoolean(Event.JOINED));
            event.setAttendances(eventJSON.optInt(Event.ATTENDANCES));
            List<Double> l = new ArrayList<>();
            if(eventJSON.optJSONArray(Event.PLACE)!=null) {
                try {
                    l.add(eventJSON.optJSONArray(Event.PLACE).getDouble(0));
                    l.add(eventJSON.optJSONArray(Event.PLACE).getDouble(1));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                event.setPlace(l);
            }

            JSONArray array = eventJSON.optJSONArray(Event.TAGS);
            if(array!=null) {
                List<Integer> listOfTags = new ArrayList<>();
                for (int j = 0; j < array.length(); j++) {
                    try {
                        listOfTags.add(array.getInt(j));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                event.setTag(listOfTags);
            }
            event.setAddress(eventJSON.optString(Event.ADDRESS));
            event.setAgeMax(eventJSON.optInt(Event.AGE_MAX, 45));
            event.setAgeMin(eventJSON.optInt(Event.AGE_MIN, 15));
            event.setBudgetMax(eventJSON.optInt(Event.BUDGET_MAX, 10000));
            event.setBudgetMin(eventJSON.optInt(Event.BUDGET_MIN,0 ));
            if (eventJSON.has("created_by"))
                event.setCreatedBy(User.parseUser(eventJSON.optJSONObject("created_by")));
            return event;


    }


}
