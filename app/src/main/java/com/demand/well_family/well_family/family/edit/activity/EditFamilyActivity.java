package com.demand.well_family.well_family.family.edit.activity;

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
import com.demand.well_family.well_family.family.edit.flag.EditFamilyCodeFlag;
import com.demand.well_family.well_family.family.edit.presenter.EditFamilyPresenter;
import com.demand.well_family.well_family.family.edit.presenter.impl.EditFamilyPresenterImpl;
import com.demand.well_family.well_family.family.edit.view.EditFamilyView;

/**
 * Created by ㅇㅇ on 2017-02-27.
 */

public class EditFamilyActivity extends Activity implements EditFamilyView, View.OnClickListener {
    private EditFamilyPresenter editFamilyPresenter;

    private EditText et_create_family_introduce;
    private EditText et_create_family_name;
    private ImageView iv_create_family_img;
    private ImageButton ib_create_family;
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

        Family family = new Family();
        family.setId(getIntent().getIntExtra("family_id", 0));
        family.setName(getIntent().getStringExtra("family_name"));
        family.setContent(getIntent().getStringExtra("family_content"));
        family.setAvatar(getIntent().getStringExtra("family_avatar"));
        family.setUser_id(getIntent().getIntExtra("family_user_id", 0));
        family.setCreated_at(getIntent().getStringExtra("family_created_at"));

        editFamilyPresenter = new EditFamilyPresenterImpl(this);
        editFamilyPresenter.onCreate(family);
    }

    @Override
    public void init(Family family) {
        et_create_family_introduce = (EditText) findViewById(R.id.et_create_family_introduce);
        et_create_family_name = (EditText) findViewById(R.id.et_create_family_name);
        iv_create_family_img = (ImageView) findViewById(R.id.iv_create_family_img);
        ib_create_family = (ImageButton) findViewById(R.id.ib_create_family);

        Glide.with(this).load(getString(R.string.cloud_front_family_avatar) + family.getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_create_family_img);
        et_create_family_introduce.setText(family.getContent());
        et_create_family_name.setText(family.getName());

        ib_create_family.setOnClickListener(this);
    }

    @Override
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
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, EditFamilyCodeFlag.READ_EXTERNAL_STORAGE_PERMISSION);
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
    public void showFamilyAvatar(Uri uri) {
        Glide.with(EditFamilyActivity.this).load(uri).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_create_family_img);
    }

    @Override
    public void navigateToBack() {
        finish();
    }

    @Override
    public void navigateToMediaStore() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, EditFamilyCodeFlag.MEDIA_STORE_REQUEST);
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
            case EditFamilyCodeFlag.READ_EXTERNAL_STORAGE_PERMISSION:
                editFamilyPresenter.onRequestPermissionsResultForReadExternalStorage(grantResults);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case EditFamilyCodeFlag.MEDIA_STORE_REQUEST:
                switch (resultCode) {
                    case  EditFamilyCodeFlag.RESULT_OK:
                        Uri uri = data.getData();
                        editFamilyPresenter.onActivityResultForPhotoUriResultOk(uri);
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

            case R.id.ib_create_family:
                navigateToMediaStore();
                break;

            case R.id.toolbar_complete:
                String familyName = et_create_family_name.getText().toString();
                String familyContent = et_create_family_introduce.getText().toString();

                editFamilyPresenter.onClickEditFamily(familyName, familyContent);
                break;
        }
    }
}
