package com.example.polina.meethere.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.polina.meethere.R;
import com.example.polina.meethere.Utils;
import com.example.polina.meethere.model.App;
import com.example.polina.meethere.model.UserProfile;
import com.example.polina.meethere.network.ServerApi;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class ProfileFragment extends android.support.v4.app.Fragment {
    private static final int RESULT_LOAD_IMAGE = 1;
    TextView followers;
    TextView followings;
    SimpleDraweeView profileImage;
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
        profileImage = (SimpleDraweeView) v.findViewById(R.id.profile_image);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });


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



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.err.println("INTENT DATA: " + data + " |||" + data.getExtras());
        if (requestCode == RESULT_LOAD_IMAGE && resultCode ==getActivity().RESULT_OK && data != null) {
            Uri uri = data.getData();
            Bitmap bitmap = ((App)getActivity().getApplication()).decodeUri(uri);
            profileImage.setImageBitmap(bitmap);
            new RefreshMyInfo().execute(bitmap);


        }
    }

    class RefreshMyInfo extends AsyncTask<Bitmap, Void, Void> {


        @Override
        protected Void doInBackground(Bitmap... params) {

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            params[0].compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            ServerApi serverApi = ((App)getActivity(). getApplication()).getServerApi();
            serverApi.uploadImage("", byteArray, Utils.PROFILE);

            return null;
        }
    }


    class LoadUser extends AsyncTask<Integer, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Integer... params) {
            return serverApi.loadUserProfile(params[0] + "");
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                ((App)getActivity().getApplication()).saveUserProfile(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void onFollowers(){

    }
    public void onFollowings(){

    }




}
