package com.example.polina.meethere.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.polina.meethere.Utils;
import com.example.polina.meethere.model.App;
import com.example.polina.meethere.model.Event;
import com.example.polina.meethere.network.ServerApi;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by polina on 03.06.16.
 */
public class ContentProvider extends android.content.ContentProvider {
    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        ServerApi serverApi =((App) getContext().getApplicationContext()).getServerApi();
        JSONObject o = serverApi.loadEventsByCategory(0);
        List<Event> events = Utils.parseEventList(o);




        MatrixCursor cursor = new MatrixCursor(new String[]{Event.ID,Event.NAME,Event.DESCRIPTION,Event.START,
        Event.END,Event.TAGS,Event.PLACE,Event.ADDRESS,Event.AGE_MAX,Event.AGE_MIN,Event.BUDGET_MAX,Event.BUDGET_MIN});

        for(Event event : events){
            cursor.addRow(new Object[]{event.getId(),event.getName(),event.getDescription(),event.getStart(),event.getEnd(),
            event.getTag(),event.getPlace(),event.getAddress(),event.getAgeMax(),event.getAgeMin(),event.getBudgetMax(),event.getBudgetMin()});
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
