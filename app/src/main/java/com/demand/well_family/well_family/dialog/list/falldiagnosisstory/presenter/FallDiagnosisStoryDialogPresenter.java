package com.demand.well_family.well_family.dialog.list.falldiagnosisstory.presenter;

import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.dto.FallDiagnosisStoryInfo;
import com.demand.well_family.well_family.util.APIErrorUtil;

/**
 * Created by ㅇㅇ on 2017-06-08.
 */

public interface FallDiagnosisStoryDialogPresenter {
    void onCreate(FallDiagnosisStory fallDiagnosisStory, FallDiagnosisStoryInfo fallDiagnosisStoryInfo);

    void onClickDialog(int position);

    void onLoadData();

    void onSuccessSetFallDiagnosisStoryDeleted();

    void onNetworkError(APIErrorUtil apiErrorUtil);
}
