package com.demand.well_family.well_family.users;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.exercise.base.activity.ExerciseActivity;
import com.demand.well_family.well_family.falldiagnosis.base.activity.FallDiagnosisActivity;
import com.demand.well_family.well_family.main.login.activity.LoginActivity;
import com.demand.well_family.well_family.main.base.activity.MainActivity;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.repository.SongStoryServerConnection;
import com.demand.well_family.well_family.repository.UserServerConnection;
import com.demand.well_family.well_family.family.manage.activity.ManageFamilyActivity;
import com.demand.well_family.well_family.repository.interceptor.NetworkInterceptor;
import com.demand.well_family.well_family.setting.base.activity.SettingActivity;
import com.demand.well_family.well_family.dto.SongPhoto;
import com.demand.well_family.well_family.dto.SongStory;
import com.demand.well_family.well_family.dto.SongStoryEmotionData;
import com.demand.well_family.well_family.dto.SongStoryInfo;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.market.MarketMainActivity;
import com.demand.well_family.well_family.memory_sound.SongMainActivity;
import com.demand.well_family.well_family.users.base.activity.UserActivity;
import com.demand.well_family.well_family.util.ErrorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.demand.well_family.well_family.main.login.activity.LoginActivity.finishList;

/**
 * Created by ㅇㅇ on 2017-02-02.
 */

public class SongStoryActivity extends Activity {
    //user_inf0(accessor)
    private int user_id;
    private String user_name;
    private int user_level;
    private String user_avatar;
    private String user_email;
    private String user_phone;
    private String user_birth;
    private String access_token;

    //story user info
    private int story_user_id;
    private String story_user_name;
    private int story_user_level;
    private String story_user_avatar;
    private String story_user_email;
    private String story_user_phone;
    private String story_user_birth;

    private UserServerConnection userServerConnection;
    private SongStoryServerConnection songStoryServerConnection;

    private RecyclerView rv_user_activity_sound;
    private ArrayList<SongStoryInfo> storyList;
    private ContentAdapter contentAdapter;


    //content
    private int content_size;
    private boolean content_isFinished = false;
    private ArrayList<SongStory> contentList;
    private int contentInsertCount = 0;
    private static final int CONTENTS_OFFSET = 20;


    //request code
    private static final int DETAIL_REQUEST = 2;

    //result code
    private static final int EDIT = 4;
    private static final int DELETE = 5;

    //toolbar
    private DrawerLayout dl;
    private ProgressDialog progressDialog;
    private ContentHandler contentHandler;
    private MainHandler mainHandler;
    private Message msg;

    private Call<ArrayList<SongStory>> call_content;

    //emotion
    private EmotionAdapter emotionAdapter;

    private static final Logger logger = LoggerFactory.getLogger(SongStoryActivity.class);
    private SharedPreferences loginInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sound);

        finishList.add(this);

        init();
        setUserInfo();

        getContentsData();

    }

    private void init() {
        story_user_id = getIntent().getIntExtra("story_user_id", 0);
        story_user_name = getIntent().getStringExtra("story_user_name");
        story_user_level = getIntent().getIntExtra("story_user_level", 0);
        story_user_avatar = getIntent().getStringExtra("story_user_avatar");
        story_user_email = getIntent().getStringExtra("story_user_email");
        story_user_phone = getIntent().getStringExtra("story_user_phone");
        story_user_birth = getIntent().getStringExtra("story_user_birth");
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
        setToolbar(this.getWindow().getDecorView(), this.getApplicationContext(), "추억 소리");
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

                Intent intent = new Intent(SongStoryActivity.this, UserActivity.class);
                //userinfo
                intent.putExtra("story_user_id", user_id);
                intent.putExtra("story_user_email", user_email);
                intent.putExtra("story_user_birth", user_birth);
                intent.putExtra("story_user_phone", user_phone);
                intent.putExtra("story_user_name", user_name);
                intent.putExtra("story_user_level", user_level);
                intent.putExtra("story_user_avatar", user_avatar);


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
                        intent = new Intent(SongStoryActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                        break;

                    case R.id.menu_family:
                        intent = new Intent(SongStoryActivity.this, ManageFamilyActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                        break;

                    case R.id.menu_market:
                        intent = new Intent(SongStoryActivity.this, MarketMainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
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
                        editor.remove("access_token");
                        editor.commit();

                        intent = new Intent(SongStoryActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;

//                    App 이용하기
                    case R.id.menu_falldiagnosis:
                        navigateToFallDiagnosisActivity();
                        break;
                    case R.id.menu_exercise:
                        navigateToExerciseActivity();
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
    public void navigateToFallDiagnosisActivity() {
        Intent intent = new Intent(this, FallDiagnosisActivity.class);
        startActivity(intent);
    }

    public void navigateToExerciseActivity() {
        Intent intent = new Intent(this, ExerciseActivity.class);
        startActivity(intent);
    }
    public void setBack() {
        finish();
    }

    private void getContentsData() {
        rv_user_activity_sound = (RecyclerView) findViewById(R.id.rv_user_activity_sound);

        //<----------------- getContentsData 다이얼로그
        mainHandler = new MainHandler();
        progressDialog = new ProgressDialog(SongStoryActivity.this);
        progressDialog.show();
        progressDialog.getWindow().setBackgroundDrawable(new
                ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.progress_dialog);

        storyList = new ArrayList<>();
        //accessor & story writer range check

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (user_id == story_user_id) {
                    //me
                    userServerConnection = new NetworkInterceptor(access_token).getClientForUserServer().create(UserServerConnection.class);
                    call_content = userServerConnection.song_story_List_Me(story_user_id);

                    call_content.enqueue(new Callback<ArrayList<SongStory>>() {
                        @Override
                        public void onResponse(Call<ArrayList<SongStory>> call, Response<ArrayList<SongStory>> response) {
                            if (response.isSuccessful()) {
                                contentList = response.body();
                                content_size = contentList.size();

                                if (content_size == 0) {
                                    //contents  비워 있음
                                } else {
//                                content_size = contentList.size();
                                    int loopSize = 0;

                                    if (content_size <= CONTENTS_OFFSET) {
                                        loopSize = content_size;
                                        content_isFinished = true;
                                    } else {
                                        loopSize = CONTENTS_OFFSET;
                                        content_size -= loopSize;
                                    }

                                    for (int i = 0; i < loopSize; i++) {
                                        storyList.add(new SongStoryInfo(story_user_id, story_user_name, story_user_avatar, contentList.get(i).getId(), contentList.get(i).getRange_id(),
                                                contentList.get(i).getSong_id(), contentList.get(i).getSong_title(), contentList.get(i).getSong_singer(),
                                                contentList.get(i).getRecord_file(), contentList.get(i).getCreated_at(), contentList.get(i).getContent(), contentList.get(i).getLocation(), contentList.get(i).getHits()));
                                    }
                                }
                                contentAdapter = new ContentAdapter(SongStoryActivity.this, storyList, R.layout.item_activity_sound);

                                Bundle bundle = new Bundle();
                                bundle.putSerializable("contentAdapter", contentAdapter);

                                msg = new Message();
                                msg.setData(bundle);
                                mainHandler.sendMessage(msg);

                                mainHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            progressDialog.dismiss();
                                        } catch (Exception e) {
                                            log(e);
                                        }
                                    }
                                }, 200);
                            } else {
                                Toast.makeText(SongStoryActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<SongStory>> call, Throwable t) {
                            log(t);
                            Toast.makeText(SongStoryActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    userServerConnection = new NetworkInterceptor(access_token).getClientForUserServer().create(UserServerConnection.class);

                    Call<Integer> call_family_check = userServerConnection.family_check(story_user_id, user_id);
                    call_family_check.enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            if (response.isSuccessful()) {
                                if (response.body() > 0) {
                                    //family
                                    userServerConnection = new NetworkInterceptor(access_token).getClientForUserServer().create(UserServerConnection.class);
                                    call_content = userServerConnection.song_story_List_Family(story_user_id);
                                    call_content.enqueue(new Callback<ArrayList<SongStory>>() {
                                        @Override
                                        public void onResponse(Call<ArrayList<SongStory>> call, Response<ArrayList<SongStory>> response) {
                                            if (response.isSuccessful()) {
                                                contentList = response.body();
                                                content_size = contentList.size();

                                                if (content_size == 0) {
                                                    //contents  비워 있음
                                                } else {
//                                            content_size = contentList.size();
                                                    int loopSize = 0;

                                                    if (content_size <= CONTENTS_OFFSET) {
                                                        loopSize = content_size;
                                                        content_isFinished = true;
                                                    } else {
                                                        loopSize = CONTENTS_OFFSET;
                                                        content_size -= loopSize;
                                                    }

                                                    for (int i = 0; i < loopSize; i++) {
                                                        storyList.add(new SongStoryInfo(story_user_id, story_user_name, story_user_avatar, contentList.get(i).getId(), contentList.get(i).getRange_id(),
                                                                contentList.get(i).getSong_id(), contentList.get(i).getSong_title(), contentList.get(i).getSong_singer(),
                                                                contentList.get(i).getRecord_file(), contentList.get(i).getCreated_at(), contentList.get(i).getContent(), contentList.get(i).getLocation(), contentList.get(i).getHits()));
                                                    }
                                                }
                                                contentAdapter = new ContentAdapter(SongStoryActivity.this, storyList, R.layout.item_activity_sound);

                                                Bundle bundle = new Bundle();
                                                bundle.putSerializable("contentAdapter", contentAdapter);

                                                msg = new Message();
                                                msg.setData(bundle);
                                                mainHandler.sendMessage(msg);

                                                mainHandler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            progressDialog.dismiss();
                                                        } catch (Exception e) {
                                                            log(e);
                                                        }
                                                    }
                                                }, 200);
                                            } else {
                                                Toast.makeText(SongStoryActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ArrayList<SongStory>> call, Throwable t) {
                                            log(t);
                                            Toast.makeText(SongStoryActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                } else {
                                    //public
                                    userServerConnection = new NetworkInterceptor(access_token).getClientForUserServer().create(UserServerConnection.class);
                                    call_content = userServerConnection.song_story_List_Public(story_user_id);
                                    call_content.enqueue(new Callback<ArrayList<SongStory>>() {
                                        @Override
                                        public void onResponse(Call<ArrayList<SongStory>> call, Response<ArrayList<SongStory>> response) {
                                            if (response.isSuccessful()) {
                                                contentList = response.body();
                                                content_size = contentList.size();

                                                if (content_size == 0) {
                                                    //contents  비워 있음
                                                } else {
//                                            content_size = contentList.size();
                                                    int loopSize = 0;

                                                    if (content_size <= CONTENTS_OFFSET) {
                                                        loopSize = content_size;
                                                        content_isFinished = true;
                                                    } else {
                                                        loopSize = CONTENTS_OFFSET;
                                                        content_size -= loopSize;
                                                    }

                                                    for (int i = 0; i < loopSize; i++) {
                                                        storyList.add(new SongStoryInfo(story_user_id, story_user_name, story_user_avatar, contentList.get(i).getId(), contentList.get(i).getRange_id(),
                                                                contentList.get(i).getSong_id(), contentList.get(i).getSong_title(), contentList.get(i).getSong_singer(),
                                                                contentList.get(i).getRecord_file(), contentList.get(i).getCreated_at(), contentList.get(i).getContent(), contentList.get(i).getLocation(), contentList.get(i).getHits()));
                                                    }
                                                }
                                                contentAdapter = new ContentAdapter(SongStoryActivity.this, storyList, R.layout.item_activity_sound);

                                                Bundle bundle = new Bundle();
                                                bundle.putSerializable("contentAdapter", contentAdapter);

                                                msg = new Message();
                                                msg.setData(bundle);
                                                mainHandler.sendMessage(msg);

                                                mainHandler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            progressDialog.dismiss();
                                                        } catch (Exception e) {
                                                            log(e);
                                                        }
                                                    }
                                                }, 200);
                                            } else {
                                                Toast.makeText(SongStoryActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ArrayList<SongStory>> call, Throwable t) {
                                            log(t);
                                            Toast.makeText(SongStoryActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(SongStoryActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
                            log(t);
                            Toast.makeText(SongStoryActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }).start();


        NestedScrollView nsv_song_story = (NestedScrollView) findViewById(R.id.nsv_song_story);
        nsv_song_story.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // Grab the last child placed in the ScrollView, we need it to determinate the bottom position.
                View view = (View) v.getChildAt(v.getChildCount() - 1);

                // Calculate the scrolldiff
                int diff = (view.getBottom() - (v.getHeight() + v.getScrollY()));

                // if diff is zero, then the bottom has been reached
                if (diff <= 0) {
                    // notify that we have reached the bottom
                    int loopSize = 0;
                    if (content_isFinished == false && content_size <= CONTENTS_OFFSET) {
                        loopSize = content_size;
                        contentInsertCount++;
                    } else {
                        loopSize = CONTENTS_OFFSET;
                        content_size -= loopSize;
                        contentInsertCount++;
                    }
                    if (content_isFinished == false) {
                        if (loopSize == content_size) {
                            content_isFinished = true;
                        }

                        progressDialog = new ProgressDialog(SongStoryActivity.this);
                        progressDialog.show();
                        progressDialog.getWindow().setBackgroundDrawable(new
                                ColorDrawable(Color.TRANSPARENT));
                        progressDialog.setContentView(R.layout.progress_dialog);
                        contentHandler = new ContentHandler();
                        final int finalLoopSize = loopSize;

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    //Thread 달기
                                    for (int i = (CONTENTS_OFFSET * contentInsertCount); i < finalLoopSize + (CONTENTS_OFFSET * contentInsertCount); i++) {
                                        storyList.add(new SongStoryInfo(story_user_id, story_user_name, story_user_avatar, contentList.get(i).getId(), contentList.get(i).getRange_id(),
                                                contentList.get(i).getSong_id(), contentList.get(i).getSong_title(), contentList.get(i).getSong_singer(),
                                                contentList.get(i).getRecord_file(), contentList.get(i).getCreated_at(), contentList.get(i).getContent(),
                                                contentList.get(i).getLocation(), contentList.get(i).getHits()));
                                        //handler
                                        msg = new Message();
                                        Bundle bundle = new Bundle();
                                        bundle.putInt("item_id", i);
                                        msg.setData(bundle);
                                        contentHandler.sendMessage(msg);
                                    }
                                } catch (Exception e) {
                                    log(e);
                                }
                                try {
                                    Thread.sleep(200);
                                } catch (InterruptedException e) {
                                    log(e);
                                }
                                progressDialog.dismiss();
                            }
                        }).start();
                    }
                }
            }
        });
    }

    class ContentHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            contentAdapter.notifyItemChanged(bundle.getInt("item_id"));
        }
    }

    class MainHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            rv_user_activity_sound.setAdapter((ContentAdapter) bundle.getSerializable("contentAdapter"));
            rv_user_activity_sound.setLayoutManager(new LinearLayoutManager(SongStoryActivity.this, LinearLayoutManager.VERTICAL, false));

        }
    }

    class ContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_sound_story_title, tv_sound_story_singer, tv_sound_story_user_name, tv_sound_story_content, tv_sound_story_location,
                tv_sound_story_like, tv_sound_story_comment, tv_sound_story_date;
        private CircleImageView iv_sound_story_avatar;
        private LinearLayout ll_sound_story_images_container, ll_sound_story_location, ll_sound_story_total;
        private ImageButton ib_item_sound_story_comment;
        private CheckBox cb_item_sound_story_like;
        private LayoutInflater story_images_inflater;
        private ImageView iv_sound_story_record;
        private RecyclerView rv_song_story_emotion;

        public ContentViewHolder(View itemView) {
            super(itemView);

            tv_sound_story_comment = (TextView) itemView.findViewById(R.id.tv_sound_story_comment);
            tv_sound_story_like = (TextView) itemView.findViewById(R.id.tv_sound_story_like);
            tv_sound_story_content = (TextView) itemView.findViewById(R.id.tv_sound_story_content);
            tv_sound_story_location = (TextView) itemView.findViewById(R.id.tv_sound_story_location);
            tv_sound_story_user_name = (TextView) itemView.findViewById(R.id.tv_sound_story_user_name);
            tv_sound_story_singer = (TextView) itemView.findViewById(R.id.tv_sound_story_singer);
            tv_sound_story_title = (TextView) itemView.findViewById(R.id.tv_sound_story_title);
            tv_sound_story_date = (TextView) itemView.findViewById(R.id.tv_sound_story_date);

            iv_sound_story_avatar = (CircleImageView) itemView.findViewById(R.id.iv_sound_story_avatar);
            ll_sound_story_total = (LinearLayout) itemView.findViewById(R.id.ll_sound_story_total);
            ll_sound_story_images_container = (LinearLayout) itemView.findViewById(R.id.ll_sound_story_images_container);
            ll_sound_story_location = (LinearLayout) itemView.findViewById(R.id.ll_sound_story_location);
            ib_item_sound_story_comment = (ImageButton) itemView.findViewById(R.id.ib_item_sound_story_comment);
            cb_item_sound_story_like = (CheckBox) itemView.findViewById(R.id.cb_item_sound_story_like);
            story_images_inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            iv_sound_story_record = (ImageView) itemView.findViewById(R.id.iv_sound_story_record);
            rv_song_story_emotion = (RecyclerView) itemView.findViewById(R.id.rv_song_story_emotion);

            cb_item_sound_story_like.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (storyList.get(getAdapterPosition()).getFirst_checked()) {
                        if (isChecked) {
                            songStoryServerConnection = new NetworkInterceptor(access_token).getClientForSongStoryServer().create(SongStoryServerConnection.class);
                            HashMap<String, String> map = new HashMap<>();
                            map.put("user_id", String.valueOf(user_id));

                            Call<ResponseBody> call_like = songStoryServerConnection.song_story_like_up(storyList.get(getAdapterPosition()).getStory_id(), map);
                            call_like.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()) {
                                        tv_sound_story_like.setText(String.valueOf(Integer.parseInt(tv_sound_story_like.getText().toString()) + 1));
                                        storyList.get(getAdapterPosition()).setChecked(true);
                                    } else {
                                        Toast.makeText(SongStoryActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    log(t);
                                    Toast.makeText(SongStoryActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            songStoryServerConnection = new NetworkInterceptor(access_token).getClientForSongStoryServer().create(SongStoryServerConnection.class);
                            Call<ResponseBody> call_dislike = songStoryServerConnection.song_story_like_down(storyList.get(getAdapterPosition()).getStory_id(), user_id);
                            call_dislike.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()) {
                                        tv_sound_story_like.setText(String.valueOf(Integer.parseInt(tv_sound_story_like.getText().toString()) - 1));
                                        storyList.get(getAdapterPosition()).setChecked(false);
                                    } else {
                                        Toast.makeText(SongStoryActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    log(t);
                                    Toast.makeText(SongStoryActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }
            });

            ll_sound_story_total.setOnClickListener(this);
            ib_item_sound_story_comment.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_sound_story_total:
                case R.id.ib_item_sound_story_comment:
                    Intent intent = new Intent(SongStoryActivity.this, SongStoryDetailActivity.class);

                    intent.putExtra("story_user_id", story_user_id);
                    intent.putExtra("story_user_name", story_user_name);
                    intent.putExtra("story_user_avatar", story_user_avatar);

                    intent.putExtra("story_id", storyList.get(getAdapterPosition()).getStory_id());

                    intent.putExtra("range_id", storyList.get(getAdapterPosition()).getRange_id());
                    intent.putExtra("song_id", storyList.get(getAdapterPosition()).getSong_id());
                    intent.putExtra("song_title", storyList.get(getAdapterPosition()).getSong_title());
                    intent.putExtra("song_singer", storyList.get(getAdapterPosition()).getSong_singer());
                    intent.putExtra("record_file", storyList.get(getAdapterPosition()).getRecord_file());
                    intent.putExtra("content", storyList.get(getAdapterPosition()).getContent());
                    intent.putExtra("created_at", storyList.get(getAdapterPosition()).getCreated_at());
                    intent.putExtra("location", storyList.get(getAdapterPosition()).getLocation());
                    intent.putExtra("hits", storyList.get(getAdapterPosition()).getHits());
                    intent.putExtra("like_checked", storyList.get(getAdapterPosition()).getChecked());

                    intent.putExtra("position", getAdapterPosition());
                    startActivityForResult(intent, DETAIL_REQUEST);

                    break;
            }
        }
    }

    class ContentAdapter extends RecyclerView.Adapter<ContentViewHolder> implements Serializable {
        private Context context;
        private ArrayList<SongStoryInfo> storyList;
        private int layout;

        //photo recycler
        private ArrayList<SongPhoto> photoList;

        public ContentAdapter(Context context, ArrayList<SongStoryInfo> storyList, int layout) {
            this.context = context;
            this.storyList = storyList;
            this.layout = layout;
        }

        @Override
        public ContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ContentViewHolder contentViewHolder = new ContentViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
            return contentViewHolder;
        }

        @Override
        public void onBindViewHolder(final ContentViewHolder holder, final int position) {
            //song title
            holder.tv_sound_story_title.setText(storyList.get(position).getSong_title());
            holder.tv_sound_story_singer.setText(storyList.get(position).getSong_singer());

            //user info
            Glide.with(context).load(getString(R.string.cloud_front_user_avatar) + story_user_avatar).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_sound_story_avatar);
            holder.tv_sound_story_user_name.setText(story_user_name);

            //content
            holder.tv_sound_story_content.setText(storyList.get(position).getContent());

            //date
            holder.tv_sound_story_date.setText(calculateTime(storyList.get(position).getCreated_at()));

            //location
            if (storyList.get(position).getLocation() != null && storyList.get(position).getLocation().length() != 0) {
                holder.ll_sound_story_location.setVisibility(View.VISIBLE);
                holder.tv_sound_story_location.setText(storyList.get(position).getLocation());
            } else {
                holder.ll_sound_story_location.setVisibility(View.GONE);
            }

            //record
            if (storyList.get(position).getRecord_file() != null && storyList.get(position).getRecord_file().length() != 0) {
                holder.iv_sound_story_record.setVisibility(View.VISIBLE);
            } else {
                holder.iv_sound_story_record.setVisibility(View.GONE);
            }

            // emotion
            songStoryServerConnection = new NetworkInterceptor(access_token).getClientForSongStoryServer().create(SongStoryServerConnection.class);
            Call<ArrayList<SongStoryEmotionData>> call_emotion_data = songStoryServerConnection.song_story_emotion_List(storyList.get(position).getStory_id());
            call_emotion_data.enqueue(new Callback<ArrayList<SongStoryEmotionData>>() {
                @Override
                public void onResponse(Call<ArrayList<SongStoryEmotionData>> call, Response<ArrayList<SongStoryEmotionData>> response) {
                    if (response.isSuccessful()) {
                        emotionAdapter = new EmotionAdapter(response.body(), SongStoryActivity.this, R.layout.item_emotion);

                        holder.rv_song_story_emotion.setAdapter(emotionAdapter);
                        holder.rv_song_story_emotion.setLayoutManager(new GridLayoutManager(SongStoryActivity.this, 2));
                    } else {
                        Toast.makeText(SongStoryActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<SongStoryEmotionData>> call, Throwable t) {
                    log(t);
                    Toast.makeText(SongStoryActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                }
            });


            //images
            songStoryServerConnection = new NetworkInterceptor(access_token).getClientForSongStoryServer().create(SongStoryServerConnection.class);
            Call<ArrayList<SongPhoto>> call_song_photo = songStoryServerConnection.song_story_photo_List(storyList.get(position).getStory_id());
            call_song_photo.enqueue(new Callback<ArrayList<SongPhoto>>() {
                @Override
                public void onResponse(Call<ArrayList<SongPhoto>> call, Response<ArrayList<SongPhoto>> response) {
                    if (response.isSuccessful()) {
                        photoList = response.body();
                        int photoListSize = photoList.size();

                        if (photoListSize == 0) {
                            holder.ll_sound_story_images_container.setVisibility(View.GONE);
                            holder.tv_sound_story_content.setMaxLines(15);
                        }
                        if (photoListSize == 1) {
                            holder.ll_sound_story_images_container.removeAllViews();
                            holder.ll_sound_story_images_container.setVisibility(View.VISIBLE);
                            holder.ll_sound_story_images_container.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1200));
                            holder.story_images_inflater.inflate(R.layout.item_main_story_image_one, holder.ll_sound_story_images_container, true);
                            ImageView iv_item_main_story_image = (ImageView) holder.ll_sound_story_images_container.findViewById(R.id.iv_item_main_story_image);
                            Glide.with(context).load(getString(R.string.cloud_front_song_stories_images) + photoList.get(0).getName() + "." + photoList.get(0).getExt()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_item_main_story_image);
                        }
                        if (photoListSize == 2) {
                            holder.ll_sound_story_images_container.removeAllViews();
                            holder.ll_sound_story_images_container.setVisibility(View.VISIBLE);
                            holder.ll_sound_story_images_container.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 800));

                            holder.story_images_inflater.inflate(R.layout.item_main_story_image_two, holder.ll_sound_story_images_container, true);
                            ImageView iv_item_main_story_image_two1 = (ImageView) holder.ll_sound_story_images_container.findViewById(R.id.iv_item_main_story_image_two1);
                            Glide.with(context).load(getString(R.string.cloud_front_song_stories_images) + photoList.get(0).getName() + "." + photoList.get(0).getExt()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_item_main_story_image_two1);
                            ImageView iv_item_main_story_image_two2 = (ImageView) holder.ll_sound_story_images_container.findViewById(R.id.iv_item_main_story_image_two2);
                            Glide.with(context).load(getString(R.string.cloud_front_song_stories_images) + photoList.get(1).getName() + "." + photoList.get(1).getExt()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_item_main_story_image_two2);
                        }
                        if (photoListSize > 2) {
                            holder.ll_sound_story_images_container.removeAllViews();
                            holder.ll_sound_story_images_container.setVisibility(View.VISIBLE);
                            holder.ll_sound_story_images_container.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 600));


                            holder.story_images_inflater.inflate(R.layout.item_main_story_image_list, holder.ll_sound_story_images_container, true);
                            RecyclerView rv_main_story = (RecyclerView) holder.ll_sound_story_images_container.findViewById(R.id.rv_main_story_images);
                            PhotoAdapter photoAdapter = new PhotoAdapter(context, photoList, R.layout.item_main_story_image, position);
                            rv_main_story.setAdapter(photoAdapter);
                            rv_main_story.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                            photoAdapter.notifyDataSetChanged();
                            rv_main_story.setHasFixedSize(true);
                        }
                    } else {
                        Toast.makeText(SongStoryActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<SongPhoto>> call, Throwable t) {
                    log(t);
                    Toast.makeText(SongStoryActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                }
            });

            songStoryServerConnection = new NetworkInterceptor(access_token).getClientForSongStoryServer().create(SongStoryServerConnection.class);
            Call<Integer> call_like_count = songStoryServerConnection.song_story_like_Count(storyList.get(position).getStory_id());
            call_like_count.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if (response.isSuccessful()) {
                        int like_count = response.body();
                        holder.tv_sound_story_like.setText(String.valueOf(like_count));
                    } else {
                        Toast.makeText(SongStoryActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    log(t);
                    Toast.makeText(SongStoryActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                }
            });

            songStoryServerConnection = new NetworkInterceptor(access_token).getClientForSongStoryServer().create(SongStoryServerConnection.class);
            Call<Integer> call_comment_count = songStoryServerConnection.song_story_comment_Count(storyList.get(position).getStory_id());
            call_comment_count.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if (response.isSuccessful()) {
                        int comment_count = response.body();
                        holder.tv_sound_story_comment.setText(String.valueOf(comment_count));
                    } else {
                        Toast.makeText(SongStoryActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    log(t);
                    Toast.makeText(SongStoryActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                }
            });

            songStoryServerConnection = new NetworkInterceptor(access_token).getClientForSongStoryServer().create(SongStoryServerConnection.class);
            Call<Integer> call_like_check = songStoryServerConnection.song_story_like_check(storyList.get(position).getStory_id(), user_id);
            call_like_check.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if (response.isSuccessful()) {
                        int checked = response.body();
                        if (checked == 1) {
                            holder.cb_item_sound_story_like.setChecked(true);
                            storyList.get(position).setFirst_checked(true);
                            storyList.get(position).setChecked(true);
                        } else {
                            holder.cb_item_sound_story_like.setChecked(false);
                            storyList.get(position).setFirst_checked(true);
                        }
                    } else {
                        Toast.makeText(SongStoryActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    log(t);
                    Toast.makeText(SongStoryActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return storyList.size();
        }

        public String calculateTime(String dateTime) {
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = transFormat.parse(dateTime);
            } catch (ParseException e) {
                log(e);
            }

            long curTime = System.currentTimeMillis();
            long regTime = date.getTime();
            long diffTime = (curTime - regTime) / 1000;

            String msg = null;
            if (diffTime < 60) {
                msg = diffTime + "초전";
            } else if ((diffTime /= 60) < 60) {
                System.out.println(diffTime);
                msg = diffTime + "분전";
            } else if ((diffTime /= 60) < 24) {
                msg = (diffTime) + "시간전";
            } else if ((diffTime /= 24) < 7) {
                msg = (diffTime) + "일전";
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yy.M.d aa h:mm");
                msg = sdf.format(date);
            }

            return msg;
        }
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout ll_main_story_image;
        private ImageView iv_main_story_content;

        public PhotoViewHolder(View itemView) {
            super(itemView);

            ll_main_story_image = (LinearLayout) itemView.findViewById(R.id.ll_main_story_image);
            iv_main_story_content = (ImageView) itemView.findViewById(R.id.iv_main_story_content);
        }
    }

    class PhotoAdapter extends RecyclerView.Adapter<PhotoViewHolder> {
        private Context context;
        private ArrayList<SongPhoto> photoList;
        private int layout;
        private int story_position;

        public PhotoAdapter(Context context, ArrayList<SongPhoto> photoList, int layout, int story_position) {
            this.context = context;
            this.photoList = photoList;
            this.layout = layout;
            this.story_position = story_position;
        }

        @Override
        public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            PhotoViewHolder photoViewHolder = new PhotoViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
            return photoViewHolder;
        }

        @Override
        public void onBindViewHolder(PhotoViewHolder holder, int position) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SongStoryActivity.this, SongStoryDetailActivity.class);


                    intent.putExtra("story_user_id", story_user_id);
                    intent.putExtra("story_user_name", story_user_name);
                    intent.putExtra("story_user_avatar", story_user_avatar);

                    intent.putExtra("story_id", storyList.get(story_position).getStory_id());

                    intent.putExtra("range_id", storyList.get(story_position).getRange_id());
                    intent.putExtra("song_id", storyList.get(story_position).getSong_id());
                    intent.putExtra("song_title", storyList.get(story_position).getSong_title());
                    intent.putExtra("song_singer", storyList.get(story_position).getSong_singer());
                    intent.putExtra("record_file", storyList.get(story_position).getRecord_file());
                    intent.putExtra("content", storyList.get(story_position).getContent());
                    intent.putExtra("created_at", storyList.get(story_position).getCreated_at());
                    intent.putExtra("location", storyList.get(story_position).getLocation());
                    intent.putExtra("hits", storyList.get(story_position).getHits());
                    intent.putExtra("like_checked", storyList.get(story_position).getChecked());

                    intent.putExtra("position", story_position);
                    startActivityForResult(intent, DETAIL_REQUEST);
                }
            });
            Glide.with(context).load(getString(R.string.cloud_front_song_stories_images) + photoList.get(position).getName() + "." + photoList.get(position).getExt()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_main_story_content);
            divideMultiPhotos(holder.ll_main_story_image, holder.iv_main_story_content);
        }

        @Override
        public int getItemCount() {
            return photoList.size();
        }

        // multi photos
        private void divideMultiPhotos(LinearLayout ll_main_story_image, ImageView iv_main_story_content) {
            ll_main_story_image.setLayoutParams(new LinearLayout.LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics()),
                    LinearLayout.LayoutParams.MATCH_PARENT));

            iv_main_story_content.setLayoutParams(new LinearLayout.LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics()),
                    LinearLayout.LayoutParams.MATCH_PARENT));
        }
    }

    private class EmotionViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_emotion;
        private ImageView iv_emotion;

        public EmotionViewHolder(View itemView) {
            super(itemView);
            tv_emotion = (TextView) itemView.findViewById(R.id.tv_emotion);
            iv_emotion = (ImageView) itemView.findViewById(R.id.iv_emotion);
            tv_emotion.setTextSize(14);
        }
    }

    private class EmotionAdapter extends RecyclerView.Adapter<EmotionViewHolder> {
        private ArrayList<SongStoryEmotionData> emotionList;
        private Context context;
        private int layout;

        public EmotionAdapter(ArrayList<SongStoryEmotionData> emotionList, Context context, int layout) {
            this.emotionList = emotionList;
            this.context = context;
            this.layout = layout;
        }

        @Override
        public int getItemCount() {
            return emotionList.size();
        }

        @Override
        public void onBindViewHolder(EmotionViewHolder holder, int position) {
            holder.tv_emotion.setText(emotionList.get(position).getName());
            Glide.with(context).load(getString(R.string.cloud_front_song_story_emotion) + emotionList.get(position).getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_emotion);
        }

        @Override
        public EmotionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            EmotionViewHolder holder = new EmotionViewHolder((LayoutInflater.from(context).inflate(layout, parent, false)));
            return holder;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DETAIL_REQUEST) {
            if (resultCode == RESULT_OK) {
                int position = data.getIntExtra("position", 0);
                Boolean like_checked = data.getBooleanExtra("like_checked", false);
                storyList.get(position).setFirst_checked(false); //like sync
                storyList.get(position).setChecked(like_checked); //like sync
                contentAdapter.notifyItemChanged(position);
            }

            if (resultCode == EDIT) {
                int position = data.getIntExtra("position", 0);
                storyList.get(position).setContent(data.getStringExtra("content"));
                storyList.get(position).setLocation(data.getStringExtra("location"));
                contentAdapter.notifyItemChanged(position);
            }

            if(resultCode == DELETE){
                int position = data.getIntExtra("position", 0);
                contentAdapter.storyList.remove(position);
                contentAdapter.notifyItemRemoved(position);
                contentAdapter.notifyItemRangeChanged(position, contentAdapter.getItemCount());

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
