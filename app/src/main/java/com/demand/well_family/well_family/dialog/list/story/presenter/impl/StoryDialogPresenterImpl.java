package com.demand.well_family.well_family.dialog.list.story.presenter.impl;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.demand.well_family.well_family.dialog.list.story.adpater.StoryDialogAdapter;
import com.demand.well_family.well_family.dialog.list.story.flag.StoryDialogFlag;
import com.demand.well_family.well_family.dialog.list.story.interactor.StoryDialogInteractor;
import com.demand.well_family.well_family.dialog.list.story.interactor.impl.StoryDialogInteractorImpl;
import com.demand.well_family.well_family.dialog.list.story.presenter.StoryDialogPresenter;
import com.demand.well_family.well_family.dialog.list.story.view.StoryDialogView;
import com.demand.well_family.well_family.dto.Report;
import com.demand.well_family.well_family.dto.Story;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.PreferenceUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-20.
 */

public class StoryDialogPresenterImpl implements StoryDialogPresenter {
    private StoryDialogInteractor storyDialogInteractor;
    private StoryDialogView storyDialogView;
    private PreferenceUtil preferenceUtil;
    private Context context;


    public StoryDialogPresenterImpl(Context context) {
        this.storyDialogView = (StoryDialogView) context;
        this.storyDialogInteractor = new StoryDialogInteractorImpl(this);
        this.preferenceUtil = new PreferenceUtil(context);
        this.context = context;
    }

    @Override
    public void onCreate(Story story, Report report) {
        storyDialogView.init();

        User user = preferenceUtil.getUserInfo();
        storyDialogInteractor.setUser(user);
        storyDialogInteractor.setStory(story);
        storyDialogInteractor.setReport(report);
    }

    @Override
    public void setStoryDialogAdapterInit() {
        ArrayList<String> storyDialogList = storyDialogInteractor.getStoryDialogList();
        storyDialogView.setStoryDialogAdapterInit(storyDialogList);
    }

    @Override
    public void setStoryContentCopied() {
        Story story = storyDialogInteractor.getStory();
        String storyContent = story.getContent();

        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("label", storyContent);
        clipboardManager.setPrimaryClip(clipData);

        storyDialogView.showMessage("클립보드에 복사되었습니다.");
        storyDialogView.navigateToBack();
    }

    @Override
    public void setStoryDeleted() {
        Story story = storyDialogInteractor.getStory();
        int storyId = story.getId();
        storyDialogInteractor.setStoryDeleted(storyId);
    }

    @Override
    public void setStoryDialogAdapter(StoryDialogAdapter storyDialogAdapter) {
        storyDialogView.setStoryDialogAdapter(storyDialogAdapter);
    }

    @Override
    public void onActivityResultForModifyResultOk(String editedStoryContent) {
        Story story = storyDialogInteractor.getStory();
        story.setContent(editedStoryContent);
        storyDialogView.navigateToBackForResultOk(story);
    }

    @Override
    public void onClickStoryDialog(int dialogPosition) {
        if (dialogPosition == StoryDialogFlag.COPY) {
            storyDialogView.setStoryContentCopied();
        }

        storyDialogView.setStoryDialogAction(dialogPosition);
    }

    @Override
    public void setStoryDialogAction(int dialogPosition) {
        Report report = storyDialogInteractor.getReport();
        Story story = storyDialogInteractor.getStory();
        User user = preferenceUtil.getUserInfo();
        int userId = user.getId();
        int storyUserId = story.getUser_id();

        if (userId == storyUserId) {
            if (dialogPosition == StoryDialogFlag.EDIT) {
                storyDialogView.navigateModifyStoryActivity(story);

            } else if (dialogPosition == StoryDialogFlag.DELETE) {
                storyDialogView.setStoryDeleted();
            } else {
                storyDialogView.navigateToBack();
            }
        } else {
            if (dialogPosition == StoryDialogFlag.REPORT) {
                storyDialogView.navigateToReportActivity(report);
            } else {
                storyDialogView.navigateToBack();
            }
        }
    }

    @Override
    public void onSuccessDeleteStory() {
        Story story  = storyDialogInteractor.getStory();
        storyDialogView.showMessage("게시물이 삭제되었습니다.");
        storyDialogView.navigateToBackAfterDelete(story);
    }

    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            storyDialogView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            storyDialogView.showMessage(apiErrorUtil.message());
        }
    }

    @Override
    public void onBackPressed() {
        Story story  = storyDialogInteractor.getStory();
        storyDialogView.navigateToBackForResultOk(story);
    }
}
