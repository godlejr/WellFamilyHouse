package com.demand.well_family.well_family.dialog.list.story.interactor.impl;

import com.demand.well_family.well_family.dialog.list.story.activity.StoryDialogActivity;
import com.demand.well_family.well_family.dialog.list.story.interactor.StoryDialogInteractor;
import com.demand.well_family.well_family.dialog.list.story.presenter.StoryDialogPresenter;
import com.demand.well_family.well_family.dto.Report;
import com.demand.well_family.well_family.dto.Story;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.StoryServerConnection;
import com.demand.well_family.well_family.repository.interceptor.HeaderInterceptor;
import com.demand.well_family.well_family.util.ErrorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ㅇㅇ on 2017-04-20.
 */

public class StoryDialogInteractorImpl implements StoryDialogInteractor {
    private StoryDialogPresenter storyDialogPresenter;
    private StoryServerConnection storyServerConnection;
    private static final Logger logger = LoggerFactory.getLogger(StoryDialogActivity.class);

    private User user;
    private Story story;
    private Report report;

    public StoryDialogInteractorImpl(StoryDialogPresenter storyDialogPresenter) {
        this.storyDialogPresenter = storyDialogPresenter;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public Story getStory() {
        return this.story;
    }

    @Override
    public void setStory(Story story) {
        this.story = story;
    }

    @Override
    public void setReport(Report report) {
        this.report = report;
    }

    @Override
    public Report getReport() {
        return report;
    }


    @Override
    public void setStoryDeleted(int storyId) {
        String accessToken = user.getAccess_token();

        storyServerConnection = new HeaderInterceptor(accessToken).getClientForStoryServer().create(StoryServerConnection.class);
        Call<Void> call_delete_story = storyServerConnection.delete_story(storyId);
        call_delete_story.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    storyDialogPresenter.onSuccessDeleteStory();
                } else {
                    storyDialogPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                log(t);
                storyDialogPresenter.onNetworkError(null);
            }
        });

    }

    @Override
    public ArrayList<String> getStoryDialogList() {
        int userId = user.getId();
        int storyUserId = story.getUser_id();

        ArrayList<String> storyDialogList = new ArrayList<>();
        storyDialogList.add("본문 복사");
        if (userId == storyUserId) {
            storyDialogList.add("수정");
            storyDialogList.add("삭제");
        } else {
            storyDialogList.add("신고하기");
        }
        storyDialogList.add("취소");

        return storyDialogList;
    }

    private static void log(Throwable throwable) {
        StackTraceElement[] ste = throwable.getStackTrace();
        String className = ste[0].getClassName();
        String methodName = ste[0].getMethodName();
        int lineNumber = ste[0].getLineNumber();
        String fileName = ste[0].getFileName();

        if (LogFlag.printFlag) {
            if (logger.isInfoEnabled()) {
                logger.info("Exception: " + throwable.getMessage());
                logger.info(className + "." + methodName + " " + fileName + " " + lineNumber + " " + "line");
            }
        }
    }
}
