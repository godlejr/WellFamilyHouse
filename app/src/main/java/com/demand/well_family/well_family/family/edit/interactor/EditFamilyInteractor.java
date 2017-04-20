package com.demand.well_family.well_family.family.edit.interactor;

import android.net.Uri;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.util.FileToBase64Util;
import com.demand.well_family.well_family.util.RealPathUtil;

/**
 * Created by Dev-0 on 2017-04-20.
 */

public interface EditFamilyInteractor {

    void setUser(User user);

    void setFamily(Family family);

    void setPhotoUri(Uri uri);

    Uri getPhotoUri();

    void setPhotoPath(RealPathUtil realPathUtil);

    void setFamilyEdited(String familyName, String familyContent);

    void setFamilyAvatarEdited(FileToBase64Util fileToBase64Util);

    void getFamilyData();

    Family getFamily();
}
