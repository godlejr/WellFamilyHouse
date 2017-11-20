package com.demand.well_family.well_family.dialog.popup.photo.interactor.impl;

import com.demand.well_family.well_family.dialog.popup.photo.interactor.PhotoPopupInteractor;
import com.demand.well_family.well_family.dialog.popup.photo.presenter.PhotoPopupPresenter;
import com.demand.well_family.well_family.dto.Photo;

import java.util.ArrayList;


/**
 * Created by ㅇㅇ on 2017-04-19.
 */

public class PhotoPopupInteractorImpl implements PhotoPopupInteractor {
    private PhotoPopupPresenter photoPopupPresenter;

    private ArrayList<Photo> photoList;
    private String avatar;
    private int intentFlag;

    private String cloudFront;
    private int position;


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
    public String getAvatar() {
        return avatar;
    }

    @Override
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public void setPosition(int position) {
        this.position = position;
    }
}
