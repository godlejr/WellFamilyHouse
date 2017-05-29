package com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.view;

import android.view.View;

import com.demand.well_family.well_family.dto.PhysicalEvaluationCategory;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-25.
 */

public interface CreatePhysicalEvaluationView {
    void init();

    void showToolbarTitle(String message);

    View getDecorView();

    void setToolbar(View decorView);

    void showMessage(String message);

    void setCreatePhysicalEvaluationAdapter(ArrayList<PhysicalEvaluationCategory> createPhysicalEvaluationList);

    void setNextView(int position);

    void playCountDown();

    void showReplayAndNextButton();

    void gonewReplayAndNextButton();


    void showPauseButton();

    void gonePauseButton();

    void showPlayButton();


    void gonePlayButton();

    void showCountDown(String message);

    void showCountDown();

    void goneCountDown();

    void showTimerLayout();

    void goneTimerLayout();


    void showMinute(String minute);

    void showSecond(String second);

    void showMilliSecond(String milliSecond);



}
