package com.demand.well_family.well_family.dialog.list.songstory.interactor;

import com.demand.well_family.well_family.dto.Report;
import com.demand.well_family.well_family.dto.Song;
import com.demand.well_family.well_family.dto.SongStory;
import com.demand.well_family.well_family.dto.StoryInfo;
import com.demand.well_family.well_family.dto.User;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-20.
 */

public interface SongStoryDialogInteractor {
    void getFamilyCheck();

    void setSongStoryDeleted(int songStoryId);

    void getFamilyCheckForClick(int position);

    void setUser(User user);
    User getUser();

    void setSongStory(SongStory songStory);
    SongStory getSongStory();

    void setSong (Song song);
    Song getSong();

    void setReport (Report report);
    Report getReport();

    void setStoryUserIsMe(boolean storyUserIsMe);
    boolean getStoryUserIsMe();
}
