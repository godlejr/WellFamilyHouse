//package com.demand.well_family.well_family.memory_sound;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.media.MediaPlayer;
//import android.os.Bundle;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.widget.NestedScrollView;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.SeekBar;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.demand.well_family.well_family.R;
//import com.demand.well_family.well_family.connection.Server_Connector;
//import com.demand.well_family.well_family.dto.CommentInfo;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//
///**
// * Created by ㅇㅇ on 2017-01-31.
// */
//
//public class SoundPlayer extends Activity implements CompoundButton.OnCheckedChangeListener {
//    //    private SeekBarThread seekBarThread;
//    private String cloud_songs;
//
//    private MediaPlayer mp;
//    private SeekBar sb_sound;
//    private Boolean isPlaying = false, isPaused = false;
//    private int pausePos, endMinute, endSecond;
//    private CheckBox cb_sound_player_like;
//    private TextView tv_sound_player_start, tv_sound_player_end;
//    private RecyclerView rv_sound_player_comment;
//    private LinearLayout ll_sound_player_info;
//
//    private ImageView iv_sound_player_album, iv_sound_player_record, iv_sound_player_start, iv_sound_player_close;
//    private TextView tv_sound_player_title;
//    private TextView tv_sound_player_singer;
//
//    private TextView tv_sound_comment_count, tv_sound_like_count;
//
//    private EditText et_sound_comment;
//
//
//    //user_info
//    private String user_id;
//    private String user_name;
//    private String user_level;
//    private String user_avatar;
//    private String user_email;
//    private String user_phone;
//    private String user_birth;
//
//    //song info
//    private String song_id;
//    private String song_name;
//    private String song_ext;
//    private String song_title;
//    private String song_singer;
//    private String song_avatar;
//    private String song_category_id;
//    private String song_created_at;
//
//    private Server_Connector connector;
//
//    //first like check
//    private Boolean first_checked = false;
//    private ArrayList<CommentInfo> commentInfoList;
//    private Button btn_sound_comment_send;
//    private CommentAdapter commentAdapter;
//    private ImageView iv_sound_player_album_bg;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.sound_activity_player);
//        getWindow().setLayout(android.view.WindowManager.LayoutParams.MATCH_PARENT, android.view.WindowManager.LayoutParams.MATCH_PARENT);
//
//        user_id = getIntent().getStringExtra("user_id");
//        user_name = getIntent().getStringExtra("user_name");
//        user_level = getIntent().getStringExtra("user_level");
//        user_avatar = getIntent().getStringExtra("user_avatar");
//        user_email = getIntent().getStringExtra("user_email");
//        user_phone = getIntent().getStringExtra("user_phone");
//        user_birth = getIntent().getStringExtra("user_birth");
//
//
//        song_id = getIntent().getStringExtra("song_id");
//        song_name = getIntent().getStringExtra("song_name");
//        song_ext = getIntent().getStringExtra("song_ext");
//        song_title = getIntent().getStringExtra("song_title");
//        song_singer = getIntent().getStringExtra("song_singer");
//        song_avatar = getIntent().getStringExtra("song_avatar");
//        song_category_id = getIntent().getStringExtra("song_category_id");
//        song_created_at = getIntent().getStringExtra("song_created_at");
//
//        tv_sound_like_count = (TextView) findViewById(R.id.tv_sound_like_count);
//        tv_sound_comment_count = (TextView) findViewById(R.id.tv_sound_comment_count);
//
//        init();
//        player();
//        getCommentData();
//        getCommentCount();
//        setCommentData();
//    }
//
//    private void setCommentData() {
//        et_sound_comment = (EditText) findViewById(R.id.et_sound_comment);
//        btn_sound_comment_send = (Button) findViewById(R.id.btn_sound_comment_send);
//
//        btn_sound_comment_send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String content = et_sound_comment.getText().toString();
//                if (content.length() != 0) {
//                    connector = new Server_Connector();
//                    connector.addVariable("user_id", user_id);
//                    connector.addVariable("content", content);
//
//                    connector.execute(getString(R.string.server_url) + song_id + "/insert_song_comment");
//                    try {
//                        JSONArray arr = new JSONArray(connector.get().trim());
//                        if (arr.length() == 0) {
//                            //insert error
//                        } else {
//                            JSONObject obj = arr.getJSONObject(0);
//                            String comment_id = String.valueOf(obj.getInt("id"));
//                            String created_at = obj.getString("created_at");
//                            commentInfoList.add(new CommentInfo(comment_id, user_id, user_name, user_avatar, content, created_at));
//                            commentAdapter.notifyItemInserted(commentInfoList.size() - 1);
//                            getCommentCount(); // data refresh
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    et_sound_comment.setText("");
//
//                    NestedScrollView nsv = (NestedScrollView) findViewById(R.id.nsv_player);
//                    nsv.fullScroll(NestedScrollView.FOCUS_DOWN);
//                }
//            }
//        });
//    }
//
//    private void player() {
//        mp = new MediaPlayer();
//        try {
//            mp.setDataSource(getString(R.string.cloud_front_songs_file) + song_name + "." + song_ext);
//            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//                    sb_sound.setProgress(0); // 시작점
//                    sb_sound.setMax(mp.getDuration());
//
//                    endMinute = mp.getDuration() / 60000;
//                    endSecond = ((mp.getDuration() % 60000) / 1000) + 1;
//
//                    if(endSecond == 60){
//                        endMinute++;
//                        endSecond = 0;
//                    }
//
//                    tv_sound_player_end.setText(String.format("%02d:%02d", endMinute, endSecond));
//
//                    Log.e("ttttt_player", String.valueOf(mp.getDuration()));
//                    mp.start();
//
//                    new SeekBarThread().start();
//                    isPlaying = true;
//                }
//            });
//            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//                    Log.e("끝", "끝");
//                    isPlaying = false;
//                    iv_sound_player_start.setImageResource(R.drawable.play_player);
//                    sb_sound.setProgress(0);
//
//                    mp.pause();
//                    mp.stop();
//                    mp.reset();
//                }
//            });
//
//            mp.prepareAsync();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
////        sb_sound.setProgress(0); // 시작점
////        sb_sound.setMax(mp.getDuration());
////
////        endMinute = mp.getDuration() / 60000;
////        endSecond = ((mp.getDuration() % 60000) / 1000) + 1;
////        tv_sound_player_end.setText(String.format("%02d:%02d", endMinute, endSecond));
////
////        Log.e("ttttt_player", String.valueOf(mp.getDuration()));
////        mp.start();
//
////        new SeekBarThread().start();
////        isPlaying = true;
//
//        sb_sound.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                Log.e("ㅇㅇㅇ", mp + "");
//
//                mp.seekTo(pausePos);
//                mp.start();
//                iv_sound_player_start.setImageResource(R.drawable.pause_player);
//
//                new SeekBarThread().start();
//                Log.e("식바식바", "식바 " + seekBar.getProgress());
//            }
//
//            public void onStartTrackingTouch(SeekBar seekBar) {
//            }
//
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                int m = progress / 60000;
//                int s = ((progress % 60000) / 1000) + 1;
//
//                if(s == 60){
//                    m++;
//                    s = 0;
//                }
//                tv_sound_player_start.setText(String.format("%02d:%02d", m, s));
//
//                pausePos = seekBar.getProgress(); // 사용자가 움직여놓은 위치
//                Log.e("tt11", "식바  " + seekBar.getProgress());
//            }
//        });
//
//    }
//
//    @Override
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        if (first_checked) {
//            if (isChecked) {
//                connector = new Server_Connector();
//                connector.addVariable("user_id", user_id);
//                try {
//                    connector.execute(getString(R.string.server_url) + song_id + "/song_like_up");
//                    tv_sound_like_count.setText(String.valueOf(Integer.parseInt(tv_sound_like_count.getText().toString()) + 1));
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } else {
//                connector = new Server_Connector();
//                connector.addVariable("user_id", user_id);
//                try {
//                    connector.execute(getString(R.string.server_url) + song_id + "/song_like_down");
//                    tv_sound_like_count.setText(String.valueOf(Integer.parseInt(tv_sound_like_count.getText().toString()) - 1));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    class SeekBarThread extends Thread {
//        public void run() {
//            while (isPlaying) {
//                sb_sound.setProgress(mp.getCurrentPosition());
//            }
//        }
//    }
//
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_BACK:
//                isPlaying = false; // 쓰레드 정지
//                if (mp != null) {
//                    mp.release(); // 자원해제
//                }
//                finish();
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    private void init() {
//        //song info
//        ll_sound_player_info = (LinearLayout) findViewById(R.id.ll_sound_player_info);
//        ll_sound_player_info.bringToFront();
//        ll_sound_player_info.invalidate();
//        tv_sound_player_title = (TextView) findViewById(R.id.tv_sound_player_title);
//        tv_sound_player_title.setText(song_title);
//        tv_sound_player_singer = (TextView) findViewById(R.id.tv_sound_player_singer);
//        tv_sound_player_singer.setText(song_singer);
//
//        iv_sound_player_album = (ImageView) findViewById(R.id.iv_sound_player_album);
//        iv_sound_player_album_bg = (ImageView) findViewById(R.id.iv_sound_player_album_bg);
//
//        iv_sound_player_album_bg.setColorFilter(ContextCompat.getColor(this, R.color.tint));
//
//        Glide.with(this).load(getString(R.string.cloud_front_songs_avatar) + song_avatar).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_sound_player_album);
//        Glide.with(this).load(getString(R.string.cloud_front_songs_avatar) + song_avatar).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_sound_player_album_bg);
//
//
//        //like
//        connector = new Server_Connector();
//        connector.execute(getString(R.string.server_url) + song_id + "/song_like_Count");
//        try {
//            JSONArray arr = new JSONArray(connector.get().trim());
//            if (arr.length() != 0) {
//                for (int i = 0; i < arr.length(); i++) {
//                    JSONObject obj = arr.getJSONObject(i);
//                    String like_count = String.valueOf(obj.getInt("like_count"));
//                    tv_sound_like_count.setText(like_count);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        //like
//        cb_sound_player_like = (CheckBox) findViewById(R.id.cb_sound_player_like);
//        connector = new Server_Connector();
//        connector.addVariable("user_id", user_id);
//        connector.execute(getString(R.string.server_url) + song_id + "/song_like_check");
//        try {
//            JSONArray arr = new JSONArray(connector.get().trim());
//            if (arr.length() != 0) {
//                for (int i = 0; i < arr.length(); i++) {
//                    JSONObject obj = arr.getJSONObject(i);
//                    int checked = obj.getInt("checked");
//                    if (checked == 1) {
//                        cb_sound_player_like.setChecked(true);
//                        first_checked = true;
//                    } else {
//                        cb_sound_player_like.setChecked(false);
//                        first_checked = true;
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        cb_sound_player_like.setOnCheckedChangeListener(this);
//
//
//        sb_sound = (SeekBar) findViewById(R.id.sb_sound);
//        iv_sound_player_start = (ImageView) findViewById(R.id.iv_sound_player_start);
//        iv_sound_player_close = (ImageView) findViewById(R.id.iv_sound_player_close);
//        iv_sound_player_close.bringToFront();
//        tv_sound_player_end = (TextView) findViewById(R.id.tv_sound_player_end);
//        tv_sound_player_start = (TextView) findViewById(R.id.tv_sound_player_start);
//        iv_sound_player_record = (ImageView) findViewById(R.id.iv_sound_player_record);
//
//        iv_sound_player_record.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(v.getContext(), SoundRecordActivity.class);
//                intent.putExtra("user_id", user_id);
//                intent.putExtra("user_email", user_email);
//                intent.putExtra("user_birth", user_birth);
//                intent.putExtra("user_phone", user_phone);
//                intent.putExtra("user_name", user_name);
//                intent.putExtra("user_level", user_level);
//                intent.putExtra("user_avatar", user_avatar);
//
//                intent.putExtra("song_id", song_id);
//                intent.putExtra("song_title", song_title);
//                intent.putExtra("song_singer", song_singer);
//                intent.putExtra("song_avatar", song_avatar);
//                startActivity(intent);
//
//                isPlaying = false; // 쓰레드 정지
//                if (mp != null) {
//                    mp.release(); // 자원해제
//                }
//
//                finish();
//            }
//        });
//
//        iv_sound_player_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                isPlaying = false; // 쓰레드 정지
//                if (mp != null) {
//                    mp.release(); // 자원해제
//                }
//                finish();
//            }
//        });
//
//        iv_sound_player_start.bringToFront();
//        iv_sound_player_start.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mp != null) {
//                    if (isPlaying) {
//                        // 재생중 -> 일시정지
//                        pausePos = mp.getCurrentPosition();
//                        mp.pause();
//
//                        isPlaying = false;
//                        isPaused = true;
//
//                        iv_sound_player_start.setImageResource(R.drawable.play_player);
//                    } else {
//                        if (isPaused) {  // 일시정지 -> 재생
//                            mp.seekTo(pausePos);
//                            mp.start();
//                            new SeekBarThread().start();
//                        } else {
//                            mp.reset();
//                            player();
//                        }
//                        isPlaying = true;
//                        isPaused = false;
//                        iv_sound_player_start.setImageResource(R.drawable.pause_player);
//                    }
//                }
//            }
//        });
//
//    }
//
//    private void getCommentCount() {
//        connector = new Server_Connector();
//        connector.execute(getString(R.string.server_url) + song_id + "/song_comment_Count");
//        try {
//            JSONArray arr = new JSONArray(connector.get().trim());
//            if (arr.length() != 0) {
//                for (int i = 0; i < arr.length(); i++) {
//                    JSONObject obj = arr.getJSONObject(i);
//                    String like_count = String.valueOf(obj.getInt("comment_count"));
//                    tv_sound_comment_count.setText(like_count);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void getCommentData() {
//        rv_sound_player_comment = (RecyclerView) findViewById(R.id.rv_sound_player_comment);
//
//        commentInfoList = new ArrayList<>();
//        connector = new Server_Connector();
//        connector.execute(getString(R.string.server_url) + song_id + "/song_comment_List");
//        try {
//            JSONArray arr = new JSONArray(connector.get().trim());
//            int length = arr.length();
//            if (length == 0) {
////                rv_sound_player_comment.setVisibility(View.GONE);
//            } else {
//                for (int i = 0; i < length; i++) {
//                    JSONObject obj = arr.getJSONObject(i);
//                    String comment_id = String.valueOf(obj.getInt("comment_id"));
//                    String user_id = String.valueOf(obj.getInt("user_id"));
//                    String user_name = obj.getString("user_name");
//                    String avatar = obj.getString("avatar");
//                    String content = obj.getString("content");
//                    String created_at = obj.getString("created_at");
//                    commentInfoList.add(new CommentInfo(comment_id, user_id, user_name, avatar, content, created_at));
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        commentAdapter = new CommentAdapter(this, commentInfoList, R.layout.item_comment);
//        rv_sound_player_comment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        rv_sound_player_comment.setAdapter(commentAdapter);
//    }
//
//    private class CommentViewHolder extends RecyclerView.ViewHolder {
//        private ImageView iv_item_comment_avatar;
//        private TextView tv_item_comment_name;
//        private TextView tv_item_comment_content;
//        private TextView tv_item_comment_date;
//
//        public CommentViewHolder(View itemView) {
//            super(itemView);
//            iv_item_comment_avatar = (ImageView) itemView.findViewById(R.id.iv_item_comment_avatar);
//            tv_item_comment_name = (TextView) itemView.findViewById(R.id.tv_item_comment_name);
//            tv_item_comment_content = (TextView) itemView.findViewById(R.id.tv_item_comment_content);
//            tv_item_comment_date = (TextView) itemView.findViewById(R.id.tv_item_comment_date);
//
//        }
//
//    }
//
//    private class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {
//        private Context context;
//        private ArrayList<CommentInfo> commentInfoList;
//        private int layout;
//
//        public CommentAdapter(Context context, ArrayList<CommentInfo> commentInfoList, int layout) {
//            this.context = context;
//            this.commentInfoList = commentInfoList;
//            this.layout = layout;
//        }
//
//        @Override
//        public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            CommentViewHolder commentViewHolder = new CommentViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
//            return commentViewHolder;
//        }
//
//        @Override
//        public void onBindViewHolder(CommentViewHolder holder, int position) {
//            Glide.with(context).load(getString(R.string.cloud_front_user_avatar) + commentInfoList.get(position).getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_item_comment_avatar);
//            holder.tv_item_comment_name.setText(commentInfoList.get(position).getUser_name());
//            holder.tv_item_comment_content.setText(commentInfoList.get(position).getContent());
//            holder.tv_item_comment_date.setText(calculateTime(commentInfoList.get(position).getCreated_at()));
//
//            Log.e("tttt", calculateTime(commentInfoList.get(position).getCreated_at()));
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return commentInfoList.size();
//        }
//
//    }
//
//    public String calculateTime(String dateTime) {
//        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date = null;
//        try {
//            date = transFormat.parse(dateTime);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        long curTime = System.currentTimeMillis();
//        long regTime = date.getTime();
//        long diffTime = (curTime - regTime) / 1000;
//
//        String msg = null;
//
//        if (diffTime < 60) {
//            // sec
//            msg = diffTime + "초전";
//        } else if ((diffTime /= 60) < 60) {
//            // min
//            System.out.println(diffTime);
//
//            msg = diffTime + "분전";
//        } else if ((diffTime /= 60) < 24) {
//            // hour
//            msg = (diffTime) + "시간전";
//        } else if ((diffTime /= 24) < 7) {
//            // day
//            msg = (diffTime) + "일전";
//        } else {
//            SimpleDateFormat sdf = new SimpleDateFormat("yy.M.d aa h:mm");
//            msg = sdf.format(date);
//        }
//
//        return msg;
//    }
//
//    @Override
//    protected void onDestroy() {
//        if (mp != null) {
//            mp.release();
//            mp = null;
//        }
//        super.onDestroy();
//    }
//}
