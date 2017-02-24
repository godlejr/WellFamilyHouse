package com.demand.well_family.well_family.register;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.demand.well_family.well_family.LoginActivity;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.connection.Server_Connection;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.log.LogFlag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ㅇㅇ on 2017-01-17.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_join_email, et_join_pwd, et_join_pwd_check, et_join_name, et_join_birthday, et_join_phone;
    private Button btn_join, btn_email_check;
    private LinearLayout noti_email, noti_pwd, noti_pwd_check, noti_name, noti_birthday, noti_phone;

    private String dateStr;
    private String birthDate;
    private String et_email;
    private boolean email_duplicate_check = false;

    private Server_Connection server_connection;

    private static final Logger logger = LoggerFactory.getLogger(RegisterActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
        setToolbar(getWindow().getDecorView());
    }

    private void init() {
        et_join_email = (EditText) findViewById(R.id.et_join_email);
        et_join_pwd = (EditText) findViewById(R.id.et_join_password);
        et_join_pwd_check = (EditText) findViewById(R.id.et_join_password_check);
        et_join_name = (EditText) findViewById(R.id.et_join_name);
        et_join_birthday = (EditText) findViewById(R.id.et_join_birthday);
        et_join_phone = (EditText) findViewById(R.id.et_join_phone);
        btn_join = (Button) findViewById(R.id.btn_join);
        btn_email_check = (Button) findViewById(R.id.btn_join_email_check);

        noti_email = (LinearLayout) findViewById(R.id.join_noti_email);
        noti_birthday = (LinearLayout) findViewById(R.id.join_noti_birthday);
        noti_name = (LinearLayout) findViewById(R.id.join_noti_name);
        noti_phone = (LinearLayout) findViewById(R.id.join_noti_phone);
        noti_pwd = (LinearLayout) findViewById(R.id.join_noti_password);
        noti_pwd_check = (LinearLayout) findViewById(R.id.join_noti_password_check);

        et_join_birthday.setOnClickListener(this);
        btn_join.setOnClickListener(this);
        btn_email_check.setOnClickListener(this);

    }

    // toolbar_main & menu
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
        toolbar_title.setText("회원 가입");

    }
    public void setBack() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_join_birthday:
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePicker = new DatePickerDialog(v.getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        dateStr = String.valueOf(year) + "-" + String.valueOf((month + 1)) + "-" + String.valueOf(dayOfMonth);
                        try {
                            Date date = formatter.parse(dateStr);
                            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
                            birthDate = transFormat.format(date);
                            et_join_birthday.setText(birthDate);
                        } catch (ParseException e) {
                            log(e);
                        }


                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)
                        , calendar.get(Calendar.DAY_OF_MONTH));

                datePicker.show();

                break;
            case R.id.btn_join:
                if (!(setNotiVisible(et_join_email, noti_email) && setNotiVisible(et_join_pwd, noti_pwd) && setNotiVisible(et_join_pwd_check, noti_pwd_check) && setNotiVisible(et_join_name, noti_name)
                        && setNotiVisible(et_join_birthday, noti_birthday) && setNotiVisible(et_join_phone, noti_phone))) {
                    break;
                }

                if (!email_duplicate_check) {
                    Toast.makeText(RegisterActivity.this, "아이디 중복확인을 해주세요.", Toast.LENGTH_LONG).show();
                    break;
                }

                if (!et_join_pwd.getText().toString().equals(et_join_pwd_check.getText().toString())) {
                    noti_pwd_check.setVisibility(View.VISIBLE);
                    break;
                } else {
                    noti_pwd_check.setVisibility(View.GONE);
                }

                if (!checkPassword(et_join_pwd.getText().toString())) {
                    Toast.makeText(v.getContext(), "비밀번호는 6~20자 대소문자, 숫자/특수문자를 포함해야합니다.", Toast.LENGTH_SHORT).show();
                } else {

                    HashMap<String, String> map = new HashMap<>();
                    map.put("email", et_join_email.getText().toString());
                    map.put("password", et_join_pwd.getText().toString());
                    map.put("name", et_join_name.getText().toString());
                    map.put("birth", et_join_birthday.getText().toString());
                    map.put("phone", et_join_phone.getText().toString());

                    server_connection = Server_Connection.retrofit.create(Server_Connection.class);
                    Call<ResponseBody> call = server_connection.join(1,map);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            Toast.makeText(RegisterActivity.this, "회원가입이 되었습니다.", Toast.LENGTH_LONG).show();
                            finish();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            log(t);
                            Toast.makeText(RegisterActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                break;

            case R.id.btn_join_email_check:
                et_email = et_join_email.getText().toString();
                if(!setNotiVisible(et_join_email, noti_email)) {
                } else if (!checkEmail(et_email)) {
                    Toast.makeText(v.getContext(), "올바른 이메일 형식을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    if (setNotiVisible(et_join_email, noti_email)) {

                        HashMap<String, String> mapEmail = new HashMap<>();
                        mapEmail.put("email", et_join_email.getText().toString());

                        server_connection = Server_Connection.retrofit.create(Server_Connection.class);
                        Call<ArrayList<User>> call_email_check  =  server_connection.email_check(mapEmail);
                        call_email_check.enqueue(new Callback<ArrayList<User>>() {
                            @Override
                            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                                if(response.body().size() == 0){
                                    Toast.makeText(RegisterActivity.this, "사용가능한 아이디입니다.", Toast.LENGTH_SHORT).show();
                                    email_duplicate_check = true;
                                } else{
                                    Toast.makeText(RegisterActivity.this, "사용중인 아이디입니다.", Toast.LENGTH_SHORT).show();
                                    email_duplicate_check = false;
                                }
                            }
                            @Override
                            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                                log(t);
                                Toast.makeText(RegisterActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                }
                break;
        }
    }

    private boolean setNotiVisible(EditText et, LinearLayout noti) {
        if (et.getText().toString().trim().length() == 0) {
            noti.setVisibility(View.VISIBLE);
            return false;
        } else {
            noti.setVisibility(View.GONE);
            return true;
        }
    }

    public boolean checkEmail(String email) {
        String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        boolean isNormal = matcher.matches();
        return isNormal;
    }

    public boolean checkPassword(String password) {
        String regex = "^(?=.*[a-zA-Z])((?=.*\\d)|(?=.*\\W)).{6,20}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        boolean isNormal = matcher.matches();
        return isNormal;
    }

    private static void log(Throwable throwable){
        StackTraceElement[] ste =  throwable.getStackTrace();
        String className = ste[0].getClassName();
        String methodName = ste[0].getMethodName();
        int lineNumber = ste[0].getLineNumber();
        String fileName = ste[0].getFileName();

        if(LogFlag.printFlag){
            if(logger.isInfoEnabled()){
                logger.info("Exception: " + throwable.getMessage());
                logger.info(className + "."+ methodName+" "+ fileName +" "+ lineNumber +" "+ "line" );
            }
        }
    }

}
