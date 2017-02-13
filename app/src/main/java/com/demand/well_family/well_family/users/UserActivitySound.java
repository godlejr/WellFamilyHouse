package com.demand.well_family.well_family.users;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
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
import com.demand.well_family.well_family.LoginActivity;
import com.demand.well_family.well_family.MainActivity;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.connection.Server_Connection;
import com.demand.well_family.well_family.dto.Check;
import com.demand.well_family.well_family.dto.CommentCount;
import com.demand.well_family.well_family.dto.LikeCount;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.dto.SongPhoto;
import com.demand.well_family.well_family.dto.SongStory;
import com.demand.well_family.well_family.dto.SongStoryEmotionData;
import com.demand.well_family.well_family.dto.SongStoryEmotionInfo;
import com.demand.well_family.well_family.dto.SongStoryInfo;
import com.demand.well_family.well_family.market.MarketMainActivity;
import com.demand.well_family.well_family.memory_sound.SoundMainActivity;

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

import static com.demand.well_family.well_family.LoginActivity.finishList;

/**
 * Created by ㅇㅇ on 2017-02-02.
 */

public class UserActivitySound extends Activity {
    //user_inf0(accessor)
    private int user_id;
    private String user_name;
    private int user_level;
    private String user_avatar;
    private String user_email;
    private String user_phone;
    private String user_birth;

    //story user info
    private int story_user_id;
    private String story_user_name;
    private int story_user_level;
    private String story_user_avatar;
    private String story_user_email;
    private String story_user_phone;
    private String story_user_birth;

    private Server_Connection server_connection;

    private RecyclerView rv_user_activity_sound;
    private ArrayList<Photo> photoList;
    private ArrayList<SongStoryInfo> storyList;
    private ContentAdapter contentAdapter;

    private String url;

    //content
    private int content_size;
    private boolean content_isFinished = false;
    private ArrayList<SongStory> contentList;
    private int contentInsertCount = 0;
    private static final int CONTENTS_OFFSET = 20;


    //intent code
    private static final int DETAIL_REQUEST = 2;

    //toolbar
    private DrawerLayout dl;
    private ProgressDialog progressDialog;
    private ContentHandler contentHandler;
    private MainHandler mainHandler;
    private Message msg;

    private Call<ArrayList<SongStory>> call_content;
    private Server_Connection server_connection_for_content;

    // emotion
    private RecyclerView rv_song_story_emotion;
    private EmotionAdapter emotionAdapter;
    private ArrayList<SongStoryEmotionData> emotionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sound);

        finishList.add(this);

        init();
        getContentsData();

        setToolbar(this.getWindow().getDecorView(), this.getApplicationContext(), "추억 소리");
    }

    private void init() {
        story_user_id = getIntent().getIntExtra("story_user_id", 0);
        story_user_name = getIntent().getStringExtra("story_user_name");
        story_user_level = getIntent().getIntExtra("story_user_level", 0);
        story_user_avatar = getIntent().getStringExtra("story_user_avatar");
        story_user_email = getIntent().getStringExtra("story_user_email");
        story_user_phone = getIntent().getStringExtra("story_user_phone");
        story_user_birth = getIntent().getStringExtra("story_user_birth");

        user_id = getIntent().getIntExtra("user_id", 0);
        user_name = getIntent().getStringExtra("user_name");
        user_level = getIntent().getIntExtra("user_level", 0);
        user_avatar = getIntent().getStringExtra("user_avatar");
        user_email = getIntent().getStringExtra("user_email");
        user_phone = getIntent().getStringExtra("user_phone");
        user_birth = getIntent().getStringExtra("user_birth");


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

                Intent intent = new Intent(UserActivitySound.this, UserActivity.class);
                //userinfo
                intent.putExtra("story_user_id", user_id);
                intent.putExtra("story_user_email", user_email);
                intent.putExtra("story_user_birth", user_birth);
                intent.putExtra("story_user_phone", user_phone);
                intent.putExtra("story_user_name", user_name);
                intent.putExtra("story_user_level", user_level);
                intent.putExtra("story_user_avatar", user_avatar);

                intent.putExtra("user_id", user_id);
                intent.putExtra("user_name", user_name);
                intent.putExtra("user_avatar", user_avatar);
                intent.putExtra("user_email", user_email);
                intent.putExtra("user_birth", user_birth);
                intent.putExtra("user_phone", user_phone);
                intent.putExtra("user_level", user_level);

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
            e.printStackTrace();
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
                        intent = new Intent(UserActivitySound.this, MainActivity.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("user_email", user_email);
                        intent.putExtra("user_birth", user_birth);
                        intent.putExtra("user_phone", user_phone);
                        intent.putExtra("user_name", user_name);
                        intent.putExtra("user_level", user_level);
                        intent.putExtra("user_avatar", user_avatar);

                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;

                    case R.id.menu_search:
                        Toast.makeText(getApplicationContext(), "준비중입니다.", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.menu_market:
                        intent = new Intent(UserActivitySound.this, MarketMainActivity.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("user_email", user_email);
                        intent.putExtra("user_birth", user_birth);
                        intent.putExtra("user_phone", user_phone);
                        intent.putExtra("user_name", user_name);
                        intent.putExtra("user_level", user_level);
                        intent.putExtra("user_avatar", user_avatar);
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

                        intent = new Intent(UserActivitySound.this, LoginActivity.class);
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
                        startLink = new Intent(getApplicationContext(), SoundMainActivity.class);
                        startLink.putExtra("user_id", getIntent().getStringExtra("user_id"));
                        startLink.putExtra("user_level", getIntent().getStringExtra("user_level"));
                        startLink.putExtra("user_email", getIntent().getStringExtra("user_email"));
                        startLink.putExtra("user_phone", getIntent().getStringExtra("user_phone"));
                        startLink.putExtra("user_name", getIntent().getStringExtra("user_name"));
                        startLink.putExtra("user_avatar", getIntent().getStringExtra("user_avatar"));
                        startLink.putExtra("user_birth", getIntent().getStringExtra("user_birth"));
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

    private void getContentsData() {
        rv_user_activity_sound = (RecyclerView) findViewById(R.id.rv_user_activity_sound);

        //<----------------- getContentsData 다이얼로그
        mainHandler = new MainHandler();
        progressDialog = new ProgressDialog(UserActivitySound.this);
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
                    server_connection = Server_Connection.retrofit.create(Server_Connection.class);
                    call_content = server_connection.song_story_List_Me(String.valueOf(story_user_id));

                    call_content.enqueue(new Callback<ArrayList<SongStory>>() {
                        @Override
                        public void onResponse(Call<ArrayList<SongStory>> call, Response<ArrayList<SongStory>> response) {
                            contentList = response.body();

                            if (contentList.size() == 0) {
                                //contents  비워 있음
                            } else {
                                content_size = contentList.size();
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
                            contentAdapter = new ContentAdapter(UserActivitySound.this, storyList, R.layout.item_activity_sound);

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
                                        e.printStackTrace();
                                    }
                                }
                            }, 200);
                        }

                        @Override
                        public void onFailure(Call<ArrayList<SongStory>> call, Throwable t) {
                            Toast.makeText(UserActivitySound.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    server_connection = Server_Connection.retrofit.create(Server_Connection.class);
                    HashMap<String, String> map = new HashMap<>();
                    map.put("user_id", String.valueOf(user_id));


                    Call<ArrayList<Check>> call_family_check = server_connection.family_check(String.valueOf(story_user_id), map);
                    call_family_check.enqueue(new Callback<ArrayList<Check>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Check>> call, Response<ArrayList<Check>> response) {
                            if (response.body().get(0).getChecked() > 0) {
                                //family
                                server_connection = Server_Connection.retrofit.create(Server_Connection.class);
                                call_content = server_connection.song_story_List_Family(String.valueOf(story_user_id));
                                call_content.enqueue(new Callback<ArrayList<SongStory>>() {
                                    @Override
                                    public void onResponse(Call<ArrayList<SongStory>> call, Response<ArrayList<SongStory>> response) {
                                        contentList = response.body();

                                        if (contentList.size() == 0) {
                                            //contents  비워 있음
                                        } else {
                                            content_size = contentList.size();
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
                                        contentAdapter = new ContentAdapter(UserActivitySound.this, storyList, R.layout.item_activity_sound);

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
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, 200);
                                    }

                                    @Override
                                    public void onFailure(Call<ArrayList<SongStory>> call, Throwable t) {
                                        Toast.makeText(UserActivitySound.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                //public
                                server_connection = Server_Connection.retrofit.create(Server_Connection.class);
                                call_content = server_connection.song_story_List_Public(String.valueOf(story_user_id));
                                call_content.enqueue(new Callback<ArrayList<SongStory>>() {
                                    @Override
                                    public void onResponse(Call<ArrayList<SongStory>> call, Response<ArrayList<SongStory>> response) {
                                        contentList = response.body();

                                        if (contentList.size() == 0) {
                                            //contents  비워 있음
                                        } else {
                                            content_size = contentList.size();
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
                                        contentAdapter = new ContentAdapter(UserActivitySound.this, storyList, R.layout.item_activity_sound);

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
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, 200);
                                    }

                                    @Override
                                    public void onFailure(Call<ArrayList<SongStory>> call, Throwable t) {
                                        Toast.makeText(UserActivitySound.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Check>> call, Throwable t) {
                            Toast.makeText(UserActivitySound.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
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

                        progressDialog = new ProgressDialog(UserActivitySound.this);
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
                                    e.printStackTrace();
                                }
                                try {
                                    Thread.sleep(200);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
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
            rv_user_activity_sound.setLayoutManager(new LinearLayoutManager(UserActivitySound.this, LinearLayoutManager.VERTICAL, false));

        }
    }

    class ContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_sound_story_title, tv_sound_story_singer, tv_sound_story_user_name, tv_sound_story_content, tv_sound_story_location,
                tv_sound_story_like, tv_sound_story_comment, tv_sound_story_date;
        private CircleImageView iv_sound_story_avatar;
        private LinearLayout ll_sound_story_images_container, ll_sound_story_location, ll_sound_story_images_icon, ll_sound_story_total;
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
//            ll_sound_story_images_icon = (LinearLayout) itemView.findViewById(R.id.ll_sound_story_images_icon);
            ll_sound_story_images_container = (LinearLayout) itemView.findViewById(R.id.ll_sound_story_images_container);
            ll_sound_story_location = (LinearLayout) itemView.findViewById(R.id.ll_sound_story_location);
            ib_item_sound_story_comment = (ImageButton) itemView.findViewById(R.id.ib_item_sound_story_comment);
            cb_item_sound_story_like = (CheckBox) itemView.findViewById(R.id.cb_item_sound_story_like);
            story_images_inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            iv_sound_story_record = (ImageView) itemView.findViewById(R.id.iv_sound_story_record);


            cb_item_sound_story_like.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (storyList.get(getAdapterPosition()).getFirst_checked()) {
                        if (isChecked) {
                            server_connection = Server_Connection.retrofit.create(Server_Connection.class);
                            HashMap<String, String> map = new HashMap<>();
                            map.put("user_id", String.valueOf(user_id));

                            Call<ResponseBody> call_like = server_connection.song_story_like_up(String.valueOf(storyList.get(getAdapterPosition()).getStory_id()), map);
                            call_like.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    tv_sound_story_like.setText(String.valueOf(Integer.parseInt(tv_sound_story_like.getText().toString()) + 1));
                                    storyList.get(getAdapterPosition()).setChecked(true);
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(UserActivitySound.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            server_connection = Server_Connection.retrofit.create(Server_Connection.class);
                            HashMap<String, String> map = new HashMap<>();
                            map.put("user_id", String.valueOf(user_id));

                            Call<ResponseBody> call_dislike = server_connection.song_story_like_down(String.valueOf(storyList.get(getAdapterPosition()).getStory_id()), map);
                            call_dislike.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    tv_sound_story_like.setText(String.valueOf(Integer.parseInt(tv_sound_story_like.getText().toString()) - 1));
                                    storyList.get(getAdapterPosition()).setChecked(false);
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(UserActivitySound.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
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
                    Intent intent = new Intent(UserActivitySound.this, SoundStoryDetail.class);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("user_email", user_email);
                    intent.putExtra("user_birth", user_birth);
                    intent.putExtra("user_phone", user_phone);
                    intent.putExtra("user_name", user_name);
                    intent.putExtra("user_level", user_level);
                    intent.putExtra("user_avatar", user_avatar);

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
//            holder.tv_sound_story_date.setText(calculateTime(storyList.get(position).getCreated_at()));

            //content
            holder.tv_sound_story_content.setText(storyList.get(position).getContent());

            //date
            holder.tv_sound_story_date.setText(calculateTime(storyList.get(position).getCreated_at()));

            //location

            if (storyList.get(position).getLocation()!=null && storyList.get(position).getLocation().length() != 0) {

                holder.ll_sound_story_location.setVisibility(View.VISIBLE);

                holder.tv_sound_story_location.setText(storyList.get(position).getLocation());
            } else {
                holder.ll_sound_story_location.setVisibility(View.GONE);
            }

            //record
            if (storyList.get(position).getRecord_file() !=null && storyList.get(position).getRecord_file().length() != 0) {
                holder.iv_sound_story_record.setVisibility(View.VISIBLE);
            } else {
                holder.iv_sound_story_record.setVisibility(View.GONE);
            }

            //images
            server_connection = Server_Connection.retrofit.create(Server_Connection.class);
            Call<ArrayList<SongPhoto>> call_song_photo = server_connection.song_story_photo_List(String.valueOf(storyList.get(position).getStory_id()));
            call_song_photo.enqueue(new Callback<ArrayList<SongPhoto>>() {
                @Override
                public void onResponse(Call<ArrayList<SongPhoto>> call, Response<ArrayList<SongPhoto>> response) {
                    photoList = response.body();
                    if (photoList.size() == 0) {
                        holder.ll_sound_story_images_container.setVisibility(View.GONE);
                        holder.tv_sound_story_content.setMaxLines(15);
                    }

                    if(photoList.size() == 1){
                        holder.ll_sound_story_images_container.removeAllViews();
                        holder.ll_sound_story_images_container.setVisibility(View.VISIBLE);
                        holder.ll_sound_story_images_container.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1200));
                        holder.story_images_inflater.inflate(R.layout.item_main_story_image_one, holder.ll_sound_story_images_container, true);
                        ImageView iv_item_main_story_image = (ImageView) holder.ll_sound_story_images_container.findViewById(R.id.iv_item_main_story_image);
                        Glide.with(context).load(getString(R.string.cloud_front_song_stories_images) + photoList.get(0).getName() + "." + photoList.get(0).getExt()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_item_main_story_image);
                    }

                    if(photoList.size() == 2){
                        holder.ll_sound_story_images_container.removeAllViews();
                        holder.ll_sound_story_images_container.setVisibility(View.VISIBLE);

                        holder.story_images_inflater.inflate(R.layout.item_main_story_image_two, holder.ll_sound_story_images_container, true);
                        ImageView iv_item_main_story_image_two1 = (ImageView) holder.ll_sound_story_images_container.findViewById(R.id.iv_item_main_story_image_two1);
                        Glide.with(context).load(getString(R.string.cloud_front_song_stories_images) + photoList.get(0).getName() + "." + photoList.get(0).getExt()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_item_main_story_image_two1);
                        ImageView iv_item_main_story_image_two2 = (ImageView) holder.ll_sound_story_images_container.findViewById(R.id.iv_item_main_story_image_two2);
                        Glide.with(context).load(getString(R.string.cloud_front_song_stories_images) + photoList.get(1).getName() + "." + photoList.get(1).getExt()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_item_main_story_image_two2);
                    }

                    if(photoList.size() > 2){
                        holder.ll_sound_story_images_container.removeAllViews();
                        holder.ll_sound_story_images_container.setVisibility(View.VISIBLE);
                        holder.story_images_inflater.inflate(R.layout.item_main_story_image_list, holder.ll_sound_story_images_container, true);
                        RecyclerView rv_main_story = (RecyclerView) holder.ll_sound_story_images_container.findViewById(R.id.rv_main_story_images);
                        PhotoAdapter photoAdapter = new PhotoAdapter(context, photoList, R.layout.item_main_story_image, position);
                        rv_main_story.setAdapter(photoAdapter);
                        rv_main_story.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                        photoAdapter.notifyDataSetChanged();
                        rv_main_story.setHasFixedSize(true);
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<SongPhoto>> call, Throwable t) {
                    Toast.makeText(UserActivitySound.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                }
            });

            server_connection = Server_Connection.retrofit.create(Server_Connection.class);
            Call<ArrayList<LikeCount>> call_like_count = server_connection.song_story_like_Count(String.valueOf(storyList.get(position).getStory_id()));
            call_like_count.enqueue(new Callback<ArrayList<LikeCount>>() {
                @Override
                public void onResponse(Call<ArrayList<LikeCount>> call, Response<ArrayList<LikeCount>> response) {
                    int like_count = response.body().get(0).getLike_count();
                    holder.tv_sound_story_like.setText(String.valueOf(like_count));
                }
                @Override
                public void onFailure(Call<ArrayList<LikeCount>> call, Throwable t) {
                    Toast.makeText(UserActivitySound.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                }
            });

            server_connection = Server_Connection.retrofit.create(Server_Connection.class);
            Call<ArrayList<CommentCount>> call_comment_count = server_connection.song_story_comment_Count(String.valueOf(storyList.get(position).getStory_id()));
            call_comment_count.enqueue(new Callback<ArrayList<CommentCount>>() {
                @Override
                public void onResponse(Call<ArrayList<CommentCount>> call, Response<ArrayList<CommentCount>> response) {
                    int comment_count = response.body().get(0).getComment_count();
                    holder.tv_sound_story_comment.setText(String.valueOf(comment_count));
                }
                @Override
                public void onFailure(Call<ArrayList<CommentCount>> call, Throwable t) {
                    Toast.makeText(UserActivitySound.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                }
            });

            server_connection = Server_Connection.retrofit.create(Server_Connection.class);
            HashMap<String, String> map = new HashMap<>();
            map.put("user_id", String.valueOf(user_id));
            Call<ArrayList<Check>> call_like_check = server_connection.song_story_like_check(String.valueOf(storyList.get(position).getStory_id()), map);
            call_like_check.enqueue(new Callback<ArrayList<Check>>() {
                @Override
                public void onResponse(Call<ArrayList<Check>> call, Response<ArrayList<Check>> response) {
                    int checked = response.body().get(0).getChecked();
                    if (checked == 1) {
                        holder.cb_item_sound_story_like.setChecked(true);
                        storyList.get(position).setFirst_checked(true);
                        storyList.get(position).setChecked(true);
                    } else {
                        holder.cb_item_sound_story_like.setChecked(false);
                        storyList.get(position).setFirst_checked(true);
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Check>> call, Throwable t) {
                    Toast.makeText(UserActivitySound.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
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
                e.printStackTrace();
            }

            long curTime = System.currentTimeMillis();
            long regTime = date.getTime();
            long diffTime = (curTime - regTime) / 1000;

            String msg = null;

            if (diffTime < 60) {
                // sec
                msg = diffTime + "초전";
            } else if ((diffTime /= 60) < 60) {
                // min
                System.out.println(diffTime);

                msg = diffTime + "분전";
            } else if ((diffTime /= 60) < 24) {
                // hour
                msg = (diffTime) + "시간전";
            } else if ((diffTime /= 24) < 7) {
                // day
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
                    Intent intent = new Intent(UserActivitySound.this, SoundStoryDetail.class);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("user_name", user_name);
                    intent.putExtra("user_avatar", user_avatar);
                    intent.putExtra("user_birth", user_birth);

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
        }
    }

    private class EmotionAdapter extends RecyclerView.Adapter<EmotionViewHolder> {
        private ArrayList<SongStoryEmotionInfo> emotionList;
        private Context context;
        private int layout;

        public EmotionAdapter(ArrayList<SongStoryEmotionInfo> emotionList, Context context, int layout) {
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
            outRect.right = space;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DETAIL_REQUEST) { //디테일
            if (resultCode == RESULT_OK) {
                int position = data.getIntExtra("position", 0);
                Boolean like_checked = data.getBooleanExtra("like_checked", false);
                storyList.get(position).setFirst_checked(false); //like sync
                storyList.get(position).setChecked(like_checked); //like sync
                contentAdapter.notifyItemChanged(position);
            }
        }
    }

}
