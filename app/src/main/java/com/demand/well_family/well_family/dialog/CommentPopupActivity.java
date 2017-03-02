package com.demand.well_family.well_family.dialog;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.demand.well_family.well_family.R;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-03-02.
 */

public class CommentPopupActivity extends Activity {
    private SharedPreferences loginInfo;
    private RecyclerView rv_popup_comment;
    private PopupAdapter popupAdapter;
    private ArrayList<String> popupList;

    private int user_id;
    private String user_name;
    private String user_avatar;
    private int user_level;
    private String user_email;
    private String user_phone;
    private String user_birth;

    private int comment_id;
    private String comment_content;
    private int position;

    private static final int EDIT_REQUEST = 1;
    private static final int DELETE_REQUEST = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_comment);
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
    }

    private void init() {
        comment_id = getIntent().getIntExtra("comment_id", 0);
        comment_content = getIntent().getStringExtra("comment_content");
        Log.e("ddddd",position+"");
        position = getIntent().getIntExtra("position", -1);

        rv_popup_comment = (RecyclerView) findViewById(R.id.rv_popup_comment);
        popupList = new ArrayList<>();
        popupList.add("댓글 복사");
        popupList.add("댓글 수정");
        popupList.add("댓글 삭제");

        popupAdapter = new PopupAdapter(CommentPopupActivity.this, popupList, R.layout.item_textview);
        rv_popup_comment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv_popup_comment.setAdapter(popupAdapter);

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
        private ArrayList<String> popupList;
        private int layout;

        public PopupAdapter(Context context, ArrayList<String> popupList, int layout) {
            this.context = context;
            this.popupList = popupList;
            this.layout = layout;
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
                    if (position == 0) {
                        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText("label", comment_content);
                        clipboardManager.setPrimaryClip(clipData);
                        Toast.makeText(CommentPopupActivity.this, " 클립보드에 복사되었습니다.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    if (position == 1) {
                        Intent modify_intent = new Intent(CommentPopupActivity.this, CommentModifyActivity.class);
                        modify_intent.putExtra("comment_id", comment_id);
                        modify_intent.putExtra("comment_content", comment_content);
                        startActivityForResult(modify_intent, EDIT_REQUEST);
                    }
                    if (position == 2) {
                        Intent modify_intent = new Intent(CommentPopupActivity.this, CommentDeleteActivity.class);
                        modify_intent.putExtra("comment_id", comment_id);
                        startActivityForResult(modify_intent, DELETE_REQUEST);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return popupList.size();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_REQUEST) {
            if (resultCode == RESULT_OK) {
                Intent intent = getIntent();
                intent.putExtra("flag", 1); //1: modify , 2:delete
                intent.putExtra("content", data.getStringExtra("content"));
                intent.putExtra("position", position);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }

        if (requestCode == DELETE_REQUEST) {
            if (resultCode == RESULT_OK) {
                Intent intent = getIntent();
                intent.putExtra("flag", 2); //1: modify , 2:delete
                intent.putExtra("position", position);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
            if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }
}