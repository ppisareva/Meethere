package com.example.polina.meethere.activities;

import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.polina.meethere.FeedAdapter;
import com.example.polina.meethere.R;
import com.example.polina.meethere.RecyclerViewPositionHelper;
import com.example.polina.meethere.Utils;
import com.example.polina.meethere.model.Feed;
import com.example.polina.meethere.model.User;

public class FeedActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>  {
    private static final int FEED_LOADER = 432112;
    FeedAdapter feedAdapter;
    Bundle arg;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    boolean flag = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.feed_list);
        feedAdapter = new FeedAdapter();
        recyclerView.setAdapter(feedAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        arg = new Bundle();
        arg.putInt(Utils.OFFSET, 0);
        getSupportLoaderManager().initLoader(FEED_LOADER, arg, FeedActivity.this);
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerViewPositionHelper mRecyclerViewHelper = RecyclerViewPositionHelper.createHelper(recyclerView);
                int visibleItemCount = recyclerView.getChildCount();
                int totalItemCount = mRecyclerViewHelper.getItemCount();
                int firstVisibleItem = mRecyclerViewHelper.findFirstVisibleItemPosition();
                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 1) {
                    if (isFlag()) {
                        setFlag(false);
                        arg.putInt(Utils.OFFSET, feedAdapter.getItemCount());
                        getSupportLoaderManager().restartLoader(FEED_LOADER, arg, FeedActivity.this);
                    }
                }

            }
        });

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, Uri.parse("content://com.example.polina.meethere.data.data/feed?offset=" + args.getInt(Utils.OFFSET)),
                new String[]{"_id", User.LAST_NAME,User.FIRST_NAME,
                User.IMAGE, User.ID, Feed.TYPE, Feed.TIME,
                com.example.polina.meethere.adapters.Event.START,
                com.example.polina.meethere.adapters.Event.BUDGET,
                com.example.polina.meethere.adapters.Event.ID,
                com.example.polina.meethere.adapters.Event.NAME,
                com.example.polina.meethere.adapters.Event.DESCRIPTION}, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        System.err.println("onLoaderFinished: " + loader + "  || " + data);
        System.err.println("onLoaderFinished COUNT:: " + data.getCount());
        if (feedAdapter.getItemCount() < data.getCount())
            setFlag(true);
        feedAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
