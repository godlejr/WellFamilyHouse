package com.demand.well_family.well_family.falldiagnosistory.base.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.dto.FallDiagnosisStoryInfo;
import com.demand.well_family.well_family.falldiagnosistory.base.presenter.FallDiagnosisStoryPresenter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-06-02.
 */

public class FallDiagnosisStoryAdapter extends RecyclerView.Adapter<FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder> implements Serializable {
    private FallDiagnosisStoryPresenter fallDiagnosisStoryPresenter;
    private ArrayList<FallDiagnosisStory> fallDiagnosisStoryList;
    private Context context;

    public FallDiagnosisStoryAdapter(Context context, ArrayList<FallDiagnosisStory> fallDiagnosisStoryList, FallDiagnosisStoryPresenter fallDiagnosisStoryPresenter) {
        this.fallDiagnosisStoryPresenter = fallDiagnosisStoryPresenter;
        this.fallDiagnosisStoryList = fallDiagnosisStoryList;
        this.context = context;

    }

    @Override
    public FallDiagnosisStoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FallDiagnosisStoryViewHolder fallDiagnosisStoryViewHolder = new FallDiagnosisStoryViewHolder(LayoutInflater.from(context).inflate(R.layout.item_falldiagnosisstory, parent, false));
        return fallDiagnosisStoryViewHolder;
    }

    @Override
    public void onBindViewHolder(FallDiagnosisStoryViewHolder holder, int position) {

        FallDiagnosisStory fallDiagnosisStory = fallDiagnosisStoryList.get(position);
        fallDiagnosisStory.setPosition(position);
        fallDiagnosisStory.setFirst_checked(false);
        holder.tv_falldiagnosisstory_date.setText(fallDiagnosisStory.getCreated_at());

        fallDiagnosisStoryPresenter.onLoadContent(holder, fallDiagnosisStory);
    }

    @Override
    public int getItemCount() {
        return fallDiagnosisStoryList.size();
    }

    public void setFallDiagnosisStoryAdapterCommentCount(FallDiagnosisStoryViewHolder holder, String count) {
        holder.tv_falldiagnosisstory_commentcount.setText(count);
    }

    public void setFallDiagnosisStoryAdapterLikeCount(FallDiagnosisStoryViewHolder holder, String count) {
        holder.tv_falldiagnosisstory_likecount.setText(count);
    }

    public void setFallDiagnosisStoryAdapterItem(FallDiagnosisStoryViewHolder holder, FallDiagnosisStoryInfo fallDiagnosisStoryInfo, FallDiagnosisStory fallDiagnosisStory) {
        fallDiagnosisStoryPresenter.setScore(holder, fallDiagnosisStory);
        fallDiagnosisStoryPresenter.setResult(holder);
        holder.tv_falldiagnosisstory_title.setText(String.valueOf(fallDiagnosisStoryInfo.getTitle()));
        Glide.with(context).load(context.getString(R.string.cloud_front_self_diagnosis) + fallDiagnosisStoryInfo.getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_falldiagnosisstory_img);
    }

    public void setFallDiagnosisStoryAdapterLikeIsChecked(FallDiagnosisStoryViewHolder holder, int position) {
        holder.cb_falldiagnosisstory_likebtn.setChecked(true);
        fallDiagnosisStoryList.get(position).setFirst_checked(true);

    }

    public void setFallDiagnosisStoryAdapterLikeIsUnChecked(FallDiagnosisStoryViewHolder holder, int position) {
        holder.cb_falldiagnosisstory_likebtn.setChecked(false);
        fallDiagnosisStoryList.get(position).setFirst_checked(true);

    }

    public void setFallDiagnosisStoryAdapterLikeChecked(FallDiagnosisStoryViewHolder holder, int position) {
        holder.tv_falldiagnosisstory_likecount.setText(String.valueOf(Integer.parseInt(holder.tv_falldiagnosisstory_likecount.getText().toString()) + 1));
        holder.cb_falldiagnosisstory_likebtn.setChecked(true);
    }


    public void setFallDiagnosisStoryAdapterLikeUnChecked(FallDiagnosisStoryViewHolder holder, int position) {
        holder.tv_falldiagnosisstory_likecount.setText(String.valueOf(Integer.parseInt(holder.tv_falldiagnosisstory_likecount.getText().toString()) - 1));
        holder.cb_falldiagnosisstory_likebtn.setChecked(false);
    }

    public void setFallDiagnosisStoryAdapterResult(FallDiagnosisStoryViewHolder holder, String result) {
        holder.tv_falldiagnosisstory_result.setText(result);
    }

    public void setFallDiagnosisStoryAdapterScore(FallDiagnosisStoryViewHolder holder, String score) {
        holder.tv_falldiagnosisstory_score.setText(score);
    }

   public void showScoreTextChangeColorWithSafe(FallDiagnosisStoryViewHolder holder){
       holder.tv_falldiagnosisstory_score.setTextColor(context.getResources().getColor(R.color.green));
   }

    public void showScoreTextChangeColorWithCaution(FallDiagnosisStoryViewHolder holder){
        holder.tv_falldiagnosisstory_score.setTextColor(context.getResources().getColor(R.color.yellow));
    }

    public void showScoreTextChangeColorWithRisk(FallDiagnosisStoryViewHolder holder){
        holder.tv_falldiagnosisstory_score.setTextColor(context.getResources().getColor(R.color.red));
    }


    public class FallDiagnosisStoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        private TextView tv_falldiagnosisstory_title;
        private TextView tv_falldiagnosisstory_date;
        private TextView tv_falldiagnosisstory_score;
        private TextView tv_falldiagnosisstory_result;
        private TextView tv_falldiagnosisstory_likecount;
        private TextView tv_falldiagnosisstory_commentcount;
        private ImageView iv_falldiagnosisstory_img;
        private CheckBox cb_falldiagnosisstory_likebtn;
        private ImageView iv_falldiagnosisstory_commentbtn;

        public FallDiagnosisStoryViewHolder(View itemView) {
            super(itemView);

            tv_falldiagnosisstory_title = (TextView) itemView.findViewById(R.id.tv_falldiagnosisstory_title);
            tv_falldiagnosisstory_date = (TextView) itemView.findViewById(R.id.tv_falldiagnosisstory_date);
            tv_falldiagnosisstory_score = (TextView) itemView.findViewById(R.id.tv_falldiagnosisstory_score);
            tv_falldiagnosisstory_result = (TextView) itemView.findViewById(R.id.tv_falldiagnosisstory_result);
            tv_falldiagnosisstory_likecount = (TextView) itemView.findViewById(R.id.tv_falldiagnosisstory_likecount);
            tv_falldiagnosisstory_commentcount = (TextView) itemView.findViewById(R.id.tv_falldiagnosisstory_commentcount);
            iv_falldiagnosisstory_img = (ImageView) itemView.findViewById(R.id.iv_falldiagnosisstory_img);
            cb_falldiagnosisstory_likebtn = (CheckBox) itemView.findViewById(R.id.cb_falldiagnosisstory_likebtn);
            iv_falldiagnosisstory_commentbtn = (ImageView) itemView.findViewById(R.id.iv_falldiagnosisstory_commentbtn);

            cb_falldiagnosisstory_likebtn.setOnCheckedChangeListener(this);
            iv_falldiagnosisstory_commentbtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_falldiagnosisstory_img:
                case R.id.tv_falldiagnosisstory_score:
                case R.id.tv_falldiagnosisstory_result:
                case R.id.iv_falldiagnosisstory_commentbtn:
                    fallDiagnosisStoryPresenter.onClickContentBody(fallDiagnosisStoryList.get(getAdapterPosition()));
                    break;
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            fallDiagnosisStoryPresenter.onCheckedChangeForLike(this, fallDiagnosisStoryList.get(getAdapterPosition()), isChecked);
        }


    }


}
