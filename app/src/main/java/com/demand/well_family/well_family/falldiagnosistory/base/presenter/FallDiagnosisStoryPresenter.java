package com.demand.well_family.well_family.falldiagnosistory.base.presenter;

import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.falldiagnosistory.base.adapter.FallDiagnosisStoryAdapter;
import com.demand.well_family.well_family.util.APIErrorUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-06-01.
 */

public interface FallDiagnosisStoryPresenter {
    void onCreate(User user);
    void onNetworkError(APIErrorUtil apiErrorUtil);
    void onLoadData();
    void onSuccessGetFallDiagnosisStory(ArrayList<FallDiagnosisStory> fallDiagnosisStoryList);

    void onLoadContent(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder ,int position);
}
