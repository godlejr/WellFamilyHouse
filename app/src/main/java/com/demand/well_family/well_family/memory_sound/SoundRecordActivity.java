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
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import com.demand.well_family.well_family.LoginActivity;
import com.demand.well_family.well_family.MainActivity;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.connection.Server_Connection;
import com.demand.well_family.well_family.dto.Range;
import com.demand.well_family.well_family.dto.SongStory;
import com.demand.well_family.well_family.dto.SongStoryEmotionInfo;
import com.demand.well_family.well_family.market.MarketMainActivity;
import com.demand.well_family.well_family.users.UserActivity;
import com.demand.well_family.well_family.util.RealPathUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import static com.demand.well_family.well_family.LoginActivity.finishList;

/**
 * Created by ㅇㅇ on 2017-02-01.
 */

public class SoundRecordActivity extends Activity {
    private TextView tv_record_song_title, tv_record_song_singer;
    private ImageView iv_record_user_avatar;
    private TextView tv_record_user_name;
    private EditText et_sound_record_memory;

    private Spinner sp_sound;
    private LinearLayout ll_sound_record_container, ll_sound_record_location;
    private ImageView iv_sound_record, iv_sound_record_complete_play, iv_sound_record_complete_replay;
    private MediaRecorder recorder;
    private String fileName, dirName, path;
    private boolean isRecording = false;

    private TextView tv_sound_record, tv_sound_record_complete;
    private int timer_sec = 0, timer_min = 0;
    private TimerHandler timerHandler;
    private Timer timer;
    private SeekBar sb_sound_record;
    private File file = null;
    private boolean isPlaying = false, isPaused = false;
    private int pausePos;
    private MediaPlayer mp;

    private Button btn_sound_record_image_upload, btn_sound_record_submit, btn_sound_record_select_emotion;
    private RecyclerView rv_sound_record_image_upload;
    private ArrayList<Uri> photoList;
    private ArrayList<String> pathList;
    private Intent intent;
    private static final int PICK_PHOTO = 77;
    private PhotoViewAdapter photoViewAdapter;
    private EditText et_sound_record_location;
    private String location = null;
    private ArrayAdapter spinnerAdapter;
    private ImageView iv_sound_record_location, iv_sound_record_location_btn;

    private HashMap<Integer, String> spList;

    //user_info
    private int user_id;
    private String user_name;
    private int user_level;
    private String user_avatar;
    private String user_email;
    private String user_phone;
    private String user_birth;

    //song info
    private int song_id;
    private String song_title;
    private String song_singer;
    private String song_avatar;

    //server
    private Server_Connection server_connection;
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

    private ArrayList<SongStoryEmotionInfo> emotionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sound_activity_record);

        user_id = getIntent().getIntExtra("user_id", 0);
        user_name = getIntent().getStringExtra("user_name");
        user_level = getIntent().getIntExtra("user_level", 0);
        user_avatar = getIntent().getStringExtra("user_avatar");
        user_email = getIntent().getStringExtra("user_email");
        user_phone = getIntent().getStringExtra("user_phone");
        user_birth = getIntent().getStringExtra("user_birth");

        song_id = getIntent().getIntExtra("song_id", 0);
        song_title = getIntent().getStringExtra("song_title");
        song_singer = getIntent().getStringExtra("song_singer");
        song_avatar = getIntent().getStringExtra("song_avatar");

        finishList.add(this);

        checkPermission();
        init();
        setToolbar(this.getWindow().getDecorView(), this, "추억기록");
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


                Intent intent = new Intent(SoundRecordActivity.this, UserActivity.class);
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
                        intent = new Intent(SoundRecordActivity.this, MainActivity.class);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("user_email", user_email);
                        intent.putExtra("user_birth", user_birth);
                        intent.putExtra("user_phone", user_phone);
                        intent.putExtra("user_name", user_name);
                        intent.putExtra("user_level", user_level);
                        intent.putExtra("user_avatar", user_avatar);
                        startActivity(intent);

                        break;

                    case R.id.menu_search:
                        Toast.makeText(getApplicationContext(), "준비중입니다.", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.menu_market:
                        intent = new Intent(SoundRecordActivity.this, MarketMainActivity.class);
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

                        intent = new Intent(SoundRecordActivity.this, LoginActivity.class);
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

    private void setBack() {
        finish();
    }

    private void setSpinner() {
        sp_sound = (Spinner) findViewById(R.id.sp_sound);

        spList = new HashMap<>();

        server_connection = Server_Connection.retrofit.create(Server_Connection.class);
        Call<ArrayList<Range>> call_song_range = server_connection.song_range_List();
        call_song_range.enqueue(new Callback<ArrayList<Range>>() {
            @Override
            public void onResponse(Call<ArrayList<Range>> call, Response<ArrayList<Range>> response) {
                ArrayList<Range> rangeList = response.body();
                for (int i = 0; i < rangeList.size(); i++) {
                    spList.put(rangeList.get(i).getId(), rangeList.get(i).getName());
                }

                String[] spinnerArray = new String[spList.size()];
                for (int i = 0; i < spList.size(); i++) {
                    spinnerArray[i] = spList.get(i + 1);
                }

                spinnerAdapter = new ArrayAdapter(SoundRecordActivity.this, R.layout.custom_spinner_item, spinnerArray) {
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

            }

            @Override
            public void onFailure(Call<ArrayList<Range>> call, Throwable t) {
                Toast.makeText(SoundRecordActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();

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
            e.printStackTrace();
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
                Toast.makeText(SoundRecordActivity.this, "최대 5분까지 녹음이 가능합니다.", Toast.LENGTH_SHORT).show();
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
            e.printStackTrace();
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
                // 사용자가 움직여놓은 위치
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
                        e.printStackTrace();
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

                            iv_sound_record_complete_play.setImageResource(R.drawable.play);
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
                iv_sound_record_complete_play.setImageResource(R.drawable.pause);

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
                    iv_sound_record_complete_play.setImageResource(R.drawable.play);

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

                                iv_sound_record_complete_play.setImageResource(R.drawable.play);
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
                            e.printStackTrace();
                        }
                    } // end else

                    isPlaying = true;
                    isPaused = false;

                    iv_sound_record_complete_play.setImageResource(R.drawable.pause);
                }
            }
        });

        iv_sound_record_complete_replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_sound_record_container.removeAllViews();
                recordInflate();

                file.delete();
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
                    iv_sound_record.setImageResource(R.drawable.stop);
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
        rv_sound_record_image_upload = (RecyclerView) findViewById(R.id.rv_sound_record_image_upload);
        btn_sound_record_select_emotion = (Button) findViewById(R.id.btn_sound_record_select_emotion);

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
        rv_sound_record_image_upload.addItemDecoration(new SpaceItemDecoration(spacing));
        rv_sound_record_image_upload.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        photoViewAdapter = new PhotoViewAdapter(photoList, this, R.layout.item_write_upload_image);
        rv_sound_record_image_upload.setAdapter(photoViewAdapter);

        setSpinner();
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
                location = et_sound_record_location.getText().toString();
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
                    Toast.makeText(SoundRecordActivity.this, "녹음을 완료하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    if (et_sound_record_memory.getText().toString().equals("") && file == null && photoList.size() == 0) {
                        return;
                    }

                    if (ll_sound_record_location.getVisibility() == View.VISIBLE) {
                        location = et_sound_record_location.getText().toString();
                    }

                    // 등록버튼
                    if (photoList.size() == 0 && et_sound_record_memory.getText().toString().length() == 0 && file == null && location.length() == 0) {
                        Toast.makeText(SoundRecordActivity.this, "하나라도 입력", Toast.LENGTH_SHORT).show();
                    } else {
                        if (location == null) {
                            location = "";
                        }

                        progressDialog = new ProgressDialog(SoundRecordActivity.this);
                        progressDialog.show();
                        progressDialog.getWindow().setBackgroundDrawable(new
                                ColorDrawable(Color.TRANSPARENT));
                        progressDialog.setContentView(R.layout.progress_dialog);

                        if (photoList.size() > 10) { // 10 개 선택업로드가 제한이지만 선택자체는 100개 할 수 도 있으므로
                            sleepTime = 850 * 10;
                        } else {
                            sleepTime = 850 * photoList.size();
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("range_id", String.valueOf(range_id));
                                map.put("song_id", String.valueOf(song_id));
                                map.put("song_title", song_title);
                                map.put("song_singer", song_singer);
                                map.put("content", et_sound_record_memory.getText().toString());
                                map.put("location", location);

                                server_connection = Server_Connection.retrofit.create(Server_Connection.class);
                                Call<ArrayList<SongStory>> call_insert_song_story = server_connection.insert_song_story(String.valueOf(user_id), map);
                                call_insert_song_story.enqueue(new Callback<ArrayList<SongStory>>() {
                                    @Override
                                    public void onResponse(Call<ArrayList<SongStory>> call, Response<ArrayList<SongStory>> response) {
                                        for (int i = 0; i < photoList.size(); i++) {
                                            server_connection = Server_Connection.retrofit.create(Server_Connection.class);
                                            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), addBase64Bitmap(encodeImage(photoList.get(i), i)));
                                            Call<ResponseBody> call_write_photo = server_connection.insert_song_photos(String.valueOf(response.body().get(0).getId()), requestBody);

                                            call_write_photo.enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    //scess
                                                }

                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                    Toast.makeText(SoundRecordActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }

                                        // 녹음 파일 업로드
                                        if (file != null) {
                                            server_connection = Server_Connection.retrofit.create(Server_Connection.class);
                                            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), addBase64Audio(file));
                                            Call<ResponseBody> call_write_audio = server_connection.insert_song_audio(String.valueOf(response.body().get(0).getId()), requestBody);
                                            call_write_audio.enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    //scess
                                                }

                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                    Toast.makeText(SoundRecordActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ArrayList<SongStory>> call, Throwable t) {
                                        Toast.makeText(SoundRecordActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                    }
                                });
                                try {
                                    Thread.sleep(sleepTime);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
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
    }


    public String addBase64Bitmap(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, baos);
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
            e.printStackTrace();
        }
        return byteArray;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EMOTION_REQUEST) { //emotion
            if (resultCode == 1001) {
                ArrayList<SongStoryEmotionInfo> dummy_emotionList = (ArrayList<SongStoryEmotionInfo>) data.getSerializableExtra("emotionList");
                emotionList = new ArrayList<>();
                for(int i =0 ; i<dummy_emotionList.size();i++){
                    if(dummy_emotionList.get(i).isChecked()){
                        emotionList.add(dummy_emotionList.get(i));
                    }
                }
                return;
            }
        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_PHOTO:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        ClipData clipdata = data.getClipData();
                        if (clipdata == null) {
                            Uri uri = data.getData();
                            String path = null;
                            try {
                                path = realPathUtil.getRealPathFromURI_API19(this, uri);
                            } catch (RuntimeException e) {
                                path = realPathUtil.getRealPathFromURI_API11to18(this, uri);
                            }
                            if (photoList.size() < 10) {
                                pathList.add(path);
                                photoList.add(uri);
                                photoViewAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(SoundRecordActivity.this, "사진은 최대 10개까지 등록이 가능합니다.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            int photoSize = clipdata.getItemCount();
                            if (photoSize > 10) {
                                photoSize = 10;
                                Toast.makeText(SoundRecordActivity.this, "사진은 최대 10개까지 등록이 가능합니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                photoSize = clipdata.getItemCount();
                            }

                            for (int i = 0; i < photoSize; i++) {
                                Uri uri = clipdata.getItemAt(i).getUri();
                                String path = null;
                                try {
                                    path = realPathUtil.getRealPathFromURI_API19(this, uri);
                                } catch (RuntimeException e) {
                                    path = realPathUtil.getRealPathFromURI_API11to18(this, uri);
                                }
                                if (photoList.size() < 10) {
                                    pathList.add(path);
                                    photoList.add(uri);
                                    photoViewAdapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(SoundRecordActivity.this, "사진은 최대 10개까지 등록이 가능합니다.", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }

                        }
                    } else {
                        Uri uri = data.getData();
                        try {
                            path = realPathUtil.getRealPathFromURI_API19(this, uri);
                        } catch (RuntimeException e) {
                            path = realPathUtil.getRealPathFromURI_API11to18(this, uri);
                        }

                        if (photoList.size() <= 10) {
                            pathList.add(path);
                            photoList.add(uri);
                            photoViewAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(SoundRecordActivity.this, "사진은 최대 10개까지 등록이 가능합니다.", Toast.LENGTH_SHORT).show();
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
            e.printStackTrace();
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
                ex.printStackTrace();
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

                    Toast.makeText(SoundRecordActivity.this, "사진이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
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

}
