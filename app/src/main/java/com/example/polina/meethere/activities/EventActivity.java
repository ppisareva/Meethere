package com.example.polina.meethere.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.polina.meethere.R;
import com.example.polina.meethere.Utils;
import com.example.polina.meethere.model.Event;
import com.example.polina.meethere.model.User;
import com.example.polina.meethere.network.ServerApi;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EventActivity extends AbstractMeethereActivity {
    TextView description;
    ImageView image;
    String id;
    CheckBox join;
    TextView time;
    TextView budget;
    TextView address;
    TextView    quantity;
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
        description = (TextView) findViewById(R.id.descriprion_my_event);
        image = (ImageView) findViewById(R.id.image_my_event);
        join = (CheckBox) findViewById(R.id.join_event);
        time = (TextView) findViewById(R.id.time_my_event);
        budget = (TextView) findViewById(R.id.my_event_budget);
        address = (TextView) findViewById(R.id.address_myevent);
        quantity = (TextView) findViewById(R.id.people_quantity_my_event);

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
