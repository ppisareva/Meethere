package com.example.polina.meethere.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.polina.meethere.R;
import com.example.polina.meethere.adapters.Category;
import com.example.polina.meethere.model.App;
import com.example.polina.meethere.model.Event;
import com.example.polina.meethere.model.User;
import com.example.polina.meethere.model.UserProfile;
import com.example.polina.meethere.network.NetworkService;

import org.apmem.tools.layouts.FlowLayout;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyInformationActivity extends AppCompatActivity {
    EditText phone;
    EditText address;
    EditText email;
    CheckBox gender;
    TextView birthday;
    Date date = null;
    SimpleDateFormat viewData;
    SimpleDateFormat comeFromServer;
    Spinner spinnerCategory ;
    JSONObject jsonObject = new JSONObject();
    FlowLayout flowLayout;
   Set<Integer> categoryList = new HashSet<>();



    View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            if (!hasFocus) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        comeFromServer = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        viewData =  new SimpleDateFormat("MM/dd/yyyy");








        UserProfile userProfile = ((App)getApplication()).getUserProfile();
        setContentView(R.layout.activity_my_information);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        flowLayout = (FlowLayout)findViewById(R.id.flow_layout);
        phone = (EditText) findViewById(R.id.edit_telephone);
        phone.setOnFocusChangeListener(onFocusChangeListener);
        phone.setHint(!userProfile.getPhone().equals("null")?userProfile.getPhone():getString(R.string.set_phone));
        address = (EditText) findViewById(R.id.edit_address);
        address.setOnFocusChangeListener(onFocusChangeListener);
        address.setHint(!userProfile.getLocation().equals("null")?userProfile.getLocation():getString(R.string.set_location));
        email = (EditText) findViewById(R.id.edit_email);
        email.setOnFocusChangeListener(onFocusChangeListener);
        email.setHint(!userProfile.getEmail().equals("null")?userProfile.getEmail():getString(R.string.set_email));
        gender = (CheckBox) findViewById(R.id.gender);
        gender.setChecked(userProfile.isGender());
        for(String i: userProfile.getCategory()){
           categoryList.add(Integer.getInteger(i));
           addView(Integer.parseInt(i));
        }


        birthday = (TextView)findViewById(R.id.birthday);
        try {
            date = comeFromServer.parse(userProfile.getBirthday());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        birthday.setText(!userProfile.getBirthday().equals("null")?viewData.format(date):getString(R.string.set_birthday));

        spinnerCategory = (Spinner) findViewById(R.id.spinner_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    public void onBirthDay(View v){
        chooseTimeDialog();

    }




    public  void onAddPreferences(View v){
        int id = spinnerCategory.getSelectedItemPosition();
        if(categoryList.contains(id)) {
            Toast.makeText(this, "Категория уже добавленна", Toast.LENGTH_LONG).show();
            return;
        }
        categoryList.add(id);
        addView(id);

    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            flowLayout.removeView(v);
        }
    };

    private void addView(int id) {
        TextView textView = new TextView(this);
        textView.setBackground(getResources().getDrawable(R.drawable.preferences));
        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_cencel, 0);
        textView.setClickable(true);

        textView.setOnClickListener(listener);
        textView.setText((getResources().getStringArray(R.array.category))[id]);
        FlowLayout.LayoutParams params =  new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(5, 5, 5, 5);

        flowLayout.addView(textView, params);

    }


    public void chooseTimeDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog;
                datePickerDialog = new DatePickerDialog(this, onDateListener, year, month, day);
                datePickerDialog.show();

    }
    DatePickerDialog.OnDateSetListener onDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            date = new Date(year-1900, monthOfYear, dayOfMonth);
            birthday.setText(viewData.format(date));
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        if(id==R.id.action_new_event) {

            try {
                if(!TextUtils.isEmpty(phone.getText())) jsonObject.put(UserProfile.PHONE, phone.getText());

              if(!TextUtils.isEmpty(address.getText())) jsonObject.put(UserProfile.LOCATION,address.getText());
                if(!TextUtils.isEmpty(email.getText()))jsonObject.put(UserProfile.EMAIL,email.getText());
               if(date!=null) jsonObject.put(UserProfile.BIRTHDAY,comeFromServer.format(date));
                jsonObject.put(UserProfile.GENDER ,gender.isChecked());
                if(!categoryList.isEmpty()){
                    JSONArray array = new JSONArray();
                    for(Integer i: categoryList){
                        array.put(i);
                    }
                    jsonObject.put(UserProfile.CATEGORY,array);
                }

                new RefreshMyInfo().execute(jsonObject);


            } catch (Exception e){

            }

        }
        return super.onOptionsItemSelected(item);
    }

    class RefreshMyInfo extends AsyncTask<JSONObject, Void, JSONObject>{
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            System.out.println(jsonObject);
          finish();
        }

        @Override
        protected JSONObject doInBackground(JSONObject... params) {
            App app = (App)getApplication();
          return   (app.getServerApi().updateProfile(params[0], String.valueOf(app.getUserProfile().getId())) );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_event, menu);
        return true;
    }






}
