package com.demand.well_family.well_family.falldiagnosisstory.base.view;

import android.support.v4.widget.NestedScrollView;
import android.view.View;

import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.dto.FallDiagnosisStoryInfo;
import com.demand.well_family.well_family.falldiagnosisstory.base.adapter.FallDiagnosisStoryAdapter;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-06-01.
 */

public interface FallDiagnosisStoryView {
    void init();

    void showMessage(String message);

    View getDecorView();

    void setToolbar(View decorView);

    void showToolbarTitle(String message);

    void setFallDiagnosisStoryAdapterInit(ArrayList<FallDiagnosisStory> fallDiagnosisStoryList);

    void setFallDiagnosisStoryAdapterItem(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, FallDiagnosisStoryInfo fallDiagnosisStoryInfo, FallDiagnosisStory fallDiagnosisStory);

    void showFallDiagnosisStoryAdapterCommentCount(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, String count);

    void showFallDiagnosisStoryAdapterLikeCount(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, String count);

//    void setFallDiagnosisStoryAdapterLikeChecked(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, int position);
//
//    void setFallDiagnosisStoryAdapterLikeUnChecked(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, int position);

    void setFallDiagnosisStoryAdapterLikeIsChecked(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, int position);

    void setFallDiagnosisStoryAdapterLikeIsUnChecked(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, int position);


    void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY);

    void showProgressDialog();

    void goneProgressDialog();

    void showFallDiagnosisStoryAdapterNotifyItemChanged(int position);

    void setFallDiagnosisStoryAdapter(FallDiagnosisStoryAdapter fallDiagnosisStoryAdapter);

    FallDiagnosisStoryAdapter getFallDiagnosisStoryAdapter();

    void setFallDiagnosisStoryAdapterLikeDown(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, int position);

    void setFallDiagnosisStoryAdapterLikeUp(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, int position);

    void showFallDiagnosisStoryAdapterResult(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, String result);

    void showFallDiagnosisStoryAdapterScore(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, String score);

    void navigateToFallDiagnosisStoryDetailActivity(FallDiagnosisStory fallDiagnosisStory, FallDiagnosisStoryInfo fallDiagnosisStoryInfo);

    void showScoreTextChangeColorWithSafe(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder);

    void showScoreTextChangeColorWithCaution(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder);

    void showScoreTextChangeColorWithRisk(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder);
}
