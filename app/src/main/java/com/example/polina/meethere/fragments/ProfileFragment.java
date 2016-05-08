package com.example.polina.meethere.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.polina.meethere.AbstractMeethereActivity;
import com.example.polina.meethere.Adapters.SimpleItem;
import com.example.polina.meethere.R;
import com.example.polina.meethere.SimpleListAdapter;
import com.example.polina.meethere.model.App;
import com.example.polina.meethere.model.UserProfile;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends android.support.v4.app.Fragment {

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
        SimpleDraweeView profileImage = (SimpleDraweeView)v.findViewById(R.id.profile_image);
        profileImage.setImageURI(Uri.parse(up.getProfileUrl()));
        RoundingParams roundingParams = RoundingParams.asCircle();
        profileImage.getHierarchy().setRoundingParams(roundingParams);
        return v;
    }




}
