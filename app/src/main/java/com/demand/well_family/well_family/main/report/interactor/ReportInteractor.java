package com.demand.well_family.well_family.main.report.interactor;

import com.demand.well_family.well_family.dto.Report;
import com.demand.well_family.well_family.dto.User;

/**
 * Created by ㅇㅇ on 2017-05-22.
 */

public interface ReportInteractor {
    void getReportCategoryList(User user);
    void setReport(Report report);
    Report getReport();

    void setCommentReported(User user);
    void setStoryReported(User user);

    void setReportCategoryId(int reportCategoryId);
    int getReportCategoryId();

}
