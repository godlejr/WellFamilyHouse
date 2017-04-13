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

public class SearchAccountActivity extends Activity {
    //user_info
    private int user_id;
    private String user_email;
    private String user_name;
    private String user_birth;
    private String user_phone;
    private int user_level;
    private String user_avatar;
    private String access_token;

    private EditText et_reset_pwd;
    private Button btn_reset_pwd;

    private MainServerConnection mainServerConnection;
    private static final Logger logger = LoggerFactory.getLogger(SearchAccountActivity.class);
    private SharedPreferences loginInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

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
        setToolbar(this.getWindow().getDecorView());
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
        toolbar_title.setText("계정 찾기");
    }

    private void init() {
        et_reset_pwd = (EditText) findViewById(R.id.et_reset_pwd);
        btn_reset_pwd = (Button) findViewById(R.id.btn_reset_pwd);

        et_reset_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    btn_reset_pwd.setVisibility(View.VISIBLE);
                } else {
                    btn_reset_pwd.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_reset_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 계정 찾기
                String email = et_reset_pwd.getText().toString();

                // 유저 정보 찾기
                mainServerConnection = new HeaderInterceptor().getClientForMainServer().create(MainServerConnection.class);
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("email", email);
                Call<User> call_find_email = mainServerConnection.findEmail(map);
                call_find_email.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()) {
                            User user = response.body();
                            if (user == null) {
                                Toast.makeText(SearchAccountActivity.this, "이메일을 확인해주세요.", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(SearchAccountActivity.this, ConfirmAccountActivity.class);
                                intent.putExtra("user_id", user.getId());
                                intent.putExtra("name", user.getName());
                                intent.putExtra("avatar", user.getAvatar());
                                intent.putExtra("email", user.getEmail());

                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Toast.makeText(SearchAccountActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        log(t);
                        Toast.makeText(SearchAccountActivity.this, "네트워크가 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                    }
                });

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
