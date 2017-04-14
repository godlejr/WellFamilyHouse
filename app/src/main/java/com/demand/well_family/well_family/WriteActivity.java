package com.demand.well_family.well_family;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.demand.well_family.well_family.dto.Story;
import com.demand.well_family.well_family.dto.StoryInfo;
import com.demand.well_family.well_family.repository.interceptor.HeaderInterceptor;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.main.base.activity.MainActivity;
import com.demand.well_family.well_family.util.ErrorUtil;
import com.demand.well_family.well_family.util.RealPathUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.demand.well_family.well_family.main.login.activity.LoginActivity.finishList;

/**
 * Created by ㅇㅇ on 2017-01-20.
 */
//
public class WriteActivity extends Activity {
    private RecyclerView rv_write_image_upload;
    private EditText et_content;
    private Button btn_write_photo_upload;
    private Button btn_write;
    private ArrayList<Uri> photoList;
    private ArrayList<String> pathList;
    private TextView tv_write_user_name;
    private ImageView iv_write_user_avatar;

    private PhotoViewAdapter photoViewAdapter;
    private static final int PICK_PHOTO = 1;

    private Intent intent;


    private int family_id;
    private String family_name;
    private String family_content;
    private String family_avatar;
    private int family_user_id;
    private String family_created_at;

    private int user_id;
    private String user_email;
    private String user_name;
    private String user_birth;
    private String user_phone;
    private int user_level;
    private String user_avatar;
    private String access_token;

    private ProgressDialog progressDialog;


    private RealPathUtil realPathUtil;
    private final int READ_EXTERNAL_STORAGE_PERMISSION = 10001;

    private StoryServerConnection storyServerConnection;

    private int sleepTime;
    private final int UPLOADONEPIC = 950;

    private int photoListSize;
    private static final Logger logger = LoggerFactory.getLogger(MainActivity.class);
    private SharedPreferences loginInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        finishList.add(this);
        setUserInfo();

        family_id = getIntent().getIntExtra("family_id", 0);
        family_name = getIntent().getStringExtra("family_name");
        family_content = getIntent().getStringExtra("family_content");
        family_avatar = getIntent().getStringExtra("family_avatar");
        family_user_id = getIntent().getIntExtra("family_user_id", 0);
        family_created_at = getIntent().getStringExtra("family_created_at");

        realPathUtil = new RealPathUtil();
        photoList = new ArrayList<>();
        pathList = new ArrayList<>();
        rv_write_image_upload = (RecyclerView) findViewById(R.id.rv_write_image_upload);
        rv_write_image_upload.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        init();
        checkPermission();
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
        setToolbar(this.getWindow().getDecorView(), this, "글쓰기");
    }

    // toolbar & menu
    public void setToolbar(View view, Context context, String title) {
        // toolbar menu, title, back
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
        finish();
    }

    private void checkPermission() {
        int readPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION);

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

    private void init() {
        et_content = (EditText) findViewById(R.id.et_write);
        btn_write = (Button) findViewById(R.id.btn_write);
        btn_write_photo_upload = (Button) findViewById(R.id.btn_write_photo_upload);
        photoViewAdapter = new PhotoViewAdapter(photoList, this, R.layout.item_write_upload_image);
        rv_write_image_upload.setAdapter(photoViewAdapter);

        tv_write_user_name = (TextView)findViewById(R.id.tv_write_user_name);
        iv_write_user_avatar = (ImageView)findViewById(R.id.iv_write_user_avatar);
        tv_write_user_name.setText(user_name);
        Glide.with(this).load(getString(R.string.cloud_front_user_avatar) +user_avatar).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_write_user_avatar);



        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoListSize = photoList.size();

                if (photoListSize != 0 || et_content.getText().toString().length() != 0) {
                    progressDialog = new ProgressDialog(WriteActivity.this);
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

                            HashMap<String, String> map = new HashMap<>();
                            map.put("user_id", String.valueOf(user_id));
                            map.put("family_id", String.valueOf(family_id));
                            map.put("family_name", family_name);
                            map.put("content", et_content.getText().toString());

                            storyServerConnection = new HeaderInterceptor(access_token).getClientForStoryServer().create(StoryServerConnection.class);
                            Call<Story> call_write_story = storyServerConnection.insert_story(map);
                            call_write_story.enqueue(new Callback<Story>() {
                                @Override
                                public void onResponse(Call<Story> call, Response<Story> response) {
                                    if (response.isSuccessful()) {
                                        Story story = response.body();
                                        int story_id = story.getId();
                                        StoryInfo storyInfo = new StoryInfo(user_id, user_name, user_avatar, story.getId(), story.getCreated_at(), story.getContent());

                                        for (int i = 0; i < photoListSize; i++) {
                                            progressDialog.setProgress(i + 1);
                                            storyServerConnection = new HeaderInterceptor(access_token).getClientForStoryServer().create(StoryServerConnection.class);
                                            final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), addBase64Bitmap(encodeImage(photoList.get(i), i)));
                                            Call<ResponseBody> call_write_photo = storyServerConnection.insert_photos(story_id, requestBody);
                                            call_write_photo.enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    if (response.isSuccessful()) {
                                                        //성공
                                                    } else {
                                                        Toast.makeText(WriteActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                    log(t);
                                                    Toast.makeText(WriteActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                                }
                                            });

                                        }

                                        try {
                                            Thread.sleep(sleepTime);
                                        } catch (InterruptedException e) {
                                            log(e);
                                        }

                                        progressDialog.dismiss();
                                        Intent intent = getIntent();
                                        intent.putExtra("storyInfo", storyInfo);
                                        setResult(Activity.RESULT_OK, intent);
                                        finish();
                                    } else {
                                        Toast.makeText(WriteActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Story> call, Throwable t) {
                                    log(t);
                                    Toast.makeText(WriteActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }).start();
                }
            }
        });

        btn_write_photo_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 사진 업로드
                if (photoList.size() >= 10) {
                    Toast.makeText(WriteActivity.this, "사진은 최대 10개까지 등록이 가능합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    intent = new Intent();
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
//                        버전 체크 (키캣 이상 : 다중 선택O)
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Photo"), 1);
                    } else {
//                       키캣 이하 (다중 선택 X)
                        intent.setAction(Intent.ACTION_PICK);
                        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                        startActivityForResult(intent, PICK_PHOTO);
                    }
                }
            }
        });
    }

    public String addBase64Bitmap(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.NO_WRAP | Base64.URL_SAFE);
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


    private class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_write_upload_image, iv_write_upload_delete;

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

                    Toast.makeText(WriteActivity.this, "사진이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
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
            Glide.with(context).load(photoList.get(position)).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_write_upload_image);
        }

        @Override
        public int getItemCount() {
            return photoList.size();
        }
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

}
