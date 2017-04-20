package com.demand.well_family.well_family.family.create.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.family.base.activity.FamilyActivity;
import com.demand.well_family.well_family.family.create.flag.CreateFamilyCodeFlag;
import com.demand.well_family.well_family.family.create.presenter.CreateFamilyPresenter;
import com.demand.well_family.well_family.family.create.presenter.impl.CreateFamilyPresenterImpl;
import com.demand.well_family.well_family.family.create.view.CreateFamilyView;
import com.demand.well_family.well_family.flag.S3FileFlag;

/**
 * Created by ㅇㅇ on 2017-02-12.
 */

public class CreateFamilyActivity extends Activity implements CreateFamilyView, View.OnClickListener {
    private CreateFamilyPresenter createFamilyPresenter;

    private ImageButton ib_create_family;
    private ImageView iv_create_family_img;
    private EditText et_create_family_name, et_create_family_introduce;
    private ProgressDialog progressDialog;

    //toolbar
    private Toolbar toolbar;
    private ImageView toolbar_back;
    private TextView toolbar_title;
    private Button toolbar_complete;
    private View decorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_create_family);
        getWindow().setLayout(android.view.WindowManager.LayoutParams.MATCH_PARENT, android.view.WindowManager.LayoutParams.MATCH_PARENT);

        createFamilyPresenter = new CreateFamilyPresenterImpl(this);
        createFamilyPresenter.onCreate();

    }

    @Override
    public void init() {
        et_create_family_introduce = (EditText) findViewById(R.id.et_create_family_introduce);
        et_create_family_name = (EditText) findViewById(R.id.et_create_family_name);
        iv_create_family_img = (ImageView) findViewById(R.id.iv_create_family_img);
        ib_create_family = (ImageButton) findViewById(R.id.ib_create_family);

        Glide.with(this).load(getString(R.string.cloud_front_family_avatar) + S3FileFlag.FAMILY_AVATAR_DEFAULT).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_create_family_img);
        ib_create_family.setOnClickListener(this);
    }

    public void setToolbar(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolBar);
        toolbar_back = (ImageView) toolbar.findViewById(R.id.toolbar_back);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_complete = (Button) toolbar.findViewById(R.id.toolbar_complete);

        toolbar_back.setOnClickListener(this);
        toolbar_complete.setOnClickListener(this);
    }

    @Override
    public void showToolbarTitle(String message) {
        toolbar_title.setText(message);
    }

    @Override
    public void setPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CreateFamilyCodeFlag.READ_EXTERNAL_STORAGE_PERMISSION);
    }

    @Override
    public View getDecorView() {
        if (decorView == null) {
            decorView = this.getWindow().getDecorView();
        }
        return decorView;
    }

    @Override
    public void showFamilyAvatar(Uri uri) {
        Glide.with(CreateFamilyActivity.this).load(uri).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_create_family_img);
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
    public void navigateToBackground() {
        finish();
    }

    @Override
    public void navigateToMediaStore() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, CreateFamilyCodeFlag.MEDIA_STORE_REQUEST);
    }

    @Override
    public void navigateToFamilyActivity(Family family) {
        Intent intent = new Intent(this, FamilyActivity.class);

        intent.putExtra("family_id", family.getId());
        intent.putExtra("family_name", family.getName());
        intent.putExtra("family_content", family.getContent());
        intent.putExtra("family_avatar", family.getAvatar());
        intent.putExtra("family_user_id", family.getUser_id());
        intent.putExtra("family_created_at", family.getCreated_at());

        startActivity(intent);
        finish();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case CreateFamilyCodeFlag.READ_EXTERNAL_STORAGE_PERMISSION:
                createFamilyPresenter.onRequestPermissionsResultForReadExternalStorage(grantResults);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CreateFamilyCodeFlag.MEDIA_STORE_REQUEST:
                switch (resultCode) {
                    case  CreateFamilyCodeFlag.RESULT_OK:
                        Uri uri = data.getData();
                        createFamilyPresenter.onActivityResultForPhotoUriResultOk(uri);
                        break;
                }
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back:
                navigateToBackground();
                break;
            case R.id.ib_create_family:
                navigateToMediaStore();
                break;

            case R.id.toolbar_complete:
                String familyName = et_create_family_name.getText().toString();
                String familyContent = et_create_family_introduce.getText().toString();

                createFamilyPresenter.onClickCreateFamily(familyName, familyContent);
                break;
        }
    }
}
