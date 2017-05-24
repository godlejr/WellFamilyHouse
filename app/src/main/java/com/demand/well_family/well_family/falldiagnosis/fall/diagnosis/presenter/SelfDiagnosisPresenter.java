package com.demand.well_family.well_family.falldiagnosis.fall.diagnosis.presenter;

import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.SelfDiagnosisCategory;
import com.demand.well_family.well_family.util.APIErrorUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-23.
 */

public interface SelfDiagnosisPresenter {
    void onCreate(FallDiagnosisCategory fallDiagnosisCategory);

    void onClickAnswer(int position, int categorySize, int flag);

    void onNetworkError(APIErrorUtil apiErrorUtil);

    void onSuccessGetDiagnosisCategories(ArrayList<SelfDiagnosisCategory> diagnosisCategoryList);

    void onLoadData();


}
