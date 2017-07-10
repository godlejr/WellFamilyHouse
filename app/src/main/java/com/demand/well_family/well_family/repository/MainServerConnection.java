package com.demand.well_family.well_family.repository;

import com.demand.well_family.well_family.dto.Category;
import com.demand.well_family.well_family.dto.User;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by ㅇㅇ on 2017-03-10.
 */

public interface MainServerConnection {

    @POST("login")
    Call<User> login(@QueryMap HashMap<String, String> map);

    @POST("join")
    Call<ResponseBody> join(@QueryMap HashMap<String, String> map);

    @GET("check")
    Call<Integer> email_check(@QueryMap HashMap<String, String> map);

    @GET("report_categories") // token 필요
    Call<ArrayList<Category>> report_category_List();

    @POST("find_password")  // token 불필요
    Call<ResponseBody> findPassword( @QueryMap HashMap<String, String> map);

    @GET("find_email")  // token 불필요
    Call<User> findEmail(@QueryMap HashMap<String, String> map);

}
