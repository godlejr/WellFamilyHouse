package com.demand.well_family.well_family.users;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.LoginActivity;
import com.demand.well_family.well_family.MainActivity;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.connection.Server_Connection;
import com.demand.well_family.well_family.dto.Check;
import com.demand.well_family.well_family.log.LogFlag;
import com.demand.well_family.well_family.market.MarketMainActivity;
import com.demand.well_family.well_family.memory_sound.SongMainActivity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.demand.well_family.well_family.LoginActivity.finishList;

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

    private ImageView iv_family_activity_avatar;
    private TextView tv_family_activity_name;
    private TextView tv_family_activity_birth;
    private TextView tv_family_activity_phone;
    private TextView tv_family_activity_email;
    private LinearLayout ll_memory_sound_story_list;
    private LinearLayout ll_user_edit;
    private ImageView iv_user_call;
    private LinearLayout ll_user_phone_info;
    private ImageButton ib_edit_profile;

    //toolbar
    private DrawerLayout dl;
    private Server_Connection server_connection;

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
        setToolbar(this.getWindow().getDecorView(), this, story_user_name);
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

                Intent intent = new Intent(UserActivity.this, UserActivity.class);
                //userinfo
                intent.putExtra("story_user_id", user_id);
                intent.putExtra("story_user_email", user_email);
                intent.putExtra("story_user_birth", user_birth);
                intent.putExtra("story_user_phone", user_phone);
                intent.putExtra("story_user_name", user_name);
                intent.putExtra("story_user_level", user_level);
                intent.putExtra("story_user_avatar", user_avatar);

                startActivity(intent);
                if (user_id == story_user_id) {
                    finish();
                }
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
                        intent = new Intent(UserActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;

                    case R.id.menu_search:
                        Toast.makeText(getApplicationContext(), "준비중입니다.", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.menu_market:
                        intent = new Intent(UserActivity.this, MarketMainActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.menu_setting:
                        Toast.makeText(getApplicationContext(), "준비중입니다.", Toast.LENGTH_SHORT).show();
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

                        intent = new Intent(UserActivity.this, LoginActivity.class);
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

    public void setBack() {
        finish();
    }

    private void setUserFunc() {
        tv_family_activity_phone = (TextView) findViewById(R.id.tv_family_activity_phone);
        tv_family_activity_phone.setText(story_user_phone);
        iv_user_call = (ImageView) findViewById(R.id.iv_user_call);
        ll_user_phone_info = (LinearLayout) findViewById(R.id.ll_user_phone_info);

        if (user_id == story_user_id) {
            //me
            ll_user_phone_info.setVisibility(View.GONE);
        } else {
            server_connection = Server_Connection.retrofit.create(Server_Connection.class);
            HashMap<String, String> map = new HashMap<>();
            map.put("user_id", String.valueOf(user_id));

            Call<ArrayList<Check>> call_family_check = server_connection.family_check(story_user_id, map);
            call_family_check.enqueue(new Callback<ArrayList<Check>>() {
                @Override
                public void onResponse(Call<ArrayList<Check>> call, Response<ArrayList<Check>> response) {
                    if (response.body().get(0).getChecked() > 0) {
                        //family
                        tv_family_activity_phone.setOnClickListener(UserActivity.this);
                        iv_user_call.setOnClickListener(UserActivity.this);
                    } else {
                        //public
                        ll_user_phone_info.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Check>> call, Throwable t) {
                    log(t);
                    Toast.makeText(UserActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    private void getUserData() {
        iv_family_activity_avatar = (ImageView) findViewById(R.id.iv_family_activity_avatar);
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
        if(user_id != story_user_id){
            ll_user_edit = (LinearLayout)findViewById(R.id.ll_user_edit);
            ll_user_edit.setVisibility(View.GONE);
        }

        ib_edit_profile = (ImageButton) findViewById(R.id.ib_edit_profile);
        ib_edit_profile.setOnClickListener(new View.OnClickListener() {
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
