package com.demand.well_family.well_family.users;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.repository.UserServerConnection;
import com.demand.well_family.well_family.repository.interceptor.HeaderInterceptor;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.dialog.popup.photo.activity.PhotoPopupActivity;
import com.demand.well_family.well_family.util.ErrorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.demand.well_family.well_family.main.login.activity.LoginActivity.finishList;

/**
 * Created by Dev-0 on 2017-01-23.
 */

public class UserActivity extends Activity implements View.OnClickListener {
    private int user_id;
    private String user_name;
    private int user_level;
    private String user_avatar;
    private String user_email;
    private String user_phone;
    private String user_birth;

    private int story_user_id;
    private String story_user_name;
    private int story_user_level;
    private String story_user_avatar;
    private String story_user_email;
    private String story_user_phone;
    private String story_user_birth;
    private String access_token;

    private ImageView iv_family_activity_avatar;
    private TextView tv_family_activity_name;
    private TextView tv_family_activity_birth;
    private TextView tv_family_activity_phone;
    private TextView tv_family_activity_phone2;
    private TextView tv_family_activity_email;
    private LinearLayout ll_memory_sound_story_list;
    private LinearLayout ll_user_edit;
    private LinearLayout ll_edit_profile;
    private RelativeLayout ll_user_phone_info;
    private ImageView iv_user_call;


    private UserServerConnection userServerConnection;

    private static final Logger logger = LoggerFactory.getLogger(UserActivity.class);
    private SharedPreferences loginInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        setUserInfo();

        story_user_id = getIntent().getIntExtra("story_user_id", 0);
        story_user_name = getIntent().getStringExtra("story_user_name");
        story_user_level = getIntent().getIntExtra("story_user_level", 0);
        story_user_avatar = getIntent().getStringExtra("story_user_avatar");
        story_user_email = getIntent().getStringExtra("story_user_email");
        story_user_phone = getIntent().getStringExtra("story_user_phone");
        story_user_birth = getIntent().getStringExtra("story_user_birth");
        finishList.add(this);

        init();
        getUserData();
        setUserFunc();
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
        setToolbar(this.getWindow().getDecorView(), this, story_user_name);
    }


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

    public void setBack() {
        finish();
    }

    private void setUserFunc() {
        tv_family_activity_phone = (TextView) findViewById(R.id.tv_family_activity_phone);
        tv_family_activity_phone.setText(story_user_phone);

        RelativeLayout rl_call_phone = (RelativeLayout) findViewById(R.id.rl_call_phone);
        rl_call_phone.bringToFront();
        tv_family_activity_phone2 = (TextView) findViewById(R.id.tv_family_activity_phone2);
        String phone = "";
        int phone_length = story_user_phone.length();
        if (phone_length == 11) {
            phone = story_user_phone.substring(0, 3) + "-" + story_user_phone.substring(3, 7) + "-" + story_user_phone.substring(7, 11);
        } else if (phone_length == 10) {
            phone = story_user_phone.substring(0, 3) + "-" + story_user_phone.substring(3, 6) + "-" + story_user_phone.substring(6, 10);
        }
        tv_family_activity_phone2.setText(phone);

        iv_user_call = (ImageView) findViewById(R.id.iv_user_call);
        ll_user_phone_info = (RelativeLayout) findViewById(R.id.ll_user_phone_info);

        if (user_id == story_user_id) {
            //me
            ll_user_phone_info.setVisibility(View.GONE);
        } else {
            userServerConnection = new HeaderInterceptor(access_token).getClientForUserServer().create(UserServerConnection.class);

            Call<Integer> call_family_check = userServerConnection.family_check(story_user_id, user_id);
            call_family_check.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if (response.isSuccessful()) {
                        if (response.body() > 0) {
                            //family
                            tv_family_activity_phone.setOnClickListener(UserActivity.this);
                            iv_user_call.setOnClickListener(UserActivity.this);
                        } else {
                            //public
                            ll_user_phone_info.setVisibility(View.GONE);
                        }
                    } else {
                        Toast.makeText(UserActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    log(t);
                    Toast.makeText(UserActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    private void getUserData() {
        iv_family_activity_avatar = (ImageView) findViewById(R.id.iv_family_activity_avatar);
        iv_family_activity_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PhotoPopupActivity.class);

                String[] filename = story_user_avatar.split("\\.");
                ArrayList<Photo> photo = new ArrayList<Photo>();
                photo.add(new Photo(0, 0, 0, filename[0], filename[1]));

                intent.putExtra("photoList", photo);
                intent.putExtra("from", "UserActivity");
                startActivity(intent);
            }
        });

        Glide.with(this).load(getString(R.string.cloud_front_user_avatar) + story_user_avatar).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_family_activity_avatar);

        tv_family_activity_name = (TextView) findViewById(R.id.tv_family_activity_name);
        tv_family_activity_name.setText(story_user_name);

        tv_family_activity_birth = (TextView) findViewById(R.id.tv_family_activity_birth);
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(user_birth);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일생");
            tv_family_activity_birth.setText(sdf.format(date));
        } catch (ParseException e) {
            log(e);
        }

        tv_family_activity_email = (TextView) findViewById(R.id.tv_family_activity_email);
        tv_family_activity_email.setText(story_user_email);
    }

    private int getAge(String dateTime) {
        int birthYear = Integer.valueOf(dateTime.split("-")[0]);
        int currentYear = new GregorianCalendar().get(Calendar.YEAR);
        return (currentYear - birthYear) + 1;
    }

    private void init() {
        if (user_id != story_user_id) {
            ll_user_edit = (LinearLayout) findViewById(R.id.ll_user_edit);
            ll_user_edit.setVisibility(View.GONE);
        }


        ll_edit_profile = (LinearLayout) findViewById(R.id.ll_edit_profile);
        ll_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, EditUserActivity.class);
                startActivity(intent);
            }
        });

        //memory sound
        ll_memory_sound_story_list = (LinearLayout) findViewById(R.id.ll_memory_sound_story_list);
        ll_memory_sound_story_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, SongStoryActivity.class);
                //story user info
                intent.putExtra("story_user_id", story_user_id);
                intent.putExtra("story_user_email", story_user_email);
                intent.putExtra("story_user_birth", story_user_birth);
                intent.putExtra("story_user_phone", story_user_phone);
                intent.putExtra("story_user_name", story_user_name);
                intent.putExtra("story_user_level", story_user_level);
                intent.putExtra("story_user_avatar", story_user_avatar);

                startActivity(intent);
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_family_activity_phone:
            case R.id.iv_user_call:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + story_user_phone));
                startActivity(intent);
                break;
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
