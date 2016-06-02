package com.example.polina.meethere.fragments;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.polina.meethere.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewEventDescriptionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewEventDescriptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewEventDescriptionFragment extends android.support.v4.app.Fragment{
    EditText name;
    EditText description;

    public static NewEventDescriptionFragment newInstance() {
        NewEventDescriptionFragment fragment = new NewEventDescriptionFragment();

        return fragment;
    }

    public NewEventDescriptionFragment() {
        // Required empty public constructor
    }

    View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            if (!hasFocus) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_new_event_description, container, false);
        setHasOptionsMenu(true);
        name = (EditText)v.findViewById(R.id.edit_name);
        name.setOnFocusChangeListener(onFocusChangeListener);
        description =(EditText) v.findViewById(R.id.edit_description);
        name.setImeOptions(EditorInfo.IME_ACTION_DONE);
        description.setImeOptions(EditorInfo.IME_ACTION_DONE);
        description.setOnFocusChangeListener(onFocusChangeListener);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.new_event_mid, menu);
    }

    public String getName (){
        return name.getText().toString();
    }

    public String getDescription (){
        return description.getText().toString();
    }


}
