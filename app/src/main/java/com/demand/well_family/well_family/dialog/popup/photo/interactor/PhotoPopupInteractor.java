package com.demand.well_family.well_family.dialog.popup.photo.interactor;

import com.demand.well_family.well_family.dialog.popup.photo.presenter.PhotoPopupPresenter;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.dto.User;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-19.
 */

public interface PhotoPopupInteractor {
    int getIntentFlag();

    void setIntentFlag(int intentFlag);

    ArrayList<Photo> getPhotoList();

    void setPhotoList(ArrayList<Photo> photoList);

    String getCloudFront();

    void setCloudFront(String cloudFront);


}
