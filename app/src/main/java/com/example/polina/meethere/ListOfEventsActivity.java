package com.example.polina.meethere;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.example.polina.meethere.Adapters.Event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListOfEventsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_events);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.events_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        MyEventsAdapter myEventsAdapter = new MyEventsAdapter(this, getListEvents());
        recyclerView.setAdapter(myEventsAdapter);
    }

    public List<Event> getListEvents(){
        List<Event> event = new ArrayList<>();
        event.add(new Event(R.drawable.bicycling, "Покатушки", "поехали на выходных кататься на великах по Труханову острову", new Date(2016,3,24), "Богатырская 6, Киев", "100 грн"));
        event.add(new Event(R.drawable.roulette,  "Поккер", "собираемся поиграть в покер, не боьшой компанией на деньги",  new Date(2016,3,24), "Новоконстантиновская 123, Киев", "БЕСПЛАТНО"));
        event.add(new Event(R.drawable.chess, "Сиграем в шахматы", "Клуб интелекруальных и находчевых приглашает на открытый чемпионат по шахматам в загородном клубе Матылек",  new Date(2016,3,24),  "Пр. Победы 24, Киев", "100 грн"));
        event.add(new Event(R.drawable.cappuccino, "Выбраться на кофе", "Девченки, пошли на кофе на этих выходных. Мои друзья открыли новую кофейню.", new Date(2016, 3, 24), "Смирнова-Ласточкина, 1а, кв 23, Одесса", "БЕСПЛАТНО"));
        event.add(new Event(R.drawable.carrot, "Здоровое питание", "Тебе надоело есть путеры и картофан, постоянно мучает изжога и диорея, не знаешь как удержать себе и не пукнуть в людном месте - приходи на бесплатный тренинг по здоровому питанию", new Date(2016, 3, 24), "1Прибалтийская 45, кв 12, Днепропетровск", "100 грн"));
 event.add(new Event(R.drawable.bicycling, "Покатушки", "поехали на выходных кататься на великах по Труханову острову", new Date(2016,3,24), "Богатырская 6, Киев", "100 грн"));
        event.add(new Event(R.drawable.roulette,  "Поккер", "собираемся поиграть в покер, не боьшой компанией на деньги",  new Date(2016,3,24), "Новоконстантиновская 123, Киев", "БЕСПЛАТНО"));
        event.add(new Event(R.drawable.chess, "Сиграем в шахматы", "Клуб интелекруальных и находчевых приглашает на открытый чемпионат по шахматам в загородном клубе Матылек",  new Date(2016,3,24),  "Пр. Победы 24, Киев", "100 грн"));
        event.add(new Event(R.drawable.cappuccino, "Выбраться на кофе", "Девченки, пошли на кофе на этих выходных. Мои друзья открыли новую кофейню.", new Date(2016, 3, 24), "Смирнова-Ласточкина, 1а, кв 23, Одесса", "БЕСПЛАТНО"));
        event.add(new Event(R.drawable.carrot, "Здоровое питание", "Тебе надоело есть путеры и картофан, постоянно мучает изжога и диорея, не знаешь как удержать себе и не пукнуть в людном месте - приходи на бесплатный тренинг по здоровому питанию", new Date(2016, 3, 24), "1Прибалтийская 45, кв 12, Днепропетровск", "100 грн"));
 event.add(new Event(R.drawable.bicycling, "Покатушки", "поехали на выходных кататься на великах по Труханову острову", new Date(2016,3,24), "Богатырская 6, Киев", "100 грн"));
        event.add(new Event(R.drawable.roulette,  "Поккер", "собираемся поиграть в покер, не боьшой компанией на деньги",  new Date(2016,3,24), "Новоконстантиновская 123, Киев", "БЕСПЛАТНО"));
        event.add(new Event(R.drawable.chess, "Сиграем в шахматы", "Клуб интелекруальных и находчевых приглашает на открытый чемпионат по шахматам в загородном клубе Матылек",  new Date(2016,3,24),  "Пр. Победы 24, Киев", "100 грн"));
        event.add(new Event(R.drawable.cappuccino, "Выбраться на кофе", "Девченки, пошли на кофе на этих выходных. Мои друзья открыли новую кофейню.", new Date(2016, 3, 24), "Смирнова-Ласточкина, 1а, кв 23, Одесса", "БЕСПЛАТНО"));
        event.add(new Event(R.drawable.carrot, "Здоровое питание", "Тебе надоело есть путеры и картофан, постоянно мучает изжога и диорея, не знаешь как удержать себе и не пукнуть в людном месте - приходи на бесплатный тренинг по здоровому питанию", new Date(2016, 3, 24), "1Прибалтийская 45, кв 12, Днепропетровск", "100 грн"));
 event.add(new Event(R.drawable.bicycling, "Покатушки", "поехали на выходных кататься на великах по Труханову острову", new Date(2016,3,24), "Богатырская 6, Киев", "100 грн"));
        event.add(new Event(R.drawable.roulette,  "Поккер", "собираемся поиграть в покер, не боьшой компанией на деньги",  new Date(2016,3,24), "Новоконстантиновская 123, Киев", "БЕСПЛАТНО"));
        event.add(new Event(R.drawable.chess, "Сиграем в шахматы", "Клуб интелекруальных и находчевых приглашает на открытый чемпионат по шахматам в загородном клубе Матылек",  new Date(2016,3,24),  "Пр. Победы 24, Киев", "100 грн"));
        event.add(new Event(R.drawable.cappuccino, "Выбраться на кофе", "Девченки, пошли на кофе на этих выходных. Мои друзья открыли новую кофейню.", new Date(2016, 3, 24), "Смирнова-Ласточкина, 1а, кв 23, Одесса", "БЕСПЛАТНО"));
        event.add(new Event(R.drawable.carrot, "Здоровое питание", "Тебе надоело есть путеры и картофан, постоянно мучает изжога и диорея, не знаешь как удержать себе и не пукнуть в людном месте - приходи на бесплатный тренинг по здоровому питанию", new Date(2016, 3, 24), "1Прибалтийская 45, кв 12, Днепропетровск", "100 грн"));

        return  event;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
