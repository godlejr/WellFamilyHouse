package com.demand.well_family.well_family.story.detail.adapter.comment;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.CommentInfo;
import com.demand.well_family.well_family.story.detail.presenter.StoryDetailPresenter;
import com.demand.well_family.well_family.util.CalculateDateUtil;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-04-21.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private StoryDetailPresenter storyDetailPresenter;

    private Context context;
    private ArrayList<CommentInfo> commentInfoList;
    private int layout;

    public CommentAdapter(Context context, ArrayList<CommentInfo> commentInfoList, int layout, StoryDetailPresenter storyDetailPresenter) {
        this.context = context;
        this.commentInfoList = commentInfoList;
        this.layout = layout;
        this.storyDetailPresenter = storyDetailPresenter;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommentViewHolder commentViewHolder = new CommentViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
        return commentViewHolder;
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, final int position) {
        Glide.with(context).load(context.getString(R.string.cloud_front_user_avatar) + commentInfoList.get(position).getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_item_comment_avatar);

        holder.tv_item_comment_name.setText(commentInfoList.get(position).getUser_name());
        holder.tv_item_comment_content.setText(commentInfoList.get(position).getContent());
        holder.tv_item_comment_date.setText(CalculateDateUtil.calculateDate(commentInfoList.get(position).getCreated_at()));
    }


    @Override
    public int getItemCount() {
        return commentInfoList.size();
    }

    public void setCommentSetContent(int position, String content) {
        commentInfoList.get(position).setContent(content);


        Log.e(commentInfoList.get(position).getContent(), commentInfoList.get(position).getContent() + "sssssssssssssss");
    }

    public void setCommentDelete(int position) {
        commentInfoList.remove(position);
    }

    public void setCommentAdded(CommentInfo commentInfo) {
        commentInfoList.add(commentInfo);
    }


    public class CommentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private LinearLayout ll_comment;
        private ImageView iv_item_comment_avatar;
        private TextView tv_item_comment_name;
        private TextView tv_item_comment_content;
        private TextView tv_item_comment_date;

        public CommentViewHolder(View itemView) {
            super(itemView);
            ll_comment = (LinearLayout) itemView.findViewById(R.id.ll_comment);
            iv_item_comment_avatar = (ImageView) itemView.findViewById(R.id.iv_item_comment_avatar);
            tv_item_comment_name = (TextView) itemView.findViewById(R.id.tv_item_comment_name);
            tv_item_comment_content = (TextView) itemView.findViewById(R.id.tv_item_comment_content);
            tv_item_comment_date = (TextView) itemView.findViewById(R.id.tv_item_comment_date);

            ll_comment.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_comment:
                    int position = getAdapterPosition();
                    CommentInfo commentInfo = commentInfoList.get(position);
                    commentInfo.setPosition(position);
                    storyDetailPresenter.onClickComment(commentInfo);
                    break;
            }

        }
    }

    public static class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.bottom = space;
            outRect.right = space;
        }
    }
}
