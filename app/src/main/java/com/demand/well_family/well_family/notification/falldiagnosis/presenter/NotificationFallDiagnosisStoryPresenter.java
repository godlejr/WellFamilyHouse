package com.demand.well_family.well_family.notification.falldiagnosis.presenter;

import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.util.APIErrorUtil;

/**
 * Created by ㅇㅇ on 2017-06-13.
 */

public interface NotificationFallDiagnosisStoryPresenter {
    void onNetworkError(APIErrorUtil apiErrorUtil);
    void onCreate(FallDiagnosisStory fallDiagnosisStory);
    void onLoadData();
}
