package com.example.polina.meethere.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.example.polina.meethere.R;
import com.example.polina.meethere.Utils;
import com.example.polina.meethere.model.App;
import com.example.polina.meethere.model.User;
import com.example.polina.meethere.model.UserProfile;
import com.example.polina.meethere.network.ServerApi;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FollowList extends AppCompatActivity {
    ListView listView;
    ServerApi serverApi;
    public static final int FOLLOWER = 0;
    public static final int FOLLOWING = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_list);
        serverApi = ((App)getApplication()).getServerApi();
        listView  = (ListView) findViewById(R.id.list_follow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Integer id  = getIntent().getIntExtra(UserProfile.USER_ID, -1);
        final Integer followTag = getIntent().getIntExtra(UserProfile.FOLLOW, -1);
        getSupportActionBar().setTitle(followTag==FOLLOWER?getString(R.string.followers):getString(R.string.followings));

        new AsyncTask<Integer, Void, JSONObject>() {
            @Override
            protected JSONObject doInBackground(Integer... params) {
                JSONObject jsonObject = new JSONObject();
                if(followTag==FOLLOWING) {
                    jsonObject = serverApi.loadFollowing(params[0]);
                }
                if(followTag==FOLLOWER){
                    jsonObject = serverApi.loadFollowers(params[0]);
                }
                return jsonObject;
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                ArrayList<User> users = parsUsers(jsonObject);
                        FollowUserAdapter followUserAdapter = new FollowUserAdapter(FollowList.this, users);
                        listView.setAdapter(followUserAdapter);
            }
        }.execute(id);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private ArrayList<User> parsUsers(JSONObject jsonObject){
        ArrayList <User> users = new ArrayList<>();
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
        return users;
    }

    public class FollowUserAdapter extends ArrayAdapter<User> {

        Context context;
        List<User> list;
        Set<Integer> positionsList = new HashSet<>();


        public FollowUserAdapter(Context context,  List<User> list) {
            super(context, R.layout.friends_list, list);
            this.context = context;
            this.list = list;
        }

        class ViewHolder {
            public TextView name;
            public SimpleDraweeView image;
        }




        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = null;
            if (convertView == null) {
                final LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                view = inflater.inflate(R.layout.friends_list, null);
                final ViewHolder viewHolder = new ViewHolder();
                viewHolder.name = (TextView) view.findViewById(R.id.dialog_text);
                viewHolder.image = (SimpleDraweeView) view.findViewById(R.id.dialog_image);
                ((CheckBox) view.findViewById(R.id.dialog_checkbox)).setVisibility(View.GONE);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(FollowList.this, UserProfileActivity.class);
                        intent.putExtra(Utils.USER_ID, list.get(position).getId());
                        startActivity(intent);
                    }
                });

                view.setTag(viewHolder);
            } else {
                view = convertView;
            }

            ViewHolder holder = (ViewHolder) view.getTag();
            holder.name.setText(list.get(position).getFirstName() + " " +list.get(position).getLastName());
            holder.image.setImageURI(Uri.parse(list.get(position).getImage()));
            RoundingParams roundingParams = RoundingParams.asCircle();
            holder.image.getHierarchy().setRoundingParams(roundingParams);
            return view;
        }
    }
}
