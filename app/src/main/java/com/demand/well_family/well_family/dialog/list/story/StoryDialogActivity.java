package com.demand.well_family.well_family.dialog.list.story;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.demand.well_family.well_family.ModifyStoryActivity;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.repository.StoryServerConnection;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.interceptor.HeaderInterceptor;
import com.demand.well_family.well_family.util.ErrorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ㅇㅇ on 2017-03-21.
 */

public class StoryDialogActivity extends Activity {
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
    private int story_user_id;
    private String story_content;
    private String story_user_name;
    private ArrayList<Photo> photoList;

    private RecyclerView rv_popup_comment;
    private ArrayList<String> popupList;
    private PopupAdapter popupAdapter;

    //request code
    private static final int MODIFY_REQUEST = 1;

    //result code
    private static final int DELETE = 5;

    private String content;
    private StoryServerConnection storyServerConnection;
    private static final Logger logger = LoggerFactory.getLogger(StoryDialogActivity.class);
    private static final int DELETE_REQUEST = 4;

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
        rv_popup_comment = (RecyclerView) findViewById(R.id.rv_popup_comment);

        story_id = getIntent().getIntExtra("story_id", 0);
        story_user_id = getIntent().getIntExtra("content_user_id", 0);
        story_content = getIntent().getStringExtra("story_content");
        position = getIntent().getIntExtra("position", 0);

        popupList = new ArrayList<>();
        popupList.add("본문 복사");
        if (user_id == story_user_id) {
            popupList.add("수정");
            popupList.add("삭제");
        } else {
            popupList.add("신고하기");
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

                    if (position == 0) { // 복사
                        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText("label", story_content);
                        clipboardManager.setPrimaryClip(clipData);
                        Toast.makeText(context, " 클립보드에 복사되었습니다.", Toast.LENGTH_LONG).show();
                        finish();
                    }

                    if (user_id == story_user_id) { // 본인
                        if (position == 1) { // 수정
                            intent = new Intent(v.getContext(), ModifyStoryActivity.class);
                            intent.putExtra("story_id", getIntent().getIntExtra("story_id", 0));
                            intent.putExtra("content", getIntent().getStringExtra("content"));
                            intent.putExtra("photoList", getIntent().getSerializableExtra("photoList"));
                            intent.putExtra("position", position);

                            startActivityForResult(intent, MODIFY_REQUEST);

                        } else if (position == 2) { // 삭제
                            storyServerConnection = new HeaderInterceptor(access_token).getClientForStoryServer().create(StoryServerConnection.class);
                            Call<Void> call_delete_story = storyServerConnection.delete_story(story_id);
                            call_delete_story.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if(response.isSuccessful()){
                                        Toast.makeText(context, "게시물이 삭제되었습니다." , Toast.LENGTH_SHORT).show();

                                        Intent intent = getIntent();
                                        intent.putExtra("position",position);
                                        setResult(DELETE, intent);
                                        finish();
                                    } else {
                                        Toast.makeText(context, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    log(t);
                                }
                            });

                        } else {
                            finish();
                        }
                    } else {
                        if (position == 1) { // 신고

                        } else { // 취소
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
            content = data.getStringExtra("content");
            if (resultCode == RESULT_OK) {
                Intent intent = getIntent();
                intent.putExtra("content", content);
                intent.putExtra("position", position);
                setResult(RESULT_OK, intent);

                finish();
            }
        }
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
//            Intent intent = getIntent();
//            intent.putExtra("content", content);
//            intent.putExtra("position", position);
//            setResult(RESULT_CANCELED, intent);
//
//            finish();
//        }
//        return true;
//    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        intent.putExtra("content", content);
        intent.putExtra("position", position);
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
