package com.demand.well_family.well_family.story.detail.interactor.impl;

import com.demand.well_family.well_family.dto.Comment;
import com.demand.well_family.well_family.dto.CommentInfo;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.dto.StoryInfo;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.StoryServerConnection;
import com.demand.well_family.well_family.repository.interceptor.NetworkInterceptor;
import com.demand.well_family.well_family.story.detail.interactor.StoryDetailInteractor;
import com.demand.well_family.well_family.story.detail.presenter.StoryDetailPresenter;
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
 * Created by Dev-0 on 2017-04-21.
 */

public class StoryDetailInteractorImpl implements StoryDetailInteractor {
    private StoryDetailPresenter storyDetailPresenter;

    private User user;
    private StoryInfo storyInfo;
    private boolean likeChecked;

    private boolean firstLikeChecked;


    private static final Logger logger = LoggerFactory.getLogger(StoryDetailInteractorImpl.class);
    private StoryServerConnection storyServerConnection;


    public StoryDetailInteractorImpl(StoryDetailPresenter storyDetailPresenter) {
        this.storyDetailPresenter = storyDetailPresenter;
        this.firstLikeChecked = false;
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
    public void setStoryInfo(StoryInfo storyInfo) {
        this.storyInfo = storyInfo;
    }

    @Override
    public StoryInfo getStoryInfo() {
        return this.storyInfo;
    }

    @Override
    public void setLikeChecked(boolean likeChecked) {
        this.likeChecked = likeChecked;
    }

    @Override
    public boolean getLikeChecked() {
        return this.likeChecked;
    }

    @Override
    public boolean isFirstLikeChecked() {
        return firstLikeChecked;
    }

    @Override
    public void setFirstLikeChecked(boolean firstLikeChecked) {
        this.firstLikeChecked = firstLikeChecked;
    }

    @Override
    public void getPhotoData() {
        String accessToken = user.getAccess_token();
        int storyId = storyInfo.getStory_id();

        storyServerConnection = new NetworkInterceptor(accessToken).getClientForStoryServer().create(StoryServerConnection.class);
        Call<ArrayList<Photo>> call_photo = storyServerConnection.family_content_photo_List(storyId);
        call_photo.enqueue(new Callback<ArrayList<Photo>>() {
            @Override
            public void onResponse(Call<ArrayList<Photo>> call, Response<ArrayList<Photo>> response) {
                if (response.isSuccessful()) {
                    ArrayList<Photo> photoList = response.body();
                    storyDetailPresenter.onSuccessGetPhotoData(photoList);
                } else {
                    storyDetailPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Photo>> call, Throwable t) {
                log(t);
                storyDetailPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void getCommentData() {
        String accessToken = user.getAccess_token();
        int storyId = storyInfo.getStory_id();

        storyServerConnection = new NetworkInterceptor(accessToken).getClientForStoryServer().create(StoryServerConnection.class);
        Call<ArrayList<CommentInfo>> call_family = storyServerConnection.family_detail_comment_List(storyId);
        call_family.enqueue(new Callback<ArrayList<CommentInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<CommentInfo>> call, Response<ArrayList<CommentInfo>> response) {
                if (response.isSuccessful()) {
                    ArrayList<CommentInfo> commentInfoList = response.body();
                    storyDetailPresenter.onSuccessGetCommentData(commentInfoList);
                } else {
                    storyDetailPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CommentInfo>> call, Throwable t) {
                log(t);
                storyDetailPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void setLikeCheck() {
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
                    storyDetailPresenter.onSuccessSetLikeCheck();
                } else {
                    storyDetailPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log(t);
                storyDetailPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void setLikeUncheck() {
        String accessToken = user.getAccess_token();
        int userId = user.getId();
        int storyId = storyInfo.getStory_id();

        storyServerConnection = new NetworkInterceptor(accessToken).getClientForStoryServer().create(StoryServerConnection.class);
        Call<ResponseBody> call_dislike = storyServerConnection.family_content_like_down(storyId, userId);
        call_dislike.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    storyDetailPresenter.onSuccessSetLikeUncheck();
                } else {
                    storyDetailPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log(t);
                storyDetailPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void setStoryHit() {
        String accessToken = user.getAccess_token();
        int storyId = storyInfo.getStory_id();

        storyServerConnection = new NetworkInterceptor(accessToken).getClientForStoryServer().create(StoryServerConnection.class);
        Call<Void> call_insert_story_hits = storyServerConnection.Insert_story_hit(storyId);
        call_insert_story_hits.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // not yet
                } else {
                    storyDetailPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                log(t);
                storyDetailPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void getStoryLikeCount() {
        String accessToken = user.getAccess_token();
        int storyId = storyInfo.getStory_id();

        storyServerConnection = new NetworkInterceptor(accessToken).getClientForStoryServer().create(StoryServerConnection.class);
        Call<Integer> call_like_count = storyServerConnection.family_like_Count(storyId);
        call_like_count.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int count = response.body();
                    storyDetailPresenter.onSuccessSetStoryLikeCount(count);
                } else {
                    storyDetailPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                log(t);
                storyDetailPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void setCommentAdded(String content) {
        String accessToken = user.getAccess_token();
        int userId = user.getId();
        int storyId = storyInfo.getStory_id();

        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", String.valueOf(userId));
        map.put("content", content);

        storyServerConnection = new NetworkInterceptor(accessToken).getClientForStoryServer().create(StoryServerConnection.class);
        Call<Comment> call_insert_comment = storyServerConnection.insert_comment(storyId, map);
        call_insert_comment.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (response.isSuccessful()) {
                    Comment comment = response.body();
                    storyDetailPresenter.onSuccessSetCommentAdded(comment);
                } else {
                    storyDetailPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                log(t);
                storyDetailPresenter.onNetworkError(null);
            }
        });
    }

    @Override
    public void getStoryCommentCount() {
        String accessToken = user.getAccess_token();
        int storyId = storyInfo.getStory_id();

        storyServerConnection = new NetworkInterceptor(accessToken).getClientForStoryServer().create(StoryServerConnection.class);
        Call<Integer> call_comment_count = storyServerConnection.family_comment_Count(storyId);
        call_comment_count.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int count = response.body();
                    storyDetailPresenter.onSuccessSetStoryCommentCount(count);
                } else {
                    storyDetailPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                log(t);
                storyDetailPresenter.onNetworkError(null);
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
