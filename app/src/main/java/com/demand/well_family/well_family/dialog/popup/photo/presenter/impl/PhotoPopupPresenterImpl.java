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
import com.demand.well_family.well_family.flag.PhotoPopupINTENTFlag;
import com.demand.well_family.well_family.util.PreferenceUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-19.
 */

public class PhotoPopupPresenterImpl implements PhotoPopupPresenter {
    private PhotoPopupView photoPopupView;
    private PreferenceUtil preferenceUtil;
    private PhotoPopupInteractor photoPopupInteractor;

    private boolean visibility = true;

    public PhotoPopupPresenterImpl(Context context) {
        this.photoPopupView = (PhotoPopupView) context;
        this.photoPopupInteractor = new PhotoPopupInteractorImpl(this);
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate(int intentFlag, ArrayList<Photo> photoList, String fromActivity) {
        photoPopupInteractor.setFromActivity(fromActivity);
        photoPopupInteractor.setIntentFlag(intentFlag);
        photoPopupInteractor.setPhotoList(photoList);

        photoPopupView.setPermission();
        photoPopupView.init();
    }

    @Override
    public void onRequestPermissionsResultForWriteExternalStorage(int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            photoPopupView.showMessage("권한을 허가해주세요.");
        }
    }

    @Override
    public void setViewPagerAdapterInit(int currentPhotoPosition) {
        ArrayList<Photo> photoList = photoPopupInteractor.getPhotoList();
        photoPopupView.setViewPagerAdapterInit(photoList);
        photoPopupView.setCurrentItem(currentPhotoPosition);
    }

    @Override
    public String getImageURL(int position) {
        int intentFlag = photoPopupInteractor.getIntentFlag();
        ArrayList<Photo> photoList = photoPopupInteractor.getPhotoList();
        String cloudFront = null;

        if (intentFlag == PhotoPopupINTENTFlag.FAMILYACTIVITY) {
            cloudFront = photoPopupView.getCloudFrontFamilyAvatar();
        }

        if (intentFlag == PhotoPopupINTENTFlag.USERACTIVITY) {
            cloudFront = photoPopupView.getCloudFrontUserAvatar();
        }

        if (intentFlag == PhotoPopupINTENTFlag.PHOTOSACTIVITY || intentFlag == PhotoPopupINTENTFlag.STORYDETAILACTIVITY) {
            cloudFront = photoPopupView.getCloudFrontStoryImages();
        }

        photoPopupInteractor.setCloudFront(cloudFront);
        String imageURL = cloudFront + photoList.get(position).getName() + "." + photoList.get(position).getExt();
        return imageURL;
    }

    @Override
    public void setViewPagerIndicator(int position) {
        int intentFlag = photoPopupInteractor.getIntentFlag();
        ArrayList<Photo> photoList = photoPopupInteractor.getPhotoList();

        if (intentFlag == PhotoPopupINTENTFlag.FAMILYACTIVITY || intentFlag == PhotoPopupINTENTFlag.USERACTIVITY) {
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
        ArrayList<Photo> photoList = photoPopupInteractor.getPhotoList();
        String cloudFront = photoPopupInteractor.getCloudFront();

        photoPopupView.setImageDownload(photoList, cloudFront, position);
    }

    @Override
    public void setImage(String avatar) {
        int intentFlag = photoPopupInteractor.getIntentFlag();
        String cloudFront = null;

        if (intentFlag == PhotoPopupINTENTFlag.FAMILYACTIVITY) {
            cloudFront = photoPopupView.getCloudFrontFamilyAvatar();
        }

        if (intentFlag == PhotoPopupINTENTFlag.USERACTIVITY) {
            cloudFront = photoPopupView.getCloudFrontUserAvatar();
        }

        if (intentFlag == PhotoPopupINTENTFlag.PHOTOSACTIVITY || intentFlag == PhotoPopupINTENTFlag.STORYDETAILACTIVITY) {
            cloudFront = photoPopupView.getCloudFrontStoryImages();
        }

        if (avatar != null) {
            photoPopupView.showPhoto();
            photoPopupView.goneViewPager();
            photoPopupView.showImage(cloudFront + avatar);
        } else {
            photoPopupView.showViewPager();
            photoPopupView.gonePhoto();
            photoPopupView.showImages();
        }
    }
}
