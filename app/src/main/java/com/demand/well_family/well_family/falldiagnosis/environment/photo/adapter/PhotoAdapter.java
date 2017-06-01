package com.demand.well_family.well_family.falldiagnosis.environment.photo.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.falldiagnosis.environment.photo.presenter.EnvironmentEvaluationPhotoPresenter;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-05-31.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    private EnvironmentEvaluationPhotoPresenter environmentEvaluationPhotoPresenter;
    private ArrayList<Uri> photoList;
    private Context context;
    private int layout;

    public PhotoAdapter(ArrayList<Uri> photoList, Context context, int layout,EnvironmentEvaluationPhotoPresenter environmentEvaluationPhotoPresenter) {
        this.photoList = photoList;
        this.context = context;
        this.layout = layout;
        this.environmentEvaluationPhotoPresenter = environmentEvaluationPhotoPresenter;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PhotoViewHolder holder = new PhotoViewHolder((LayoutInflater.from(context).inflate(layout, parent, false)));
        return holder;
    }

    @Override
    public void onBindViewHolder(PhotoAdapter.PhotoViewHolder holder, int position) {
        Glide.with(context).load( photoList.get(position) ).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_write_upload_image);
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public void setPhotoDelete(int position){
        photoList.remove(position);
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iv_write_upload_image, iv_write_upload_delete;

        public PhotoViewHolder(View itemView) {
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
                    environmentEvaluationPhotoPresenter.onClickPhotoDelete(position);
                    break;
            }
        }
    }
}