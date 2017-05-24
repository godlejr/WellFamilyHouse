package com.demand.well_family.well_family.story.edit.interactor;

import android.graphics.Bitmap;
import android.net.Uri;

import com.demand.well_family.well_family.dto.Story;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.util.FileToBase64Util;
import com.demand.well_family.well_family.util.RealPathUtil;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-05-22.
 */

public interface EditStoryInteractor {
    void setUser(User user);

    void setStory(Story story);

    Story getStory();

    ArrayList<Uri> getPhotoList();

    void setPhotoList(ArrayList<Uri> photoList);

    ArrayList<URL> getCdnPhotoList();

    void setCdnPhotoList(ArrayList<URL> cdnPhotoList);

    ArrayList<String> getPathList();

    void setPathList(ArrayList<String> pathList);

    ArrayList<Bitmap> getBitmapPhotos();

    void setBitmapPhotos(ArrayList<Bitmap> bitmapPhotos);

    void setCdnPhotoAdded(String cloudFrontUrl,String namme, String ext);

    void setPhotoPath(RealPathUtil realPathUtil, Uri uri);

    void setPhotoForUri(FileToBase64Util fileToBase64Util, Uri uri,String path);

    void setPhotoForUrl(final URL url);


    void setStoryEdited(String content);

    void setPhotoAdded(FileToBase64Util fileToBase64Util, Bitmap bitmap);
}
