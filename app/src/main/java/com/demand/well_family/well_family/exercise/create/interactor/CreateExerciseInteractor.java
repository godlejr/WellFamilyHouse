package com.demand.well_family.well_family.exercise.create.interactor;

import com.demand.well_family.well_family.dto.ExerciseCategory;
import com.demand.well_family.well_family.dto.User;

/**
 * Created by ㅇㅇ on 2017-06-26.
 */

public interface CreateExerciseInteractor {
    boolean isPlaying();

    void setPlaying(boolean playing);

    void setSeekBarStart();


    User getUser();

    void setUser(User user);

    ExerciseCategory getExerciseCategory();

    void setExerciseCategory(ExerciseCategory exerciseCategory);

    void log(Throwable throwable);
}
