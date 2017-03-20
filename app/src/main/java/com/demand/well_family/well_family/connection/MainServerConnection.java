package com.demand.well_family.well_family.connection;

import com.demand.well_family.well_family.dto.Category;
import com.demand.well_family.well_family.dto.User;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by ㅇㅇ on 2017-03-10.
 */

public interface MainServerConnection {
//
//    Retrofit retrofit = new Retrofit.Builder()
//            .baseUrl("http://ec2-52-78-186-215.ap-northeast-2.compute.amazonaws.com/main/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build();
//
//    Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
//            .baseUrl("http://ec2-52-78-186-215.ap-northeast-2.compute.amazonaws.com/main/")
//            .addConverterFactory(GsonConverterFactory.create());


    @POST("login")
    Call<User> login(@QueryMap HashMap<String, String> map);

    @POST("join")
    Call<ResponseBody> join(@QueryMap HashMap<String, String> map);

    @GET("check")
    Call<Integer> email_check(@QueryMap HashMap<String, String> map);

    @GET("report_categories") // token 필요
    Call<ArrayList<Category>> report_category_List();
}
