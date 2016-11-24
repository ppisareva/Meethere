package com.example.polina.meethere;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.polina.meethere.model.App;
import com.example.polina.meethere.model.User;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONObject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by polina on 31.08.16.
 */
public class FollowersDialigAdapter extends ArrayAdapter<User> {

    Context context;
    List<User> list;
    Set<Integer> positionsList = new HashSet<>();
    String eventId;


    public FollowersDialigAdapter(Context context,  List<User> list, String eventId) {
        super(context, R.layout.friends_list, list);
        this.context = context;
        this.list = list;
        this.eventId = eventId;
    }

    public static class ViewHolder {
        public TextView name;
        public SimpleDraweeView image;
        public CheckBox checkBox;



    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = null;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            view = inflater.inflate(R.layout.friends_list, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.dialog_text);
            viewHolder.image = (SimpleDraweeView) view.findViewById(R.id.dialog_image);
            viewHolder.checkBox = (CheckBox) view.findViewById(R.id.dialog_checkbox);
            viewHolder.checkBox.setVisibility(View.VISIBLE);
            viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        positionsList.add(position);
                        new SendInvite().execute(list.get(position).getId());
                    }
                }
            });
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.name.setText(list.get(position).getFirstName() + " " +list.get(position).getLastName());
        holder.image.setImageURI(Uri.parse(list.get(position).getImage()));
        holder.checkBox.setChecked(positionsList.contains(position));
        return view;
    }

    class SendInvite extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... params) {
            return ((App)context.getApplicationContext()).getServerApi().sendInvite(eventId, params[0]);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            Toast.makeText(context,context.getString(R.string.friends_invited),
                             Toast.LENGTH_SHORT).show();
        }
    }


}

