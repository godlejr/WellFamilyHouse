package com.demand.well_family.well_family.falldiagnosistory.detail.adapter.environment.photo;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.EnvironmentPhoto;
import com.demand.well_family.well_family.falldiagnosistory.detail.presenter.FallDiagnosisStoryDetailPresenter;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-06-09.
 */

public class FallDiagnosisStoryDetailEnvEvaluationPhotoAdapter extends RecyclerView.Adapter<FallDiagnosisStoryDetailEnvEvaluationPhotoAdapter.FallDiagnosisStoryDetailEnvEvaluationPhotoViewHolder> implements View.OnClickListener {
    private FallDiagnosisStoryDetailPresenter fallDiagnosisStoryDetailPresenter;
    private Context context;
    private ArrayList<EnvironmentPhoto> environmentPhotoList;

    public FallDiagnosisStoryDetailEnvEvaluationPhotoAdapter(Context context, ArrayList<EnvironmentPhoto> environmentPhotoList, FallDiagnosisStoryDetailPresenter fallDiagnosisStoryDetailPresenter) {
        this.fallDiagnosisStoryDetailPresenter = fallDiagnosisStoryDetailPresenter;
        this.context = context;
        this.environmentPhotoList = environmentPhotoList;
    }

    @Override
    public FallDiagnosisStoryDetailEnvEvaluationPhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FallDiagnosisStoryDetailEnvEvaluationPhotoViewHolder holder = new FallDiagnosisStoryDetailEnvEvaluationPhotoViewHolder(LayoutInflater.from(context).inflate(R.layout.item_falldiagnosisstorydetail_envevaluation_photo, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(FallDiagnosisStoryDetailEnvEvaluationPhotoViewHolder holder, int position) {
        EnvironmentPhoto environmentPhoto = environmentPhotoList.get(position);
        String fileName = environmentPhoto.getName() + "." + environmentPhoto.getExt();

        Glide.with(context).load(context.getString(R.string.cloud_front_stories_environment) + fileName).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_falldiagnosisstorydetail_envevaluation);

        holder.iv_falldiagnosisstorydetail_envevaluation.setOnClickListener(this);

    }

    @Override
    public int getItemCount() {
        return environmentPhotoList.size();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_falldiagnosisstorydetail_envevaluation:
                fallDiagnosisStoryDetailPresenter.onClickPhoto(environmentPhotoList);
                break;
        }
    }


    public class FallDiagnosisStoryDetailEnvEvaluationPhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_falldiagnosisstorydetail_envevaluation;

        public FallDiagnosisStoryDetailEnvEvaluationPhotoViewHolder(View itemView) {
            super(itemView);
            iv_falldiagnosisstorydetail_envevaluation = (ImageView) itemView.findViewById(R.id.iv_falldiagnosisstorydetail_envevaluation);
        }
    }
}
