package com.demand.well_family.well_family.connection;

import com.demand.well_family.well_family.dto.Check;
import com.demand.well_family.well_family.dto.Comment;
import com.demand.well_family.well_family.dto.CommentCount;
import com.demand.well_family.well_family.dto.CommentInfo;
import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.Identification;
import com.demand.well_family.well_family.dto.LikeCount;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.dto.SongPhoto;
import com.demand.well_family.well_family.dto.SongStory;
import com.demand.well_family.well_family.dto.SongStoryAvatar;
import com.demand.well_family.well_family.dto.SongStoryComment;
import com.demand.well_family.well_family.dto.Story;
import com.demand.well_family.well_family.dto.StoryInfo;
import com.demand.well_family.well_family.dto.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by Dev-0 on 2017-02-10.
 */

public interface Server_Connection {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://ec2-52-78-186-215.ap-northeast-2.compute.amazonaws.com/family/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @POST("{family_id}/update_family_avatar")
    Call<ResponseBody> update_family_avatar(@Path("family_id") String family_id, @Body RequestBody base64);

    @POST("{user_id}/insert_family")
    Call<ArrayList<Identification>> insert_family(@Path("user_id") String user_id, @QueryMap Map<String, String> map);

    @POST("join")
    Call<ResponseBody> join(@QueryMap Map<String, String> map);

    @GET("{user_id}/family_Info")
    Call<ArrayList<Family>> family_Info(@Path("user_id") String user_id);

    @GET(" {family_id}/family")
    Call<ArrayList<Family>> family(@Path("family_id") String family_id);

    @POST("login")
    Call<ArrayList<User>> login(@QueryMap Map<String, String> map);

    @GET("{family_id}/family_content_List")
    Call<ArrayList<StoryInfo>> family_content_List(@Path("family_id") String family_id);

    @POST("{story_id}/family_content_like_up")
    Call<ResponseBody> family_content_like_up(@Path("story_id") String story_id, @QueryMap HashMap<String, String> map);

    @POST("{story_id}/family_content_like_down")
    Call<ResponseBody> family_content_like_down(@Path("story_id") String story_id, @QueryMap HashMap<String, String> map);

    @POST("user_Info")
    Call<ArrayList<User>> user_Info(@QueryMap HashMap<String, String> map);

    @GET("{story_id}/family_content_photo_List")
    Call<ArrayList<Photo>> family_content_photo_List(@Path("story_id") String story_id);

    @GET("{story_id}/family_like_Count")
    Call<ArrayList<LikeCount>> family_like_Count(@Path("story_id") String story_id);

    @GET("{story_id}/family_comment_Count")
    Call<ArrayList<CommentCount>> family_comment_Count(@Path("story_id") String story_id);

    @POST("{story_id}/family_content_like_check")
    Call<ArrayList<Check>> family_content_like_check(@Path("story_id") String story_id, @QueryMap HashMap<String, String> map);

    @POST("{family_id}/family_user_Info")
    Call<ArrayList<User>> family_user_Info(@Path("family_id") String family_id, @QueryMap HashMap<String, String> map);

    @POST("{family_id}/family_photo_List")
    Call<ArrayList<Photo>> family_photo_List(@Path("family_id") String family_id);

    @POST("{story_id}/insert_comment")
    Call<ArrayList<Comment>> insert_comment(@Path("story_id") String story_id, @QueryMap HashMap<String, String> map);

    @GET("{story_id}/family_detail_comment_List")
    Call<ArrayList<CommentInfo>> family_detail_comment_List(@Path("story_id") String story_id);

    @POST("{user_id}/insert_story")
    Call<ArrayList<Story>> insert_story(@Path("user_id") String user_id,@QueryMap HashMap<String, String> map);

    @POST("{story_id}/insert_photos")
    Call<ResponseBody> insert_photos(@Path("story_id") String story_id,@Body RequestBody requestBody);

    @POST("{story_user_id}/family_check")
    Call<ArrayList<Check>> family_check(@Path("story_user_id") String story_user_id, @QueryMap HashMap<String, String> map);

    @GET("{story_user_id}/song_story_List_Me")
    Call<ArrayList<SongStory>> song_story_List_Me(@Path("story_user_id") String story_user_id);

    @GET("{story_user_id}/song_story_List_Family")
    Call<ArrayList<SongStory>> song_story_List_Family(@Path("story_user_id") String story_user_id);

    @GET("{story_user_id}/song_story_List_Public")
    Call<ArrayList<SongStory>> song_story_List_Public(@Path("story_user_id") String story_user_id);

    @POST("{song_story_id}/song_story_like_up")
    Call<ResponseBody> song_story_like_up(@Path("song_story_id") String song_story_id, @QueryMap HashMap<String, String> map);

    @POST("{song_story_id}/song_story_like_down")
    Call<ResponseBody> song_story_like_down(@Path("song_story_id") String song_story_id, @QueryMap HashMap<String, String> map);

    @GET("{story_user_id}/song_story_photo_List")
    Call<ArrayList<SongPhoto>> song_story_photo_List(@Path("story_user_id") String story_user_id);

    @GET("{song_story_id}/song_story_like_Count")
    Call<ArrayList<LikeCount>> song_story_like_Count(@Path("song_story_id") String song_story_id);

    @GET("{song_story_id}/song_story_comment_Count")
    Call<ArrayList<CommentCount>> song_story_comment_Count(@Path("song_story_id") String song_story_id);

    @POST("{song_story_id}/song_story_like_check")
    Call<ArrayList<Check>> song_story_like_check(@Path("song_story_id") String song_story_id, @QueryMap HashMap<String, String> map);

    @POST("{song_story_id}/song_story_avatar")
    Call<ArrayList<SongStoryAvatar>> song_story_avatar(@Path("song_story_id") String song_story_id, @QueryMap HashMap<String, String> map);

    @POST("{song_story_id}/insert_song_story_comment")
    Call<ArrayList<SongStoryComment>> insert_song_story_comment(@Path("song_story_id") String story_id, @QueryMap HashMap<String, String> map);

    @GET("{song_story_id}/song_story_comment_List")
    Call<ArrayList<CommentInfo>> song_story_comment_List(@Path("song_story_id") String song_story_id);



}
