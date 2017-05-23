package com.demand.well_family.well_family.story.edit.presenter;

import android.net.Uri;

import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.dto.Story;
import com.demand.well_family.well_family.util.APIErrorUtil;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-05-22.
 */

public interface EditStoryPresenter {


    void onCreate(Story story);

    void onBackPressed();

    void onRequestPermissionsResultForReadExternalStorage(int[] grantResults);

    void onLoadData(ArrayList<Photo> prevPhotoList, String cloudFrontUrl);

    void onClickPhotoAdd(int sdkInt, int kitkat);

    void onSuccessSetStoryEdited();

    void onClickPhotoDelete(int position,int flag);

    void onActivityResultForPhotoUriResultOk(Uri uri);

    void onClickStoryEdit(String content);

    void onNetworkError(APIErrorUtil apiErrorUtil);


}
