package com.demand.well_family.well_family.main.report.presenter;

import com.demand.well_family.well_family.dto.Category;
import com.demand.well_family.well_family.dto.Report;
import com.demand.well_family.well_family.util.APIErrorUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-22.
 */

public interface ReportPresenter {
    void onCreate(Report report, int intentFlag);
    void onNetworkError(APIErrorUtil apiErrorUtil);

    void onSuccessGetReportCategoryList(ArrayList<Category> reportCategoryList);
    void onSuccessSetCommentReported();
    void onSuccessSetStoryReported();

    void onClickReport(int reportCategoryId);
}
