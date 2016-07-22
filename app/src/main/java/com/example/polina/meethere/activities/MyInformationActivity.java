package com.example.polina.meethere.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.polina.meethere.R;

public class MyInformationActivity extends AppCompatActivity {
    EditText phone;
    EditText address;
    EditText email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_information);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        phone = (EditText) findViewById(R.id.edit_telephone);
        address = (EditText) findViewById(R.id.edit_address);
        email = (EditText) findViewById(R.id.edit_email);
        Spinner spinnerCategory = (Spinner) findViewById(R.id.spinner_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        if(id==R.id.action_new_event){
           finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_event, menu);
        return true;
    }

  public void onDateOfBirth(View v){

  }
    public void onGander(View v){

    }


}
