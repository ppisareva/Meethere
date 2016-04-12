package com.example.polina.meethere;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.polina.meethere.Adapters.AllCategory;

import java.util.ArrayList;
import java.util.List;

public class CategoryListActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        AllCategoryListAdapter listAdapter = new AllCategoryListAdapter(getAllCategory());
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.all_category_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(listAdapter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public List<AllCategory> getAllCategory() {
        List<AllCategory> allCategory= new ArrayList<>();
        String arr [] = getResources().getStringArray(R.array.category);
        allCategory.add(new AllCategory(R.drawable.ic_people_black_24dp, arr[1]));
        allCategory.add(new AllCategory(R.drawable.ic_palette_black_24dp, arr[2]));
        allCategory.add(new AllCategory(R.drawable.ic_book_black_24dp, arr[3]));
        allCategory.add(new AllCategory(R.drawable.ic_business, arr[4]));
        allCategory.add(new AllCategory(R.drawable.ic_car, arr[5]));
        for(int i =6; i<arr.length; i++){
            allCategory.add(new AllCategory(R.drawable.ic_android, arr[i]));
        }
        return allCategory;
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

