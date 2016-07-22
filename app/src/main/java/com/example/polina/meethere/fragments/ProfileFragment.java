package com.example.polina.meethere.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.polina.meethere.R;
import com.example.polina.meethere.model.App;
import com.example.polina.meethere.model.User;
import com.example.polina.meethere.model.UserProfile;
import com.example.polina.meethere.network.ServerApi;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileFragment extends android.support.v4.app.Fragment {
    TextView followers;
    TextView followings;
    ServerApi serverApi;

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();

        return fragment;
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        UserProfile up = ((App)getActivity().getApplication()).getUserProfile();
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        ((TextView)v.findViewById(R.id.user_name)).setText(up.getName());
        ((TextView)v.findViewById(R.id.location)).setText(up.getLocation());

        ((TextView) v.findViewById(R.id.followers)).setText(up.getFollowers()+"");
        ((TextView)v.findViewById(R.id.followings)).setText(up.getFollowings()+"");
        serverApi = ((App) getActivity().getApplication()).getServerApi();
        SharedPreferences sharedPreferences = ((App)getActivity().getApplication()).pref();
        int id = sharedPreferences.getInt(UserProfile.USER_ID, -1);

        new LoadUser().execute(id);
        SimpleDraweeView profileImage = (SimpleDraweeView)v.findViewById(R.id.profile_image);
        profileImage.setImageURI(Uri.parse(up.getProfileUrl()));
        RoundingParams roundingParams = RoundingParams.asCircle();
        profileImage.getHierarchy().setRoundingParams(roundingParams);
        return v;
    }

    class LoadUser extends AsyncTask<Integer, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Integer... params) {
            return serverApi.loadUserProfile(params[0] + "");
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            UserProfile userProfile = UserProfile.parseUserProfile(jsonObject);
            try {
                ((App)getActivity().getApplication()).saveUserProfile(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            UserProfile up = ((App)getActivity().getApplication()).getUserProfile();

        }
    }


    public void onFollowers(){

    }

    public void onFollowings(){

    }


}
