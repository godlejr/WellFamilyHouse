package com.demand.well_family.well_family.family.create.interactor;

import android.net.Uri;

import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.util.FileToBase64Util;
import com.demand.well_family.well_family.util.RealPathUtil;

/**
 * Created by Dev-0 on 2017-04-19.
 */

public interface CreateFamilyInteractor {

    void setUser(User user);

    void setPhotoUri(Uri uri);

    Uri getPhotoUri();

    void setPhotoPath(RealPathUtil realPathUtil);

    void setFamilyAdded(String familyName, String familyContent);

    void setFamilyAvatarAdded(int familyId, FileToBase64Util fileToBase64Util);

    void getFamilyData(int familyId);
}
