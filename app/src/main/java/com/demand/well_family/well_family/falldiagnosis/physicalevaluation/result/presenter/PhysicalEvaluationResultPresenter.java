package com.demand.well_family.well_family.falldiagnosis.physicalevaluation.result.presenter;

import com.demand.well_family.well_family.util.APIErrorUtil;

/**
 * Created by ㅇㅇ on 2017-05-26.
 */

public interface PhysicalEvaluationResultPresenter {
    void onCreate();
    void onNetworkError(APIErrorUtil apiErrorUtil);

    void onClickConfirm();

}
