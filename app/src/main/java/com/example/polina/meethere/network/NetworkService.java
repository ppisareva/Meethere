package com.example.polina.meethere.network;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.LocalBroadcastManager;

import com.example.polina.meethere.Utils;
import com.example.polina.meethere.model.App;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

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
    private static final String DATA = "com.example.polina.meethere.network.extra.PARAM1";
    private static final String IMAGE = "com.example.polina.meethere.network.extra.PARAM2";
    public static final String STATUS = "status";
    public static final String ID = "id";
    private static final String CATEGORY = "category";
    public static final String ACTION_UPDATE_EVENT = "com.example.polina.meethere.network.action.UPDATE" ;

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
        if(imageBitmap!=null) {
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            intent.putExtra(IMAGE, byteArray);
        }
        intent.setAction(ACTION_CREATE_EVENT);
        intent.putExtra(DATA, data);
        context.startService(intent);
    }

    public static void startActionUpdateEvent(Context context, String data, Bitmap imageBitmap, String id) {
        Intent intent = new Intent(context, NetworkService.class);
        if(imageBitmap!=null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            intent.putExtra(IMAGE, byteArray);
        }
        intent.putExtra(ID, id);
        intent.setAction(ACTION_UPDATE_EVENT);
        intent.putExtra(DATA, data);


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
        intent.putExtra(DATA, param1);
        intent.putExtra(IMAGE, param2);
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
                    final String param1 = intent.getStringExtra(DATA);
                    final byte [] param2 = intent.getByteArrayExtra(IMAGE);
                    handleActionCreateEvent(param1, param2);
                    break;
                case ACTION_UPDATE_EVENT:
                    final String d = intent.getStringExtra(DATA);
                    final byte [] i = intent.getByteArrayExtra(IMAGE);
                    final String id = intent.getStringExtra(ID);
                    handleActionUpdateEvent(d,i,id );
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
                serverApi.uploadImage(id, imageByteArray, Utils.EVENT);

            } catch (Exception e) {
                e.printStackTrace();
            }



        }
        Intent intent = new Intent(ACTION_CREATE_EVENT);
        intent.putExtra(STATUS, o != null);
        LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);
    }

    private void handleActionUpdateEvent(String data, byte [] imageByteArray, String idEvent) {
        ServerApi serverApi = ((App) getApplication()).getServerApi();
        JSONObject o = serverApi.updateEvent(data, idEvent);
        if(imageByteArray!=null) {
            serverApi.uploadImage(idEvent, imageByteArray, Utils.EVENT);
        }

        Intent intent = new Intent(ACTION_UPDATE_EVENT);
        intent.putExtra(STATUS, true);
        LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);
    }


    private void handleActionLoadEventsByCategory(int category) {
        ServerApi serverApi = ((App) getApplication()).getServerApi();
        JSONObject o = serverApi.loadEventsByCategory(category+"","" );
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
