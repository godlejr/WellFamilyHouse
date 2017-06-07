package com.demand.well_family.well_family.falldiagnosistory.base.interactor.impl;

import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.dto.FallDiagnosisStoryInfo;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.falldiagnosistory.base.adapter.FallDiagnosisStoryAdapter;
import com.demand.well_family.well_family.falldiagnosistory.base.interactor.FallDiagnosisStoryInteractor;
import com.demand.well_family.well_family.falldiagnosistory.base.presenter.FallDiagnosisStoryPresenter;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.FallDiagnosisStoryServerConnection;
import com.demand.well_family.well_family.repository.UserServerConnection;
import com.demand.well_family.well_family.repository.interceptor.HeaderInterceptor;
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
 * Created by ㅇㅇ on 2017-06-01.
 */

public class FallDiagnosisStoryInteractorImpl implements FallDiagnosisStoryInteractor {
    private FallDiagnosisStoryPresenter fallDiagnosisStoryPresenter;

    private User storyUser;
    private User user;

    private UserServerConnection userServerConnection;
    private FallDiagnosisStoryServerConnection fallDiagnosisStoryServerConnection;
    private static final Logger logger = LoggerFactory.getLogger(FallDiagnosisStoryInteractorImpl.class);


    private ArrayList<FallDiagnosisStory> fallDiagnosisStoryInfoList;
    private ArrayList<FallDiagnosisStory> fallDiagnosisStoryList;

    private FallDiagnosisStory fallDiagnosisStory;
    private FallDiagnosisStoryInfo fallDiagnosisStoryInfo;

    private int storyListSize;
    private boolean isStoryEnd;
    private int storyInsertCount = 0;
    private static final int CONTENT_OFFSET = 20;


    public FallDiagnosisStoryInteractorImpl(FallDiagnosisStoryPresenter fallDiagnosisStoryPresenter) {
        this.fallDiagnosisStoryPresenter = fallDiagnosisStoryPresenter;
    }


    public FallDiagnosisStoryInfo getFallDiagnosisStoryInfo() {
        return this.fallDiagnosisStoryInfo;
    }

    public void setFallDiagnosisStoryInfo(FallDiagnosisStoryInfo fallDiagnosisStoryInfo) {
        this.fallDiagnosisStoryInfo = fallDiagnosisStoryInfo;
    }

    @Override
    public User getStoryUser() {
        return storyUser;
    }

    @Override
    public void setStoryUser(User user) {
        this.storyUser = user;
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
    public void setFallDiagnosisStory(FallDiagnosisStory fallDiagnosisStory) {
        this.fallDiagnosisStory = fallDiagnosisStory;
    }

    @Override
    public FallDiagnosisStory getFallDiagnosisStory() {
        return this.fallDiagnosisStory;
    }

    @Override
    public ArrayList<FallDiagnosisStory> getFallDiagnosisStoryInfoList() {
        return fallDiagnosisStoryInfoList;
    }

    @Override
    public void setFallDiagnosisStoryInfoList(ArrayList<FallDiagnosisStory> fallDiagnosisStoryInfoList) {
        this.fallDiagnosisStoryInfoList = fallDiagnosisStoryInfoList;
    }

    @Override
    public ArrayList<FallDiagnosisStory> getFallDiagnosisStoryList() {
        return fallDiagnosisStoryList;
    }

    @Override
    public void setFallDiagnosisStoryList(ArrayList<FallDiagnosisStory> fallDiagnosisStoryList) {
        this.fallDiagnosisStoryList = fallDiagnosisStoryList;
    }

    @Override
    public void getFallDiagnosisStoryData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String accessToken = user.getAccess_token();
                int storyUserId = storyUser.getId();

                userServerConnection = new HeaderInterceptor(accessToken).getClientForUserServer().create(UserServerConnection.class);
                Call<ArrayList<FallDiagnosisStory>> callSelectFallDiagnosisStoryList = userServerConnection.selectFallDiagnosisStoryList(storyUserId);
                callSelectFallDiagnosisStoryList.enqueue(new Callback<ArrayList<FallDiagnosisStory>>() {
                    @Override
                    public void onResponse(Call<ArrayList<FallDiagnosisStory>> call, Response<ArrayList<FallDiagnosisStory>> response) {
                        if (response.isSuccessful()) {
                            fallDiagnosisStoryInfoList = response.body();
                            fallDiagnosisStoryPresenter.onSuccessGetFallDiagnosisStory();
                        } else {
                            fallDiagnosisStoryPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<FallDiagnosisStory>> call, Throwable t) {
                        log(t);
                        fallDiagnosisStoryPresenter.onNetworkError(null);
                    }
                });
            }
        }).start();
    }

    @Override
    public void getFallDiagnosisStoryCommentCount(final FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, FallDiagnosisStory fallDiagnosisStory) {
        String accessToken = user.getAccess_token();
        int StoryId = fallDiagnosisStory.getId();

        fallDiagnosisStoryServerConnection = new HeaderInterceptor(accessToken).getFallDiagnosisStoryServer().create(FallDiagnosisStoryServerConnection.class);
        Call<Integer> callSelectFallDiagnosisStoryCommentCount = fallDiagnosisStoryServerConnection.selectFallDiagnosisStoryCommentCount(StoryId);
        callSelectFallDiagnosisStoryCommentCount.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int commentCount = response.body();
                    fallDiagnosisStoryPresenter.onSuccessGetFallDiagnosisStoryCommentCount(holder, commentCount);
                } else {
                    fallDiagnosisStoryPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                log(t);
                fallDiagnosisStoryPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void getFallDiagnosisStoryLikeCount(final FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, FallDiagnosisStory fallDiagnosisStory) {
        String accessToken = user.getAccess_token();
        int StoryId = fallDiagnosisStory.getId();

        fallDiagnosisStoryServerConnection = new HeaderInterceptor(accessToken).getFallDiagnosisStoryServer().create(FallDiagnosisStoryServerConnection.class);
        Call<Integer> callSelectFallDiagnosisStoryCommentCount = fallDiagnosisStoryServerConnection.selectFallDiagnosisStoryLikeCount(StoryId);
        callSelectFallDiagnosisStoryCommentCount.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int likeCount = response.body();
                    fallDiagnosisStoryPresenter.onSuccessGetFallDiagnosisStoryLikeCount(holder, likeCount);
                } else {
                    fallDiagnosisStoryPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                log(t);
                fallDiagnosisStoryPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public ArrayList<FallDiagnosisStory> getFallDiagnosisStoryListWithOffset() {
        storyListSize = fallDiagnosisStoryInfoList.size();
        fallDiagnosisStoryList = new ArrayList<>();

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
                fallDiagnosisStoryList.add(fallDiagnosisStoryInfoList.get(i));
            }
        }

        return fallDiagnosisStoryList;
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

            fallDiagnosisStoryPresenter.onGettingStoryDataAdded();

            final int finalLoopSize = loopSize;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = (CONTENT_OFFSET * storyInsertCount); i < finalLoopSize + (CONTENT_OFFSET * storyInsertCount); i++) {
                            fallDiagnosisStoryList.add(fallDiagnosisStoryInfoList.get(i));
                            fallDiagnosisStoryPresenter.onSuccessGetStoryDataAdded(i);
                        }
                        Thread.sleep(200);
                    } catch (Exception e) {
                        log(e);
                    }
                    fallDiagnosisStoryPresenter.onSuccessThreadRun();
                }
            }).start();
        }
    }

    @Override
    public void getFallDiagnosisStoryInfoData(final FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, final FallDiagnosisStory fallDiagnosisStory) {
        String accessToken = user.getAccess_token();
        int storyId = fallDiagnosisStory.getId();

        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", String.valueOf(fallDiagnosisStory.getUser_id()));
        map.put("fall_diagnosis_category_id", String.valueOf(fallDiagnosisStory.getFall_diagnosis_category_id()));
        map.put("fall_diagnosis_risk_category_id", String.valueOf(fallDiagnosisStory.getFall_diagnosis_risk_category_id()));

        fallDiagnosisStoryServerConnection = new HeaderInterceptor(accessToken).getFallDiagnosisStoryServer().create(FallDiagnosisStoryServerConnection.class);
        Call<FallDiagnosisStoryInfo> callSelectFallDiagnosisStoryInfo = fallDiagnosisStoryServerConnection.selectFallDiagnosisStoryInfo(storyId, map);
        callSelectFallDiagnosisStoryInfo.enqueue(new Callback<FallDiagnosisStoryInfo>() {
            @Override
            public void onResponse(Call<FallDiagnosisStoryInfo> call, Response<FallDiagnosisStoryInfo> response) {
                if (response.isSuccessful()) {
                    fallDiagnosisStoryInfo = response.body();
                    fallDiagnosisStoryPresenter.onSuccessGetFallDiagnosisStoryInfo(holder, fallDiagnosisStoryInfo, fallDiagnosisStory);
                } else {
                    fallDiagnosisStoryPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<FallDiagnosisStoryInfo> call, Throwable t) {
                log(t);
                fallDiagnosisStoryPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void getContentLikeCheck(final FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, final FallDiagnosisStory fallDiagnosisStory) {
        String accessToken = user.getAccess_token();
        int storyId = fallDiagnosisStory.getId();
        int userId = user.getId();

        fallDiagnosisStoryServerConnection = new HeaderInterceptor(accessToken).getFallDiagnosisStoryServer().create(FallDiagnosisStoryServerConnection.class);
        Call<Integer> callSelectFallDiagnosisStoryInfo = fallDiagnosisStoryServerConnection.selectFallDiagnosisStoryLikeCheck(storyId, userId);
        callSelectFallDiagnosisStoryInfo.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int isChecked = response.body();
                    int position = fallDiagnosisStory.getPosition();
                    fallDiagnosisStoryPresenter.onSuccessGetContentLikeCheck(holder, isChecked, position);
                } else {
                    fallDiagnosisStoryPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                log(t);
                fallDiagnosisStoryPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void setContentLikeUp(final FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, final FallDiagnosisStory fallDiagnosisStory) {
        String accessToken = user.getAccess_token();
        int storyId = fallDiagnosisStory.getId();
        int userId = user.getId();

        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", String.valueOf(userId));

        fallDiagnosisStoryServerConnection = new HeaderInterceptor(accessToken).getFallDiagnosisStoryServer().create(FallDiagnosisStoryServerConnection.class);
        Call<ResponseBody> callSelectFallDiagnosisStoryInfo = fallDiagnosisStoryServerConnection.insertFallDiagnosisStoryLikeUp(storyId, map);
        callSelectFallDiagnosisStoryInfo.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    int position = fallDiagnosisStory.getPosition();
                    fallDiagnosisStoryPresenter.onSuccessSetContentLikeUp(holder, position);
                } else {
                    fallDiagnosisStoryPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log(t);
                fallDiagnosisStoryPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void setContentLikeDown(final FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, final FallDiagnosisStory fallDiagnosisStory) {
        String accessToken = user.getAccess_token();
        int storyId = fallDiagnosisStory.getId();
        int userId = user.getId();

        fallDiagnosisStoryServerConnection = new HeaderInterceptor(accessToken).getFallDiagnosisStoryServer().create(FallDiagnosisStoryServerConnection.class);
        Call<ResponseBody> callSelectFallDiagnosisStoryInfo = fallDiagnosisStoryServerConnection.deleteFallDiagnosisStoryLikeDown(storyId, userId);
        callSelectFallDiagnosisStoryInfo.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    int position = fallDiagnosisStory.getPosition();
                    fallDiagnosisStoryPresenter.onSuccessSetContentLikeDown(holder, position);
                } else {
                    fallDiagnosisStoryPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log(t);
                fallDiagnosisStoryPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void setFallDiagnosisStoryHit(FallDiagnosisStory fallDiagnosisStory) {
        String accessToken = user.getAccess_token();
        int storyId = fallDiagnosisStory.getId();

        fallDiagnosisStoryServerConnection = new HeaderInterceptor(accessToken).getFallDiagnosisStoryServer().create(FallDiagnosisStoryServerConnection.class);
        Call<ResponseBody> callSelectFallDiagnosisStoryInfo = fallDiagnosisStoryServerConnection.updateFallDiagnosisStoryHit(storyId);
        callSelectFallDiagnosisStoryInfo.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // success
                } else {
                    fallDiagnosisStoryPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log(t);
                fallDiagnosisStoryPresenter.onNetworkError(null);
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
