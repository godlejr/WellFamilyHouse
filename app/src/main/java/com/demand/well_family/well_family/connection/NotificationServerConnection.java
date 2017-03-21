package com.demand.well_family.well_family.connection;

import com.demand.well_family.well_family.dto.NotificationInfo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by ㅇㅇ on 2017-03-13.
 */

public interface NotificationServerConnection {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://ec2-52-78-186-215.ap-northeast-2.compute.amazonaws.com/notifications/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    @GET("{notification_id}/families")
    Call<NotificationInfo> NotificationForCreatingFamily(@Path("notification_id") int notification_id);

    @GET("{notification_id}/comments")
    Call<NotificationInfo> NotificationForWritingComment(@Path("notification_id") int notification_id);

    @GET("{notification_id}/likes")
    Call<NotificationInfo> NotificationForLike(@Path("notification_id") int notification_id);

    @GET("{notification_id}/stories")
    Call<NotificationInfo> NotificationForWritingStory(@Path("notification_id") int notification_id);

    @GET("{notification_id}/songstories")
    Call<NotificationInfo> NotificationForWritingSongStory(@Path("notification_id") int notification_id);

    @PUT("{notification_id}")
    Call<ResponseBody> notificationInfo (@Path("notification_id") int notification_id);
}
