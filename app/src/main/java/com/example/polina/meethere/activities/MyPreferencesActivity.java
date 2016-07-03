package com.example.polina.meethere.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.example.polina.meethere.PreferencesAdapter;
import com.example.polina.meethere.R;

import java.util.ArrayList;
import java.util.List;

public class MyPreferencesActivity extends AppCompatActivity {
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_preferences);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editText = (EditText)findViewById(R.id.add_preferences);
        RecyclerViewHeader header = (RecyclerViewHeader) findViewById(R.id.header);
        RecyclerView list = (RecyclerView) findViewById(R.id.all_preferences);
        PreferencesAdapter adapter = new PreferencesAdapter(newList());
        list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        list.setAdapter(adapter);
        header.attachTo(list);

    }

    public void onAddPreferences (View v){
        editText.setText("");
    }

    private List<String> newList() {
        List<String> list = new ArrayList<>();
        list.add("Игры");
        list.add("На природе");
        list.add("Межвидовое порно");
        list.add("Кольянчиг");
        list.add("Синька");
        list.add("С друзьями");
        list.add("Для мамочек");
        list.add("Наука и техника");
        list.add("Игры");
        list.add("На природе");
        list.add("Межвидовое порно");
        list.add("Кольянчиг");
        list.add("Синька");
        list.add("С друзьями");
        list.add("Для мамочек");
        list.add("Наука и техника");
             return list;
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
