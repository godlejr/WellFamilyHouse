package com.demand.well_family.well_family.story.detail.adapter.photo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.story.detail.presenter.StoryDetailPresenter;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-04-21.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    private StoryDetailPresenter storyDetailPresenter;

    private Context context;
    private ArrayList<Photo> photoList;
    private int layout;

    public PhotoAdapter(Context context, ArrayList<Photo> photoList, int layout, StoryDetailPresenter storyDetailPresenter) {
        this.context = context;
        this.photoList = photoList;
        this.layout = layout;
        this.storyDetailPresenter = storyDetailPresenter;
    }


    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PhotoViewHolder photoViewHolder = new PhotoViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
        return photoViewHolder;
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, final int position) {
        Glide.with(context).load(context.getString(R.string.cloud_front_stories_images) + photoList.get(position).getName() + "." + photoList.get(position).getExt()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_item_detail_photo);
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public ArrayList<Photo> getPhotoList() {
        return this.photoList;
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView iv_item_detail_photo;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            iv_item_detail_photo = (ImageView) itemView.findViewById(R.id.iv_item_detail_photo);
            iv_item_detail_photo.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_item_detail_photo:
                    int position = getAdapterPosition();

                    storyDetailPresenter.onClickPhoto(position);
                    break;
            }

        }
    }
}
