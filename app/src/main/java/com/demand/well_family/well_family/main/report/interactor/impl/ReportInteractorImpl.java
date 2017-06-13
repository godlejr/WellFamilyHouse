package com.demand.well_family.well_family.main.report.interactor.impl;

import com.demand.well_family.well_family.dto.Category;
import com.demand.well_family.well_family.dto.Report;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.main.report.interactor.ReportInteractor;
import com.demand.well_family.well_family.main.report.presenter.ReportPresenter;
import com.demand.well_family.well_family.repository.MainServerConnection;
import com.demand.well_family.well_family.repository.UserServerConnection;
import com.demand.well_family.well_family.repository.interceptor.NetworkInterceptor;
import com.demand.well_family.well_family.util.ErrorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ㅇㅇ on 2017-05-22.
 */

public class ReportInteractorImpl implements ReportInteractor{
    private ReportPresenter reportPresenter;

    private Report report;
    private int reportCategoryId;

    private UserServerConnection userServerConnection;
    private MainServerConnection mainServerConnection;
    private static final Logger logger = LoggerFactory.getLogger(ReportInteractorImpl.class);

    public ReportInteractorImpl(ReportPresenter reportPresenter) {
        this.reportPresenter = reportPresenter;
    }

    @Override
    public void setReport(Report report) {
        this.report = report;
    }

    @Override
    public Report getReport() {
        return this.report;
    }

    @Override
    public void setStoryReported(User user) {
        int storyCategoryId = report.getWriting_category_id();
        int storyId = report.getWriting_id();

        String accessToken = user.getAccess_token();
        int userId = user.getId();

        HashMap<String, String> map = new HashMap<>();
        map.put("story_category_id", String.valueOf(storyCategoryId));
        map.put("report_category_id", String.valueOf(reportCategoryId));
        map.put("story_id", String.valueOf(storyId));

        userServerConnection = new NetworkInterceptor(accessToken).getClientForUserServer().create(UserServerConnection.class);
        final Call<ResponseBody> call_report = userServerConnection.insert_story_report(userId, map);
        call_report.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    reportPresenter.onSuccessSetStoryReported();
                } else {
                    reportPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log(t);
                reportPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void setReportCategoryId(int reportCategoryId) {
        this.reportCategoryId = reportCategoryId;
    }

    @Override
    public int getReportCategoryId() {
        return this.reportCategoryId;
    }

    @Override
    public void setCommentReported(User user) {
        int commentCategoryId = report.getWriting_category_id();
        int commentId = report.getWriting_id();

        String accessToken = user.getAccess_token();
        int userId = user.getId();

        HashMap<String, String> map = new HashMap<>();
        map.put("comment_category_id", String.valueOf(commentCategoryId));
        map.put("report_category_id", String.valueOf(reportCategoryId));
        map.put("comment_id", String.valueOf(commentId));

        userServerConnection = new NetworkInterceptor(accessToken).getClientForUserServer().create(UserServerConnection.class);
        final Call<ResponseBody> call_report = userServerConnection.insert_comment_report(userId, map);
        call_report.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    reportPresenter.onSuccessSetCommentReported();
                } else {
                   reportPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
               reportPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void getReportCategoryList(User user) {
        String accessToken = user.getAccess_token();

        mainServerConnection = new NetworkInterceptor(accessToken).getClientForMainServer().create(MainServerConnection.class);
        final Call<ArrayList<Category>> call_report_category_list = mainServerConnection.report_category_List();
        call_report_category_list.enqueue(new Callback<ArrayList<Category>>() {
            @Override
            public void onResponse(Call<ArrayList<Category>> call, Response<ArrayList<Category>> response) {
                if(response.isSuccessful()) {
                    ArrayList<Category> reportCategoryList = response.body();
                    reportPresenter.onSuccessGetReportCategoryList(reportCategoryList);
                } else {
                    reportPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Category>> call, Throwable t) {
                log(t);
                reportPresenter.onNetworkError(null);
            }
        });

    }


    private static void log(Throwable throwable) {
        StackTraceElement[] ste = throwable.getStackTrace();
        String className = ste[0].getClassName();
        String methodName = ste[0].getMethodName();
        int lineNumber = ste[0].getLineNumber();
        String fileName = ste[0].getFileName();

        if (LogFlag.printFlag) {
            if (logger.isInfoEnabled()) {
                logger.error("Exception: " + throwable.getMessage());
                logger.error(className + "." + methodName + " " + fileName + " " + lineNumber + " " + "line");
            }
        }
    }
}
