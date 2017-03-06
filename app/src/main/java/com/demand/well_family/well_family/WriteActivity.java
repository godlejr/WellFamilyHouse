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
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.connection.Server_Connection;
import com.demand.well_family.well_family.dto.Story;
import com.demand.well_family.well_family.dto.StoryInfo;
import com.demand.well_family.well_family.log.LogFlag;
import com.demand.well_family.well_family.market.MarketMainActivity;
import com.demand.well_family.well_family.memory_sound.SongMainActivity;
import com.demand.well_family.well_family.settings.SettingActivity;
import com.demand.well_family.well_family.users.UserActivity;
import com.demand.well_family.well_family.util.RealPathUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.demand.well_family.well_family.LoginActivity.finishList;

/**
 * Created by ㅇㅇ on 2017-01-20.
 */
//
public class WriteActivity extends Activity {
    private RecyclerView rv_write_image_upload;
    private EditText et_content;
    private Button btn_write_image_upload;
    private Button btn_write;
    private ArrayList<Uri> photoList;
    private ArrayList<String> pathList;


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

    private ProgressDialog progressDialog;


    private RealPathUtil realPathUtil;
    private final int READ_EXTERNAL_STORAGE_PERMISSION = 10001;

    //toolbar
    private DrawerLayout dl;
    private Server_Connection server_connection;

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
        setToolbar(this.getWindow().getDecorView(), this, "글쓰기");
    }

    // toolbar & menu
    public void setToolbar(View view, Context context, String title) {
        NavigationView nv = (NavigationView) view.findViewById(R.id.nv);
        nv.setItemIconTintList(null);
        dl = (DrawerLayout) view.findViewById(R.id.dl);

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
        ImageView toolbar_menu = (ImageView) toolbar.findViewById(R.id.toolbar_menu);
        toolbar_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl.openDrawer(GravityCompat.START);
            }
        });

        // header
        View nv_header_view = nv.getHeaderView(0);
        LinearLayout ll_menu_mypage = (LinearLayout) nv_header_view.findViewById(R.id.ll_menu_mypage);
        ll_menu_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dl.closeDrawers();

                Intent intent = new Intent(WriteActivity.this, UserActivity.class);
                startActivity(intent);

            }
        });
        TextView tv_menu_name = (TextView) nv_header_view.findViewById(R.id.tv_menu_name);
        tv_menu_name.setText(user_name);

        TextView tv_menu_birth = (TextView) nv_header_view.findViewById(R.id.tv_menu_birth);
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(user_birth);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일생");
            tv_menu_birth.setText(sdf.format(date));
        } catch (ParseException e) {
            log(e);
        }

        ImageView iv_menu_avatar = (ImageView) nv_header_view.findViewById(R.id.iv_menu_avatar);
        Glide.with(context).load(getString(R.string.cloud_front_user_avatar) + user_avatar).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_menu_avatar);


        // menu
        Menu menu = nv.getMenu();
        MenuItem menu_all = menu.findItem(R.id.menu_all);
        SpannableString s = new SpannableString(menu_all.getTitle());
        s.setSpan(new TextAppearanceSpan(view.getContext(), R.style.NavigationDrawer), 0, s.length(), 0);
        menu_all.setTitle(s);

        MenuItem menu_apps = menu.findItem(R.id.menu_apps);
        s = new SpannableString(menu_apps.getTitle());
        s.setSpan(new TextAppearanceSpan(view.getContext(), R.style.NavigationDrawer), 0, s.length(), 0);
        menu_apps.setTitle(s);

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);
                dl.closeDrawers();

                Intent startLink;
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        intent = new Intent(WriteActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;

                    case R.id.menu_search:
                        Toast.makeText(getApplicationContext(), "준비중입니다.", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.menu_market:
                        intent = new Intent(WriteActivity.this, MarketMainActivity.class);
                        startActivity(intent);

                        break;

                    case R.id.menu_setting:
                        Intent settingIntent = new Intent(getApplicationContext(), SettingActivity.class);
                        startActivity(settingIntent);
                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                        break;

                    case R.id.menu_help:
                        Toast.makeText(getApplicationContext(), "준비중입니다.", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.menu_logout:
                        SharedPreferences loginInfo = getSharedPreferences("loginInfo", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = loginInfo.edit();
                        editor.remove("user_id");
                        editor.remove("user_name");
                        editor.remove("user_email");
                        editor.remove("user_birth");
                        editor.remove("user_avatar");
                        editor.remove("user_phone");
                        editor.remove("user_level");
                        editor.commit();

                        intent = new Intent(WriteActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;

//                    App 이용하기
                    case R.id.menu_selffeet:
                        Toast.makeText(getApplicationContext(), "준비중입니다.", Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.menu_bubblefeet:
                        startLink = getPackageManager().getLaunchIntentForPackage(getString(R.string.bubblefeet));
                        if (startLink == null) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.market_front) + getString(R.string.bubblefeet))));
                        } else {
                            startActivity(startLink);
                        }
                        break;

                    case R.id.menu_happyfeet:
                        startLink = getPackageManager().getLaunchIntentForPackage(getString(R.string.happyfeet));
                        if (startLink == null) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.market_front) + getString(R.string.happyfeet))));
                        } else {
                            startActivity(startLink);
                        }
                        break;

                    case R.id.menu_memory_sound:
                        startLink = new Intent(getApplicationContext(), SongMainActivity.class);
                        startActivity(startLink);
                        break;
                }
                return true;
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
        photoViewAdapter = new PhotoViewAdapter(photoList, this, R.layout.item_write_upload_image);

        et_content = (EditText) findViewById(R.id.et_write);
        btn_write = (Button) findViewById(R.id.btn_write);
        btn_write_image_upload = (Button) findViewById(R.id.btn_write_upload);

        rv_write_image_upload.setAdapter(photoViewAdapter);

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
                            map.put("family_id", String.valueOf(family_id));
                            map.put("content", et_content.getText().toString());

                            server_connection = Server_Connection.retrofit.create(Server_Connection.class);
                            Call<ArrayList<Story>> call_write_story = server_connection.insert_story(user_id, map);
                            call_write_story.enqueue(new Callback<ArrayList<Story>>() {
                                @Override
                                public void onResponse(Call<ArrayList<Story>> call, Response<ArrayList<Story>> response) {
                                    int story_id = response.body().get(0).getId();
                                    StoryInfo storyInfo = new StoryInfo(user_id, user_name, user_avatar, response.body().get(0).getId(), response.body().get(0).getCreated_at(), response.body().get(0).getContent());

                                    for (int i = 0; i < photoListSize; i++) {
                                        progressDialog.setProgress(i + 1);
                                        server_connection = Server_Connection.retrofit.create(Server_Connection.class);
                                        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), addBase64Bitmap(encodeImage(photoList.get(i), i)));
                                        Call<ResponseBody> call_write_photo = server_connection.insert_photos(story_id, requestBody);
                                        call_write_photo.enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                //성공
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
                                }

                                @Override
                                public void onFailure(Call<ArrayList<Story>> call, Throwable t) {
                                    log(t);
                                    Toast.makeText(WriteActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }).start();
                }
            }
        });

        btn_write_image_upload.setOnClickListener(new View.OnClickListener() {
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
        bm.compress(Bitmap.CompressFormat.JPEG, 90, baos);
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
