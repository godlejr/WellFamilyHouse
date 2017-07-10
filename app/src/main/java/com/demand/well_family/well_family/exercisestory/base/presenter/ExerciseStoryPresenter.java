package com.demand.well_family.well_family.exercisestory.base.presenter;

import com.demand.well_family.well_family.dto.ExerciseStory;
import com.demand.well_family.well_family.exercisestory.base.adapter.ExerciseStoryAdapter;
import com.demand.well_family.well_family.util.APIErrorUtil;

/**
 * Created by ㅇㅇ on 2017-06-27.
 */

public interface ExerciseStoryPresenter {
    void onCreate(int storyUserId);

    void onSuccessGetCommentCount(ExerciseStoryAdapter.ExerciseStoryViewHolder holder, int count);

    void onSuccessGetLikeCount(ExerciseStoryAdapter.ExerciseStoryViewHolder holder, int count);

    void onSuccessGetContentLikeCheck(ExerciseStoryAdapter.ExerciseStoryViewHolder holder, int isChecked, int position);

    void onNetworkError(APIErrorUtil apiErrorUtil);

    void onLoadData();

    void onLoadContent(ExerciseStoryAdapter.ExerciseStoryViewHolder holder, ExerciseStory exerciseStory);

    void onSuccessGetExerciseStoryData();

    void onGettingStoryDataAdded();

    void onSuccessGetStoryDataAdded(int i);

    void onSuccessThreadRun();

    void onScrollChange(int difference);

    void onClickContentBody(ExerciseStory exerciseStory);

    void onCheckedChangeForLike(ExerciseStoryAdapter.ExerciseStoryViewHolder holder, ExerciseStory exerciseStory, boolean isChecked);

    void onSuccessSetContentLikeDown(ExerciseStoryAdapter.ExerciseStoryViewHolder holder, int position);

    void onSuccessSetContentLikeUp(ExerciseStoryAdapter.ExerciseStoryViewHolder holder, int position);
}
