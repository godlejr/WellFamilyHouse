package com.demand.well_family.well_family.main.report.view;

import android.view.View;

import com.demand.well_family.well_family.dto.Category;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-22.
 */

public interface ReportView {
    void init();
    void showMessage(String message);

    void setToolbar(View decorView);
    View getDecorView();
    void showToolbarTitle(String title);

    void navigateToBack();

    void setReportAdapterInit(ArrayList<Category> reportList);

    void setAuthorName(String authorName);
    void setReportContent(String content);
}
