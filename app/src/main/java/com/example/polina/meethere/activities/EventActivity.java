package com.example.polina.meethere.activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.polina.meethere.R;
import com.example.polina.meethere.Utils;
import com.example.polina.meethere.adapters.CommentAdapter;
import com.example.polina.meethere.data.CalendarContentResolver;
import com.example.polina.meethere.model.Comment;
import com.example.polina.meethere.model.App;
import com.example.polina.meethere.model.Event;
import com.example.polina.meethere.model.User;
import com.example.polina.meethere.model.UserProfile;
import com.example.polina.meethere.network.NetworkService;
import com.example.polina.meethere.network.ServerApi;
import com.example.polina.meethere.views.EndlessRecyclerViewScrollListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EventActivity extends AbstractMeethereActivity implements LoaderManager.LoaderCallbacks<List<Comment>> {
    private static final int DIALOG_REMOVE_COMMENT = 101011;

    TextView description;
    ImageView image;
    String id;
    CheckBox join;
    Boolean isJoined = false;
    TextView time;
    TextView date;
    TextView budget;
    TextView address;
    TextView    quantity;
    EditText comment;
    MapView mapView;
    RecyclerView recyclerView;
    CommentAdapter commentAdapter;
    ServerApi serverApi;
    Double lat;
    Double lng;
    int id_user;
    Toolbar toolbar;
    CollapsingToolbarLayout collapseToolbar;
    LinearLayout inviteFriends;
    List<User> users = new ArrayList<>();
    CalendarContentResolver calenderReselver;
    ProgressBar progressBar;



    Event event;

    public static final String IMG_PATTERN = "https://s3-us-west-1.amazonaws.com/meethere/%s.jpg";
    public static final String BC_FILTER = "broadcast.filter.event.";
    private LinearLayout edit;
    private BroadcastReceiver createUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean status = intent.getBooleanExtra(NetworkService.STATUS, false);
            if (status) {
                String url = String.format(IMG_PATTERN, id);
                image.setImageResource(R.drawable.ic_star);
                new LoadEvent().execute(id);

            }
        }
    };
    private DialogInterface.OnClickListener dialogClickListener =  new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                   if(event!=null) {
                       calenderReselver = new CalendarContentResolver(EventActivity.this);

                    if(calenderReselver.isInCalendar(event.getStart(),event.getName())){
                        Toast.makeText(EventActivity.this, getString(R.string.events_allready_created), Toast.LENGTH_LONG).show();
                    } else {
                        calenderReselver.addEventToCalendar(event);
                    }
                   }
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };

    public void progressBarOn(){
        if(progressBar==null) return;
        progressBar.setVisibility(View.VISIBLE);

    }

    public void progressOff(){
        if(progressBar==null) return;
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_layout_container);
        id = getIntent().getStringExtra(Utils.EVENT_ID);
        progressBarOn();
        serverApi = app().getServerApi();
        progressBar = (ProgressBar) findViewById(R.id.avloadingIndicatorView);
        View header = getLayoutInflater().inflate(R.layout.event_activity, null);
        description = (TextView) header.findViewById(R.id.descriprion_my_event);
        image = (ImageView) findViewById(R.id.image_my_event);
        inviteFriends = (LinearLayout) header.findViewById(R.id.layout_invite_friends);
        edit = (LinearLayout) header.findViewById(R.id.layout_edit_event);
        join = (CheckBox) header.findViewById(R.id.join_event);
        time = (TextView) header.findViewById(R.id.time_my_event);
        date = (TextView) header.findViewById(R.id.date_my_event);

        mapView = (MapView) header.findViewById(R.id.eventMap);

        mapView.onCreate(savedInstanceState);
        budget = (TextView) header.findViewById(R.id.my_event_budget);
        address = (TextView) header.findViewById(R.id.address_myevent);
        quantity = (TextView) header.findViewById(R.id.people_quantity_my_event);
        comment = (EditText) header.findViewById(R.id.make_comments);
        collapseToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
       // collapseToolbar.setCollapsedTitleTextColor(Color.WHITE);
        collapseToolbar.setCollapsedTitleTextAppearance(R.style.ActionBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra(Utils.EVENT_NAME));
        join.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                System.out.println(isChecked + " join event");
                if(isChecked) join.setText(R.string.leave_event);
                else join.setText(R.string.joned_event);
               new JoinEvent().execute(isChecked);
            }
        });
        inviteFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(EventActivity.this, UserListActivity.class );
                intent.putExtra(UserProfile.USER_ID, ((App)getApplication()).getUserProfile().getId());
                intent.putExtra(UserProfile.FOLLOW, UserListActivity.FRIENDS);
                intent.putExtra(Event.ID, id);
                startActivity(intent);

            }
        });
        String url = String.format(IMG_PATTERN, id);
        image.setImageURI(Uri.parse(url));
        new LoadEvent().execute(id);
        new LoadUsers().execute();

        initComments(header);
        LocalBroadcastManager.getInstance(this).registerReceiver(createUpdateReceiver, new IntentFilter(NetworkService.ACTION_UPDATE_EVENT));
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(BC_FILTER + id));
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(NetworkService.ACTION_UPDATE_EVENT));


// dialog

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        mapView.onDestroy();
    }

    private void initComments(View header) {

        recyclerView = (RecyclerView) findViewById(R.id.comments);
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lm);
        final int userId = app().getUserProfile().getId();
        commentAdapter = new CommentAdapter(this, header,new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Comment c = commentAdapter.getComments().get(position);
                if (c.getCreatedById() !=  userId && userId != Integer.parseInt(event.getCreatedBy().getId()))
                    return false;
                Bundle b = new Bundle();
                b.putInt("position", position);
                b.putString("comment_id", c.getId());
                b.putString("comment_text", c.getText());
                showDialog(DIALOG_REMOVE_COMMENT, b);
                return true;
            }
        });
        recyclerView.setAdapter(commentAdapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(lm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                Bundle b = new Bundle();
                b.putInt("page", page*5);
                getSupportLoaderManager().restartLoader(1, b, EventActivity.this).forceLoad();
            }
        });
        Bundle b = new Bundle();
        b.putInt("page", 0);
        getSupportLoaderManager().initLoader(1, new Bundle(), this).forceLoad();
        final View sendButton = header.findViewById(R.id.comment);
        sendButton.setEnabled(false);
        comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                sendButton.setEnabled(!s.toString().isEmpty());
            }
        });
    }

    public void onSendComment(View view) {
        final String str = comment.getText().toString();
        comment.getText().clear();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(comment.getWindowToken(), 0);

        new AsyncTask<Void, Void, Comment>() {
            @Override
            protected Comment doInBackground(Void... params) {
                return serverApi.sendComment(id, str);
            }

            @Override
            protected void onPostExecute(Comment comment) {
                if (comment == null) {
                    Toast.makeText(EventActivity.this, R.string.error_auth, Toast.LENGTH_SHORT).show();
                    EventActivity.this.comment.setText(str);
                    return;
                }
                commentAdapter.getComments().add(0, comment);
                commentAdapter.notifyItemInserted(1);
            }
        }.execute();
    }

    @Override
    public Loader<List<Comment>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<List<Comment>>(this) {
            @Override
            public List<Comment> loadInBackground() {
                return serverApi.getComments(EventActivity.this.id, args.getInt("page"));
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Comment>> loader, List<Comment> data) {
        if(data==null) return;
        commentAdapter.getComments().addAll(data);
        int size = commentAdapter.getItemCount();
        commentAdapter.notifyItemRangeInserted(size - data.size(), data.size());
    }

    @Override
    public void onLoaderReset(Loader<List<Comment>> loader) {
        commentAdapter.getComments().clear();

    }

    private class JoinEvent extends AsyncTask<Boolean, Void, Boolean> {
        protected Boolean doInBackground(Boolean... args) {
            Boolean join = args[0];
            JSONObject jsonObject = new JSONObject();
            if (join) {
                jsonObject = serverApi.joinEvent(id);
                return true;
            } else {
                jsonObject = serverApi.unjoinEvent(id);
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean)
            {
                if(!isJoined) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EventActivity.this);
                    builder.setMessage("Добавить в календарь?").setPositiveButton("Да", dialogClickListener)
                            .setNegativeButton("Нет", dialogClickListener).show();
                }

            } else {
                isJoined=false;

            }
        }
    }


//    private class LoadJoiners extends AsyncTask<String, Void, JSONObject>{
//
//        @Override
//        protected JSONObject doInBackground(String... params) {
//            String id = params[0];
//
//            JSONObject jsonObject = serverApi.loadJoiners(id);
//
//            return jsonObject;
//        }
//
//        @Override
//        protected void onPostExecute(JSONObject jsonObject) {
//            System.out.println(jsonObject + " ------------------========================");
//            List<User> users = Utils.parseUsersList(jsonObject);
//            System.out.println(users.toArray().toString());
//            quantity.setText(users.size()+"");
//
//
//        }
//    }



    private class LoadEvent extends AsyncTask<String, Void, JSONObject> {
        protected JSONObject doInBackground(String... args) {
           String id = args[0];
            JSONObject jsonObject = serverApi.loadEvent(id);
            return jsonObject;
        }


        protected void onPostExecute(JSONObject result) {
            try {
                System.out.println(result);
                if(result==null){
                    Toast.makeText(EventActivity.this, getString(R.string.on_internet_connection), Toast.LENGTH_LONG).show();
                    finish();
                }
                event = Utils.parseEvent(result);
                collapseToolbar.setTitle(event.getName());

                description.setText(event.getDescription());
                isJoined = event.getJoin();


                join.setChecked(event.getJoin());
                if(event.getJoin()==true){
                    join.setText(R.string.leave_event);
                }
                quantity.setText(event.getAttendances()+"");
                id_user =  ((App) getApplication()).pref().getInt(UserProfile.USER_ID, -1);
                if(id_user==event.getUserId()){
                    join.setVisibility(View.GONE);
                    edit.setVisibility(View.VISIBLE);
                    edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(EventActivity.this, NewEventActivity.class);
                            intent.putExtra(Event.NAME, event.getName());
                            intent.putExtra(Event.DESCRIPTION, event.getDescription());

                          int arr[] = new int[event.getTag().size()];

                                for (int i = 0; i < event.getTag().size(); i++) {
                                    arr[i] = event.getTag().get(i);
                                }

                            intent.putExtra(Event.TAGS, arr);
                            intent.putExtra(Event.START, event.getStart());
                            intent.putExtra(Event.END, event.getEnd());
                            intent.putExtra(Event.ID, event.getId());

                            intent.putExtra(Event.PLACE, event.getAddress()!=null?event.getAddress():"");
                            intent.putExtra(Event.LAT, event.getPlace()!=null?event.getPlace().get(0):0);
                            intent.putExtra(Event.LNG, event.getPlace()!=null?event.getPlace().get(1):0);
                            intent.putExtra(Event.BUDGET_MIN, event.getBudgetMin());
                            intent.putExtra(Event.BUDGET_MAX, event.getBudgetMax());


                            startActivity(intent);
                        }
                    });
                }
              time.setText(Utils.parseDataDate(event.getStart()));
                String eventEndTime = Utils.parseDataTime(event.getEnd());

                date.setText(Utils.parseDataTime(event.getStart()) + " - " + eventEndTime.substring(eventEndTime.length()-5, eventEndTime.length()));


                if(event.getBudgetMax() == event.getBudgetMin()){
                    budget.setText("" + event.getBudgetMin());
                } else {
                    budget.setText(event.getBudgetMin()+ " - " + event.getBudgetMax());
                }

                if(event.getPlace()!=null){

                    lat = event.getPlace().get(0);
                    lng = event.getPlace().get(1);
                    mapView.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            LatLng pin = new LatLng(lat, lng);

                            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                            googleMap.setMyLocationEnabled(true);
                            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 500, null);

                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pin, 15));
                            googleMap.addMarker(new MarkerOptions()
                                    .position(pin).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin))
                                    .title(event.getName()));
                        }
                    });

                }

                address.setText(event.getAddress());
            } catch (Exception e) {
                e.printStackTrace();
            }
            progressOff();
        }
    }


    public void onMap(View v){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra(Event.LAT, lat);
        intent.putExtra(Event.LNG, lng);
        startActivity(intent);
    }

    public void onJoiners(View v){

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent();
            intent.putExtra(Event.NAME, event.getName());
            intent.putExtra(Event.START, event.getStart());
            intent.putExtra(Event.BUDGET_MIN, event.getBudgetMin());
            setResult(RESULT_OK, intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed(){
        Intent intent = new Intent();
        intent.putExtra(Event.NAME, event.getName());
        intent.putExtra(Event.START, event.getStart());
        intent.putExtra(Event.BUDGET_MIN, event.getBudgetMin());
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    protected void onPrepareDialog(int id, final Dialog dialog, final Bundle args) {
        final AlertDialog ad = (AlertDialog)dialog;
        ad.setMessage(args.getString("comment_text"));
        View.OnClickListener dialogClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View btn) {
                ad.dismiss();
                int pos = args.getInt("position");
                commentAdapter.getComments().remove(pos);
                commentAdapter.notifyItemRemoved(pos+1);
                if (btn.getTag() != null) {
                    new AsyncTask<Void, Void, Void>() {

                        @Override
                        protected Void doInBackground(Void... params) {
                            serverApi.deleteComment(event.getId(), args.getString("comment_id"));
                            return null;
                        }
                    }.execute();

                }
            }
        };
        ad.getButton(DialogInterface.BUTTON_POSITIVE).setTag(new Object());
        ad.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(dialogClickListener);
        ad.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(dialogClickListener);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_REMOVE_COMMENT) {
            AlertDialog.Builder ab = new AlertDialog.Builder(this);
            ab.setTitle(R.string.remove_this_comment);
            ab.setPositiveButton(android.R.string.yes, null);
            ab.setNegativeButton(android.R.string.no, null);
            ab.setMessage("");
            return ab.create();
        }
        return null;

    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Comment c = null;
            try {
                c = new Comment(new JSONObject(intent.getStringExtra("data")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (!commentAdapter.getComments().isEmpty() && TextUtils.equals(c.getId(), commentAdapter.getComments().get(0).getId()))
                return;
            commentAdapter.getComments().add(0, c);
            commentAdapter.notifyItemInserted(1);
        }
    };

    class LoadUsers extends AsyncTask<Void, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(Void... params) {
            App app = (App) getApplication();

             return app.getServerApi().loadFollowing(app.getUserProfile().getId());
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if(jsonObject!=null){
                JSONArray arr= null;
                try {
                     arr = jsonObject.getJSONArray("results");

                for(int i =0; i<arr.length(); i++){
                    User user = User.parseUser( arr.getJSONObject(i));
                    users.add(user);
                }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }


        }
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }



    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
