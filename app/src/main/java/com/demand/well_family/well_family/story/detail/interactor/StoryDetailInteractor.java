package com.demand.well_family.well_family.story.detail.interactor;

import com.demand.well_family.well_family.dto.StoryInfo;
import com.demand.well_family.well_family.dto.User;

/**
 * Created by Dev-0 on 2017-04-21.
 */

public interface StoryDetailInteractor {
    void setUser(User user);

    User getUser();

    void setStoryInfo(StoryInfo storyInfo);

    StoryInfo getStoryInfo();

    void setLikeChecked(boolean likeChecked);

    boolean getLikeChecked();

    void setStoryHit();

    void getStoryLikeCount();

    void setCommentAdded(String content);

    void getStoryCommentCount();

    boolean isFirstLikeChecked();

    void setFirstLikeChecked(boolean firstLikeChecked);

    void getPhotoData();

    void getCommentData();

    void setLikeCheck();

    void setLikeUncheck();
}
