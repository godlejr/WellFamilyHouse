package com.demand.well_family.well_family.family.photo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dialog.popup.photo.activity.PhotoPopupActivity;
import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.family.photo.adapter.PhotosAdapter;
import com.demand.well_family.well_family.family.photo.presenter.PhotosPresenter;
import com.demand.well_family.well_family.family.photo.presenter.impl.PhotosPresenterImpl;
import com.demand.well_family.well_family.family.photo.view.PhotosView;
import com.demand.well_family.well_family.flag.PhotoPopupINTENTFlag;

import java.util.ArrayList;

import static com.demand.well_family.well_family.main.login.activity.LoginActivity.finishList;

/**
 * Created by ㅇㅇ on 2017-01-19.
 */

public class PhotosActivity extends Activity implements PhotosView, View.OnClickListener {
    private PhotosPresenter photosPresenter;

    private RecyclerView rv_photos;
    private ProgressDialog progressDialog;

    //toolbar
    private Toolbar toolbar;
    private ImageView toolbar_back;
    private TextView toolbar_title;
    private View decorView;

    private PhotosAdapter photosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);

        Intent intent = getIntent();
        Family family = new Family();

        family.setId(intent.getIntExtra("family_id", 0));
        family.setName(intent.getStringExtra("family_name"));
        family.setContent(intent.getStringExtra("family_content"));
        family.setUser_id(intent.getIntExtra("family_user_id", 0));
        family.setAvatar(intent.getStringExtra("family_avatar"));
        family.setCreated_at(intent.getStringExtra("family_created_at"));

        photosPresenter = new PhotosPresenterImpl(this);
        photosPresenter.onCreate(family);
    }

    @Override
    public void init() {
        finishList.add(this);
        rv_photos = (RecyclerView) findViewById(R.id.rv_photos);
        photosPresenter.getPhotoData();
    }

    @Override
    public void setToolbar(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolBar);
        toolbar_back = (ImageView) toolbar.findViewById(R.id.toolbar_back);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);

        toolbar_back.setOnClickListener(this);
    }

    @Override
    public void showToolbarTitle(String message) {
        toolbar_title.setText(message);
    }

    @Override
    public View getDecorView() {
        if (decorView == null) {
            decorView = this.getWindow().getDecorView();
        }
        return decorView;
    }

    @Override
    public void setPhotosItem(ArrayList<Photo> photoList) {
        photosAdapter = new PhotosAdapter(PhotosActivity.this, photoList, R.layout.item_photo, photosPresenter);
        rv_photos.setAdapter(photosAdapter);
        rv_photos.setLayoutManager(new GridLayoutManager(PhotosActivity.this, 3));
    }

    @Override
    public void setPhotosItemSpace() {
        int spacing = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin) / 2;
        rv_photos.addItemDecoration(new PhotosAdapter.SpaceItemDecoration(spacing));
        rv_photos.setHasFixedSize(true);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.progress_dialog);
    }

    @Override
    public void goneProgressDialog() {
        progressDialog.dismiss();
    }


    @Override
    public void navigateToBack() {
        finish();
    }

    @Override
    public void navigateToPhotoPopupActivity(ArrayList<Photo> photoList, int position) {
        Intent intent = new Intent(PhotosActivity.this, PhotoPopupActivity.class);
        intent.putExtra("photoList", photoList);
        intent.putExtra("photo_position", position);
        intent.putExtra("intent_flag", PhotoPopupINTENTFlag.PHOTOSACTIVITY);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back:
                navigateToBack();
                break;

        }
    }
}


