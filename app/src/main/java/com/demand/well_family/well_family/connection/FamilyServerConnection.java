package com.demand.well_family.well_family.connection;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.Photo;
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
 * Created by ㅇㅇ on 2017-03-10.
 */

public interface FamilyServerConnection {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://ec2-52-78-186-215.ap-northeast-2.compute.amazonaws.com/families/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("{family_id}")
    Call<Family> family(@Path("family_id") int family_id);

    @GET("{family_id}/usersBut/{user_id}")
    Call<ArrayList<User>> family_user_Info(@Path("family_id") int family_id, @Path("user_id") int user_id);

    @GET("{family_id}/contents")
    Call<ArrayList<StoryInfo>> family_content_List(@Path("family_id") int family_id);

    @GET("{family_id}/photos")
    Call<ArrayList<Photo>> family_photo_List(@Path("family_id") int family_id);

    @PUT("{family_id}/avatars")
    Call<ResponseBody> update_family_avatar(@Path("family_id") int family_id, @Body RequestBody base64);

    @POST("{family_id}/users")
    Call<ResponseBody> insert_user_into_family(@Path("family_id") int family_id, @QueryMap HashMap<String, String> map);

    @DELETE("{family_id}/users")
    Call<ResponseBody> delete_user_from_family(@Path("family_id") int family_id, @QueryMap HashMap<String, String> map);

    @PUT("{family_id}")
    Call<ResponseBody> update_family_info(@Path("family_id") int family_id, @QueryMap HashMap<String, String> map);
}
