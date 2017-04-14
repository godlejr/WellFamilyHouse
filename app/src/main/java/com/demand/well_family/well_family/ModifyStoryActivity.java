package com.demand.well_family.well_family;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.repository.StoryServerConnection;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.interceptor.HeaderInterceptor;
import com.demand.well_family.well_family.main.base.activity.MainActivity;
import com.demand.well_family.well_family.util.ErrorUtil;
import com.demand.well_family.well_family.util.RealPathUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ㅇㅇ on 2017-03-28.
 */

public class ModifyStoryActivity extends Activity {
    //user_info
    private int user_id;
    private String user_email;
    private String user_name;
    private String user_birth;
    private String user_phone;
    private int user_level;
    private String user_avatar;
    private String access_token;

    //story_info
    private ArrayList<Uri> photoList; // 갤러리 이미지
    private ArrayList<URL> cdnPhotoList;
    private ArrayList<String> pathList;
    private String content;
    private int story_id;

    private static final Logger logger = LoggerFactory.getLogger(MainActivity.class);
    private SharedPreferences loginInfo;

    private RecyclerView rv_write_image_upload;
    private RecyclerView rv_write_image_upload2;
    private EditText et_write;
    private Button btn_write_photo_upload;
    private Button btn_write;

    private PhotoViewAdapter photoViewAdapter;
    private RealPathUtil realPathUtil;
    public final static int READ_EXTERNAL_STORAGE_PERMISSION = 2;
    public final static int PICK_PHOTO = 1;

    private StoryServerConnection storyServerConnection;
    private ProgressDialog progressDialog;
    private int sleepTime;
    private final int UPLOADONEPIC = 950;

    private ArrayList<Bitmap> bitmapPhotos;
    private PhotoViewAdapter2 photoViewAdapter2;

    private int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_story);

        checkPermission();
        setUserInfo();
        init();
        getContentData();
    }

    private void setUserInfo() {
        loginInfo = getSharedPreferences("loginInfo", Activity.MODE_PRIVATE);
        user_id = loginInfo.getInt("user_id", 0);
        user_level = loginInfo.getInt("user_level", 0);
        user_name = loginInfo.getString("user_name", null);
        user_email = loginInfo.getString("user_email", null);
        user_birth = loginInfo.getString("user_birth", null);
        user_avatar = loginInfo.getString("user_avatar", null);
        user_phone = loginInfo.getString("user_phone", null);
        access_token = loginInfo.getString("access_token", null);
        setToolbar(this.getWindow().getDecorView(), this, "수정하기");
    }

    private void init() {

        bitmapPhotos = new ArrayList<>();
        story_id = getIntent().getIntExtra("story_id", 0);
        content = getIntent().getStringExtra("content");
        position = getIntent().getIntExtra("position", 0);

        realPathUtil = new RealPathUtil();
        rv_write_image_upload = (RecyclerView) findViewById(R.id.rv_write_image_upload);
        rv_write_image_upload2 = (RecyclerView) findViewById(R.id.rv_write_image_upload2);

        et_write = (EditText) findViewById(R.id.et_write);
        btn_write = (Button) findViewById(R.id.btn_write);
        btn_write_photo_upload = (Button) findViewById(R.id.btn_write_photo_upload);

        btn_write_photo_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 사진 업로드
                Intent intent;
                if (photoList.size() >= 10) {
                    Toast.makeText(ModifyStoryActivity.this, "사진은 최대 10개까지 등록이 가능합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    intent = new Intent();
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
//                        버전 체크 (키캣 이상 : 다중 선택O)
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Photo"), PICK_PHOTO);
                    } else {
//                       키캣 이하 (다중 선택 X)
                        intent.setAction(Intent.ACTION_PICK);
                        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                        startActivityForResult(intent, PICK_PHOTO);
                    }
                }
            }
        });

        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int photoListSize = photoList.size();

                if (photoListSize != 0 || et_write.getText().toString().length() != 0) {
                    progressDialog = new ProgressDialog(ModifyStoryActivity.this);
                    progressDialog.show();
                    progressDialog.getWindow().setBackgroundDrawable(new
                            ColorDrawable(Color.TRANSPARENT));
                    progressDialog.setContentView(R.layout.progress_dialog);

                    if (photoListSize > 10) {
                        sleepTime = UPLOADONEPIC * 10;
                    } else {
                        if (photoListSize == 1) {
                            sleepTime = 2500;
                        } else {
                            sleepTime = UPLOADONEPIC * photoListSize;
                        }
                    }

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final String content = et_write.getText().toString();
                            HashMap<String, String> map = new HashMap<>();
                            map.put("content", content);

                            final int photoSize = photoViewAdapter.getItemCount() + photoViewAdapter2.getItemCount();
                            if (photoSize != 0) {

                                for (int j = 0; j < cdnPhotoList.size(); j++) {
                                    bitmapPhotos.add(urlToBitmap(cdnPhotoList.get(j)));
                                }

                                for (int j = 0; j < photoListSize; j++) {
                                    bitmapPhotos.add(encodeImage(photoList.get(j), j));
                                }

                            }
                            storyServerConnection = new HeaderInterceptor(access_token).getClientForStoryServer().create(StoryServerConnection.class);
                            Call<Void> call_write_story = storyServerConnection.update_story(story_id, map);
                            call_write_story.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()) {

                                        if (photoSize != 0) {

                                            for (int i = 0; i < photoSize; i++) {
                                                progressDialog.setProgress(i + 1);
                                                storyServerConnection = new HeaderInterceptor(access_token).getClientForStoryServer().create(StoryServerConnection.class);

                                                final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), addBase64Bitmap(bitmapPhotos.get(i)));
                                                Call<ResponseBody> call_write_photo = storyServerConnection.insert_photos(story_id, requestBody);
                                                call_write_photo.enqueue(new Callback<ResponseBody>() {
                                                    @Override
                                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                        if (response.isSuccessful()) {

                                                        } else {
                                                            Toast.makeText(ModifyStoryActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                        log(t);
                                                        Toast.makeText(ModifyStoryActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                                    }
                                                });

                                            }

                                            try {
                                                Thread.sleep(sleepTime);
                                            } catch (InterruptedException e) {
                                                log(e);
                                            }
                                        }
                                        progressDialog.dismiss();
                                        Intent intent = getIntent();
                                        intent.putExtra("content", content);
                                        intent.putExtra("position", position);


                                        setResult(RESULT_OK, intent);
                                        finish();

                                    } else {
                                        Toast.makeText(ModifyStoryActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    log(t);
                                    Toast.makeText(ModifyStoryActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }).start();
                }
            }
        });
    }

    private Bitmap urlToBitmap(final URL url) {

        Bitmap bm = null;
        HttpURLConnection conn = null;
        BufferedInputStream bis = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.connect();

            int nSize = conn.getContentLength();
            bis = new BufferedInputStream(conn.getInputStream(), nSize);
            bm = BitmapFactory.decodeStream(bis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }

        return bm;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_PHOTO:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        ClipData clipdata = data.getClipData();
                        if (clipdata == null) { // 1개
                            Uri uri = data.getData();
                            String path = null;
                            try {
                                path = realPathUtil.getRealPathFromURI_API19(this, uri);
                            } catch (RuntimeException e) {
                                log(e);
                                path = realPathUtil.getRealPathFromURI_API11to18(this, uri);
                            }
                            pathList.add(path);
                            photoList.add(uri);
                            photoViewAdapter.notifyDataSetChanged();
                        } else { // 여러개
                            int clipDataSize = clipdata.getItemCount();

                            for (int i = 0; i < clipDataSize; i++) {
                                Uri uri = clipdata.getItemAt(i).getUri();
                                String path = null;
                                try {
                                    path = realPathUtil.getRealPathFromURI_API19(this, uri);
                                } catch (RuntimeException e) {
                                    log(e);
                                    path = realPathUtil.getRealPathFromURI_API11to18(this, uri);
                                }
                                pathList.add(path);
                                photoList.add(uri);
                                photoViewAdapter.notifyDataSetChanged();
                            }
                        }
                    } else {
                        Uri uri = data.getData();
                        String path = null;
                        try {
                            path = realPathUtil.getRealPathFromURI_API11to18(this, uri);
                        } catch (RuntimeException e) {
                            log(e);
                            path = realPathUtil.getRealPathFromURI_API19(this, uri);
                        }
                        pathList.add(path);
                        photoList.add(uri);
                        photoViewAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    }

    private void getContentData() {
        et_write.setText(content);

        //photoList
        pathList = new ArrayList<>();
        photoList = new ArrayList<>(); // 갤러리 이미지
        cdnPhotoList = new ArrayList<>(); // 서버 이미지
        ArrayList<Photo> photos = (ArrayList<Photo>) getIntent().getSerializableExtra("photoList");
        int photosSize = photos.size();
        for (int i = 0; i < photosSize; i++) {
//            cdnPhotoList.add(Uri.parse(getString(R.string.cloud_front_stories_images) + photos.get(i).getName() + "." + photos.get(i).getExt()));
            try {
                cdnPhotoList.add(new URL(getString(R.string.cloud_front_stories_images) + photos.get(i).getName() + "." + photos.get(i).getExt()));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        photoViewAdapter = new PhotoViewAdapter(photoList, this, R.layout.item_write_upload_image);
        rv_write_image_upload.setAdapter(photoViewAdapter);
        rv_write_image_upload.setLayoutManager(new LinearLayoutManager(ModifyStoryActivity.this, LinearLayoutManager.HORIZONTAL, false));

        photoViewAdapter2 = new PhotoViewAdapter2(cdnPhotoList, this, R.layout.item_write_upload_image);
        rv_write_image_upload2.setAdapter(photoViewAdapter2);
        rv_write_image_upload2.setLayoutManager(new LinearLayoutManager(ModifyStoryActivity.this, LinearLayoutManager.HORIZONTAL, false));
    }

    private class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_write_upload_image;
        private ImageView iv_write_upload_delete;

        public PhotoViewHolder(View itemView) {
            super(itemView);

            iv_write_upload_image = (ImageView) itemView.findViewById(R.id.iv_write_upload_image);
            iv_write_upload_delete = (ImageView) itemView.findViewById(R.id.iv_write_upload_delete);

            iv_write_upload_delete.bringToFront();
            iv_write_upload_delete.invalidate();

            iv_write_upload_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    photoList.remove(getAdapterPosition());
                    photoViewAdapter.notifyDataSetChanged();
                    Toast.makeText(ModifyStoryActivity.this, "사진이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private class PhotoViewHolder2 extends RecyclerView.ViewHolder {
        private ImageView iv_write_upload_image;
        private ImageView iv_write_upload_delete;

        public PhotoViewHolder2(View itemView) {
            super(itemView);

            iv_write_upload_image = (ImageView) itemView.findViewById(R.id.iv_write_upload_image);
            iv_write_upload_delete = (ImageView) itemView.findViewById(R.id.iv_write_upload_delete);

            iv_write_upload_delete.bringToFront();
            iv_write_upload_delete.invalidate();

            iv_write_upload_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cdnPhotoList.remove(getAdapterPosition());
                    photoViewAdapter2.notifyDataSetChanged();
                    Toast.makeText(ModifyStoryActivity.this, "사진이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private class PhotoViewAdapter extends RecyclerView.Adapter<PhotoViewHolder> {
        private ArrayList<Uri> photoList;
        private Context context;
        private int layout;

        public PhotoViewAdapter(ArrayList<Uri> photoList, Context context, int layout) {
            this.photoList = photoList;
            this.context = context;
            this.layout = layout;
        }

        @Override
        public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            PhotoViewHolder holder = new PhotoViewHolder((LayoutInflater.from(context).inflate(layout, parent, false)));
            return holder;
        }

        @Override
        public void onBindViewHolder(PhotoViewHolder holder, int position) {
            Log.e("사진 수정", photoList.get(position).toString());
            Glide.with(context).load(photoList.get(position)).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_write_upload_image);
        }

        @Override
        public int getItemCount() {
            return photoList.size();
        }
    }

    private class PhotoViewAdapter2 extends RecyclerView.Adapter<PhotoViewHolder2> {
        private ArrayList<URL> photoList;
        private Context context;
        private int layout;

        public PhotoViewAdapter2(ArrayList<URL> photoList, Context context, int layout) {
            this.photoList = photoList;
            this.context = context;
            this.layout = layout;
        }

        @Override
        public PhotoViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
            PhotoViewHolder2 holder = new PhotoViewHolder2((LayoutInflater.from(context).inflate(layout, parent, false)));
            return holder;
        }

        @Override
        public void onBindViewHolder(PhotoViewHolder2 holder, int position) {
            Glide.with(context).load(photoList.get(position)).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_write_upload_image);
        }

        @Override
        public int getItemCount() {
            return photoList.size();
        }
    }

    private void checkPermission() {
        int readPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION);

        if (readPermission == PackageManager.PERMISSION_DENIED) {
            Log.e("WRITE PERMISSION", "권한X");

        } else {
            Log.e("WRITE PERMISSION", "권한O");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == READ_EXTERNAL_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "권한을 허가해주세요.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private Bitmap encodeImage(Uri uri, int i) {
        Bitmap bm = null;
        try {
            bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            ExifInterface exifInterface = new ExifInterface(pathList.get(i));
            int exifOrientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int exifDegree = exifOrientationToDegrees(exifOrientation);
            bm = rotate(bm, exifDegree);
        } catch (IOException e) {
            log(e);
        }

        return bm;
    }

    public Bitmap rotate(Bitmap bitmap, int degrees) {
        if (degrees != 0 && bitmap != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2,
                    (float) bitmap.getHeight() / 2);
            try {
                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
                if (bitmap != converted) {
                    bitmap.recycle();
                    bitmap = converted;
                }
            } catch (OutOfMemoryError ex) {
                log(ex);
            }
        }
        return bitmap;
    }

    public int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    public String addBase64Bitmap(Bitmap bm) {
        Log.e("ㅇㅇㅇㅇ", bm.toString());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.NO_WRAP | Base64.URL_SAFE);
    }


    // toolbar & menu
    public void setToolbar(View view, Context context, String title) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolBar);
        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText(title);
        ImageView toolbar_back = (ImageView) toolbar.findViewById(R.id.toolbar_back);
        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBack();
            }
        });
    }

    private void setBack() {
        String content = et_write.getText().toString();
        Intent intent = getIntent();
        intent.putExtra("content", content);
        intent.putExtra("position", position);
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    private static void log(Throwable throwable) {
        StackTraceElement[] ste = throwable.getStackTrace();
        String className = ste[0].getClassName();
        String methodName = ste[0].getMethodName();
        int lineNumber = ste[0].getLineNumber();
        String fileName = ste[0].getFileName();

        if (LogFlag.printFlag) {
            if (logger.isInfoEnabled()) {
                logger.info("Exception: " + throwable.getMessage());
                logger.info(className + "." + methodName + " " + fileName + " " + lineNumber + " " + "line");
            }
        }
    }

    @Override
    public void onBackPressed() {
        setBack();
    }
}
