package com.demand.well_family.well_family.create_family;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.demand.well_family.well_family.connection.Server_Connection;
import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.Identification;
import com.demand.well_family.well_family.family.FamilyActivity;
import com.demand.well_family.well_family.log.LogFlag;
import com.demand.well_family.well_family.memory_sound.SoundRecordActivity;
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
    private Server_Connection server_connection;
    private ProgressDialog progressDialog;

    private int sleepTime;
    private final int UPLOADONEPIC = 850;
    private final int READ_EXTERNAL_STORAGE_PERMISSION = 10001;

    private static final Logger logger = LoggerFactory.getLogger(CreateFamilyActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_create_family);
        getWindow().setLayout(android.view.WindowManager.LayoutParams.MATCH_PARENT, android.view.WindowManager.LayoutParams.MATCH_PARENT);

        init();
        setToolbar(getWindow().getDecorView());
        checkPermission();
    }

    private void init() {
        et_create_family_introduce = (EditText) findViewById(R.id.et_create_family_introduce);
        et_create_family_name = (EditText) findViewById(R.id.et_create_family_name);
        iv_create_family_img = (ImageView) findViewById(R.id.iv_create_family_img);
        Glide.with(this).load(getString(R.string.cloud_front_family_avatar) + "family_avatar.jpg").thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_create_family_img);

        realPathUtil = new RealPathUtil();

        user_id = getIntent().getIntExtra("user_id", 0);
        user_name = getIntent().getStringExtra("user_name");
        user_level = getIntent().getIntExtra("user_level", 0);
        user_avatar = getIntent().getStringExtra("user_avatar");
        user_email = getIntent().getStringExtra("user_email");
        user_phone = getIntent().getStringExtra("user_phone");
        user_birth = getIntent().getStringExtra("user_birth");

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
                    String family_introfuce = et_create_family_introduce.getText().toString();

                    HashMap<String, String> map = new HashMap<>();
                    map.put("family_name", family_name);
                    map.put("family_content", family_introfuce);

                    progressDialog = new ProgressDialog(CreateFamilyActivity.this);
                    progressDialog.show();
                    progressDialog.getWindow().setBackgroundDrawable(new
                            ColorDrawable(Color.TRANSPARENT));
                    progressDialog.setContentView(R.layout.progress_dialog);


                    server_connection = Server_Connection.retrofit.create(Server_Connection.class);
                    Call<ArrayList<Identification>> call = server_connection.insert_family(user_id, map);
                    call.enqueue(new Callback<ArrayList<Identification>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Identification>> call, Response<ArrayList<Identification>> response) {
                            ArrayList<Identification> identificationList = response.body();
                            final int family_id = identificationList.get(0).getId();

                            if (family_photo_uri != null) {
                                server_connection = Server_Connection.retrofit.create(Server_Connection.class);
                                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), addBase64Bitmap(encodeImage(family_photo_uri)));

                                Call<ResponseBody> call_photo = server_connection.update_family_avatar(family_id, requestBody);
                                call_photo.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        server_connection = Server_Connection.retrofit.create(Server_Connection.class);
                                        Call<ArrayList<Family>> call_family = server_connection.family(family_id);
                                        call_family.enqueue(new Callback<ArrayList<Family>>() {
                                            @Override
                                            public void onResponse(Call<ArrayList<Family>> call, Response<ArrayList<Family>> response) {
                                                ArrayList<Family> familyList = response.body();
                                                Intent intent = new Intent(CreateFamilyActivity.this, FamilyActivity.class);

                                                //family info
                                                intent.putExtra("family_id", familyList.get(0).getId());
                                                intent.putExtra("family_name", familyList.get(0).getName());
                                                intent.putExtra("family_content", familyList.get(0).getContent());
                                                intent.putExtra("family_avatar", familyList.get(0).getAvatar());
                                                intent.putExtra("family_user_id", familyList.get(0).getUser_id());
                                                intent.putExtra("family_created_at", familyList.get(0).getCreated_at());

                                                //user info
                                                intent.putExtra("user_id", user_id);
                                                intent.putExtra("user_email", user_email);
                                                intent.putExtra("user_birth", user_birth);
                                                intent.putExtra("user_phone", user_phone);
                                                intent.putExtra("user_name", user_name);
                                                intent.putExtra("user_level", user_level);
                                                intent.putExtra("user_avatar", user_avatar);

                                                try {
                                                    Thread.sleep(sleepTime);
                                                } catch (InterruptedException e) {
                                                    log(e);
                                                }

                                                progressDialog.dismiss();
                                                startActivity(intent);

                                                finish();
                                            }

                                            @Override
                                            public void onFailure(Call<ArrayList<Family>> call, Throwable t) {
                                                log(t);
                                                Toast.makeText(CreateFamilyActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        log(t);
                                        Toast.makeText(CreateFamilyActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                server_connection = Server_Connection.retrofit.create(Server_Connection.class);
                                Call<ArrayList<Family>> call_family = server_connection.family(family_id);
                                call_family.enqueue(new Callback<ArrayList<Family>>() {
                                    @Override
                                    public void onResponse(Call<ArrayList<Family>> call, Response<ArrayList<Family>> response) {
                                        ArrayList<Family> familyList = response.body();
                                        Intent intent = new Intent(CreateFamilyActivity.this, FamilyActivity.class);

                                        //family info
                                        intent.putExtra("family_id", familyList.get(0).getId());
                                        intent.putExtra("family_name", familyList.get(0).getName());
                                        intent.putExtra("family_content", familyList.get(0).getContent());
                                        intent.putExtra("family_avatar", familyList.get(0).getAvatar());
                                        intent.putExtra("family_user_id", familyList.get(0).getUser_id());
                                        intent.putExtra("family_created_at", familyList.get(0).getCreated_at());

                                        //user info
                                        intent.putExtra("user_id", user_id);
                                        intent.putExtra("user_email", user_email);
                                        intent.putExtra("user_birth", user_birth);
                                        intent.putExtra("user_phone", user_phone);
                                        intent.putExtra("user_name", user_name);
                                        intent.putExtra("user_level", user_level);
                                        intent.putExtra("user_avatar", user_avatar);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(Call<ArrayList<Family>> call, Throwable t) {
                                        log(t);
                                        Toast.makeText(CreateFamilyActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Identification>> call, Throwable t) {
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
        bm.compress(Bitmap.CompressFormat.JPEG, 90, baos);
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
