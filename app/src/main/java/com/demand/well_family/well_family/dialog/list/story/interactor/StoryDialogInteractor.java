package com.demand.well_family.well_family.dialog.list.story.interactor;

import com.demand.well_family.well_family.dto.Report;
import com.demand.well_family.well_family.dto.Story;
import com.demand.well_family.well_family.dto.User;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-20.
 */

public interface StoryDialogInteractor {
    ArrayList<String> getStoryDialogList();

    void setStoryDeleted(int storyId);

    User getUser();
    void setUser(User user);

    Story getStory();
    void setStory(Story story);

    void setReport (Report report);
    Report getReport();
}
