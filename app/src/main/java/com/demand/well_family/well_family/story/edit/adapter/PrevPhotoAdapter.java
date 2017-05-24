package com.demand.well_family.well_family.story.edit.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.story.edit.flag.EditStoryCodeFlag;
import com.demand.well_family.well_family.story.edit.presenter.EditStoryPresenter;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-05-22.
 */

public class PrevPhotoAdapter extends RecyclerView.Adapter<PrevPhotoAdapter.PrevPhotoViewHolder> {
    private EditStoryPresenter editStoryPresenter;
    private ArrayList<URL> photoList;
    private Context context;
    private int layout;

    public PrevPhotoAdapter(ArrayList<URL> photoList, Context context, int layout, EditStoryPresenter editStoryPresenter) {
        this.photoList = photoList;
        this.context = context;
        this.layout = layout;
        this.editStoryPresenter = editStoryPresenter;
    }

    @Override
    public PrevPhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PrevPhotoViewHolder holder = new PrevPhotoViewHolder((LayoutInflater.from(context).inflate(layout, parent, false)));
        return holder;
    }

    @Override
    public void onBindViewHolder(PrevPhotoViewHolder holder, int position) {
        Glide.with(context).load(photoList.get(position)).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_write_upload_image);
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public void setPhotoDelete(int position){
        photoList.remove(position);
    }

    public class PrevPhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView iv_write_upload_image;
        private ImageView iv_write_upload_delete;

        public PrevPhotoViewHolder(View itemView) {
            super(itemView);

            iv_write_upload_image = (ImageView) itemView.findViewById(R.id.iv_write_upload_image);
            iv_write_upload_delete = (ImageView) itemView.findViewById(R.id.iv_write_upload_delete);

            iv_write_upload_delete.bringToFront();
            iv_write_upload_delete.invalidate();

            iv_write_upload_delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_write_upload_delete:
                    int position = getAdapterPosition();
                    editStoryPresenter.onClickPhotoDelete(position, EditStoryCodeFlag.PREV_PHOTO_FLAG);
                    break;
            }
        }
    }
}