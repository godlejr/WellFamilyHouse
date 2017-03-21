package com.demand.well_family.well_family.connection;

import com.demand.well_family.well_family.dto.Comment;
import com.demand.well_family.well_family.dto.CommentInfo;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.dto.Story;
import com.demand.well_family.well_family.dto.StoryInfoForNotification;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by ㅇㅇ on 2017-03-13.
 */

public interface StoryServerConnection {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://ec2-52-78-186-215.ap-northeast-2.compute.amazonaws.com/stories/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    @GET("{story_id}/comment_count")
    Call<Integer> family_comment_Count(@Path("story_id") int story_id);

    @GET("{story_id}/like_count")
    Call<Integer> family_like_Count(@Path("story_id") int story_id);


    @GET("{story_id}/photos")
    Call<ArrayList<Photo>> family_content_photo_List(@Path("story_id") int story_id);

    @POST("{story_id}/photos")
    Call<ResponseBody> insert_photos(@Path("story_id") int story_id, @Body RequestBody requestBody);

    @POST("{story_id}/likes")
    Call<ResponseBody> family_content_like_up(@Path("story_id") int story_id, @QueryMap HashMap<String, String> map);

    @DELETE("{story_id}/likes/{user_id}")
    Call<ResponseBody> family_content_like_down(@Path("story_id") int story_id, @Path("user_id") int user_id);


    @GET("{story_id}/like_check/{user_id}")
    Call<Integer> family_content_like_check(@Path("story_id") int story_id, @Path("user_id") int user_id);


    @GET("{story_id}/comments")
    Call<ArrayList<CommentInfo>> family_detail_comment_List(@Path("story_id") int story_id);

    @POST("{story_id}/comments")
    Call<Comment> insert_comment(@Path("story_id") int story_id, @QueryMap HashMap<String, String> map);

    @GET("{story_id}")
    Call<StoryInfoForNotification> storyDetailForNotification(@Path("story_id") int story_id);

    @POST("./") // (*) user_id는 POST로 던지기
    Call<Story> insert_story(@QueryMap HashMap<String, String> map);


}
