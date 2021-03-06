package com.demand.well_family.well_family.falldiagnosis.environment.photo.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.demand.well_family.well_family.BuildConfig;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;
import com.demand.well_family.well_family.falldiagnosis.environment.photo.adapter.PhotoAdapter;
import com.demand.well_family.well_family.falldiagnosis.environment.photo.flag.EnvironmentEvaluationPhotoCodeFlag;
import com.demand.well_family.well_family.falldiagnosis.environment.photo.presenter.EnvironmentEvaluationPhotoPresenter;
import com.demand.well_family.well_family.falldiagnosis.environment.photo.presenter.impl.EnvironmentEvaluationPhotoPresenterImpl;
import com.demand.well_family.well_family.falldiagnosis.environment.photo.view.EnvironmentEvaluationPhotoView;
import com.demand.well_family.well_family.falldiagnosis.environment.result.activity.EnvironmentEvaluationResultActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dev-0 on 2017-05-30.
 */

public class EnvironmentEvaluationPhotoActivity extends Activity implements EnvironmentEvaluationPhotoView, View.OnClickListener {
    private EnvironmentEvaluationPhotoPresenter environmentEvaluationPhotoPresenter;

    private TextView tv_photoenvironmentevaluation_content;
    private TextView tv_photoenvironmentevaluation_photo_size;
    private TextView tv_photoenvironmentevaluation_photo_total_size;

    private LinearLayout ll_photoenvironmentevaluation_inflate;

    private Button btn_photoenvironmentevaluation_photo;
    private Button btn_photoenvironmentevaluation_jump;
    private Button btn_photoenvironmentevaluation_save;

    private LayoutInflater layoutInflater;
    private LinearLayout ll_environmentevaluation_nophoto;
    private PhotoAdapter photoAdapter;


    private View decorView;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private ImageView toolbarBack;
    private RecyclerView rv_environmentevaluation_photolist;
    private File file;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoenvironmentevaluation);

        FallDiagnosisContentCategory fallDiagnosisContentCategory = new FallDiagnosisContentCategory();
        fallDiagnosisContentCategory.setName(getIntent().getStringExtra("fall_diagnosis_content_category_name"));
        fallDiagnosisContentCategory.setId(getIntent().getIntExtra("fall_diagnosis_content_category_id", 0));

        FallDiagnosisCategory fallDiagnosisCategory = new FallDiagnosisCategory();
        fallDiagnosisCategory.setId(getIntent().getIntExtra("category_id", 0));

        int environmentEvaluationCategorySize = getIntent().getIntExtra("environmentEvaluationCategorySize", 0);
        ArrayList<Integer> answerList = (ArrayList<Integer>) getIntent().getSerializableExtra("answerList");

        environmentEvaluationPhotoPresenter = new EnvironmentEvaluationPhotoPresenterImpl(this);
        environmentEvaluationPhotoPresenter.onCreate(fallDiagnosisCategory, fallDiagnosisContentCategory, answerList, environmentEvaluationCategorySize);
    }

    @Override
    public void init() {
        tv_photoenvironmentevaluation_content = (TextView) findViewById(R.id.tv_photoenvironmentevaluation_content);
        tv_photoenvironmentevaluation_photo_size = (TextView) findViewById(R.id.tv_photoenvironmentevaluation_photo_size);
        tv_photoenvironmentevaluation_photo_total_size = (TextView) findViewById(R.id.tv_photoenvironmentevaluation_photo_total_size);
        tv_photoenvironmentevaluation_content = (TextView) findViewById(R.id.tv_photoenvironmentevaluation_content);

        ll_photoenvironmentevaluation_inflate = (LinearLayout) findViewById(R.id.ll_photoenvironmentevaluation_inflate);

        btn_photoenvironmentevaluation_photo = (Button) findViewById(R.id.btn_photoenvironmentevaluation_photo);
        btn_photoenvironmentevaluation_jump = (Button) findViewById(R.id.btn_photoenvironmentevaluation_jump);
        btn_photoenvironmentevaluation_save = (Button) findViewById(R.id.btn_photoenvironmentevaluation_save);
        layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        environmentEvaluationPhotoPresenter.onLoadData();
        btn_photoenvironmentevaluation_photo.setOnClickListener(this);
        btn_photoenvironmentevaluation_jump.setOnClickListener(this);
        btn_photoenvironmentevaluation_save.setOnClickListener(this);
    }


    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showContent(String message) {
        tv_photoenvironmentevaluation_content.setText(message);
    }

    @Override
    public View getDecorView() {
        if (decorView == null) {
            decorView = this.getWindow().getDecorView();
        }
        return decorView;
    }

    @Override
    public void setToolbar(View decorView) {
        toolbar = (Toolbar) decorView.findViewById(R.id.toolBar);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbarBack = (ImageView) toolbar.findViewById(R.id.toolbar_back);
        toolbarBack.setOnClickListener(this);
    }

    @Override
    public void showToolbar(String message) {
        toolbarTitle.setText(message);
    }


    @Override
    public void setPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, EnvironmentEvaluationPhotoCodeFlag.CAMERA_EXTERNAL_STORAGE_PERMISSION);
        } else {
            init();
        }
    }

    @Override
    public void navigateToBack() {
        finish();
    }

    @Override
    public void setNoPhotoView() {
        layoutInflater.inflate(R.layout.item_environmentevaluation_nophoto, ll_photoenvironmentevaluation_inflate, true);
        ll_environmentevaluation_nophoto = (LinearLayout) ll_photoenvironmentevaluation_inflate.findViewById(R.id.ll_environmentevaluation_nophoto);
    }

    @Override
    public void showCamera() {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        String url = "wellfamily_" + System.currentTimeMillis() + ".jpg";
        Uri uri = null;

        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/Camera", url);

        uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, EnvironmentEvaluationPhotoCodeFlag.TAKE_A_PICTURE);
    }

    @Override
    public void setPhotoItem(ArrayList<Uri> photoList) {
        layoutInflater.inflate(R.layout.item_environmentevaluation_photolist, ll_photoenvironmentevaluation_inflate, true);

        rv_environmentevaluation_photolist = (RecyclerView) ll_photoenvironmentevaluation_inflate.findViewById(R.id.rv_environmentevaluation_photolist);
        photoAdapter = new PhotoAdapter(photoList, this, R.layout.item_write_upload_image, environmentEvaluationPhotoPresenter);
        rv_environmentevaluation_photolist.setAdapter(photoAdapter);
        rv_environmentevaluation_photolist.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public void setPhotoAdapterDelete(int position) {
        if(photoAdapter != null) {
            photoAdapter.setPhotoDelete(position);
        }
    }

    @Override
    public void goneInflatedView() {
        ll_photoenvironmentevaluation_inflate.removeAllViews();
    }

    @Override
    public void showPhotoSize(String message) {
        tv_photoenvironmentevaluation_photo_size.setText(message);
    }

    @Override
    public void navigateToEnvironmentEvaluationResultActivity(FallDiagnosisCategory fallDiagnosisCategory, FallDiagnosisContentCategory fallDiagnosisContentCategory, ArrayList<Integer> answerList, int environmentEvaluationCategorySize) {
        Intent intent = new Intent(this, EnvironmentEvaluationResultActivity.class);

        intent.putExtra("category_id", fallDiagnosisCategory.getId());
        intent.putExtra("fall_diagnosis_content_category_name", fallDiagnosisContentCategory.getName());
        intent.putExtra("fall_diagnosis_content_category_id", fallDiagnosisContentCategory.getId());
        intent.putExtra("environmentEvaluationCategorySize", environmentEvaluationCategorySize);
        intent.putExtra("answerList", answerList);

        startActivity(intent);
        finish();
    }

    @Override
    public void navigateToEnvironmentEvaluationResultActivity(FallDiagnosisCategory fallDiagnosisCategory, FallDiagnosisContentCategory fallDiagnosisContentCategory, ArrayList<Integer> answerList, ArrayList<Uri> photoList, ArrayList<String> pathList, int environmentEvaluationCategorySize) {
        Intent intent = new Intent(this, EnvironmentEvaluationResultActivity.class);

        intent.putExtra("category_id", fallDiagnosisCategory.getId());
        intent.putExtra("fall_diagnosis_content_category_name", fallDiagnosisContentCategory.getName());
        intent.putExtra("fall_diagnosis_content_category_id", fallDiagnosisContentCategory.getId());
        intent.putExtra("environmentEvaluationCategorySize", environmentEvaluationCategorySize);
        intent.putExtra("photoList", photoList);
        intent.putExtra("pathList", pathList);
        intent.putExtra("answerList", answerList);

        startActivity(intent);
        finish();
    }


    @Override
    public void showPhotoAdapterNotifyDataChanged() {
        if(photoAdapter != null) {
            photoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case EnvironmentEvaluationPhotoCodeFlag.CAMERA_EXTERNAL_STORAGE_PERMISSION:
                environmentEvaluationPhotoPresenter.onRequestPermissionsResultForCameraExternalStorage(grantResults);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case EnvironmentEvaluationPhotoCodeFlag.TAKE_A_PICTURE:
                switch (resultCode) {
                    case EnvironmentEvaluationPhotoCodeFlag.RESULT_OK:
                        Uri uri = null;
                        if (data != null) {
                            uri = data.getData();
                        } else {
                            try {
                                showProgressDialog();
                                boolean isBroadCast = (new SendBroadcastTask()).execute().get();

                                if (isBroadCast) {
                                    uri = getLastPhotoUri();
                                    goneProgressDialog();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                        environmentEvaluationPhotoPresenter.onActivityResultForCameraUriResultOk(uri);

                        break;
                }
                break;
        }
    }

    public class SendBroadcastTask extends AsyncTask<Object, Object, Boolean> {

        @Override
        protected Boolean doInBackground(Object... urls) {

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file));
            intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            sendBroadcast(intent);

            try {
                Thread.sleep(700);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return true;
        }

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


    public Uri getLastPhotoUri() {
        Uri uri = null;

        String[] column = {MediaStore.Images.Media.DATA};

        // where id is equal to

        try {
            Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, null, null, null);

            if (cursor != null && cursor.moveToLast()) {
                Uri u = Uri.parse(cursor.getString(0));

                File wallpaper_file = new File(u.getPath());
                uri = getImageContentUri(this, wallpaper_file.getAbsolutePath());


                int id = cursor.getInt(1);
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return uri;
    }

    public static Uri getImageContentUri(Context context, String absPath) {

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                , new String[]{MediaStore.Images.Media._ID}
                , MediaStore.Images.Media.DATA + "=? "
                , new String[]{absPath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Integer.toString(id));

        } else if (!absPath.isEmpty()) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, absPath);
            return context.getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } else {
            return null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back:
                finish();
                break;

            case R.id.btn_photoenvironmentevaluation_photo:
                environmentEvaluationPhotoPresenter.onClickCamera();
                break;

            case R.id.btn_photoenvironmentevaluation_jump:
                environmentEvaluationPhotoPresenter.onClickJump();
                break;

            case R.id.btn_photoenvironmentevaluation_save:
                environmentEvaluationPhotoPresenter.onClickSave();
                break;
        }
    }
}
