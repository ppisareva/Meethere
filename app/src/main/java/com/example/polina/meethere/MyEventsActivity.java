package com.example.polina.meethere;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.polina.meethere.fragments.MyEventsListsFragment;

public class MyEventsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       MyEventsListsFragment myEventsListsFragment = MyEventsListsFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment, myEventsListsFragment)
                .addToBackStack(null).commit();
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
