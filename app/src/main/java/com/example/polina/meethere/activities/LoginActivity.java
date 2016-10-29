package com.example.polina.meethere.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.polina.meethere.R;
import com.example.polina.meethere.Utils;
import com.example.polina.meethere.model.App;
import com.example.polina.meethere.model.UserProfile;
import com.example.polina.meethere.views.FadeView;
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
    LinearLayout signUp;
    LinearLayout logIn;
    Animation slide;
    EditText logInName;
    EditText logInPassword;
    EditText signUpName;
    EditText signUpSurname;
    EditText signUpEmail;
    EditText signUpPassword;
    EditText signUpConfirmPassword;
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
        userExist = (TextView) findViewById(R.id.userExist);
        loader = (ProgressBar) findViewById(R.id.avloadingIndicatorView);
        signUp = (LinearLayout) findViewById(R.id.signUp_layout);
        logIn = (LinearLayout) findViewById(R.id.login_layout);
        logInName = (EditText)findViewById(R.id.email_login);
        logInPassword = (EditText) findViewById(R.id.password_logIn);
        signUpName = (EditText) findViewById(R.id.name_signUp);
        signUpName.setOnFocusChangeListener(onFocusChangeListener);
        signUpSurname = (EditText)findViewById(R.id.last_name_signUp);
        signUpSurname.setOnFocusChangeListener(onFocusChangeListener);
        signUpEmail = (EditText) findViewById(R.id.email_signUp);
        signUpEmail.setOnFocusChangeListener(onFocusChangeListener);
        signUpPassword = (EditText) findViewById(R.id.password_signUp);
        signUpPassword.setOnFocusChangeListener(onFocusChangeListener);
        signUpConfirmPassword = (EditText) findViewById(R.id.password_confirm_signUp);
        signUpConfirmPassword.setOnFocusChangeListener(onFocusChangeListener);
        slide = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_animation);
        slide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        callbackManager = CallbackManager.Factory.create();
        if (app().getUserProfile() != null) {
            goToMain();
            return;
        }
//        Vlad's login
//      new LoginWithFbTask().execute("EAAGlmxaRKmEBAMuE70E98Brd6gRsaKKdqrInH2w2Ju9aDa1vZAlxrlilDOUp1h7l52LK6iMbqXZCHyKkAtWrMBCcyBlAuVB6kxfEEOoVATdesZAp5y4fkhnb95HZCNL4xBFncr7EdTaXL2PmDrT8HMmJTY9PKZA2nKO5NF0AwpUUYVifUiKaW0tEEDTXVfnsZD");
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends", "user_location");
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
    }

   private void onProgressBar(){
       loader.setVisibility(View.VISIBLE);
   }

    private void offProgressBar(){
        loader.setVisibility(View.INVISIBLE);
    }

    View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            if (!hasFocus) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                TextView view = (TextView) v;
                String text = view.getText().toString();
                if (text.matches("")) {
                    view.setHint(getString(R.string.not_empty));
                    view.setHintTextColor(getResources().getColor(R.color.red));
                    view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_asterisk, 0);
                    return;
                } else {
                    view.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }

                if(v.getId()==R.id.email_signUp) {
                    if (!text.matches("")) {
                        new CheckExistedUser().execute(text);
                    }
                }


            }
        }
    };
    public void onBack(View v) {
        logIn.setVisibility(View.GONE);
        signUp.setVisibility(View.GONE);
    }

    public void onLogIn(View v) {
        logIn.setVisibility(View.VISIBLE);

        logIn.startAnimation(slide);
    }

    public void onSubmitLogin (View v){
       new LogIn().execute(logInName.getText().toString(), logInPassword.getText().toString());

    }

    public void onSubminSignUp (View v){
        if(signUpPassword.getText().toString().equals(signUpConfirmPassword.getText().toString())) {
            Boolean create = true;
            if(signUpName.getText().equals("") ||signUpSurname.getText().equals("")||signUpEmail.equals("")||signUpPassword.getText().equals("")){
                create = false;
            }

            if(newUser&&create) {
                onProgressBar();
                new SignUp().execute(signUpName.getText().toString(), signUpSurname.getText().toString(), signUpEmail.getText().toString(), signUpPassword.getText().toString());
            } else {
                Toast.makeText(this, getString(R.string.user_cannot_be_created ), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.wrong_password), Toast.LENGTH_LONG).show();
        }

    }

    public void onSignUp(View v) {
        signUp.setVisibility(View.VISIBLE);

        signUp.startAnimation(slide);
    }

    private void goToMain() {
        finish();
        startActivity(new Intent(this, MainActivity.class));
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
                if (!jsonObject.has(UserProfile.CATEGORY) || jsonObject.get(UserProfile.CATEGORY) == null) {
                    gotoChooseCategory();
                } else {
                    goToMain();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                goToMain();
            }

        }
    }

    private void gotoChooseCategory() {
        startActivity(new Intent(LoginActivity.this, ChooseCategoryActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private class CheckExistedUser extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... params) {
           return app.getServerApi().checkUser(params[0]);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
             Boolean alreadyExist=jsonObject.getBoolean(Utils.REGISTERD);
                if(alreadyExist){
                    userExist.setVisibility(View.VISIBLE);
                    signUpEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_asterisk, 0);
                    newUser = false;
                } else {
                    userExist.setVisibility(View.INVISIBLE);
                    signUpEmail.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    newUser = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Toast.makeText(LoginActivity.this, jsonObject.toString(), Toast.LENGTH_LONG).show();
        }
    }
    private class SignUp extends AsyncTask<String, Void, JSONObject>{
        @Override
        protected JSONObject doInBackground(String... params) {

            return app.getServerApi().signUp( params[0],params[1],params[2],params[3]);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            Toast.makeText(LoginActivity.this, jsonObject.toString(), Toast.LENGTH_LONG).show();
            offProgressBar();

        }
    }

    private class LogIn extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... params) {
           return app.getServerApi().logIn(params[0], params[1]);

        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            Toast.makeText(LoginActivity.this, jsonObject.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
