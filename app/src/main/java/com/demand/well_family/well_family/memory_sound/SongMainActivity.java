package com.demand.well_family.well_family.memory_sound;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.main.login.activity.LoginActivity;
import com.demand.well_family.well_family.main.base.activity.MainActivity;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.repository.SongServerConnection;
import com.demand.well_family.well_family.family.ManageFamilyActivity;
import com.demand.well_family.well_family.repository.interceptor.HeaderInterceptor;
import com.demand.well_family.well_family.setting.base.activity.SettingActivity;
import com.demand.well_family.well_family.dto.Song;
import com.demand.well_family.well_family.dto.SongCategory;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.market.MarketMainActivity;
import com.demand.well_family.well_family.users.UserActivity;
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
 * Created by ㅇㅇ on 2017-01-31.
 */

public class SongMainActivity extends Activity implements View.OnClickListener {
    private RecyclerView rv_sound_famous_chart;
    private RecyclerView rv_sound_category;
    private ImageButton ib_sound_chart;
    private ImageButton ib_sound_today;
    private TextView tv_sound_random_title;
    private TextView tv_sound_random_singer;
    private TextView tv_sound_random_comment_count;
    private ImageView iv_sound_random_avatar;
    private static final Logger logger = LoggerFactory.getLogger(SongMainActivity.class);

    //user_info
    private int user_id;
    private String user_name;
    private int user_level;
    private String user_avatar;
    private String user_email;
    private String user_phone;
    private String user_birth;
    private String access_token;

    //random song info
    private int random_id;
    private String random_name;
    private String random_ext;
    private String random_title;
    private String random_singer;
    private String random_avatar;
    private int random_category_id;
    private String random_created_at;

    private SongServerConnection songServerConnection;
    private ArrayList<SongCategory> songCategoryList;
    private LinearLayout ll_sound_random;
    private ArrayList<Song> songList;

    // toolbar
    private DrawerLayout dl;
    private SharedPreferences loginInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sound_activity_main);

        setUserInfo();
        finishList.add(this);

        init();
        getChartData();
        getCategoryData();
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
        setToolbar(this.getWindow().getDecorView(), this, "추억소리");
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
                // 함수 호출
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
                Intent intent = new Intent(SongMainActivity.this, UserActivity.class);
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
                        intent = new Intent(SongMainActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                        break;

                    case R.id.menu_family:
                        intent = new Intent(SongMainActivity.this, ManageFamilyActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                        break;

                    case R.id.menu_market:
                        intent = new Intent(SongMainActivity.this, MarketMainActivity.class);
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

                        intent = new Intent(SongMainActivity.this, LoginActivity.class);
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
                        break;
                }
                return true;
            }
        });
    }

    private void setBack() {
        finish();
    }

    private void init() {
        tv_sound_random_title = (TextView) findViewById(R.id.tv_sound_random_title);
        tv_sound_random_singer = (TextView) findViewById(R.id.tv_sound_random_singer);
        tv_sound_random_comment_count = (TextView) findViewById(R.id.tv_sound_random_comment_count);

        iv_sound_random_avatar = (ImageView) findViewById(R.id.iv_sound_random_avatar);

        songServerConnection = new HeaderInterceptor(access_token).getClientForSongServer().create(SongServerConnection.class);
        Call<Song> call_song_random = songServerConnection.song_random();
        call_song_random.enqueue(new Callback<Song>() {
            @Override
            public void onResponse(Call<Song> call, Response<Song> response) {
                if (response.isSuccessful()) {
                    Song songInfo = response.body();

                    if (songInfo == null) {

                    } else {
                        songServerConnection = new HeaderInterceptor(access_token).getClientForSongServer().create(SongServerConnection.class);
                        random_id = songInfo.getId();
                        random_avatar = songInfo.getAvatar();
                        random_singer = songInfo.getSinger();
                        random_title = songInfo.getTitle();
                        random_name = songInfo.getName();
                        random_category_id = songInfo.getCategory_id();
                        random_created_at = songInfo.getCreated_at();
                        random_ext = songInfo.getExt();

                        Call<Integer> call_song_comment_Count = songServerConnection.song_comment_Count(random_id);
                        call_song_comment_Count.enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                if (response.isSuccessful()) {
                                    String like_count = String.valueOf(response.body());
                                    tv_sound_random_comment_count.setText(like_count + "건");

                                    tv_sound_random_title.setText(random_title);
                                    tv_sound_random_singer.setText(random_singer);

                                    Glide.with(SongMainActivity.this).load(getString(R.string.cloud_front_songs_avatar) + random_avatar).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_sound_random_avatar);

                                    ll_sound_random = (LinearLayout) findViewById(R.id.ll_sound_random);
                                    ib_sound_today = (ImageButton) findViewById(R.id.ib_sound_today);

                                    ib_sound_today.setOnClickListener(SongMainActivity.this);
                                    ll_sound_random.setOnClickListener(SongMainActivity.this);
                                } else {
                                    Toast.makeText(SongMainActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {
                                log(t);
                                Toast.makeText(SongMainActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } else {
                    Toast.makeText(SongMainActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Song> call, Throwable t) {
                log(t);
                Toast.makeText(SongMainActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_sound_today:
            case R.id.ll_sound_random:
                songServerConnection = new HeaderInterceptor(access_token).getClientForSongServer().create(SongServerConnection.class);
                Call<ResponseBody> call_insert_song_hit = songServerConnection.Insert_Song_hit(random_id);
                call_insert_song_hit.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(SongMainActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                        }
                        ///scess
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        log(t);
                        Toast.makeText(SongMainActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                    }
                });

                Intent intent = new Intent(SongMainActivity.this, SongPlayer.class);

                //song info
                intent.putExtra("song_id", random_id);
                intent.putExtra("song_name", random_name);
                intent.putExtra("song_ext", random_ext);
                intent.putExtra("song_title", random_title);
                intent.putExtra("song_singer", random_singer);
                intent.putExtra("song_avatar", random_avatar);
                intent.putExtra("song_category_id", random_category_id);
                intent.putExtra("song_created_at", random_created_at);

                startActivity(intent);
                break;
        }
    }

    private class ChartViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_sound_chart;
        TextView tv_sound_chart_title;
        TextView tv_sound_chart_singer;

        public ChartViewHolder(View itemView) {
            super(itemView);

            iv_sound_chart = (ImageView) itemView.findViewById(R.id.iv_sound_chart);
            tv_sound_chart_singer = (TextView) itemView.findViewById(R.id.tv_sound_chart_singer);
            tv_sound_chart_title = (TextView) itemView.findViewById(R.id.tv_sound_chart_title);

            iv_sound_chart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    songServerConnection = new HeaderInterceptor(access_token).getClientForSongServer().create(SongServerConnection.class);
                    Call<ResponseBody> call_insert_song_hit = songServerConnection.Insert_Song_hit(songList.get(getAdapterPosition()).getId());
                    call_insert_song_hit.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (!response.isSuccessful()) {
                                Toast.makeText(SongMainActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            log(t);
                            Toast.makeText(SongMainActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                        }
                    });

                    Intent intent = new Intent(SongMainActivity.this, SongPlayer.class);

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

    private class ChartAdapter extends RecyclerView.Adapter<ChartViewHolder> {
        Context context;
        ArrayList<Song> songList;
        int layout;

        public ChartAdapter(Context context, ArrayList<Song> songList, int layout) {
            this.context = context;
            this.songList = songList;
            this.layout = layout;
        }

        @Override
        public ChartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ChartViewHolder chartViewHolder = new ChartViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
            return chartViewHolder;
        }

        @Override
        public void onBindViewHolder(ChartViewHolder holder, int position) {
            Glide.with(context).load(getString(R.string.cloud_front_songs_avatar) + songList.get(position).getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_sound_chart);
            holder.tv_sound_chart_singer.setText(songList.get(position).getSinger());
            holder.tv_sound_chart_title.setText(songList.get(position).getTitle());
        }

        @Override
        public int getItemCount() {
            return songList.size();
        }
    }

    private void getChartData() {
        songServerConnection = new HeaderInterceptor(access_token).getClientForSongServer().create(SongServerConnection.class);

        Call<ArrayList<Song>> call_song_list_by_Hits = songServerConnection.song_list_by_Hits();

        call_song_list_by_Hits.enqueue(new Callback<ArrayList<Song>>() {
            @Override
            public void onResponse(Call<ArrayList<Song>> call, Response<ArrayList<Song>> response) {
                if (response.isSuccessful()) {
                    songList = response.body();
                    rv_sound_famous_chart = (RecyclerView) findViewById(R.id.rv_sound_famous_chart);
                    rv_sound_famous_chart.setAdapter(new ChartAdapter(SongMainActivity.this, songList, R.layout.sound_item_chart));
                    rv_sound_famous_chart.setLayoutManager(new LinearLayoutManager(SongMainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                    rv_sound_famous_chart.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin) / 2));

                    final ArrayList<Song> IntentSongList = songList;
                    ib_sound_chart = (ImageButton) findViewById(R.id.ib_sound_chart);
                    ib_sound_chart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 인기 추억 사운드

                            Intent intent = new Intent(SongMainActivity.this, SongChartListActivity.class);
                            intent.putExtra("songList", IntentSongList);
                            startActivity(intent);
                        }
                    });
                } else {
                    Toast.makeText(SongMainActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Song>> call, Throwable t) {
                log(t);
                Toast.makeText(SongMainActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });
    }


    private class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tv_sound_category;
        ImageView iv_sound_category;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            tv_sound_category = (TextView) itemView.findViewById(R.id.tv_sound_category);
            iv_sound_category = (ImageView) itemView.findViewById(R.id.iv_sound_category);

            final ArrayList<SongCategory> intentSongCategoryList = songCategoryList;
            iv_sound_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(SongMainActivity.this, SongCategoryListActivity.class);
                    intent.putExtra("category_id", intentSongCategoryList.get(getAdapterPosition()).getId());
                    intent.putExtra("category_name", intentSongCategoryList.get(getAdapterPosition()).getName());
                    startActivity(intent);
                }
            });
        }
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {
        Context context;
        int layout;
        ArrayList<SongCategory> songCategoryList;

        public CategoryAdapter(Context context, ArrayList<SongCategory> songCategoryList, int layout) {
            this.context = context;
            this.layout = layout;
            this.songCategoryList = songCategoryList;
        }

        @Override
        public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CategoryViewHolder categoryViewHolder = new CategoryViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
            return categoryViewHolder;
        }

        @Override
        public void onBindViewHolder(CategoryViewHolder holder, int position) {
            holder.tv_sound_category.bringToFront();
            holder.tv_sound_category.invalidate();
            holder.tv_sound_category.setText(songCategoryList.get(position).getName());
            Glide.with(context).load(getString(R.string.cloud_front_songs_avatar) + songCategoryList.get(position).getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_sound_category);
        }

        @Override
        public int getItemCount() {
            return songCategoryList.size();
        }
    }

    private void getCategoryData() {
        songServerConnection = new HeaderInterceptor(access_token).getClientForSongServer().create(SongServerConnection.class);

        Call<ArrayList<SongCategory>> call_song_category_List = songServerConnection.song_category_List();

        call_song_category_List.enqueue(new Callback<ArrayList<SongCategory>>() {
            @Override
            public void onResponse(Call<ArrayList<SongCategory>> call, Response<ArrayList<SongCategory>> response) {
                if (response.isSuccessful()) {
                    songCategoryList = response.body();
                    rv_sound_category = (RecyclerView) findViewById(R.id.rv_sound_category);
                    rv_sound_category.setAdapter(new CategoryAdapter(SongMainActivity.this, songCategoryList, R.layout.sound_item_category));
                    rv_sound_category.setLayoutManager(new LinearLayoutManager(SongMainActivity.this, LinearLayoutManager.VERTICAL, false));
                    rv_sound_category.setLayoutManager(new GridLayoutManager(SongMainActivity.this, 3));
                    rv_sound_category.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin) / 2)); // horizontal spacing
                } else {
                    Toast.makeText(SongMainActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SongCategory>> call, Throwable t) {
                log(t);
                Toast.makeText(SongMainActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });
    }


    private class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.bottom = space;
            outRect.right = space;
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
