package com.demand.well_family.well_family.family.base.interactor;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.StoryInfo;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.family.base.adapter.content.ContentAdapter;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-04-18.
 */

public interface FamilyInteractor {

    void setNotificationFlag(boolean notificationFlag);

    boolean getNotificationFlag();

    void setUser(User user);

    void setFamily(Family family);

    User getUser();

    Family getFamily();

    void setThreadContentAdd(StoryInfo storyInfo);

    void setContentLikeCheck(ContentAdapter.ContentViewHolder holder, StoryInfo storyInfo);

    void setContentLikeUncheck(ContentAdapter.ContentViewHolder holder, StoryInfo storyInfo);


    void getUserData();

    void getContentData();

    void getContentDataAdded();

    void getContentUser(int contentUserId);

    ArrayList<StoryInfo> getStoryListWithOffset();

    void getContentPhotoData(ContentAdapter.ContentViewHolder holder, StoryInfo storyInfo);

    void getContentLikeCount(ContentAdapter.ContentViewHolder holder, StoryInfo storyInfo);

    void getContentCommentCount(ContentAdapter.ContentViewHolder holder, StoryInfo storyInfo);

    void getContentLikeCheck(ContentAdapter.ContentViewHolder holder, StoryInfo storyInfo);

    void getFamilyDeleteCheck();

    String getUserBirth(String birth);
}
