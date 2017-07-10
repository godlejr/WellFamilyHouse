package com.demand.well_family.well_family.dialog.list.exercise.view;

import com.demand.well_family.well_family.dto.ExerciseStory;
import com.demand.well_family.well_family.dto.FallDiagnosisStory;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-07-07.
 */

public interface ExerciseStoryDialogView {
    void init();

    void setExerciseStoryDialogAdapterList(ArrayList<String> dialogList);

    void showMessage(String message);

    void navigateToBackForResultOk(ExerciseStory fallDiagnosisStory);
}
