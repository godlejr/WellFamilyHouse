package com.demand.well_family.well_family.exercisestory.base.interactor.impl;

import com.demand.well_family.well_family.dto.ExerciseStory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.exercisestory.base.adapter.ExerciseStoryAdapter;
import com.demand.well_family.well_family.exercisestory.base.interactor.ExerciseStoryInteractor;
import com.demand.well_family.well_family.exercisestory.base.presenter.ExerciseStoryPresenter;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.ExerciseStoryServerConnection;
import com.demand.well_family.well_family.repository.UserServerConnection;
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

public class ExerciseStoryInteractorImpl implements ExerciseStoryInteractor {
    private ExerciseStoryPresenter exerciseStoryPresenter;

    private User user;
    private User storyUser;
    private UserServerConnection userServerConnection;

    private int storyListSize;
    private boolean isStoryEnd;
    private int storyInsertCount = 0;
    private static final int CONTENT_OFFSET = 20;


    private static final Logger logger = LoggerFactory.getLogger(ExerciseStoryInteractorImpl.class);

    private ArrayList<ExerciseStory> exerciseStoryList;
    private ArrayList<ExerciseStory> exerciseStoryTempList;
    private ExerciseStoryServerConnection exerciseStoryServerConnection;

    public ExerciseStoryInteractorImpl(ExerciseStoryPresenter exerciseStoryPresenter) {
        this.exerciseStoryPresenter = exerciseStoryPresenter;
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
    public User getStoryUser() {
        return storyUser;
    }

    @Override
    public void setStoryUser(User storyUser) {
        this.storyUser = storyUser;
    }

    @Override
    public void getExerciseStoryData(int storyUserId) {
        String accessToken = user.getAccess_token();

        userServerConnection = new NetworkInterceptor(accessToken).getClientForUserServer().create(UserServerConnection.class);
        Call<ArrayList<ExerciseStory>> callGetExerciseStoryList = userServerConnection.selectExerciseStoryList(storyUserId);
        callGetExerciseStoryList.enqueue(new Callback<ArrayList<ExerciseStory>>() {
            @Override
            public void onResponse(Call<ArrayList<ExerciseStory>> call, Response<ArrayList<ExerciseStory>> response) {
                if (response.isSuccessful()) {
                    exerciseStoryList = response.body();
                    exerciseStoryPresenter.onSuccessGetExerciseStoryData();
                } else {
                    exerciseStoryPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }

            }

            @Override
            public void onFailure(Call<ArrayList<ExerciseStory>> call, Throwable t) {
                log(t);
                exerciseStoryPresenter.onNetworkError(null);

            }
        });

    }

    @Override
    public ArrayList<ExerciseStory> getExerciseStoryListWithOffset() {
        storyListSize = exerciseStoryList.size();
        exerciseStoryTempList = new ArrayList<>();

        if (storyListSize == 0) {

        } else {
            int loopSize = 0;
            if (storyListSize <= CONTENT_OFFSET) {
                loopSize = storyListSize;
                isStoryEnd = true;
            } else {
                loopSize = CONTENT_OFFSET;
                storyListSize -= loopSize;
            }
            for (int i = 0; i < loopSize; i++) {
                exerciseStoryTempList.add(exerciseStoryList.get(i));
            }
        }

        return exerciseStoryTempList;
    }

    @Override
    public void getStoryDataAdded() {
        int loopSize = 0;
        if (isStoryEnd == false && storyListSize <= CONTENT_OFFSET) {
            loopSize = storyListSize;
            storyInsertCount++;
        } else {
            loopSize = CONTENT_OFFSET;
            storyListSize -= loopSize;
            storyInsertCount++;
        }

        if (isStoryEnd == false) {
            if (loopSize == storyListSize) {
                isStoryEnd = true;
            }

            exerciseStoryPresenter.onGettingStoryDataAdded();

            final int finalLoopSize = loopSize;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = (CONTENT_OFFSET * storyInsertCount); i < finalLoopSize + (CONTENT_OFFSET * storyInsertCount); i++) {
                            exerciseStoryTempList.add(exerciseStoryList.get(i));
                            exerciseStoryPresenter.onSuccessGetStoryDataAdded(i);
                        }
                        Thread.sleep(200);
                    } catch (Exception e) {
                        log(e);
                    }
                    exerciseStoryPresenter.onSuccessThreadRun();
                }
            }).start();
        }
    }

    @Override
    public void getLikeCount(final ExerciseStoryAdapter.ExerciseStoryViewHolder holder, int exerciseStoryId) {
        String accessToken = user.getAccess_token();
        exerciseStoryServerConnection = new NetworkInterceptor(accessToken).getExerciseStoryServer().create(ExerciseStoryServerConnection.class);
        Call<Integer> callGetExerciseStoryLikeCount = exerciseStoryServerConnection.selectExerciseStoryLikeCount(exerciseStoryId);
        callGetExerciseStoryLikeCount.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int count = response.body();
                    exerciseStoryPresenter.onSuccessGetLikeCount(holder, count);
                } else {
                    exerciseStoryPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                log(t);
                exerciseStoryPresenter.onNetworkError(null);
            }
        });

    }

    @Override
    public void getCommentCount(final ExerciseStoryAdapter.ExerciseStoryViewHolder holder, int exerciseStoryId) {
        String accessToken = user.getAccess_token();
        exerciseStoryServerConnection = new NetworkInterceptor(accessToken).getExerciseStoryServer().create(ExerciseStoryServerConnection.class);
        Call<Integer> callGetExerciseStoryCommentCount = exerciseStoryServerConnection.selectExerciseStoryCommentCount(exerciseStoryId);
        callGetExerciseStoryCommentCount.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int count = response.body();
                    exerciseStoryPresenter.onSuccessGetCommentCount(holder, count);
                } else {
                    exerciseStoryPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                log(t);
                exerciseStoryPresenter.onNetworkError(null);
            }
        });

    }

    @Override
    public void setExerciseStoryHit(int exerciseStoryId) {
        String accessToken = user.getAccess_token();
        exerciseStoryServerConnection = new NetworkInterceptor(accessToken).getExerciseStoryServer().create(ExerciseStoryServerConnection.class);
        Call<ResponseBody> callUpdateExerciseStoryHit = exerciseStoryServerConnection.updateExerciseStoryHit(exerciseStoryId);
        callUpdateExerciseStoryHit.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    //success
                } else {
                    exerciseStoryPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log(t);
                exerciseStoryPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void getContentLikeCheck(final ExerciseStoryAdapter.ExerciseStoryViewHolder holder, final ExerciseStory exerciseStory) {
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
                    exerciseStoryPresenter.onSuccessGetContentLikeCheck(holder, isChecked, position);
                } else {
                    exerciseStoryPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                log(t);
                exerciseStoryPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void setContentLikeUp(final ExerciseStoryAdapter.ExerciseStoryViewHolder holder, final ExerciseStory exerciseStory) {
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
                    exerciseStoryPresenter.onSuccessSetContentLikeUp(holder, position);
                } else {
                    exerciseStoryPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log(t);
                exerciseStoryPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void setContentLikeDown(final ExerciseStoryAdapter.ExerciseStoryViewHolder holder, final ExerciseStory exerciseStory) {
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
                    exerciseStoryPresenter.onSuccessSetContentLikeDown(holder, position);
                } else {
                    exerciseStoryPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log(t);
                exerciseStoryPresenter.onNetworkError(null);
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
