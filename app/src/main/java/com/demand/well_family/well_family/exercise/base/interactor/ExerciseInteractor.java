package com.demand.well_family.well_family.exercise.base.interactor;

import com.demand.well_family.well_family.dto.User;

/**
 * Created by ㅇㅇ on 2017-06-23.
 */

public interface ExerciseInteractor {
    User getUser();

    void setUser(User user);

    void getExerciseCategoryList();
}
