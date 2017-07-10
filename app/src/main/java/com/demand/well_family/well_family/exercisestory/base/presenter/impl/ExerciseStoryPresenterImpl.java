package com.demand.well_family.well_family.exercisestory.base.presenter.impl;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.demand.well_family.well_family.dto.ExerciseStory;
import com.demand.well_family.well_family.dto.FallDiagnosisStoryInfo;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.exercisestory.base.adapter.ExerciseStoryAdapter;
import com.demand.well_family.well_family.exercisestory.base.interactor.ExerciseStoryInteractor;
import com.demand.well_family.well_family.exercisestory.base.interactor.impl.ExerciseStoryInteractorImpl;
import com.demand.well_family.well_family.exercisestory.base.presenter.ExerciseStoryPresenter;
import com.demand.well_family.well_family.exercisestory.base.view.ExerciseStoryView;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.PreferenceUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-06-27.
 */

public class ExerciseStoryPresenterImpl implements ExerciseStoryPresenter {
    private ExerciseStoryInteractor exerciseStoryInteractor;
    private ExerciseStoryView exerciseStoryView;
    private PreferenceUtil preferenceUtil;
    private StoryChangeHandler storyChangeHandler;
    private Handler mainHanlder;

    public ExerciseStoryPresenterImpl(Context context) {
        this.exerciseStoryInteractor = new ExerciseStoryInteractorImpl(this);
        this.exerciseStoryView = (ExerciseStoryView) context;
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate(int storyUserId) {
        User user = preferenceUtil.getUserInfo();
        exerciseStoryInteractor.setUser(user);
        exerciseStoryInteractor.getExerciseStoryData(storyUserId);

        exerciseStoryView.init();

        View decorView = exerciseStoryView.getDecorView();
        exerciseStoryView.setToolbar(decorView);
        exerciseStoryView.showToolbarTitle("낙상예방");
    }

    @Override
    public void onLoadData() {
        mainHanlder = new MainHanlder();
        exerciseStoryView.showProgressDialog();

    }

    @Override
    public void onLoadContent(ExerciseStoryAdapter.ExerciseStoryViewHolder holder, ExerciseStory exerciseStory) {
        int exerciseStoryId = exerciseStory.getId();
        exerciseStoryInteractor.getCommentCount(holder, exerciseStoryId);
        exerciseStoryInteractor.getLikeCount(holder, exerciseStoryId);
        exerciseStoryInteractor.getContentLikeCheck(holder, exerciseStory);
    }


    @Override
    public void onSuccessGetExerciseStoryData() {
        ArrayList<ExerciseStory> fallDiagnosisStory = exerciseStoryInteractor.getExerciseStoryListWithOffset();
        exerciseStoryView.setExerciseStoryAdapterItem(fallDiagnosisStory);

        Bundle bundle = new Bundle();
        bundle.putSerializable("exerciseStoryAdapter", exerciseStoryView.getExerciseStoryAdapter());

        Message message = new Message();
        message.setData(bundle);

        mainHanlder.sendMessage(message);
        mainHanlder.postDelayed(new Runnable() {
            @Override
            public void run() {
                exerciseStoryView.goneProgressDialog();
            }
        }, 200);


    }

    @Override
    public void onGettingStoryDataAdded() {
        exerciseStoryView.showProgressDialog();
        storyChangeHandler = new StoryChangeHandler();
    }

    @Override
    public void onSuccessGetStoryDataAdded(int position) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putInt("item_id", position);
        message.setData(bundle);
        storyChangeHandler.sendMessage(message);
    }

    @Override
    public void onSuccessThreadRun() {
        exerciseStoryView.goneProgressDialog();
    }

    @Override
    public void onScrollChange(int difference) {
        if (difference <= 0) {
            exerciseStoryInteractor.getStoryDataAdded();
        }
    }

    @Override
    public void onClickContentBody(ExerciseStory exerciseStory) {
        int exerciseStoryId = exerciseStory.getId();
        exerciseStoryInteractor.setExerciseStoryHit(exerciseStoryId);
        exerciseStoryView.navigateToExerciseStoryDetailActivity(exerciseStory);
    }

    @Override
    public void onCheckedChangeForLike(ExerciseStoryAdapter.ExerciseStoryViewHolder holder, ExerciseStory exerciseStory, boolean isChecked) {
        boolean isFirstChecked = exerciseStory.getFirstChecked();

        if (isFirstChecked) {
            if (isChecked) {
                exerciseStoryInteractor.setContentLikeUp(holder, exerciseStory);
            } else {
                exerciseStoryInteractor.setContentLikeDown(holder, exerciseStory);
            }
        }
    }

    @Override
    public void onSuccessSetContentLikeDown(ExerciseStoryAdapter.ExerciseStoryViewHolder holder, int position) {
        exerciseStoryView.setExerciseStoryAdapterLikeDown(holder, position);
    }

    @Override
    public void onSuccessSetContentLikeUp(ExerciseStoryAdapter.ExerciseStoryViewHolder holder, int position) {
        exerciseStoryView.setExerciseStoryAdapterLikeUp(holder, position);
    }

    @Override
    public void onSuccessGetCommentCount(ExerciseStoryAdapter.ExerciseStoryViewHolder holder, int count) {
        exerciseStoryView.showExerciseStoryAdapterCommentCount(holder, String.valueOf(count));
    }

    @Override
    public void onSuccessGetLikeCount(ExerciseStoryAdapter.ExerciseStoryViewHolder holder, int count) {
        exerciseStoryView.showExerciseStoryAdapterLikeCount(holder, String.valueOf(count));
    }

    @Override
    public void onSuccessGetContentLikeCheck(ExerciseStoryAdapter.ExerciseStoryViewHolder holder, int isChecked, int position) {
        if (isChecked > 0) {
            exerciseStoryView.setExerciseStoryAdapterLikeIsChecked(holder, position);
        } else {
            exerciseStoryView.setExerciseStoryAdapterLikeIsUnChecked(holder, position);
        }
    }


    public class StoryChangeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            int position = bundle.getInt("item_id");

            exerciseStoryView.showExerciseStoryAdapterNotifyItemChanged(position);
        }
    }

    public class MainHanlder extends Handler {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            Bundle bundle = message.getData();
            ExerciseStoryAdapter fallDiagnosisStoryAdapter = (ExerciseStoryAdapter) bundle.getSerializable("exerciseStoryAdapter");

            exerciseStoryView.setExerciseStoryAdapter(fallDiagnosisStoryAdapter);
        }
    }


    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            exerciseStoryView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            exerciseStoryView.showMessage(apiErrorUtil.message());
        }
    }

}
