package com.demand.well_family.well_family.dialog.list.exercise.presenter;

import com.demand.well_family.well_family.dto.ExerciseStory;
import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.dto.FallDiagnosisStoryInfo;
import com.demand.well_family.well_family.util.APIErrorUtil;

/**
 * Created by ㅇㅇ on 2017-07-07.
 */

public interface ExerciseStoryDialogPresenter {
    void onNetworkError(APIErrorUtil apiErrorUtil);

    void onCreate(ExerciseStory exerciseStory);

    void onLoadData();

    void onSuccessSetFallDiagnosisStoryDeleted();

    void onClickDialog(int adapterPosition);
}
