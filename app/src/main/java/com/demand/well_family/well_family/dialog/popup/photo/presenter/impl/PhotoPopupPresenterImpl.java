package com.demand.well_family.well_family.dialog.popup.photo.presenter.impl;

import android.content.Context;
import android.content.pm.PackageManager;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dialog.popup.photo.adapter.ViewPagerAdapter;
import com.demand.well_family.well_family.dialog.popup.photo.async.ImageDownloadAsyncTask;
import com.demand.well_family.well_family.dialog.popup.photo.interactor.PhotoPopupInteractor;
import com.demand.well_family.well_family.dialog.popup.photo.interactor.impl.PhotoPopupInteractorImpl;
import com.demand.well_family.well_family.dialog.popup.photo.presenter.PhotoPopupPresenter;
import com.demand.well_family.well_family.dialog.popup.photo.view.PhotoPopupView;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.flag.PermissionFlag;
import com.demand.well_family.well_family.util.PreferenceUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-19.
 */

public class PhotoPopupPresenterImpl implements PhotoPopupPresenter {
    private PhotoPopupView photoPopupView;
    private PreferenceUtil preferenceUtil;
    private PhotoPopupInteractor photoPopupInteractor;


    private  String cloud_front;
    private Context context;
    private String fromActivity;
    private ArrayList<Photo> photoList;
    private boolean visibility = true;
    private ImageDownloadAsyncTask imageDownloadAsyncTask;

    public PhotoPopupPresenterImpl(Context context) {
        this.photoPopupView = (PhotoPopupView) context;
        this.photoPopupInteractor = new PhotoPopupInteractorImpl(this);
        this.preferenceUtil = new PreferenceUtil(context);
        this.context = context;
    }

    @Override
    public void onCreate() {
        photoPopupView.checkPermission();
        photoPopupView.init();
        photoPopupView.setViewPagerAdapterInit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, int[] grantResults) {
        if (requestCode == PermissionFlag.WRITE_EXTERNAL_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                photoPopupView.showMessage("권한을 허가해주세요.");
            }
        }
    }

    @Override
    public void setViewPagerAdapterInit(String fromActivity, ArrayList<Photo> photoList, int currentPhotoPosition) {
        this.fromActivity = fromActivity;
        this.photoList = photoList;

        photoPopupView.setCurrentItem(currentPhotoPosition);
    }

    @Override
    public void setViewPagerAdapter(ViewPagerAdapter viewPagerAdapter) {
        photoPopupView.setViewPagerAdapter(viewPagerAdapter);
    }

    @Override
    public String getImageURL(int position) {
        switch (fromActivity) {
            case "FamilyActivity":
                cloud_front = context.getString(R.string.cloud_front_family_avatar);
                break;
            case "UserActivity":
                cloud_front = context.getString(R.string.cloud_front_user_avatar);
                break;
            case "PhotosActivity":
            case "StoryDetailActivity":
                cloud_front = context.getString(R.string.cloud_front_stories_images);
                break;
        }

        String imageURL = cloud_front + photoList.get(position).getName() + "." + photoList.get(position).getExt();
        return imageURL;
    }

    @Override
    public void setViewPagerIndicator(int position) {
        if (fromActivity.equals("FamilyActivity") || fromActivity.equals("UserActivity")) {
            photoPopupView.setViewPagerIndicator("");
        } else {
            String viewPagerPosition = (position + 1) + " / " + photoList.size();
            photoPopupView.setViewPagerIndicator(viewPagerPosition);
        }
    }

    @Override
    public void setPopupTitleBar() {
        if (visibility) {
            photoPopupView.showPopupTitleBar();
        } else {
            photoPopupView.gonePopupTitleBar();
        }
    }

    @Override
    public void setPopupTitleBarVisibility() {
        visibility = !visibility;
    }


    @Override
    public void onClickImageDownload(int position) {
        String imageURL = cloud_front + photoList.get(position).getName() + "." + photoList.get(position).getExt();
        imageDownloadAsyncTask = new ImageDownloadAsyncTask(context, photoList.get(position).getName() + "." + photoList.get(position).getExt());
        imageDownloadAsyncTask.execute(imageURL);
    }

}
