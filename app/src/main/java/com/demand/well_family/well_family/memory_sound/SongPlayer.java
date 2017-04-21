package com.demand.well_family.well_family.memory_sound;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import com.demand.well_family.well_family.repository.SongServerConnection;
import com.demand.well_family.well_family.dialog.list.comment.activity.CommentDialogActivity;
import com.demand.well_family.well_family.dto.CommentInfo;
import com.demand.well_family.well_family.dto.SongComment;
import com.demand.well_family.well_family.repository.interceptor.HeaderInterceptor;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.util.ErrorUtil;

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
 * Created by ㅇㅇ on 2017-01-31.
 */

public class SongPlayer extends Activity implements CompoundButton.OnCheckedChangeListener {
    private MediaPlayer mp;
    private SeekBar sb_sound;
    private Boolean isPlaying = false;
    private Boolean isPaused = false;
    private int pausePos, endMinute, endSecond;
    private CheckBox cb_sound_player_like;
    private TextView tv_sound_player_start;
    private TextView tv_sound_player_end;
    private RecyclerView rv_sound_player_comment;
    private LinearLayout ll_sound_player_info;

    private ImageView iv_sound_player_album;
    private ImageView iv_sound_player_record;
    private ImageView iv_sound_player_start;
    private ImageView iv_sound_player_close;
    private TextView tv_sound_player_title;
    private TextView tv_sound_player_singer;

    private TextView tv_sound_comment_count;
    private TextView tv_sound_like_count;

    private EditText et_sound_comment;


    //user_info
    private int user_id;
    private String user_name;
    private int user_level;
    private String user_avatar;
    private String user_email;
    private String user_phone;
    private String user_birth;
    private String access_token;

    //song info
    private int song_id;
    private String song_name;
    private String song_ext;
    private String song_title;
    private String song_singer;
    private String song_avatar;
    private int song_category_id;
    private String song_created_at;

    private SongServerConnection songServerConnection;
    private static final int COMMENT_EDIT_REQUEST = 1;

    //first like check
    private Boolean first_checked = false;
    private ArrayList<CommentInfo> commentInfoList;
    private Button btn_sound_comment_send;
    private CommentAdapter commentAdapter;
    private ImageView iv_sound_player_album_bg;

    private static final Logger logger = LoggerFactory.getLogger(SongPlayer.class);
    private SharedPreferences loginInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.sound_activity_player);
        getWindow().setLayout(android.view.WindowManager.LayoutParams.MATCH_PARENT, android.view.WindowManager.LayoutParams.MATCH_PARENT);

        setUserInfo();

        song_id = getIntent().getIntExtra("song_id", 0);
        song_name = getIntent().getStringExtra("song_name");
        song_ext = getIntent().getStringExtra("song_ext");
        song_title = getIntent().getStringExtra("song_title");
        song_singer = getIntent().getStringExtra("song_singer");
        song_avatar = getIntent().getStringExtra("song_avatar");
        song_category_id = getIntent().getIntExtra("song_category_id", 0);
        song_created_at = getIntent().getStringExtra("song_created_at");

        tv_sound_like_count = (TextView) findViewById(R.id.tv_sound_like_count);
        tv_sound_comment_count = (TextView) findViewById(R.id.tv_sound_comment_count);

        init();
        player();
        getCommentData();
        getCommentCount();
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
    }

    private void setCommentData() {
        et_sound_comment = (EditText) findViewById(R.id.et_sound_comment);
        btn_sound_comment_send = (Button) findViewById(R.id.btn_sound_comment_send);

        btn_sound_comment_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String content = et_sound_comment.getText().toString();
                if (content.length() != 0) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("user_id", String.valueOf(user_id));
                    map.put("content", content);

                    songServerConnection = new HeaderInterceptor(access_token).getClientForSongServer().create(SongServerConnection.class);
                    Call<SongComment> call_insert_Song_comment = songServerConnection.insert_song_comment(song_id, map);
                    call_insert_Song_comment.enqueue(new Callback<SongComment>() {
                        @Override
                        public void onResponse(Call<SongComment> call, Response<SongComment> response) {
                            if (response.isSuccessful()) {
                                SongComment songComment = response.body();
                                int comment_id = songComment.getId();
                                String created_at = songComment.getCreated_at();

                                commentInfoList.add(new CommentInfo(comment_id, user_id, user_name, user_avatar, content, created_at));
                                commentAdapter.notifyItemInserted(commentInfoList.size() - 1);
                                getCommentCount();
                                et_sound_comment.setText("");
                                NestedScrollView nsv = (NestedScrollView) findViewById(R.id.nsv_player);
                                nsv.fullScroll(NestedScrollView.FOCUS_DOWN);
                            } else {
                                Toast.makeText(SongPlayer.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<SongComment> call, Throwable t) {
                            log(t);
                            Toast.makeText(SongPlayer.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    private void player() {
        mp = new MediaPlayer();
        try {
            mp.setDataSource(getString(R.string.cloud_front_songs_file) + song_name + "." + song_ext);
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(final MediaPlayer mp) {
                    sb_sound.setProgress(0);
                    sb_sound.setMax(mp.getDuration());

                    endMinute = mp.getDuration() / 60000;
                    endSecond = ((mp.getDuration() % 60000) / 1000) + 1;

                    if (endSecond == 60) {
                        endMinute++;
                        endSecond = 0;
                    }

                    tv_sound_player_end.setText(String.format("%02d:%02d", endMinute, endSecond));
                    mp.start();
                    new SeekBarThread().start();
                    isPlaying = true;

                    iv_sound_player_start.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mp != null) {
                                if (isPlaying) {
                                    // 재생중 -> 일시정지
                                    pausePos = mp.getCurrentPosition();
                                    mp.pause();

                                    isPlaying = false;
                                    isPaused = true;
                                    Glide.with(SongPlayer.this).load(R.drawable.song_story_play_player).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_sound_player_start);
                                } else {
                                    if (isPaused) {  // 일시정지 -> 재생
                                        mp.seekTo(pausePos);
                                        mp.start();
                                        new SeekBarThread().start();
                                    } else {
                                        mp.reset();
                                        player();
                                    }

                                    isPlaying = true;
                                    isPaused = false;
                                    Glide.with(SongPlayer.this).load(R.drawable.song_story_pause_player).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_sound_player_start);
                                }
                            }
                        }
                    });
                }
            });

            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    isPlaying = false;
                    Glide.with(SongPlayer.this).load(R.drawable.song_story_play_player).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_sound_player_start);
                    sb_sound.setProgress(0);

                    mp.pause();
                    mp.stop();
                    mp.reset();
                }
            });

            mp.prepareAsync();
        } catch (IOException e) {
            log(e);
        }

        sb_sound.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (isPlaying) {
                    mp.seekTo(pausePos);
                    mp.start();
                    Glide.with(SongPlayer.this).load(R.drawable.song_story_pause_player).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_sound_player_start);

                    new SeekBarThread().start();
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int m = progress / 60000;
                int s = ((progress % 60000) / 1000) + 1;

                if (s == 60) {
                    m++;
                    s = 0;
                }

                tv_sound_player_start.setText(String.format("%02d:%02d", m, s));
                pausePos = seekBar.getProgress();
            }
        });

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", String.valueOf(user_id));
        songServerConnection = new HeaderInterceptor(access_token).getClientForSongServer().create(SongServerConnection.class);
        if (first_checked) {
            if (isChecked) {
                Call<ResponseBody> call_song_like_up = songServerConnection.song_like_up(song_id, map);
                call_song_like_up.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            tv_sound_like_count.setText(String.valueOf(Integer.parseInt(tv_sound_like_count.getText().toString()) + 1));
                        } else {
                            Toast.makeText(SongPlayer.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        log(t);
                        Toast.makeText(SongPlayer.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                    }
                });

            } else {
                Call<ResponseBody> call_song_like_down = songServerConnection.song_like_down(song_id, user_id);
                call_song_like_down.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            tv_sound_like_count.setText(String.valueOf(Integer.parseInt(tv_sound_like_count.getText().toString()) - 1));
                        } else {
                            Toast.makeText(SongPlayer.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        log(t);
                        Toast.makeText(SongPlayer.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    class SeekBarThread extends Thread {
        public void run() {
            while (isPlaying) {
                sb_sound.setProgress(mp.getCurrentPosition());
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                isPlaying = false; // 쓰레드 정지
                if (mp != null) {
                    mp.release(); // 자원해제
                }
                finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void init() {
        //song info
        ll_sound_player_info = (LinearLayout) findViewById(R.id.ll_sound_player_info);
        ll_sound_player_info.bringToFront();
        ll_sound_player_info.invalidate();
        tv_sound_player_title = (TextView) findViewById(R.id.tv_sound_player_title);
        tv_sound_player_title.setText(song_title);
        tv_sound_player_singer = (TextView) findViewById(R.id.tv_sound_player_singer);
        tv_sound_player_singer.setText(song_singer);

        iv_sound_player_album = (ImageView) findViewById(R.id.iv_sound_player_album);
        iv_sound_player_album_bg = (ImageView) findViewById(R.id.iv_sound_player_album_bg);

        iv_sound_player_album_bg.setColorFilter(ContextCompat.getColor(this, R.color.tint));

        Glide.with(this).load(getString(R.string.cloud_front_songs_avatar) + song_avatar).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_sound_player_album);
        Glide.with(this).load(getString(R.string.cloud_front_songs_avatar) + song_avatar).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_sound_player_album_bg);

        //like
        songServerConnection = new HeaderInterceptor(access_token).getClientForSongServer().create(SongServerConnection.class);
        Call<Integer> call_song_like_count = songServerConnection.song_like_Count(song_id);
        call_song_like_count.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int like_count = response.body();
                    tv_sound_like_count.setText(String.valueOf(like_count));
                } else {
                    Toast.makeText(SongPlayer.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                log(t);
                Toast.makeText(SongPlayer.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });

        //like
        cb_sound_player_like = (CheckBox) findViewById(R.id.cb_sound_player_like);
        songServerConnection = new HeaderInterceptor(access_token).getClientForSongServer().create(SongServerConnection.class);

        Call<Integer> call_song_like_check = songServerConnection.song_like_check(song_id, user_id);
        call_song_like_check.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int checked = response.body();
                    if (checked == 1) {
                        cb_sound_player_like.setChecked(true);
                    } else {
                        cb_sound_player_like.setChecked(false);
                    }
                    first_checked = true;
                } else {
                    Toast.makeText(SongPlayer.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                log(t);
                Toast.makeText(SongPlayer.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });

        cb_sound_player_like.setOnCheckedChangeListener(this);

        sb_sound = (SeekBar) findViewById(R.id.sb_sound);
        iv_sound_player_start = (ImageView) findViewById(R.id.iv_sound_player_start);
        iv_sound_player_close = (ImageView) findViewById(R.id.iv_sound_player_close);
        iv_sound_player_close.bringToFront();
        tv_sound_player_end = (TextView) findViewById(R.id.tv_sound_player_end);
        tv_sound_player_start = (TextView) findViewById(R.id.tv_sound_player_start);
        iv_sound_player_record = (ImageView) findViewById(R.id.iv_sound_player_record);

        iv_sound_player_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SongRecordActivity.class);
                intent.putExtra("song_id", song_id);
                intent.putExtra("song_title", song_title);
                intent.putExtra("song_singer", song_singer);
                intent.putExtra("song_avatar", song_avatar);
                startActivity(intent);

                isPlaying = false; // 쓰레드 정지
                if (mp != null) {
                    mp.release(); // 자원해제
                }

                finish();
            }
        });

        iv_sound_player_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPlaying = false; // 쓰레드 정지
                if (mp != null) {
                    mp.release(); // 자원해제
                }
                finish();
            }
        });

        iv_sound_player_start.bringToFront();
    }

    private void getCommentCount() {
        songServerConnection = new HeaderInterceptor(access_token).getClientForSongServer().create(SongServerConnection.class);
        Call<Integer> call_song_comment_count = songServerConnection.song_comment_Count(song_id);
        call_song_comment_count.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    int comment_count = response.body();
                    tv_sound_comment_count.setText(String.valueOf(comment_count));
                } else {
                    Toast.makeText(SongPlayer.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                log(t);
                Toast.makeText(SongPlayer.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getCommentData() {
        rv_sound_player_comment = (RecyclerView) findViewById(R.id.rv_sound_player_comment);

        songServerConnection = new HeaderInterceptor(access_token).getClientForSongServer().create(SongServerConnection.class);
        Call<ArrayList<CommentInfo>> call_song_comment_List = songServerConnection.song_comment_List(song_id);
        call_song_comment_List.enqueue(new Callback<ArrayList<CommentInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<CommentInfo>> call, Response<ArrayList<CommentInfo>> response) {
                if (response.isSuccessful()) {
                    commentInfoList = response.body();
                    commentAdapter = new CommentAdapter(SongPlayer.this, commentInfoList, R.layout.item_comment);
                    rv_sound_player_comment.setLayoutManager(new LinearLayoutManager(SongPlayer.this, LinearLayoutManager.VERTICAL, false));
                    rv_sound_player_comment.setAdapter(commentAdapter);
                } else {
                    Toast.makeText(SongPlayer.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CommentInfo>> call, Throwable t) {
                log(t);
                Toast.makeText(SongPlayer.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
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
                        Intent intent = new Intent(SongPlayer.this, CommentDialogActivity.class);
                        intent.putExtra("comment_id", commentInfoList.get(position).getComment_id());
                        intent.putExtra("comment_content", commentInfoList.get(position).getContent());
                        intent.putExtra("position", position);
                        intent.putExtra("act_flag", 2);
                        startActivityForResult(intent, COMMENT_EDIT_REQUEST);
                    } else {
                        Intent intent = new Intent(SongPlayer.this, CommentDialogActivity.class);
                        intent.putExtra("comment_id", commentInfoList.get(position).getComment_id());
                        intent.putExtra("comment_user_name", commentInfoList.get(position).getUser_name());
                        intent.putExtra("comment_content", commentInfoList.get(position).getContent());
                        intent.putExtra("act_flag", 4);
                        intent.putExtra("comment_category_id", 2);
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

    @Override
    protected void onDestroy() {
        if (mp != null) {
            mp.release();
            mp = null;
        }
        super.onDestroy();
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
