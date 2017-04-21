package com.demand.well_family.well_family.dialog.list.story.presenter;

import com.demand.well_family.well_family.dialog.list.story.adpater.StoryDialogAdapter;
import com.demand.well_family.well_family.dto.Report;
import com.demand.well_family.well_family.dto.Story;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.util.APIErrorUtil;

/**
 * Created by ㅇㅇ on 2017-04-20.
 */

public interface StoryDialogPresenter {
    void onCreate(Story story, Report report);

    void setStoryDialogAdapterInit();

    void setStoryDialogAdapter(StoryDialogAdapter storyDialogAdapter);

    void setStoryContentCopied();

    void setStoryDeleted();

    void setStoryDialogAction(int dialogPosition);

    void onActivityResultForModifyResultOk(String editedStoryContent);

    void onClickStoryDialog(int dialogPosition);

    void onNetworkError(APIErrorUtil apiErrorUtil);

    void onSuccessDeleteStory();

    void onBackPressed();
}
