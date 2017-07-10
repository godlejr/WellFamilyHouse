package com.demand.well_family.well_family.dialog.list.falldiagnosisstory.presenter.impl;

import android.content.Context;

import com.demand.well_family.well_family.dialog.list.falldiagnosisstory.flag.FallDiagnosisStoryActFlag;
import com.demand.well_family.well_family.dialog.list.falldiagnosisstory.interactor.FallDiagnosisStoryDialogInteractor;
import com.demand.well_family.well_family.dialog.list.falldiagnosisstory.interactor.impl.FallDiagnosisStoryDialogInteractorImpl;
import com.demand.well_family.well_family.dialog.list.falldiagnosisstory.presenter.FallDiagnosisStoryDialogPresenter;
import com.demand.well_family.well_family.dialog.list.falldiagnosisstory.view.FallDiagnosisStoryDialogView;
import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.dto.FallDiagnosisStoryInfo;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.util.APIErrorUtil;
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
    public void onCreate(FallDiagnosisStory fallDiagnosisStory, FallDiagnosisStoryInfo fallDiagnosisStoryInfo) {
        User user = preferenceUtil.getUserInfo();
        fallDiagnosisStoryDialogInteractor.setUser(user);
        fallDiagnosisStoryDialogInteractor.setFallDiagnosisStory(fallDiagnosisStory);
        fallDiagnosisStoryDialogInteractor.setFallDiagnosisStoryInfo(fallDiagnosisStoryInfo);

        fallDiagnosisStoryDialogView.init();
    }

    @Override
    public void onLoadData() {
        ArrayList<String> dialogList = new ArrayList<>();
        dialogList.add("삭제");
        fallDiagnosisStoryDialogView.setFallDiagnosisStoryDialogAdapterList(dialogList);
    }

    @Override
    public void onSuccessSetFallDiagnosisStoryDeleted() {
        FallDiagnosisStory fallDiagnosisStory = fallDiagnosisStoryDialogInteractor.getFallDiagnosisStory();
        fallDiagnosisStoryDialogView.navigateToBackForResultOk(fallDiagnosisStory);
        fallDiagnosisStoryDialogView.showMessage("게시글이 삭제되었습니다.");
    }

    @Override
    public void onClickDialog(int position) {
        if(position == FallDiagnosisStoryActFlag.DELETE){
            fallDiagnosisStoryDialogInteractor.setFallDiagnosisStoryDeleted();
        }
    }

    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            fallDiagnosisStoryDialogView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            fallDiagnosisStoryDialogView.showMessage(apiErrorUtil.message());
        }
    }

}
