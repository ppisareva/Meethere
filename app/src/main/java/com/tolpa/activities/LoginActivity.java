package com.tolpa.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tolpa.R;
import com.tolpa.model.App;
import com.tolpa.views.FadeView;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends AbstractMeethereActivity {
    LoginButton loginButton;
    CallbackManager callbackManager;

    private static final int[] PROMOS_TEXT = {R.string.promo_1, R.string.promo_2, R.string.promo_3};
    private static final int PROMO_SIZE = PROMOS_TEXT.length;
    private static final long UPDATE_TIME_INTERVAL = 5000;

    private ViewPager pager;
    private FadeView background;
    App app;
    ProgressBar loader;
    TextView userExist;
    Boolean newUser = false;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseInstanceId.getInstance().getToken();
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_registration);
        app = (App) getApplication();
        loader = (ProgressBar) findViewById(R.id.avloadingIndicatorView);
        callbackManager = CallbackManager.Factory.create();

//        Vlad's login
//      new LoginWithFbTask().execute("EAAGlmxaRKmEBAMuE70E98Brd6gRsaKKdqrInH2w2Ju9aDa1vZAlxrlilDOUp1h7l52LK6iMbqXZCHyKkAtWrMBCcyBlAuVB6kxfEEOoVATdesZAp5y4fkhnb95HZCNL4xBFncr7EdTaXL2PmDrT8HMmJTY9PKZA2nKO5NF0AwpUUYVifUiKaW0tEEDTXVfnsZD");
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                new LoginWithFbTask().execute(loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        background = (FadeView) findViewById(R.id.fade_view);
        background.setVisibility(View.VISIBLE);
        initPager();
        initTimer();
        new PingTask().execute();
    }


    private void initTimer() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isFinishing()) cancel();
                        int idx = pager.getCurrentItem();
                        if (idx + 100 > pager.getAdapter().getCount())
                            pager.setCurrentItem(100 / PROMO_SIZE * PROMO_SIZE);
                        else
                            pager.setCurrentItem(idx + 1, true);
                    }
                });
            }
        }, UPDATE_TIME_INTERVAL, UPDATE_TIME_INTERVAL);
    }

    private void initPager() {
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new PagerAdapter() {

            @Override
            public int getCount() {
                return 1000;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View root = getLayoutInflater().inflate(R.layout.promo_text, null);
                TextView tv = (TextView) root.findViewById(R.id.text);
                container.addView(root, 0);
                tv.setText(PROMOS_TEXT[position % PROMOS_TEXT.length]);
                return root;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
        pager.setCurrentItem(pager.getAdapter().getCount() / 2 / PROMO_SIZE * PROMO_SIZE, true);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                background.setProgress(positionOffset, position % PROMO_SIZE);
                background.invalidate();
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class LoginWithFbTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            findViewById(R.id.avloadingIndicatorView).setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            return app().getServerApi().auth(params[0], FirebaseInstanceId.getInstance().getToken());
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            System.out.println("User profile " + jsonObject);
            if (findViewById(R.id.avloadingIndicatorView) == null) return;
            findViewById(R.id.avloadingIndicatorView).setVisibility(View.GONE);
            if (jsonObject == null) {
                Toast.makeText(getApplicationContext(), R.string.error_auth, Toast.LENGTH_LONG).show();
                return;
            }

            try {
                app().saveUserProfile(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            postLogin(jsonObject);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void onLogIn(View v) {
        startActivity(new Intent(this, EnterCredentialsActivity.class));
    }

    private class PingTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {
            app().getServerApi().ping();
            return null;
        }
    }
}
