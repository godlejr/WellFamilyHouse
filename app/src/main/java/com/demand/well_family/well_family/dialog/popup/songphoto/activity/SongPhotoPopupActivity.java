package com.demand.well_family.well_family.dialog.popup.songphoto.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dialog.popup.songphoto.adapter.ViewPagerAdapter;
import com.demand.well_family.well_family.dialog.popup.songphoto.async.ImageDownloadAsyncTask;
import com.demand.well_family.well_family.dialog.popup.songphoto.presenter.SongPhotoPresenter;
import com.demand.well_family.well_family.dialog.popup.songphoto.presenter.impl.SongPhotoPresenterImpl;
import com.demand.well_family.well_family.dialog.popup.songphoto.view.SongPhotoView;
import com.demand.well_family.well_family.dto.SongPhoto;
import com.demand.well_family.well_family.flag.PermissionFlag;
import com.demand.well_family.well_family.main.base.adapter.viewpager.ViewPageAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-01-23.
 */

public class SongPhotoPopupActivity extends Activity implements SongPhotoView, View.OnClickListener {
    private SongPhotoPresenter songPhotoPresenter;

    private LinearLayout ll_popup_top;
    private ImageView  iv_popup_close, iv_popup_download;
    private ViewPager photo_viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_photo);

        songPhotoPresenter = new SongPhotoPresenterImpl(this);
        songPhotoPresenter.onCreate();
    }


    @Override
    public void init() {
        ll_popup_top = (LinearLayout) findViewById(R.id.ll_popup_top);
        iv_popup_close = (ImageView) findViewById(R.id.iv_popup_close);
        iv_popup_download = (ImageView) findViewById(R.id.iv_popup_download);
        ArrayList<SongPhoto> photoList = (ArrayList<SongPhoto>) getIntent().getSerializableExtra("photoList");
        photo_viewPager = (ViewPager) findViewById(R.id.photo_viewPager);

        ll_popup_top.setVisibility(View.VISIBLE);
        ll_popup_top.bringToFront();
        ll_popup_top.invalidate();

        int currentPhotoPosition = getIntent().getIntExtra("photo_position", 0);
        photo_viewPager.setAdapter(new ViewPagerAdapter(getLayoutInflater(), photoList, songPhotoPresenter));
        photo_viewPager.setCurrentItem(currentPhotoPosition);

        iv_popup_close.setOnClickListener(this);
        iv_popup_download.setOnClickListener(this);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(SongPhotoPopupActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPopupTop() {
        ll_popup_top.setVisibility(View.VISIBLE);
    }

    @Override
    public void gonePopupTop() {
        ll_popup_top.setVisibility(View.GONE);
    }

    @Override
    public int getPopupTopVisibility() {
        return ll_popup_top.getVisibility();
    }

    @Override
    public void checkPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PermissionFlag.WRITE_EXTERNAL_STORAGE_PERMISSION);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_popup_close:
                finish();
                break;

            case R.id.iv_popup_download:
                int viewpagerCurrentItem = photo_viewPager.getCurrentItem();
                ArrayList<SongPhoto> photoList = (ArrayList<SongPhoto>) getIntent().getSerializableExtra("photoList");
                String imageURL = getString(R.string.cloud_front_song_stories_images) + photoList.get(viewpagerCurrentItem).getName() + "." + photoList.get(viewpagerCurrentItem).getExt();

                ImageDownloadAsyncTask imageDownload = new ImageDownloadAsyncTask(photoList.get(viewpagerCurrentItem).getName() + "." + photoList.get(viewpagerCurrentItem).getExt(), SongPhotoPopupActivity.this);
                imageDownload.execute(imageURL);
                break;
        }
    }
}
