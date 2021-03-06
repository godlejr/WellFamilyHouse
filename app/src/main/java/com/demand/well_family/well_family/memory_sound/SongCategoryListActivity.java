package com.demand.well_family.well_family.memory_sound;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.demand.well_family.well_family.repository.SongServerConnection;
import com.demand.well_family.well_family.family.manage.activity.ManageFamilyActivity;
import com.demand.well_family.well_family.repository.interceptor.NetworkInterceptor;
import com.demand.well_family.well_family.setting.base.activity.SettingActivity;
import com.demand.well_family.well_family.dto.Song;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.market.MarketMainActivity;
import com.demand.well_family.well_family.users.base.activity.UserActivity;
import com.demand.well_family.well_family.util.ErrorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.demand.well_family.well_family.main.login.activity.LoginActivity.finishList;

/**
 * Created by Dev-0 on 2017-02-01.
 */

public class SongCategoryListActivity extends Activity {
    private TextView toolbar_title;
    private int category_id;
    private String category_name;

    private SongServerConnection songServerConnection;
    private ArrayList<Song> songList;
    private RecyclerView rv_song_list;

    //user_info
    private int user_id;
    private String user_name;
    private int user_level;
    private String user_avatar;
    private String user_email;
    private String user_phone;
    private String user_birth;
    private String access_token;

    //toolbar
    private DrawerLayout dl;

    private static final Logger logger = LoggerFactory.getLogger(SongCategoryListActivity.class);
    private SharedPreferences loginInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        setUserInfo();

        category_id = getIntent().getIntExtra("category_id", 0);
        category_name = getIntent().getStringExtra("category_name");

        finishList.add(this);

        init();
        getSongsData();
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
        setToolbar(this.getWindow().getDecorView(), this, "????????????");
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

                Intent intent = new Intent(SongCategoryListActivity.this, UserActivity.class);
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
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy??? MM??? dd??????");
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
                        intent = new Intent(SongCategoryListActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                        break;

                    case R.id.menu_family:
                        intent = new Intent(SongCategoryListActivity.this, ManageFamilyActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                        break;

                    case R.id.menu_market:
                        intent = new Intent(SongCategoryListActivity.this, MarketMainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                        break;

                    case R.id.menu_setting:
                        Intent settingIntent = new Intent(getApplicationContext(), SettingActivity.class);
                        startActivity(settingIntent);
                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                        break;

                    case R.id.menu_notice:
                        Intent noticeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.noticeUrl)));
                        startActivity(noticeIntent);
                        break;

                    case R.id.menu_help:
                        Intent questionIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.questionUrl)));
                        startActivity(questionIntent);
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

                        intent = new Intent(SongCategoryListActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;

//                    App ????????????
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

    private void setBack() {
        finish();
    }

    private class SongViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout ll_item_song;
        private ImageView iv_item_song_avatar;
        private TextView tv_item_song_title, tv_item_song_singer;

        public SongViewHolder(View itemView) {
            super(itemView);
            ll_item_song = (LinearLayout) itemView.findViewById(R.id.ll_item_song);
            iv_item_song_avatar = (ImageView) itemView.findViewById(R.id.iv_item_song_avatar);
            tv_item_song_title = (TextView) itemView.findViewById(R.id.tv_item_song_title);
            tv_item_song_singer = (TextView) itemView.findViewById(R.id.tv_item_song_singer);

            ll_item_song.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    songServerConnection = new NetworkInterceptor(access_token).getClientForSongServer().create(SongServerConnection.class);
                    Call<ResponseBody> call_insert_song_hit = songServerConnection.Insert_Song_hit(songList.get(getAdapterPosition()).getId());
                    call_insert_song_hit.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (!response.isSuccessful()) {
                                Toast.makeText(SongCategoryListActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                            }
                            //scess
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            log(t);
                            Toast.makeText(SongCategoryListActivity.this, "???????????? ??????????????????. ?????? ???????????????.", Toast.LENGTH_LONG).show();
                        }
                    });


                    Intent intent = new Intent(SongCategoryListActivity.this, SongPlayer.class);

                    //song info
                    intent.putExtra("song_id", songList.get(getAdapterPosition()).getId());
                    intent.putExtra("song_name", songList.get(getAdapterPosition()).getName());
                    intent.putExtra("song_ext", songList.get(getAdapterPosition()).getExt());
                    intent.putExtra("song_title", songList.get(getAdapterPosition()).getTitle());
                    intent.putExtra("song_singer", songList.get(getAdapterPosition()).getSinger());
                    intent.putExtra("song_avatar", songList.get(getAdapterPosition()).getAvatar());
                    intent.putExtra("song_category_id", songList.get(getAdapterPosition()).getCategory_id());
                    intent.putExtra("song_created_at", songList.get(getAdapterPosition()).getCreated_at());

                    startActivity(intent);
                }
            });
        }

    }

    private class SongAdapter extends RecyclerView.Adapter<SongViewHolder> {
        private Context context;
        private ArrayList<Song> songList;
        private int layout;

        public SongAdapter(Context context, ArrayList<Song> songList, int layout) {
            this.context = context;
            this.songList = songList;
            this.layout = layout;
        }

        @Override
        public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            SongViewHolder songViewHolder = new SongViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
            return songViewHolder;
        }


        @Override
        public void onBindViewHolder(SongViewHolder holder, int position) {
            Glide.with(context).load(getString(R.string.cloud_front_songs_avatar) + songList.get(position).getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_item_song_avatar);
            holder.tv_item_song_title.setText(songList.get(position).getTitle());
            holder.tv_item_song_singer.setText(songList.get(position).getSinger());
        }

        @Override
        public int getItemCount() {
            return songList.size();
        }
    }

    private void getSongsData() {
        rv_song_list = (RecyclerView) findViewById(R.id.rv_song_list);

        songServerConnection = new NetworkInterceptor(access_token).getClientForSongServer().create(SongServerConnection.class);
        Call<ArrayList<Song>> call_song_list_by_Category = songServerConnection.song_list_by_Category(category_id);
        call_song_list_by_Category.enqueue(new Callback<ArrayList<Song>>() {
            @Override
            public void onResponse(Call<ArrayList<Song>> call, Response<ArrayList<Song>> response) {
                if (response.isSuccessful()) {
                    songList = response.body();
                    rv_song_list.setAdapter(new SongAdapter(SongCategoryListActivity.this, songList, R.layout.item_song));
                    rv_song_list.setLayoutManager(new LinearLayoutManager(SongCategoryListActivity.this, LinearLayoutManager.VERTICAL, false));
                } else {
                    Toast.makeText(SongCategoryListActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Song>> call, Throwable t) {
                log(t);
                Toast.makeText(SongCategoryListActivity.this, "???????????? ??????????????????. ?????? ???????????????.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void init() {
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(category_name);
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
