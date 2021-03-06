package com.tolpa.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tolpa.R;
import com.tolpa.Utils;
import com.tolpa.activities.FeedActivity;
import com.tolpa.activities.MainActivity;
import com.tolpa.activities.UserListActivity;
import com.tolpa.activities.MyInformationActivity;
import com.tolpa.activities.SettingsActivity;
import com.tolpa.model.App;
import com.tolpa.model.UserProfile;
import com.tolpa.network.ServerApi;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class ProfileFragment extends android.support.v4.app.Fragment {
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int MY_EVENT = 0;
    private static final int MY_NEWS = 1;
    private static final int CHANGE_PROFILY = 2;
    private static final int SETTINGS = 3;
    TextView followers;
    TextView followings;
    LinearLayout onFollower;
    LinearLayout onFollowing;
    SimpleDraweeView profileImage;
    ServerApi serverApi;
    App app;
    int id;

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();

        return fragment;
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        UserProfile up = ((App)getActivity().getApplication()).getUserProfile();
        app = ((App)getActivity().getApplicationContext());
        View v = inflater.inflate(R.layout.profile_fragment, container, false);
        ((TextView)v.findViewById(R.id.user_name)).setText(up.getName());
        ((TextView)v.findViewById(R.id.location)).setText(!up.getLocation().equals("null")?up.getLocation():"");
        profileImage = (SimpleDraweeView) v.findViewById(R.id.profile_image);
        onFollower = (LinearLayout) v.findViewById(R.id.on_followers);
        onFollower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(getActivity(), UserListActivity.class);
                intent.putExtra(UserProfile.USER_ID, id);
                intent.putExtra(UserProfile.FOLLOW, UserListActivity.FOLLOWER);
                startActivity(intent);

            }
        });
        onFollowing = (LinearLayout) v.findViewById(R.id.on_followings);
        onFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserListActivity.class);
                intent.putExtra(UserProfile.USER_ID, id);
                intent.putExtra(UserProfile.FOLLOW, UserListActivity.FOLLOWING);
                startActivity(intent);
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });

        GridView gridview = (GridView) v.findViewById(R.id.profile_menu);
        gridview.setAdapter(new ImageAdapter(getActivity()));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                switch (position){
                    case MY_EVENT:
                        MyEventsListsFragment  myEventsListsFragment = MyEventsListsFragment.newInstance();
                        ((MainActivity)getActivity()).setmContent(myEventsListsFragment);
                        ((MainActivity)getActivity()).navigationView.getMenu().getItem(1).setChecked(true);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main, myEventsListsFragment)
                                .commit();
                        break;
                    case MY_NEWS:
                        startActivity(new Intent(getContext(), FeedActivity.class));
                        break;
                    case CHANGE_PROFILY:
                        startActivity(new Intent(getContext(), MyInformationActivity.class));
                        break;
                    case SETTINGS:
                        Intent intent = new Intent(getContext(), SettingsActivity.class);
                        startActivity(intent);
                        break;
                }

            }
        });
        ((TextView) v.findViewById(R.id.followers)).setText(up.getFollowers()+"");
        ((TextView)v.findViewById(R.id.followings)).setText(up.getFollowings()+"");
        serverApi = ((App) getActivity().getApplication()).getServerApi();
        SharedPreferences sharedPreferences = ((App)getActivity().getApplication()).pref();
        id = sharedPreferences.getInt(UserProfile.USER_ID, -1);
        new LoadUser().execute(id);
        profileImage.setImageURI(Uri.parse(up.getProfileUrl()));
        RoundingParams roundingParams = RoundingParams.asCircle();
        profileImage.getHierarchy().setRoundingParams(roundingParams);
        return v;
    }






    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode ==getActivity().RESULT_OK && data != null) {
            System.err.println("INTENT DATA: " + data + " |||" + data.getExtras());
            Uri uri = data.getData();
            Bitmap bitmap = ((App)getActivity().getApplication()).decodeUri(uri);
            profileImage.setImageURI(uri);
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

        @Override
        protected void onPostExecute(Void aVoid) {

           new LoadUser().execute(id);

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
                if(jsonObject!=null)
                    app.saveUserProfile(jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }






    private class ImageAdapter extends BaseAdapter {
        Context context;
        LayoutInflater lInflater;
        String text[];
        TypedArray image;
        public ImageAdapter(Context context) {
            this.context=context;
            lInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            text = context.getResources().getStringArray(R.array.menu_name);
            image = context.getResources().obtainTypedArray(R.array.menu_images);

        }

        @Override
        public int getCount() {
            return text.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = lInflater.inflate(R.layout.profile_menu, parent, false);
            }
            ((TextView) view.findViewById(R.id.menu_text)).setText(text[position]);
            ((ImageView) view.findViewById(R.id.menu_image)).setImageResource(image.getResourceId(position, R.drawable.ic_android));
            return view;
        }
    }

    }

