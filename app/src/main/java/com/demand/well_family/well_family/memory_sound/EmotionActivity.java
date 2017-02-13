package com.demand.well_family.well_family.memory_sound;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.Emotion;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ㅇㅇ on 2017-02-12.
 */

public class EmotionActivity extends Activity {
    private RecyclerView rv_emotion;
    private Button btn_emotion;

    private EmotionAdapter emotionAdapter;
    private HashMap<String, String> map;  // key : id , value : category_id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_emotion);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        init();
    }

    private void init() {
        final ArrayList<Emotion> emotionList = new ArrayList<>();
        map = new HashMap<>();

        btn_emotion = (Button) findViewById(R.id.btn_emotion);
        btn_emotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // map에 id, category_id 들어있음

                Log.e("tttttt", map.toString());

            }
        });

        rv_emotion = (RecyclerView) findViewById(R.id.rv_emotion);

        emotionList.add(new Emotion("1", "1" , "2017-02-12", "행복해요", "https://cdn.zeplin.io/58997773056e36218ca50ad1/assets/B330C832-3F6A-401B-9744-27C26B23D065.png"));
        emotionList.add(new Emotion("2", "1", "2017-02-12", "인생은 즐거워요", "https://cdn.zeplin.io/58997773056e36218ca50ad1/assets/B330C832-3F6A-401B-9744-27C26B23D065.png"));
        emotionList.add(new Emotion("3", "1", "2017-02-12", "완전 설레요", "https://cdn.zeplin.io/58997773056e36218ca50ad1/assets/B330C832-3F6A-401B-9744-27C26B23D065.png"));
        emotionList.add(new Emotion("4", "1", "2017-02-12", "감동 받았어요", "https://cdn.zeplin.io/58997773056e36218ca50ad1/assets/B330C832-3F6A-401B-9744-27C26B23D065.png"));

        emotionList.add(new Emotion("5", "2", "2017-02-12", "인생은 살만해요", "https://cdn.zeplin.io/58997773056e36218ca50ad1/assets/13DEC6B4-C78E-462F-B262-4A04FEB0D26A.png"));
        emotionList.add(new Emotion("6", "2", "2017-02-12", "그리워요", "https://cdn.zeplin.io/58997773056e36218ca50ad1/assets/13DEC6B4-C78E-462F-B262-4A04FEB0D26A.png"));
        emotionList.add(new Emotion("7", "2", "2017-02-12", "궁금해요", "https://cdn.zeplin.io/58997773056e36218ca50ad1/assets/13DEC6B4-C78E-462F-B262-4A04FEB0D26A.png"));

        emotionList.add(new Emotion("8", "3", "2017-02-12", "인생은 서글퍼요", "https://cdn.zeplin.io/58997773056e36218ca50ad1/assets/AE8BAC05-2E1D-42B2-953C-B54B2AB45699.png"));
        emotionList.add(new Emotion("9", "3", "2017-02-12", "우울해요", "https://cdn.zeplin.io/58997773056e36218ca50ad1/assets/AE8BAC05-2E1D-42B2-953C-B54B2AB45699.png"));
        emotionList.add(new Emotion("10", "3", "2017-02-12", "외로워요", "https://cdn.zeplin.io/58997773056e36218ca50ad1/assets/AE8BAC05-2E1D-42B2-953C-B54B2AB45699.png"));
        emotionList.add(new Emotion("11", "3", "2017-02-12", "화나요", "https://cdn.zeplin.io/58997773056e36218ca50ad1/assets/AE8BAC05-2E1D-42B2-953C-B54B2AB45699.png"));



        emotionAdapter = new EmotionAdapter(emotionList, this, R.layout.item_emotion);
        rv_emotion.setAdapter(emotionAdapter);
        rv_emotion.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

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
        private ArrayList<Emotion> emotionList;
        private Context context;
        private int layout;

        public EmotionAdapter(ArrayList<Emotion> emotionList, Context context, int layout) {
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
            Glide.with(EmotionActivity.this).load(emotionList.get(position).getImage()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_emotion);
            holder.tv_emotion.setText(emotionList.get(position).getName());

            holder.ll_emotion_background.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(map.get(emotionList.get(position).getId()) != null) {
                       map.remove(emotionList.get(position).getId());

                        holder.ll_emotion_background.setBackgroundColor(Color.WHITE);
                    } else{
                        map.put(emotionList.get(position).getId(), emotionList.get(position).getCategory_id());

                        holder.ll_emotion_background.setBackgroundColor(Color.parseColor("#ebe4d6"));
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return emotionList.size();
        }
    }

}
