package com.demand.well_family.well_family.repository;

import com.demand.well_family.well_family.dto.CommentInfo;
import com.demand.well_family.well_family.dto.ExerciseStory;
import com.demand.well_family.well_family.dto.ExerciseStoryComment;

import java.util.ArrayList;
import java.util.HashMap;

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
 * Created by ㅇㅇ on 2017-06-23.
 */

public interface ExerciseStoryServerConnection {
    @POST("./")
    Call<Integer> insertExerciseStory(@Body ExerciseStory exerciseStory);

    @GET("{exercise_story_id}")
    Call<ExerciseStory> selectExerciseStory(@Path("exercise_story_id") int exercise_story_id);

    @GET("{exercise_story_id}/comment_count")
    Call<Integer> selectExerciseStoryCommentCount(@Path("exercise_story_id") int exercise_story_id);

    @GET("{exercise_story_id}/like_count")
    Call<Integer> selectExerciseStoryLikeCount(@Path("exercise_story_id") int exercise_story_id);


    @POST("{exercise_story_id}/likes")
        // map : user_id
    Call<ResponseBody> insertExerciseStoryLikeUp(@Path("exercise_story_id") int exercise_story_id, @QueryMap HashMap<String, String> map);

    @DELETE("{exercise_story_id}/likes/{user_id}")
    Call<ResponseBody> deleteExerciseStoryLikeDown(@Path("user_id") int user_id, @Path("exercise_story_id") int exercise_story_id);

    @GET("{exercise_story_id}/like_check/{user_id}")
    Call<Integer> selectExerciseStoryLikeCheck(@Path("user_id") int user_id, @Path("exercise_story_id") int exercise_story_id);

    @PUT("{exercise_story_id}/hits")
    Call<ResponseBody> updateExerciseStoryHit(@Path("exercise_story_id") int exercise_story_id);


    @POST("{exercise_story_id}/comments")
// map: user_id, content
    Call<ExerciseStoryComment> insertExerciseStoryComment(@Path("exercise_story_id") int exercise_story_id, @QueryMap HashMap<String, String> map);

    @DELETE("{exercise_story_id}")
    Call<ResponseBody> deleteExerciseStory(@Path("exercise_story_id") int exercise_story_id);

    @GET("{exercise_story_id}/comments")
    Call<ArrayList<CommentInfo>> selectExerciseStoryCommentList(@Path("exercise_story_id") int exercise_story_id);
}




