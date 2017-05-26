package com.demand.well_family.well_family.falldiagnosis.physicalevaluation.base.presenter;

import com.demand.well_family.well_family.util.APIErrorUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-25.
 */

public interface PhysicalEvaluationPresenter {
    void onCreate();

    void onNetworkError(APIErrorUtil apiErrorUtil);

    void onLoadData();

    void onSuccessGetPhysicalEvaluationCategories(ArrayList<String> physicalEvaluationList);

    void onClickStart();

}
