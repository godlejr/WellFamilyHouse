package com.demand.well_family.well_family.family.create.presenter;

import android.net.Uri;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.util.APIErrorUtil;

/**
 * Created by Dev-0 on 2017-04-19.
 */

public interface CreateFamilyPresenter {

    void onCreate();

    void onSuccessGetFamilyData(Family family);


    void onSuccessSetFamilyAvatarAdded(int familyId);

    void onSuccessSetFamilyAdded(int familyId);

    void onClickCreateFamily(String familyName, String familyContent);

    void onActivityResultForPhotoUriResultOk(Uri uri);

    void onNetworkError(APIErrorUtil apiErrorUtil);

    void onRequestPermissionsResultForReadExternalStorage(int[] grantResults);

}
