package com.demand.well_family.well_family.connection;


import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.FavoriteCategory;
import com.demand.well_family.well_family.dto.Notification;
import com.demand.well_family.well_family.dto.SongStory;
import com.demand.well_family.well_family.dto.User;

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
 * Created by ㅇㅇ on 2017-03-10.
 */

public interface UserServerConnection {

    @GET("{user_id}")
    Call<User> user_Info(@Path("user_id") int user_id);

    @PUT("{user_id}/tokens/{token}/deviceids/{device_id}")
    Call<ResponseBody> update_deviceId_token(@Path("user_id") int user_id, @Path("token") String token, @Path("device_id") String device_id);

    @GET("{user_id}/deviceids/{device_id}")
    Call<Integer> check_device_id(@Path("user_id") int user_id, @Path("device_id") String device_id);

    @PUT("{user_id}/tokens/{token}")
    Call<ResponseBody> update_token(@Path("user_id") int user_id, @Path("token") String token);


    @GET("{story_user_id}/family_check/{user_id}")
    Call<Integer> family_check(@Path("story_user_id") int story_user_id, @Path("user_id") int user_id);


    @GET("{story_user_id}/public_songstories")
    Call<ArrayList<SongStory>> song_story_List_Public(@Path("story_user_id") int story_user_id);

    @GET("{story_user_id}/family_songstories")
    Call<ArrayList<SongStory>> song_story_List_Family(@Path("story_user_id") int story_user_id);

    @GET("{story_user_id}/me_songstories")
    Call<ArrayList<SongStory>> song_story_List_Me(@Path("story_user_id") int story_user_id);


    @POST("{user_id}/families")
    Call<Integer> insert_family(@Path("user_id") int user_id, @QueryMap HashMap<String, String> map);

    @GET("{user_id}/families")
    Call<ArrayList<Family>> family_Info(@Path("user_id") int user_id);


    @GET("{other_user_id}/sole_family_check/{user_id}")
    Call<Integer> family_user_check(@Path("other_user_id") int other_user_id, @Path("user_id") int user_id, @QueryMap HashMap<String, String> map);

    @GET("favorite_categories")
    Call<ArrayList<FavoriteCategory>> favorite_category_List();


    @GET("{user_id}/check_genders")
    Call<Integer> check_gender(@Path("user_id") int user_id);

    @GET("{user_id}/favorite_check/{favorite_category_id}")
    Call<Integer> check_favorite(@Path("user_id") int user_id, @Path("favorite_category_id") int favorite_category_id);

    @GET("{user_id}/song_favorite_check/{song_category_id}")
    Call<Integer> check_song_category(@Path("user_id") int user_id, @Path("song_category_id") int song_category_id);

    @PUT("{user_id}")
    Call<ResponseBody> update_user_info(@Path("user_id") int user_id, @QueryMap HashMap<String, String> map);

    @PUT("{user_id}/avatars")
    Call<ResponseBody> update_user_avatar(@Path("user_id") int user_id, @Body RequestBody requestBody);

    @DELETE("{user_id}/favorites")
    Call<ResponseBody> delete_favorite(@Path("user_id") int user_id);

    @DELETE("{user_id}/song_categories")
    Call<ResponseBody> delete_song_category(@Path("user_id") int user_id);


    @POST("{user_id}/favorites")
    Call<ResponseBody> insert_favorite(@Path("user_id") int user_id, @QueryMap HashMap<String, String> map);

    @POST("{user_id}/song_categories")
    Call<ResponseBody> insert_song_category(@Path("user_id") int user_id, @QueryMap HashMap<String, String> map);

    @POST("{user_id}/comment_reports")
    Call<ResponseBody> insert_comment_report(@Path("user_id") int user_id, @QueryMap HashMap<String, String> map);

    @GET("{user_id}/notifications")
    Call<ArrayList<Notification>> notifications(@Path("user_id") int user_id);


}
