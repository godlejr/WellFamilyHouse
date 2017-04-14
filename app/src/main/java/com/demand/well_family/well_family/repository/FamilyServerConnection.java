package com.demand.well_family.well_family.repository;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.dto.StoryInfo;
import com.demand.well_family.well_family.dto.UserInfoForFamilyJoin;

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

public interface FamilyServerConnection {
    @GET("{family_id}")
    Call<Family> family(@Path("family_id") int family_id);

    @GET("{family_id}/usersBut/{user_id}")
    Call<ArrayList<UserInfoForFamilyJoin>> family_user_Info(@Path("family_id") int family_id, @Path("user_id") int user_id);

    @GET("{family_id}/contents")
    Call<ArrayList<StoryInfo>> family_content_List(@Path("family_id") int family_id);

    @GET("{family_id}/photos")
    Call<ArrayList<Photo>> family_photo_List(@Path("family_id") int family_id);

    @PUT("{family_id}/avatars")
    Call<ResponseBody> update_family_avatar(@Path("family_id") int family_id, @Body RequestBody base64);

    @POST("{family_id}/users")
    Call<ResponseBody> insert_user_into_family(@Path("family_id") int family_id, @QueryMap HashMap<String, String> map);

    @DELETE("{family_id}/users/{user_id}")// 탈퇴 하기
    Call<ResponseBody> delete_user_from_family(@Path("family_id") int family_id, @Path("user_id") int user_id);

    @PUT("{family_id}")
    Call<ResponseBody> update_family_info(@Path("family_id") int family_id, @QueryMap HashMap<String, String> map);

    @GET("{family_id}/find_user")
    Call<ArrayList<UserInfoForFamilyJoin>> find_user(@Path("family_id") int family_id, @QueryMap HashMap<String, String> map);

    @PUT(" {family_id}/users/{user_id}")
        // 초대 승인, 가입 승인
    Call<Void> update_user_for_familyjoin(@Path("family_id") int family_id, @Path("user_id") int user_id);

    @GET("{family_id}/family_joiners")
    Call<ArrayList<UserInfoForFamilyJoin>> family_joiners(@Path("family_id") int family_id);

    @DELETE("{family_id}")
    Call<ResponseBody> delete_family(@Path("family_id") int family_id);
}
