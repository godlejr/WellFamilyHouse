package com.demand.well_family.well_family.family.photo.interactor.impl;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.family.photo.interactor.PhotosInteractor;
import com.demand.well_family.well_family.family.photo.presenter.PhotosPresenter;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.FamilyServerConnection;
import com.demand.well_family.well_family.repository.interceptor.NetworkInterceptor;
import com.demand.well_family.well_family.util.ErrorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dev-0 on 2017-04-21.
 */

public class PhotosInteractorImpl implements PhotosInteractor {
    private PhotosPresenter photosPresenter;

    private Family family;
    private User user;

    private FamilyServerConnection familyServerConnection;

    private static final Logger logger = LoggerFactory.getLogger(PhotosInteractorImpl.class);


    public PhotosInteractorImpl(PhotosPresenter photosPresenter) {
        this.photosPresenter = photosPresenter;
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
    public void getPhotoData() {
        final String accessToken = user.getAccess_token();
        final int familyId = family.getId();
        new Thread(new Runnable() {
            @Override
            public void run() {
                familyServerConnection = new NetworkInterceptor(accessToken).getClientForFamilyServer().create(FamilyServerConnection.class);

                Call<ArrayList<Photo>> call_photo = familyServerConnection.family_photo_List(familyId);
                call_photo.enqueue(new Callback<ArrayList<Photo>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Photo>> call, Response<ArrayList<Photo>> response) {
                        if (response.isSuccessful()) {
                            ArrayList<Photo> photoList = response.body();
                            photosPresenter.onSuccessGetPhotoData(photoList);
                        } else {
                            photosPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Photo>> call, Throwable t) {
                        log(t);
                        photosPresenter.onNetworkError(null);
                    }
                });

            }
        }).start();
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
