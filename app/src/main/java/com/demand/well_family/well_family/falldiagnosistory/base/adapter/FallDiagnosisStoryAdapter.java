package com.demand.well_family.well_family.falldiagnosistory.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.falldiagnosistory.base.presenter.FallDiagnosisStoryPresenter;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-06-02.
 */

public class FallDiagnosisStoryAdapter extends RecyclerView.Adapter<FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder> {
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


    }

    @Override
    public int getItemCount() {
        return fallDiagnosisStoryList.size();
    }

    public class FallDiagnosisStoryViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_falldiagnosisstory_title;
        private TextView tv_falldiagnosisstory_date;
        private TextView tv_falldiagnosisstory_score;
        private TextView tv_falldiagnosisstory_result;
        private TextView tv_falldiagnosisstory_likecount;
        private TextView tv_falldiagnosisstory_commentcount;
        private ImageView iv_falldiagnosisstory_img;
        private ImageView iv_falldiagnosisstory_likebtn;
        private ImageView iv_falldiagnosisstory_commentbtn;

        public FallDiagnosisStoryViewHolder(View itemView) {
            super(itemView);

            tv_falldiagnosisstory_title = (TextView) itemView.findViewById(R.id.tv_falldiagnosisstory_title);
            tv_falldiagnosisstory_date = (TextView) itemView.findViewById(R.id.tv_falldiagnosisstory_date);
            tv_falldiagnosisstory_score = (TextView) itemView.findViewById(R.id.tv_falldiagnosisstory_score);
            tv_falldiagnosisstory_result = (TextView) itemView.findViewById(R.id.tv_falldiagnosisstory_title);
            tv_falldiagnosisstory_likecount = (TextView) itemView.findViewById(R.id.tv_falldiagnosisstory_likecount);
            tv_falldiagnosisstory_commentcount = (TextView) itemView.findViewById(R.id.tv_falldiagnosisstory_commentcount);
            iv_falldiagnosisstory_img = (ImageView) itemView.findViewById(R.id.iv_falldiagnosisstory_img);
            iv_falldiagnosisstory_likebtn = (ImageView) itemView.findViewById(R.id.iv_falldiagnosisstory_likebtn);
            iv_falldiagnosisstory_commentbtn = (ImageView) itemView.findViewById(R.id.iv_falldiagnosisstory_commentbtn);

        }
    }

}
