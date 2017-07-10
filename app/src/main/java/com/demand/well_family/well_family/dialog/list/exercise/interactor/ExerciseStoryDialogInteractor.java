package com.demand.well_family.well_family.dialog.list.exercise.interactor;

import com.demand.well_family.well_family.dto.ExerciseStory;
import com.demand.well_family.well_family.dto.User;

/**
 * Created by ㅇㅇ on 2017-07-07.
 */

public interface ExerciseStoryDialogInteractor {
    ExerciseStory getExerciseStory();

    void setExerciseStory(ExerciseStory exerciseStory);

    User getUser();

    void setUser(User user);

    void setExerciseStoryDeleted();
}
