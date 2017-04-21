package com.demand.well_family.well_family.family.photo.presenter;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.util.APIErrorUtil;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-04-21.
 */

public interface PhotosPresenter {

    void onCreate(Family family);

    void onSuccessGetPhotoData(ArrayList<Photo> photoList);

    void onClickPhoto(ArrayList<Photo> photoList, int position);

    void getPhotoData();

    void onNetworkError(APIErrorUtil apiErrorUtil);
}
