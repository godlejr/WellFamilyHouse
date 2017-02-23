package com.demand.well_family.well_family.register;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.demand.well_family.well_family.MainActivity;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.connection.Server_Connection;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.log.LogFlag;
import com.google.firebase.iid.FirebaseInstanceId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public class SNSRegisterActivity extends Activity implements View.OnClickListener {
    private EditText et_sns_phone;
    private Button btn_sns_register;
    private EditText et_sns_birth;
    private LinearLayout ll_sns_noti_birth;
    private LinearLayout ll_sns_noti_phone;

    private String email;
    private String password;
    private String name;

    private Server_Connection server_connection;

    private static final Logger logger = LoggerFactory.getLogger(SNSRegisterActivity.class);
    private SharedPreferences loginInfo;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sns_register);
        init();
    }

    private void init() {
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        name = getIntent().getStringExtra("name");

        btn_sns_register = (Button) findViewById(R.id.btn_sns_register);
        et_sns_birth = (EditText) findViewById(R.id.et_sns_birth);
        et_sns_phone = (EditText) findViewById(R.id.et_sns_phone);

        ll_sns_noti_birth = (LinearLayout) findViewById(R.id.ll_sns_noti_birth);
        ll_sns_noti_phone = (LinearLayout) findViewById(R.id.ll_sns_noti_phone);

        et_sns_birth.setOnClickListener(this);
        btn_sns_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_sns_birth:
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePicker = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
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
                    map.put("phone", phone);

                    server_connection = Server_Connection.retrofit.create(Server_Connection.class);
                    Call<ResponseBody> call_join = server_connection.join(map);
                    call_join.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            server_connection = Server_Connection.retrofit.create(Server_Connection.class);
                            HashMap<String, String> map = new HashMap<>();
                            map.put("email", email);
                            map.put("password", password);
                            Call<ArrayList<User>> call_login = server_connection.login(map);
                            call_login.enqueue(new Callback<ArrayList<User>>() {
                                @Override
                                public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                                    final ArrayList<User> userList = response.body();

                                    final String device_id = getDeviceId();
                                    final String token = FirebaseInstanceId.getInstance().getToken();

                                    server_connection = Server_Connection.retrofit.create(Server_Connection.class);
                                    HashMap<String, String> map = new HashMap<>();
                                    map.put("device_id", device_id);
                                    map.put("token", token);
                                    Call<ResponseBody> call_update_deviceId_token = server_connection.update_deviceId_token(userList.get(0).getId(), map);

                                    call_update_deviceId_token.enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                            loginInfo = getSharedPreferences("loginInfo", Activity.MODE_PRIVATE);
                                            editor = loginInfo.edit();
                                            editor.putInt("user_id", userList.get(0).getId());
                                            editor.putString("user_name", userList.get(0).getName());
                                            editor.putString("user_email", userList.get(0).getEmail());
                                            editor.putString("user_birth", userList.get(0).getBirth());
                                            editor.putString("user_avatar", userList.get(0).getAvatar());
                                            editor.putString("user_phone", userList.get(0).getPhone());
                                            editor.putString("device_id", device_id);
                                            editor.putString("token", token);
                                            editor.putInt("user_level", userList.get(0).getLevel());
                                            editor.commit();

                                            Intent intent = new Intent(SNSRegisterActivity.this, MainActivity.class);
                                            intent.putExtra("user_id", userList.get(0).getId());
                                            intent.putExtra("user_email", userList.get(0).getEmail());
                                            intent.putExtra("user_birth", userList.get(0).getBirth());
                                            intent.putExtra("user_phone", userList.get(0).getPhone());
                                            intent.putExtra("user_name", userList.get(0).getName());
                                            intent.putExtra("user_level", userList.get(0).getLevel());
                                            intent.putExtra("user_avatar", userList.get(0).getAvatar());
                                            startActivity(intent);
                                            finish();
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            log(t);
                                            Toast.makeText(SNSRegisterActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }

                                @Override
                                public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                                    log(t);
                                    Toast.makeText(SNSRegisterActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            log(t);
                            Toast.makeText(SNSRegisterActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
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
