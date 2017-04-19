package com.demand.well_family.well_family.family.base.adapter.photo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.dto.StoryInfo;
import com.demand.well_family.well_family.family.base.presenter.FamilyPresenter;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-04-18.
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    private FamilyPresenter familyPresenter;

    private Context context;
    private ArrayList<Photo> photoList;
    private StoryInfo storyInfo;

    private int layout;

    public PhotoAdapter(Context context, ArrayList<Photo> photoList, int layout, FamilyPresenter familyPresenter, StoryInfo storyInfo) {
        this.context = context;
        this.photoList = photoList;
        this.layout = layout;
        this.familyPresenter = familyPresenter;
        this.storyInfo = storyInfo;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PhotoViewHolder photoViewHolder = new PhotoViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
        return photoViewHolder;
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        Glide.with(context).load(context.getString(R.string.cloud_front_stories_images) + photoList.get(position).getName() + "." + photoList.get(position).getExt()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_main_story_content);
        divideMultiPhotos(holder.ll_main_story_image, holder.iv_main_story_content);
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    private void divideMultiPhotos(LinearLayout ll_main_story_image, ImageView iv_main_story_content) {

        ll_main_story_image.setLayoutParams(new LinearLayout.LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, context.getResources().getDisplayMetrics()),
                LinearLayout.LayoutParams.MATCH_PARENT));

        iv_main_story_content.setLayoutParams(new LinearLayout.LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, context.getResources().getDisplayMetrics()),
                LinearLayout.LayoutParams.MATCH_PARENT));
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout ll_main_story_image;
        private ImageView iv_main_story_content;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ll_main_story_image = (LinearLayout) itemView.findViewById(R.id.ll_main_story_image);
            iv_main_story_content = (ImageView) itemView.findViewById(R.id.iv_main_story_content);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    familyPresenter.onClickContentPhoto(storyInfo);
                }
            });
        }
    }
}
