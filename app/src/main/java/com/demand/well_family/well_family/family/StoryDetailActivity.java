package com.demand.well_family.well_family.family;

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
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.LoginActivity;
import com.demand.well_family.well_family.MainActivity;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.connection.StoryServerConnection;
import com.demand.well_family.well_family.dialog.StoryPopupActivity;
import com.demand.well_family.well_family.interceptor.HeaderInterceptor;
import com.demand.well_family.well_family.settings.SettingActivity;
import com.demand.well_family.well_family.dialog.CommentPopupActivity;
import com.demand.well_family.well_family.dto.Comment;
import com.demand.well_family.well_family.dto.CommentInfo;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.market.MarketMainActivity;
import com.demand.well_family.well_family.memory_sound.SongMainActivity;
import com.demand.well_family.well_family.photos.PhotoPopupActivity;
import com.demand.well_family.well_family.users.UserActivity;
import com.demand.well_family.well_family.util.ErrorUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

/**
 * Created by Dev-0 on 2017-01-23.
 */

public class StoryDetailActivity extends Activity implements CompoundButton.OnCheckedChangeListener {
    private int user_id;
    private String user_name;
    private String user_avatar;
    private int user_level;
    private String user_email;
    private String user_phone;
    private String user_birth;

    private int content_user_id;
    private String content;
    private int story_id;
    private String story_user_name;
    private String story_user_avatar;
    private String story_user_created_at;

    private Boolean like_checked;
    private EditText et_story_detail_write_comment;
    private Button btn_story_detail_send_comment;

    //first like check
    private Boolean first_checked = false;


    private ImageView iv_item_story_avatar;
    private TextView tv_item_story_name;
    private TextView tv_item_story_date;
    private TextView tv_story_detail_content;
    private TextView tv_story_detail_like_count;
    private TextView tv_story_detail_comment_count;
    private CheckBox btn_item_main_story_detail_like;

    //recycler
    private RecyclerView rv_story_comments;
    private RecyclerView rv_content_detail;

    private ArrayList<Photo> photoList;
    private ArrayList<CommentInfo> commentInfoList;

    //adapter
    private PhotoAdapter photoAdapter;
    private CommentAdapter commentAdapter;

    //toolbar
    private DrawerLayout dl;
    private StoryServerConnection storyServerConnection;

    //request code
    private static final int COMMENT_EDIT_REQUEST = 1;
    private static final int DETAIL_REQUEST = 2;
    private static final int POPUP_REQUEST = 3;

    //result code
    private static final int DELETE = 5;



    private static final Logger logger = LoggerFactory.getLogger(StoryDetailActivity.class);
    private SharedPreferences loginInfo;
    private String access_token;

    private ImageView iv_item_story_menu;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);

        setUserInfo();


        content_user_id = getIntent().getIntExtra("content_user_id", 0);
        content = getIntent().getStringExtra("content");
        story_id = getIntent().getIntExtra("story_id", 0);
        story_user_name = getIntent().getStringExtra("story_user_name");
        story_user_avatar = getIntent().getStringExtra("story_user_avatar");
        story_user_created_at = getIntent().getStringExtra("story_user_created_at");
        like_checked = getIntent().getBooleanExtra("like_checked", false);
        position = getIntent().getIntExtra("position", 0);
        finishList.add(this);

        init();
        getContentData();
        getCommentCount();
        getCommentData();
        setCommentData();

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
        setToolbar(this.getWindow().getDecorView(), this.getApplicationContext(), "");
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

                Intent intent = new Intent(StoryDetailActivity.this, UserActivity.class);
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
                        intent = new Intent(StoryDetailActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        break;
                    case R.id.menu_market:
                        intent = new Intent(StoryDetailActivity.this, MarketMainActivity.class);
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

                        intent = new Intent(StoryDetailActivity.this, LoginActivity.class);
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
        intent.putExtra("content", content);
        intent.putExtra("position", position);
        intent.putExtra("like_checked", like_checked);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void setCommentData() {
        et_story_detail_write_comment = (EditText) findViewById(R.id.et_story_detail_write_comment);
        btn_story_detail_send_comment = (Button) findViewById(R.id.btn_story_detail_send_comment);

        btn_story_detail_send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String content = et_story_detail_write_comment.getText().toString();
                if (content.length() != 0) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("user_id", String.valueOf(user_id));
                    map.put("content", content);

//                    storyServerConnection = StoryServerConnection.retrofit.create(StoryServerConnection.class);
                    storyServerConnection = new HeaderInterceptor(access_token).getClientForStoryServer().create(StoryServerConnection.class);
                    Call<Comment> call_insert_comment = storyServerConnection.insert_comment(story_id, map);
                    call_insert_comment.enqueue(new Callback<Comment>() {
                        @Override
                        public void onResponse(Call<Comment> call, Response<Comment> response) {
                            if (response.isSuccessful()) {
                                int comment_id = response.body().getId();
                                String created_at = response.body().getCreated_at();
                                commentInfoList.add(new CommentInfo(comment_id, user_id, user_name, user_avatar, content, created_at));
                                commentAdapter.notifyItemInserted(commentInfoList.size() - 1);
                                getCommentCount();

                                et_story_detail_write_comment.setText("");
                                NestedScrollView nsv = (NestedScrollView) findViewById(R.id.ns_story_detail);
                                nsv.fullScroll(NestedScrollView.FOCUS_DOWN);
                            } else {
                                Toast.makeText(StoryDetailActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Comment> call, Throwable t) {
                            log(t);
                            Toast.makeText(StoryDetailActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        });
    }

    private void getCommentCount() {
        tv_story_detail_comment_count = (TextView) findViewById(R.id.tv_story_detail_comment_count);

        storyServerConnection = new HeaderInterceptor(access_token).getClientForStoryServer().create(StoryServerConnection.class);
        Call<Integer> call_comment_count = storyServerConnection.family_comment_Count(story_id);
        call_comment_count.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int comment_count = response.body();
                    tv_story_detail_comment_count.setText(String.valueOf(comment_count));
                } else {
                    Toast.makeText(StoryDetailActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                log(t);
                Toast.makeText(StoryDetailActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getCommentData() {
        rv_story_comments = (RecyclerView) findViewById(R.id.rv_story_comments);
        //comment count

        storyServerConnection = new HeaderInterceptor(access_token).getClientForStoryServer().create(StoryServerConnection.class);
        Call<ArrayList<CommentInfo>> call_family = storyServerConnection.family_detail_comment_List(story_id);
        call_family.enqueue(new Callback<ArrayList<CommentInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<CommentInfo>> call, Response<ArrayList<CommentInfo>> response) {
                if (response.isSuccessful()) {
                    commentInfoList = response.body();
                    commentAdapter = new CommentAdapter(StoryDetailActivity.this, commentInfoList, R.layout.item_comment);
                    rv_story_comments.setLayoutManager(new LinearLayoutManager(StoryDetailActivity.this, LinearLayoutManager.VERTICAL, false));
                    rv_story_comments.setAdapter(commentAdapter);
                    int spacing = getResources().getDimensionPixelSize(R.dimen.comment_divider_height);
                    rv_story_comments.addItemDecoration(new StoryDetailActivity.SpaceItemDecoration(spacing));
                } else {
                    Toast.makeText(StoryDetailActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CommentInfo>> call, Throwable t) {
                log(t);
                Toast.makeText(StoryDetailActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private class CommentViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout ll_comment;
        private ImageView iv_item_comment_avatar;
        private TextView tv_item_comment_name;
        private TextView tv_item_comment_content;
        private TextView tv_item_comment_date;

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
                        Intent intent = new Intent(StoryDetailActivity.this, CommentPopupActivity.class);
                        intent.putExtra("comment_id", commentInfoList.get(position).getComment_id());
                        intent.putExtra("comment_content", commentInfoList.get(position).getContent());
                        intent.putExtra("position", position);
                        intent.putExtra("act_flag", 1);
                        startActivityForResult(intent, COMMENT_EDIT_REQUEST);
                    } else {
                        Intent intent = new Intent(StoryDetailActivity.this, CommentPopupActivity.class);
                        intent.putExtra("comment_id", commentInfoList.get(position).getComment_id());
                        intent.putExtra("comment_user_name", commentInfoList.get(position).getUser_name());
                        intent.putExtra("comment_content", commentInfoList.get(position).getContent());
                        intent.putExtra("act_flag", 4);
                        intent.putExtra("comment_category_id", 1);
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

    private void getContentData() {
        rv_content_detail = (RecyclerView) findViewById(R.id.rv_content_detail);
        photoList = new ArrayList<>();

        storyServerConnection = new HeaderInterceptor(access_token).getClientForStoryServer().create(StoryServerConnection.class);
        Call<ArrayList<Photo>> call_photo = storyServerConnection.family_content_photo_List(story_id);
        call_photo.enqueue(new Callback<ArrayList<Photo>>() {
            @Override
            public void onResponse(Call<ArrayList<Photo>> call, Response<ArrayList<Photo>> response) {
                if (response.isSuccessful()) {
                    photoList = response.body();
                    if (photoList.size() == 0) {
                        rv_content_detail.setVisibility(View.GONE);
                    } else {
                        photoAdapter = new PhotoAdapter(StoryDetailActivity.this, photoList, R.layout.item_detail_photo);
                        rv_content_detail.setAdapter(photoAdapter);
                        rv_content_detail.setLayoutManager(new LinearLayoutManager(StoryDetailActivity.this, LinearLayoutManager.VERTICAL, false));
                    }
                } else {
                    Toast.makeText(StoryDetailActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Photo>> call, Throwable t) {
                log(t);
                Toast.makeText(StoryDetailActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
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
        private ArrayList<Photo> photoList;
        private int layout;

        public PhotoAdapter(Context context, ArrayList<Photo> photoList, int layout) {
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
            Glide.with(context).load(getString(R.string.cloud_front_stories_images) + photoList.get(position).getName() + "." + photoList.get(position).getExt()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_item_detail_photo);
            holder.iv_item_detail_photo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), PhotoPopupActivity.class);
                    intent.putExtra("from", "StoryDetailActivity");
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

    private void init() {
        //user_info
        iv_item_story_avatar = (ImageView) findViewById(R.id.iv_item_story_avatar);
        Glide.with(this).load(getString(R.string.cloud_front_user_avatar) + story_user_avatar).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_item_story_avatar);
        tv_item_story_name = (TextView) findViewById(R.id.tv_item_story_name);
        tv_item_story_name.setText(story_user_name);
        tv_item_story_date = (TextView) findViewById(R.id.tv_item_story_date);
        tv_item_story_date.setText(calculateTime(story_user_created_at));

        //menu
        iv_item_story_menu = (ImageView) findViewById(R.id.iv_item_story_menu);
        iv_item_story_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), StoryPopupActivity.class);
                intent.putExtra("story_id", story_id);
                intent.putExtra("content", content);
                intent.putExtra("photoList", photoList);
                intent.putExtra("content_user_id", content_user_id);
                intent.putExtra("position", position);
                startActivityForResult(intent, POPUP_REQUEST);
            }
        });

        //content
        tv_story_detail_content = (TextView) findViewById(R.id.tv_story_detail_content);
        tv_story_detail_content.setText(content);

        //like count
        tv_story_detail_like_count = (TextView) findViewById(R.id.tv_story_detail_like_count);

        storyServerConnection = new HeaderInterceptor(access_token).getClientForStoryServer().create(StoryServerConnection.class);
        Call<Integer> call_like_count = storyServerConnection.family_like_Count(story_id);
        call_like_count.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int like_count = response.body();
                    tv_story_detail_like_count.setText(String.valueOf(like_count));
                } else {
                    Toast.makeText(StoryDetailActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                log(t);
                Toast.makeText(StoryDetailActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });

        //like
        btn_item_main_story_detail_like = (CheckBox) findViewById(R.id.btn_item_main_story_detail_like);
        if (like_checked) {
            btn_item_main_story_detail_like.setChecked(true);
            first_checked = !first_checked;
        } else {
            btn_item_main_story_detail_like.setChecked(false);
            first_checked = !first_checked;
        }

        btn_item_main_story_detail_like.setOnCheckedChangeListener(this);
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

    //like check
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (first_checked) {
            if (isChecked) {
                storyServerConnection = new HeaderInterceptor(access_token).getClientForStoryServer().create(StoryServerConnection.class);
                HashMap<String, String> map = new HashMap<>();
                map.put("user_id", String.valueOf(user_id));

                Call<ResponseBody> call_like = storyServerConnection.family_content_like_up(story_id, map);
                call_like.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            tv_story_detail_like_count.setText(String.valueOf(Integer.parseInt(tv_story_detail_like_count.getText().toString()) + 1));
                            like_checked = !like_checked;
                        } else {
                            Toast.makeText(StoryDetailActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        log(t);
                        Toast.makeText(StoryDetailActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                    }
                });

            } else {
                storyServerConnection = new HeaderInterceptor(access_token).getClientForStoryServer().create(StoryServerConnection.class);
                Call<ResponseBody> call_dislike = storyServerConnection.family_content_like_down(story_id, user_id);
                call_dislike.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            tv_story_detail_like_count.setText(String.valueOf(Integer.parseInt(tv_story_detail_like_count.getText().toString()) - 1));
                            like_checked = !like_checked;
                        } else {
                            Toast.makeText(StoryDetailActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        log(t);
                        Toast.makeText(StoryDetailActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == POPUP_REQUEST) {
            if (resultCode == RESULT_OK) {
                String content = data.getStringExtra("content");
                Intent intent = getIntent();
                intent.putExtra("position", position);
                intent.putExtra("content", content);
                setResult(RESULT_OK, intent);
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
            int flag = data.getIntExtra("flag", 0);

            if (resultCode == RESULT_OK) {

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

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        intent.putExtra("position", position);
        intent.putExtra("like_checked", like_checked);
        intent.putExtra("content", content);
        setResult(Activity.RESULT_OK, intent);

        finish();
        super.onBackPressed();
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
