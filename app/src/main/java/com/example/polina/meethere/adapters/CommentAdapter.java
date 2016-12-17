package com.example.polina.meethere.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.polina.meethere.R;
import com.example.polina.meethere.Utils;
import com.example.polina.meethere.activities.UserProfileActivity;
import com.example.polina.meethere.model.Comment;
import com.example.polina.meethere.model.App;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by polina on 03.07.16.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> implements View.OnLongClickListener {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private final Context context;
    private View header;
    private AdapterView.OnItemLongClickListener listener;
    final private List<Comment> comments = new ArrayList();

    public CommentAdapter(Context context, View header, AdapterView.OnItemLongClickListener listener) {
        this.context = context;
        this.header = header;
        this.listener = listener;
    }


    public List<Comment> getComments() {
        return comments;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, null);

            ViewHolder viewHolder = new ItemViewHolder(view);
            return viewHolder;
        } else if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder(header);
        }
        return null;
    }

    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int position) {
        if (position == 0) return;
        position--;
        h.itemView.setTag(position);
        final ItemViewHolder holder = (ItemViewHolder)h;
        Comment c = comments.get(position);
        holder.userId = c.getCreatedById();
        holder.createBy.setText(c.getCreatedBy());
        holder.text.setText(c.getText());
        holder.setUserId(c.getCreatedById());
        holder.image.setImageURI(Uri.parse(c.getCreatedByUrl()));
        long now = System.currentTimeMillis();
        long t = Math.min(c.getCreatedAt(), now);
        holder.createdAt.setText(DateUtils.getRelativeTimeSpanString(t, now, DateUtils.SECOND_IN_MILLIS));
    }

    @Override
    public int getItemCount() {
        return comments == null ? 1 : comments.size()+1;
    }

    @Override
    public boolean onLongClick(View v) {
        if (listener != null) {
            int position = (int) v.getTag();
            listener.onItemLongClick(null, v, position, 0);
        }
        return false;
    }



    public class ItemViewHolder extends ViewHolder implements View.OnClickListener {
        public TextView text;
        public TextView createBy;
        public TextView createdAt;
        public SimpleDraweeView image;
        public View container;

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int userId;

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(CommentAdapter.this);
            text = (TextView) itemView.findViewById(R.id.text);
            createBy = (TextView) itemView.findViewById(R.id.created_by);
            createdAt = (TextView) itemView.findViewById(R.id.created_at);
            image = (SimpleDraweeView) itemView.findViewById(R.id.profile_image);
            RoundingParams roundingParams = RoundingParams.asCircle();
            image.getHierarchy().setRoundingParams(roundingParams);
            image.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            System.out.println(userId);
            if(userId==((App)context.getApplicationContext()).getUserProfile().getId()) return;
            Intent intent = new Intent(context, UserProfileActivity.class);
            intent.putExtra(Utils.USER_ID, userId);
            context.startActivity(intent);
        }
    }

    public class HeaderViewHolder extends ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }
    abstract class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}




