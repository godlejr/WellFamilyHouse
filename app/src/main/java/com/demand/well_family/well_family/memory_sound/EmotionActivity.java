package com.demand.well_family.well_family.memory_sound;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.repository.SongServerConnection;
import com.demand.well_family.well_family.dto.SongStoryEmotionInfo;
import com.demand.well_family.well_family.repository.interceptor.HeaderInterceptor;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.util.ErrorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ㅇㅇ on 2017-02-12.
 */

public class EmotionActivity extends Activity {
    private RecyclerView rv_emotion;
    private Button btn_emotion;

    private EmotionAdapter emotionAdapter;
    private HashMap<Integer, Integer> map;  // key : id , value : category_id
    private SongServerConnection songServerConnection;
    private ArrayList<SongStoryEmotionInfo> emotionList;

    private static final Logger logger = LoggerFactory.getLogger(EmotionActivity.class);

    private SharedPreferences loginInfo;
    private String access_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_emotion);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(android.view.WindowManager.LayoutParams.MATCH_PARENT, android.view.WindowManager.LayoutParams.MATCH_PARENT);

        init();
    }

    private void init() {
        loginInfo = getSharedPreferences("loginInfo", Activity.MODE_PRIVATE);
        access_token = loginInfo.getString("access_token", null);


        map = new HashMap<>();

        btn_emotion = (Button) findViewById(R.id.btn_emotion);
        btn_emotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.putExtra("emotionList",emotionList);
                setResult(1001, intent);
                finish();
            }
        });

        rv_emotion = (RecyclerView) findViewById(R.id.rv_emotion);

//        songServerConnection = SongServerConnection.retrofit.create(SongServerConnection.class);
        songServerConnection = new HeaderInterceptor(access_token).getClientForSongServer().create(SongServerConnection.class);
        Call<ArrayList<SongStoryEmotionInfo>> call_emotions = songServerConnection.song_story_emotion_List();
        call_emotions.enqueue(new Callback<ArrayList<SongStoryEmotionInfo>>() {
            @Override
            public void onResponse(Call<ArrayList<SongStoryEmotionInfo>> call, Response<ArrayList<SongStoryEmotionInfo>> response) {
                if(response.isSuccessful()) {
                    emotionList = response.body();
                    emotionAdapter = new EmotionAdapter(emotionList, EmotionActivity.this, R.layout.item_emotion);
                    rv_emotion.setAdapter(emotionAdapter);
                    rv_emotion.setLayoutManager(new LinearLayoutManager(EmotionActivity.this, LinearLayoutManager.VERTICAL, false));
                } else {
                    Toast.makeText(EmotionActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<SongStoryEmotionInfo>> call, Throwable t) {
                log(t);
                Toast.makeText(EmotionActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });

    }


    private class EmotionViewHolder extends RecyclerView.ViewHolder {
        TextView tv_emotion;
        ImageView iv_emotion;
        LinearLayout ll_emotion_background;

        public EmotionViewHolder(View itemView) {
            super(itemView);
            tv_emotion = (TextView) itemView.findViewById(R.id.tv_emotion);
            iv_emotion = (ImageView) itemView.findViewById(R.id.iv_emotion);
            ll_emotion_background = (LinearLayout) itemView.findViewById(R.id.ll_emotion_background);
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
        public EmotionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            EmotionViewHolder userViewHolder = new EmotionViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
            return userViewHolder;
        }

        @Override
        public void onBindViewHolder(final EmotionViewHolder holder, final int position) {
            Glide.with(EmotionActivity.this).load(getString(R.string.cloud_front_song_story_emotion) + emotionList.get(position).getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_emotion);
            holder.tv_emotion.setText(emotionList.get(position).getName());

            holder.ll_emotion_background.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (emotionList.get(position).isChecked()) {
                        holder.ll_emotion_background.setBackgroundColor(Color.WHITE);
                        emotionList.get(position).setChecked(false);
                    } else {
                        holder.ll_emotion_background.setBackgroundColor(Color.parseColor("#ebe4d6"));
                        emotionList.get(position).setChecked(true);
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return emotionList.size();
        }
    }

    private static void log(Throwable throwable){
        StackTraceElement[] ste =  throwable.getStackTrace();
        String className = ste[0].getClassName();
        String methodName = ste[0].getMethodName();
        int lineNumber = ste[0].getLineNumber();
        String fileName = ste[0].getFileName();

        if(LogFlag.printFlag){
            if(logger.isInfoEnabled()){
                logger.info("Exception: " + throwable.getMessage());
                logger.info(className + "."+ methodName+" "+ fileName +" "+ lineNumber +" "+ "line" );
            }
        }
    }

}
