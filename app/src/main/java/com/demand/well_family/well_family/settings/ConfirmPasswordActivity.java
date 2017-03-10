package com.demand.well_family.well_family.settings;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.demand.well_family.well_family.R;

/**
 * Created by ㅇㅇ on 2017-03-06.
 */

public class ConfirmPasswordActivity extends Activity {
    private EditText et_confirm_email;
    private EditText et_confirm_pwd;
    private Button btn_confirm_pwd;
    private LinearLayout ll_search_pwd;

    //user_info
    private int user_id;
    private String user_name;
    private String user_avatar;
    private String user_email;
    private String user_birth;
    private String user_phone;
    private int user_level;
    private SharedPreferences loginInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_password);
        
        setUserInfo();
        setToolbar(getWindow().getDecorView());
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
    private void setToolbar(View view){
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) view.findViewById(R.id.toolBar);
        ImageView toolbar_back = (ImageView) toolbar.findViewById(R.id.toolbar_back);
        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText("비밀번호 확인");
    }

    private void init(){
        et_confirm_email = (EditText)findViewById(R.id.et_confirm_email);
        et_confirm_pwd = (EditText)findViewById(R.id.et_confirm_pwd);
        btn_confirm_pwd = (Button)findViewById(R.id.btn_confirm_pwd);
        ll_search_pwd = (LinearLayout)findViewById(R.id.ll_search_pwd);

        btn_confirm_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_confirm_email.getText().length() == 0 || et_confirm_pwd.getText().length() == 0){
                    Toast.makeText(ConfirmPasswordActivity.this, "이메일과 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(v.getContext(), SearchAccountActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        ll_search_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 비밀번호 찾기
                Intent intent = new Intent(v.getContext(), SearchAccountActivity.class);
                startActivity(intent);
            }
        });

        et_confirm_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(et_confirm_email.getText().length() != 0 && s.length() != 0){
                    btn_confirm_pwd.setBackgroundResource(R.drawable.round_corner_btn);
                }else {
                    btn_confirm_pwd.setBackgroundResource(R.drawable.round_corner_btn_gray);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}