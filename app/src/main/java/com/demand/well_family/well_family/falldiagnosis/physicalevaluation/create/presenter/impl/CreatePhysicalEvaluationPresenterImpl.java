package com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.presenter.impl;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.PhysicalEvaluation;
import com.demand.well_family.well_family.dto.PhysicalEvaluationCategory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.interactor.CreatePhysicalEvaluationInteractor;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.interactor.impl.CreatePhysicalEvaluationInteractorImpl;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.presenter.CreatePhysicalEvaluationPresenter;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.view.CreatePhysicalEvaluationView;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.PreferenceUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-25.
 */

public class CreatePhysicalEvaluationPresenterImpl implements CreatePhysicalEvaluationPresenter {
    private CreatePhysicalEvaluationInteractor createPhysicalEvaluationInteractor;
    private CreatePhysicalEvaluationView createPhysicalEvaluationView;
    private PreferenceUtil preferenceUtil;
    private CountDownHandler countDownHandler;
    private TimerHandler timerHandler;

    public CreatePhysicalEvaluationPresenterImpl(Context context) {
        this.createPhysicalEvaluationInteractor = new CreatePhysicalEvaluationInteractorImpl(this);
        this.createPhysicalEvaluationView = (CreatePhysicalEvaluationView) context;
        this.preferenceUtil = new PreferenceUtil(context);
        this.countDownHandler = new CountDownHandler();
        this.timerHandler = new TimerHandler();
    }

    @Override
    public void onCreate(FallDiagnosisCategory fallDiagnosisCategory) {
        createPhysicalEvaluationInteractor.setFallDiagnosisCategory(fallDiagnosisCategory);
        createPhysicalEvaluationView.init();

        View decorView = createPhysicalEvaluationView.getDecorView();
        createPhysicalEvaluationView.setToolbar(decorView);
        createPhysicalEvaluationView.showToolbarTitle("신체능력 평가");
    }

    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            createPhysicalEvaluationView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            createPhysicalEvaluationView.showMessage(apiErrorUtil.message());
        }
    }

    @Override
    public void onLoadData() {
        User user = preferenceUtil.getUserInfo();
        createPhysicalEvaluationInteractor.getPhysicalEvaluationCategories(user);
    }

    @Override
    public void onSuccessGetPhysicalEvaluationCategories(ArrayList<PhysicalEvaluationCategory> physicalEvaluationList) {
        createPhysicalEvaluationView.setCreatePhysicalEvaluationAdapter(physicalEvaluationList);
    }

    @Override
    public void onClickStart() {
        createPhysicalEvaluationView.playCountDown();
        createPhysicalEvaluationView.gonePlayButton();
        createPhysicalEvaluationInteractor.getCountDownSecond();
    }

    @Override
    public void onCompletionCountDown() {
        createPhysicalEvaluationView.goneCountDown();
        createPhysicalEvaluationView.showPauseButton();
        createPhysicalEvaluationView.showTimerLayout();
        createPhysicalEvaluationInteractor.getTimer();
    }

    @Override
    public void onSuccessGetCountDownSecond(int second) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        if (second > 0) {
            bundle.putString("message", String.valueOf(second));
        } else {
            bundle.putString("message", "시작합니다.");
        }
        message.setData(bundle);

        countDownHandler.sendMessage(message);
    }

    @Override
    public void onSuccessGetTimer(int minute, int second, int milliSecond) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("minute", createPhysicalEvaluationInteractor.getTimes(minute));
        bundle.putString("second", createPhysicalEvaluationInteractor.getTimes(second));
        bundle.putString("milliSecond", createPhysicalEvaluationInteractor.getTimes(milliSecond));

        message.setData(bundle);

        timerHandler.sendMessage(message);
    }

    @Override
    public void onClickPause() {
        createPhysicalEvaluationInteractor.setTimerPause();
        createPhysicalEvaluationView.gonePauseButton();
        createPhysicalEvaluationView.showReplayAndNextButton();
    }

    @Override
    public void onClickReplay() {
        createPhysicalEvaluationView.gonewReplayAndNextButton();
        createPhysicalEvaluationView.showPauseButton();
        createPhysicalEvaluationInteractor.getTimer();
    }

    @Override
    public void onClickNext(PhysicalEvaluation physicalEvaluation, int position) {
        ArrayList<PhysicalEvaluationCategory> physicalEvaluationCategoryList = createPhysicalEvaluationInteractor.getPhysicalEvaluationCategoryList();
        int physicalEvaluationCategorySize = physicalEvaluationCategoryList.size();
        int physicalEvaluationCategoryId = physicalEvaluationCategoryList.get(position).getId();

        if (position != physicalEvaluationCategorySize-1) {
            physicalEvaluation.setPhysical_evaluation_category_id(physicalEvaluationCategoryId);
            createPhysicalEvaluationInteractor.setPhysicalEvaluationAdded(physicalEvaluation);
            createPhysicalEvaluationView.gonewReplayAndNextButton();
            createPhysicalEvaluationView.showPlayButton();
            createPhysicalEvaluationView.goneTimerLayout();
            createPhysicalEvaluationView.showCountDown();
            createPhysicalEvaluationView.setNextView(position);

        } else {
            physicalEvaluation.setPhysical_evaluation_category_id(physicalEvaluationCategoryId);
            createPhysicalEvaluationInteractor.setPhysicalEvaluationAdded(physicalEvaluation);

            FallDiagnosisCategory fallDiagnosisCategory = createPhysicalEvaluationInteractor.getFallDiagnosisCategory();
            ArrayList<PhysicalEvaluation> physicalEvaluationList = createPhysicalEvaluationInteractor.getPhysicalEvaluationList();
            createPhysicalEvaluationView.navigateToPhysicalEvaluationResultActivity(fallDiagnosisCategory, physicalEvaluationCategoryList, physicalEvaluationList);
        }
    }

    @Override
    public void onBackPressed() {
        createPhysicalEvaluationView.releaseCountDown();
        createPhysicalEvaluationView.navigateToBack();
    }

    public class CountDownHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String message = bundle.getString("message");

            createPhysicalEvaluationView.showCountDown(message);
        }
    }


    public class TimerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String minute = bundle.getString("minute");
            String second = bundle.getString("second");
            String milliSecond = bundle.getString("milliSecond");

            createPhysicalEvaluationView.showMinute(minute);
            createPhysicalEvaluationView.showSecond(second);
            createPhysicalEvaluationView.showMilliSecond(milliSecond);
        }
    }
}
