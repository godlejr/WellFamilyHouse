package com.demand.well_family.well_family.settings;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.flag.LogFlag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ㅇㅇ on 2017-03-09.
 */

public class ResetPasswordActivity extends Activity{
    private EditText et_reset_pwd1;
    private EditText et_reset_pwd2;
    private Button btn_reset_pwd;

    //user_info
    private int user_id;
    private String user_name;
    private String user_avatar;
    private String user_email;
    private String user_birth;
    private String user_phone;
    private int user_level;
    private SharedPreferences loginInfo;
    private static final Logger logger = LoggerFactory.getLogger(ResetPasswordActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

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
        toolbar_title.setText("비밀번호 재설정");
    }

    private void init(){
        btn_reset_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_reset_pwd1.getText().length() == 0 || et_reset_pwd2.getText().length() == 0 ||
                        !et_reset_pwd1.equals(et_reset_pwd2)){
                    Toast.makeText(ResetPasswordActivity.this, "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ResetPasswordActivity.this, "비밀번호가 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });


        et_reset_pwd2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(et_reset_pwd1.getText().length() != 0 && s.length() != 0){
                    btn_reset_pwd.setBackgroundResource(R.drawable.round_corner_btn);
                }else {
                    btn_reset_pwd.setBackgroundResource(R.drawable.round_corner_btn_gray);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
