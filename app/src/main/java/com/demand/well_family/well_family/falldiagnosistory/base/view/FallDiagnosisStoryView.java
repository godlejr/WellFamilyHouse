package com.demand.well_family.well_family.falldiagnosistory.base.view;

import android.view.View;

import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.falldiagnosistory.base.adapter.FallDiagnosisStoryAdapter;

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

    void setFallDiagnosisStoryAdapterCommentCount(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, String count);
    void setFallDiagnosisStoryAdapterLikeCount(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, String count);
}
