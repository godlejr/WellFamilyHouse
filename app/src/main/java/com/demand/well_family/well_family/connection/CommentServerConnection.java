package com.demand.well_family.well_family.connection;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.DELETE;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by ㅇㅇ on 2017-03-13.
 */

public interface CommentServerConnection {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://ec2-52-78-186-215.ap-northeast-2.compute.amazonaws.com/comments/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @PUT("{comment_id}")
    Call<ResponseBody> update_comment(@Path("comment_id") int comment_id, @QueryMap HashMap<String, String> map);

    @DELETE("{comment_id}")
    Call<ResponseBody> delete_comment(@Path("comment_id") int comment_id, @QueryMap HashMap<String, String> map);
}
