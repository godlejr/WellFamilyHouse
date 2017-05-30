package com.demand.well_family.well_family.repository;

import com.demand.well_family.well_family.dto.EnvironmentEvaluationCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;
import com.demand.well_family.well_family.dto.PhysicalEvaluationCategory;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by ㅇㅇ on 2017-05-24.
 */

public interface FallDiagnosisServerConnection {
    @GET("categories")
    Call<ArrayList<FallDiagnosisCategory>> getCategoryList();

    @GET("categories/{fall_diagnosis_category_id}")
    Call<ArrayList<FallDiagnosisContentCategory>> getDiagnosisCategories(@Path("fall_diagnosis_category_id") int fall_diagnosis_category_id);

    @GET("categories/{fall_diagnosis_category_id}/physicalEvaluationCategories")
    Call<ArrayList<PhysicalEvaluationCategory>> getPhysicalEvaluationCategories(@Path("fall_diagnosis_category_id") int fall_diagnosis_category_id);

    @GET("categories/{fall_diagnosis_category_id}/environmentEvaluationCategories/{fall_diagnosis_content_category_id}")
    Call<ArrayList<EnvironmentEvaluationCategory>> getEnvironmentEvaluationCategories(@Path("fall_diagnosis_category_id") int fall_diagnosis_category_id,
                                                                                      @Path("fall_diagnosis_content_category_id") int fall_diagnosis_content_category_id);
}
