package com.demand.well_family.well_family.family.edit.presenter;

import android.net.Uri;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.util.APIErrorUtil;

/**
 * Created by Dev-0 on 2017-04-20.
 */

public interface EditFamilyPresenter {

    void onCreate(Family family);

    void onClickEditFamily(String familyName, String familyContent);

    void onSuccessSetFamilyEdited();

    void onSuccessSetFamilyAvatarEdited();

    void onSuccessGetFamilyData(Family family);

    void onActivityResultForPhotoUriResultOk(Uri uri);

    void onNetworkError(APIErrorUtil apiErrorUtil);

    void onRequestPermissionsResultForReadExternalStorage(int[] grantResults);
}
