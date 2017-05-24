package com.demand.well_family.well_family.story.create.interactor.impl;

import android.net.Uri;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.Story;
import com.demand.well_family.well_family.dto.StoryInfo;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.StoryServerConnection;
import com.demand.well_family.well_family.repository.interceptor.HeaderInterceptor;
import com.demand.well_family.well_family.story.create.flag.CreateStoryCodeFlag;
import com.demand.well_family.well_family.story.create.interactor.CreateStoryInteractor;
import com.demand.well_family.well_family.story.create.presenter.CreateStoryPresenter;
import com.demand.well_family.well_family.story.create.presenter.impl.CreateStoryPresenterImpl;
import com.demand.well_family.well_family.util.ErrorUtil;
import com.demand.well_family.well_family.util.FileToBase64Util;
import com.demand.well_family.well_family.util.RealPathUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dev-0 on 2017-05-22.
 */

public class CreateStoryInteractorImpl implements CreateStoryInteractor {
    private CreateStoryPresenter createStoryPresenter;
    private static final Logger logger = LoggerFactory.getLogger(CreateStoryInteractorImpl.class);


    private User user;
    private Family family;

    private ArrayList<Uri> photoList;
    private ArrayList<String> pathList;
    private StoryServerConnection storyServerConnection;

    public CreateStoryInteractorImpl(CreateStoryPresenterImpl createStoryPresenter) {
        this.createStoryPresenter = createStoryPresenter;
        this.photoList = new ArrayList<>();
        this.pathList = new ArrayList<>();
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void setFamily(Family family) {
        this.family = family;
    }

    @Override
    public ArrayList<Uri> getPhotoList() {
        return photoList;
    }

    @Override
    public void setPhotoList(ArrayList<Uri> photoList) {
        this.photoList = photoList;
    }

    @Override
    public ArrayList<String> getPathList() {
        return pathList;
    }

    @Override
    public void setPathList(ArrayList<String> pathList) {
        this.pathList = pathList;
    }

    @Override
    public void setPhotoPath(RealPathUtil realPathUtil, Uri uri) {
        String path = null;
        try {
            path = realPathUtil.getRealPathFromURI_API19(uri);
        } catch (RuntimeException e) {
            log(e);
            path = realPathUtil.getRealPathFromURI_API11to18(uri);
        }
        pathList.add(path);
        photoList.add(uri);
    }

    @Override
    public void setStoryAdded(final String content) {
        final String accessToken = user.getAccess_token();
        final int userId = user.getId();
        final String userName = user.getName();
        final String userAvatar = user.getAvatar();
        final int familyId = family.getId();
        final String familyName = family.getName();

        new Thread(new Runnable() {
            @Override
            public void run() {

                HashMap<String, String> map = new HashMap<>();
                map.put("user_id", String.valueOf(userId));
                map.put("family_id", String.valueOf(familyId));
                map.put("family_name", familyName);
                map.put("content", content);

                storyServerConnection = new HeaderInterceptor(accessToken).getClientForStoryServer().create(StoryServerConnection.class);
                Call<Story> call_write_story = storyServerConnection.insert_story(map);
                call_write_story.enqueue(new Callback<Story>() {
                    @Override
                    public void onResponse(Call<Story> call, Response<Story> response) {
                        if (response.isSuccessful()) {
                            Story story = response.body();
                            int storyId = story.getId();
                            String createdAt = story.getCreated_at();
                            String content = story.getContent();
                            StoryInfo storyInfo = new StoryInfo(userId, userName, userAvatar, storyId, createdAt, content);

                            createStoryPresenter.onSuccessSetStoryAdded(storyInfo);

                        } else {
                            createStoryPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                        }
                    }

                    @Override
                    public void onFailure(Call<Story> call, Throwable t) {
                        log(t);
                        createStoryPresenter.onNetworkError(null);
                    }
                });
            }
        }).start();

    }

    @Override
    public void setPhotoAdded(FileToBase64Util fileToBase64Util, StoryInfo storyInfo, Uri photo, String path) {
        String accessToken = user.getAccess_token();
        int storyId = storyInfo.getStory_id();
        int sleepTime = 0;
        int photoSize = photoList.size();

        if (photoSize == 1) {
            sleepTime = 2500;
        } else {
            sleepTime = CreateStoryCodeFlag.ONE_PHOTO_FOR_TIME;
        }

        storyServerConnection = new HeaderInterceptor(accessToken).getClientForStoryServer().create(StoryServerConnection.class);
        final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), fileToBase64Util.addBase64Bitmap(fileToBase64Util.encodeImage(photo, path)));
        Call<ResponseBody> call_write_photo = storyServerConnection.insert_photos(storyId, requestBody);
        call_write_photo.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    //성공
                } else {
                    createStoryPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log(t);
                createStoryPresenter.onNetworkError(null);
            }
        });

        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            log(e);
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
