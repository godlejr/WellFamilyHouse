package com.demand.well_family.well_family.family.base.adapter.content;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.dto.StoryInfo;
import com.demand.well_family.well_family.family.base.adapter.photo.PhotoAdapter;
import com.demand.well_family.well_family.family.base.presenter.FamilyPresenter;
import com.demand.well_family.well_family.util.CalculateDateUtil;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-04-18.
 */

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ContentViewHolder> implements Serializable {
    private FamilyPresenter familyPresenter;

    private Context context;
    private ArrayList<StoryInfo> storyList;
    private int layout;


    //photo recycler
    private RecyclerView rv_main_story;


    public ContentAdapter(Context context, ArrayList<StoryInfo> storyList, int layout, FamilyPresenter familyPresenter) {
        this.context = context;
        this.storyList = storyList;
        this.layout = layout;
        this.familyPresenter = familyPresenter;
    }

    @Override
    public ContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ContentViewHolder contentViewHolder = new ContentViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
        return contentViewHolder;
    }

    @Override
    public void onBindViewHolder(final ContentViewHolder holder, final int position) {

        //set position
        storyList.get(position).setPosition(position);

        //user info
        Glide.with(context).load(context.getString(R.string.cloud_front_user_avatar) + storyList.get(position).getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_writer_avatar);
        holder.tv_writer_name.setText(storyList.get(position).getName());
        holder.tv_write_date.setText(CalculateDateUtil.calculateDate(storyList.get(position).getCreated_at()));

        //content
        holder.tv_content_text.setText(storyList.get(position).getContent());

        //photo, likeCount, commentCount, likeCheck
        familyPresenter.onLoadContent(holder, storyList.get(position));
    }


    @Override
    public int getItemCount() {
        return storyList.size();
    }

    public void setContentDelete(int position) {
        storyList.remove(position);
    }

    public void setContentChange(int position, String content, Boolean likeCheck) {
        storyList.get(position).setFirst_checked(false);
        storyList.get(position).setChecked(likeCheck);
        storyList.get(position).setContent(content);
    }

    public void setContentAdd(StoryInfo storyInfo) {
        storyList.add(0, storyInfo);
    }

    public void setContentLikeCheck(ContentAdapter.ContentViewHolder holder, int position) {
        holder.tv_item_main_story_like.setText(String.valueOf(Integer.parseInt(holder.tv_item_main_story_like.getText().toString()) + 1));
        storyList.get(position).setChecked(true);
    }

    public void setContentLikeUncheck(ContentAdapter.ContentViewHolder holder, int position) {
        holder.tv_item_main_story_like.setText(String.valueOf(Integer.parseInt(holder.tv_item_main_story_like.getText().toString()) - 1));
        storyList.get(position).setChecked(false);
    }

    public void setContentLikeIsChecked(ContentAdapter.ContentViewHolder holder, int position) {
        holder.btn_item_main_story_like.setChecked(true);
        storyList.get(position).setFirst_checked(true);
        storyList.get(position).setChecked(true);
    }

    public void setContentLikeIsNotChecked(ContentAdapter.ContentViewHolder holder, int position) {
        holder.btn_item_main_story_like.setChecked(false);
        storyList.get(position).setFirst_checked(true);
    }

    public void setContentCommentCount(ContentViewHolder holder, String count) {
        holder.tv_item_main_comment_story_count.setText(count);
    }

    public void setContentLikeCount(ContentViewHolder holder, String count) {
        holder.tv_item_main_story_like.setText(count);
    }

    public void setContentNoPhoto(ContentViewHolder holder) {
        holder.story_images_container.setVisibility(View.GONE);
        holder.tv_content_text.setMaxLines(15);
    }

    public void setContentOnePhoto(ContentViewHolder holder, ArrayList<Photo> photoList) {
        holder.story_images_container.removeAllViews();
        holder.story_images_container.setVisibility(View.VISIBLE);
        holder.story_images_container.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1200));
        holder.story_images_inflater.inflate(R.layout.item_main_story_image_one, holder.story_images_container, true);
        ImageView iv_item_main_story_image = (ImageView) holder.story_images_container.findViewById(R.id.iv_item_main_story_image);
        Glide.with(context).load(context.getString(R.string.cloud_front_stories_images) + photoList.get(0).getName() + "." + photoList.get(0).getExt()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_item_main_story_image);
    }

    public void setContentTwoPhoto(ContentViewHolder holder, ArrayList<Photo> photoList) {
        holder.story_images_container.removeAllViews();
        holder.story_images_container.setVisibility(View.VISIBLE);

        holder.story_images_inflater.inflate(R.layout.item_main_story_image_two, holder.story_images_container, true);
        ImageView iv_item_main_story_image_two1 = (ImageView) holder.story_images_container.findViewById(R.id.iv_item_main_story_image_two1);
        Glide.with(context).load(context.getString(R.string.cloud_front_stories_images) + photoList.get(0).getName() + "." + photoList.get(0).getExt()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_item_main_story_image_two1);
        ImageView iv_item_main_story_image_two2 = (ImageView) holder.story_images_container.findViewById(R.id.iv_item_main_story_image_two2);
        Glide.with(context).load(context.getString(R.string.cloud_front_stories_images) + photoList.get(1).getName() + "." + photoList.get(1).getExt()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_item_main_story_image_two2);
    }


    public void setContentMultiPhoto(ContentViewHolder holder, ArrayList<Photo> photoList, StoryInfo storyInfo) {
        holder.story_images_container.removeAllViews();
        holder.story_images_container.setVisibility(View.VISIBLE);

        holder.story_images_inflater.inflate(R.layout.item_main_story_image_list, holder.story_images_container, true);
        rv_main_story = (RecyclerView) holder.story_images_container.findViewById(R.id.rv_main_story_images);
        PhotoAdapter photoAdapter = new PhotoAdapter(context, photoList, R.layout.item_main_story_image, familyPresenter, storyInfo);
        rv_main_story.setAdapter(photoAdapter);
        rv_main_story.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        photoAdapter.notifyDataSetChanged();
    }


    public class ContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        private ImageView iv_writer_avatar;
        private TextView tv_writer_name;
        private TextView tv_write_date;
        private TextView tv_content_text;
        private CheckBox btn_item_main_story_like;

        private TextView tv_item_main_story_like;
        private TextView tv_item_main_comment_story_count;
        private ImageButton btn_item_main_story_comment;
        private ImageView iv_item_story_menu;

        private LinearLayout ll_item_main_story_like_comment_info;
        private LinearLayout story_images_container;
        private LayoutInflater story_images_inflater;

        public ContentViewHolder(View itemView) {
            super(itemView);
            iv_writer_avatar = (ImageView) itemView.findViewById(R.id.iv_item_story_avatar);
            tv_writer_name = (TextView) itemView.findViewById(R.id.tv_item_story_name);
            tv_write_date = (TextView) itemView.findViewById(R.id.tv_item_story_date);
            tv_content_text = (TextView) itemView.findViewById(R.id.tv_item_main_story_content);
            btn_item_main_story_like = (CheckBox) itemView.findViewById(R.id.btn_item_main_story_like);

            iv_item_story_menu = (ImageView) itemView.findViewById(R.id.iv_item_story_menu);
            iv_item_story_menu.setVisibility(View.GONE);

            tv_item_main_story_like = (TextView) itemView.findViewById(R.id.tv_item_main_story_like);
            tv_item_main_comment_story_count = (TextView) itemView.findViewById(R.id.tv_item_main_comment_story_count);

            story_images_container = (LinearLayout) itemView.findViewById(R.id.story_images_container);
            story_images_inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            ll_item_main_story_like_comment_info = (LinearLayout) itemView.findViewById(R.id.ll_item_main_story_like_comment_info);
            btn_item_main_story_comment = (ImageButton) itemView.findViewById(R.id.btn_item_main_story_comment);

            btn_item_main_story_like.setOnCheckedChangeListener(this);

            iv_writer_avatar.setOnClickListener(this);
            tv_writer_name.setOnClickListener(this);

            tv_content_text.setOnClickListener(this);
            story_images_container.setOnClickListener(this);
            ll_item_main_story_like_comment_info.setOnClickListener(this);
            btn_item_main_story_comment.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_item_story_avatar:
                case R.id.tv_item_story_name:
                    int contentUserId = storyList.get(getAdapterPosition()).getUser_id();
                    familyPresenter.onClickContentUser(contentUserId);
                    break;

                case R.id.tv_item_main_story_content:
                case R.id.ll_item_main_story_like_comment_info:
                case R.id.story_images_container:
                case R.id.btn_item_main_story_comment:
                    familyPresenter.onClickContentBody(storyList.get(getAdapterPosition()));
                    break;
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            boolean isFirstChecked = storyList.get(getAdapterPosition()).getFirst_checked();
            familyPresenter.onCheckedChangeForLike(this, storyList.get(getAdapterPosition()), isFirstChecked, isChecked);
        }
    }
}