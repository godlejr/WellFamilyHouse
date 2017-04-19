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

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dialog.popup.photo.adapter.ViewPagerAdapter;
import com.demand.well_family.well_family.dialog.popup.photo.presenter.PhotoPopupPresenter;
import com.demand.well_family.well_family.dialog.popup.photo.presenter.impl.PhotoPopupPresenterImpl;
import com.demand.well_family.well_family.dialog.popup.photo.view.PhotoPopupView;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.flag.PermissionFlag;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-01-23.
 */

public class PhotoPopupActivity extends Activity implements PhotoPopupView, View.OnClickListener {
    private PhotoPopupPresenter photoPopupPresenter;

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

        photoPopupPresenter = new PhotoPopupPresenterImpl(this);
        photoPopupPresenter.onCreate();
    }

    @Override
    public void checkPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PermissionFlag.WRITE_EXTERNAL_STORAGE_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        photoPopupPresenter.onRequestPermissionsResult(requestCode, grantResults);
    }

    @Override
    public void init() {
        iv_popup_close = (ImageView) findViewById(R.id.iv_popup_close);
        iv_popup_download = (ImageView) findViewById(R.id.iv_popup_download);
        photo_viewPager = (ViewPager) findViewById(R.id.photo_viewPager);
        ll_popup_top = (LinearLayout) findViewById(R.id.ll_popup_top);

        iv_popup_close.setOnClickListener(this);
        iv_popup_download.setOnClickListener(this);

        photoPopupPresenter.setPopupTitleBar();
    }

    @Override
    public void setViewPagerAdapterInit() {
        ArrayList<Photo> photoList = (ArrayList<Photo>) getIntent().getSerializableExtra("photoList");
        int currentPhotoPosition = getIntent().getIntExtra("photo_position", 0);
        String fromActivity = getIntent().getStringExtra("from");

        viewPagerAdapter = new ViewPagerAdapter(getLayoutInflater(), photoList, photoPopupPresenter);
        photoPopupPresenter.setViewPagerAdapter(viewPagerAdapter);

        photoPopupPresenter.setViewPagerAdapterInit(fromActivity, photoList, currentPhotoPosition);
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
    public void setViewPagerAdapter(ViewPagerAdapter viewPagerAdapter) {
        photo_viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void setViewPagerIndicator(String position) {
        viewPagerAdapter.setViewPagerIndicator(position);
    }

    @Override
    public void setPopupTitleBar() {
        photoPopupPresenter.setPopupTitleBar();
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


}
