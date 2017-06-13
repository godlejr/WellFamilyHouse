package com.demand.well_family.well_family.dialog.list.falldiagnosisstory.presenter.impl;

import android.content.Context;

import com.demand.well_family.well_family.dialog.list.falldiagnosisstory.interactor.FallDiagnosisStoryDialogInteractor;
import com.demand.well_family.well_family.dialog.list.falldiagnosisstory.interactor.impl.FallDiagnosisStoryDialogInteractorImpl;
import com.demand.well_family.well_family.dialog.list.falldiagnosisstory.presenter.FallDiagnosisStoryDialogPresenter;
import com.demand.well_family.well_family.dialog.list.falldiagnosisstory.view.FallDiagnosisStoryDialogView;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.util.PreferenceUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-06-08.
 */

public class FallDiagnosisStoryDialogPresenterImpl implements FallDiagnosisStoryDialogPresenter {
    private FallDiagnosisStoryDialogInteractor fallDiagnosisStoryDialogInteractor;
    private FallDiagnosisStoryDialogView fallDiagnosisStoryDialogView;
    private PreferenceUtil preferenceUtil;

    public FallDiagnosisStoryDialogPresenterImpl(Context context) {
        this.fallDiagnosisStoryDialogInteractor = new FallDiagnosisStoryDialogInteractorImpl(this);
        this.fallDiagnosisStoryDialogView = (FallDiagnosisStoryDialogView) context;
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate() {
        User user = preferenceUtil.getUserInfo();
        fallDiagnosisStoryDialogInteractor.setUser(user);

        fallDiagnosisStoryDialogView.init();
    }

    @Override
    public void onLoadData() {
        ArrayList<String> dialogList = new ArrayList<>();
        dialogList.add("삭제");
        fallDiagnosisStoryDialogView.setFallDiagnosisStoryDialogAdapterList(dialogList);
    }

    @Override
    public void onClickDialog(int position) {
        if(position == 0){
            fallDiagnosisStoryDialogInteractor.setFallDiagnosisStoryDeleted();
        }
    }

}
