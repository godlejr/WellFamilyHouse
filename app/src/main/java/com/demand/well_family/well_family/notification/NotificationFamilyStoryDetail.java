package com.demand.well_family.well_family.notification;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
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
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.repository.StoryServerConnection;
import com.demand.well_family.well_family.dialog.list.comment.activity.CommentDialogActivity;
import com.demand.well_family.well_family.dto.Comment;
import com.demand.well_family.well_family.dto.CommentInfo;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.dto.StoryInfoForNotification;
import com.demand.well_family.well_family.story.StoryDetailActivity;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.interceptor.HeaderInterceptor;
import com.demand.well_family.well_family.dialog.popup.photo.activity.PhotoPopupActivity;
import com.demand.well_family.well_family.util.ErrorUtil;

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

import static com.demand.well_family.well_family.main.login.activity.LoginActivity.finishList;

/**
 * Created by ㅇㅇ on 2017-03-07.
 */

public class NotificationFamilyStoryDetail extends Activity implements CompoundButton.OnCheckedChangeListener {
    //user_info
    private int user_id;
    private String user_name;
    private String user_avatar;
    private int user_level;
    private String user_email;
    private String user_phone;
    private String user_birth;
    private String access_token;
    private SharedPreferences loginInfo;

    private static final Logger logger = LoggerFactory.getLogger(StoryDetailActivity.class);

    //comment
    private EditText et_story_detail_write_comment;
    private Button btn_story_detail_send_comment;

    //like check
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

    //content
    private ArrayList<Photo> photoList;
    private ArrayList<CommentInfo> commentInfoList;

    //adapter
    private PhotoAdapter photoAdapter;
    private CommentAdapter commentAdapter;

    //toolbar
    private DrawerLayout dl;

    private StoryServerConnection storyServerConnection;
    private static final int COMMENT_EDIT_REQUEST = 1;

    //story_info
    private int content_user_id;
    private String content;
    private int story_id;
    private String story_user_name;
    private String story_user_avatar;
    private String story_user_created_at;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);
        story_id = getIntent().getIntExtra("story_id", 0);
        setUserInfo();
        init();
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
        setToolbar(getWindow().getDecorView());
    }

    private void init() {
        iv_item_story_avatar = (ImageView) findViewById(R.id.iv_item_story_avatar);
        tv_item_story_name = (TextView) findViewById(R.id.tv_item_story_name);
        tv_item_story_date = (TextView) findViewById(R.id.tv_item_story_date);
        tv_story_detail_content = (TextView) findViewById(R.id.tv_story_detail_content);
        btn_item_main_story_detail_like = (CheckBox) findViewById(R.id.btn_item_main_story_detail_like);
        tv_story_detail_like_count = (TextView) findViewById(R.id.tv_story_detail_like_count);
        tv_story_detail_comment_count = (TextView) findViewById(R.id.tv_story_detail_comment_count);
        tv_story_detail_comment_count = (TextView) findViewById(R.id.tv_story_detail_comment_count);
        tv_story_detail_comment_count.setTextColor(Color.parseColor("#424242"));
        tv_story_detail_like_count.setTextColor(Color.parseColor("#424242"));
        finishList.add(this);

        // hit
        storyServerConnection = new HeaderInterceptor(access_token).getClientForStoryServer().create(StoryServerConnection.class);
        Call<Void> call_insert_story_hits = storyServerConnection.Insert_story_hit(story_id);
        call_insert_story_hits.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {

                } else {
                    Toast.makeText(NotificationFamilyStoryDetail.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                log(t);
                Toast.makeText(NotificationFamilyStoryDetail.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });


        storyServerConnection = new HeaderInterceptor(access_token).getClientForStoryServer().create(StoryServerConnection.class);
        Call<StoryInfoForNotification> call_story_info = storyServerConnection.storyDetailForNotification(story_id);
        call_story_info.enqueue(new Callback<StoryInfoForNotification>() {
            @Override
            public void onResponse(Call<StoryInfoForNotification> call, Response<StoryInfoForNotification> response) {
                if (response.isSuccessful()) {
                    StoryInfoForNotification storyInfoForNotification = response.body();

                    tv_item_story_name.setText(storyInfoForNotification.getUser_name());
                    tv_item_story_date.setText(calculateTime(storyInfoForNotification.getCreated_at()));
                    tv_story_detail_content.setText(storyInfoForNotification.getContent());

                    storyServerConnection = new HeaderInterceptor(access_token).getClientForStoryServer().create(StoryServerConnection.class);
                    Call<Integer> call_like_check = storyServerConnection.family_content_like_check(story_id, user_id);
                    call_like_check.enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            if (response.isSuccessful()) {
                                int checked = response.body();
                                if (checked == 1) {
                                    btn_item_main_story_detail_like.setChecked(true);
                                } else {
                                    btn_item_main_story_detail_like.setChecked(false);
                                }
                                first_checked = true;
                            } else {
                                Toast.makeText(NotificationFamilyStoryDetail.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
                            log(t);
                            Toast.makeText(NotificationFamilyStoryDetail.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                        }
                    });

                    Glide.with(NotificationFamilyStoryDetail.this).load(getString(R.string.cloud_front_user_avatar) + storyInfoForNotification.getUser_avatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_item_story_avatar);

                    getContentData();
                    getCommentCount();
                    getCommentData();
                    setCommentData();
                } else {
                    Toast.makeText(NotificationFamilyStoryDetail.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StoryInfoForNotification> call, Throwable t) {
                log(t);
                Toast.makeText(NotificationFamilyStoryDetail.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });
        btn_item_main_story_detail_like.setOnCheckedChangeListener(NotificationFamilyStoryDetail.this);

    }

    private void getCommentCount() {
        storyServerConnection = new HeaderInterceptor(access_token).getClientForStoryServer().create(StoryServerConnection.class);
        Call<Integer> call_comment_count = storyServerConnection.family_comment_Count(story_id);
        call_comment_count.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int comment_count = response.body();
                    tv_story_detail_comment_count.setText(String.valueOf(comment_count));
                } else {
                    Toast.makeText(NotificationFamilyStoryDetail.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                log(t);
                Toast.makeText(NotificationFamilyStoryDetail.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });
    }

    // comment
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
                                Toast.makeText(NotificationFamilyStoryDetail.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Comment> call, Throwable t) {
                            log(t);
                            Toast.makeText(NotificationFamilyStoryDetail.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                        }
                    });

                }
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
                    commentAdapter = new CommentAdapter(NotificationFamilyStoryDetail.this, commentInfoList, R.layout.item_comment);
                    rv_story_comments.setLayoutManager(new LinearLayoutManager(NotificationFamilyStoryDetail.this, LinearLayoutManager.VERTICAL, false));
                    rv_story_comments.setAdapter(commentAdapter);
                    int spacing = getResources().getDimensionPixelSize(R.dimen.comment_divider_height);
                    rv_story_comments.addItemDecoration(new SpaceItemDecoration(spacing));
                } else {
                    Toast.makeText(NotificationFamilyStoryDetail.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CommentInfo>> call, Throwable t) {
                log(t);
                Toast.makeText(NotificationFamilyStoryDetail.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setToolbar(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolBar);
        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText(" ");
        ImageView toolbar_back = (ImageView) toolbar.findViewById(R.id.toolbar_back);
        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // data
    private void getContentData() {
        rv_content_detail = (RecyclerView) findViewById(R.id.rv_content_detail);
        rv_story_comments = (RecyclerView) findViewById(R.id.rv_story_comments);

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
                        photoAdapter = new PhotoAdapter(NotificationFamilyStoryDetail.this, photoList, R.layout.item_detail_photo);
                        rv_content_detail.setAdapter(photoAdapter);
                        rv_content_detail.setLayoutManager(new LinearLayoutManager(NotificationFamilyStoryDetail.this, LinearLayoutManager.VERTICAL, false));
                    }
                } else {
                    Toast.makeText(NotificationFamilyStoryDetail.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Photo>> call, Throwable t) {
                log(t);
                Toast.makeText(NotificationFamilyStoryDetail.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });

        // comment
        storyServerConnection = new HeaderInterceptor(access_token).getClientForStoryServer().create(StoryServerConnection.class);
        Call<ArrayList<CommentInfo>> call_family = storyServerConnection.family_detail_comment_List(story_id);
        call_family.enqueue(new Callback<ArrayList<CommentInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<CommentInfo>> call, Response<ArrayList<CommentInfo>> response) {
                if (response.isSuccessful()) {
                    commentInfoList = response.body();
                    commentAdapter = new CommentAdapter(NotificationFamilyStoryDetail.this, commentInfoList, R.layout.item_comment);
                    rv_story_comments.setLayoutManager(new LinearLayoutManager(NotificationFamilyStoryDetail.this, LinearLayoutManager.VERTICAL, false));
                    rv_story_comments.setAdapter(commentAdapter);
                    int spacing = getResources().getDimensionPixelSize(R.dimen.comment_divider_height);
                    rv_story_comments.addItemDecoration(new SpaceItemDecoration(spacing));
                } else {
                    Toast.makeText(NotificationFamilyStoryDetail.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CommentInfo>> call, Throwable t) {
                log(t);
                Toast.makeText(NotificationFamilyStoryDetail.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });

        // comment_count
        storyServerConnection = new HeaderInterceptor(access_token).getClientForStoryServer().create(StoryServerConnection.class);
        Call<Integer> call_comment_count = storyServerConnection.family_comment_Count(story_id);
        call_comment_count.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int comment_count = response.body();
                    tv_story_detail_comment_count.setText(String.valueOf(comment_count));
                } else {
                    Toast.makeText(NotificationFamilyStoryDetail.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                log(t);
                Toast.makeText(NotificationFamilyStoryDetail.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });

        // like_count
        storyServerConnection = new HeaderInterceptor(access_token).getClientForStoryServer().create(StoryServerConnection.class);
        Call<Integer> call_like_count = storyServerConnection.family_like_Count(story_id);
        call_like_count.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int like_count = response.body();
                    tv_story_detail_like_count.setText(String.valueOf(like_count));
                } else {
                    Toast.makeText(NotificationFamilyStoryDetail.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                log(t);
                Toast.makeText(NotificationFamilyStoryDetail.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });
    }


    // content
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
                        Intent intent = new Intent(v.getContext(), CommentDialogActivity.class);
                        intent.putExtra("comment_id", commentInfoList.get(position).getComment_id());
                        intent.putExtra("comment_content", commentInfoList.get(position).getContent());
                        intent.putExtra("position", position);
                        intent.putExtra("act_flag", 1);
                        startActivityForResult(intent, COMMENT_EDIT_REQUEST);
                    } else {
                        Intent intent = new Intent(v.getContext(), CommentDialogActivity.class);
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

    // like check
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
                        } else {
                            Toast.makeText(NotificationFamilyStoryDetail.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        log(t);
                        Toast.makeText(NotificationFamilyStoryDetail.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                    }
                });

            } else {
                storyServerConnection = new HeaderInterceptor(access_token).getClientForStoryServer().create(StoryServerConnection.class);
                HashMap<String, String> map = new HashMap<>();
                map.put("user_id", String.valueOf(user_id));

                Call<ResponseBody> call_dislike = storyServerConnection.family_content_like_down(story_id, user_id);
                call_dislike.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            tv_story_detail_like_count.setText(String.valueOf(Integer.parseInt(tv_story_detail_like_count.getText().toString()) - 1));
                        } else {
                            Toast.makeText(NotificationFamilyStoryDetail.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        log(t);
                        Toast.makeText(NotificationFamilyStoryDetail.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
                    //photo_delete
                    int position = data.getIntExtra("position", -1);
                    commentAdapter.commentInfoList.remove(position);
                    commentAdapter.notifyItemRemoved(position);
                    commentAdapter.notifyItemRangeChanged(position, commentAdapter.getItemCount());

                }
            }
        }
    }


    // etc
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
