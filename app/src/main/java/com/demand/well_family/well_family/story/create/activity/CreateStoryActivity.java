package com.demand.well_family.well_family.story.create.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.StoryInfo;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.story.create.adapter.PhotoAdapter;
import com.demand.well_family.well_family.story.create.flag.CreateStoryCodeFlag;
import com.demand.well_family.well_family.story.create.presenter.CreateStoryPresenter;
import com.demand.well_family.well_family.story.create.presenter.impl.CreateStoryPresenterImpl;
import com.demand.well_family.well_family.story.create.view.CreateStoryView;

import java.util.ArrayList;

import static com.demand.well_family.well_family.main.login.activity.LoginActivity.finishList;

/**
 * Created by ㅇㅇ on 2017-01-20.
 */
//
public class CreateStoryActivity extends Activity implements CreateStoryView, View.OnClickListener {
    private CreateStoryPresenter createStoryPresenter;

    private RecyclerView rv_write_image_upload;
    private EditText et_content;
    private Button btn_write_photo_upload;
    private Button btn_write;

    private TextView tv_write_user_name;
    private ImageView iv_write_user_avatar;

    private PhotoAdapter photoAdapter;
    private ProgressDialog progressDialog;

    //toolbar
    private Toolbar toolbar;
    private ImageView toolbar_back;
    private TextView toolbar_title;
    private View decorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        finishList.add(this);

        Family family = new Family();
        family.setId(getIntent().getIntExtra("family_id", 0));
        family.setName(getIntent().getStringExtra("family_name"));
        family.setContent(getIntent().getStringExtra("family_content"));
        family.setAvatar(getIntent().getStringExtra("family_avatar"));
        family.setUser_id(getIntent().getIntExtra("family_user_id", 0));
        family.setCreated_at(getIntent().getStringExtra("family_created_at"));

        createStoryPresenter = new CreateStoryPresenterImpl(this);
        createStoryPresenter.onCreate(family);
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
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPhotoAdapterNotifyDataChanged() {
        photoAdapter.notifyDataSetChanged();
    }

    @Override
    public void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.progress_dialog);
    }

    @Override
    public void setProgressDialog(int position) {
        progressDialog.setProgress(position);

    }

    @Override
    public void goneProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void setPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CreateStoryCodeFlag.READ_EXTERNAL_STORAGE_PERMISSION);
    }

    @Override
    public void setPhotoAdapterDelete(int position) {
        photoAdapter.setPhotoDelete(position);
    }

    @Override
    public void setPhotoItem(ArrayList<Uri> photoList) {
        photoAdapter = new PhotoAdapter(photoList, this, R.layout.item_write_upload_image, createStoryPresenter);
        rv_write_image_upload.setAdapter(photoAdapter);
        rv_write_image_upload.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public void navigateToMultiMediaStore() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Photo"), CreateStoryCodeFlag.PICK_PHOTO);
    }

    @Override
    public void navigateToMediaStore() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, CreateStoryCodeFlag.PICK_PHOTO);
    }

    @Override
    public void navigateToBack() {
        finish();
    }

    @Override
    public void navigateToBack(StoryInfo storyInfo) {
        Intent intent = getIntent();
        intent.putExtra("storyInfo", storyInfo);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case CreateStoryCodeFlag.READ_EXTERNAL_STORAGE_PERMISSION:
                createStoryPresenter.onRequestPermissionsResultForReadExternalStorage(grantResults);
                break;
        }
    }

    @Override
    public void init(User user) {
        et_content = (EditText) findViewById(R.id.et_write);
        btn_write = (Button) findViewById(R.id.btn_write);
        btn_write_photo_upload = (Button) findViewById(R.id.btn_write_photo_upload);
        rv_write_image_upload = (RecyclerView) findViewById(R.id.rv_write_image_upload);

        createStoryPresenter.onLoadData();

        tv_write_user_name = (TextView) findViewById(R.id.tv_write_user_name);
        iv_write_user_avatar = (ImageView) findViewById(R.id.iv_write_user_avatar);
        tv_write_user_name.setText(user.getName());
        Glide.with(this).load(getString(R.string.cloud_front_user_avatar) + user.getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_write_user_avatar);


        btn_write.setOnClickListener(this);
        btn_write_photo_upload.setOnClickListener(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CreateStoryCodeFlag.PICK_PHOTO:
                switch (resultCode) {
                    case  CreateStoryCodeFlag.RESULT_OK:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            ClipData clipdata = data.getClipData();
                            if (clipdata == null) {
                                Uri uri = data.getData();
                                createStoryPresenter.onActivityResultForPhotoUriResultOk(uri);

                            } else {
                                int clipDataSize = clipdata.getItemCount();
                                for (int i = 0; i < clipDataSize; i++) {
                                    Uri uri = clipdata.getItemAt(i).getUri();
                                    createStoryPresenter.onActivityResultForPhotoUriResultOk(uri);
                                }
                            }
                        } else {
                            Uri uri = data.getData();
                            createStoryPresenter.onActivityResultForPhotoUriResultOk(uri);
                        }
                        break;
                }
                break;
        }

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back:
                navigateToBack();
                break;

            case R.id.btn_write:
                String content = et_content.getText().toString();
                createStoryPresenter.onClickStoryAdd(content);
                break;
            case R.id.btn_write_photo_upload:
                createStoryPresenter.onClickPhotoAdd(Build.VERSION.SDK_INT,Build.VERSION_CODES.KITKAT);
                break;
        }
    }
}
