package com.demand.well_family.well_family.dialog.list.story.view;

import com.demand.well_family.well_family.dialog.list.story.adpater.StoryDialogAdapter;
import com.demand.well_family.well_family.dto.Report;
import com.demand.well_family.well_family.dto.Story;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-20.
 */

public interface StoryDialogView {
    void init();

    void setStoryDialogAdapter(StoryDialogAdapter storyDialogAdapter);

    void setStoryDialogAdapterInit(ArrayList<String> storyDialogList);

    void setStoryContentCopied();

    void setStoryDeleted();

    void setStoryDialogAction(int dialogPosition);

    void navigateToBack();

    void navigateToBackForResultOk(Story story);

    void navigateToBackForResultCanceled(Story story);

    void navigateToBackAfterDelete(Story story);

    void navigateModifyStoryActivity(Story story);

    void navigateToReportActivity(Report report);

    void showMessage(String message);

}
