package com.example.polina.meethere.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by polina on 02.09.16.
 */
public class EventsSQLiteHelper extends SQLiteOpenHelper {
    // Database table
    public static final String TABLE_EVENTS = "events";
    public static final String COLUMN_ID = "event_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION =  "description";
    public static final String  COLUMN_START="start";
    public static final String  COLUMN_JOIND   =  "joined";
    public static final String   COLUMN_BUDGET_MIN=     "budget_min";
    public static final String   COLUMN_ADDRESS=     "address";

    public static final String   COLUMN_TAGS =     "tags";
    public static final String COLUMN_LAT = "lat";
    public static final String COLUMN_LNG = "lng";
    public static final String ATTENDANCE = "attendance";

    private static final String DATABASE_NAME = "events.db";
    private static final int DATABASE_VERSION = 4;

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table IF NOT EXISTS "
            + TABLE_EVENTS
            + "(_id INTEGER primary key, "
            + COLUMN_ID + "  text not null, "
            + COLUMN_NAME + " , "
            + COLUMN_DESCRIPTION + " ,"
            + COLUMN_START  + "  ,"

            + COLUMN_TAGS + "  ,"
            + COLUMN_JOIND + "  ,"
            + COLUMN_ADDRESS  + "  ,"
            + COLUMN_BUDGET_MIN  + "  ,"
            + COLUMN_LAT  + "  ,"
            + COLUMN_LNG  + "  ,"
            + ATTENDANCE
            + ");";

    public EventsSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    public void deleteDB (SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        {
            Log.w(EventsSQLiteHelper.class.getName(), "Upgrading database from version "
                    + oldVersion + " to " + newVersion
                    + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
            onCreate(db);
        }
    }

    public Cursor getCursor(String category) {

        return getReadableDatabase().query(TABLE_EVENTS, null, COLUMN_TAGS + " = ?",  new String[]{category}, null, null, null);
    }
}
