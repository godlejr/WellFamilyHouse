package com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.presenter;

import com.demand.well_family.well_family.util.APIErrorUtil;

/**
 * Created by ㅇㅇ on 2017-05-25.
 */

public interface CreatePhysicalEvaluationPresenter {
    void onCreate();
    void onNetworkError(APIErrorUtil apiErrorUtil);

}
