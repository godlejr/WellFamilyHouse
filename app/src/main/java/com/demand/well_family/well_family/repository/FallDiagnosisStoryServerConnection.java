package com.demand.well_family.well_family.repository;

import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.dto.FallDiagnosisStoryInfo;
import com.demand.well_family.well_family.dto.PhysicalEvaluation;
import com.demand.well_family.well_family.dto.PhysicalEvaluationScore;

import java.util.HashMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by Dev-0 on 2017-05-26.
 */

public interface FallDiagnosisStoryServerConnection {

    @POST("./")
    Call<Integer> insertFallDiagnosisStory(@Body FallDiagnosisStory fallDiagnosisStory);

    @POST("{fall_diagnosis_story_id}/self_diagnosis")
    Call<ResponseBody> insertSelfDiagnosis(@Path("fall_diagnosis_story_id") int fall_diagnosis_story_id, @QueryMap HashMap<String, String> map);

    @POST("{fall_diagnosis_story_id}/physical_evaluation")
    Call<ResponseBody> insertPhysicalEvaluation(@Path("fall_diagnosis_story_id") int fall_diagnosis_story_id, @Body PhysicalEvaluation physicalEvaluation);

    @POST("{fall_diagnosis_story_id}/physical_evaluation_score")
    Call<ResponseBody> insertPhysicalEvaluationScore(@Path("fall_diagnosis_story_id") int fall_diagnosis_story_id, @Body PhysicalEvaluationScore physicalEvaluationScore);

    @POST("{fall_diagnosis_story_id}/environment_evaluation")
    Call<ResponseBody> insertEnvironmentEvaluation(@Path("fall_diagnosis_story_id") int fall_diagnosis_story_id, @QueryMap HashMap<String, String> map);

    @POST("{fall_diagnosis_story_id}/environment_photo")
    Call<ResponseBody> insertEnvironmentPhoto(@Path("fall_diagnosis_story_id") int fall_diagnosis_story_id, @Body RequestBody requestBody);





    @GET("{fall_diagnosis_story_id}/comment_count")
    Call<Integer> selectFallDiagnosisStoryCommentCount(@Path("fall_diagnosis_story_id") int fall_diagnosis_story_id);

    @GET("{fall_diagnosis_story_id}/like_count")
    Call<Integer> selectFallDiagnosisStoryLikeCount(@Path("fall_diagnosis_story_id") int fall_diagnosis_story_id);





    @POST("{fall_diagnosis_story_id}/likes")
    Call<ResponseBody> insertFallDiagnosisStoryLikeUp(@Path("fall_diagnosis_story_id") int fall_diagnosis_story_id, @QueryMap HashMap<String, String> map);

    @DELETE("{fall_diagnosis_story_id}/likes/{user_id}")
    Call<ResponseBody> deleteFallDiagnosisStoryLikeDown(@Path("fall_diagnosis_story_id") int fall_diagnosis_story_id, @Path("user_id") int user_id);

    @GET("{fall_diagnosis_story_id}/like_check/{user_id}")
    Call<Integer> selectFallDiagnosisStoryLikeCheck(@Path("fall_diagnosis_story_id") int fall_diagnosis_story_id, @Path("user_id") int user_id);





    @PUT("{fall_diagnosis_story_id}/hits")
    Call<ResponseBody> updateFallDiagnosisStoryHit(@Path("fall_diagnosis_story_id") int fall_diagnosis_story_id);

    @GET("{fall_diagnosis_story_id}/infos")    //
    Call<FallDiagnosisStoryInfo> selectFallDiagnosisStoryInfo(@Path("fall_diagnosis_story_id") int fall_diagnosis_story_id, @QueryMap HashMap<String, String> map);


}
