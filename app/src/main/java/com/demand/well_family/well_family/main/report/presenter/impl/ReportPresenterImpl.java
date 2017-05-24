package com.demand.well_family.well_family.main.report.presenter.impl;

import android.content.Context;
import android.view.View;

import com.demand.well_family.well_family.dto.Category;
import com.demand.well_family.well_family.dto.Report;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.flag.ReportINTETNFlag;
import com.demand.well_family.well_family.main.report.interactor.ReportInteractor;
import com.demand.well_family.well_family.main.report.interactor.impl.ReportInteractorImpl;
import com.demand.well_family.well_family.main.report.presenter.ReportPresenter;
import com.demand.well_family.well_family.main.report.view.ReportView;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.PreferenceUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-22.
 */

public class ReportPresenterImpl implements ReportPresenter{
    private ReportInteractor reportInteractor;
    private ReportView reportView;
    private PreferenceUtil preferenceUtil;

    private int intentFlag;

    public ReportPresenterImpl(Context context) {
        this.reportInteractor = new ReportInteractorImpl(this);
        this.reportView = (ReportView)context;
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate(Report report, int intentFlag) {
        this.intentFlag = intentFlag;
        reportInteractor.setReport(report);

        reportView.init();
        reportView.setAuthorName(report.getAuthor_name());
        reportView.setReportContent(report.getWriting_content());

        View decorView = reportView.getDecorView();
        reportView.setToolbar(decorView);
        reportView.showToolbarTitle("신고하기");

        User user = preferenceUtil.getUserInfo();
        reportInteractor.getReportCategoryList(user);
    }

    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            reportView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            reportView.showMessage(apiErrorUtil.message());
        }
    }

    @Override
    public void onSuccessGetReportCategoryList(ArrayList<Category> reportCategoryList) {
        reportView.setReportAdapterInit(reportCategoryList);
    }

    @Override
    public void onSuccessSetCommentReported() {
        reportView.showMessage("신고가 완료되었습니다.");
        reportView.navigateToBack();
    }

    @Override
    public void onSuccessSetStoryReported() {
        reportView.showMessage("신고가 완료되었습니다.");
        reportView.navigateToBack();
    }

    @Override
    public void onClickReport(int reportCategoryId) {
        User user = preferenceUtil.getUserInfo();
        reportInteractor.setReportCategoryId(reportCategoryId);

        if(intentFlag == ReportINTETNFlag.COMMENT){
            reportInteractor.setCommentReported(user);
        } else if (intentFlag == ReportINTETNFlag.STORY){
            reportInteractor.setStoryReported(user);
        }
    }


}
