package com.demand.well_family.well_family.dialog.popup.photo.interactor.impl;

import com.demand.well_family.well_family.dialog.popup.photo.interactor.PhotoPopupInteractor;
import com.demand.well_family.well_family.dialog.popup.photo.presenter.PhotoPopupPresenter;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.dto.SelfDiagnosisCategory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.repository.FallDiagnosisServerConnection;
import com.demand.well_family.well_family.repository.interceptor.HeaderInterceptor;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ㅇㅇ on 2017-04-19.
 */

public class PhotoPopupInteractorImpl implements PhotoPopupInteractor {
    private PhotoPopupPresenter photoPopupPresenter;

    private ArrayList<Photo> photoList;
    private int intentFlag;
    private String cloudFront;
    private String fromActivity;

    public PhotoPopupInteractorImpl(PhotoPopupPresenter photoPopupPresenter) {
        this.photoPopupPresenter = photoPopupPresenter;
    }

    @Override
    public int getIntentFlag() {
        return this.intentFlag;
    }

    @Override
    public void setIntentFlag(int intentFlag) {
        this.intentFlag = intentFlag;
    }

    @Override
    public ArrayList<Photo> getPhotoList() {
        return this.photoList;
    }

    @Override
    public void setPhotoList(ArrayList<Photo> photoList) {
        this.photoList = photoList;
    }

    @Override
    public String getCloudFront() {
        return this.cloudFront;
    }

    @Override
    public void setCloudFront(String cloudFront) {
        this.cloudFront = cloudFront;
    }

    @Override
    public String getFromActivity() {
        return this.fromActivity;
    }

    @Override
    public void setFromActivity(String fromActivity) {
        this.fromActivity = fromActivity;
    }



}
