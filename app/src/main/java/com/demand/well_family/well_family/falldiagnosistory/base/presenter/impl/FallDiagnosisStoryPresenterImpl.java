package com.demand.well_family.well_family.falldiagnosistory.base.presenter.impl;

import android.content.Context;
import android.view.View;

import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.falldiagnosistory.base.adapter.FallDiagnosisStoryAdapter;
import com.demand.well_family.well_family.falldiagnosistory.base.interactor.FallDiagnosisStoryInteractor;
import com.demand.well_family.well_family.falldiagnosistory.base.interactor.impl.FallDiagnosisStoryInteractorImpl;
import com.demand.well_family.well_family.falldiagnosistory.base.presenter.FallDiagnosisStoryPresenter;
import com.demand.well_family.well_family.falldiagnosistory.base.view.FallDiagnosisStoryView;
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

    public FallDiagnosisStoryPresenterImpl(Context context) {
        this.fallDiagnosisStoryInteractor = new FallDiagnosisStoryInteractorImpl(this);
        this.fallDiagnosisStoryView = (FallDiagnosisStoryView)context;
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate(User user) {
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
        fallDiagnosisStoryInteractor.getFallDiagnosisStory();
    }

    @Override
    public void onSuccessGetFallDiagnosisStory(ArrayList<FallDiagnosisStory> fallDiagnosisStoryList) {
        fallDiagnosisStoryView.setFallDiagnosisStoryAdapterInit(fallDiagnosisStoryList);
    }

    @Override
    public void onLoadContent(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, int position) {

    }
}
