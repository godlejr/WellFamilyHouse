package com.demand.well_family.well_family.exercisestory.base.interactor;

import com.demand.well_family.well_family.dto.ExerciseStory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.exercisestory.base.adapter.ExerciseStoryAdapter;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-06-27.
 */

public interface ExerciseStoryInteractor {
    User getUser();

    void setUser(User user);

    User getStoryUser();

    void setStoryUser(User storyUser);

    void getExerciseStoryData(int storyUserId);

    ArrayList<ExerciseStory> getExerciseStoryListWithOffset();

    void getStoryDataAdded();

    void getLikeCount(ExerciseStoryAdapter.ExerciseStoryViewHolder holder, int exerciseStoryId);

    void getCommentCount(ExerciseStoryAdapter.ExerciseStoryViewHolder holder, int exerciseStoryId);

    void setExerciseStoryHit(int exerciseStoryId);

    void getContentLikeCheck(ExerciseStoryAdapter.ExerciseStoryViewHolder holder, ExerciseStory exerciseStory);

    void setContentLikeUp(ExerciseStoryAdapter.ExerciseStoryViewHolder holder, ExerciseStory exerciseStory);

    void setContentLikeDown(ExerciseStoryAdapter.ExerciseStoryViewHolder holder, ExerciseStory exerciseStory);
}
