package com.demand.well_family.well_family.connection;


import com.demand.well_family.well_family.dto.CommentInfo;
import com.demand.well_family.well_family.dto.SongPhoto;
import com.demand.well_family.well_family.dto.SongStoryComment;
import com.demand.well_family.well_family.dto.SongStoryEmotionData;
import com.demand.well_family.well_family.dto.SongStoryInfoForNotification;

import java.util.ArrayList;
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
 * Created by ㅇㅇ on 2017-03-13.
 */

public interface SongStoryServerConnection {


    @GET("{song_story_id}")
    Call<SongStoryInfoForNotification> storyDetailForNotification(@Path("song_story_id") int song_story_id);

    @POST("{song_story_id}/photos")
    Call<ResponseBody> insert_song_photos(@Path("song_story_id") int song_story_id, @Body RequestBody requestBody);

    @PUT("{song_story_id}/audios")
    Call<ResponseBody> insert_song_audio(@Path("song_story_id") int song_story_id, @Body RequestBody requestBody);

    @GET("{song_story_id}/comment_count")
    Call<Integer> song_story_comment_Count(@Path("song_story_id") int song_story_id);

    @GET("{song_story_id}/like_count")
    Call<Integer> song_story_like_Count(@Path("song_story_id") int song_story_id);

    @POST("{song_story_id}/likes")
    Call<ResponseBody> song_story_like_up(@Path("song_story_id") int song_story_id, @QueryMap HashMap<String, String> map);

    @DELETE("{song_story_id}/likes/{user_id}")
    Call<ResponseBody> song_story_like_down(@Path("song_story_id") int song_story_id, @Path("user_id") int user_id);

    @GET("{song_story_id}/like_check/{user_id}")
    Call<Integer> song_story_like_check(@Path("song_story_id") int song_story_id, @Path("user_id") int user_id);

    @GET("{song_story_id}/photos")
    Call<ArrayList<SongPhoto>> song_story_photo_List(@Path("song_story_id") int song_story_id);

    @GET("{song_story_id}/comments")
    Call<ArrayList<CommentInfo>> song_story_comment_List(@Path("song_story_id") int song_story_id);

    @POST("{song_story_id}/comments")
    Call<SongStoryComment> insert_song_story_comment(@Path("song_story_id") int story_id, @QueryMap HashMap<String, String> map);

    @POST("{song_story_id}/emotions")
    Call<ResponseBody> insert_emotion_into_song_story(@Path("song_story_id") int song_story_id, @QueryMap HashMap<String, String> map);

    @GET("{song_story_id}/emotions")
    Call<ArrayList<SongStoryEmotionData>> song_story_emotion_List(@Path("song_story_id") int song_story_id);

    @PUT("{song_story_id}")
    Call<Void> update_story(@Path("song_story_id") int song_story_id, @QueryMap HashMap<String, String> map);

    @DELETE("{song_story_id}")
    Call<Void> delete_story(@Path("song_story_id") int song_story_id);

    @PUT("{song_story_id}/hits")
    Call<Void> Insert_song_story_hit(@Path("song_story_id") int song_story_id);
}
