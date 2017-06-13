package com.demand.well_family.well_family.falldiagnosistory.base.presenter.impl;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.dto.FallDiagnosisStoryInfo;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.falldiagnosistory.base.adapter.FallDiagnosisStoryAdapter;
import com.demand.well_family.well_family.falldiagnosistory.base.interactor.FallDiagnosisStoryInteractor;
import com.demand.well_family.well_family.falldiagnosistory.base.interactor.impl.FallDiagnosisStoryInteractorImpl;
import com.demand.well_family.well_family.falldiagnosistory.base.presenter.FallDiagnosisStoryPresenter;
import com.demand.well_family.well_family.falldiagnosistory.base.view.FallDiagnosisStoryView;
import com.demand.well_family.well_family.falldiagnosistory.flag.FallDiagnosisStoryCodeFlag;
import com.demand.well_family.well_family.flag.FallDiagnosisFlag;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.PreferenceUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-06-01.
 */

public class FallDiagnosisStoryPresenterImpl implements FallDiagnosisStoryPresenter {
    private FallDiagnosisStoryInteractor fallDiagnosisStoryInteractor;
    private FallDiagnosisStoryView fallDiagnosisStoryView;
    private PreferenceUtil preferenceUtil;

    private StoryChangeHandler storyChangeHandler;
    private StoryAddHandler storyAddHandler;
    private MainHanlder mainHanlder;

    public FallDiagnosisStoryPresenterImpl(Context context) {
        this.fallDiagnosisStoryInteractor = new FallDiagnosisStoryInteractorImpl(this);
        this.fallDiagnosisStoryView = (FallDiagnosisStoryView) context;
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate(User storyUser) {
        fallDiagnosisStoryInteractor.setStoryUser(storyUser);

        User user = preferenceUtil.getUserInfo();
        fallDiagnosisStoryInteractor.setUser(user);

        fallDiagnosisStoryView.init();

        View decorView = fallDiagnosisStoryView.getDecorView();
        fallDiagnosisStoryView.setToolbar(decorView);
        fallDiagnosisStoryView.showToolbarTitle("낙상진단");
    }

    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            fallDiagnosisStoryView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            fallDiagnosisStoryView.showMessage(apiErrorUtil.message());
        }
    }

    @Override
    public void onLoadData() {
        mainHanlder = new MainHanlder();
        fallDiagnosisStoryView.showProgressDialog();
        fallDiagnosisStoryInteractor.getFallDiagnosisStoryData();
    }

    @Override
    public void onSuccessGetFallDiagnosisStory() {
        ArrayList<FallDiagnosisStory> fallDiagnosisStory = fallDiagnosisStoryInteractor.getFallDiagnosisStoryListWithOffset();
        fallDiagnosisStoryView.setFallDiagnosisStoryAdapterInit(fallDiagnosisStory);

        Bundle bundle = new Bundle();
        bundle.putSerializable("fallDiagnosisStoryAdapter", fallDiagnosisStoryView.getFallDiagnosisStoryAdapter());

        Message message = new Message();
        message.setData(bundle);


        mainHanlder.sendMessage(message);

        mainHanlder.postDelayed(new Runnable() {
            @Override
            public void run() {
                fallDiagnosisStoryView.goneProgressDialog();
            }
        }, 200);
    }

    @Override
    public void onSuccessGetContentLikeCheck(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, int isChecked, int position) {
        if (isChecked > 0) {
            fallDiagnosisStoryView.setFallDiagnosisStoryAdapterLikeIsChecked(holder, position);
        } else {
            fallDiagnosisStoryView.setFallDiagnosisStoryAdapterLikeIsUnChecked(holder, position);
        }
    }

    @Override
    public void onSuccessSetContentLikeUp(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, int position) {
        fallDiagnosisStoryView.setFallDiagnosisStoryAdapterLikeUp(holder, position);
    }

    @Override
    public void onSuccessSetContentLikeDown(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, int position) {
        fallDiagnosisStoryView.setFallDiagnosisStoryAdapterLikeDown(holder, position);
    }

    @Override
    public void onSuccessGetFallDiagnosisStoryCommentCount(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, int count) {
        fallDiagnosisStoryView.showFallDiagnosisStoryAdapterCommentCount(holder, String.valueOf(count));
    }

    @Override
    public void onSuccessGetFallDiagnosisStoryLikeCount(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, int count) {
        fallDiagnosisStoryView.showFallDiagnosisStoryAdapterLikeCount(holder, String.valueOf(count));
    }

    @Override
    public void onSuccessGetFallDiagnosisStoryInfo(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, FallDiagnosisStoryInfo fallDiagnosisStoryInfo, FallDiagnosisStory fallDiagnosisStory) {
        fallDiagnosisStoryView.setFallDiagnosisStoryAdapterItem(holder, fallDiagnosisStoryInfo, fallDiagnosisStory);
        fallDiagnosisStoryInteractor.getContentLikeCheck(holder, fallDiagnosisStory);
    }

    @Override
    public void onLoadContent(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, FallDiagnosisStory fallDiagnosisStory) {
        fallDiagnosisStoryInteractor.getFallDiagnosisStoryInfoData(holder, fallDiagnosisStory);
        fallDiagnosisStoryInteractor.getFallDiagnosisStoryLikeCount(holder, fallDiagnosisStory);
        fallDiagnosisStoryInteractor.getFallDiagnosisStoryCommentCount(holder, fallDiagnosisStory);

    }

    @Override
    public void onClickContentBody(FallDiagnosisStory fallDiagnosisStory) {
        FallDiagnosisStoryInfo fallDiagnosisStoryInfo = fallDiagnosisStory.getFallDiagnosisStoryInfo();

        fallDiagnosisStoryInteractor.setFallDiagnosisStoryHit(fallDiagnosisStory);

        fallDiagnosisStoryView.navigateToFallDiagnosisStoryDetailActivity(fallDiagnosisStory, fallDiagnosisStoryInfo );
    }

    @Override
    public void onScrollChange(int difference) {
        if (difference <= 0) {
            fallDiagnosisStoryInteractor.getStoryDataAdded();
        }
    }

    @Override
    public void onGettingStoryDataAdded() {
        fallDiagnosisStoryView.showProgressDialog();
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
        fallDiagnosisStoryView.goneProgressDialog();
    }


    public class MainHanlder extends Handler {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            Bundle bundle = message.getData();
            FallDiagnosisStoryAdapter fallDiagnosisStoryAdapter = (FallDiagnosisStoryAdapter) bundle.getSerializable("fallDiagnosisStoryAdapter");

            fallDiagnosisStoryView.setFallDiagnosisStoryAdapter(fallDiagnosisStoryAdapter);
        }
    }


    public class StoryChangeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            int position = bundle.getInt("item_id");

            fallDiagnosisStoryView.showFallDiagnosisStoryAdapterNotifyItemChanged(position);
        }
    }

    public class StoryAddHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            int position = bundle.getInt("item_id");

            fallDiagnosisStoryView.showFallDiagnosisStoryAdapterNotifyItemChanged(position);
        }
    }

    @Override
    public void onCheckedChangeForLike(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, FallDiagnosisStory fallDiagnosisStory, boolean isChecked) {
        boolean isFirstChecked = fallDiagnosisStory.getFirstChecked();

        if (isFirstChecked) {
            if (isChecked) {
                fallDiagnosisStoryInteractor.setContentLikeUp(holder, fallDiagnosisStory);
            } else {
                fallDiagnosisStoryInteractor.setContentLikeDown(holder, fallDiagnosisStory);
            }
        }

    }

    @Override
    public void setResult(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder) {
        FallDiagnosisStoryInfo fallDiagnosisStoryInfo = fallDiagnosisStoryInteractor.getFallDiagnosisStoryInfo();
        fallDiagnosisStoryView.showFallDiagnosisStoryAdapterResult(holder, fallDiagnosisStoryInfo.getRisk_comment());
    }

    @Override
    public void setScore(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, FallDiagnosisStory fallDiagnosisStory) {
        FallDiagnosisStoryInfo fallDiagnosisStoryInfo = fallDiagnosisStoryInteractor.getFallDiagnosisStoryInfo();

        int riskCategoryId = fallDiagnosisStory.getFall_diagnosis_risk_category_id();
        int categoryId = fallDiagnosisStory.getFall_diagnosis_category_id();
        int totalCount = fallDiagnosisStoryInfo.getTotal_count();
        int score = fallDiagnosisStoryInfo.getScore();

        if (categoryId == FallDiagnosisFlag.PHYSICAL_EVALUATION) {
            fallDiagnosisStoryView.showFallDiagnosisStoryAdapterScore(holder, score + "점");
        }

        if (categoryId == FallDiagnosisFlag.SELF_DIAGNOSIS || categoryId == FallDiagnosisFlag.RISK_EVALUATION) {
            fallDiagnosisStoryView.showFallDiagnosisStoryAdapterScore(holder, score + "/" + totalCount);
        }

        if (riskCategoryId == FallDiagnosisStoryCodeFlag.SAFE) {
            fallDiagnosisStoryView.showScoreTextChangeColorWithSafe(holder);
        }
        if (riskCategoryId == FallDiagnosisStoryCodeFlag.CAUTION) {
            fallDiagnosisStoryView.showScoreTextChangeColorWithCaution(holder);
        }
        if (riskCategoryId == FallDiagnosisStoryCodeFlag.RISK) {
            fallDiagnosisStoryView.showScoreTextChangeColorWithRisk(holder);
        }

    }
}
