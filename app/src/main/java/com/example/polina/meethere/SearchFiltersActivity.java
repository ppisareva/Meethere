package com.example.polina.meethere;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.edmodo.rangebar.RangeBar;

public class SearchFiltersActivity extends AppCompatActivity {

    LinearLayout layout;
    Button startDate;
    Button endDate;
    RangeBar rangeBarAge;
    RangeBar rangeBarBudget;
    TextView rightIndexDistance;
    TextView rightIndexAge;
    TextView leftIndexDictance;
    TextView leftIndexAge;
    TextView rightIndexBudget;
    TextView leftIndexBudget;
    ImageView imageView;
    public static final int MIN_AGE = 15;
    public static final int MIN_BUDGET = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_filters);
        imageView = (ImageView) findViewById(R.id.image_price);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Spinner spinnerCategory = (Spinner) findViewById(R.id.spinner_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        startDate = (Button) findViewById(R.id.button_from);
        startDate.setText(Utils.getCurrentDate());
        endDate = (Button) findViewById(R.id.button_till);
        endDate.setText(Utils.getCurrentTimePlusHour()[0]);
        rangeBarAge =(RangeBar) findViewById(R.id.range_bar_ege);
        rangeBarBudget = (RangeBar) findViewById(R.id.range_bar_budget);
        leftIndexAge = (TextView)findViewById(R.id.leftIndexValue);
        leftIndexDictance = (TextView) findViewById(R.id.leftIndexValue_distance);
        leftIndexBudget = (TextView) findViewById(R.id.leftIndexValue_budget);
        rightIndexAge = (TextView) findViewById(R.id.rightIndexValue);
        rightIndexDistance = (TextView) findViewById(R.id.rightIndexValue_distance);
        rightIndexBudget = (TextView) findViewById(R.id.rightIndexValue_budget);
        rangeBarAge.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onIndexChangeListener(RangeBar rangeBar, int left, int right) {

                leftIndexAge.setText("" + (MIN_AGE + left));
                rightIndexAge.setText("" + (MIN_AGE + 1 + right));
                System.out.println(rangeBarAge.getRightIndex());

            }
        });

        rangeBarBudget.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onIndexChangeListener(RangeBar rangeBar, int left, int right) {

                leftIndexBudget.setText("" + (MIN_BUDGET*left));
                rightIndexBudget.setText(""  + MIN_BUDGET*right);
            }
        });



        rangeBarAge.setTickHeight(0);
        rangeBarBudget.setTickHeight(0);
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
