package com.demand.well_family.well_family.create_family;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
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
import com.demand.well_family.well_family.connection.FamilyServerConnection;
import com.demand.well_family.well_family.connection.UserServerConnection;
import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.family.FamilyActivity;
import com.demand.well_family.well_family.interceptor.HeaderInterceptor;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.util.ErrorUtils;
import com.demand.well_family.well_family.util.RealPathUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ㅇㅇ on 2017-02-12.
 */

public class CreateFamilyActivity extends Activity {
    private ImageButton ib_create_family;
    private ImageView iv_create_family_img;
    private EditText et_create_family_name, et_create_family_introduce;
    private Uri family_photo_uri = null;
    private RealPathUtil realPathUtil;
    private String path;

    //user_info
    private int user_id;
    private String user_email;
    private String user_name;
    private String user_birth;
    private String user_phone;
    private int user_level;
    private String user_avatar;
    private ProgressDialog progressDialog;

    private UserServerConnection userServerConnection;
    private FamilyServerConnection familyServerConnection;

    private final int READ_EXTERNAL_STORAGE_PERMISSION = 10001;

    private static final Logger logger = LoggerFactory.getLogger(CreateFamilyActivity.class);
    private SharedPreferences loginInfo;
    private String access_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_create_family);
        getWindow().setLayout(android.view.WindowManager.LayoutParams.MATCH_PARENT, android.view.WindowManager.LayoutParams.MATCH_PARENT);

        setUserInfo();
        init();
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

        setToolbar(getWindow().getDecorView());
    }

    private void init() {
        et_create_family_introduce = (EditText) findViewById(R.id.et_create_family_introduce);
        et_create_family_name = (EditText) findViewById(R.id.et_create_family_name);
        iv_create_family_img = (ImageView) findViewById(R.id.iv_create_family_img);
        Glide.with(this).load(getString(R.string.cloud_front_family_avatar) + "family_avatar.jpg").thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_create_family_img);

        realPathUtil = new RealPathUtil();

        ib_create_family = (ImageButton) findViewById(R.id.ib_create_family);
        ib_create_family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, 7777);
            }
        });

        checkPermission();
    }

    public void setToolbar(View view) {
        // toolbar_main
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolBar);
        ImageView toolbar_back = (ImageView) toolbar.findViewById(R.id.toolbar_back);
        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBack();
            }
        });

        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText("가족 만들기");

        Button toolbar_complete = (Button) toolbar.findViewById(R.id.toolbar_complete);
        toolbar_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_create_family_name.length() == 0) {
                    Toast.makeText(CreateFamilyActivity.this, "가족 이름을 입력하세요.", Toast.LENGTH_SHORT).show();
                } else if (et_create_family_introduce.length() == 0) {
                    Toast.makeText(CreateFamilyActivity.this, "가족 소개를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    String family_name = et_create_family_name.getText().toString();
                    String family_introduce = et_create_family_introduce.getText().toString();

                    HashMap<String, String> map = new HashMap<>();
                    map.put("family_name", family_name);
                    map.put("family_content", family_introduce);

                    progressDialog = new ProgressDialog(CreateFamilyActivity.this);
                    progressDialog.show();
                    progressDialog.getWindow().setBackgroundDrawable(new
                            ColorDrawable(Color.TRANSPARENT));
                    progressDialog.setContentView(R.layout.progress_dialog);


                    userServerConnection = new HeaderInterceptor(access_token).getClientForUserServer().create(UserServerConnection.class);
                    Call<Integer> call = userServerConnection.insert_family(user_id, map);
                    call.enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            if (response.isSuccessful()) {
                                final int family_id = response.body();

                                if (family_photo_uri != null) {
                                    familyServerConnection = new HeaderInterceptor(access_token).getClientForFamilyServer().create(FamilyServerConnection.class);
                                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), addBase64Bitmap(encodeImage(family_photo_uri)));

                                    Call<ResponseBody> call_photo = familyServerConnection.update_family_avatar(family_id, requestBody);
                                    call_photo.enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            if (response.isSuccessful()) {
                                                familyServerConnection = new HeaderInterceptor(access_token).getClientForFamilyServer().create(FamilyServerConnection.class);
                                                Call<Family> call_family = familyServerConnection.family(family_id);
                                                call_family.enqueue(new Callback<Family>() {
                                                    @Override
                                                    public void onResponse(Call<Family> call, Response<Family> response) {
                                                        if (response.isSuccessful()) {
                                                            Family familyInfo = response.body();
                                                            Intent intent = new Intent(CreateFamilyActivity.this, FamilyActivity.class);

                                                            //family info
                                                            intent.putExtra("family_id", familyInfo.getId());
                                                            intent.putExtra("family_name", familyInfo.getName());
                                                            intent.putExtra("family_content", familyInfo.getContent());
                                                            intent.putExtra("family_avatar", familyInfo.getAvatar());
                                                            intent.putExtra("family_user_id", familyInfo.getUser_id());
                                                            intent.putExtra("family_created_at", familyInfo.getCreated_at());

                                                            try {
                                                                Thread.sleep(200);
                                                            } catch (InterruptedException e) {
                                                                log(e);
                                                            }

                                                            progressDialog.dismiss();
                                                            startActivity(intent);

                                                            finish();
                                                        } else {
                                                            Toast.makeText(CreateFamilyActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<Family> call, Throwable t) {
                                                        log(t);
                                                        Toast.makeText(CreateFamilyActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(CreateFamilyActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            log(t);
                                            Toast.makeText(CreateFamilyActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                } else {
                                    familyServerConnection = new HeaderInterceptor(access_token).getClientForFamilyServer().create(FamilyServerConnection.class);
                                    Call<Family> call_family = familyServerConnection.family(family_id);
                                    call_family.enqueue(new Callback<Family>() {
                                        @Override
                                        public void onResponse(Call<Family> call, Response<Family> response) {
                                            if (response.isSuccessful()) {
                                                Family familyInfo = response.body();
                                                Intent intent = new Intent(CreateFamilyActivity.this, FamilyActivity.class);

                                                //family info
                                                intent.putExtra("family_id", familyInfo.getId());
                                                intent.putExtra("family_name", familyInfo.getName());
                                                intent.putExtra("family_content", familyInfo.getContent());
                                                intent.putExtra("family_avatar", familyInfo.getAvatar());
                                                intent.putExtra("family_user_id", familyInfo.getUser_id());
                                                intent.putExtra("family_created_at", familyInfo.getCreated_at());

                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(CreateFamilyActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Family> call, Throwable t) {
                                            log(t);
                                            Toast.makeText(CreateFamilyActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(CreateFamilyActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
                            log(t);
                            Toast.makeText(CreateFamilyActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

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

    private Bitmap encodeImage(Uri uri) {
        Bitmap bm = null;
        try {
            bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            ExifInterface exifInterface = new ExifInterface(path);
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
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.NO_WRAP | Base64.URL_SAFE);
    }

    public void setBack() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 7777) {
                if (data != null) {
                    family_photo_uri = data.getData();
                    try {
                        path = realPathUtil.getRealPathFromURI_API19(this, family_photo_uri);
                    } catch (Exception e) {
                        log(e);
                        path = realPathUtil.getRealPathFromURI_API11to18(this, family_photo_uri);
                    }
                    Glide.with(CreateFamilyActivity.this).load(family_photo_uri).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_create_family_img);
                } else {
                    Toast.makeText(this, "사진을 다시 선택해주세요.", Toast.LENGTH_SHORT).show();
                    Log.e("갤러리 이미지 선택 오류", "");
                }
            }
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
