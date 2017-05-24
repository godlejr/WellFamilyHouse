package com.demand.well_family.well_family.falldiagnosis.base.presenter;

import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.falldiagnosis.base.adapter.FallDiagnosisCategoryAdapter;
import com.demand.well_family.well_family.util.APIErrorUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-24.
 */

public interface FallDiagnosisPresenter {
    void onCreate();

    String setCategoryContent(FallDiagnosisCategoryAdapter.FallDiagnosisCategoryViewHolder holder, int categoryId);

    void getDiagnosisCategory();

    void onNetworkError(APIErrorUtil apiErrorUtil);

    void onSuccessGetCategoryList(ArrayList<FallDiagnosisCategory> fallDiagnosisCategoryList);

    void onClickCategory(FallDiagnosisCategory fallDiagnosisCategory);
}
