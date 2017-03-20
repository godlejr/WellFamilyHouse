package com.demand.well_family.well_family.connection;


import com.demand.well_family.well_family.dto.CommentInfo;
import com.demand.well_family.well_family.dto.Range;
import com.demand.well_family.well_family.dto.Song;
import com.demand.well_family.well_family.dto.SongCategory;
import com.demand.well_family.well_family.dto.SongComment;
import com.demand.well_family.well_family.dto.SongStory;
import com.demand.well_family.well_family.dto.SongStoryEmotionInfo;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by ㅇㅇ on 2017-03-13.
 */

public interface SongServerConnection {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://ec2-52-78-186-215.ap-northeast-2.compute.amazonaws.com/songs/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("categories")
    Call<ArrayList<SongCategory>> song_category_List();

    @GET("{song_id}/comment_count")
    Call<Integer> song_comment_Count(@Path("song_id") int song_id);

    @GET("{song_id}/like_count")
    Call<Integer> song_like_Count(@Path("song_id") int song_id);

    @GET("charts")
    Call<ArrayList<Song>> song_list_by_Hits();

    @GET("randoms")
    Call<Song> song_random();

    @GET("categories/{category_id}")
    Call<ArrayList<Song>> song_list_by_Category(@Path("category_id") int category_id);

    @PUT("{song_id}/hits")
    Call<ResponseBody> Insert_Song_hit(@Path("song_id") int song_id);

    @GET("{song_id}/comments")
    Call<ArrayList<CommentInfo>> song_comment_List(@Path("song_id") int song_id);

    @POST("{song_id}/likes")
    Call<ResponseBody> song_like_up(@Path("song_id") int song_id, @QueryMap HashMap<String, String> map);

    @DELETE("{song_id}/likes/{user_id}")
    Call<ResponseBody> song_like_down(@Path("song_id") int song_id, @Path("user_id") int user_id);

    @GET("{song_id}/like_check/{user_id}")
    Call<Integer> song_like_check(@Path("song_id") int song_id, @Path("user_id") int user_id);

    @POST("{song_id}/comments")  // (*) user_id는 POST로 던지기
    Call<SongComment> insert_song_comment(@Path("song_id") int song_id, @QueryMap HashMap<String, String> map);

    @GET("ranges")
    Call<ArrayList<Range>> song_range_List();

    @POST("stories") // (*) user_id는 POST로 던지기
    Call<SongStory> insert_song_story(@QueryMap HashMap<String, String> map);

    @GET("emotions")
    Call<ArrayList<SongStoryEmotionInfo>> song_story_emotion_List();



    @GET("{song_id}/avatars")
    Call<String> song_story_avatar(@Path("song_id") int song_id);
}
