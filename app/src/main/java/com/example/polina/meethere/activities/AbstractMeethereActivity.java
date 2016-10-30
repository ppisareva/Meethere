package com.example.polina.meethere.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.example.polina.meethere.model.App;
import com.example.polina.meethere.model.UserProfile;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ko3a4ok on 5/7/16.
 */
public class AbstractMeethereActivity extends AppCompatActivity {

    protected App app() {
        return (App) getApplication();
    }

    protected void goToMain() {
        ActivityCompat.finishAffinity(this);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    protected void postLogin(JSONObject jsonObject) {
        try {
            if (jsonObject.has("profile") && jsonObject.optJSONObject("profile").optJSONArray(UserProfile.CATEGORY).length() == 0) {
                gotoChooseCategory();
            } else {
                goToMain();
            }
        } catch (Exception e) {
            e.printStackTrace();
            goToMain();
        }

    }

    private void gotoChooseCategory() {
        finish();
        startActivity(new Intent(this, ChooseCategoryActivity.class));
    }


}
