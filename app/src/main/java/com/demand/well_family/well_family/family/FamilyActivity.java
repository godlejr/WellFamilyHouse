package com.demand.well_family.well_family.family;

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
import android.widget.Button;
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
import com.demand.well_family.well_family.WriteActivity;
import com.demand.well_family.well_family.connection.Server_Connection;
import com.demand.well_family.well_family.dto.Check;
import com.demand.well_family.well_family.dto.CommentCount;
import com.demand.well_family.well_family.dto.LikeCount;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.dto.StoryInfo;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.market.MarketMainActivity;
import com.demand.well_family.well_family.memory_sound.SoundMainActivity;
import com.demand.well_family.well_family.photos.PhotosActivity;
import com.demand.well_family.well_family.search.SearchUserActivity;
import com.demand.well_family.well_family.users.UserActivity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.demand.well_family.well_family.LoginActivity.finishList;

//import com.demand.well_family.well_family.WriteActivity;
//import com.demand.well_family.well_family.photos.PhotosActivity;

/**
 * Created by Dev-0 on 2017-01-19.
 */

public class FamilyActivity extends Activity {
    private ImageView iv_family_edit;
    private ImageView iv_family_avatar;
    private TextView tv_family_content;
    private RecyclerView rv_family_users;
    private Button btn_family_photos, btn_family_write;
    private RecyclerView rv_family_content;

    //family_info
    private int family_id;
    private String family_name;
    private String family_content;
    private String family_avatar;
    private int family_user_id;
    private String family_created_at;

    //user_info
    private int user_id;
    private String user_email;
    private String user_name;
    private String user_birth;
    private String user_phone;
    private int user_level;
    private String user_avatar;


    //list
    private ArrayList<User> userList;
    private ArrayList<StoryInfo> storyList;

    private UserAdapter userAdapter;
    private ContentAdapter contentAdapter;

    //intent code
    private static final int WRITE_REQUEST = 1;
    private static final int DETAIL_REQUEST = 2;
    private static final int CONTENTS_OFFSET = 20;
    private boolean content_isFinished = false;

    //content
    private int content_size;
    private ArrayList<StoryInfo> contentList;
    private int contentInsertCount = 0;

    // toolbar
    private DrawerLayout dl;
    private ProgressDialog progressDialog;

    private ContentHandler contentHandler;
    private MainHanlder mainHanlder;
    private Message msg;
    private ImageView iv_family_writer_avatar;

    private View dialogView;
    private Server_Connection server_connection;
    private LinearLayout ll_user_add_exist;
    private ContentAddHandler contentAddHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_main);

        finishList.add(this);

        family_id = getIntent().getIntExtra("family_id", 0);
        family_name = getIntent().getStringExtra("family_name");
        family_content = getIntent().getStringExtra("family_content");
        family_avatar = getIntent().getStringExtra("family_avatar");
        family_user_id = getIntent().getIntExtra("family_user_id", 0);
        family_created_at = getIntent().getStringExtra("family_created_at");

        init();
        getUserData();
        getContentsData();
        editFamilyData();

        setToolbar(this.getWindow().getDecorView(), this.getApplicationContext(), family_name);
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
                Intent intent = new Intent(FamilyActivity.this, UserActivity.class);
                //userinfo
                intent.putExtra("story_user_id", user_id);
                intent.putExtra("story_user_email", user_email);
                intent.putExtra("story_user_birth", user_birth);
                intent.putExtra("story_user_phone", user_phone);
                intent.putExtra("story_user_name", user_name);
                intent.putExtra("story_user_level", user_level);
                intent.putExtra("story_user_avatar", user_avatar);

                intent.putExtra("user_id", user_id);
                intent.putExtra("user_" +
                        "name", user_name);
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
        Log.e("ttttt5", view + "");

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
                        intent = new Intent(FamilyActivity.this, MainActivity.class);
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
                        intent = new Intent(FamilyActivity.this, MarketMainActivity.class);
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

                        intent = new Intent(FamilyActivity.this, LoginActivity.class);
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
                        startLink.putExtra("user_id", user_id);
                        startLink.putExtra("user_level", user_level);
                        startLink.putExtra("user_email", user_email);
                        startLink.putExtra("user_phone", user_phone);
                        startLink.putExtra("user_name", user_name);
                        startLink.putExtra("user_avatar", user_avatar);
                        startLink.putExtra("user_birth", user_birth);
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

    private void editFamilyData() {
        iv_family_edit = (ImageView) findViewById(R.id.iv_family_edit);
        iv_family_edit.bringToFront();
        iv_family_edit.invalidate();
        iv_family_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    //content                                                                                                               //<-------------------- getContentsData
    private void getContentsData() {
        rv_family_content = (RecyclerView) findViewById(R.id.rv_family_contents);

        storyList = new ArrayList<>();

        mainHanlder = new MainHanlder();
        progressDialog = new ProgressDialog(FamilyActivity.this);
        progressDialog.show();
        progressDialog.getWindow().setBackgroundDrawable(new
                ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.progress_dialog);


        new Thread(new Runnable() {
            @Override
            public void run() {
                server_connection = Server_Connection.retrofit.create(Server_Connection.class);

                Call<ArrayList<StoryInfo>> call = server_connection.family_content_List(String.valueOf(family_id));
                call.enqueue(new Callback<ArrayList<StoryInfo>>() {
                    @Override
                    public void onResponse(Call<ArrayList<StoryInfo>> call, Response<ArrayList<StoryInfo>> response) {
                        contentList = response.body();

                        if (contentList.size() == 0) {
                            // contents 비어있음
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
                                storyList.add(new StoryInfo(contentList.get(i).getUser_id(), contentList.get(i).getName(), contentList.get(i).getAvatar(),
                                        contentList.get(i).getStory_id(), contentList.get(i).getCreated_at(), contentList.get(i).getContent()));
                            }
                        }
                        contentAdapter = new ContentAdapter(FamilyActivity.this, storyList, R.layout.item_main_story);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("contentAdapter", contentAdapter);

                        msg = new Message();
                        msg.setData(bundle);
                        mainHanlder.sendMessage(msg);
                    }

                    @Override
                    public void onFailure(Call<ArrayList<StoryInfo>> call, Throwable t) {
                        Toast.makeText(FamilyActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                    }
                });

                mainHanlder.postDelayed(new Runnable() {
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
        }).start();

        ////<------------- 여기까지 러너블

        NestedScrollView nsv = (NestedScrollView) findViewById(R.id.nsv);
        nsv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
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

                        progressDialog = new ProgressDialog(FamilyActivity.this);
                        progressDialog.show();
                        progressDialog.getWindow().setBackgroundDrawable(new
                                ColorDrawable(Color.TRANSPARENT));
                        progressDialog.setContentView(R.layout.progress_dialog);


                        final int finalLoopSize = loopSize;
                        contentHandler = new ContentHandler();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    //dialogView.setEnabled(false);
                                    //Thread.sleep(600);
                                    //rv_family_users.setEnabled(false);

                                    for (int i = (CONTENTS_OFFSET * contentInsertCount); i < finalLoopSize + (CONTENTS_OFFSET * contentInsertCount); i++) {

                                        storyList.add(new StoryInfo(contentList.get(i).getUser_id(), contentList.get(i).getName(), contentList.get(i).getAvatar(),
                                                contentList.get(i).getStory_id(), contentList.get(i).getCreated_at(), contentList.get(i).getContent()));
                                        //handler
                                        msg = new Message();
                                        Bundle bundle = new Bundle();
                                        bundle.putInt("item_id", i);
                                        msg.setData(bundle);
                                        contentHandler.sendMessage(msg);
                                    }
                                    Thread.sleep(200);
                                    //rv_family_users.setEnabled(true);
                                } catch (Exception e) {
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

    class ContentAddHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            contentAdapter.notifyItemInserted(0);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class ContentHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            contentAdapter.notifyItemChanged(bundle.getInt("item_id"));
        }
    }

    class MainHanlder extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            rv_family_content.setAdapter((ContentAdapter) bundle.getSerializable("contentAdapter"));
            rv_family_content.setLayoutManager(new LinearLayoutManager(FamilyActivity.this, LinearLayoutManager.VERTICAL, false));

        }
    }

    private class ContentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView iv_writer_avatar;
        private TextView tv_writer_name;
        private TextView tv_write_date;
        private TextView tv_content_text;
        private CheckBox btn_item_main_story_like;

        private TextView tv_item_main_story_like;
        private TextView tv_item_main_comment_story_count;
        private ImageButton btn_item_main_story_comment;

        private LinearLayout ll_item_main_story_like_comment_info;
        private LinearLayout story_images_container;
        private LayoutInflater story_images_inflater;

        public ContentViewHolder(View itemView) {
            super(itemView);
            iv_writer_avatar = (ImageView) itemView.findViewById(R.id.iv_item_story_avatar);
            tv_writer_name = (TextView) itemView.findViewById(R.id.tv_item_story_name);
            tv_write_date = (TextView) itemView.findViewById(R.id.tv_item_story_date);
            tv_content_text = (TextView) itemView.findViewById(R.id.tv_item_main_story_content);
            btn_item_main_story_like = (CheckBox) itemView.findViewById(R.id.btn_item_main_story_like);

            tv_item_main_story_like = (TextView) itemView.findViewById(R.id.tv_item_main_story_like);
            tv_item_main_comment_story_count = (TextView) itemView.findViewById(R.id.tv_item_main_comment_story_count);

            story_images_container = (LinearLayout) itemView.findViewById(R.id.story_images_container);
            story_images_inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

            ll_item_main_story_like_comment_info = (LinearLayout) itemView.findViewById(R.id.ll_item_main_story_like_comment_info);
            btn_item_main_story_comment = (ImageButton) itemView.findViewById(R.id.btn_item_main_story_comment);

            //like
            btn_item_main_story_like.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (storyList.get(getAdapterPosition()).getFirst_checked()) {
                        if (isChecked) {
                            server_connection = Server_Connection.retrofit.create(Server_Connection.class);
                            HashMap<String, String> map = new HashMap<>();
                            map.put("user_id", String.valueOf(user_id));

                            Call<ResponseBody> call_like = server_connection.family_content_like_up(String.valueOf(storyList.get(getAdapterPosition()).getStory_id()), map);
                            call_like.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    tv_item_main_story_like.setText(String.valueOf(Integer.parseInt(tv_item_main_story_like.getText().toString()) + 1));
                                    storyList.get(getAdapterPosition()).setChecked(true);
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(FamilyActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            server_connection = Server_Connection.retrofit.create(Server_Connection.class);
                            HashMap<String, String> map = new HashMap<>();
                            map.put("user_id", String.valueOf(user_id));

                            Call<ResponseBody> call_dislike = server_connection.family_content_like_down(String.valueOf(storyList.get(getAdapterPosition()).getStory_id()), map);
                            call_dislike.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    tv_item_main_story_like.setText(String.valueOf(Integer.parseInt(tv_item_main_story_like.getText().toString()) - 1));
                                    storyList.get(getAdapterPosition()).setChecked(false);
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Toast.makeText(FamilyActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }
            });
            iv_writer_avatar.setOnClickListener(this);
            tv_writer_name.setOnClickListener(this);

            tv_content_text.setOnClickListener(this);
            story_images_container.setOnClickListener(this);
            ll_item_main_story_like_comment_info.setOnClickListener(this);
            btn_item_main_story_comment.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_item_story_avatar:
                case R.id.tv_item_story_name:
                    server_connection = Server_Connection.retrofit.create(Server_Connection.class);
                    HashMap<String, String> map = new HashMap<>();
                    map.put("user_id", String.valueOf(user_id));

                    Call<ArrayList<User>> call_user = server_connection.user_Info(map);
                    call_user.enqueue(new Callback<ArrayList<User>>() {
                        @Override
                        public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                            if (response.body().size() == 0) {
                                //spring error
                            } else {
                                Intent intent = new Intent(FamilyActivity.this, UserActivity.class);

                                //userinfo
                                intent.putExtra("story_user_id", response.body().get(0).getId());
                                intent.putExtra("story_user_email", response.body().get(0).getEmail());
                                intent.putExtra("story_user_birth", response.body().get(0).getBirth());
                                intent.putExtra("story_user_phone", response.body().get(0).getPhone());
                                intent.putExtra("story_user_name", response.body().get(0).getName());
                                intent.putExtra("story_user_level", response.body().get(0).getLevel());
                                intent.putExtra("story_user_avatar", response.body().get(0).getAvatar());

                                intent.putExtra("user_id", user_id);
                                intent.putExtra("user_name", user_name);
                                intent.putExtra("user_avatar", user_avatar);
                                intent.putExtra("user_email", user_email);
                                intent.putExtra("user_birth", user_birth);
                                intent.putExtra("user_phone", user_phone);
                                intent.putExtra("user_level", user_level);

                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<User>> call, Throwable t) {

                        }
                    });

                    break;

                case R.id.tv_item_main_story_content:
                case R.id.ll_item_main_story_like_comment_info:
                case R.id.story_images_container:
                case R.id.btn_item_main_story_comment:
                    Intent intent = new Intent(FamilyActivity.this, DetailStoryActivity.class);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("user_email", user_email);
                    intent.putExtra("user_birth", user_birth);
                    intent.putExtra("user_phone", user_phone);
                    intent.putExtra("user_name", user_name);
                    intent.putExtra("user_level", user_level);
                    intent.putExtra("user_avatar", user_avatar);

                    intent.putExtra("content_user_id", storyList.get(getAdapterPosition()).getUser_id());
                    intent.putExtra("content", storyList.get(getAdapterPosition()).getContent());
                    intent.putExtra("story_id", storyList.get(getAdapterPosition()).getStory_id());
                    intent.putExtra("story_user_name", storyList.get(getAdapterPosition()).getName());
                    intent.putExtra("story_user_avatar", storyList.get(getAdapterPosition()).getAvatar());
                    intent.putExtra("story_user_created_at", storyList.get(getAdapterPosition()).getCreated_at());
                    intent.putExtra("like_checked", storyList.get(getAdapterPosition()).getChecked());
                    intent.putExtra("position", getAdapterPosition());
                    startActivityForResult(intent, DETAIL_REQUEST);
                    break;
            }
        }
    }

    private class ContentAdapter extends RecyclerView.Adapter<ContentViewHolder> implements Serializable {
        private Context context;
        private ArrayList<StoryInfo> storyList;
        private int layout;
        //photo recycler
        private RecyclerView rv_main_story;

        public ContentAdapter(Context context, ArrayList<StoryInfo> storyList, int layout) {
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
            //user info
            Glide.with(context).load(getString(R.string.cloud_front_user_avatar) + storyList.get(position).getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_writer_avatar);
            holder.tv_writer_name.setText(storyList.get(position).getName());
            holder.tv_write_date.setText(calculateTime(storyList.get(position).getCreated_at()));

            //content
            holder.tv_content_text.setText(storyList.get(position).getContent());

            //images
            server_connection = Server_Connection.retrofit.create(Server_Connection.class);
            Call<ArrayList<Photo>> call_photo = server_connection.family_content_photo_List(String.valueOf(storyList.get(position).getStory_id()));
            call_photo.enqueue(new Callback<ArrayList<Photo>>() {
                @Override
                public void onResponse(Call<ArrayList<Photo>> call, Response<ArrayList<Photo>> response) {
                    ArrayList<Photo>  photoList = response.body();

                    if (photoList.size() == 0) {
                        holder.story_images_container.setVisibility(View.GONE);
                        holder.tv_content_text.setMaxLines(15);
                    }
                    if (photoList.size() == 1) {
                        holder.story_images_container.removeAllViews();
                        holder.story_images_container.setVisibility(View.VISIBLE);
                        holder.story_images_container.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1200));
                        holder.story_images_inflater.inflate(R.layout.item_main_story_image_one, holder.story_images_container, true);
                        ImageView iv_item_main_story_image = (ImageView) holder.story_images_container.findViewById(R.id.iv_item_main_story_image);
                        Glide.with(context).load(getString(R.string.cloud_front_stories_images) + photoList.get(0).getName() + "." + photoList.get(0).getExt()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_item_main_story_image);
                    }

                    if (photoList.size() == 2) {
                        holder.story_images_container.removeAllViews();
                        holder.story_images_container.setVisibility(View.VISIBLE);

                        holder.story_images_inflater.inflate(R.layout.item_main_story_image_two, holder.story_images_container, true);
                        ImageView iv_item_main_story_image_two1 = (ImageView) holder.story_images_container.findViewById(R.id.iv_item_main_story_image_two1);
                        Glide.with(context).load(getString(R.string.cloud_front_stories_images) + photoList.get(0).getName() + "." + photoList.get(0).getExt()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_item_main_story_image_two1);
                        ImageView iv_item_main_story_image_two2 = (ImageView) holder.story_images_container.findViewById(R.id.iv_item_main_story_image_two2);
                        Glide.with(context).load(getString(R.string.cloud_front_stories_images) + photoList.get(1).getName() + "." + photoList.get(1).getExt()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_item_main_story_image_two2);
                    }

                    if (photoList.size() > 2) {
                        holder.story_images_container.removeAllViews();
                        holder.story_images_container.setVisibility(View.VISIBLE);

                        holder.story_images_inflater.inflate(R.layout.item_main_story_image_list, holder.story_images_container, true);
                        rv_main_story = (RecyclerView) holder.story_images_container.findViewById(R.id.rv_main_story_images);
                        PhotoAdapter photoAdapter = new PhotoAdapter(context, photoList, R.layout.item_main_story_image, position);
                        rv_main_story.setAdapter(photoAdapter);
                        rv_main_story.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                        photoAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Photo>> call, Throwable t) {
                    Toast.makeText(FamilyActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                }
            });


            server_connection = Server_Connection.retrofit.create(Server_Connection.class);
            Call<ArrayList<LikeCount>> call_like_count = server_connection.family_like_Count(String.valueOf(storyList.get(position).getStory_id()));
            call_like_count.enqueue(new Callback<ArrayList<LikeCount>>() {
                @Override
                public void onResponse(Call<ArrayList<LikeCount>> call, Response<ArrayList<LikeCount>> response) {
                        int like_count = response.body().get(0).getLike_count();
                        holder.tv_item_main_story_like.setText(String.valueOf(like_count));
                }
                @Override
                public void onFailure(Call<ArrayList<LikeCount>> call, Throwable t) {
                    Toast.makeText(FamilyActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                }
            });

            server_connection = Server_Connection.retrofit.create(Server_Connection.class);
            Call<ArrayList<CommentCount>> call_comment_count = server_connection.family_comment_Count(String.valueOf(storyList.get(position).getStory_id()));
            call_comment_count.enqueue(new Callback<ArrayList<CommentCount>>() {
                @Override
                public void onResponse(Call<ArrayList<CommentCount>> call, Response<ArrayList<CommentCount>> response) {
                        int comment_count = response.body().get(0).getComment_count();
                        holder.tv_item_main_comment_story_count.setText(String.valueOf(comment_count));
                }
                @Override
                public void onFailure(Call<ArrayList<CommentCount>> call, Throwable t) {
                    Toast.makeText(FamilyActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                }
            });

            server_connection = Server_Connection.retrofit.create(Server_Connection.class);
            HashMap<String, String> map = new HashMap<>();
            map.put("user_id", String.valueOf(user_id));
            Call<ArrayList<Check>> call_like_check = server_connection.family_content_like_check(String.valueOf(storyList.get(position).getStory_id()), map);
            call_like_check.enqueue(new Callback<ArrayList<Check>>() {
                @Override
                public void onResponse(Call<ArrayList<Check>> call, Response<ArrayList<Check>> response) {
                    int checked = response.body().get(0).getChecked();
                    if (checked == 1) {
                        holder.btn_item_main_story_like.setChecked(true);
                        storyList.get(position).setFirst_checked(true);
                        storyList.get(position).setChecked(true);
                    } else {
                        holder.btn_item_main_story_like.setChecked(false);
                        storyList.get(position).setFirst_checked(true);
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Check>> call, Throwable t) {
                    Toast.makeText(FamilyActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                }
            });
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

        @Override
        public int getItemCount() {
            return storyList.size();
        }
    }


    //photo list
    private class PhotoViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout ll_main_story_image;
        private ImageView iv_main_story_content;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ll_main_story_image = (LinearLayout) itemView.findViewById(R.id.ll_main_story_image);
            iv_main_story_content = (ImageView) itemView.findViewById(R.id.iv_main_story_content);

        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoViewHolder> {
        private Context context;
        private ArrayList<Photo> photoList;
        private int layout;
        private int story_position;

        public PhotoAdapter(Context context, ArrayList<Photo> photoList, int layout, int story_position) {
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
                    Intent intent = new Intent(FamilyActivity.this, DetailStoryActivity.class);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("user_email", user_email);
                    intent.putExtra("user_birth", user_birth);
                    intent.putExtra("user_phone", user_phone);
                    intent.putExtra("user_name", user_name);
                    intent.putExtra("user_level", user_level);
                    intent.putExtra("user_avatar", user_avatar);

                    intent.putExtra("content_user_id", storyList.get(story_position).getUser_id());
                    intent.putExtra("content", storyList.get(story_position).getContent());
                    intent.putExtra("story_id", storyList.get(story_position).getStory_id());
                    intent.putExtra("story_user_name", storyList.get(story_position).getName());
                    intent.putExtra("story_user_avatar", storyList.get(story_position).getAvatar());
                    intent.putExtra("story_user_created_at", storyList.get(story_position).getCreated_at());
                    intent.putExtra("like_checked", storyList.get(story_position).getChecked());
                    intent.putExtra("position", story_position);
                    startActivityForResult(intent, DETAIL_REQUEST);
                }
            });
            Glide.with(context).load(getString(R.string.cloud_front_stories_images) + photoList.get(position).getName() + "." + photoList.get(position).getExt()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_main_story_content);
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

    //user
    private void getUserData() {
        user_id = getIntent().getIntExtra("user_id",0);
        user_name = getIntent().getStringExtra("user_name");
        user_level = getIntent().getIntExtra("user_level",0);
        user_avatar = getIntent().getStringExtra("user_avatar");
        user_email = getIntent().getStringExtra("user_email");
        user_phone = getIntent().getStringExtra("user_phone");
        user_birth = getIntent().getStringExtra("user_birth");

        //write user_avatar
        iv_family_writer_avatar = (ImageView) findViewById(R.id.iv_family_writer_avatar);
        Glide.with(this).load(getString(R.string.cloud_front_user_avatar) + user_avatar).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_family_writer_avatar);
        //grid view
        rv_family_users = (RecyclerView) findViewById(R.id.rv_family_users);

        //my info
        userList = new ArrayList<>();
        userList.add(new User(user_id, user_email, user_name, user_birth, user_phone, user_avatar, user_level));

        server_connection = Server_Connection.retrofit.create(Server_Connection.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", String.valueOf(user_id));

        Call<ArrayList<User>> call_users = server_connection.family_user_Info(String.valueOf(family_id), map);
        call_users.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                if(response.body().size() ==0){
                    //유저 없음
                }else{
                    for (int i = 0; i < response.body().size(); i++) {
                        userList.add(new User(response.body().get(i).getId(), response.body().get(i).getEmail(), response.body().get(i).getName(), response.body().get(i).getBirth(),
                                response.body().get(i).getPhone(), response.body().get(i).getAvatar(), response.body().get(i).getLevel()));
                    }
                }
                userAdapter = new UserAdapter(FamilyActivity.this, userList, R.layout.item_users_familys);
                rv_family_users.setAdapter(userAdapter);
                rv_family_users.setLayoutManager(new LinearLayoutManager(FamilyActivity.this, LinearLayoutManager.HORIZONTAL, false));
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                Toast.makeText(FamilyActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });


    }

    private class UserViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_family_item;
        private TextView name;

        public UserViewHolder(View itemView) {
            super(itemView);
            iv_family_item = (ImageView) itemView.findViewById(R.id.iv_family_item);
            name = (TextView) itemView.findViewById(R.id.tv_family_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FamilyActivity.this, UserActivity.class);
                    //user info
                    intent.putExtra("story_user_id", userList.get(getAdapterPosition()).getId());
                    intent.putExtra("story_user_email", userList.get(getAdapterPosition()).getEmail());
                    intent.putExtra("story_user_birth", userList.get(getAdapterPosition()).getBirth());
                    intent.putExtra("story_user_phone", userList.get(getAdapterPosition()).getPhone());
                    intent.putExtra("story_user_name", userList.get(getAdapterPosition()).getName());
                    intent.putExtra("story_user_level", userList.get(getAdapterPosition()).getLevel());
                    intent.putExtra("story_user_avatar", userList.get(getAdapterPosition()).getAvatar());

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
        }
    }

    private class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {
        private Context context;
        private ArrayList<User> userList;
        private int layout;

        public UserAdapter(Context context, ArrayList<User> userList, int layout) {
            this.context = context;
            this.userList = userList;
            this.layout = layout;
        }

        @Override
        public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            UserViewHolder userViewHolder = new UserViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
            return userViewHolder;
        }

        @Override
        public void onBindViewHolder(UserViewHolder holder, int position) {
            Glide.with(context).load(getString(R.string.cloud_front_user_avatar) + userList.get(position).getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_family_item);

            if (userList.get(position).getId() == user_id) {
                holder.name.setText("나");
            } else {
                holder.name.setText(userList.get(position).getName());
            }
        }

        @Override
        public int getItemCount() {
            return userList.size();
        }
    }


    private void init() {
        iv_family_avatar = (ImageView) findViewById(R.id.iv_family_avatar);
        tv_family_content = (TextView) findViewById(R.id.tv_family_content);
        ll_user_add_exist = (LinearLayout)findViewById(R.id.ll_user_add_exist);

        ll_user_add_exist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FamilyActivity.this, SearchUserActivity.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("user_name", user_name);
                intent.putExtra("user_avatar", user_avatar);
                intent.putExtra("user_email", user_email);
                intent.putExtra("user_birth", user_birth);
                intent.putExtra("user_phone", user_phone);
                intent.putExtra("user_level", user_level);

                intent.putExtra("family_id", family_id);
                intent.putExtra("family_name", family_name);
                intent.putExtra("family_content", family_content);
                intent.putExtra("family_avatar", family_avatar);
                intent.putExtra("family_user_id", family_user_id);
                intent.putExtra("family_created_at", family_created_at);

                startActivity(intent);
                finish();
            }
        });

        Glide.with(this).load(getString(R.string.cloud_front_family_avatar) + family_avatar).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_family_avatar);
        tv_family_content.setText(getIntent().getStringExtra("family_content"));

        btn_family_photos = (Button) findViewById(R.id.btn_family_photos);
        btn_family_photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FamilyActivity.this, PhotosActivity.class);
                intent.putExtra("family_id", family_id);
                intent.putExtra("family_name", family_name);
                intent.putExtra("family_content", family_content);
                intent.putExtra("family_avatar", family_avatar);
                intent.putExtra("family_user_id", family_user_id);
                intent.putExtra("family_created_at", family_created_at);

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

        // 버튼 을 텍스트뷰로 바꿔서 자바단 수정
//        글쓰기 버튼
        /*btn_family_write = (Button) findViewById(R.id.btn_family_write);
        btn_family_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FamilyActivity.this, WriteActivity.class);
                intent.putExtra("family_id", family_id);
                intent.putExtra("family_name", family_name);
                intent.putExtra("family_content", family_content);
                intent.putExtra("family_avatar", family_avatar);

                intent.putExtra("user_id", user_id);
                intent.putExtra("user_name", user_name);
                intent.putExtra("user_avatar", user_avatar);
                intent.putExtra("user_email", user_email);
                intent.putExtra("user_birth", user_birth);
                intent.putExtra("user_phone", user_phone);
                intent.putExtra("user_level", user_level);
                startActivityForResult(intent, WRITE_REQUEST);
            }
        });*/
        TextView btn_family_write = (TextView) findViewById(R.id.btn_family_write);
        btn_family_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FamilyActivity.this, WriteActivity.class);
                intent.putExtra("family_id", family_id);
                intent.putExtra("family_name", family_name);
                intent.putExtra("family_content", family_content);
                intent.putExtra("family_avatar", family_avatar);
                intent.putExtra("family_user_id", family_user_id);
                intent.putExtra("family_created_at", family_created_at);

                intent.putExtra("user_id", user_id);
                intent.putExtra("user_name", user_name);
                intent.putExtra("user_avatar", user_avatar);
                intent.putExtra("user_email", user_email);
                intent.putExtra("user_birth", user_birth);
                intent.putExtra("user_phone", user_phone);
                intent.putExtra("user_level", user_level);
                startActivityForResult(intent, WRITE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WRITE_REQUEST) { //글쓰기
            if (resultCode == RESULT_OK) {
                progressDialog = new ProgressDialog(FamilyActivity.this);
                progressDialog.show();
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                progressDialog.setContentView(R.layout.progress_dialog);
                contentAddHandler = new ContentAddHandler();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        StoryInfo storyInfo = (StoryInfo) data.getSerializableExtra("storyInfo");
                        storyList.add(0, storyInfo);

                        msg = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putInt("item_id", 0);
                        msg.setData(bundle);
                        contentAddHandler.sendMessage(msg);
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                }).start();

            }
        }

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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(FamilyActivity.this, MainActivity.class);
        intent.putExtra("user_id", user_id);
        intent.putExtra("user_email", user_email);
        intent.putExtra("user_birth", user_birth);
        intent.putExtra("user_phone", user_phone);
        intent.putExtra("user_name", user_name);
        intent.putExtra("user_level", user_level);
        intent.putExtra("user_avatar", user_avatar);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        super.onBackPressed();
    }
}
