package com.example.polina.meethere.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.polina.meethere.R;
import com.example.polina.meethere.Utils;
import com.example.polina.meethere.adapters.CommentAdapter;
import com.example.polina.meethere.data.Comment;
import com.example.polina.meethere.model.Event;
import com.example.polina.meethere.model.User;
import com.example.polina.meethere.network.ServerApi;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EventActivity extends AbstractMeethereActivity implements LoaderManager.LoaderCallbacks<List<Comment>> {
    TextView description;
    ImageView image;
    String id;
    CheckBox join;
    TextView time;
    TextView budget;
    TextView address;
    TextView    quantity;
    EditText comment;
    RecyclerView recyclerView;
    CommentAdapter commentAdapter;
    ServerApi serverApi;
    Double lat;
    Double lng;

    public static final String IMG_PATTERN = "https://s3-us-west-1.amazonaws.com/meethere/%s.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_layout_container);
        id = getIntent().getStringExtra(Utils.EVENT_ID);
        serverApi = app().getServerApi();
        View header = getLayoutInflater().inflate(R.layout.event_header, null);
        description = (TextView) header.findViewById(R.id.descriprion_my_event);
        image = (ImageView) findViewById(R.id.image_my_event);
        join = (CheckBox) header.findViewById(R.id.join_event);
        time = (TextView) header.findViewById(R.id.time_my_event);
        budget = (TextView) header.findViewById(R.id.my_event_budget);
        address = (TextView) header.findViewById(R.id.address_myevent);
        quantity = (TextView) header.findViewById(R.id.people_quantity_my_event);
        comment = (EditText) header.findViewById(R.id.make_comments);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra(Utils.EVENT_NAME));
        join.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                System.out.println(isChecked + " join event");
               new JoinEvent().execute(isChecked);
            }
        });
        String url = String.format(IMG_PATTERN, id);
        image.setImageURI(Uri.parse(url));
        new LoadEvent().execute(id);
        new LoadJoiners().execute(id);
        initComments(header);
    }


    private void initComments(View header) {

        recyclerView = (RecyclerView) findViewById(R.id.comments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        commentAdapter = new CommentAdapter(this, header);
        recyclerView.setAdapter(commentAdapter);
        getSupportLoaderManager().initLoader(1, null, this).forceLoad();
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
                commentAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    public Loader<List<Comment>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<Comment>>(this) {
            @Override
            public List<Comment> loadInBackground() {
                return serverApi.getComments(EventActivity.this.id, 0);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Comment>> loader, List<Comment> data) {
        commentAdapter.setComments(data);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(commentAdapter);
    }

    @Override
    public void onLoaderReset(Loader<List<Comment>> loader) {
        commentAdapter.setComments(null);

    }

    private class JoinEvent extends AsyncTask<Boolean, Void, Void> {
        protected Void doInBackground(Boolean... args) {
            Boolean join = args[0];
            JSONObject jsonObject = new JSONObject();
            if (join) {
                jsonObject = serverApi.joinEvent(id);
            } else {
                jsonObject = serverApi.unjoinEvent(id);
            }
            System.out.println(jsonObject);


            return null;
        }
        }

//    TODO: Polya, it's wrong!!!! As me, how to do it correctly
    private class LoadJoiners extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... params) {
            String id = params[0];

            JSONObject jsonObject = serverApi.loadJoiners(id);

            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            System.out.println(jsonObject + " ------------------========================");
            List<User> users = Utils.parseJoinedList(jsonObject);
            System.out.println(users.toArray().toString());
            quantity.setText(users.size()+"");


        }
    }



    private class LoadEvent extends AsyncTask<String, Void, JSONObject> {
        protected JSONObject doInBackground(String... args) {
           String id = args[0];

            JSONObject jsonObject = serverApi.loadEvent(id);

            return jsonObject;

        }


        protected void onPostExecute(JSONObject result) {
            try {
                Event event = Utils.parseEvent(result);
                description.setText(event.getDescription());
                join.setChecked(event.getJoin());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Date dateStart = simpleDateFormat.parse(event.getStart());
                Date dateEnd = simpleDateFormat.parse(event.getEnd());
                simpleDateFormat = new SimpleDateFormat("EEE, MMM dd kk:mm");




                time.setText(simpleDateFormat.format(dateStart) + " - " + simpleDateFormat.format(dateEnd));
                if(event.getBudgetMax() == event.getBudgetMin()){
                    budget.setText("" + event.getBudgetMin());
                } else {
                    budget.setText(event.getBudgetMin()+ " - " + event.getBudgetMax());
                }

                if(event.getPlace()!=null){
                    address.setClickable(true);
                    lat = event.getPlace().get(1);
                    lng = event.getPlace().get(0);
                    address.setText(R.string.see_on_map);
                } else {
                    address.setCompoundDrawables(null, null, null, null);
                    address.setText(event.getAddress());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
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
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
