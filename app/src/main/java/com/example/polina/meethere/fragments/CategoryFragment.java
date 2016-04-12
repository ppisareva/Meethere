package com.example.polina.meethere.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bartoszlipinski.recyclerviewheader2.RecyclerViewHeader;
import com.example.polina.meethere.Adapters.Event;
import com.example.polina.meethere.Adapters.Events;
import com.example.polina.meethere.HeaderAdapter;
import com.example.polina.meethere.R;
import com.example.polina.meethere.VerticalEventAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CategoryFragment extends android.support.v4.app.Fragment  {


    public static CategoryFragment newInstance() {
        CategoryFragment fragment = new CategoryFragment();

        return fragment;
    }

    public CategoryFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View v =inflater.inflate(R.layout.fragment_category, container, false);
        RecyclerView verticalList = (RecyclerView) v.findViewById(R.id.vertical_list);

        verticalList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        List <Events> events =  getListEvents();
        VerticalEventAdapter verticalEventAdapter = new VerticalEventAdapter(events,getActivity());

        RecyclerViewHeader header = (RecyclerViewHeader) v.findViewById(R.id.header);
        RecyclerView  headerView = (RecyclerView) v.findViewById(R.id.category_list_header);
        headerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        HeaderAdapter headerAdapter = new HeaderAdapter(getCategory());
        headerView.setAdapter(headerAdapter);


        header.attachTo(verticalList);
        verticalList.setAdapter(verticalEventAdapter);

        return v;
    }


    public List<Events> getListEvents(){
        List<Event> event = new ArrayList<>();
        event.add(new Event(R.drawable.bicycling, "Покатушки", "поехали на выходных кататься на великах по Труханову острову", new Date(2016,3,24), "100 (+), 23(-)", "100 грн"));
        event.add(new Event(R.drawable.roulette,  "Поккер", "собираемся поиграть в покер, не боьшой компанией на деньги",  new Date(2016,3,24), "49(+), 34(-)", "БЕСПЛАТНО"));
        event.add(new Event(R.drawable.chess, "Сиграем в шахматы", "Клуб интелекруальных и находчевых приглашает на открытый чемпионат по шахматам в загородном клубе Матылек",  new Date(2016,3,24),  "100 (+), 23(-)", "100 грн"));
        event.add(new Event(R.drawable.cappuccino, "Выбраться на кофе", "Девченки, пошли на кофе на этих выходных. Мои друзья открыли новую кофейню.", new Date(2016, 3, 24), "49(+), 34(-)", "БЕСПЛАТНО"));
        event.add(new Event(R.drawable.carrot, "Здоровое питание", "Тебе надоело есть путеры и картофан, постоянно мучает изжога и диорея, не знаешь как удержать себе и не пукнуть в людном месте - приходи на бесплатный тренинг по здоровому питанию", new Date(2016, 3, 24), "100 (+), 23(-)", "100 грн"));

        List<Events> eventsList = new ArrayList<>();
        eventsList.add(new Events("Популярное", event));
        eventsList.add(new Events("Фитнес", event));
        eventsList.add(new Events("Еда и напитки", event));
        eventsList.add(new Events("Духовность", event));
        eventsList.add(new Events("С друзьями", event));
        eventsList.add(new Events("На природе", event));
        eventsList.add(new Events("Книги", event));
        eventsList.add(new Events("Политика", event));

        return  eventsList;
    }

    public List<String> getCategory(){
        List<String > c = new ArrayList<>();
        c.add("Фитнес");
        c.add("Еда и Напитки");
        c.add("Исскуство и Культура");
        c.add("Танци");
        c.add("Мода");
        c.add("Больше");
        return c;
    }
}
