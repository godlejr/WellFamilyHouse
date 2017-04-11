package com.demand.well_family.well_family.users;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.MainActivity;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.connection.SongServerConnection;
import com.demand.well_family.well_family.connection.UserServerConnection;
import com.demand.well_family.well_family.dto.FavoriteCategory;
import com.demand.well_family.well_family.dto.SongCategory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.interceptor.HeaderInterceptor;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.util.ErrorUtils;
import com.demand.well_family.well_family.util.RealPathUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.demand.well_family.well_family.LoginActivity.finishList;


/**
 * Created by ㅇㅇ on 2017-02-23.
 */

public class EditUserActivity extends Activity {
    private CircleImageView iv_edit_profile_avatar;
    private ImageButton ib_edit_profile_avatar;
    private TextView tv_edit_profile_email;
    private EditText et_edit_profile_name;
    private EditText et_edit_profile_birth;
    private EditText et_edit_profile_phone;
    private RadioButton rb_female;
    private RadioButton rb_man;
    private Button btn_edit_profile;

    private RecyclerView rv_profile_favorite;
    private RecyclerView rv_profile_song;
    private ProfileOptionAdapter profileOptionAdapter;
    private ProfileSongAdapter profileSongAdapter;

    private String dateStr, birthDate;

    // userInfo
    private int user_id;
    private String user_name;
    private String user_avatar;
    private String user_email;
    private String user_birth;
    private String user_phone;
    private int user_level;
    private String access_token;

    private final int READ_EXTERNAL_STORAGE_PERMISSION = 10001;

    private static final Logger logger = LoggerFactory.getLogger(EditUserActivity.class);
    private static final int PICK_PHOTO = 777;
    private RealPathUtil realPathUtil;
    private String avatarPath;
    private int user_gender = 0;
    private SharedPreferences loginInfo;

    private ArrayList<FavoriteCategory> favoriteList;

    private UserServerConnection userServerConnection;
    private SongServerConnection songServerConnection;

    private ArrayList<SongCategory> songList;
    private ProgressDialog progressDialog;
    private Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        setUserInfo();
        checkPermission();
        init();
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

    private Bitmap encodeImage(Uri uri) {
        Bitmap bm = null;
        try {
            bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            ExifInterface exifInterface = new ExifInterface(avatarPath);
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






    private void init() {
        iv_edit_profile_avatar = (CircleImageView) findViewById(R.id.iv_edit_profile_avatar);
        ib_edit_profile_avatar = (ImageButton) findViewById(R.id.ib_edit_profile_avatar);
        tv_edit_profile_email = (TextView) findViewById(R.id.tv_edit_profile_email);
        et_edit_profile_name = (EditText) findViewById(R.id.et_edit_profile_name);
        et_edit_profile_birth = (EditText) findViewById(R.id.et_edit_profile_birth);
        et_edit_profile_phone = (EditText) findViewById(R.id.et_edit_profile_phone);
        rb_female = (RadioButton) findViewById(R.id.rb_female);
        rb_man = (RadioButton) findViewById(R.id.rb_man);
        rv_profile_favorite = (RecyclerView) findViewById(R.id.rv_profile_favorite);
        rv_profile_song = (RecyclerView) findViewById(R.id.rv_profile_song);
        realPathUtil = new RealPathUtil();


        tv_edit_profile_email.setText(user_email);
        tv_edit_profile_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditUserActivity.this, "이메일은 변경할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        et_edit_profile_name.setText(user_name);
        et_edit_profile_phone.setText(user_phone);
        Glide.with(EditUserActivity.this).load(getString(R.string.cloud_front_user_avatar) + user_avatar).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_edit_profile_avatar);

        et_edit_profile_name.setFilters(new InputFilter[]{characterFilter});


        et_edit_profile_birth.setText(user_birth);
        et_edit_profile_birth.setFocusable(false);
        et_edit_profile_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePicker = new DatePickerDialog(v.getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        dateStr = String.valueOf(year) + "-" + String.valueOf((month + 1)) + "-" + String.valueOf(dayOfMonth);
                        try {
                            Date date = formatter.parse(dateStr);
                            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
                            birthDate = transFormat.format(date);
                            et_edit_profile_birth.setText(birthDate);
                        } catch (ParseException e) {
                            log(e);
                        }

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)
                        , calendar.get(Calendar.DAY_OF_MONTH));

                datePicker.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                datePicker.show();
            }
        });


        userServerConnection = new HeaderInterceptor(access_token).getClientForUserServer().create(UserServerConnection.class);
        Call<Integer> call_gender_check = userServerConnection.check_gender(user_id);
        call_gender_check.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int checked = response.body();
                    if (checked != 0) {
                        if (checked == 1) {
                            rb_man.setChecked(true);
                            user_gender = 1;
                        } else {
                            rb_female.setChecked(true);
                            user_gender = 2;
                        }
                    }
                } else {
                    Toast.makeText(EditUserActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                log(t);
                Toast.makeText(EditUserActivity.this, "네트워크가 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });

        rb_man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_gender = 1;
            }
        });

        rb_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_gender = 2;
            }
        });

        ib_edit_profile_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_PHOTO);
            }
        });

        userServerConnection = new HeaderInterceptor(access_token).getClientForUserServer().create(UserServerConnection.class);
        Call<ArrayList<FavoriteCategory>> call_favorite_list = userServerConnection.favorite_category_List();
        call_favorite_list.enqueue(new Callback<ArrayList<FavoriteCategory>>() {
            @Override
            public void onResponse(Call<ArrayList<FavoriteCategory>> call, Response<ArrayList<FavoriteCategory>> response) {
                if (response.isSuccessful()) {
                    favoriteList = response.body();

                    profileOptionAdapter = new ProfileOptionAdapter(EditUserActivity.this, favoriteList, R.layout.item_text);
                    rv_profile_favorite.setAdapter(profileOptionAdapter);
                    rv_profile_favorite.setLayoutManager(new GridLayoutManager(EditUserActivity.this, 3));
                } else {
                    Toast.makeText(EditUserActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<FavoriteCategory>> call, Throwable t) {
                log(t);
                Toast.makeText(EditUserActivity.this, "네트워크가 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });

        songServerConnection = new HeaderInterceptor(access_token).getClientForSongServer().create(SongServerConnection.class);
        Call<ArrayList<SongCategory>> call_song_category_list = songServerConnection.song_category_List();
        call_song_category_list.enqueue(new Callback<ArrayList<SongCategory>>() {
            @Override
            public void onResponse(Call<ArrayList<SongCategory>> call, Response<ArrayList<SongCategory>> response) {
                if (response.isSuccessful()) {
                    songList = response.body();
                    profileSongAdapter = new ProfileSongAdapter(EditUserActivity.this, songList, R.layout.item_text);
                    rv_profile_song.setAdapter(profileSongAdapter);
                    rv_profile_song.setLayoutManager(new GridLayoutManager(EditUserActivity.this, 3));
                } else {
                    Toast.makeText(EditUserActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SongCategory>> call, Throwable t) {
                log(t);
                Toast.makeText(EditUserActivity.this, "네트워크가 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });

        btn_edit_profile = (Button) findViewById(R.id.btn_edit_profile);
        btn_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_name = et_edit_profile_name.getText().toString();
                user_birth = et_edit_profile_birth.getText().toString();
                user_phone = et_edit_profile_phone.getText().toString();

                progressDialog = new ProgressDialog(EditUserActivity.this);
                progressDialog.show();
                progressDialog.getWindow().setBackgroundDrawable(new
                        ColorDrawable(Color.TRANSPARENT));
                progressDialog.setContentView(R.layout.progress_dialog);

                HashMap<String, String> map = new HashMap<>();
                map.put("name", user_name);
                map.put("birth", user_birth);
                map.put("profile_phone", user_phone);
                map.put("gender", String.valueOf(user_gender));

                userServerConnection = new HeaderInterceptor(access_token).getClientForUserServer().create(UserServerConnection.class);
                Call<ResponseBody> call_update_user_info = userServerConnection.update_user_info(user_id, map);
                call_update_user_info.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            //scss

                            //avatar
                            if (uri != null) {
                                userServerConnection = new HeaderInterceptor(access_token).getClientForUserServer().create(UserServerConnection.class);
                                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), addBase64Bitmap(encodeImage(uri)));
                                Call<ResponseBody> call_update_user_avatar = userServerConnection.update_user_avatar(user_id, requestBody);
                                call_update_user_avatar.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        if (response.isSuccessful()) {
                                            //scss
                                            resetUserinfo();
                                        } else {
                                            Toast.makeText(EditUserActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        log(t);
                                        Toast.makeText(EditUserActivity.this, "네트워크가 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                //sync
                                resetUserinfo();
                            }


                            //favorite and song category
                            final int favorite_size = favoriteList.size();
                            //photo_delete prior favorite list
                            userServerConnection = new HeaderInterceptor(access_token).getClientForUserServer().create(UserServerConnection.class);
                            Call<ResponseBody> call_delete_favorite_list = userServerConnection.delete_favorite(user_id);
                            call_delete_favorite_list.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()) {
                                        for (int i = 0; i < favorite_size; i++) {
                                            if (favoriteList.get(i).isChecked()) {
                                                userServerConnection = new HeaderInterceptor(access_token).getClientForUserServer().create(UserServerConnection.class);
                                                HashMap<String, String> map = new HashMap<>();
                                                map.put("favorite_id", String.valueOf(favoriteList.get(i).getId()));

                                                Call<ResponseBody> call_insert_favorite = userServerConnection.insert_favorite(user_id, map);
                                                call_insert_favorite.enqueue(new Callback<ResponseBody>() {
                                                    @Override
                                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                        if (!response.isSuccessful()) {
                                                            Toast.makeText(EditUserActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                                                        }
                                                        //scss
                                                    }

                                                    @Override
                                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                        log(t);
                                                        Toast.makeText(EditUserActivity.this, "네트워크가 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                        }
                                    } else {
                                        Toast.makeText(EditUserActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    log(t);
                                    Toast.makeText(EditUserActivity.this, "네트워크가 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                }
                            });


                            final int song_size = songList.size();
                            //photo_delete prior song category list
                            userServerConnection = new HeaderInterceptor(access_token).getClientForUserServer().create(UserServerConnection.class);
                            Call<ResponseBody> call_delete_song_list = userServerConnection.delete_song_category(user_id);
                            call_delete_song_list.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()) {
                                        for (int i = 0; i < song_size; i++) {
                                            if (songList.get(i).isChecked()) {
                                                userServerConnection = new HeaderInterceptor(access_token).getClientForUserServer().create(UserServerConnection.class);
                                                HashMap<String, String> map = new HashMap<>();
                                                map.put("song_category_id", String.valueOf(songList.get(i).getId()));
                                                Call<ResponseBody> call_insert_song_category = userServerConnection.insert_song_category(user_id, map);
                                                call_insert_song_category.enqueue(new Callback<ResponseBody>() {
                                                    @Override
                                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                        if (!response.isSuccessful()) {
                                                            Toast.makeText(EditUserActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                                                        }
                                                        //scss
                                                    }

                                                    @Override
                                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                        log(t);
                                                        Toast.makeText(EditUserActivity.this, "네트워크가 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                        }
                                    } else {
                                        Toast.makeText(EditUserActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    log(t);
                                    Toast.makeText(EditUserActivity.this, "네트워크가 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                }
                            });


                            int sleepTime = 500;
                            if (uri != null) {
                                sleepTime = 1500;
                            }
                            try {
                                Thread.sleep(sleepTime);
                            } catch (InterruptedException e) {
                                log(e);
                            }

                            progressDialog.dismiss();
                            //intent

                            Intent intent = new Intent(EditUserActivity.this, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(EditUserActivity.this, "프로필이 변경되었습니다.", Toast.LENGTH_LONG).show();

                            int finishListSize = finishList.size();
                            for (int i = 0; i < finishListSize; i++) {
                                finishList.get(i).finish();
                            }
                            finish();

                        } else {
                            Toast.makeText(EditUserActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        log(t);
                        Toast.makeText(EditUserActivity.this, "네트워크가 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }

    public void setToolbar(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolBar);
        ImageView toolbar_back = (ImageView) toolbar.findViewById(R.id.toolbar_back);
        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText("프로필 설정");

        Button toolbar_complete = (Button) toolbar.findViewById(R.id.toolbar_complete);
        toolbar_complete.setVisibility(View.INVISIBLE);
    }

    private void resetUserinfo() {
        userServerConnection = new HeaderInterceptor(access_token).getClientForUserServer().create(UserServerConnection.class);
        Call<User> call_user_info = userServerConnection.user_Info(user_id);
        call_user_info.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User userInfo = response.body();

                    loginInfo = getSharedPreferences("loginInfo", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor_update = loginInfo.edit();
                    editor_update.putString("user_name", userInfo.getName());
                    editor_update.putString("user_email", userInfo.getEmail());
                    editor_update.putString("user_birth", userInfo.getBirth());
                    editor_update.putString("user_avatar", userInfo.getAvatar());
                    editor_update.putString("user_phone", userInfo.getPhone());
                    editor_update.apply();
                } else {
                    Toast.makeText(EditUserActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                log(t);
                Toast.makeText(EditUserActivity.this, "네트워크가 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_PHOTO) {
                uri = data.getData();
                try {
                    avatarPath = realPathUtil.getRealPathFromURI_API19(this, uri);
                } catch (RuntimeException e) {
                    log(e);
                    avatarPath = realPathUtil.getRealPathFromURI_API11to18(this, uri);
                }

                Glide.with(EditUserActivity.this).load(uri).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_edit_profile_avatar);
            }
        }

    }

    private class ProfileOptionViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_option;

        public ProfileOptionViewHolder(View itemView) {
            super(itemView);
            tv_option = (TextView) itemView.findViewById(R.id.tv_option);
        }
    }

    private class ProfileOptionAdapter extends RecyclerView.Adapter<ProfileOptionViewHolder> {
        private Context context;
        private ArrayList<FavoriteCategory> favoriteList;
        private int layout;

        public ProfileOptionAdapter(Context context, ArrayList<FavoriteCategory> favoriteList, int layout) {
            this.context = context;
            this.favoriteList = favoriteList;
            this.layout = layout;
        }

        @Override
        public ProfileOptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ProfileOptionViewHolder profileOptionViewHolder = new ProfileOptionViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
            return profileOptionViewHolder;
        }

        @Override
        public void onBindViewHolder(final ProfileOptionViewHolder holder, final int position) {
            holder.tv_option.setText(favoriteList.get(position).getName());

            userServerConnection = new HeaderInterceptor(access_token).getClientForUserServer().create(UserServerConnection.class);
            Call<Integer> call_check_favorite = userServerConnection.check_favorite(user_id, favoriteList.get(position).getId());
            call_check_favorite.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if (response.isSuccessful()) {
                        int checked = response.body();

                        if (checked > 0) {
                            favoriteList.get(position).setChecked(true);
                            holder.tv_option.setBackgroundResource(R.drawable.round_corner_border_brown_r30);
                            holder.tv_option.setTextColor(Color.parseColor("#cc3a1c"));
                        } else {
                            holder.tv_option.setTextColor(Color.parseColor("#999999"));
                        }
                    } else {
                        Toast.makeText(EditUserActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    log(t);
                    Toast.makeText(EditUserActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                }
            });


            holder.tv_option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (favoriteList.get(position).isChecked()) {
                        holder.tv_option.setBackgroundResource(R.drawable.round_corner_border_gray_r30);
                        holder.tv_option.setTextColor(Color.parseColor("#999999"));
                        favoriteList.get(position).setChecked(false);
                    } else {
                        holder.tv_option.setBackgroundResource(R.drawable.round_corner_border_brown_r30);
                        holder.tv_option.setTextColor(Color.parseColor("#cc3a1c"));
                        favoriteList.get(position).setChecked(true);
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return favoriteList.size();
        }
    }

    private class ProfileSongViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_option;

        public ProfileSongViewHolder(View itemView) {
            super(itemView);
            tv_option = (TextView) itemView.findViewById(R.id.tv_option);
        }
    }

    private class ProfileSongAdapter extends RecyclerView.Adapter<ProfileSongViewHolder> {
        private Context context;
        private ArrayList<SongCategory> songList;
        private int layout;

        public ProfileSongAdapter(Context context, ArrayList<SongCategory> songList, int layout) {
            this.context = context;
            this.songList = songList;
            this.layout = layout;
        }

        @Override
        public ProfileSongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ProfileSongViewHolder profileOptionViewHolder = new ProfileSongViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
            return profileOptionViewHolder;
        }

        @Override
        public void onBindViewHolder(final ProfileSongViewHolder holder, final int position) {
            holder.tv_option.setText(songList.get(position).getName());
            userServerConnection = new HeaderInterceptor(access_token).getClientForUserServer().create(UserServerConnection.class);

            Call<Integer> call_check_song_category = userServerConnection.check_song_category(user_id, songList.get(position).getId());
            call_check_song_category.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if (response.isSuccessful()) {
                        int checked = response.body();

                        if (checked > 0) {
                            songList.get(position).setChecked(true);
                            holder.tv_option.setBackgroundResource(R.drawable.round_corner_border_brown_r30);
                            holder.tv_option.setTextColor(Color.parseColor("#cc3a1c"));
                        } else {
                            holder.tv_option.setTextColor(Color.parseColor("#999999"));
                        }
                    } else {
                        Toast.makeText(context, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    log(t);
                    Toast.makeText(EditUserActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                }
            });

            holder.tv_option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (songList.get(position).isChecked()) {
                        holder.tv_option.setBackgroundResource(R.drawable.round_corner_border_gray_r30);
                        holder.tv_option.setTextColor(Color.parseColor("#999999"));
                        songList.get(position).setChecked(false);
                    } else {
                        holder.tv_option.setBackgroundResource(R.drawable.round_corner_border_brown_r30);
                        holder.tv_option.setTextColor(Color.parseColor("#cc3a1c"));
                        songList.get(position).setChecked(true);
                    }
                }
            });


        }

        @Override
        public int getItemCount() {
            return songList.size();
        }
    }

    InputFilter characterFilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            int length = source.length();
            for (int i = 0; i < length; i++) {
                Pattern pattern = Pattern.compile("^[a-zA-Z0-9가-힣ㄱ-ㅎㅏ-ㅣ\\u318D\\u119E\\u11A2\\u2022\\u2025a\\u00B7\\uFE55]+$");
                if (!pattern.matcher(source).matches()) {
                    Toast.makeText(EditUserActivity.this, "특수문자는 입력이 불가합니다.", Toast.LENGTH_SHORT).show();
                    return "";
                }
            }
            return null;
        }
    };



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
