package com.example.polina.meethere.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.polina.meethere.R;
import com.example.polina.meethere.data.Comment;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by polina on 03.07.16.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private final Context context;
    private View header;
    final private List<Comment> comments = new ArrayList();

    public CommentAdapter(Context context, View header) {
        this.context = context;
        this.header = header;
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
        ItemViewHolder holder = (ItemViewHolder)h;
        Comment c = comments.get(position);
        holder.createBy.setText(c.getCreatedBy());
        holder.text.setText(c.getText());
        holder.image.setImageURI(Uri.parse(c.getCreatedByUrl()));
        long now = System.currentTimeMillis();
        long t = Math.min(c.getCreatedAt(), now);
        holder.createdAt.setText(DateUtils.getRelativeTimeSpanString(t, now, DateUtils.SECOND_IN_MILLIS));
    }

    @Override
    public int getItemCount() {
        return comments == null ? 1 : comments.size()+1;
    }

    public class ItemViewHolder extends ViewHolder {
        public TextView text;
        public TextView createBy;
        public TextView createdAt;
        public SimpleDraweeView image;

        public ItemViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
            createBy = (TextView) itemView.findViewById(R.id.created_by);
            createdAt = (TextView) itemView.findViewById(R.id.created_at);
            image = (SimpleDraweeView) itemView.findViewById(R.id.profile_image);
            RoundingParams roundingParams = RoundingParams.asCircle();
            image.getHierarchy().setRoundingParams(roundingParams);

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




