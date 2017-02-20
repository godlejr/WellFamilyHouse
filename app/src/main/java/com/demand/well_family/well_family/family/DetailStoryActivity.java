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
import com.demand.well_family.well_family.connection.Server_Connection;
import com.demand.well_family.well_family.dto.Comment;
import com.demand.well_family.well_family.dto.CommentCount;
import com.demand.well_family.well_family.dto.CommentInfo;
import com.demand.well_family.well_family.dto.LikeCount;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.log.LogFlag;
import com.demand.well_family.well_family.market.MarketMainActivity;
import com.demand.well_family.well_family.memory_sound.SoundMainActivity;
import com.demand.well_family.well_family.users.UserActivity;

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

public class DetailStoryActivity extends Activity implements CompoundButton.OnCheckedChangeListener {
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
    private Server_Connection server_connection;

    private static final Logger logger = LoggerFactory.getLogger(DetailStoryActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);

        finishList.add(this);

        user_id = getIntent().getIntExtra("user_id", 0);
        user_name = getIntent().getStringExtra("user_name");
        user_level = getIntent().getIntExtra("user_level", 0);
        user_avatar = getIntent().getStringExtra("user_avatar");
        user_email = getIntent().getStringExtra("user_email");
        user_phone = getIntent().getStringExtra("user_phone");
        user_birth = getIntent().getStringExtra("user_birth");


        content_user_id = getIntent().getIntExtra("content_user_id", 0);
        content = getIntent().getStringExtra("content");
        story_id = getIntent().getIntExtra("story_id", 0);
        story_user_name = getIntent().getStringExtra("story_user_name");
        story_user_avatar = getIntent().getStringExtra("story_user_avatar");
        story_user_created_at = getIntent().getStringExtra("story_user_created_at");
        like_checked = getIntent().getBooleanExtra("like_checked", false);


        init();
        getContentData();
        getCommentCount();
        getCommentData();
        setCommentData();

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

                Intent intent = new Intent(DetailStoryActivity.this, UserActivity.class);
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
                        intent = new Intent(DetailStoryActivity.this, MainActivity.class);
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
                        intent = new Intent(DetailStoryActivity.this, MarketMainActivity.class);
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

                        intent = new Intent(DetailStoryActivity.this, LoginActivity.class);
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
        Intent intent = getIntent();
        intent.putExtra("position", getIntent().getIntExtra("position", 0));
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

                    server_connection = Server_Connection.retrofit.create(Server_Connection.class);
                    Call<ArrayList<Comment>> call_insert_comment = server_connection.insert_comment(story_id, map);
                    call_insert_comment.enqueue(new Callback<ArrayList<Comment>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Comment>> call, Response<ArrayList<Comment>> response) {

                            int comment_id = response.body().get(0).getId();
                            String created_at = response.body().get(0).getCreated_at();
                            commentInfoList.add(new CommentInfo(comment_id, user_id, user_name, user_avatar, content, created_at));
                            commentAdapter.notifyItemInserted(commentInfoList.size() - 1);
                            getCommentCount();

                            et_story_detail_write_comment.setText("");
                            NestedScrollView nsv = (NestedScrollView) findViewById(R.id.ns_story_detail);
                            nsv.fullScroll(NestedScrollView.FOCUS_DOWN);
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Comment>> call, Throwable t) {
                            log(t);
                            Toast.makeText(DetailStoryActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        });
    }

    private void getCommentCount() {
        tv_story_detail_comment_count = (TextView) findViewById(R.id.tv_story_detail_comment_count);

        server_connection = Server_Connection.retrofit.create(Server_Connection.class);
        Call<ArrayList<CommentCount>> call_comment_count = server_connection.family_comment_Count(story_id);
        call_comment_count.enqueue(new Callback<ArrayList<CommentCount>>() {
            @Override
            public void onResponse(Call<ArrayList<CommentCount>> call, Response<ArrayList<CommentCount>> response) {
                int comment_count = response.body().get(0).getComment_count();
                tv_story_detail_comment_count.setText(String.valueOf(comment_count));
            }

            @Override
            public void onFailure(Call<ArrayList<CommentCount>> call, Throwable t) {
                log(t);
                Toast.makeText(DetailStoryActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getCommentData() {
        rv_story_comments = (RecyclerView) findViewById(R.id.rv_story_comments);
        //comment count

        server_connection = Server_Connection.retrofit.create(Server_Connection.class);
        Call<ArrayList<CommentInfo>> call_family = server_connection.family_detail_comment_List(story_id);
        call_family.enqueue(new Callback<ArrayList<CommentInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<CommentInfo>> call, Response<ArrayList<CommentInfo>> response) {
                commentInfoList = response.body();
                commentAdapter = new CommentAdapter(DetailStoryActivity.this, commentInfoList, R.layout.item_comment);
                rv_story_comments.setLayoutManager(new LinearLayoutManager(DetailStoryActivity.this, LinearLayoutManager.VERTICAL, false));
                rv_story_comments.setAdapter(commentAdapter);
                int spacing = getResources().getDimensionPixelSize(R.dimen.comment_divider_height);
                rv_story_comments.addItemDecoration(new DetailStoryActivity.SpaceItemDecoration(spacing));
            }

            @Override
            public void onFailure(Call<ArrayList<CommentInfo>> call, Throwable t) {
                log(t);
                Toast.makeText(DetailStoryActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private class CommentViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_item_comment_avatar;
        private TextView tv_item_comment_name;
        private TextView tv_item_comment_content;
        private TextView tv_item_comment_date;

        public CommentViewHolder(View itemView) {
            super(itemView);
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
        public void onBindViewHolder(CommentViewHolder holder, int position) {
            Glide.with(context).load(getString(R.string.cloud_front_user_avatar) + commentInfoList.get(position).getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_item_comment_avatar);

            holder.tv_item_comment_name.setText(commentInfoList.get(position).getUser_name());
            holder.tv_item_comment_content.setText(commentInfoList.get(position).getContent());
            holder.tv_item_comment_date.setText(calculateTime(commentInfoList.get(position).getCreated_at()));
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

        server_connection = Server_Connection.retrofit.create(Server_Connection.class);
        Call<ArrayList<Photo>> call_photo = server_connection.family_content_photo_List(story_id);
        call_photo.enqueue(new Callback<ArrayList<Photo>>() {
            @Override
            public void onResponse(Call<ArrayList<Photo>> call, Response<ArrayList<Photo>> response) {
                photoList = response.body();
                if (photoList.size() == 0) {
                    rv_content_detail.setVisibility(View.GONE);
                } else {
                    photoAdapter = new PhotoAdapter(DetailStoryActivity.this, photoList, R.layout.item_detail_photo);
                    rv_content_detail.setAdapter(photoAdapter);
                    rv_content_detail.setLayoutManager(new LinearLayoutManager(DetailStoryActivity.this, LinearLayoutManager.VERTICAL, false));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Photo>> call, Throwable t) {
                log(t);
                Toast.makeText(DetailStoryActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
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
        public void onBindViewHolder(PhotoViewHolder holder, int position) {
            Glide.with(context).load(getString(R.string.cloud_front_stories_images) + photoList.get(position).getName() + "." + photoList.get(position).getExt()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_item_detail_photo);
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

        //content
        tv_story_detail_content = (TextView) findViewById(R.id.tv_story_detail_content);
        tv_story_detail_content.setText(content);

        //like count
        tv_story_detail_like_count = (TextView) findViewById(R.id.tv_story_detail_like_count);

        server_connection = Server_Connection.retrofit.create(Server_Connection.class);
        Call<ArrayList<LikeCount>> call_like_count = server_connection.family_like_Count(story_id);
        call_like_count.enqueue(new Callback<ArrayList<LikeCount>>() {
            @Override
            public void onResponse(Call<ArrayList<LikeCount>> call, Response<ArrayList<LikeCount>> response) {
                int like_count = response.body().get(0).getLike_count();
                tv_story_detail_like_count.setText(String.valueOf(like_count));
            }
            @Override
            public void onFailure(Call<ArrayList<LikeCount>> call, Throwable t) {
                log(t);
                Toast.makeText(DetailStoryActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
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
                server_connection = Server_Connection.retrofit.create(Server_Connection.class);
                HashMap<String, String> map = new HashMap<>();
                map.put("user_id", String.valueOf(user_id));

                Call<ResponseBody> call_like = server_connection.family_content_like_up(story_id, map);
                call_like.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        tv_story_detail_like_count.setText(String.valueOf(Integer.parseInt(tv_story_detail_like_count.getText().toString()) + 1));
                        like_checked = !like_checked;
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        log(t);
                        Toast.makeText(DetailStoryActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                    }
                });

            } else {
                server_connection = Server_Connection.retrofit.create(Server_Connection.class);
                HashMap<String, String> map = new HashMap<>();
                map.put("user_id", String.valueOf(user_id));

                Call<ResponseBody> call_dislike = server_connection.family_content_like_down(story_id, map);
                call_dislike.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        tv_story_detail_like_count.setText(String.valueOf(Integer.parseInt(tv_story_detail_like_count.getText().toString()) - 1));
                        like_checked = !like_checked;
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        log(t);
                        Toast.makeText(DetailStoryActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        intent.putExtra("position", getIntent().getIntExtra("position", 0));
        intent.putExtra("like_checked", like_checked);
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
