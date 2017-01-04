package com.tolpa.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.tolpa.R;
import com.tolpa.fragments.MyEventsListsFragment;

public class MyEventsActivity extends AppCompatActivity {
    MyEventsListsFragment myEventsListsFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.my_events));
       myEventsListsFragment = MyEventsListsFragment.newInstance();
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
