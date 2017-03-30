package com.demand.well_family.well_family.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.demand.well_family.well_family.ModifyStoryActivity;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.connection.SongStoryServerConnection;
import com.demand.well_family.well_family.connection.StoryServerConnection;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.dto.SongStoryEmotionData;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.interceptor.HeaderInterceptor;
import com.demand.well_family.well_family.memory_sound.ModifySongStoryActivity;
import com.demand.well_family.well_family.util.ErrorUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ㅇㅇ on 2017-03-29.
 */

public class SongStoryPopup extends Activity {
    // user_info
    private SharedPreferences loginInfo;
    private int user_id;
    private String user_name;
    private String user_avatar;
    private int user_level;
    private String user_email;
    private String user_phone;
    private String user_birth;
    private String access_token;
    private int position;

    // story_info
    private int story_id;
    private int content_user_id;
    private String content;
    private String story_user_name;
    private String location;
    private ArrayList<Photo> photoList;
    private int song_id;
    private String song_avatar;
    private String song_title;
    private String song_singer;
    private int story_position;

    private RecyclerView rv_popup_comment;
    private ArrayList<String> popupList;
    private PopupAdapter popupAdapter;

    //request code
    private static final int MODIFY_REQUEST = 1;

    //result code
    private static final int DELETE = 5;

    private SongStoryServerConnection songStoryServerConnection;
    private static final Logger logger = LoggerFactory.getLogger(SongStoryPopup.class);
    private static final int DELETE_REQUEST = 4;
    private ArrayList<SongStoryEmotionData> emotionList;
    private boolean story_user_is_me;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_comment);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(android.view.WindowManager.LayoutParams.WRAP_CONTENT, android.view.WindowManager.LayoutParams.WRAP_CONTENT);

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
    }

    private void init() {
        emotionList = (ArrayList<SongStoryEmotionData>) getIntent().getSerializableExtra("emotionList");

        story_position = getIntent().getIntExtra("position", 0);
        rv_popup_comment = (RecyclerView) findViewById(R.id.rv_popup_comment);

        story_id = getIntent().getIntExtra("story_id", 0);
        content_user_id = getIntent().getIntExtra("content_user_id", 0);
        content = getIntent().getStringExtra("content");
        Log.e("tt2", content);

        location = getIntent().getStringExtra("location");
        position = getIntent().getIntExtra("position", 0);
        photoList = (ArrayList<Photo>) getIntent().getSerializableExtra("photoList");

        song_id = getIntent().getIntExtra("song_id", 0);
        song_avatar = getIntent().getStringExtra("song_avatar");
        song_title = getIntent().getStringExtra("song_title");
        song_singer = getIntent().getStringExtra("song_singer");
        story_user_is_me = getIntent().getBooleanExtra("story_user_is_me", false);

        popupList = new ArrayList<>();
        popupList.add("본문 복사");
        popupList.add("수정");
        if (story_user_is_me) {
            popupList.add("삭제");
        }
        popupList.add("취소");

        popupAdapter = new PopupAdapter(this, popupList, R.layout.item_textview);
        rv_popup_comment.setAdapter(popupAdapter);
        rv_popup_comment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private class PopupViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_popup_comment_text;

        public PopupViewHolder(View itemView) {
            super(itemView);
            tv_popup_comment_text = (TextView) itemView.findViewById(R.id.tv_popup_comment_text);
        }
    }

    private class PopupAdapter extends RecyclerView.Adapter<PopupViewHolder> {
        private Context context;
        private int layout;
        private ArrayList<String> popupList;

        public PopupAdapter(Context context, ArrayList<String> popupList, int layout) {
            this.context = context;
            this.layout = layout;
            this.popupList = popupList;
        }

        @Override
        public int getItemCount() {
            return popupList.size();
        }

        @Override
        public PopupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            PopupViewHolder popupViewHolder = new PopupViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
            return popupViewHolder;
        }

        @Override
        public void onBindViewHolder(PopupViewHolder holder, final int position) {
            holder.tv_popup_comment_text.setText(popupList.get(position));
            holder.tv_popup_comment_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;

                    if (position == 1) { // 수정
                        intent = new Intent(v.getContext(), ModifySongStoryActivity.class);
                        intent.putExtra("story_id", story_id);
                        intent.putExtra("content", content);
                        intent.putExtra("photoList", photoList);
                        intent.putExtra("position", story_position);
                        intent.putExtra("location", getIntent().getStringExtra("location"));

                        intent.putExtra("song_id", song_id);
                        intent.putExtra("song_avatar", song_avatar);
                        intent.putExtra("song_title", song_title);
                        intent.putExtra("song_singer", song_singer);
                        intent.putExtra("emotionList", emotionList);

                        startActivityForResult(intent, MODIFY_REQUEST);
                    }
                    if(story_user_is_me) {
                        if (position == 2) { // 삭제
                            songStoryServerConnection = new HeaderInterceptor(access_token).getClientForSongStoryServer().create(SongStoryServerConnection.class);
                            Call<Void> call_delete_story = songStoryServerConnection.delete_story(story_id);
                            call_delete_story.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(context, "게시물이 삭제되었습니다.", Toast.LENGTH_SHORT).show();

                                        Intent intent = getIntent();
                                        intent.putExtra("position", position);
                                        setResult(DELETE, intent);
                                        finish();
                                    } else {
                                        Toast.makeText(context, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    log(t);
                                }
                            });
                        }

                        if (position == 3) { // 취소
                            finish();
                        }
                    }else{
                        if (position == 2) { // 취소
                            finish();
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MODIFY_REQUEST) {
            if (resultCode == RESULT_OK) {
                content = data.getStringExtra("content");
                Intent intent = getIntent();
                intent.putExtra("content", content);
                intent.putExtra("position", story_position);
                setResult(RESULT_OK, intent);

                finish();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            Intent intent = getIntent();
            intent.putExtra("content", content);
            intent.putExtra("position", story_position);
            setResult(RESULT_CANCELED, intent);

            finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        intent.putExtra("content", content);
        intent.putExtra("position", story_position);
        setResult(RESULT_CANCELED, intent);

        finish();
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
