package com.demand.well_family.well_family.memory_sound;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.connection.SongServerConnection;
import com.demand.well_family.well_family.connection.SongStoryServerConnection;
import com.demand.well_family.well_family.interceptor.HeaderInterceptor;
import com.demand.well_family.well_family.dto.Range;
import com.demand.well_family.well_family.dto.SongStory;
import com.demand.well_family.well_family.dto.SongStoryEmotionInfo;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.util.ErrorUtil;
import com.demand.well_family.well_family.util.RealPathUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.demand.well_family.well_family.main.login.activity.LoginActivity.finishList;

/**
 * Created by ㅇㅇ on 2017-02-01.
 */

public class SongRecordActivity extends Activity {
    private TextView tv_record_song_title;
    private TextView tv_record_song_singer;
    private ImageView iv_record_user_avatar;
    private TextView tv_record_user_name;
    private EditText et_sound_record_memory;

    private Spinner sp_sound;
    private LinearLayout ll_sound_record_container;
    private LinearLayout ll_sound_record_location;
    private ImageView iv_sound_record;
    private ImageView iv_sound_record_complete_play;
    private ImageView iv_sound_record_complete_replay;
    private MediaRecorder recorder;
    private String fileName;
    private String dirName;
    private String path;
    private boolean isRecording = false;

    private TextView tv_sound_record;
    private TextView tv_sound_record_complete;
    private int timer_sec = 0;
    private int timer_min = 0;
    private TimerHandler timerHandler;
    private Timer timer;
    private SeekBar sb_sound_record;
    private File file = null;
    private boolean isPlaying = false;
    private boolean isPaused = false;
    private int pausePos;
    private MediaPlayer mp;

    private Button btn_sound_record_image_upload;
    private Button btn_sound_record_submit;
    private Button btn_sound_record_select_emotion;
    private RecyclerView rv_sound_record_image_upload_gallery;
    private ArrayList<Uri> photoList;
    private ArrayList<String> pathList;
    private Intent intent;
    private static final int PICK_PHOTO = 77;
    private PhotoViewAdapter photoViewAdapter;
    private EditText et_sound_record_location;
    private String location = null;
    private ArrayAdapter<String> spinnerAdapter;
    private ImageView iv_sound_record_location;
    private ImageView iv_sound_record_location_btn;

    private HashMap<Integer, String> spList;

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
    private String song_title;
    private String song_singer;
    private String song_avatar;

    //server
    private SongServerConnection songServerConnection;
    private SongStoryServerConnection songStoryServerConnection;
    private RealPathUtil realPathUtil;
    private ProgressDialog progressDialog;

    private int range_id;

    private static final int EMOTION_REQUEST = 2;
    private final int RECORD_EXTERNAL_STORAGE_PERMISSION = 10003;

    //toolbar
    private DrawerLayout dl;
    private ImageView iv_sound_record_avatar;

    // progress
    private int sleepTime;

    // emotion
    private ArrayList<SongStoryEmotionInfo> emotionList;
    private RecyclerView rv_record_emotion;
    private EmotionAdapter emotionAdapter;

    private static final Logger logger = LoggerFactory.getLogger(SongRecordActivity.class);
    private SharedPreferences loginInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sound_activity_record);

        setUserInfo();

        song_id = getIntent().getIntExtra("song_id", 0);
        song_title = getIntent().getStringExtra("song_title");
        song_singer = getIntent().getStringExtra("song_singer");
        song_avatar = getIntent().getStringExtra("song_avatar");

        finishList.add(this);

        checkPermission();
        init();
        setSpinner();
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
        setToolbar(this.getWindow().getDecorView(), this, "추억기록");
    }


    public void setToolbar(View view, Context context, String title) {
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
    }

    private void setBack() {
        finish();
    }

    private void setSpinner() {
        sp_sound = (Spinner) findViewById(R.id.sp_sound);

        spList = new HashMap<Integer, String>();

        songServerConnection = new HeaderInterceptor(access_token).getClientForSongServer().create(SongServerConnection.class);
        Call<ArrayList<Range>> call_song_range = songServerConnection.song_range_List();
        call_song_range.enqueue(new Callback<ArrayList<Range>>() {
            @Override
            public void onResponse(Call<ArrayList<Range>> call, Response<ArrayList<Range>> response) {
                if (response.isSuccessful()) {
                    ArrayList<Range> rangeList = response.body();
                    int rangeListSize = rangeList.size();

                    for (int i = 0; i < rangeListSize; i++) {
                        spList.put(rangeList.get(i).getId(), rangeList.get(i).getName());
                    }

                    String[] spinnerArray = new String[rangeListSize];
                    for (int i = 0; i < rangeListSize; i++) {
                        spinnerArray[i] = spList.get(i + 1);
                    }

                    spinnerAdapter = new ArrayAdapter<String>(SongRecordActivity.this, R.layout.custom_spinner_item, spinnerArray) {
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);
                            TextView tv = (TextView) view;
                            tv.setBackgroundColor(Color.WHITE);
                            tv.setTextColor(Color.parseColor("#424242"));
                            tv.append("  ▼");
                            return view;
                        }

                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            View view = super.getDropDownView(position, convertView, parent);
                            TextView tv = (TextView) view;
                            tv.setBackgroundColor(Color.WHITE);
                            tv.setTextColor(Color.parseColor("#424242"));
                            tv.setGravity(Gravity.CENTER);
                            return view;
                        }
                    };
                    spinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_item);

                    sp_sound.setAdapter(spinnerAdapter);
                    sp_sound.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            range_id = position + 1;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            range_id = 1;
                        }
                    });
                } else {
                    Toast.makeText(SongRecordActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Range>> call, Throwable t) {
                log(t);
                Toast.makeText(SongRecordActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void startRec() {
        GregorianCalendar cal = new GregorianCalendar();
        String current = cal.get(Calendar.YEAR) + "." + (cal.get(Calendar.MONTH) + 1) + "." + cal.get(Calendar.DAY_OF_MONTH) + "."
                + cal.get(Calendar.HOUR_OF_DAY) + "." + cal.get(Calendar.MINUTE) + "." + cal.get(Calendar.SECOND);
        fileName = "record(" + current + ").mp4";

        if (recorder == null) {
            recorder = new MediaRecorder();
        } else {
            recorder.reset();
        }

        try {
            path = dirName + fileName;

            recorder.setOutputFile(path);
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            recorder.prepare();
            recorder.start();
        } catch (Exception e) {
            log(e);
        }

        TimerTask timeTask = new TimerTask() {
            @Override
            public void run() {
                if (timer_min >= 5) {
                    timer_min = timer_sec = 0;
                    timerHandler.sendEmptyMessage(1);
                } else if (timer_sec < 59) {
                    timer_sec++;
                } else {
                    timer_min++;
                    timer_sec = 0;
                }

                timerHandler.sendEmptyMessage(0);
            }
        };

        timer = new Timer();
        timer.schedule(timeTask, 0, 1000);
    }

    class TimerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tv_sound_record.setText(timer_min + ":" + timer_sec);

            if (msg.what == 1) {
                Toast.makeText(SongRecordActivity.this, "최대 5분까지 녹음이 가능합니다.", Toast.LENGTH_SHORT).show();
                timer.cancel();
                stopRec();
            }
        }
    }

    class SeekBarThread extends Thread {
        public void run() {
            while (isPlaying) {
                sb_sound_record.setProgress(mp.getCurrentPosition());
            }
        }
    }

    public void stopRec() {
        try {
            recorder.stop();
            recorder.release();
            recorder = null;
            isRecording = false;
        } catch (Exception e) {
            log(e);
        }

        file = new File(path);
        Uri recordUri = Uri.fromFile(file);

        timer.cancel();
        timer_sec = timer_min = 0;

//       재생 ======= 다시녹음
        ll_sound_record_container.removeAllViews();
        getLayoutInflater().inflate(R.layout.sound_item_record2, ll_sound_record_container, true);
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);

        long timeInmillisec = Long.parseLong(time);
        long duration = timeInmillisec / 1000;
        long hours = duration / 3600;
        long minutes = (duration - hours * 3600) / 60;
        final long seconds = duration - (hours * 3600 + minutes * 60) + 1;

        tv_sound_record_complete = (TextView) ll_sound_record_container.findViewById(R.id.tv_sound_record_complete);

        iv_sound_record_complete_play = (ImageView) ll_sound_record_container.findViewById(R.id.iv_sound_record_complete_play);
        iv_sound_record_complete_replay = (ImageView) ll_sound_record_container.findViewById(R.id.iv_sound_record_complete_replay);

        tv_record_song_title = (TextView) ll_sound_record_container.findViewById(R.id.tv_record_song_title);
        tv_record_song_title.setText(song_title);
        tv_record_song_singer = (TextView) ll_sound_record_container.findViewById(R.id.tv_record_song_singer);
        tv_record_song_singer.setText(song_singer);

        iv_sound_record_avatar = (ImageView) ll_sound_record_container.findViewById(R.id.iv_sound_record_avatar);
        Glide.with(this).load(getString(R.string.cloud_front_songs_avatar) + song_avatar).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_sound_record_avatar);

        tv_sound_record_complete.setText(String.format(Locale.getDefault(), "%02d : %02d", minutes, seconds));

        sb_sound_record = (SeekBar) ll_sound_record_container.findViewById(R.id.sb_sound_record);
        sb_sound_record.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isPlaying = false;
                if (mp != null) {
                    mp.pause();
                }
            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {
                if (isPaused) {
                    pausePos = seekBar.getProgress();
                    mp.seekTo(pausePos);
                    mp.start();
                    seekBar.setProgress(pausePos);
                } else {
                    pausePos = seekBar.getProgress();

                    mp = new MediaPlayer();
                    try {
                        mp.setDataSource(path);
                    } catch (IOException e) {
                        log(e);
                    }

                    mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            seekBar.setMax(mp.getDuration());
                            seekBar.setProgress(pausePos);

                            mp.seekTo(pausePos);
                            mp.start();
                        }
                    });

                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            isPlaying = isPaused = false;

                            iv_sound_record_complete_play.setImageResource(R.drawable.song_story_play);
                            seekBar.setProgress(0);
                            mp.pause();
                            mp.stop();
                            mp.reset();
                        }
                    });

                    mp.prepareAsync();
                }

                isPlaying = true;
                isPaused = false;
                new SeekBarThread().start();
                iv_sound_record_complete_play.setImageResource(R.drawable.song_story_pause);

            }
        });

        iv_sound_record_complete_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    mp.pause();
                    pausePos = mp.getCurrentPosition();
                    isPlaying = false;
                    isPaused = true;
                    iv_sound_record_complete_play.setImageResource(R.drawable.song_story_play);

                    mp.seekTo(pausePos);
                } else {
                    if (isPaused) {
                        mp.seekTo(pausePos);
                        mp.start();
                        new SeekBarThread().start();
                    } else {
                        mp = new MediaPlayer();
                        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer player) {
                                sb_sound_record.setMax(mp.getDuration());
                                sb_sound_record.setProgress(0);

                                new SeekBarThread().start();
                                player.start();
                            }
                        });

                        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                isPlaying = isPaused = false;

                                iv_sound_record_complete_play.setImageResource(R.drawable.song_story_play);
                                sb_sound_record.setProgress(0);
                                mp.pause();
                                mp.stop();
                                mp.reset();
                            }
                        });
                        try {
                            mp.setDataSource(path);
                            mp.prepareAsync();
                        } catch (IOException e) {
                            log(e);
                        }
                    } // end else

                    isPlaying = true;
                    isPaused = false;

                    iv_sound_record_complete_play.setImageResource(R.drawable.song_story_pause);
                }
            }
        });

        iv_sound_record_complete_replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_sound_record_container.removeAllViews();
                recordInflate();

                file.delete();
                file = null;
                isPlaying = isPaused = false;

                if (mp != null) {
                    mp.stop();
                    mp.release();
                    mp = null;
                }
            }
        });
    }

    private void recordInflate() {
        getLayoutInflater().inflate(R.layout.sound_item_record, ll_sound_record_container, true);
        tv_sound_record = (TextView) ll_sound_record_container.findViewById(R.id.tv_sound_record);
        iv_sound_record = (ImageView) ll_sound_record_container.findViewById(R.id.iv_sound_record);
        tv_record_song_title = (TextView) ll_sound_record_container.findViewById(R.id.tv_record_song_title);
        tv_record_song_title.setText(song_title);
        tv_record_song_singer = (TextView) ll_sound_record_container.findViewById(R.id.tv_record_song_singer);
        tv_record_song_singer.setText(song_singer);

        iv_sound_record_avatar = (ImageView) ll_sound_record_container.findViewById(R.id.iv_sound_record_avatar);
        Glide.with(this).load(getString(R.string.cloud_front_songs_avatar) + song_avatar).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_sound_record_avatar);

        iv_sound_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRecording) {
                    startRec();
                    isRecording = true;
                    iv_sound_record.setImageResource(R.drawable.song_story_stop);
                } else {
                    stopRec();
                    isRecording = false;
                }
            }
        });
    }

    private void checkPermission() {
        if ((ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Log.e("checkSelfPermission", "RECORD_AUDIO  or WRITE_EXTERNAL_STORAGE: NOT GRANTED");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.RECORD_AUDIO)) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, RECORD_EXTERNAL_STORAGE_PERMISSION);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECORD_AUDIO
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE}, RECORD_EXTERNAL_STORAGE_PERMISSION);
            }
        } else {
            Log.e("checkSelfPermission", "RECORD_AUDIO or WRITE_EXTERNAL_STORAGE: GRANTED");
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RECORD_EXTERNAL_STORAGE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("권한 사용 동의 O", " ");
                } else {
                    Log.e("권한 사용 동의 X", " ");
                }
                break;
        }
    }

    private void init() {
        //song info + user info
        et_sound_record_memory = (EditText) findViewById(R.id.et_sound_record_memory);
        iv_record_user_avatar = (ImageView) findViewById(R.id.iv_record_user_avatar);
        Glide.with(this).load(getString(R.string.cloud_front_user_avatar) + user_avatar).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_record_user_avatar);
        tv_record_user_name = (TextView) findViewById(R.id.tv_record_user_name);
        tv_record_user_name.setText(user_name);

        LinearLayout ll_sound_record_location_btn = (LinearLayout) findViewById(R.id.ll_sound_record_location_btn);
        iv_sound_record_location_btn = (ImageView) findViewById(R.id.iv_sound_record_location_btn);
        iv_sound_record_location = (ImageView) findViewById(R.id.iv_sound_record_location);
        btn_sound_record_submit = (Button) findViewById(R.id.btn_sound_record_submit);
        et_sound_record_location = (EditText) findViewById(R.id.et_sound_record_location);
        ll_sound_record_location = (LinearLayout) findViewById(R.id.ll_sound_record_location);
        btn_sound_record_image_upload = (Button) findViewById(R.id.btn_sound_record_image_upload);
        btn_sound_record_select_emotion = (Button) findViewById(R.id.btn_sound_record_select_emotion);
        rv_sound_record_image_upload_gallery = (RecyclerView) findViewById(R.id.rv_sound_record_image_upload_gallery);
        et_sound_record_memory.clearFocus();

        photoList = new ArrayList<>();
        pathList = new ArrayList<>();
        realPathUtil = new RealPathUtil();
        ll_sound_record_container = (LinearLayout) findViewById(R.id.ll_sound_record_container);
        timerHandler = new TimerHandler();

        dirName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/test/";
        File folder = new File(dirName);
        folder.mkdirs();

        int spacing = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin) / 2;
        rv_sound_record_image_upload_gallery.addItemDecoration(new SpaceItemDecoration(spacing));
        rv_sound_record_image_upload_gallery.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        photoViewAdapter = new PhotoViewAdapter(photoList, this, R.layout.item_write_upload_image);
        rv_sound_record_image_upload_gallery.setAdapter(photoViewAdapter);

        recordInflate();

        ll_sound_record_location_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_sound_record_location.getVisibility() == View.VISIBLE) {
                    ll_sound_record_location.setVisibility(View.GONE);
                    iv_sound_record_location_btn.setImageResource(R.drawable.arrow_bottom);
                } else {
                    ll_sound_record_location.setVisibility(View.VISIBLE);
                    iv_sound_record_location_btn.setImageResource(R.drawable.arrow_top);
                }
//                location = et_sound_record_location.getText().toString();
            }
        });

        btn_sound_record_image_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                        버전 체크 (키캣 이상 : 다중 선택O)
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Photo"), PICK_PHOTO);
                } else {
//                       키캣 이하 (다중 선택 X)
                    intent.setAction(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, PICK_PHOTO);
                }
            }
        });

        btn_sound_record_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecording) {
                    Toast.makeText(SongRecordActivity.this, "녹음을 완료하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    final int photoListSize = photoList.size();

                    if (location != null) {
                        location = et_sound_record_location.getText().toString();
                    }

                    // 등록버튼
                    if (photoListSize == 0 && et_sound_record_memory.getText().toString().length() == 0 && file == null && location == null && emotionList == null) {
                        Toast.makeText(SongRecordActivity.this, "추억을 작성해주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (location == null) {
                            location = "";
                        }

                        progressDialog = new ProgressDialog(SongRecordActivity.this);
                        progressDialog.show();
                        progressDialog.getWindow().setBackgroundDrawable(new
                                ColorDrawable(Color.TRANSPARENT));
                        progressDialog.setContentView(R.layout.progress_dialog);

                        if (photoListSize > 10) { // 10 개 선택업로드가 제한이지만 선택자체는 100개 할 수 도 있으므로
                            sleepTime = 850 * 10;
                        } else {
                            sleepTime = 850 * photoListSize;
                        }

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("user_id", String.valueOf(user_id));
                                map.put("range_id", String.valueOf(range_id));
                                map.put("song_id", String.valueOf(song_id));
                                map.put("song_title", song_title);
                                map.put("song_singer", song_singer);
                                map.put("content", et_sound_record_memory.getText().toString());
                                map.put("location", location);


                                songServerConnection = new HeaderInterceptor(access_token).getClientForSongServer().create(SongServerConnection.class);
                                Log.e("range_id", range_id + "");
                                Call<SongStory> call_insert_song_story = songServerConnection.insert_song_story(map);
                                call_insert_song_story.enqueue(new Callback<SongStory>() {
                                    @Override
                                    public void onResponse(Call<SongStory> call, Response<SongStory> response) {
                                        if (response.isSuccessful()) {
                                            final int song_story_id = response.body().getId();

                                            // photos
                                            for (int i = 0; i < photoListSize; i++) {
                                                songStoryServerConnection = new HeaderInterceptor(access_token).getClientForSongStoryServer().create(SongStoryServerConnection.class);
                                                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), addBase64Bitmap(encodeImage(photoList.get(i), i)));
                                                Call<ResponseBody> call_write_photo = songStoryServerConnection.insert_song_photos(song_story_id, requestBody);

                                                call_write_photo.enqueue(new Callback<ResponseBody>() {
                                                    @Override
                                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                        if (!response.isSuccessful()) {
                                                            Toast.makeText(SongRecordActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                                                        }
                                                        //scess
                                                    }

                                                    @Override
                                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                        log(t);
                                                        Toast.makeText(SongRecordActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }

                                            // audio
                                            if (file != null) {
                                                songStoryServerConnection = new HeaderInterceptor(access_token).getClientForSongStoryServer().create(SongStoryServerConnection.class);
                                                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), addBase64Audio(file));
                                                Call<ResponseBody> call_write_audio = songStoryServerConnection.insert_song_audio(song_story_id, requestBody);
                                                call_write_audio.enqueue(new Callback<ResponseBody>() {
                                                    @Override
                                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                        if (!response.isSuccessful()) {
                                                            Toast.makeText(SongRecordActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                                                        }
                                                        //scess
                                                    }

                                                    @Override
                                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                        log(t);
                                                        Toast.makeText(SongRecordActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }

                                            // emotion
                                            if (emotionList != null) {
                                                int emotionListSize = emotionList.size();

                                                for (int i = 0; i < emotionListSize; i++) {
                                                    songStoryServerConnection = new HeaderInterceptor(access_token).getClientForSongStoryServer().create(SongStoryServerConnection.class);
                                                    HashMap<String, String> map = new HashMap<String, String>();
                                                    map.put("song_story_emotion_id", String.valueOf(emotionList.get(i).getId()));
                                                    Call<ResponseBody> call_insert_emotions = songStoryServerConnection.insert_emotion_into_song_story(song_story_id, map);
                                                    call_insert_emotions.enqueue(new Callback<ResponseBody>() {
                                                        @Override
                                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                            if (!response.isSuccessful()) {
                                                                Toast.makeText(SongRecordActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                                                            }
                                                            //scess
                                                        }

                                                        @Override
                                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                            log(t);
                                                            Toast.makeText(SongRecordActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                                }
                                            }
                                        } else {
                                            Toast.makeText(SongRecordActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<SongStory> call, Throwable t) {
                                        log(t);
                                        Toast.makeText(SongRecordActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                    }
                                });
                                try {
                                    Thread.sleep(sleepTime);
                                } catch (InterruptedException e) {
                                    log(e);
                                }
                                progressDialog.dismiss();
                                finish();
                            }
                        }).start();
                    }
                }
            }
        });

        iv_sound_record_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_sound_record_location.setText("");
            }
        });

        btn_sound_record_select_emotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("추억등록");
                intent = new Intent(v.getContext(), EmotionActivity.class);
                startActivityForResult(intent, EMOTION_REQUEST);
            }
        });


        // emotion
        rv_record_emotion = (RecyclerView) findViewById(R.id.rv_record_emotion);
        rv_record_emotion.setLayoutManager(new GridLayoutManager(SongRecordActivity.this, 2));
    }


    public String addBase64Bitmap(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.NO_WRAP | Base64.URL_SAFE);
    }

    public String addBase64Audio(File file) {
        byte[] bytes = convertFileToByteArray(file);
        return Base64.encodeToString(bytes, 0);
    }

    public static byte[] convertFileToByteArray(File file) {
        byte[] byteArray = null;
        try {
            InputStream inputStream = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024 * 8];
            int bytesRead = 0;

            while ((bytesRead = inputStream.read(b)) != -1) {
                bos.write(b, 0, bytesRead);
            }

            byteArray = bos.toByteArray();
        } catch (IOException e) {
            log(e);
        }
        return byteArray;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EMOTION_REQUEST) { //emotion
            if (resultCode == 1001) {
                ArrayList<SongStoryEmotionInfo> dummy_emotionList = (ArrayList<SongStoryEmotionInfo>) data.getSerializableExtra("emotionList");
                emotionList = new ArrayList<>();

                int dummy_emotionListSize = dummy_emotionList.size();
                for (int i = 0; i < dummy_emotionListSize; i++) {
                    if (dummy_emotionList.get(i).isChecked()) {
                        emotionList.add(dummy_emotionList.get(i));
                    }
                }

                emotionAdapter = new EmotionAdapter(emotionList, SongRecordActivity.this, R.layout.item_emotion);
                rv_record_emotion.setAdapter(emotionAdapter);

                return;
            }
        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_PHOTO:
                    int photoListSize = photoList.size();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        ClipData clipdata = data.getClipData();
                        if (clipdata == null) {
                            Uri uri = data.getData();
                            String path = null;
                            try {
                                path = realPathUtil.getRealPathFromURI_API19(this, uri);
                            } catch (RuntimeException e) {
                                log(e);
                                path = realPathUtil.getRealPathFromURI_API11to18(this, uri);
                            }
                            if (photoListSize < 10) {
                                pathList.add(path);
                                photoList.add(uri);
                                photoViewAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(SongRecordActivity.this, "사진은 최대 10개까지 등록이 가능합니다.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            int photoSize = clipdata.getItemCount();
                            if (photoSize > 10) {
                                photoSize = 10;
                                Toast.makeText(SongRecordActivity.this, "사진은 최대 10개까지 등록이 가능합니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                photoSize = clipdata.getItemCount();
                            }

                            for (int i = 0; i < photoSize; i++) {
                                Uri uri = clipdata.getItemAt(i).getUri();
                                String path = null;
                                try {
                                    path = realPathUtil.getRealPathFromURI_API19(this, uri);
                                } catch (RuntimeException e) {
                                    log(e);
                                    path = realPathUtil.getRealPathFromURI_API11to18(this, uri);
                                }
                                if (photoListSize < 10) {
                                    pathList.add(path);
                                    photoList.add(uri);
                                    photoViewAdapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(SongRecordActivity.this, "사진은 최대 10개까지 등록이 가능합니다.", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }

                        }
                    } else {
                        Uri uri = data.getData();
                        try {
                            path = realPathUtil.getRealPathFromURI_API11to18(this, uri);
                        } catch (RuntimeException e) {
                            log(e);
                            path = realPathUtil.getRealPathFromURI_API19(this, uri);
                        }

                        if (photoListSize <= 10) {
                            pathList.add(path);
                            photoList.add(uri);
                            photoViewAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(SongRecordActivity.this, "사진은 최대 10개까지 등록이 가능합니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
            }
        }
    }

    private Bitmap encodeImage(Uri uri, int i) {
        Bitmap bm = null;
        try {
            bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            ExifInterface exifInterface = new ExifInterface(pathList.get(i));
            int exifOrientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int exifDegree = exifOrientationToDegrees(exifOrientation);
            bm = rotate(bm, exifDegree);
        } catch (IOException e) {
            log(e);
        }
        return bm;
    }

    public Bitmap rotate(Bitmap bitmap, int degrees) {
        if (degrees != 0 && bitmap != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2,
                    (float) bitmap.getHeight() / 2);
            try {
                Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), m, true);
                if (bitmap != converted) {
                    bitmap.recycle();
                    bitmap = converted;
                }
            } catch (OutOfMemoryError ex) {
                log(ex);
            }
        }
        return bitmap;
    }

    public int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_write_upload_image, iv_write_upload_delete;

        public PhotoViewHolder(View itemView) {
            super(itemView);

            iv_write_upload_image = (ImageView) itemView.findViewById(R.id.iv_write_upload_image);
            iv_write_upload_delete = (ImageView) itemView.findViewById(R.id.iv_write_upload_delete);

            iv_write_upload_delete.bringToFront();
            iv_write_upload_delete.invalidate();

            iv_write_upload_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    photoList.remove(getAdapterPosition());
                    photoViewAdapter.notifyDataSetChanged();

                    Toast.makeText(SongRecordActivity.this, "사진이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private class PhotoViewAdapter extends RecyclerView.Adapter<PhotoViewHolder> {
        private ArrayList<Uri> photoList;
        private Context context;
        private int layout;

        public PhotoViewAdapter(ArrayList<Uri> photoList, Context context, int layout) {
            this.photoList = photoList;
            this.context = context;
            this.layout = layout;
        }

        @Override
        public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            PhotoViewHolder holder = new PhotoViewHolder((LayoutInflater.from(context).inflate(layout, parent, false)));
            return holder;
        }

        @Override
        public void onBindViewHolder(PhotoViewHolder holder, int position) {
            Glide.with(context).load(photoList.get(position)).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_write_upload_image);
        }

        @Override
        public int getItemCount() {
            return photoList.size();
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
