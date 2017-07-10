package com.demand.well_family.well_family.exercisestory.detail.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.CommentInfo;
import com.demand.well_family.well_family.exercisestory.detail.presenter.ExerciseStoryDetailPresenter;
import com.demand.well_family.well_family.falldiagnosisstory.detail.presenter.FallDiagnosisStoryDetailPresenter;
import com.demand.well_family.well_family.util.CalculateDateUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-07-07.
 */


public class ExerciseStoryDetailCommentAdapter extends RecyclerView.Adapter<ExerciseStoryDetailCommentAdapter.ExerciseStoryDetailCommentViewHolder> {
    private ExerciseStoryDetailPresenter exerciseStoryDetailPresenter;
    private Context context;
    private ArrayList<CommentInfo> commentInfoList;

    public ExerciseStoryDetailCommentAdapter(Context context, ArrayList<CommentInfo> commentInfoList, ExerciseStoryDetailPresenter exerciseStoryDetailPresenter) {
        this.exerciseStoryDetailPresenter = exerciseStoryDetailPresenter;
        this.context = context;
        this.commentInfoList = commentInfoList;
    }

    @Override
    public ExerciseStoryDetailCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ExerciseStoryDetailCommentViewHolder holder = new ExerciseStoryDetailCommentViewHolder(LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ExerciseStoryDetailCommentViewHolder holder, int position) {
        CommentInfo commentInfo = commentInfoList.get(position);

        holder.tv_item_comment_name.setText(commentInfo.getUser_name());
        holder.tv_item_comment_content.setText(commentInfo.getContent());
        holder.tv_item_comment_date.setText(CalculateDateUtil.calculateDate(commentInfo.getCreated_at()));
        Glide.with(context).load(context.getString(R.string.cloud_front_user_avatar) + commentInfo.getAvatar()).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_item_comment_avatar);
    }

    @Override
    public int getItemCount() {
        return commentInfoList.size();
    }

    public void setCommentAdded(CommentInfo commentInfo) {
        commentInfoList.add(commentInfo);
    }

    public void setCommentDelete(int position) {
        commentInfoList.remove(position);
    }

    public void setCommentSetContent(int position, String content) {
        commentInfoList.get(position).setContent(content);
    }

    public class ExerciseStoryDetailCommentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView iv_item_comment_avatar;
        private TextView tv_item_comment_name;
        private TextView tv_item_comment_content;
        private TextView tv_item_comment_date;

        public ExerciseStoryDetailCommentViewHolder(View itemView) {
            super(itemView);
            iv_item_comment_avatar = (ImageView) itemView.findViewById(R.id.iv_item_comment_avatar);
            tv_item_comment_name = (TextView) itemView.findViewById(R.id.tv_item_comment_name);
            tv_item_comment_content = (TextView) itemView.findViewById(R.id.tv_item_comment_content);
            tv_item_comment_date = (TextView) itemView.findViewById(R.id.tv_item_comment_date);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            CommentInfo commentInfo = commentInfoList.get(getAdapterPosition());
            commentInfo.setPosition(getAdapterPosition());

            exerciseStoryDetailPresenter.onClickComment(commentInfo);
        }
    }
}