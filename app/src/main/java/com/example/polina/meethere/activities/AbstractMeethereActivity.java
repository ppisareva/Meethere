package com.example.polina.meethere.activities;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import com.example.polina.meethere.model.App;

/**
 * Created by ko3a4ok on 5/7/16.
 */
public class AbstractMeethereActivity extends AppCompatActivity {

    protected App app() {
        return (App) getApplication();
    }
}
