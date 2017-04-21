package com.demand.well_family.well_family.family.photo.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.family.photo.presenter.PhotosPresenter;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-04-21.
 */

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder> {
    private PhotosPresenter photosPresenter;

    private Context context;
    private ArrayList<Photo> photoList;
    private int layout;

    public PhotosAdapter(Context context, ArrayList<Photo> photoList, int layout, PhotosPresenter photosPresenter) {
        this.context = context;
        this.photoList = photoList;
        this.layout = layout;
        this.photosPresenter = photosPresenter;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PhotoViewHolder holder = new PhotoViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        Glide.with(context).load(context.getString(R.string.cloud_front_stories_images) + photoList.get(position).getName() + "." + photoList.get(position).getExt()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(
                holder.iv_item_photo);
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }


    public class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView iv_item_photo;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            iv_item_photo = (ImageView) itemView.findViewById(R.id.iv_item_photo);

            iv_item_photo.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_item_photo:
                    int position = getAdapterPosition();
                    photosPresenter.onClickPhoto(photoList, position);

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
