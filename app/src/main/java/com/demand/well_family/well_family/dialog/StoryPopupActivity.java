package com.demand.well_family.well_family.dialog;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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

import com.demand.well_family.well_family.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ㅇㅇ on 2017-03-21.
 */

public class StoryPopupActivity extends Activity {
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

    // story_info
    private int story_id;
    private int story_user_id;
    private String story_content;
    private String story_user_name;

    private RecyclerView rv_popup_comment;
    private ArrayList<String> popupList;
    private PopupAdapter popupAdapter;

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
        story_user_id = getIntent().getIntExtra("story_user_id", 0);
        story_content = getIntent().getStringExtra("story_content");


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
                    if (position == 0) { // 복사
                        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText("label", story_content);
                        clipboardManager.setPrimaryClip(clipData);
                        Toast.makeText(context, " 클립보드에 복사되었습니다.", Toast.LENGTH_LONG).show();
                        finish();
                    }

                    if (user_id == story_user_id) { // 본인
                        if (position == 1) { // 수정

                        } else if (position == 2) { // 삭제

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

}
