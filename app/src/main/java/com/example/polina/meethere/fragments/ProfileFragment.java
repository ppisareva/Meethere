package com.example.polina.meethere.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.polina.meethere.R;
import com.example.polina.meethere.model.App;
import com.example.polina.meethere.model.UserProfile;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

public class ProfileFragment extends android.support.v4.app.Fragment {
    TextView followers;
    TextView followings;

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
        followers = (TextView) v.findViewById(R.id.followers);
        followings = (TextView)v.findViewById(R.id.followings);
        SimpleDraweeView profileImage = (SimpleDraweeView)v.findViewById(R.id.profile_image);
        profileImage.setImageURI(Uri.parse(up.getProfileUrl()));
        RoundingParams roundingParams = RoundingParams.asCircle();
        profileImage.getHierarchy().setRoundingParams(roundingParams);
        return v;
    }


    public void onFollowers(){

    }

    public void onFollowings(){

    }


}
