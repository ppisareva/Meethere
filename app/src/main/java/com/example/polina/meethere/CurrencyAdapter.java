package com.example.polina.meethere;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by polina on 30.03.16.
 */
public class CurrencyAdapter extends BaseAdapter {

    List<Integer> list = new ArrayList<>();
    LayoutInflater layoutInflater;

    public CurrencyAdapter(Context context, List<Integer> list) {
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size()-1;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView ==null){
            convertView = layoutInflater.inflate(R.layout.spinner_view,
                    null);
        }

        ((ImageView) convertView.findViewById(R.id.image_spinner)).setImageResource(list.get(position));


        return convertView;
    }


}