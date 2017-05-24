package com.demand.well_family.well_family.story.create.presenter;

import android.net.Uri;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.StoryInfo;
import com.demand.well_family.well_family.util.APIErrorUtil;

/**
 * Created by Dev-0 on 2017-05-22.
 */

public interface CreateStoryPresenter {

    void onCreate(Family family);

    void onLoadData();

    void onClickPhotoAdd(int sdkInt, int kitkat);

    void onRequestPermissionsResultForReadExternalStorage(int[] grantResults);

    void onActivityResultForPhotoUriResultOk(Uri uri);

    void onSuccessSetStoryAdded(StoryInfo storyInfo);


    void onClickStoryAdd(String content);

    void onNetworkError(APIErrorUtil apiErrorUtil);

    void onClickPhotoDelete(int position);


}
