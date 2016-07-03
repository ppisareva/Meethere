package com.example.polina.meethere;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.polina.meethere.model.App;
import com.example.polina.meethere.model.Event;
import com.example.polina.meethere.model.User;
import com.example.polina.meethere.network.ServerApi;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EventActivity extends AppCompatActivity {
    TextView name;
    TextView description;
    ImageView image;
    String id;
    CheckBox join;
    TextView time;
    TextView budget;
    TextView address;
    TextView    quantity;
    ImageView imageMap;
    ServerApi serverApi;
    Double lat;
    Double lng;
    public static final String IMG_PATTERN = "https://s3-us-west-1.amazonaws.com/meethere/%s.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        id = getIntent().getStringExtra(Utils.EVENT_ID);
        serverApi = ((App) getApplication()).getServerApi();
        name =  (TextView) findViewById(R.id.name_my_event);
        description = (TextView) findViewById(R.id.descriprion_my_event);
        image = (ImageView) findViewById(R.id.image_my_event);
        join = (CheckBox) findViewById(R.id.join_event);
        time = (TextView) findViewById(R.id.time_my_event);
        budget = (TextView) findViewById(R.id.my_event_budget);
        address = (TextView) findViewById(R.id.address_myevent);
        imageMap = (ImageView) findViewById(R.id.image_map);
        quantity = (TextView) findViewById(R.id.people_quantity_my_event);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        join.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                System.out.println(isChecked + " join event");
               new JoinEvent().execute(isChecked);
            }
        });
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
            name.setText(event.getName());
            description.setText(event.getDescription());
                join.setChecked(event.getJoin());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Date dateStart = simpleDateFormat.parse(event.getStart());
                Date dateEnd = simpleDateFormat.parse(event.getEnd());
                simpleDateFormat = new SimpleDateFormat("EEE, MMM dd kk:mm");




            time.setText(simpleDateFormat.format(dateStart) + " - " + simpleDateFormat.format(dateEnd));
                if(event.getBudgetMax()-event.getBudgetMin()==0){
                    budget.setText(event.getBudgetMin()+"");
                } else {
                    budget.setText(event.getBudgetMin()+ " - " + event.getBudgetMax());
                }

            if(event.getPlace()!=null){
                address.setText(getResources().getString(R.string.see_on_map));
                imageMap.setVisibility(View.VISIBLE);
                lat = event.getPlace().get(1);
                lng = event.getPlace().get(0);
            } else {
                address.setText(event.getAddress());
            }
            String url = String.format(IMG_PATTERN, id);
            image.setImageURI(Uri.parse(url));
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
