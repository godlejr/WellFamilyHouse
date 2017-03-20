package com.demand.well_family.well_family.intro;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.demand.well_family.well_family.LoginActivity;
import com.demand.well_family.well_family.MainActivity;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.connection.UserServerConnection;
import com.demand.well_family.well_family.interceptor.HeaderInterceptor;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.util.ErrorUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ㅇㅇ on 2017-01-25.
 */

public class IntroActivity extends Activity {
    private SharedPreferences loginInfo;
    private String email;
    private String name;
    private String birth;
    private String avatar;
    private String phone;
    private int id;
    private int level;
    private String device_id;
    private String token;
    private String access_token;
    private UserServerConnection userServerConnection;

    private static final Logger logger = LoggerFactory.getLogger(IntroActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loginInfo = getSharedPreferences("loginInfo", Activity.MODE_PRIVATE);
                id = loginInfo.getInt("user_id", 0);
                level = loginInfo.getInt("user_level", 0);
                token = loginInfo.getString("token", null);
                device_id = loginInfo.getString("device_id", null);
                access_token = loginInfo.getString("access_token", null);

                if (id != 0) {
//                    userServerConnection = UserServerConnection.retrofit.create(UserServerConnection.class);
                    userServerConnection = new HeaderInterceptor(access_token).getClientForUserServer().create(UserServerConnection.class);
//                    HashMap<String, String> map = new HashMap<String, String>();
//                    map.put("device_id", device_id);

                    Call<Integer> call_device_check = userServerConnection.check_device_id(id, device_id);
                    call_device_check.enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            if (response.isSuccessful()) {
                                if (response.body() > 0) {
                                    Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(IntroActivity.this, "다른 기기에서 접속중입니다. 로그인 시 다른기기에서 강제 로그아웃 됩니다.", Toast.LENGTH_LONG);
                                    SharedPreferences.Editor editor = loginInfo.edit();
                                    editor.remove("user_id");
                                    editor.remove("user_name");
                                    editor.remove("user_email");
                                    editor.remove("user_birth");
                                    editor.remove("user_avatar");
                                    editor.remove("user_phone");
                                    editor.remove("user_level");
                                    editor.remove("device_id");
                                    editor.remove("access_token");
                                    editor.commit();

                                    Intent mainIntent = new Intent(IntroActivity.this, LoginActivity.class);
                                    IntroActivity.this.startActivity(mainIntent);
                                    IntroActivity.this.finish();
                                }
                            } else {
                                Toast.makeText(IntroActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
                            log(t);
                            Toast.makeText(IntroActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                        }
                    });

                } else {
                    Intent mainIntent = new Intent(IntroActivity.this, LoginActivity.class);
                    IntroActivity.this.startActivity(mainIntent);
                    IntroActivity.this.finish();
                }
            }
        }, 2000);
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
