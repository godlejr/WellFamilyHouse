package com.demand.well_family.well_family.settings;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.demand.well_family.well_family.connection.MainServerConnection;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.interceptor.HeaderInterceptor;
import com.demand.well_family.well_family.util.ErrorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private static final Logger logger = LoggerFactory.getLogger(ConfirmPasswordActivity.class);

    private MainServerConnection mainServerConnection;

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

    private void setToolbar(View view) {
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

    private void init() {
        et_confirm_email = (EditText) findViewById(R.id.et_confirm_email);
        et_confirm_pwd = (EditText) findViewById(R.id.et_confirm_pwd);
        btn_confirm_pwd = (Button) findViewById(R.id.btn_confirm_pwd);
        ll_search_pwd = (LinearLayout) findViewById(R.id.ll_search_pwd);

        btn_confirm_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_confirm_email.getText().toString();
                String password = et_confirm_pwd.getText().toString();

                if (email.length() == 0 || password.length() == 0) {
                    Toast.makeText(ConfirmPasswordActivity.this, "이메일과 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("email", email);
                    map.put("password", password);

                    mainServerConnection = new HeaderInterceptor().getClientForMainServer().create(MainServerConnection.class);
                    Call<User> call_login = mainServerConnection.login(map);
                    call_login.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful()) {
                                if (response.body() != null) {
                                    Intent intent = new Intent(ConfirmPasswordActivity.this, SearchAccountActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(ConfirmPasswordActivity.this, "이메일과 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(ConfirmPasswordActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            log(t);
                            Toast.makeText(ConfirmPasswordActivity.this, "네트워크가 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                        }
                    });


                }
            }
        });

        ll_search_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 비밀번호 찾기
                Intent intent = new Intent(v.getContext(), SearchAccountActivity.class);
                startActivity(intent);
                finish();
            }
        });

        et_confirm_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_confirm_email.getText().length() != 0 && s.length() != 0) {
                    btn_confirm_pwd.setBackgroundResource(R.drawable.round_corner_btn_brown_r10);
                } else {
                    btn_confirm_pwd.setBackgroundResource(R.drawable.round_corner_btn_gray_r10);
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
