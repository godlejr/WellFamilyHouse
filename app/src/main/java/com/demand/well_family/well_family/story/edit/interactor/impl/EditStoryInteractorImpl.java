package com.demand.well_family.well_family.story.edit.interactor.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.demand.well_family.well_family.dto.Story;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.StoryServerConnection;
import com.demand.well_family.well_family.repository.interceptor.NetworkInterceptor;
import com.demand.well_family.well_family.story.edit.async.UrlToBitmapAsyncTask;
import com.demand.well_family.well_family.story.edit.flag.EditStoryCodeFlag;
import com.demand.well_family.well_family.story.edit.interactor.EditStoryInteractor;
import com.demand.well_family.well_family.story.edit.presenter.EditStoryPresenter;
import com.demand.well_family.well_family.story.edit.presenter.impl.EditStoryPresenterImpl;
import com.demand.well_family.well_family.util.ErrorUtil;
import com.demand.well_family.well_family.util.FileToBase64Util;
import com.demand.well_family.well_family.util.RealPathUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dev-0 on 2017-05-22.
 */

public class EditStoryInteractorImpl implements EditStoryInteractor {
    private EditStoryPresenter editStoryPresenter;

    private User user;
    private Story story;

    private Context context;

    private ArrayList<Uri> photoList;
    private ArrayList<URL> cdnPhotoList;
    private ArrayList<String> pathList;
    private ArrayList<Bitmap> bitmapPhotos;

    private static final Logger logger = LoggerFactory.getLogger(EditStoryInteractorImpl.class);
    private StoryServerConnection storyServerConnection;


    public EditStoryInteractorImpl(EditStoryPresenterImpl editStoryPresenter, Context context) {
        this.editStoryPresenter = editStoryPresenter;
        pathList = new ArrayList<>();
        photoList = new ArrayList<>();
        cdnPhotoList = new ArrayList<>();
        bitmapPhotos = new ArrayList<>();
        this.context = context;
    }


    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void setStory(Story story) {
        this.story = story;
    }

    @Override
    public Story getStory() {
        return this.story;
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
    public ArrayList<URL> getCdnPhotoList() {
        return cdnPhotoList;
    }

    @Override
    public void setCdnPhotoList(ArrayList<URL> cdnPhotoList) {
        this.cdnPhotoList = cdnPhotoList;
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
    public ArrayList<Bitmap> getBitmapPhotos() {
        return bitmapPhotos;
    }

    @Override
    public void setBitmapPhotos(ArrayList<Bitmap> bitmapPhotos) {
        this.bitmapPhotos = bitmapPhotos;
    }

    @Override
    public void setCdnPhotoAdded(String cloudFrontUrl, String namme, String ext) {
        try {
            cdnPhotoList.add(new URL(cloudFrontUrl + namme + "." + ext));
        } catch (MalformedURLException e) {
            log(e);
        }
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
    public void setPhotoForUri(FileToBase64Util fileToBase64Util, Uri uri, String path) {

        bitmapPhotos.add(fileToBase64Util.encodeImage(uri, path));

    }

    @Override
    public void setPhotoForUrl(final URL url) {
        UrlToBitmapAsyncTask urlToBitmapAsyncTask = new UrlToBitmapAsyncTask();

        try {
            Bitmap bitmap = urlToBitmapAsyncTask.execute(url).get();
            bitmapPhotos.add(bitmap);
        } catch (InterruptedException e) {
            log(e);
        } catch (ExecutionException e) {
            log(e);
        }
    }

    @Override
    public void setStoryEdited(final String content) {
        final String accessToken = user.getAccess_token();
        final int storyId = story.getId();

        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap<>();
                map.put("content", content);

                storyServerConnection = new NetworkInterceptor(accessToken).getClientForStoryServer().create(StoryServerConnection.class);
                Call<Void> call_write_story = storyServerConnection.update_story(storyId, map);
                call_write_story.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            editStoryPresenter.onSuccessSetStoryEdited();
                        } else {
                            editStoryPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        log(t);
                        editStoryPresenter.onNetworkError(null);
                    }
                });
            }
        }).start();
    }


    @Override
    public void setPhotoAdded(FileToBase64Util fileToBase64Util, Bitmap bitmap) {
        String accessToken = user.getAccess_token();
        int storyId = story.getId();
        int bitmapSize = bitmapPhotos.size();
        int sleepTime;

        if (bitmapSize == 1) {
            sleepTime = 2500;
        } else {
            sleepTime = EditStoryCodeFlag.ONE_PHOTO_FOR_TIME;
        }

        storyServerConnection = new NetworkInterceptor(accessToken).getClientForStoryServer().create(StoryServerConnection.class);

        final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), fileToBase64Util.addBase64Bitmap(bitmap));
        Call<ResponseBody> call_write_photo = storyServerConnection.insert_photos(storyId, requestBody);
        call_write_photo.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                } else {
                    editStoryPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log(t);
                editStoryPresenter.onNetworkError(null);
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
