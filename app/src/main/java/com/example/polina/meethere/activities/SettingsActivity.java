package com.example.polina.meethere.activities;


import android.annotation.TargetApi;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.preference.SwitchPreference;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.example.polina.meethere.R;
import com.example.polina.meethere.Utils;
import com.example.polina.meethere.model.App;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {
    static App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        app = (App)getApplication();

    }




    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
//     * A preference value change listener that updates the preference's summary
//     * to reflect its new value.
//     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(listPreference.getKey(), index);
                    jsonObject.put(Utils.USER_ID, app.getUserProfile().getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new AsyncTask<JSONObject, Void, JSONObject>() {
                    @Override
                    protected JSONObject doInBackground(JSONObject... params) {
                        return app.getServerApi().privacySettings(params[0]);
                    }
                }.execute(jsonObject);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);


            } else if (preference instanceof SwitchPreference) {
                SwitchPreference swichPreference = (SwitchPreference) preference;
                boolean isOn = ((Boolean) value).booleanValue();
                String prefValue = swichPreference.getKey();
                new AsyncTask<Object, Void, JSONObject>() {
                    @Override
                    protected JSONObject doInBackground(Object... params) {
                        String kay = (String)params[0];
                        Boolean value = (Boolean) params[1];
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put(kay, value);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                       return app.getServerApi().generalSettings(jsonObject);
                    }
                }.execute(prefValue, isOn);

            }
            return true;
        }
    };

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    private static void bindPreferenceSummaryToValueBoolean(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getBoolean(preference.getKey(), true));
    }
//
//    /**
//     * This method stops fragment injection in malicious applications.
//     * Make sure to deny any unknown fragments here.
//     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
              //  || DataSyncPreferenceFragment.class.getName().equals(fragmentName)
                || NotificationPreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);


            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValueBoolean(findPreference("messages"));
            bindPreferenceSummaryToValueBoolean(findPreference("upcoming_events"));
            bindPreferenceSummaryToValueBoolean(findPreference("near_events"));
            bindPreferenceSummaryToValueBoolean(findPreference("recommended_events"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_notification);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("email"));
            bindPreferenceSummaryToValue(findPreference("phone"));
            bindPreferenceSummaryToValue(findPreference("categories"));
            bindPreferenceSummaryToValue(findPreference("follow"));
            bindPreferenceSummaryToValue(findPreference("events"));
            bindPreferenceSummaryToValue(findPreference("private_info"));
        }
    }


        /**
         * This fragment shows data and sync preferences only. It is used when the
         * activity is showing a two-pane settings UI.
         */
//        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//        public static class DataSyncPreferenceFragment extends PreferenceFragment {
//            @Override
//            public void onCreate(Bundle savedInstanceState) {
//                super.onCreate(savedInstanceState);
//                addPreferencesFromResource(R.xml.pref_data_sync);
//                setHasOptionsMenu(true);
//
//                // Bind the summaries of EditText/List/Dialog/Ringtone preferences
//                // to their values. When their values change, their summaries are
//                // updated to reflect the new value, per the Android Design
//                // guidelines.
//
//            }
//
//
//        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                finish();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

