package com.demand.well_family.well_family.falldiagnosis.selfdiagnosis.result.presenter;

import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.util.APIErrorUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-23.
 */

public interface SelfDiagnosisResultPresenter {
    void onCreate(ArrayList<Integer> answerList, FallDiagnosisCategory fallDiagnosisCategory, int fallDiagnosisContentCategorySize);

    void onLoadData();

    void onClickSendResult();

    void onNetworkError(APIErrorUtil apiErrorUtil);

    void onSuccessSetStoryAdded(int storyId);

    void onSuccessSetSelfDiagnosisAdded();
}
