package com.demand.well_family.well_family.register;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.demand.well_family.well_family.main.base.activity.MainActivity;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.connection.MainServerConnection;
import com.demand.well_family.well_family.connection.UserServerConnection;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.flag.JoinFlag;
import com.demand.well_family.well_family.interceptor.HeaderInterceptor;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.util.ErrorUtil;
import com.google.firebase.iid.FirebaseInstanceId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dev-0 on 2017-02-23.
 */

public class JoinFromSNSActivity extends Activity implements View.OnClickListener {
    private EditText et_sns_phone;
    private Button btn_sns_register;
    private EditText et_sns_birth;
    private LinearLayout ll_sns_noti_birth;
    private LinearLayout ll_sns_noti_phone;

    private String email;
    private String password;
    private String name;
    private int login_category_id;
    private String access_token;

    private MainServerConnection mainServerConnection;
    private UserServerConnection userServerConnection;

    private static final Logger logger = LoggerFactory.getLogger(JoinFromSNSActivity.class);
    private SharedPreferences loginInfo;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sns_register);
        init();
        setToolbar(getWindow().getDecorView());
    }

    private void init() {
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        name = getIntent().getStringExtra("name");
        login_category_id = getIntent().getIntExtra("login_category_id", JoinFlag.DEMAND);

        btn_sns_register = (Button) findViewById(R.id.btn_sns_register);
        et_sns_birth = (EditText) findViewById(R.id.et_sns_birth);
        et_sns_phone = (EditText) findViewById(R.id.et_sns_phone);

        ll_sns_noti_birth = (LinearLayout) findViewById(R.id.ll_sns_noti_birth);
        ll_sns_noti_phone = (LinearLayout) findViewById(R.id.ll_sns_noti_phone);

        et_sns_birth.setOnClickListener(this);
        btn_sns_register.setOnClickListener(this);
    }

    public void setToolbar(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolBar);
        ImageView toolbar_back = (ImageView) toolbar.findViewById(R.id.toolbar_back);
        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBack();
            }
        });

        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText(" ");
    }

    public void setBack() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_sns_birth:
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePicker = new DatePickerDialog(v.getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        String dateStr = String.valueOf(year) + "-" + String.valueOf((month + 1)) + "-" + String.valueOf(dayOfMonth);
                        try {
                            Date date = formatter.parse(dateStr);
                            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
                            String birthDate = transFormat.format(date);
                            et_sns_birth.setText(birthDate);
                        } catch (ParseException e) {
                            log(e);
                        }
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)
                        , calendar.get(Calendar.DAY_OF_MONTH));
                datePicker.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                datePicker.show();

                break;
            case R.id.btn_sns_register:
                String phone = et_sns_phone.getText().toString();
                String birth = et_sns_birth.getText().toString();
                if (birth.equals("") || birth.length() == 0) {
                    ll_sns_noti_birth.setVisibility(View.VISIBLE);
                    break;
                } else if (phone.equals("") || phone.length() == 0) {
                    ll_sns_noti_phone.setVisibility(View.VISIBLE);
                    break;
                } else {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("email", email);
                    map.put("password", password);
                    map.put("name", name);
                    map.put("birth", birth);
                    map.put("profile_phone", phone);
                    map.put("login_category_id", String.valueOf(login_category_id));

                    mainServerConnection = new HeaderInterceptor().getClientForMainServer().create(MainServerConnection.class);
                    Call<ResponseBody> call_join = mainServerConnection.join(map);
                    call_join.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                HashMap<String, String> map = new HashMap<>();
                                map.put("email", email);
                                map.put("password", password);

                                mainServerConnection = new HeaderInterceptor().getClientForMainServer().create(MainServerConnection.class);
                                Call<User> call_login = mainServerConnection.login(map);
                                call_login.enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User> response) {
                                        if (response.isSuccessful()) {
                                            final User userInfo = response.body();

                                            final String device_id = getDeviceId();
                                            final String token = FirebaseInstanceId.getInstance().getToken();

                                            access_token = userInfo.getAccess_token();
                                            userServerConnection = new HeaderInterceptor(access_token).getClientForUserServer().create(UserServerConnection.class);
                                            Call<ResponseBody> call_update_deviceId_token = userServerConnection.update_deviceId_token(userInfo.getId(), token, device_id);

                                            call_update_deviceId_token.enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    if (response.isSuccessful()) {
                                                        loginInfo = getSharedPreferences("loginInfo", Activity.MODE_PRIVATE);
                                                        editor = loginInfo.edit();
                                                        editor.putInt("user_id", userInfo.getId());
                                                        editor.putString("user_name", userInfo.getName());
                                                        editor.putString("user_email", userInfo.getEmail());
                                                        editor.putString("user_birth", userInfo.getBirth());
                                                        editor.putString("user_avatar", userInfo.getAvatar());
                                                        editor.putString("user_phone", userInfo.getPhone());
                                                        editor.putString("device_id", device_id);
                                                        editor.putString("token", token);
                                                        editor.putInt("user_level", userInfo.getLevel());
                                                        editor.putString("access_token", access_token);

                                                        editor.commit();

                                                        Intent intent = new Intent(JoinFromSNSActivity.this, MainActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        Toast.makeText(JoinFromSNSActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                    log(t);
                                                    Toast.makeText(JoinFromSNSActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        } else {
                                            Toast.makeText(JoinFromSNSActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<User> call, Throwable t) {
                                        log(t);
                                        Toast.makeText(JoinFromSNSActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                    }
                                });
                            } else {
                                Toast.makeText(JoinFromSNSActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            log(t);
                            Toast.makeText(JoinFromSNSActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                break;
        }
    }

    private String getDeviceId() {
        final String androidId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        logger.info(androidId);
        return androidId.toString();
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
