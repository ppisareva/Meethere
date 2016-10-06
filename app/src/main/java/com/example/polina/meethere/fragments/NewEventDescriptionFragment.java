package com.example.polina.meethere.fragments;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.polina.meethere.R;
import com.example.polina.meethere.model.Event;


public class NewEventDescriptionFragment extends android.support.v4.app.Fragment{
    EditText name;
    EditText description;
    Bundle b;


    public static NewEventDescriptionFragment newInstance(String name, String description) {
        NewEventDescriptionFragment fragment = new NewEventDescriptionFragment();
        Bundle bundle =  new Bundle();
        bundle.putString(Event.NAME, name);
        bundle.putString(Event.DESCRIPTION, description);
        fragment.setArguments(bundle);

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
                TextView view = (TextView) v;
                String text = view.getText().toString();
                if (text.matches("")) {
                    view.setHint("Должно быть заполненно");
                    view.setHintTextColor(getResources().getColor(R.color.red));
                    view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_asterisk, 0);
                    return;
                } else {
                    view.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }


            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_new_event_description, container, false);
        setHasOptionsMenu(true);
        name = (EditText)v.findViewById(R.id.edit_name);

        name.setOnFocusChangeListener(onFocusChangeListener);
        description =(EditText) v.findViewById(R.id.edit_description);
        if(b!=null) {
            name.setText(b.getString(Event.NAME));
            description.setText(b.getString(Event.DESCRIPTION));
        }

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
