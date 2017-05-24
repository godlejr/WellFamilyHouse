package com.demand.well_family.well_family.repository;

import com.demand.well_family.well_family.dto.Category;
import com.demand.well_family.well_family.dto.SelfDiagnosisCategory;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by ㅇㅇ on 2017-05-24.
 */

public interface FallDiagnosisServerConnection {
    @GET("categories")
    Call<ArrayList<Category>> getCategoryList();

    @GET("categories/{fall_diagnosis_category_id}")
    Call<ArrayList<SelfDiagnosisCategory>> getDiagnosisCategories(@Path("fall_diagnosis_category_id") int fall_diagnosis_category_id);
}
