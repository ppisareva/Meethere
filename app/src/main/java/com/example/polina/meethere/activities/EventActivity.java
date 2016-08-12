package com.example.polina.meethere.activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.polina.meethere.R;
import com.example.polina.meethere.Utils;
import com.example.polina.meethere.adapters.CommentAdapter;
import com.example.polina.meethere.data.Comment;
import com.example.polina.meethere.model.Event;
import com.example.polina.meethere.model.User;
import com.example.polina.meethere.network.ServerApi;
import com.example.polina.meethere.views.EndlessRecyclerViewScrollListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EventActivity extends AbstractMeethereActivity implements LoaderManager.LoaderCallbacks<List<Comment>> {
    private static final int DIALOG_REMOVE_COMMENT = 101011;
    TextView description;
    ImageView image;
    String id;
    CheckBox join;
    TextView time;
    TextView budget;
    TextView address;
    TextView    quantity;
    EditText comment;
    RecyclerView recyclerView;
    CommentAdapter commentAdapter;
    ServerApi serverApi;
    Double lat;
    Double lng;
    Event event;

    public static final String IMG_PATTERN = "https://s3-us-west-1.amazonaws.com/meethere/%s.jpg";
    public static final String BC_FILTER = "broadcast.filter.event.";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_layout_container);
        id = getIntent().getStringExtra(Utils.EVENT_ID);
        serverApi = app().getServerApi();
        View header = getLayoutInflater().inflate(R.layout.event_header, null);
        description = (TextView) header.findViewById(R.id.descriprion_my_event);
        image = (ImageView) findViewById(R.id.image_my_event);
        join = (CheckBox) header.findViewById(R.id.join_event);
        time = (TextView) header.findViewById(R.id.time_my_event);
        budget = (TextView) header.findViewById(R.id.my_event_budget);
        address = (TextView) header.findViewById(R.id.address_myevent);
        quantity = (TextView) header.findViewById(R.id.people_quantity_my_event);
        comment = (EditText) header.findViewById(R.id.make_comments);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra(Utils.EVENT_NAME));
        join.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                System.out.println(isChecked + " join event");
               new JoinEvent().execute(isChecked);
            }
        });
        String url = String.format(IMG_PATTERN, id);
        image.setImageURI(Uri.parse(url));
        new LoadEvent().execute(id);
        initComments(header);

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(BC_FILTER + id));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    private void initComments(View header) {

        recyclerView = (RecyclerView) findViewById(R.id.comments);
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(lm);
        final int userId = app().getUserProfile().getId();
        commentAdapter = new CommentAdapter(this, header, new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Comment c = commentAdapter.getComments().get(position);
                if (c.getCreatedById() !=  userId && userId != Integer.parseInt(event.getCreatedBy().getId()))
                    return false;
                Bundle b = new Bundle();
                b.putInt("position", position);
                b.putString("comment_id", c.getId());
                b.putString("comment_text", c.getText());
                showDialog(DIALOG_REMOVE_COMMENT, b);
                return true;
            }
        });
        recyclerView.setAdapter(commentAdapter);
        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(lm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                Bundle b = new Bundle();
                b.putInt("page", page*5);
                getSupportLoaderManager().restartLoader(1, b, EventActivity.this).forceLoad();
            }
        });
        Bundle b = new Bundle();
        b.putInt("page", 0);
        getSupportLoaderManager().initLoader(1, new Bundle(), this).forceLoad();
        final View sendButton = header.findViewById(R.id.comment);
        sendButton.setEnabled(false);
        comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                sendButton.setEnabled(!s.toString().isEmpty());
            }
        });
    }

    public void onSendComment(View view) {
        final String str = comment.getText().toString();
        comment.getText().clear();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(comment.getWindowToken(), 0);

        new AsyncTask<Void, Void, Comment>() {
            @Override
            protected Comment doInBackground(Void... params) {
                return serverApi.sendComment(id, str);
            }

            @Override
            protected void onPostExecute(Comment comment) {
                if (comment == null) {
                    Toast.makeText(EventActivity.this, R.string.error_auth, Toast.LENGTH_SHORT).show();
                    EventActivity.this.comment.setText(str);
                    return;
                }
                commentAdapter.getComments().add(0, comment);
                commentAdapter.notifyItemInserted(1);
            }
        }.execute();
    }

    @Override
    public Loader<List<Comment>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<List<Comment>>(this) {
            @Override
            public List<Comment> loadInBackground() {
                return serverApi.getComments(EventActivity.this.id, args.getInt("page"));
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Comment>> loader, List<Comment> data) {
        commentAdapter.getComments().addAll(data);
        int size = commentAdapter.getItemCount();
        commentAdapter.notifyItemRangeInserted(size - data.size(), data.size());
    }

    @Override
    public void onLoaderReset(Loader<List<Comment>> loader) {
        commentAdapter.getComments().clear();

    }

    private class JoinEvent extends AsyncTask<Boolean, Void, Void> {
        protected Void doInBackground(Boolean... args) {
            Boolean join = args[0];
            JSONObject jsonObject = new JSONObject();
            if (join) {
                jsonObject = serverApi.joinEvent(id);
            } else {
                jsonObject = serverApi.unjoinEvent(id);
            }
            System.out.println(jsonObject);


            return null;
        }
        }



    private class LoadEvent extends AsyncTask<String, Void, JSONObject> {
        protected JSONObject doInBackground(String... args) {
           String id = args[0];

            JSONObject jsonObject = serverApi.loadEvent(id);

            return jsonObject;

        }


        protected void onPostExecute(JSONObject result) {
            try {
                event = Utils.parseEvent(result);
                description.setText(event.getDescription());
                join.setChecked(event.getJoin());
                quantity.setText(event.getAttendances()+"");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Date dateStart = simpleDateFormat.parse(event.getStart());
                Date dateEnd = simpleDateFormat.parse(event.getEnd());
                simpleDateFormat = new SimpleDateFormat("EEE, MMM dd kk:mm");




                time.setText(simpleDateFormat.format(dateStart) + " - " + simpleDateFormat.format(dateEnd));
                if(event.getBudgetMax() == event.getBudgetMin()){
                    budget.setText("" + event.getBudgetMin());
                } else {
                    budget.setText(event.getBudgetMin()+ " - " + event.getBudgetMax());
                }

                if(event.getPlace()!=null){
                    address.setClickable(true);
                    lat = event.getPlace().get(1);
                    lng = event.getPlace().get(0);
                    address.setText(R.string.see_on_map);
                } else {
                    address.setCompoundDrawables(null, null, null, null);
                    address.setText(event.getAddress());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }


    public void onMap(View v){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra(Event.LAT, lat);
        intent.putExtra(Event.LNG, lng);
        startActivity(intent);
    }

    public void onJoiners(View v){

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPrepareDialog(int id, final Dialog dialog, final Bundle args) {
        final AlertDialog ad = (AlertDialog)dialog;
        ad.setMessage(args.getString("comment_text"));
        View.OnClickListener dialogClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View btn) {
                ad.dismiss();
                int pos = args.getInt("position");
                commentAdapter.getComments().remove(pos);
                commentAdapter.notifyItemRemoved(pos+1);
                if (btn.getTag() != null) {
                    new AsyncTask<Void, Void, Void>() {

                        @Override
                        protected Void doInBackground(Void... params) {
                            serverApi.deleteComment(event.getId(), args.getString("comment_id"));
                            return null;
                        }
                    }.execute();

                }
            }
        };
        ad.getButton(DialogInterface.BUTTON_POSITIVE).setTag(new Object());
        ad.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(dialogClickListener);
        ad.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(dialogClickListener);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_REMOVE_COMMENT) {
            AlertDialog.Builder ab = new AlertDialog.Builder(this);
            ab.setTitle(R.string.remove_this_comment);
            ab.setPositiveButton(android.R.string.yes, null);
            ab.setNegativeButton(android.R.string.no, null);
            ab.setMessage("");
            return ab.create();
        }
        return null;

    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Comment c = null;
            try {
                c = new Comment(new JSONObject(intent.getStringExtra("data")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (!commentAdapter.getComments().isEmpty() && TextUtils.equals(c.getId(), commentAdapter.getComments().get(0).getId()))
                return;
            commentAdapter.getComments().add(0, c);
            commentAdapter.notifyItemInserted(1);
        }
    };
}
