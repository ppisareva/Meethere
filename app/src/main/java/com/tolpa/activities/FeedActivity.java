package com.tolpa.activities;

import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.tolpa.adapters.FeedAdapter;
import com.tolpa.R;
import com.tolpa.adapters.RecyclerViewPositionHelper;
import com.tolpa.Utils;
import com.tolpa.model.Feed;
import com.tolpa.model.User;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.news));
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.feed_list);
        feedAdapter = new FeedAdapter(this);
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
        return new CursorLoader(this, Uri.parse(Utils.URI_ATHORITY +"feed?offset=" + args.getInt(Utils.OFFSET)),
                new String[]{"_id", User.LAST_NAME,User.FIRST_NAME,
                User.IMAGE, User.ID, Feed.TYPE, Feed.TIME,
                com.tolpa.adapters.Event.START,
                com.tolpa.adapters.Event.BUDGET,
                com.tolpa.adapters.Event.ID,
                com.tolpa.adapters.Event.NAME,
                com.tolpa.adapters.Event.DESCRIPTION}, null, null, null);
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
