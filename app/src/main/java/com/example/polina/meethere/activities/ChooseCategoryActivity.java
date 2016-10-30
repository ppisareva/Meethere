package com.example.polina.meethere.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.polina.meethere.CategoryChooseAdapter;
import com.example.polina.meethere.R;
import com.example.polina.meethere.model.App;
import com.example.polina.meethere.model.Event;
import com.example.polina.meethere.model.UserProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChooseCategoryActivity extends AbstractMeethereActivity {
    CategoryChooseAdapter adapter;
    App app ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (App)getApplication();
        setContentView(R.layout.activity_choose_category);
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.category_choose);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        String[] myResArray = getResources().getStringArray(R.array.category);
        List<String> myResArrayList = Arrays.asList(myResArray);
        Set<Integer> checked = new HashSet<>();
        adapter = new CategoryChooseAdapter(myResArrayList,checked);
        recyclerView.setAdapter(adapter);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_event, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_new_create) {
            if(adapter.getTags().size()==0){
                Toast.makeText(this, "SELECT AT LEAST ONE CATEGORY", Toast.LENGTH_LONG);
                return true;
            }

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(UserProfile.CATEGORY, new JSONArray(adapter.getTags()));
                new Update().execute(jsonObject);

            }catch (Exception e){
                e.printStackTrace();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    class Update extends AsyncTask<JSONObject, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(JSONObject... params) {
            return app.getServerApi().updateProfile(params[0], app.getUserProfile().getId()+"");
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            try {
                app.saveUserProfile(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            goToMain();

        }
    }

}
