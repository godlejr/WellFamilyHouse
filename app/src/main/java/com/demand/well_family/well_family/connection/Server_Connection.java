package com.demand.well_family.well_family.connection;

import com.demand.well_family.well_family.dto.Check;
import com.demand.well_family.well_family.dto.Comment;
import com.demand.well_family.well_family.dto.CommentCount;
import com.demand.well_family.well_family.dto.CommentInfo;
import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.Identification;
import com.demand.well_family.well_family.dto.LikeCount;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.dto.Range;
import com.demand.well_family.well_family.dto.Song;
import com.demand.well_family.well_family.dto.SongCategory;
import com.demand.well_family.well_family.dto.SongComment;
import com.demand.well_family.well_family.dto.SongPhoto;
import com.demand.well_family.well_family.dto.SongStory;
import com.demand.well_family.well_family.dto.SongStoryAvatar;
import com.demand.well_family.well_family.dto.SongStoryComment;
import com.demand.well_family.well_family.dto.SongStoryEmotionData;
import com.demand.well_family.well_family.dto.SongStoryEmotionInfo;
import com.demand.well_family.well_family.dto.Story;
import com.demand.well_family.well_family.dto.StoryInfo;
import com.demand.well_family.well_family.dto.User;

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
import retrofit2.http.PUT;
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

    @PUT("{family_id}/update_family_avatar")
    Call<ResponseBody> update_family_avatar(@Path("family_id") int family_id, @Body RequestBody base64);

    @POST("{user_id}/insert_family")
    Call<ArrayList<Identification>> insert_family(@Path("user_id") int user_id, @QueryMap HashMap<String, String> map);

    @POST("{login_category_id}/join")
    Call<ResponseBody> join(@Path("login_category_id") int login_category_id,@QueryMap HashMap<String, String> map);

    @GET("{user_id}/family_Info")
    Call<ArrayList<Family>> family_Info(@Path("user_id") int user_id);

    @GET(" {family_id}/family")
    Call<ArrayList<Family>> family(@Path("family_id") int family_id);

    @POST("login")
    Call<ArrayList<User>> login(@QueryMap HashMap<String, String> map);

    @PUT("{user_id}/update_deviceId_token")
    Call<ResponseBody> update_deviceId_token(@Path("user_id") int user_id, @QueryMap HashMap<String,String> map);

    @POST("{user_id}/check_device_id")
    Call<ArrayList<Check>> check_device_id(@Path("user_id") int user_id, @QueryMap HashMap<String, String> map);

    @PUT("{user_id}/update_token")
    Call<ResponseBody> update_token(@Path("user_id") int user_id, @QueryMap HashMap<String,String> map);

    @GET("{family_id}/family_content_List")
    Call<ArrayList<StoryInfo>> family_content_List(@Path("family_id") int family_id);

    @POST("{story_id}/family_content_like_up")
    Call<ResponseBody> family_content_like_up(@Path("story_id") int story_id, @QueryMap HashMap<String, String> map);

    @DELETE("{story_id}/family_content_like_down")
    Call<ResponseBody> family_content_like_down(@Path("story_id") int story_id, @QueryMap HashMap<String, String> map);

    @POST("user_Info")
    Call<ArrayList<User>> user_Info(@QueryMap HashMap<String, String> map);

    @GET("{story_id}/family_content_photo_List")
    Call<ArrayList<Photo>> family_content_photo_List(@Path("story_id") int story_id);

    @GET("{story_id}/family_like_Count")
    Call<ArrayList<LikeCount>> family_like_Count(@Path("story_id") int story_id);

    @GET("{story_id}/family_comment_Count")
    Call<ArrayList<CommentCount>> family_comment_Count(@Path("story_id") int story_id);

    @POST("{story_id}/family_content_like_check")
    Call<ArrayList<Check>> family_content_like_check(@Path("story_id") int story_id, @QueryMap HashMap<String, String> map);

    @POST("{family_id}/family_user_Info")
    Call<ArrayList<User>> family_user_Info(@Path("family_id") int family_id, @QueryMap HashMap<String, String> map);

    @POST("{family_id}/family_photo_List")
    Call<ArrayList<Photo>> family_photo_List(@Path("family_id") int family_id);

    @POST("{story_id}/insert_comment")
    Call<ArrayList<Comment>> insert_comment(@Path("story_id") int story_id, @QueryMap HashMap<String, String> map);

    @GET("{story_id}/family_detail_comment_List")
    Call<ArrayList<CommentInfo>> family_detail_comment_List(@Path("story_id") int story_id);

    @POST("{user_id}/insert_story")
    Call<ArrayList<Story>> insert_story(@Path("user_id") int user_id,@QueryMap HashMap<String, String> map);

    @POST("{story_id}/insert_photos")
    Call<ResponseBody> insert_photos(@Path("story_id") int story_id,@Body RequestBody requestBody);

    @POST("{story_user_id}/family_check")
    Call<ArrayList<Check>> family_check(@Path("story_user_id") int story_user_id, @QueryMap HashMap<String, String> map);

    @GET("{story_user_id}/song_story_List_Me")
    Call<ArrayList<SongStory>> song_story_List_Me(@Path("story_user_id") int story_user_id);

    @GET("{story_user_id}/song_story_List_Family")
    Call<ArrayList<SongStory>> song_story_List_Family(@Path("story_user_id") int story_user_id);

    @GET("{story_user_id}/song_story_List_Public")
    Call<ArrayList<SongStory>> song_story_List_Public(@Path("story_user_id") int story_user_id);

    @POST("{song_story_id}/song_story_like_up")
    Call<ResponseBody> song_story_like_up(@Path("song_story_id") int song_story_id, @QueryMap HashMap<String, String> map);

    @DELETE("{song_story_id}/song_story_like_down")
    Call<ResponseBody> song_story_like_down(@Path("song_story_id") int song_story_id, @QueryMap HashMap<String, String> map);

    @GET("{story_user_id}/song_story_photo_List")
    Call<ArrayList<SongPhoto>> song_story_photo_List(@Path("story_user_id") int story_user_id);

    @GET("{song_story_id}/song_story_like_Count")
    Call<ArrayList<LikeCount>> song_story_like_Count(@Path("song_story_id") int song_story_id);

    @GET("{song_story_id}/song_story_comment_Count")
    Call<ArrayList<CommentCount>> song_story_comment_Count(@Path("song_story_id") int song_story_id);

    @POST("{song_story_id}/song_story_like_check")
    Call<ArrayList<Check>> song_story_like_check(@Path("song_story_id") int song_story_id, @QueryMap HashMap<String, String> map);

    @POST("{song_story_id}/song_story_avatar")
    Call<ArrayList<SongStoryAvatar>> song_story_avatar(@Path("song_story_id") int song_story_id, @QueryMap HashMap<String, String> map);

    @POST("{song_story_id}/insert_song_story_comment")
    Call<ArrayList<SongStoryComment>> insert_song_story_comment(@Path("song_story_id") int story_id, @QueryMap HashMap<String, String> map);

    @GET("{song_story_id}/song_story_comment_List")
    Call<ArrayList<CommentInfo>> song_story_comment_List(@Path("song_story_id") int song_story_id);

    @POST("{user_id}/find_user")
    Call<ArrayList<User>> find_user(@Path("user_id") int user_id, @QueryMap HashMap<String, String> map);

    @POST("{family_id}/insert_user_into_family")
    Call<ResponseBody> insert_user_into_family(@Path("family_id") int family_id, @QueryMap HashMap<String, String> map);

    @DELETE("{family_id}/delete_user_from_family")
    Call<ResponseBody> delete_user_from_family(@Path("family_id") int family_id, @QueryMap HashMap<String, String> map);


    //memory sound

    @PUT("{song_id}/Insert_Song_hit")
    Call<ResponseBody> Insert_Song_hit(@Path("song_id") int song_id);

    @POST("{song_id}/song_like_up")
    Call<ResponseBody> song_like_up(@Path("song_id") int song_id, @QueryMap HashMap<String, String> map);

    @DELETE("{song_id}/song_like_down")
    Call<ResponseBody> song_like_down(@Path("song_id") int song_id, @QueryMap HashMap<String, String> map);

    @POST("{song_id}/song_like_Count")
    Call<ArrayList<LikeCount>> song_like_Count(@Path("song_id") int song_id);

    @POST("{song_id}/song_like_check")
    Call<ArrayList<Check>> song_like_check(@Path("song_id") int song_id, @QueryMap HashMap<String, String> map);

    @POST("{song_id}/song_comment_Count")
    Call<ArrayList<CommentCount>> song_comment_count(@Path("song_id") int song_id);

    @POST("song_range_List")
    Call<ArrayList<Range>> song_range_List();

    @POST("{user_id}/insert_song_story")
    Call<ArrayList<SongStory>> insert_song_story(@Path("user_id") int user_id, @QueryMap HashMap<String, String> map);

    @POST("{song_story_id}/insert_song_photos")
    Call<ResponseBody> insert_song_photos(@Path("song_story_id") int song_story_id, @Body RequestBody requestBody);

    @PUT("{song_story_id}/insert_song_audio")
    Call<ResponseBody> insert_song_audio(@Path("song_story_id") int song_story_id, @Body RequestBody requestBody);

    @POST("{song_id}/insert_song_comment")
    Call<ArrayList<SongComment>> insert_song_comment(@Path("song_id") int song_id, @QueryMap HashMap<String, String> map);

    @POST("{song_id}/song_comment_List")
    Call<ArrayList<CommentInfo>> song_comment_List(@Path("song_id") int song_id);

    @POST("song_random")
    Call<ArrayList<Song>> song_random();

    @GET("{category_id}/song_list_by_Category")
    Call<ArrayList<Song>> song_list_by_Category(@Path("category_id") int category_id);

    @POST("{song_id}/song_comment_Count")
    Call<ArrayList<CommentCount>> song_comment_Count(@Path("song_id") int song_id);

    @POST("song_list_by_Hits")
    Call<ArrayList<Song>> song_list_by_Hits();

    @POST("song_category_List")
    Call<ArrayList<SongCategory>> song_category_List();

    @POST("email_check")
    Call<ArrayList<User>> email_check(@QueryMap HashMap<String, String> map);

    @GET("song_story_emotion_List")
    Call<ArrayList<SongStoryEmotionInfo>> song_story_emotion_List();

    @POST("{song_story_id}/insert_emotion_into_song_story")
    Call<ResponseBody> insert_emotion_into_song_story(@Path("song_story_id") int song_story_id, @QueryMap HashMap<String, String> map);

    @GET("{song_story_id}/song_story_emotion_data_List")
    Call<ArrayList<SongStoryEmotionData>> song_story_emotion_List(@Path("song_story_id") int song_story_id);

    @POST("{other_user_id}/family_user_check")
    Call<ArrayList<Check>> family_user_check(@Path("other_user_id") int other_user_id, @QueryMap HashMap<String, String> map);


}
