package com.demand.well_family.well_family.repository.interceptor;

import com.demand.well_family.well_family.main.intro.activity.IntroActivity;
import com.demand.well_family.well_family.repository.converter.NullOnEmptyConverterFactory;
import com.demand.well_family.well_family.util.NetworkUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ㅇㅇ on 2017-03-14.
 */

public class NetworkInterceptor implements Interceptor {
    private String access_token = null;
    public Retrofit retrofit;
    private OkHttpClient client;
    private Gson gson = new GsonBuilder().setLenient().create();

    public NetworkInterceptor() {
        super();
    }

    public NetworkInterceptor(String access_token) {
        this.access_token = access_token;

        int cacheSize = 10 * 1024 * 1024; // 10 MB
        Cache cache = new Cache(new File(IntroActivity.context.getCacheDir().toString()), cacheSize);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(this);
        builder.cache(cache);
        builder.networkInterceptors().add(mCacheControlInterceptor);

        client = builder.build();
    }

    private static final Interceptor mCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            // Add Cache Control only for GET methods
            if (request.method().equals("GET")) {
                if (NetworkUtil.isNetworkAvailable(IntroActivity.context)) {
                    // 1 day
                    request.newBuilder()
                            .header("Cache-Control", "only-if-cached")
                            .build();
                } else {
                    // 4 weeks stale
                    request.newBuilder()
                            .header("Cache-Control", "public, max-stale=2419200")
                            .build();
                }
            }

            Response response = chain.proceed(request);

            // Re-write response CC header to force use of cache
            return response.newBuilder()
                    .header("Cache-Control", "public, max-age=86400") // 1 day
                    .build();
        }
    };


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

    public Retrofit getClientForFamilyServerAccessNull() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://ec2-52-78-186-215.ap-northeast-2.compute.amazonaws.com/families/").addConverterFactory(new NullOnEmptyConverterFactory())
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
                .baseUrl("http://ec2-52-78-186-215.ap-northeast-2.compute.amazonaws.com/notifications/").addConverterFactory(new NullOnEmptyConverterFactory())
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

    public Retrofit getFallDiagnosisServer() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://ec2-52-78-186-215.ap-northeast-2.compute.amazonaws.com/fall_diagnosis/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        return retrofit;
    }

    public Retrofit getFallDiagnosisStoryServer() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://ec2-52-78-186-215.ap-northeast-2.compute.amazonaws.com/fall_diagnosis_stories/")
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        return retrofit;
    }


}