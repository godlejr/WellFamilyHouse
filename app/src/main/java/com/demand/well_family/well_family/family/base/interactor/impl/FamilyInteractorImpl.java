package com.demand.well_family.well_family.family.base.interactor.impl;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.dto.StoryInfo;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.family.base.adapter.content.ContentAdapter;
import com.demand.well_family.well_family.family.base.interactor.FamilyInteractor;
import com.demand.well_family.well_family.family.base.presenter.FamilyPresenter;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.FamilyServerConnection;
import com.demand.well_family.well_family.repository.StoryServerConnection;
import com.demand.well_family.well_family.repository.UserServerConnection;
import com.demand.well_family.well_family.repository.interceptor.NetworkInterceptor;
import com.demand.well_family.well_family.util.ErrorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dev-0 on 2017-04-18.
 */

public class FamilyInteractorImpl implements FamilyInteractor {
    private FamilyPresenter familyPresenter;

    private boolean notificationFlag;
    private Family family;
    private User user;

    private int contentSize;
    private boolean isContentEnd;
    private int contentInsertCount = 0;
    private static final int CONTENT_OFFSET = 20;

    private ArrayList<StoryInfo> storyInfoList;
    private ArrayList<StoryInfo> storyList = null;

    private UserServerConnection userServerConnection;
    private FamilyServerConnection familyServerConnection;
    private StoryServerConnection storyServerConnection;

    private static final Logger logger = LoggerFactory.getLogger(FamilyInteractorImpl.class);


    public FamilyInteractorImpl(FamilyPresenter familyPresenter) {
        this.familyPresenter = familyPresenter;
        this.isContentEnd = false;
    }


    @Override
    public void setNotificationFlag(boolean notificationFlag) {
        this.notificationFlag = notificationFlag;
    }

    @Override
    public boolean getNotificationFlag() {
        return this.notificationFlag;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public User getUser() {
        return this.user;
    }

    @Override
    public Family getFamily() {
        return this.family;
    }

    @Override
    public void getUserData() {
        String accessToken = user.getAccess_token();
        int userId = user.getId();
        int familyId = family.getId();

        familyServerConnection = new NetworkInterceptor(accessToken).getClientForFamilyServer().create(FamilyServerConnection.class);
        Call<ArrayList<User>> call_users = familyServerConnection.family_user_Info(familyId, userId);
        call_users.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                if (response.isSuccessful()) {
                    ArrayList<User> userList = response.body();
                    familyPresenter.onSuccessGetUserData(userList);
                } else {
                    familyPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                log(t);
                familyPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void getContentData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String accessToken = user.getAccess_token();
                int familyId = family.getId();

                familyServerConnection = new NetworkInterceptor(accessToken).getClientForFamilyServer().create(FamilyServerConnection.class);
                Call<ArrayList<StoryInfo>> call = familyServerConnection.family_content_List(familyId);
                call.enqueue(new Callback<ArrayList<StoryInfo>>() {
                    @Override
                    public void onResponse(Call<ArrayList<StoryInfo>> call, Response<ArrayList<StoryInfo>> response) {
                        if (response.isSuccessful()) {
                            storyInfoList = response.body();
                            familyPresenter.onSuccessGetContentData();

                        } else {
                            familyPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<StoryInfo>> call, Throwable t) {
                        log(t);
                        familyPresenter.onNetworkError(null);
                    }
                });
            }
        }).start();
    }

    @Override
    public void getContentDataAdded() {
        int loopSize = 0;
        if (isContentEnd == false && contentSize <= CONTENT_OFFSET) {
            loopSize = contentSize;
            contentInsertCount++;
        } else {
            loopSize = CONTENT_OFFSET;
            contentSize -= loopSize;
            contentInsertCount++;
        }

        if (isContentEnd == false) {
            if (loopSize == contentSize) {
                isContentEnd = true;
            }

            familyPresenter.onGettingContentDataAdded();

            final int finalLoopSize = loopSize;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = (CONTENT_OFFSET * contentInsertCount); i < finalLoopSize + (CONTENT_OFFSET * contentInsertCount); i++) {
                            storyList.add(new StoryInfo(storyInfoList.get(i).getUser_id(), storyInfoList.get(i).getName(), storyInfoList.get(i).getAvatar(),
                                    storyInfoList.get(i).getStory_id(), storyInfoList.get(i).getCreated_at(), storyInfoList.get(i).getContent()));

                            familyPresenter.onSuccessGetContentDataAdded(i);
                        }
                        Thread.sleep(200);
                    } catch (Exception e) {
                        log(e);
                    }
                    familyPresenter.onSuccessThreadRun();
                }
            }).start();
        }
    }

    @Override
    public void getContentUser(int contentUserId) {
        String accessToken = user.getAccess_token();

        userServerConnection = new NetworkInterceptor(accessToken).getClientForUserServer().create(UserServerConnection.class);

        Call<User> call_user = userServerConnection.user_Info(contentUserId);
        call_user.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    familyPresenter.onSuccessGetContentUser(user);
                } else {
                    familyPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                log(t);
                familyPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public ArrayList<StoryInfo> getStoryListWithOffset() {
        contentSize = storyInfoList.size();
        storyList = new ArrayList<>();

        if (contentSize == 0) {
            // there is no content
        } else {
            int loopSize = 0;
            if (contentSize <= CONTENT_OFFSET) {
                loopSize = contentSize;
                isContentEnd = true;
            } else {
                loopSize = CONTENT_OFFSET;
                contentSize -= loopSize;
            }
            for (int i = 0; i < loopSize; i++) {
                storyList.add(new StoryInfo(storyInfoList.get(i).getUser_id(), storyInfoList.get(i).getName(), storyInfoList.get(i).getAvatar(),
                        storyInfoList.get(i).getStory_id(), storyInfoList.get(i).getCreated_at(), storyInfoList.get(i).getContent()));
            }
        }

        return storyList;
    }

    @Override
    public void getContentPhotoData(final ContentAdapter.ContentViewHolder holder, final StoryInfo storyInfo) {
        String accessToken = user.getAccess_token();
        int storyId = storyInfo.getStory_id();

        storyServerConnection = new NetworkInterceptor(accessToken).getClientForStoryServer().create(StoryServerConnection.class);
        Call<ArrayList<Photo>> call_photo = storyServerConnection.family_content_photo_List(storyId);
        call_photo.enqueue(new Callback<ArrayList<Photo>>() {
            @Override
            public void onResponse(Call<ArrayList<Photo>> call, Response<ArrayList<Photo>> response) {
                if (response.isSuccessful()) {
                    ArrayList<Photo> photoList = response.body();
                    familyPresenter.onSuccessGetContentPhotoData(holder,photoList, storyInfo);
                } else {
                    familyPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Photo>> call, Throwable t) {
                log(t);
                familyPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void getContentLikeCount(final ContentAdapter.ContentViewHolder holder, StoryInfo storyInfo) {
        String accessToken = user.getAccess_token();
        int storyId = storyInfo.getStory_id();

        storyServerConnection = new NetworkInterceptor(accessToken).getClientForStoryServer().create(StoryServerConnection.class);
        Call<Integer> call_like_count = storyServerConnection.family_like_Count(storyId);
        call_like_count.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int count = response.body();
                    familyPresenter.onSuccessGetContentLikeCount(holder,count);
                } else {
                    familyPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                log(t);
                familyPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void getContentCommentCount(final ContentAdapter.ContentViewHolder holder, StoryInfo storyInfo) {
        String accessToken = user.getAccess_token();
        int storyId = storyInfo.getStory_id();

        storyServerConnection = new NetworkInterceptor(accessToken).getClientForStoryServer().create(StoryServerConnection.class);
        Call<Integer> call_comment_count = storyServerConnection.family_comment_Count(storyId);
        call_comment_count.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int count = response.body();
                    familyPresenter.onSuccessGetContentCommentCount(holder,count);
                } else {
                    familyPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                log(t);
                familyPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void getContentLikeCheck(final ContentAdapter.ContentViewHolder holder, final StoryInfo storyInfo) {
        String accessToken = user.getAccess_token();
        int userId = user.getId();
        int storyId = storyInfo.getStory_id();

        storyServerConnection = new NetworkInterceptor(accessToken).getClientForStoryServer().create(StoryServerConnection.class);
        Call<Integer> call_like_check = storyServerConnection.family_content_like_check(storyId, userId);
        call_like_check.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int check = response.body();
                    int position = storyInfo.getPosition();
                    familyPresenter.onSuccessGetContentLikeCheck(holder,check, position);
                } else {
                    familyPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                log(t);
                familyPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void setFamily(Family family) {
        this.family = family;
    }

    @Override
    public void setThreadContentAdd(final StoryInfo storyInfo) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                familyPresenter.onSuccessSetThreadContentAdd(storyInfo);
                try {
                    Thread.sleep(3500);
                } catch (InterruptedException e) {
                    log(e);
                }
                familyPresenter.onSuccessThreadRun();
            }
        }).start();
    }

    @Override
    public void setContentLikeCheck(final ContentAdapter.ContentViewHolder holder, final StoryInfo storyInfo) {
        String accessToken = user.getAccess_token();
        int userId = user.getId();
        int storyId = storyInfo.getStory_id();

        storyServerConnection = new NetworkInterceptor(accessToken).getClientForStoryServer().create(StoryServerConnection.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", String.valueOf(userId));

        Call<ResponseBody> call_like = storyServerConnection.family_content_like_up(storyId, map);
        call_like.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    int position = storyInfo.getPosition();
                    familyPresenter.onSuccessSetContentLikeCheck(holder,position);
                } else {
                    familyPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log(t);
                familyPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void setContentLikeUncheck(final ContentAdapter.ContentViewHolder holder, final StoryInfo storyInfo) {
        String accessToken = user.getAccess_token();
        int userId = user.getId();
        int storyId = storyInfo.getStory_id();

        storyServerConnection = new NetworkInterceptor(accessToken).getClientForStoryServer().create(StoryServerConnection.class);

        Call<ResponseBody> call_dislike = storyServerConnection.family_content_like_down(storyId, userId);
        call_dislike.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    int position = storyInfo.getPosition();
                    familyPresenter.onSuccessSetContentLikeUncheck(holder,position);
                } else {
                    familyPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log(t);
                familyPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void getFamilyDeleteCheck() {
        String accessToken = user.getAccess_token();
        int familyId = family.getId();

        familyServerConnection = new NetworkInterceptor(accessToken).getClientForFamilyServerAccessNull().create(FamilyServerConnection.class);
        Call<Family> call_family = familyServerConnection.family(familyId);
        call_family.enqueue(new Callback<Family>() {
            @Override
            public void onResponse(Call<Family> call, Response<Family> response) {
                if (response.isSuccessful()) {
                    Family family = response.body();
                    familyPresenter.onSuccessFamilyDeleteCheck(family);
                } else {
                    familyPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<Family> call, Throwable t) {
                log(t);
                familyPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public String getUserBirth(String birth) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(birth);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일생");
            return simpleDateFormat.format(date);
        } catch (ParseException e) {
            log(e);
            return null;
        }
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
