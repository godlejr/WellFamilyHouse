package com.demand.well_family.well_family.connection;

import com.demand.well_family.well_family.dto.NotificationInfo;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by ㅇㅇ on 2017-03-13.
 */

public interface NotificationServerConnection {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://ec2-52-78-186-215.ap-northeast-2.compute.amazonaws.com/notifications/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    @GET("{notification_id}/family_formation")
    Call<NotificationInfo> NotificationForCreatingFamily(@Path("notification_id") int notification_id);

    @GET("{notification_id}/writing_stories")
    Call<NotificationInfo> NotificationForWritingStory(@Path("notification_id") int notification_id);

    @PUT("{notification_id}")
    Call<ResponseBody> notificationInfo (@Path("notification_id") int notification_id);
}
