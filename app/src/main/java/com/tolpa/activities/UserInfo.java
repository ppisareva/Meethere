package com.tolpa.activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tolpa.R;
import com.tolpa.Utils;
import com.tolpa.model.App;
import com.tolpa.model.User;
import com.tolpa.model.UserProfile;
import com.tolpa.network.ServerApi;

import org.apmem.tools.layouts.FlowLayout;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class UserInfo extends AppCompatActivity {

    TextView phone;
    TextView address;
    TextView email;
    CheckBox gender;
    TextView birthday;
    SimpleDateFormat viewData;
    SimpleDateFormat comeFromServer;
    FlowLayout flowLayout;
    ServerApi serverApi;
    UserProfile userProfile;
    ArrayList<Integer> categoryList = new ArrayList<>();
    LinearLayout contactInfo;
    LinearLayout personalInfo;
    LinearLayout preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        serverApi = ((App)getApplication()).getServerApi();
        String id = getIntent().getStringExtra(User.ID);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        contactInfo = (LinearLayout) findViewById(R.id.contact_info);
        personalInfo = (LinearLayout) findViewById(R.id.personal_info);
        preferences = (LinearLayout) findViewById(R.id.preferences);
        flowLayout = (FlowLayout)findViewById(R.id.flow_layout);
        phone = (TextView) findViewById(R.id.edit_telephone);
        address = (TextView) findViewById(R.id.edit_address);
        email = (TextView) findViewById(R.id.edit_email);
        gender = (CheckBox) findViewById(R.id.gender);
        birthday = (TextView)findViewById(R.id.birthday);
        new AsyncTask<String, Void, JSONObject>() {
            @Override
            protected JSONObject doInBackground(String... params) {
                return serverApi.loadUserProfile(params[0]);
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                userProfile = UserProfile.parseUserProfile(jsonObject);
                getSupportActionBar().setTitle(userProfile.getName());
                if(jsonObject.has(UserProfile.EMAIL)||jsonObject.has(UserProfile.PHONE)){
                    contactInfo.setVisibility(View.VISIBLE);
                    if(userProfile.getEmail()!=null){
                        email.setText(userProfile.getEmail());
                    } else{
                        findViewById(R.id.contact_email).setVisibility(View.GONE);
                    }

                    if(jsonObject.has(UserProfile.PHONE)){
                        phone.setText(userProfile.getPhone());
                    } else {
                        findViewById(R.id.phone_layout).setVisibility(View.GONE);
                    }
                }

                if(jsonObject.has(UserProfile.BIRTHDAY)||jsonObject.has(UserProfile.LOCATION)||jsonObject.has(UserProfile.GENDER)){
                    personalInfo.setVisibility(View.VISIBLE);
                    if(userProfile.getBirthday()!=null){
                        birthday.setText(Utils.parsBirthDay(userProfile.getBirthday()));
                    } else {
                        findViewById(R.id.personal_birthday).setVisibility(View.GONE);
                    }
                    if(userProfile.getLocation()!=null){
                        address.setText(userProfile.getLocation());
                    } else {
                        findViewById(R.id.personal_location).setVisibility(View.GONE);
                    }
                    if(jsonObject.has(UserProfile.GENDER)){
                        gender.setChecked(userProfile.isGender());
                    } else {
                        findViewById(R.id.personal_gender).setVisibility(View.GONE);
                    }
                }

                if(jsonObject.has(UserProfile.CATEGORY)) {
                    preferences.setVisibility(View.VISIBLE);
                    for (String i : userProfile.getCategory()) {
                            categoryList.add(Integer.valueOf(i));
                            addView(Integer.parseInt(i));
                    }
                }
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

    private void addView(int id) {
        TextView textView = new TextView(this);
        textView.setBackground(getResources().getDrawable(R.drawable.preferences));
        textView.setClickable(true);
        textView.setTag(id);
        textView.setText((getResources().getStringArray(R.array.category))[id]);
        FlowLayout.LayoutParams params =  new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(5, 5, 5, 5);
        flowLayout.addView(textView, params);

    }


}
