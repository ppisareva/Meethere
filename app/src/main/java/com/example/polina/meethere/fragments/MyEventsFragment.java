package com.example.polina.meethere.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.polina.meethere.Adapters.Event;
import com.example.polina.meethere.Adapters.Events;
import com.example.polina.meethere.MyEventsAdapter;
import com.example.polina.meethere.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyEventsFragment extends android.support.v4.app.Fragment {

    private RecyclerView list;
    MyEventsAdapter adapter;

    public static MyEventsFragment newInstance() {
        MyEventsFragment fragment = new MyEventsFragment();

        return fragment;
    }

    public MyEventsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_events, container, false);
        list = (RecyclerView) v.findViewById(R.id.my_events_list);
        adapter = new MyEventsAdapter(getActivity(), getListEvents());
        list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        list.setAdapter(adapter);
        return v;

    }


    public List<Event> getListEvents(){
        List<Event> event = new ArrayList<>();
        event.add(new Event(R.drawable.bicycling, "Покатушки", "поехали на выходных кататься на великах по Труханову острову", new Date(2016,3,24), "Богатырская 6, Киев", "100 грн"));
        event.add(new Event(R.drawable.roulette,  "Поккер", "собираемся поиграть в покер, не боьшой компанией на деньги",  new Date(2016,3,24), "Новоконстантиновская 123, Киев", "БЕСПЛАТНО"));
        event.add(new Event(R.drawable.chess, "Сиграем в шахматы", "Клуб интелекруальных и находчевых приглашает на открытый чемпионат по шахматам в загородном клубе Матылек",  new Date(2016,3,24),  "Пр. Победы 24, Киев", "100 грн"));
        event.add(new Event(R.drawable.cappuccino,  "Выбраться на кофе", "Девченки, пошли на кофе на этих выходных. Мои друзья открыли новую кофейню.",  new Date(2016,3,24),  "Смирнова-Ласточкина, 1а, кв 23, Одесса", "БЕСПЛАТНО"));
        event.add(new Event(R.drawable.carrot,  "Здоровое питание", "Тебе надоело есть путеры и картофан, постоянно мучает изжога и диорея, не знаешь как удержать себе и не пукнуть в людном месте - приходи на бесплатный тренинг по здоровому питанию",  new Date(2016,3,24),  "1Прибалтийская 45, кв 12, Днепропетровск", "100 грн"));

        return  event;
    }

}
