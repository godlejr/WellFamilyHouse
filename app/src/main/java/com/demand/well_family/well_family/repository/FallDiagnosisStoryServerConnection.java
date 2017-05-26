package com.demand.well_family.well_family.repository;

import com.demand.well_family.well_family.dto.FallDiagnosisStory;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by Dev-0 on 2017-05-26.
 */

public interface FallDiagnosisStoryServerConnection {

    @POST("./")
    Call<Integer> insertFallDiagnosisStory(@Body FallDiagnosisStory fallDiagnosisStory);

    @POST("{fall_diagnosis_story_id}")
    Call<ResponseBody> insertSelfDiagnosis(@Path("fall_diagnosis_story_id") int fall_diagnosis_story_id, @QueryMap HashMap<String, String> map);
}
