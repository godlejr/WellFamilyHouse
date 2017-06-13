package com.demand.well_family.well_family.dialog.popup.photo.activity;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dialog.popup.photo.adapter.ViewPagerAdapter;
import com.demand.well_family.well_family.dialog.popup.photo.async.ImageDownloadAsyncTask;
import com.demand.well_family.well_family.dialog.popup.photo.presenter.PhotoPopupPresenter;
import com.demand.well_family.well_family.dialog.popup.photo.presenter.impl.PhotoPopupPresenterImpl;
import com.demand.well_family.well_family.dialog.popup.photo.view.PhotoPopupView;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.family.edit.flag.EditFamilyCodeFlag;
import com.demand.well_family.well_family.flag.PermissionFlag;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-01-23.
 */

public class PhotoPopupActivity extends Activity implements PhotoPopupView, View.OnClickListener {
    private PhotoPopupPresenter photoPopupPresenter;

    private ImageView iv_photo_popup_image;
    private ImageView iv_popup_close;
    private ImageView iv_popup_download;
    private LinearLayout ll_popup_top;
    private ViewPager photo_viewPager;
    private ViewPagerAdapter viewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_photo);
        getWindow().setLayout(android.view.WindowManager.LayoutParams.MATCH_PARENT, android.view.WindowManager.LayoutParams.MATCH_PARENT);


        ArrayList<Photo> photoList = (ArrayList<Photo>) getIntent().getSerializableExtra("photoList");
        int intentFlag = getIntent().getIntExtra("intent_flag", 0);

        photoPopupPresenter = new PhotoPopupPresenterImpl(this);
        photoPopupPresenter.onCreate(intentFlag, photoList);
    }

    @Override
    public void setPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PermissionFlag.WRITE_EXTERNAL_STORAGE_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case EditFamilyCodeFlag.WRITE_EXTERNAL_STORAGE_PERMISSION:
                photoPopupPresenter.onRequestPermissionsResultForWriteExternalStorage(grantResults);
                break;
        }
    }

    @Override
    public void init() {
        iv_photo_popup_image = (ImageView)findViewById(R.id.iv_photo_popup_image);
        iv_popup_close = (ImageView) findViewById(R.id.iv_popup_close);
        iv_popup_download = (ImageView) findViewById(R.id.iv_popup_download);
        photo_viewPager = (ViewPager) findViewById(R.id.photo_viewPager);
        ll_popup_top = (LinearLayout) findViewById(R.id.ll_popup_top);

        iv_popup_close.setOnClickListener(this);
        iv_popup_download.setOnClickListener(this);

        photoPopupPresenter.setPopupTitleBar();

        String avatar = getIntent().getStringExtra("avatar");
        photoPopupPresenter.setImage(avatar);
    }

    @Override
    public void setViewPagerAdapterInit(ArrayList<Photo> photoList) {
        viewPagerAdapter = new ViewPagerAdapter(getLayoutInflater(), photoList, photoPopupPresenter);
        photo_viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_popup_close:
                finish();
                break;

            case R.id.iv_popup_download:
                int viewpagerCurrentItem = photo_viewPager.getCurrentItem();
                photoPopupPresenter.onClickImageDownload(viewpagerCurrentItem);
                break;
        }
    }


    @Override
    public void setViewPagerIndicator(String position) {
        viewPagerAdapter.setViewPagerIndicator(position);
    }

    @Override
    public void showPopupTitleBar() {
        ll_popup_top.setVisibility(View.VISIBLE);
        ll_popup_top.bringToFront();
        ll_popup_top.invalidate();
        photoPopupPresenter.setPopupTitleBarVisibility();
    }

    @Override
    public void gonePopupTitleBar() {
        ll_popup_top.setVisibility(View.GONE);
        photoPopupPresenter.setPopupTitleBarVisibility();
    }

    @Override
    public void setCurrentItem(int position) {
        photo_viewPager.setCurrentItem(position);
    }

    @Override
    public void navigateToBack() {
        finish();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(PhotoPopupActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setImageDownload(ArrayList<Photo> photoList, String cloudFront, int position) {
        String imageURL = cloudFront + photoList.get(position).getName() + "." + photoList.get(position).getExt();
        ImageDownloadAsyncTask imageDownloadAsyncTask = new ImageDownloadAsyncTask(PhotoPopupActivity.this, photoList.get(position).getName() + "." + photoList.get(position).getExt());
        imageDownloadAsyncTask.execute(imageURL);
    }

    @Override
    public String getCloudFrontFamilyAvatar() {
        return getString(R.string.cloud_front_family_avatar);
    }

    @Override
    public String getCloudFrontUserAvatar() {
        return getString(R.string.cloud_front_user_avatar);
    }

    @Override
    public String getCloudFrontStoryImages() {
        return getString(R.string.cloud_front_stories_images);
    }

    @Override
    public String getCloudFrontFallDiagnosisStoryImages() {
        return getString(R.string.cloud_front_stories_environment);
    }


    @Override
    public void showImage(String avatar) {
        Glide.with(this).load(avatar).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_photo_popup_image);
    }

    @Override
    public void showImages() {
        int currentPhotoPosition = getIntent().getIntExtra("photo_position", 0);
        photoPopupPresenter.setViewPagerAdapterInit(currentPhotoPosition);
    }

    @Override
    public void goneViewPager() {
        photo_viewPager.setVisibility(View.GONE);
    }

    @Override
    public void showViewPager() {
        photo_viewPager.setVisibility(View.VISIBLE);
    }

    @Override
    public void gonePhoto() {
        iv_photo_popup_image.setVisibility(View.GONE);
    }

    @Override
    public void showPhoto() {
        iv_photo_popup_image.setVisibility(View.VISIBLE);
    }
}
