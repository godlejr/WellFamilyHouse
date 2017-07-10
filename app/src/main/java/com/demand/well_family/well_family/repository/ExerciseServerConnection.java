package com.demand.well_family.well_family.repository;

import com.demand.well_family.well_family.dto.ExerciseCategory;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by ㅇㅇ on 2017-06-23.
 */

public interface ExerciseServerConnection {
    @GET("./")
    Call<ArrayList<ExerciseCategory>> selectExerciseCategoryList(); //@QueryMap HashMap<String, String> map



}
