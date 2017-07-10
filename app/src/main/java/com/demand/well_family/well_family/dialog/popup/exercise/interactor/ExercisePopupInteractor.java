package com.demand.well_family.well_family.dialog.popup.exercise.interactor;

import com.demand.well_family.well_family.dto.ExerciseCategory;
import com.demand.well_family.well_family.dto.ExerciseStory;
import com.demand.well_family.well_family.dto.User;

/**
 * Created by ㅇㅇ on 2017-06-26.
 */

public interface ExercisePopupInteractor {
    ExerciseCategory getExerciseCategory();

    void setExerciseCategory(ExerciseCategory exerciseCategory);

    User getUser();

    void setUser(User user);

    void setExerciseStoryAdded(ExerciseStory exerciseStory);
}
