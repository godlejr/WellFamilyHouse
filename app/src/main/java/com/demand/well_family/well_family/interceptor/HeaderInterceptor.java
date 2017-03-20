package com.demand.well_family.well_family.interceptor;

import android.content.SharedPreferences;
import android.support.v4.content.SharedPreferencesCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by ㅇㅇ on 2017-03-14.
 */

public class HeaderInterceptor implements Interceptor {
    private String access_token = null;
    public  Retrofit retrofit;
    private OkHttpClient client;
    private Gson gson = new GsonBuilder().setLenient().create();

    public HeaderInterceptor() {
        super();
    }

    public HeaderInterceptor(String access_token) {
        this.access_token = access_token;

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(this);
        client = httpClient.build();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        if (access_token != null) {
            request = request.newBuilder()
                    .addHeader("Authorization", access_token)
                    .build();
        } else {
            request = request.newBuilder().build();
        }

        Response response = chain.proceed(request);
        return response;
    }

    public Retrofit getClientForMainServer() {
        if (access_token == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://ec2-52-78-186-215.ap-northeast-2.compute.amazonaws.com/main/").addConverterFactory(new NullOnEmptyConverterFactory())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        } else { // report
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://ec2-52-78-186-215.ap-northeast-2.compute.amazonaws.com/main/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
        }

        return retrofit;
    }

    public Retrofit getClientForUserServer() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://ec2-52-78-186-215.ap-northeast-2.compute.amazonaws.com/users/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        return retrofit;
    }

    public Retrofit getClientForStoryServer() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://ec2-52-78-186-215.ap-northeast-2.compute.amazonaws.com/stories/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        return retrofit;
    }

    public Retrofit getClientForFamilyServer() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://ec2-52-78-186-215.ap-northeast-2.compute.amazonaws.com/families/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        return retrofit;
    }

    public Retrofit getClientForCommentServer() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://ec2-52-78-186-215.ap-northeast-2.compute.amazonaws.com/comments/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        return retrofit;
    }

    public Retrofit getClientForNotificationServer() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://ec2-52-78-186-215.ap-northeast-2.compute.amazonaws.com/notifications/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        return retrofit;
    }

    public Retrofit getClientForSongStoryServer() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://ec2-52-78-186-215.ap-northeast-2.compute.amazonaws.com/songstories/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        return retrofit;
    }

    public Retrofit getClientForSongServer() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://ec2-52-78-186-215.ap-northeast-2.compute.amazonaws.com/songs/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        return retrofit;
    }

}
