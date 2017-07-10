package com.demand.well_family.well_family.exercisestory.detail.interactor.impl;

import com.demand.well_family.well_family.dto.CommentInfo;
import com.demand.well_family.well_family.dto.ExerciseStory;
import com.demand.well_family.well_family.dto.ExerciseStoryComment;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.exercisestory.detail.interactor.ExerciseStoryDetailInteractor;
import com.demand.well_family.well_family.exercisestory.detail.presenter.ExerciseStoryDetailPresenter;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.ExerciseStoryServerConnection;
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
 * Created by ㅇㅇ on 2017-06-27.
 */

public class ExerciseStoryDetailInteractorImpl implements ExerciseStoryDetailInteractor {
    private ExerciseStoryDetailPresenter exerciseStoryDetailPresenter;
    private ExerciseStoryServerConnection exerciseStoryServerConnection;
    private User user;
    private ExerciseStory exerciseStory;
    private ArrayList<CommentInfo> commentInfoList;

    private static final Logger logger = LoggerFactory.getLogger(ExerciseStoryDetailInteractorImpl.class);

    public ExerciseStoryDetailInteractorImpl(ExerciseStoryDetailPresenter exerciseStoryDetailPresenter) {
        this.exerciseStoryDetailPresenter = exerciseStoryDetailPresenter;
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
    public ExerciseStory getExerciseStory() {
        return exerciseStory;
    }

    @Override
    public void setExerciseStory(ExerciseStory exerciseStory) {
        this.exerciseStory = exerciseStory;
    }

    @Override
    public void getLikeCount() {
        int exerciseStoryId = exerciseStory.getId();
        String accessToken = user.getAccess_token();
        exerciseStoryServerConnection = new NetworkInterceptor(accessToken).getExerciseStoryServer().create(ExerciseStoryServerConnection.class);
        Call<Integer> callGetExerciseStoryLikeCount = exerciseStoryServerConnection.selectExerciseStoryLikeCount(exerciseStoryId);
        callGetExerciseStoryLikeCount.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int count = response.body();
                    exerciseStoryDetailPresenter.onSuccessGetLikeCount(count);
                } else {
                    exerciseStoryDetailPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                log(t);
                exerciseStoryDetailPresenter.onNetworkError(null);
            }
        });

    }

    @Override
    public void getCommentCount() {
        int exerciseStoryId = exerciseStory.getId();
        String accessToken = user.getAccess_token();
        exerciseStoryServerConnection = new NetworkInterceptor(accessToken).getExerciseStoryServer().create(ExerciseStoryServerConnection.class);
        Call<Integer> callGetExerciseStoryCommentCount = exerciseStoryServerConnection.selectExerciseStoryCommentCount(exerciseStoryId);
        callGetExerciseStoryCommentCount.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int count = response.body();
                    exerciseStoryDetailPresenter.onSuccessGetCommentCount(count);
                } else {
                    exerciseStoryDetailPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                log(t);
                exerciseStoryDetailPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void getExerciseStoryCommentList(){
        int exerciseStoryId = exerciseStory.getId();
        String accessToken = user.getAccess_token();
        exerciseStoryServerConnection = new NetworkInterceptor(accessToken).getExerciseStoryServer().create(ExerciseStoryServerConnection.class);
        Call<ArrayList<CommentInfo>> callGetExerciseStoryCommentList = exerciseStoryServerConnection.selectExerciseStoryCommentList(exerciseStoryId);
        callGetExerciseStoryCommentList.enqueue(new Callback<ArrayList<CommentInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<CommentInfo>> call, Response<ArrayList<CommentInfo>> response) {
                if (response.isSuccessful()) {
                    commentInfoList = response.body();
                    exerciseStoryDetailPresenter.onSuccessGetExerciseStoryCommentList(commentInfoList);
                } else {
                    exerciseStoryDetailPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CommentInfo>> call, Throwable t) {
                log(t);
                exerciseStoryDetailPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void setExerciseStoryComment(CommentInfo commentInfo) {
        int exerciseStoryId = exerciseStory.getId();
        int userId = commentInfo.getUser_id();
        String accessToken = user.getAccess_token();
        String content = commentInfo.getContent();

        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", String.valueOf(userId));
        map.put("content", content);
        exerciseStoryServerConnection = new NetworkInterceptor(accessToken).getExerciseStoryServer().create(ExerciseStoryServerConnection.class);
        Call<ExerciseStoryComment> callInsertExerciseStoryComment = exerciseStoryServerConnection.insertExerciseStoryComment(exerciseStoryId, map);
        callInsertExerciseStoryComment.enqueue(new Callback<ExerciseStoryComment>() {
            @Override
            public void onResponse(Call<ExerciseStoryComment> call, Response<ExerciseStoryComment> response) {
                if (response.isSuccessful()) {
                    ExerciseStoryComment exerciseStoryComment = response.body();
                    exerciseStoryDetailPresenter.onSuccessSetExerciseStoryComment(exerciseStoryComment);
                } else {
                    exerciseStoryDetailPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ExerciseStoryComment> call, Throwable t) {
                log(t);
                exerciseStoryDetailPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void setContentLikeUp() {
        String accessToken = user.getAccess_token();
        int storyId = exerciseStory.getId();
        int userId = user.getId();

        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", String.valueOf(userId));

        exerciseStoryServerConnection = new NetworkInterceptor(accessToken).getExerciseStoryServer().create(ExerciseStoryServerConnection.class);
        Call<ResponseBody> callSetExerciseStoryLikeUp = exerciseStoryServerConnection.insertExerciseStoryLikeUp(storyId, map);
        callSetExerciseStoryLikeUp.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    int position = exerciseStory.getPosition();
                    exerciseStoryDetailPresenter.onSuccessSetContentLikeUp(position);
                } else {
                    exerciseStoryDetailPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log(t);
                exerciseStoryDetailPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void setContentLikeDown() {
        String accessToken = user.getAccess_token();
        int storyId = exerciseStory.getId();
        int userId = user.getId();

        exerciseStoryServerConnection = new NetworkInterceptor(accessToken).getExerciseStoryServer().create(ExerciseStoryServerConnection.class);
        Call<ResponseBody> callDeleteExerciseStoryLikeDown = exerciseStoryServerConnection.deleteExerciseStoryLikeDown(userId, storyId);
        callDeleteExerciseStoryLikeDown.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    int position = exerciseStory.getPosition();
                    exerciseStoryDetailPresenter.onSuccessSetContentLikeDown(position);
                } else {
                    exerciseStoryDetailPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log(t);
                exerciseStoryDetailPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void getContentLikeCheck() {
        String accessToken = user.getAccess_token();
        int storyId = exerciseStory.getId();
        int userId = user.getId();

        exerciseStoryServerConnection = new NetworkInterceptor(accessToken).getExerciseStoryServer().create(ExerciseStoryServerConnection.class);
        Call<Integer> callSelectExerciseStoryLikeCheck = exerciseStoryServerConnection.selectExerciseStoryLikeCheck(userId, storyId);
        callSelectExerciseStoryLikeCheck.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int isChecked = response.body();
                    int position = exerciseStory.getPosition();
                    exerciseStoryDetailPresenter.onSuccessGetContentLikeCheck(isChecked, position);
                } else {
                    exerciseStoryDetailPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                log(t);
                exerciseStoryDetailPresenter.onNetworkError(null);
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
