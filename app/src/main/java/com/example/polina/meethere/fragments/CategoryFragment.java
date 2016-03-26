package com.example.polina.meethere.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.polina.meethere.Adapters.Event;
import com.example.polina.meethere.Adapters.Events;
import com.example.polina.meethere.HorizontalEventAdapter;
import com.example.polina.meethere.R;
import com.example.polina.meethere.VerticalEventAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CategoryFragment extends android.support.v4.app.Fragment  {
    RecyclerView verticalList;


    public static CategoryFragment newInstance() {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    public CategoryFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View v =inflater.inflate(R.layout.fragment_category, container, false);
        verticalList = (RecyclerView) v.findViewById(R.id.vertical_list);
        verticalList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        List <Events> events =  getListEvents();
        VerticalEventAdapter verticalEventAdapter = new VerticalEventAdapter(events,getActivity());
        verticalList.setAdapter(verticalEventAdapter);
        return v;
    }


    public List<Events> getListEvents(){
        List<Event> event = new ArrayList<>();
        event.add(new Event(R.drawable.bicycling, "Покатушки", "поехали на выходных кататься на великах по Труханову острову", new Date(2016,3,24)));
        event.add(new Event(R.drawable.roulette,  "Поккер", "собираемся поиграть в покер, не боьшой компанией на деньги",  new Date(2016,3,24)));
        event.add(new Event(R.drawable.chess, "Сиграем в шахматы", "Клуб интелекруальных и находчевых приглашает на открытый чемпионат по шахматам в загородном клубе Матылек",  new Date(2016,3,24)));
        event.add(new Event(R.drawable.cappuccino,  "Выбраться на кофе", "Девченки, пошли на кофе на этих выходных. Мои друзья открыли новую кофейню.",  new Date(2016,3,24)));
        event.add(new Event(R.drawable.carrot,  "Здоровое питание", "Тебе надоело есть путеры и картофан, постоянно мучает изжога и диорея, не знаешь как удержать себе и не пукнуть в людном месте - приходи на бесплатный тренинг по здоровому питанию",  new Date(2016,3,24)));
        List<Events> eventsList = new ArrayList<>();
        for(int i = 0; i<10; i++){
            eventsList.add(new Events("Популярное", event));
        }
        return  eventsList;
    }
}
