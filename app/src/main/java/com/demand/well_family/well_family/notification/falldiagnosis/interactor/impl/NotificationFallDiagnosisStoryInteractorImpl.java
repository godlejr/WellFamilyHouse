package com.demand.well_family.well_family.notification.falldiagnosis.interactor.impl;

import com.demand.well_family.well_family.dto.CommentInfo;
import com.demand.well_family.well_family.dto.EnvironmentEvaluationCategory;
import com.demand.well_family.well_family.dto.EnvironmentPhoto;
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.dto.FallDiagnosisStoryComment;
import com.demand.well_family.well_family.dto.FallDiagnosisStoryInfo;
import com.demand.well_family.well_family.dto.PhysicalEvaluationScore;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.notification.falldiagnosis.interactor.NotificationFallDiagnosisStoryInteractor;
import com.demand.well_family.well_family.notification.falldiagnosis.presenter.NotificationFallDiagnosisStoryPresenter;
import com.demand.well_family.well_family.repository.FallDiagnosisStoryServerConnection;
import com.demand.well_family.well_family.repository.interceptor.NetworkInterceptor;
import com.demand.well_family.well_family.util.ErrorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ㅇㅇ on 2017-06-13.
 */

public class NotificationFallDiagnosisStoryInteractorImpl implements NotificationFallDiagnosisStoryInteractor {
    private static Logger logger = LoggerFactory.getLogger(NotificationFallDiagnosisStoryInteractorImpl.class);
    private NotificationFallDiagnosisStoryPresenter notificationFallDiagnosisStoryPresenter;
    private User user;
    private FallDiagnosisStory fallDiagnosisStory;
    private FallDiagnosisStoryServerConnection fallDiagnosisStoryServerConnection;
    private FallDiagnosisStoryInfo fallDiagnosisStoryInfo;


    public NotificationFallDiagnosisStoryInteractorImpl(NotificationFallDiagnosisStoryPresenter notificationFallDiagnosisStoryPresenter) {
        this.notificationFallDiagnosisStoryPresenter = notificationFallDiagnosisStoryPresenter;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void setContentLikeUp() {
        String accessToken = user.getAccess_token();
        int storyId = fallDiagnosisStory.getId();
        int userId = user.getId();

        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", String.valueOf(userId));

        fallDiagnosisStoryServerConnection = new NetworkInterceptor(accessToken).getFallDiagnosisStoryServer().create(FallDiagnosisStoryServerConnection.class);
        Call<ResponseBody> callSelectFallDiagnosisStoryInfo = fallDiagnosisStoryServerConnection.insertFallDiagnosisStoryLikeUp(storyId, map);
        callSelectFallDiagnosisStoryInfo.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    notificationFallDiagnosisStoryPresenter.onSuccessSetContentLikeUp();
                } else {
                    notificationFallDiagnosisStoryPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log(t);
                notificationFallDiagnosisStoryPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void setContentLikeDown() {
        String accessToken = user.getAccess_token();
        int storyId = fallDiagnosisStory.getId();
        int userId = user.getId();

        fallDiagnosisStoryServerConnection = new NetworkInterceptor(accessToken).getFallDiagnosisStoryServer().create(FallDiagnosisStoryServerConnection.class);
        Call<ResponseBody> callSelectFallDiagnosisStoryInfo = fallDiagnosisStoryServerConnection.deleteFallDiagnosisStoryLikeDown(storyId, userId);
        callSelectFallDiagnosisStoryInfo.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    notificationFallDiagnosisStoryPresenter.onSuccessSetContentLikeDown();
                } else {
                    notificationFallDiagnosisStoryPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log(t);
                notificationFallDiagnosisStoryPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void getPhysicalEvaluationScore() {
        String accessToken = user.getAccess_token();
        int storyId = fallDiagnosisStory.getId();

        fallDiagnosisStoryServerConnection = new NetworkInterceptor(accessToken).getFallDiagnosisStoryServer().create(FallDiagnosisStoryServerConnection.class);
        Call<PhysicalEvaluationScore> callSelectPhysicalEvaluationScore= fallDiagnosisStoryServerConnection.selectPhysicalEvaluationScore(storyId);
        callSelectPhysicalEvaluationScore.enqueue(new Callback<PhysicalEvaluationScore>() {
            @Override
            public void onResponse(Call<PhysicalEvaluationScore> call, Response<PhysicalEvaluationScore> response) {
                if (response.isSuccessful()) {
                    PhysicalEvaluationScore physicalEvaluationScore = response.body();
                    notificationFallDiagnosisStoryPresenter.onSuccessGetPhysicalEvaluationScore(physicalEvaluationScore);
                } else {
                    notificationFallDiagnosisStoryPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<PhysicalEvaluationScore> call, Throwable t) {
                log(t);
                notificationFallDiagnosisStoryPresenter.onNetworkError(null);
            }
        });

    }

    @Override
    public void getSelfDiagnosisList() {
        String accessToken = user.getAccess_token();
        int storyId = fallDiagnosisStory.getId();

        fallDiagnosisStoryServerConnection = new NetworkInterceptor(accessToken).getFallDiagnosisStoryServer().create(FallDiagnosisStoryServerConnection.class);
        Call<ArrayList<FallDiagnosisContentCategory>> callSelectSelfDiagnosisList= fallDiagnosisStoryServerConnection.selectSelfDiagnosisList(storyId);
        callSelectSelfDiagnosisList.enqueue(new Callback<ArrayList<FallDiagnosisContentCategory>>() {
            @Override
            public void onResponse(Call<ArrayList<FallDiagnosisContentCategory>> call, Response<ArrayList<FallDiagnosisContentCategory>> response) {
                if (response.isSuccessful()) {
                    ArrayList<FallDiagnosisContentCategory> fallDiagnosisContentCategoryList = response.body();
                    notificationFallDiagnosisStoryPresenter.onSuccessGetSelfDiagnosisList(fallDiagnosisContentCategoryList);
                } else {
                    notificationFallDiagnosisStoryPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<FallDiagnosisContentCategory>> call, Throwable t) {
                log(t);
                notificationFallDiagnosisStoryPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void getEnvironmentPhoto() {
        String accessToken = user.getAccess_token();
        int storyId = fallDiagnosisStory.getId();

        fallDiagnosisStoryServerConnection = new NetworkInterceptor(accessToken).getFallDiagnosisStoryServer().create(FallDiagnosisStoryServerConnection.class);
        Call<ArrayList<EnvironmentPhoto>> callSelectEnvironmentPhoto= fallDiagnosisStoryServerConnection.selectEnvironmentPhoto(storyId);
        callSelectEnvironmentPhoto.enqueue(new Callback<ArrayList<EnvironmentPhoto>>() {
            @Override
            public void onResponse(Call<ArrayList<EnvironmentPhoto>> call, Response<ArrayList<EnvironmentPhoto>> response) {
                if (response.isSuccessful()) {
                    ArrayList<EnvironmentPhoto> environmentPhotoList = response.body();
                    notificationFallDiagnosisStoryPresenter.onSuccessGetEnvironmentPhoto(environmentPhotoList);
                } else {
                    notificationFallDiagnosisStoryPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<EnvironmentPhoto>> call, Throwable t) {
                log(t);
                notificationFallDiagnosisStoryPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void getEnvironmentEvaluationList() {
        String accessToken = user.getAccess_token();
        int storyId = fallDiagnosisStory.getId();

        fallDiagnosisStoryServerConnection = new NetworkInterceptor(accessToken).getFallDiagnosisStoryServer().create(FallDiagnosisStoryServerConnection.class);
        Call<ArrayList<EnvironmentEvaluationCategory> > callSelectEnvironmentEvaluationList= fallDiagnosisStoryServerConnection.selectEnvironmentEvaluationList(storyId);
        callSelectEnvironmentEvaluationList.enqueue(new Callback<ArrayList<EnvironmentEvaluationCategory>>() {
            @Override
            public void onResponse(Call<ArrayList<EnvironmentEvaluationCategory>> call, Response<ArrayList<EnvironmentEvaluationCategory>> response) {
                if (response.isSuccessful()) {
                    ArrayList<EnvironmentEvaluationCategory> environmentEvaluationCategoryList = response.body();
                    notificationFallDiagnosisStoryPresenter.onSuccessGetEnvironmentEvaluationList(environmentEvaluationCategoryList);
                } else {
                    notificationFallDiagnosisStoryPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<EnvironmentEvaluationCategory>> call, Throwable t) {
                log(t);
                notificationFallDiagnosisStoryPresenter.onNetworkError(null);
            }
        });

    }

    @Override
    public void getFallDiagnosisStoryCommentList() {
        String accessToken = user.getAccess_token();
        int storyId = fallDiagnosisStory.getId();

        fallDiagnosisStoryServerConnection = new NetworkInterceptor(accessToken).getFallDiagnosisStoryServer().create(FallDiagnosisStoryServerConnection.class);
        Call< ArrayList<CommentInfo>  > callSelectFallDiagnosisStoryCommentList= fallDiagnosisStoryServerConnection.selectFalldiagnosisStoryCommentList(storyId);
        callSelectFallDiagnosisStoryCommentList.enqueue(new Callback<ArrayList<CommentInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<CommentInfo>> call, Response<ArrayList<CommentInfo>> response) {
                if (response.isSuccessful()) {
                    ArrayList<CommentInfo> commentInfoList = response.body();
                    notificationFallDiagnosisStoryPresenter.onSuccessGetFallDiagnosisStoryCommentList(commentInfoList);
                } else {
                    notificationFallDiagnosisStoryPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CommentInfo>> call, Throwable t) {
                log(t);
                notificationFallDiagnosisStoryPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void setFallDiagnosisStoryComment(CommentInfo commentInfo) {
        String accessToken = user.getAccess_token();
        int storyId = fallDiagnosisStory.getId();

        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", String.valueOf(commentInfo.getUser_id()));
        map.put("content", commentInfo.getContent());

        fallDiagnosisStoryServerConnection = new NetworkInterceptor(accessToken).getFallDiagnosisStoryServer().create(FallDiagnosisStoryServerConnection.class);
        Call<FallDiagnosisStoryComment> callSelectFallDiagnosisStoryCommentList= fallDiagnosisStoryServerConnection.insertFalldiagnosisStoryComment(storyId, map);
        callSelectFallDiagnosisStoryCommentList.enqueue(new Callback<FallDiagnosisStoryComment>() {
            @Override
            public void onResponse(Call<FallDiagnosisStoryComment> call, Response<FallDiagnosisStoryComment> response) {
                if (response.isSuccessful()) {
                    FallDiagnosisStoryComment fallDiagnosisStoryComment = response.body();
                    notificationFallDiagnosisStoryPresenter.onSuccessSetFallDiagnosisStoryComment(fallDiagnosisStoryComment);
                } else {
                    notificationFallDiagnosisStoryPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<FallDiagnosisStoryComment> call, Throwable t) {
                log(t);
                notificationFallDiagnosisStoryPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public FallDiagnosisStory getFallDiagnosisStory() {
        return this.fallDiagnosisStory;
    }

    @Override
    public void setFallDiagnosisStory(FallDiagnosisStory fallDiagnosisStory) {
        this.fallDiagnosisStory = fallDiagnosisStory;
    }

    @Override
    public FallDiagnosisStoryInfo getFallDiagnosisStoryInfo() {
        return this.fallDiagnosisStoryInfo;
    }

    @Override
    public void setFallDiagnosisStoryInfo(FallDiagnosisStoryInfo fallDiagnosisStoryInfo) {
        this.fallDiagnosisStoryInfo = fallDiagnosisStoryInfo;
    }

    @Override
    public void getFallDiagnosisStoryCommentCount() {
        String accessToken = user.getAccess_token();
        int StoryId = fallDiagnosisStory.getId();

        fallDiagnosisStoryServerConnection = new NetworkInterceptor(accessToken).getFallDiagnosisStoryServer().create(FallDiagnosisStoryServerConnection.class);
        Call<Integer> callSelectFallDiagnosisStoryCommentCount = fallDiagnosisStoryServerConnection.selectFallDiagnosisStoryCommentCount(StoryId);
        callSelectFallDiagnosisStoryCommentCount.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int commentCount = response.body();
                    notificationFallDiagnosisStoryPresenter.onSuccessGetFallDiagnosisStoryCommentCount(commentCount);
                } else {
                    notificationFallDiagnosisStoryPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                log(t);
                notificationFallDiagnosisStoryPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void getFallDiagnosisStoryLikeCount() {
        String accessToken = user.getAccess_token();
        int StoryId = fallDiagnosisStory.getId();

        fallDiagnosisStoryServerConnection = new NetworkInterceptor(accessToken).getFallDiagnosisStoryServer().create(FallDiagnosisStoryServerConnection.class);
        Call<Integer> callSelectFallDiagnosisStoryCommentCount = fallDiagnosisStoryServerConnection.selectFallDiagnosisStoryLikeCount(StoryId);
        callSelectFallDiagnosisStoryCommentCount.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int likeCount = response.body();
                    notificationFallDiagnosisStoryPresenter.onSuccessGetFallDiagnosisStoryLikeCount(likeCount);
                } else {
                    notificationFallDiagnosisStoryPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                log(t);
                notificationFallDiagnosisStoryPresenter.onNetworkError(null);
            }
        });
    }



    private static void log(Throwable throwable) {
        StackTraceElement[] ste = throwable.getStackTrace();
        String className = ste[0].getClassName();
        String methodName = ste[0].getMethodName();
        int lineNumber = ste[0].getLineNumber();
        String fileName = ste[0].getFileName();

        if (LogFlag.printFlag) {
            if (logger.isInfoEnabled()) {
                logger.error("Exception: " + throwable.getMessage());
                logger.error(className + "." + methodName + " " + fileName + " " + lineNumber + " " + "line");
            }
        }
    }
}
