package com.demand.well_family.well_family.exercisestory.detail.interactor;

import com.demand.well_family.well_family.dto.CommentInfo;
import com.demand.well_family.well_family.dto.ExerciseStory;
import com.demand.well_family.well_family.dto.User;

/**
 * Created by ㅇㅇ on 2017-06-27.
 */

public interface ExerciseStoryDetailInteractor {

    User getUser();

    void setUser(User user);

    ExerciseStory getExerciseStory();

    void setExerciseStory(ExerciseStory exerciseStory);

    void getLikeCount();

    void getCommentCount();

    void getExerciseStoryCommentList();

    void setExerciseStoryComment(CommentInfo commentInfo);

    void setContentLikeUp();

    void setContentLikeDown();

    void getContentLikeCheck();
}
