package com.demand.well_family.well_family.dialog.list.falldiagnosisstory.view;

import com.demand.well_family.well_family.dto.FallDiagnosisStory;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-06-08.
 */

public interface FallDiagnosisStoryDialogView {
    void init();

    void setFallDiagnosisStoryDialogAdapterList(ArrayList<String> dialogList);

    void showMessage(String message);

    void navigateToBackForResultOk(FallDiagnosisStory fallDiagnosisStory);
}
