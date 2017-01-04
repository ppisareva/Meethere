package com.tolpa.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.tolpa.R;
import com.tolpa.model.App;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A login screen that offers login via email/password.
 */
public class EnterCredentialsActivity extends AbstractMeethereActivity implements LoaderCallbacks {

    public static final int LOADER_EMAIL = 0;
    public static final int LOADER_IS_REGISTRED = 1;

    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView emailView;
    private EditText passwordView;
    private EditText repasswordView;
    private EditText firstNameView;
    private EditText lastNameView;
    private View mProgressView;
    private View additionalFields;
    private TextView loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_credentials);
        setupActionBar();
        // Set up the login form.
        emailView = (AutoCompleteTextView) findViewById(R.id.email);
        emailView.setOnFocusChangeListener(onFocusChangeListener);
        additionalFields = findViewById(R.id.additional_input);
        populateAutoComplete();

        passwordView = (EditText) findViewById(R.id.password);
        passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        repasswordView = (EditText) findViewById(R.id.repassword);
        firstNameView = (EditText) findViewById(R.id.first_name);
        lastNameView = (EditText) findViewById(R.id.last_name);
        loginButton = (TextView) findViewById(R.id.email_sign_in_button);
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mProgressView = findViewById(R.id.login_progress);
    }

    private View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                String password = passwordView.getText().toString();
                switch (v.getId()) {
                    case R.id.email:
                        String email = emailView.getText().toString();
                        if (TextUtils.isEmpty(email)) {
                            emailView.setError(getString(R.string.error_field_required));
                            break;
                        } else if (!isEmailValid(email)) {
                            emailView.setError(getString(R.string.error_invalid_email));
                            break;
                        }
                        if (getLoaderManager().getLoader(LOADER_IS_REGISTRED) == null)
                            getLoaderManager().initLoader(LOADER_IS_REGISTRED, null, EnterCredentialsActivity.this);
                        else
                            getLoaderManager().restartLoader(LOADER_IS_REGISTRED, null, EnterCredentialsActivity.this);
                        break;
                    case R.id.password:
                        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
                            passwordView.setError(getString(R.string.error_invalid_password));
                        }
                        break;
                    case R.id.repassword:
                        if (!TextUtils.equals(password, repasswordView.getText().toString())) {
                            repasswordView.setError(getString(R.string.error_repassword_not_match));
                        }
                        break;
                    case R.id.first_name:
                        if (TextUtils.isEmpty(firstNameView.getText().toString())) {
                            firstNameView.setError(getString(R.string.error_field_required));
                        }
                        break;
                    case R.id.last_name:
                        if (TextUtils.isEmpty(lastNameView.getText().toString())) {
                            lastNameView.setError(getString(R.string.error_field_required));
                        }
                        break;
                }
            }
        }
    };

    private void populateAutoComplete() {
        getLoaderManager().initLoader(LOADER_EMAIL, null, this);
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        emailView.setError(null);
        passwordView.setError(null);

        // Store values at the time of the login attempt.
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            passwordView.setError(getString(R.string.error_invalid_password));
            focusView = passwordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailView.setError(getString(R.string.error_field_required));
            focusView = emailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailView.setError(getString(R.string.error_invalid_email));
            focusView = emailView;
            cancel = true;
        } else if (additionalFields.getVisibility() == View.VISIBLE) {
            if (!TextUtils.equals(password, repasswordView.getText().toString())) {
                repasswordView.setError(getString(R.string.error_repassword_not_match));
                focusView = repasswordView;
                cancel = true;
            } else if (TextUtils.isEmpty(firstNameView.getText().toString())) {
                firstNameView.setError(getString(R.string.error_field_required));
                focusView = firstNameView;
                cancel = true;
            } else if (TextUtils.isEmpty(lastNameView.getText().toString())) {
                lastNameView.setError(getString(R.string.error_field_required));
                focusView = lastNameView;
                cancel = true;
            }
        }



        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            if (additionalFields.getVisibility() == View.VISIBLE)
                mAuthTask.execute(firstNameView.getText().toString(), lastNameView.getText().toString());
            else
                mAuthTask.execute();
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        loginButton.setVisibility(show ? View.GONE : View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        if (i == LOADER_EMAIL) {
            return new CursorLoader(this,
                    // Retrieve data rows for the device user's 'profile' contact.
                    Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                            ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                    // Select only email addresses.
                    ContactsContract.Contacts.Data.MIMETYPE +
                            " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                    .CONTENT_ITEM_TYPE},

                    // Show primary email addresses first. Note that there won't be
                    // a primary email address if the user hasn't specified one.
                    ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
        }
        if (i == LOADER_IS_REGISTRED) {
            return new CheckEmailAsyncLoader(this, emailView.getText().toString());
        }
        return null;

    }

    @Override
    public void onLoadFinished(Loader loader, Object result) {
        switch (loader.getId()) {
            case LOADER_EMAIL:
                List<String> emails = new ArrayList<>();
                Cursor cursor = (Cursor) result;
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    emails.add(cursor.getString(ProfileQuery.ADDRESS));
                    cursor.moveToNext();
                }

                addEmailsToAutoComplete(emails);
                return;
            case LOADER_IS_REGISTRED:
                boolean registered = (Boolean) result;
                additionalFields.setVisibility(registered ? View.GONE : View.VISIBLE);
                loginButton.setText(registered ? R.string.login : R.string.sign_up);


        }
    }

    @Override
    public void onLoaderReset(Loader cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(EnterCredentialsActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        emailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<String, Void, JSONObject> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject o;
            if (params.length == 0)
                o = app().getServerApi().logIn(mEmail, mPassword);
            else
                o = app().getServerApi().signUp(params[0], params[1], mEmail, mPassword);
            if (o == null)
                return null;
            try {
                app().saveUserProfile(o);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return o;
        }

        @Override
        protected void onPostExecute(final JSONObject o) {
            mAuthTask = null;
            showProgress(false);

            if (o != null) {
                postLogin(o);

            } else {
                passwordView.setError(getString(R.string.error_incorrect_password));
                passwordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    private static class CheckEmailAsyncLoader extends AsyncTaskLoader<Boolean> {

        private String email;
        CheckEmailAsyncLoader(Context ctx, String email) {
            super(ctx);
            this.email = email;
        }

        @Override
        public Boolean loadInBackground() {
            return ((App)getContext().getApplicationContext()).getServerApi().checkUser(email);
        }

        @Override
        protected void onStartLoading() {
            forceLoad();

        }

        @Override
        protected void onReset() {
            super.onReset();
            onStopLoading();
        }

    }
}

