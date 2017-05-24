package com.demand.well_family.well_family.story.create.interactor;

import android.net.Uri;

import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.StoryInfo;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.util.FileToBase64Util;
import com.demand.well_family.well_family.util.RealPathUtil;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-05-22.
 */

public interface CreateStoryInteractor {
    void setUser(User user);

    void setFamily(Family family);

    ArrayList<Uri> getPhotoList();

    void setPhotoList(ArrayList<Uri> photoList);

    ArrayList<String> getPathList();

    void setPathList(ArrayList<String> pathList);

    void setPhotoPath(RealPathUtil realPathUtil, Uri uri);

    void setStoryAdded(String content);

    void setPhotoAdded(FileToBase64Util fileToBase64Util, StoryInfo storyInfo, Uri photo, String path);
}
