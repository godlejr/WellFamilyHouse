package com.demand.well_family.well_family.repository;

import com.demand.well_family.well_family.dto.NotificationInfo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by ㅇㅇ on 2017-03-13.
 */

public interface NotificationServerConnection {

    @GET("{notification_id}/families")
    Call<NotificationInfo> NotificationForCreatingFamilyAndJoinAndWantToJoin(@Path("notification_id") int notification_id);

    @GET("{notification_id}/comments")
    Call<NotificationInfo> NotificationForWritingComment(@Path("notification_id") int notification_id);

    @GET("{notification_id}/likes")
    Call<NotificationInfo> NotificationForLike(@Path("notification_id") int notification_id);

    @GET("{notification_id}/stories")
    Call<NotificationInfo> NotificationForWritingStory(@Path("notification_id") int notification_id);

    @GET("{notification_id}/songstories")
    Call<NotificationInfo> NotificationForWritingSongStory(@Path("notification_id") int notification_id);

    @GET("{notification_id}/falldiagnosisstories")
    Call<NotificationInfo> NotificationForWritingFallDiagnosisStory(@Path("notification_id") int notification_id);

    @PUT("{notification_id}")
    Call<ResponseBody> notificationInfo (@Path("notification_id") int notification_id);




    @GET("{notification_id}/exercisestories")
    Call<NotificationInfo> NotificationForWritingExerciseStroy(@Path("notification_id") int notification_id);



}
