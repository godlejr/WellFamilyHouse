package com.demand.well_family.well_family.story.edit.activity;

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
import android.support.annotation.Nullable;
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

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.dto.Story;
import com.demand.well_family.well_family.story.create.flag.CreateStoryCodeFlag;
import com.demand.well_family.well_family.story.edit.adapter.PhotoAdapter;
import com.demand.well_family.well_family.story.edit.adapter.PrevPhotoAdapter;
import com.demand.well_family.well_family.story.edit.flag.EditStoryCodeFlag;
import com.demand.well_family.well_family.story.edit.presenter.EditStoryPresenter;
import com.demand.well_family.well_family.story.edit.presenter.impl.EditStoryPresenterImpl;
import com.demand.well_family.well_family.story.edit.view.EditStoryView;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-03-28.
 */

public class EditStoryActivity extends Activity implements EditStoryView, View.OnClickListener {
    private EditStoryPresenter editStoryPresenter;

    //toolbar
    private Toolbar toolbar;
    private ImageView toolbar_back;
    private TextView toolbar_title;
    private View decorView;

    private RecyclerView rv_write_image_upload;
    private RecyclerView rv_write_image_upload2;
    private EditText et_write;
    private Button btn_write_photo_upload;
    private Button btn_write;
    private ProgressDialog progressDialog;

    private PhotoAdapter photoAdapter;
    private PrevPhotoAdapter prevPhotoAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_story);

        editStoryPresenter = new EditStoryPresenterImpl(this);
        Story story = new Story();

        story.setId(getIntent().getIntExtra("story_id", 0));
        story.setContent(getIntent().getStringExtra("content"));
        story.setPosition(getIntent().getIntExtra("position", 0));

        editStoryPresenter.onCreate(story);

    }

    @Override
    public void init(Story story) {
        rv_write_image_upload = (RecyclerView) findViewById(R.id.rv_write_image_upload);
        rv_write_image_upload2 = (RecyclerView) findViewById(R.id.rv_write_image_upload2);

        et_write = (EditText) findViewById(R.id.et_write);
        btn_write = (Button) findViewById(R.id.btn_write);
        btn_write_photo_upload = (Button) findViewById(R.id.btn_write_photo_upload);


        et_write.setText(story.getContent());

        ArrayList<Photo> prevPhotoList = (ArrayList<Photo>) getIntent().getSerializableExtra("photoList");
        editStoryPresenter.onLoadData(prevPhotoList, getString(R.string.cloud_front_stories_images));

        btn_write_photo_upload.setOnClickListener(this);

        btn_write.setOnClickListener(this);
    }

    @Override
    public void setProgressDialog(int position) {
        progressDialog.setProgress(position);

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
    public void showPhotoAdapterNotifyDataChanged() {
        photoAdapter.notifyDataSetChanged();
    }

    @Override
    public void showPrevPhotoAdapterNotifyDataChanged() {
        prevPhotoAdapter.notifyDataSetChanged();
    }

    @Override
    public void setPhotoAdapterDelete(int position) {
        photoAdapter.setPhotoDelete(position);
    }

    @Override
    public void setPrevPhotoAdapterDelete(int position) {
        prevPhotoAdapter.setPhotoDelete(position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case EditStoryCodeFlag.PICK_PHOTO:
                switch (resultCode) {
                    case EditStoryCodeFlag.RESULT_OK:
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            ClipData clipdata = data.getClipData();
                            if (clipdata == null) {
                                Uri uri = data.getData();
                                editStoryPresenter.onActivityResultForPhotoUriResultOk(uri);

                            } else {
                                int clipDataSize = clipdata.getItemCount();
                                for (int i = 0; i < clipDataSize; i++) {
                                    Uri uri = clipdata.getItemAt(i).getUri();
                                    editStoryPresenter.onActivityResultForPhotoUriResultOk(uri);
                                }
                            }
                        } else {
                            Uri uri = data.getData();
                            editStoryPresenter.onActivityResultForPhotoUriResultOk(uri);
                        }
                        break;
                }
                break;
        }
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
    public void setPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, EditStoryCodeFlag.READ_EXTERNAL_STORAGE_PERMISSION);
    }

    @Override
    public void navigateToBackResultCancel(int position) {
        String content = et_write.getText().toString();
        Intent intent = getIntent();
        intent.putExtra("content", content);
        intent.putExtra("position", position);
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public void navigateToBackResultOk(int position) {
        String content = et_write.getText().toString();
        Intent intent = getIntent();
        intent.putExtra("content", content);
        intent.putExtra("position", position);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void navigateToBack() {
        finish();
    }

    @Override
    public void setPhotoItem(ArrayList<Uri> photoList) {
        photoAdapter = new PhotoAdapter(photoList, this, R.layout.item_write_upload_image, editStoryPresenter);
        rv_write_image_upload.setAdapter(photoAdapter);
        rv_write_image_upload.setLayoutManager(new LinearLayoutManager(EditStoryActivity.this, LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public void setPrevPhotoItem(ArrayList<URL> prevPhotoList) {
        prevPhotoAdapter = new PrevPhotoAdapter(prevPhotoList, this, R.layout.item_write_upload_image, editStoryPresenter);
        rv_write_image_upload2.setAdapter(prevPhotoAdapter);
        rv_write_image_upload2.setLayoutManager(new LinearLayoutManager(EditStoryActivity.this, LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back:
                editStoryPresenter.onBackPressed();
                break;
            case R.id.btn_write_photo_upload:
                editStoryPresenter.onClickPhotoAdd(Build.VERSION.SDK_INT, Build.VERSION_CODES.KITKAT);
                break;
            case R.id.btn_write:
                String content = et_write.getText().toString();
                editStoryPresenter.onClickStoryEdit(content);
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case EditStoryCodeFlag.READ_EXTERNAL_STORAGE_PERMISSION:
                editStoryPresenter.onRequestPermissionsResultForReadExternalStorage(grantResults);
                break;
        }
    }


    @Override
    public void onBackPressed() {
        editStoryPresenter.onBackPressed();
    }
}
