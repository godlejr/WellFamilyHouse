package com.demand.well_family.well_family.users;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.LoginActivity;
import com.demand.well_family.well_family.MainActivity;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.connection.SongServerConnection;
import com.demand.well_family.well_family.connection.SongStoryServerConnection;
import com.demand.well_family.well_family.connection.UserServerConnection;
import com.demand.well_family.well_family.dialog.CommentPopupActivity;
import com.demand.well_family.well_family.dialog.SongStoryPopup;
import com.demand.well_family.well_family.dto.CommentInfo;
import com.demand.well_family.well_family.dto.SongPhoto;
import com.demand.well_family.well_family.dto.SongStoryComment;
import com.demand.well_family.well_family.dto.SongStoryEmotionData;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.interceptor.HeaderInterceptor;
import com.demand.well_family.well_family.market.MarketMainActivity;
import com.demand.well_family.well_family.memory_sound.SongMainActivity;
import com.demand.well_family.well_family.photos.SongPhotoPopupActivity;
import com.demand.well_family.well_family.settings.SettingActivity;
import com.demand.well_family.well_family.util.ErrorUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.demand.well_family.well_family.LoginActivity.finishList;

/**
 * Created by ㅇㅇ on 2017-02-03.
 */

public class SongStoryDetailActivity extends Activity implements CompoundButton.OnCheckedChangeListener {
    private TextView tv_sound_story_detail_title;
    private TextView tv_sound_story_detail_singer;
    private TextView tv_sound_story_detail_writer_name;
    private TextView tv_sound_story_detail_play;
    private TextView tv_sound_story_detail_end;
    private TextView tv_sound_story_detail_content;
    private TextView tv_sound_story_detail_location;
    private TextView tv_sound_story_detail_date;
    private TextView tv_sound_story_detail_like_count;
    private TextView tv_sound_story_detail_comment_count;

    private NestedScrollView nsv_sound_story_detail;
    private CircleImageView iv_sound_story_detail_avatar;
    private SeekBar sb_sound_story_detail;
    private ImageView iv_sound_story_detail_play;
    private ImageView iv_sound_story_menu;

    private RecyclerView rv_sound_story_detail;
    private RecyclerView rv_sound_story_detail_comments;
    private LinearLayout ll_sound_story_detail_location;
    private LinearLayout ll_sound_story_detail_image;
    private LinearLayout ll_sound_story_detail_play;
    private CheckBox cb_sound_story_detail;

    private EditText et_sound_story_comment;
    private Button btn_sound_story_comment_submit;

    private MediaPlayer mp;
    private int pausePos;
    private boolean isPaused = false, isPlaying = false;
    private String record_file;
    private int endMinute, endSecond;
    private ImageView iv_sound_story_detail_share;

    // photo
    private PhotoAdapter photoAdapter;
    private ArrayList<SongPhoto> photoList;

    // comment
    private CommentAdapter commentAdapter;
    private ArrayList<CommentInfo> commentInfoList;

    //intent
    private String user_name;
    private String user_avatar;
    private int user_id;
    private int story_user_id;
    private String story_user_name;
    private String story_user_avatar;
    private int story_id;
    private int range_id;
    private int song_id;
    private String song_title;
    private String song_singer;
    private String created_at;
    private String location;
    private int hits;
    private boolean like_checked;
    private String content;

    //first like check
    private Boolean first_checked = false;


    //toolbar
    private DrawerLayout dl;
    private String user_birth;
    private String user_phone;
    private int user_level;
    private String user_email;
    private ImageView iv_sound_detail_song_img;

    //server_connection
    private SongStoryServerConnection songStoryServerConnection;
    private SongServerConnection songServerConnection;
    private UserServerConnection userServerConnection;

    //emotion
    private RecyclerView rv_detail_emotion;
    private EmotionAdapter emotionAdapter;

    private static final int COMMENT_EDIT_REQUEST = 1;

    private static final Logger logger = LoggerFactory.getLogger(SongStoryDetailActivity.class);
    private SharedPreferences loginInfo;

    private String song_avatar;
    private String access_token;

    private ArrayList<SongStoryEmotionData> emotionList;

    //request_code
    private static final int POPUP_REQUEST = 3;

    //result code
    private static final int EDIT = 4;
    private static final int DELETE = 5;

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sound_acivity_story_detail);

        finishList.add(this);

        setUserInfo();

        story_user_id = getIntent().getIntExtra("story_user_id", 0);
        story_user_name = getIntent().getStringExtra("story_user_name");
        story_user_avatar = getIntent().getStringExtra("story_user_avatar");

        story_id = getIntent().getIntExtra("story_id", 0);
        range_id = getIntent().getIntExtra("range_id", 0);
        song_id = getIntent().getIntExtra("song_id", 0);

        song_title = getIntent().getStringExtra("song_title");
        song_singer = getIntent().getStringExtra("song_singer");
        record_file = getIntent().getStringExtra("record_file");
        created_at = getIntent().getStringExtra("created_at");
        content = getIntent().getStringExtra("content");
        location = getIntent().getStringExtra("location");
        hits = getIntent().getIntExtra("hits", 0);
        like_checked = getIntent().getBooleanExtra("like_checked", false);
        position = getIntent().getIntExtra("position", 0);

        init();

        getCommentCount();
        getContentData();
        getCommentData();
        setCommentData();
        getEmotionData();
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

                Intent intent = new Intent(SongStoryDetailActivity.this, UserActivity.class);
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
                        intent = new Intent(SongStoryDetailActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;

                    case R.id.menu_search:
                        Toast.makeText(getApplicationContext(), "준비중입니다.", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.menu_market:
                        intent = new Intent(SongStoryDetailActivity.this, MarketMainActivity.class);
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
                        editor.remove("access_token");
                        editor.commit();

                        intent = new Intent(SongStoryDetailActivity.this, LoginActivity.class);
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
        Intent intent = getIntent();
        intent.putExtra("position", position);
        intent.putExtra("like_checked", like_checked);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void getCommentCount() {
        tv_sound_story_detail_comment_count = (TextView) findViewById(R.id.tv_sound_story_detail_comment_count);

        songStoryServerConnection = new HeaderInterceptor(access_token).getClientForSongStoryServer().create(SongStoryServerConnection.class);
        Call<Integer> call_comment_count = songStoryServerConnection.song_story_comment_Count(story_id);
        call_comment_count.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int comment_count = response.body();
                    tv_sound_story_detail_comment_count.setText(String.valueOf(comment_count));
                } else {
                    Toast.makeText(SongStoryDetailActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                log(t);
                Toast.makeText(SongStoryDetailActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void init() {
        tv_sound_story_detail_title = (TextView) findViewById(R.id.tv_sound_story_detail_title);
        tv_sound_story_detail_singer = (TextView) findViewById(R.id.tv_sound_story_detail_singer);
        tv_sound_story_detail_writer_name = (TextView) findViewById(R.id.tv_sound_story_detail_writer_name);
        iv_sound_detail_song_img = (ImageView) findViewById(R.id.iv_sound_detail_song_img);

        tv_sound_story_detail_date = (TextView) findViewById(R.id.tv_sound_story_detail_date);
        tv_sound_story_detail_play = (TextView) findViewById(R.id.tv_sound_story_detail_play);
        tv_sound_story_detail_end = (TextView) findViewById(R.id.tv_sound_story_detail_end);
        tv_sound_story_detail_content = (TextView) findViewById(R.id.tv_sound_story_detail_content);
        tv_sound_story_detail_location = (TextView) findViewById(R.id.tv_sound_story_detail_location);

        iv_sound_story_detail_avatar = (CircleImageView) findViewById(R.id.iv_sound_story_detail_avatar);
        iv_sound_story_detail_play = (ImageView) findViewById(R.id.iv_sound_story_detail_play);
        iv_sound_story_detail_share = (ImageView) findViewById(R.id.iv_sound_story_detail_share);

        sb_sound_story_detail = (SeekBar) findViewById(R.id.sb_sound_story_detail);
        cb_sound_story_detail = (CheckBox) findViewById(R.id.cb_sound_story_detail_like);

        ll_sound_story_detail_location = (LinearLayout) findViewById(R.id.ll_sound_story_detail_location);
        ll_sound_story_detail_image = (LinearLayout) findViewById(R.id.ll_sound_story_detail_image);
        ll_sound_story_detail_play = (LinearLayout) findViewById(R.id.ll_sound_story_detail_play);
        nsv_sound_story_detail = (NestedScrollView) findViewById(R.id.nsv_sound_story_detail);

        // song
        tv_sound_story_detail_title.setText(song_title);
        tv_sound_story_detail_singer.setText(song_singer);


        songServerConnection = new HeaderInterceptor(access_token).getClientForSongServer().create(SongServerConnection.class);
        Call<String> call_song_avatar = songServerConnection.song_story_avatar(song_id);
        call_song_avatar.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful()) {
                    song_avatar = response.body().toString();
                    Glide.with(SongStoryDetailActivity.this).load(getString(R.string.cloud_front_songs_avatar) + song_avatar)
                            .thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_sound_detail_song_img);
                } else {
                    Toast.makeText(SongStoryDetailActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                log(t);
                Toast.makeText(SongStoryDetailActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });


        // user info
        Glide.with(this).load(getString(R.string.cloud_front_user_avatar) + story_user_avatar)
                .thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_sound_story_detail_avatar);
        tv_sound_story_detail_writer_name.setText(story_user_name);
        tv_sound_story_detail_date.setText(calculateTime(created_at));

        // record
        if (record_file != null && record_file.length() != 0) {
            setPlayer();
        } else {
            ll_sound_story_detail_play.setVisibility(View.GONE);
        }

        // content, location
        tv_sound_story_detail_content.setText(content); // 내용
        if (location.length() != 0 && location != null) {
            tv_sound_story_detail_location.setText(location);
        } else {
            ll_sound_story_detail_location.setVisibility(View.GONE);
        }

        // like
        tv_sound_story_detail_like_count = (TextView) findViewById(R.id.tv_sound_story_detail_like_count);

        songStoryServerConnection = new HeaderInterceptor(access_token).getClientForSongStoryServer().create(SongStoryServerConnection.class);
        Call<Integer> call_like_count = songStoryServerConnection.song_story_like_Count(story_id);
        call_like_count.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int like_count = response.body();
                    tv_sound_story_detail_like_count.setText(String.valueOf(like_count));
                } else {
                    Toast.makeText(SongStoryDetailActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                log(t);
                Toast.makeText(SongStoryDetailActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });

        if (like_checked) {
            cb_sound_story_detail.setChecked(true);
            first_checked = !first_checked;
        } else {
            cb_sound_story_detail.setChecked(false);
            first_checked = !first_checked;
        }
        cb_sound_story_detail.setOnCheckedChangeListener(this);

        // share
        iv_sound_story_detail_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");

                List<ResolveInfo> resInfo = getApplicationContext().getPackageManager().queryIntentActivities(intent, 0);
                List shareIntentList = new ArrayList();
                for (int i = 0; i < resInfo.size(); i++) {
                    Intent shareIntent = (Intent) intent.clone();
                    ResolveInfo info = resInfo.get(i);
                    final ComponentName name = new ComponentName(info.activityInfo.applicationInfo.packageName, info.activityInfo.name);


                    String content = tv_sound_story_detail_content.getText().toString();
                    shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);

                    shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    shareIntent.setComponent(name);

                    shareIntent.setPackage(info.activityInfo.packageName);
                    shareIntentList.add(shareIntent);
                }

                Intent chooser = Intent.createChooser((Intent) shareIntentList.remove(0), "share");
                chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, shareIntentList.toArray(new Parcelable[]{}));
                startActivity(chooser);
            }
        });

        iv_sound_story_menu = (ImageView) findViewById(R.id.iv_sound_story_menu);
        userServerConnection = new HeaderInterceptor(access_token).getClientForUserServer().create(UserServerConnection.class);
        Call<Integer> call_family_check = userServerConnection.family_check(story_user_id, user_id);
        call_family_check.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    if (response.body() > 0) { // 가족
                        iv_sound_story_menu.setVisibility(View.VISIBLE);
                        iv_sound_story_menu.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(SongStoryDetailActivity.this, SongStoryPopup.class);
                                intent.putExtra("story_id", story_id);
                                intent.putExtra("content", content);
                                intent.putExtra("photoList", photoList);
                                intent.putExtra("position", position);
                                intent.putExtra("song_id", song_id);
                                intent.putExtra("song_avatar", song_avatar);
                                intent.putExtra("song_title", song_title);
                                intent.putExtra("song_singer", song_singer);
                                intent.putExtra("emotionList", emotionList);
                                intent.putExtra("location", location);
                                if(story_user_id ==  user_id){
                                    intent.putExtra("story_user_is_me", true);
                                }

                                startActivityForResult(intent, POPUP_REQUEST);

                            }
                        });
                    } else {
                        iv_sound_story_menu.setVisibility(View.GONE);
                    }

                } else {
                    Toast.makeText(SongStoryDetailActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                log(t);
                Toast.makeText(SongStoryDetailActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });


    }

    private void setPlayer() {
        if (mp == null) {
            mp = new MediaPlayer();
        }

        try {
            mp.setDataSource(getString(R.string.cloud_front_song_story_record) + record_file);
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    endMinute = mp.getDuration() / 60000;
                    endSecond = ((mp.getDuration() % 60000) / 1000);

                    tv_sound_story_detail_end.setText(endMinute + ":" + endSecond);

                    sb_sound_story_detail.setMax(mp.getDuration());
                    sb_sound_story_detail.setProgress(0);
                    isPlaying = true;

                    mp.start();
                    new SeekBarThread().start();
                }
            });

            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    isPlaying = isPaused = false;

                    Glide.with(SongStoryDetailActivity.this).load(R.drawable.play).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_sound_story_detail_play);
                    sb_sound_story_detail.setProgress(0);
                    mp.pause();
                    mp.stop();
                    mp.reset();
                }
            });

            mp.prepareAsync();
        } catch (IOException e) {
            log(e);
        }

        sb_sound_story_detail.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int m = seekBar.getProgress() / 60000;
                int s = ((seekBar.getProgress() % 60000) / 1000);

                tv_sound_story_detail_play.setText(m + ":" + s);
                pausePos = seekBar.getProgress();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mp.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (isPlaying) {
                    if (mp != null) {
                        mp.seekTo(pausePos);
                        mp.start();
                    }
                    isPlaying = true;

                    new SeekBarThread().start();
                    Glide.with(SongStoryDetailActivity.this).load(R.drawable.pause).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_sound_story_detail_play);
                }
            }
        });

        iv_sound_story_detail_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp != null) {
                    if (isPlaying) {
                        pausePos = mp.getCurrentPosition();
                        mp.pause();
                        isPlaying = false;
                        isPaused = true;

                        Glide.with(SongStoryDetailActivity.this).load(R.drawable.play).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_sound_story_detail_play);
                    } else {
                        if (isPaused) {  // 일시정지 -> 재생
                            mp.seekTo(pausePos);
                            mp.start();
                        } else { // 처음 재생 or 끝까지 갔다가 다시 재생
                            try {
                                mp = new MediaPlayer();
                                mp.setDataSource(getString(R.string.cloud_front_song_story_record) + record_file);
                                mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                    @Override
                                    public void onPrepared(MediaPlayer player) {
                                        endMinute = mp.getDuration() / 60000;
                                        endSecond = ((mp.getDuration() % 60000) / 1000);
                                        tv_sound_story_detail_end.setText(endMinute + ":" + endSecond);

                                        sb_sound_story_detail.setMax(mp.getDuration());
                                        sb_sound_story_detail.setProgress(0);

                                        player.start();
                                    }
                                });
                                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        isPlaying = isPaused = false;

                                        Glide.with(SongStoryDetailActivity.this).load(R.drawable.play).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_sound_story_detail_play);
                                        sb_sound_story_detail.setProgress(0);
                                        mp.pause();
                                        mp.stop();
                                        mp.reset();
                                    }
                                });
                                mp.prepareAsync();
                            } catch (IOException e) {
                                log(e);
                            }
                        }

                        isPlaying = true;
                        isPaused = false;

                        new SeekBarThread().start();
                        Glide.with(SongStoryDetailActivity.this).load(R.drawable.pause).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_sound_story_detail_play);
                    }
                }
            }
        });
    }

    private void getContentData() {

        rv_sound_story_detail = (RecyclerView) findViewById(R.id.rv_sound_story_detail);

        //images
        songStoryServerConnection = new HeaderInterceptor(access_token).getClientForSongStoryServer().create(SongStoryServerConnection.class);
        Call<ArrayList<SongPhoto>> call_song_photo = songStoryServerConnection.song_story_photo_List(story_id);
        call_song_photo.enqueue(new Callback<ArrayList<SongPhoto>>() {
            @Override
            public void onResponse(Call<ArrayList<SongPhoto>> call, Response<ArrayList<SongPhoto>> response) {
                if (response.isSuccessful()) {
                    photoList = response.body();
                    if (photoList.size() == 0) {
                        rv_sound_story_detail.setVisibility(View.GONE);
                        ll_sound_story_detail_image.setVisibility(View.GONE);
                    } else {
                        photoAdapter = new PhotoAdapter(SongStoryDetailActivity.this, photoList, R.layout.item_detail_photo);
                        rv_sound_story_detail.setAdapter(photoAdapter);
                        rv_sound_story_detail.setLayoutManager(new LinearLayoutManager(SongStoryDetailActivity.this, LinearLayoutManager.VERTICAL, false));
                        rv_sound_story_detail.addItemDecoration(new SpaceItemDecoration(16));
                    }
                } else {
                    Toast.makeText(SongStoryDetailActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SongPhoto>> call, Throwable t) {
                log(t);
                Toast.makeText(SongStoryDetailActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void getEmotionData() {
        rv_detail_emotion = (RecyclerView) findViewById(R.id.rv_detail_emotion);

        songStoryServerConnection = new HeaderInterceptor(access_token).getClientForSongStoryServer().create(SongStoryServerConnection.class);
        Call<ArrayList<SongStoryEmotionData>> call_emotion_data = songStoryServerConnection.song_story_emotion_List(story_id);
        call_emotion_data.enqueue(new Callback<ArrayList<SongStoryEmotionData>>() {
            @Override
            public void onResponse(Call<ArrayList<SongStoryEmotionData>> call, Response<ArrayList<SongStoryEmotionData>> response) {
                if (response.isSuccessful()) {
                    emotionList = response.body();
                    //emotion
                    emotionAdapter = new EmotionAdapter(emotionList, SongStoryDetailActivity.this, R.layout.item_emotion);
                    rv_detail_emotion.setAdapter(emotionAdapter);
                    rv_detail_emotion.setLayoutManager(new GridLayoutManager(SongStoryDetailActivity.this, 2));
                } else {
                    Toast.makeText(SongStoryDetailActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SongStoryEmotionData>> call, Throwable t) {
                log(t);
                Toast.makeText(SongStoryDetailActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_item_detail_photo;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            iv_item_detail_photo = (ImageView) itemView.findViewById(R.id.iv_item_detail_photo);
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoViewHolder> {
        private Context context;
        private ArrayList<SongPhoto> photoList;
        private int layout;

        public PhotoAdapter(Context context, ArrayList<SongPhoto> photoList, int layout) {
            this.context = context;
            this.photoList = photoList;
            this.layout = layout;
        }

        @Override
        public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            PhotoViewHolder photoViewHolder = new PhotoViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
            return photoViewHolder;
        }

        @Override
        public void onBindViewHolder(PhotoViewHolder holder, final int position) {
            Glide.with(context).load(getString(R.string.cloud_front_song_stories_images) + photoList.get(position).getName() + "." + photoList.get(position).getExt()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_item_detail_photo);
            holder.iv_item_detail_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), SongPhotoPopupActivity.class);
                    intent.putExtra("photo_position", position);
                    intent.putExtra("photoList", photoList);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return photoList.size();
        }
    }

    private void setCommentData() {
        et_sound_story_comment = (EditText) findViewById(R.id.et_sound_story_comment);
        btn_sound_story_comment_submit = (Button) findViewById(R.id.btn_sound_story_comment_submit);

        btn_sound_story_comment_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String content = et_sound_story_comment.getText().toString();
                if (content.length() != 0) {
                    songStoryServerConnection = new HeaderInterceptor(access_token).getClientForSongStoryServer().create(SongStoryServerConnection.class);
                    final HashMap<String, String> map = new HashMap<>();
                    map.put("user_id", String.valueOf(user_id));
                    map.put("content", content);
                    Call<SongStoryComment> call_insert_comment = songStoryServerConnection.insert_song_story_comment(story_id, map);
                    call_insert_comment.enqueue(new Callback<SongStoryComment>() {
                        @Override
                        public void onResponse(Call<SongStoryComment> call, Response<SongStoryComment> response) {
                            if (response.isSuccessful()) {
                                SongStoryComment songStoryComment = response.body();
                                int comment_id = songStoryComment.getId();
                                String created_at = songStoryComment.getCreated_at();
                                commentInfoList.add(new CommentInfo(comment_id, user_id, user_name, user_avatar, content, created_at));
                                commentAdapter.notifyItemInserted(commentInfoList.size() - 1);
                                getCommentCount();

                                et_sound_story_comment.setText("");

                                NestedScrollView nsv = (NestedScrollView) findViewById(R.id.nsv_sound_story_detail);
                                nsv.fullScroll(NestedScrollView.FOCUS_DOWN);
                            } else {
                                Toast.makeText(SongStoryDetailActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<SongStoryComment> call, Throwable t) {
                            log(t);
                            Toast.makeText(SongStoryDetailActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    private void getCommentData() {
        rv_sound_story_detail_comments = (RecyclerView) findViewById(R.id.rv_sound_story_detail_comments);

        songStoryServerConnection = new HeaderInterceptor(access_token).getClientForSongStoryServer().create(SongStoryServerConnection.class);
        Call<ArrayList<CommentInfo>> call_family = songStoryServerConnection.song_story_comment_List(story_id);
        call_family.enqueue(new Callback<ArrayList<CommentInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<CommentInfo>> call, Response<ArrayList<CommentInfo>> response) {
                if (response.isSuccessful()) {
                    commentInfoList = response.body();
                    commentAdapter = new CommentAdapter(SongStoryDetailActivity.this, commentInfoList, R.layout.item_comment);
                    rv_sound_story_detail_comments.setAdapter(commentAdapter);
                    rv_sound_story_detail_comments.setLayoutManager(new LinearLayoutManager(SongStoryDetailActivity.this, LinearLayoutManager.VERTICAL, false));
                } else {
                    Toast.makeText(SongStoryDetailActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CommentInfo>> call, Throwable t) {
                log(t);
                Toast.makeText(SongStoryDetailActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });

    }

    private class CommentViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_item_comment_avatar;
        private TextView tv_item_comment_name;
        private TextView tv_item_comment_content;
        private TextView tv_item_comment_date;
        private LinearLayout ll_comment;

        public CommentViewHolder(View itemView) {
            super(itemView);
            ll_comment = (LinearLayout) itemView.findViewById(R.id.ll_comment);
            iv_item_comment_avatar = (ImageView) itemView.findViewById(R.id.iv_item_comment_avatar);
            tv_item_comment_name = (TextView) itemView.findViewById(R.id.tv_item_comment_name);
            tv_item_comment_content = (TextView) itemView.findViewById(R.id.tv_item_comment_content);
            tv_item_comment_date = (TextView) itemView.findViewById(R.id.tv_item_comment_date);
        }
    }

    private class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {
        private Context context;
        private ArrayList<CommentInfo> commentInfoList;
        private int layout;

        public CommentAdapter(Context context, ArrayList<CommentInfo> commentInfoList, int layout) {
            this.context = context;
            this.commentInfoList = commentInfoList;
            this.layout = layout;
        }

        @Override
        public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CommentViewHolder commentViewHolder = new CommentViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
            return commentViewHolder;
        }

        @Override
        public void onBindViewHolder(CommentViewHolder holder, final int position) {
            Glide.with(context).load(getString(R.string.cloud_front_user_avatar) + commentInfoList.get(position).getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_item_comment_avatar);

            holder.tv_item_comment_name.setText(commentInfoList.get(position).getUser_name());
            holder.tv_item_comment_content.setText(commentInfoList.get(position).getContent());
            holder.tv_item_comment_date.setText(calculateTime(commentInfoList.get(position).getCreated_at()));

            holder.ll_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (user_id == commentInfoList.get(position).getUser_id()) {
                        Intent intent = new Intent(SongStoryDetailActivity.this, CommentPopupActivity.class);
                        intent.putExtra("comment_id", commentInfoList.get(position).getComment_id());
                        intent.putExtra("comment_content", commentInfoList.get(position).getContent());
                        intent.putExtra("position", position);
                        intent.putExtra("act_flag", 3);
                        startActivityForResult(intent, COMMENT_EDIT_REQUEST);
                    } else {
                        Intent intent = new Intent(SongStoryDetailActivity.this, CommentPopupActivity.class);
                        intent.putExtra("comment_id", commentInfoList.get(position).getComment_id());
                        intent.putExtra("comment_user_name", commentInfoList.get(position).getUser_name());
                        intent.putExtra("comment_content", commentInfoList.get(position).getContent());
                        intent.putExtra("act_flag", 4);
                        intent.putExtra("comment_category_id", 3);
                        startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return commentInfoList.size();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == POPUP_REQUEST) {
            if (resultCode == RESULT_OK) {
                Intent intent = getIntent();
                intent.putExtra("position", position);
                intent.putExtra("content", data.getStringExtra("content"));
                intent.putExtra("location",  data.getStringExtra("location"));


                setResult(EDIT, intent);
                finish();
            }

            if (resultCode == DELETE) {
                Intent intent = getIntent();
                intent.putExtra("position", position);
                setResult(DELETE, intent);
                finish();
            }
        }
        if (requestCode == COMMENT_EDIT_REQUEST) {
            if (resultCode == RESULT_OK) {
                int flag = data.getIntExtra("flag", 0);
                if (flag == 1) {
                    //modify
                    int position = data.getIntExtra("position", -1);
                    commentAdapter.commentInfoList.get(position).setContent(data.getStringExtra("content"));
                    commentAdapter.notifyItemChanged(position);
                }

                if (flag == 2) {
                    //delete
                    int position = data.getIntExtra("position", -1);
                    commentAdapter.commentInfoList.remove(position);
                    commentAdapter.notifyItemRemoved(position);
                    commentAdapter.notifyItemRangeChanged(position, commentAdapter.getItemCount());

                }
            }
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

    private class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.bottom = space;
        }
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
            if (diffTime < 0) {
                diffTime = 0;
            }
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (first_checked) {
            if (isChecked) {
                songStoryServerConnection = new HeaderInterceptor(access_token).getClientForSongStoryServer().create(SongStoryServerConnection.class);
                HashMap<String, String> map = new HashMap<>();
                map.put("user_id", String.valueOf(user_id));

                Call<ResponseBody> call_like = songStoryServerConnection.song_story_like_up(story_id, map);
                call_like.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            tv_sound_story_detail_like_count.setText(String.valueOf(Integer.parseInt(tv_sound_story_detail_like_count.getText().toString()) + 1));
                            like_checked = !like_checked;
                        } else {
                            Toast.makeText(SongStoryDetailActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        log(t);
                        Toast.makeText(SongStoryDetailActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                songStoryServerConnection = new HeaderInterceptor(access_token).getClientForSongStoryServer().create(SongStoryServerConnection.class);
                HashMap<String, String> map = new HashMap<>();
                map.put("user_id", String.valueOf(user_id));

                Call<ResponseBody> call_dislike = songStoryServerConnection.song_story_like_down(story_id, user_id);
                call_dislike.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            tv_sound_story_detail_like_count.setText(String.valueOf(Integer.parseInt(tv_sound_story_detail_like_count.getText().toString()) - 1));
                            like_checked = !like_checked;
                        } else {
                            Toast.makeText(SongStoryDetailActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        log(t);
                        Toast.makeText(SongStoryDetailActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Intent intent = getIntent();
                intent.putExtra("position", getIntent().getIntExtra("position", 0));
                intent.putExtra("like_checked", like_checked);
                setResult(Activity.RESULT_OK, intent);
                finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    class SeekBarThread extends Thread {
        public void run() {
            while (isPlaying) {
                sb_sound_story_detail.setProgress(mp.getCurrentPosition());
            }
        }

    }


    @Override
    protected void onPause() {
        if (mp != null) {
            isPlaying = false;

            mp.stop();
            mp.release();
            mp = null;
        }

        super.onPause();
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
