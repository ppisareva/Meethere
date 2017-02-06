package com.tolpa.data;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;

import com.tolpa.model.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by polina on 16.09.16.
 */
public class CalendarContentResolver {
    String[] proj =
            new String[]{
                    "title",
                    "dtstart", "dtend"};

    public static final Uri CALENDAR_URI = Uri.parse("content://com.android.calendar/events");
   public static final Uri REMINDERS_URI = Uri.parse("content://com.android.calendar/reminders");

    ContentResolver contentResolver;

    public CalendarContentResolver(Context ctx) {
        contentResolver = ctx.getContentResolver();
    }


    public Boolean isInCalendar(String start, String title) {
        Cursor cursor = contentResolver.query(CALENDAR_URI, proj,"title=?" , new String[]{title }, null);
        Boolean res = false;
        try {
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    long str = Long.parseLong(cursor.getString(1));

                    if(parseDate(start)==str ){
                        return res = true;
                    }
                }
            }

        } catch (AssertionError ex) {
            ex.printStackTrace();
        }

        return res;
    }

    private long parseDate(String time){
        SimpleDateFormat simpleDateFormat =  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public void addEventToCalendar(Event event){

        ContentValues values = new ContentValues();
        values.put("title", event.getName());
        values.put("calendar_id", 1);
        TimeZone timeZone = TimeZone.getDefault();
        values.put("eventTimezone", timeZone.getID());
        values.put("dtstart", parseDate(event.getStart()));
        values.put("dtend", parseDate(event.getEnd())); // ends 60 minutes from now
        values.put("description", event.getDescription());
        values.put("hasAlarm", 1);
        Uri res = contentResolver.insert(CALENDAR_URI, values);
        values = new ContentValues();
        values.put( "event_id", Long.parseLong(res.getLastPathSegment()));
        values.put( "method", 1 );
        values.put( "minutes", 10 );
        try {
            contentResolver.insert(REMINDERS_URI, values);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }



}