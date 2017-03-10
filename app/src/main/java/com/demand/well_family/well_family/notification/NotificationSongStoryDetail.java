package com.demand.well_family.well_family.notification;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.connection.Server_Connection;
import com.demand.well_family.well_family.dialog.CommentPopupActivity;
import com.demand.well_family.well_family.dto.Comment;
import com.demand.well_family.well_family.dto.CommentCount;
import com.demand.well_family.well_family.dto.CommentInfo;
import com.demand.well_family.well_family.dto.LikeCount;
import com.demand.well_family.well_family.dto.SongPhoto;
import com.demand.well_family.well_family.dto.SongStory;
import com.demand.well_family.well_family.dto.SongStoryAvatar;
import com.demand.well_family.well_family.dto.SongStoryComment;
import com.demand.well_family.well_family.dto.SongStoryEmotionData;
import com.demand.well_family.well_family.log.LogFlag;
import com.demand.well_family.well_family.memory_sound.EmotionActivity;
import com.demand.well_family.well_family.photos.SongPhotoPopupActivity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ㅇㅇ on 2017-03-07.
 */

public class NotificationSongStoryDetail extends Activity implements CompoundButton.OnCheckedChangeListener{
    //user_info
    private int user_id;
    private String user_name;
    private String user_avatar;
    private int user_level;
    private String user_email;
    private String user_phone;
    private String user_birth;
    private SharedPreferences loginInfo;

    private static final Logger logger = LoggerFactory.getLogger(NotificationSongStoryDetail.class);

    // story_user_info
    private ImageView iv_sound_story_detail_avatar;
    private TextView tv_sound_story_detail_writer_name;
    private TextView tv_sound_story_detail_date;

    // like
    private CheckBox cb_sound_story_detail_like;
    private TextView tv_sound_story_detail_like_count;
    private boolean like_checked;
    private boolean first_checked = false;

    //comment
    private TextView tv_sound_story_detail_comment_count;
    private static final int COMMENT_EDIT_REQUEST = 1;
    private CommentAdapter commentAdapter;
    private RecyclerView rv_sound_story_detail_comments;

    //share
    private ImageView iv_sound_story_detail_share;

    // content
    private TextView tv_sound_story_detail_location;
    private TextView tv_sound_story_detail_content;

    //photo
    private LinearLayout ll_sound_story_detail_image;
    private PhotoAdapter photoAdapter;
    private RecyclerView rv_sound_story_detail;
    private ArrayList<SongPhoto> photoList;

    // emotion
    private EmotionAdapter emotionAdapter;
    private RecyclerView rv_detail_emotion;

    //record
    private MediaPlayer mp;
    private int pausePos;
    private int endMinute;
    private int endSecond;
    private boolean isPlaying;
    private boolean isPaused;

    // song_info
    private ImageView iv_sound_detail_song_img;
    private TextView tv_sound_story_detail_title;
    private TextView tv_sound_story_detail_singer;
    private ImageView iv_sound_story_detail_play; // play button
    private SeekBar sb_sound_story_detail;
    private TextView tv_sound_story_detail_play;
    private TextView tv_sound_story_detail_end;

    // song_story_info
    private int song_story_id;
    private int range_id;
    private int song_id;
    private String song_title;
    private String song_singer;
    private String record_file;
    private String content;
    private String location;
    private int hits;
    private String created_at;
    private String updated_at;
    private Boolean isChecked; // like cheched

    // comment
    private EditText et_sound_story_comment;
    private Button btn_sound_story_comment_submit;
    private ArrayList<CommentInfo> commentInfoList;

    // toolbar
    private NavigationView nv;
    private DrawerLayout dl;


    private Server_Connection server_connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sound_acivity_story_detail);

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
    }
    private void init() {
        song_story_id = getIntent().getIntExtra("song_story_id", 179);

        tv_sound_story_detail_like_count = (TextView)findViewById(R.id.tv_sound_story_detail_like_count);
        tv_sound_story_detail_comment_count = (TextView)findViewById(R.id.tv_sound_story_detail_comment_count);
        cb_sound_story_detail_like = (CheckBox)findViewById(R.id.cb_sound_story_detail_like);
        iv_sound_detail_song_img = (ImageView)findViewById(R.id.iv_sound_detail_song_img);
        ll_sound_story_detail_image = (LinearLayout) findViewById(R.id.ll_sound_story_detail_image);

        // like
        if (like_checked) {
            cb_sound_story_detail_like.setChecked(true);
            first_checked = !first_checked;
        } else {
            cb_sound_story_detail_like.setChecked(false);
            first_checked = !first_checked;
        }
        cb_sound_story_detail_like.setOnCheckedChangeListener(this);

        //like_count
        server_connection = Server_Connection.retrofit.create(Server_Connection.class);
        Call<ArrayList<LikeCount>> call_like_count = server_connection.song_story_like_Count(song_story_id);
        call_like_count.enqueue(new Callback<ArrayList<LikeCount>>() {
            @Override
            public void onResponse(Call<ArrayList<LikeCount>> call, Response<ArrayList<LikeCount>> response) {
                int like_count = response.body().get(0).getLike_count();
                tv_sound_story_detail_like_count.setText(String.valueOf(like_count));
            }

            @Override
            public void onFailure(Call<ArrayList<LikeCount>> call, Throwable t) {
                log(t);
                Toast.makeText(NotificationSongStoryDetail.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });

        //comment
        getCommentCount();
        getCommentData();
        setCommentData();

        //emotion
        getEmotionData();

        //photo
        server_connection = Server_Connection.retrofit.create(Server_Connection.class);
        Call<ArrayList<SongPhoto>> call_song_photo = server_connection.song_story_photo_List(song_story_id);
        call_song_photo.enqueue(new Callback<ArrayList<SongPhoto>>() {
            @Override
            public void onResponse(Call<ArrayList<SongPhoto>> call, Response<ArrayList<SongPhoto>> response) {
                photoList = response.body();
                if (photoList.size() == 0) {
                    rv_sound_story_detail.setVisibility(View.GONE);
                    ll_sound_story_detail_image.setVisibility(View.GONE);
                } else {
                    photoAdapter = new PhotoAdapter(NotificationSongStoryDetail.this, photoList, R.layout.item_detail_photo);
                    rv_sound_story_detail.setAdapter(photoAdapter);
                    rv_sound_story_detail.setLayoutManager(new LinearLayoutManager(NotificationSongStoryDetail.this, LinearLayoutManager.VERTICAL, false));
                    rv_sound_story_detail.addItemDecoration(new SpaceItemDecoration(16));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SongPhoto>> call, Throwable t) {
                log(t);
                Toast.makeText(NotificationSongStoryDetail.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });

        // story_info
//        server_connection = Server_Connection.retrofit.create(Server_Connection.class);
//        Call<ArrayList<SongStory>> call_song_story = server_connection.song_story_info(song_story_id);
//        call_song_story.enqueue(new Callback<ArrayList<SongStory>>() {
//            @Override
//            public void onResponse(Call<ArrayList<SongStory>> call, Response<ArrayList<SongStory>> response) {
//                SongStory songStory = response.body().get(0);
//                song_id = songStory.getSong_id();
//                range_id = songStory.getRange_id();
//                song_title = songStory.getSong_title();
//                song_singer = songStory.getSong_singer();
//                record_file = songStory.getRecord_file();
//                content = songStory.getContent();
//                location = songStory.getLocation();
//                hits = songStory.getHits();
//                created_at = songStory.getCreated_at();
//                first_checked = songStory.getFirst_checked();
//                isChecked = songStory.getChecked();
//
//                //song_info
//                getSongAvatar();
//                tv_sound_story_detail_title.setText(song_title);
//                tv_sound_story_detail_singer.setText(song_singer);
//
//                //content, location
//                tv_sound_story_detail_location.setText(location);
//                tv_sound_story_detail_content.setText(content);
//                tv_sound_story_detail_date.setText(calculateTime(created_at));
//
//                //record
//                setPlayer();
//
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<SongStory>> call, Throwable t) {
//                log(t);
//                Toast.makeText(NotificationSongStoryDetail.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
//            }
//        });

    }

    // song
    private void getSongAvatar(){
        HashMap<String, String> map = new HashMap<>();
        map.put("song_id", String.valueOf(song_id));
        server_connection = Server_Connection.retrofit.create(Server_Connection.class);
        Call<ArrayList<SongStoryAvatar>> call_song_story_avatar = server_connection.song_story_avatar(song_story_id, map);
        call_song_story_avatar.enqueue(new Callback<ArrayList<SongStoryAvatar>>() {
            @Override
            public void onResponse(Call<ArrayList<SongStoryAvatar>> call, Response<ArrayList<SongStoryAvatar>> response) {
                String song_avatar = response.body().get(0).getAvatar();
                Glide.with(NotificationSongStoryDetail.this).load(getString(R.string.cloud_front_songs_avatar) + song_avatar)
                        .thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_sound_detail_song_img);
            }

            @Override
            public void onFailure(Call<ArrayList<SongStoryAvatar>> call, Throwable t) {
                log(t);
                Toast.makeText(NotificationSongStoryDetail.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });
    }

    //emotion
    private void getEmotionData() {
        rv_detail_emotion = (RecyclerView) findViewById(R.id.rv_detail_emotion);

        server_connection = Server_Connection.retrofit.create(Server_Connection.class);
        Call<ArrayList<SongStoryEmotionData>> call_emotion_data = server_connection.song_story_emotion_List(song_story_id);
        call_emotion_data.enqueue(new Callback<ArrayList<SongStoryEmotionData>>() {
            @Override
            public void onResponse(Call<ArrayList<SongStoryEmotionData>> call, Response<ArrayList<SongStoryEmotionData>> response) {
                //emotion
                emotionAdapter = new EmotionAdapter(response.body(), NotificationSongStoryDetail.this, R.layout.item_emotion);
                rv_detail_emotion.setAdapter(emotionAdapter);
                rv_detail_emotion.setLayoutManager(new GridLayoutManager(NotificationSongStoryDetail.this, 2));
            }

            @Override
            public void onFailure(Call<ArrayList<SongStoryEmotionData>> call, Throwable t) {
                log(t);
                Toast.makeText(NotificationSongStoryDetail.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });
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

    //comment
    private void setCommentData() {
        et_sound_story_comment = (EditText) findViewById(R.id.et_sound_story_comment);
        btn_sound_story_comment_submit = (Button) findViewById(R.id.btn_sound_story_comment_submit);

        btn_sound_story_comment_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String content = et_sound_story_comment.getText().toString();
                if (content.length() != 0) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("user_id", String.valueOf(user_id));
                    map.put("content", content);

                    server_connection = Server_Connection.retrofit.create(Server_Connection.class);
                    Call<ArrayList<SongStoryComment>> call_insert_comment = server_connection.insert_song_story_comment(song_story_id, map);
                    call_insert_comment.enqueue(new Callback<ArrayList<SongStoryComment>>() {
                        @Override
                        public void onResponse(Call<ArrayList<SongStoryComment>> call, Response<ArrayList<SongStoryComment>> response) {
                            int comment_id = response.body().get(0).getId();
                            String created_at = response.body().get(0).getCreated_at();
                            commentInfoList.add(new CommentInfo(comment_id, user_id, user_name, user_avatar, content, created_at));
                            commentAdapter.notifyItemInserted(commentInfoList.size() - 1);
                            getCommentCount();

                            et_sound_story_comment.setText("");

                            NestedScrollView nsv = (NestedScrollView) findViewById(R.id.nsv_sound_story_detail);
                            nsv.fullScroll(NestedScrollView.FOCUS_DOWN);
                        }

                        @Override
                        public void onFailure(Call<ArrayList<SongStoryComment>> call, Throwable t) {
                            log(t);
                            Toast.makeText(NotificationSongStoryDetail.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
    private void getCommentData() {
        rv_sound_story_detail_comments = (RecyclerView) findViewById(R.id.rv_sound_story_detail_comments);

        server_connection = Server_Connection.retrofit.create(Server_Connection.class);
        Call<ArrayList<CommentInfo>> call_family = server_connection.song_story_comment_List(song_story_id);
        call_family.enqueue(new Callback<ArrayList<CommentInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<CommentInfo>> call, Response<ArrayList<CommentInfo>> response) {
                commentInfoList = response.body();
                commentAdapter = new CommentAdapter(NotificationSongStoryDetail.this, commentInfoList, R.layout.item_comment);
                rv_sound_story_detail_comments.setAdapter(commentAdapter);
                rv_sound_story_detail_comments.setLayoutManager(new LinearLayoutManager(NotificationSongStoryDetail.this, LinearLayoutManager.VERTICAL, false));
            }

            @Override
            public void onFailure(Call<ArrayList<CommentInfo>> call, Throwable t) {
                log(t);
                Toast.makeText(NotificationSongStoryDetail.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });

    }
    private void getCommentCount(){
        server_connection = Server_Connection.retrofit.create(Server_Connection.class);
        Call<ArrayList<CommentCount>> call_comment_count = server_connection.song_story_comment_Count(song_story_id);
        call_comment_count.enqueue(new Callback<ArrayList<CommentCount>>() {
            @Override
            public void onResponse(Call<ArrayList<CommentCount>> call, Response<ArrayList<CommentCount>> response) {
                int comment_count = response.body().get(0).getComment_count();
                tv_sound_story_detail_comment_count.setText(String.valueOf(comment_count));
            }

            @Override
            public void onFailure(Call<ArrayList<CommentCount>> call, Throwable t) {
                log(t);
                Toast.makeText(NotificationSongStoryDetail.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
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
                        Intent intent = new Intent(NotificationSongStoryDetail.this, CommentPopupActivity.class);
                        intent.putExtra("comment_id", commentInfoList.get(position).getComment_id());
                        intent.putExtra("comment_content", commentInfoList.get(position).getContent());
                        intent.putExtra("position", position);
                        intent.putExtra("act_flag", 3);
                        startActivityForResult(intent, COMMENT_EDIT_REQUEST);
                    }else{
                        Intent intent = new Intent(NotificationSongStoryDetail.this, CommentPopupActivity.class);
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

    //record
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

                    Glide.with(NotificationSongStoryDetail.this).load(R.drawable.play).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_sound_story_detail_play);
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
                    Glide.with(NotificationSongStoryDetail.this).load(R.drawable.pause).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_sound_story_detail_play);
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

                        Glide.with(NotificationSongStoryDetail.this).load(R.drawable.play).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_sound_story_detail_play);

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

                                        Glide.with(NotificationSongStoryDetail.this).load(R.drawable.play).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_sound_story_detail_play);
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
                        Glide.with(NotificationSongStoryDetail.this).load(R.drawable.pause).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_sound_story_detail_play);
                    }
                }
            }
        });
    }

    //photo
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
                    //delete
                    int position = data.getIntExtra("position", -1);
                    commentAdapter.commentInfoList.remove(position);
                    commentAdapter.notifyItemRemoved(position);
                    commentAdapter.notifyItemRangeChanged(position,commentAdapter.getItemCount());

                }
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (first_checked) {
            if (isChecked) {

                server_connection = Server_Connection.retrofit.create(Server_Connection.class);
                HashMap<String, String> map = new HashMap<>();
                map.put("user_id", String.valueOf(user_id));

                Call<ResponseBody> call_like = server_connection.song_story_like_up(song_story_id, map);
                call_like.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        tv_sound_story_detail_like_count.setText(String.valueOf(Integer.parseInt(tv_sound_story_detail_like_count.getText().toString()) + 1));
                        like_checked = !like_checked;
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        log(t);
                        Toast.makeText(NotificationSongStoryDetail.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                server_connection = Server_Connection.retrofit.create(Server_Connection.class);
                HashMap<String, String> map = new HashMap<>();
                map.put("user_id", String.valueOf(user_id));

                Call<ResponseBody> call_dislike = server_connection.song_story_like_down(song_story_id, map);
                call_dislike.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        tv_sound_story_detail_like_count.setText(String.valueOf(Integer.parseInt(tv_sound_story_detail_like_count.getText().toString()) - 1));
                        like_checked = !like_checked;
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        log(t);
                        Toast.makeText(NotificationSongStoryDetail.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                    }
                });
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

    //etc
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
    class SeekBarThread extends Thread {
        public void run() {
            while (isPlaying) {
                sb_sound_story_detail.setProgress(mp.getCurrentPosition());
            }
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
