package com.example.polina.meethere.network;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.LocalBroadcastManager;

import com.example.polina.meethere.model.App;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.net.URL;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class NetworkService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_CREATE_EVENT = "com.example.polina.meethere.network.action.FOO";
    private static final String ACTION_LOAD_EVENTS = "com.example.polina.meethere.network.action.LOAD" ;
    private static final String ACTION_BAZ = "com.example.polina.meethere.network.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.example.polina.meethere.network.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.example.polina.meethere.network.extra.PARAM2";
    public static final String STATUS = "status";
    public static final String ID = "auto_id_0";
    private static final String CATEGORY = "category";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionCreateNewEvent(Context context, String data, Bitmap imageBitmap) {
        Intent intent = new Intent(context, NetworkService.class);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        intent.setAction(ACTION_CREATE_EVENT);
        intent.putExtra(EXTRA_PARAM1, data);
        intent.putExtra(EXTRA_PARAM2, byteArray);
        context.startService(intent);
    }

    public static void startActionLoadEventsByCategory(Context context, int category) {
        Intent intent = new Intent(context, NetworkService.class);
        intent.setAction(ACTION_LOAD_EVENTS);
        intent.putExtra(CATEGORY,category);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, NetworkService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public NetworkService() {
        super("NetworkService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            switch (action){
                case ACTION_CREATE_EVENT:
                    final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                    final byte [] param2 = intent.getByteArrayExtra(EXTRA_PARAM2);
                    handleActionCreateEvent(param1, param2);
                    break;
                case ACTION_LOAD_EVENTS:
                    final int category = intent.getIntExtra(CATEGORY,0);
                    handleActionLoadEventsByCategory(category);
                    break;

            }

        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionCreateEvent(String data, byte [] imageByteArray) {
        ServerApi serverApi = ((App) getApplication()).getServerApi();
        JSONObject o = serverApi.createEvent(data);
        if(o!=null){
            try {
                String id = o.getString(ID);
                serverApi.uploadImage(id, imageByteArray);

            } catch (Exception e) {
                e.printStackTrace();
            }



        }
        Intent intent = new Intent(ACTION_CREATE_EVENT);
        intent.putExtra(STATUS, o != null);
        LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);
    }


    private void handleActionLoadEventsByCategory(int category) {
        ServerApi serverApi = ((App) getApplication()).getServerApi();
        JSONObject o = serverApi.loadEventsByCategory(category);
        Intent intent = new Intent(ACTION_LOAD_EVENTS);
        intent.putExtra(STATUS, o != null);
        LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);
    }
    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
