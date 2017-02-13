package com.demand.well_family.well_family;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.demand.well_family.well_family.connection.Server_Connection;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.register.AgreementActivity;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button login;
    private EditText email, pwd;
    private Button register;

    private String et_email;
    private String et_pwd;

    private SharedPreferences loginInfo;
    private SharedPreferences.Editor editor;
    private Server_Connection server_connection;

    public static ArrayList<Activity> finishList = new ArrayList<Activity>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        finishList.add(this);

        init();
    }

    private void init() {
        login = (Button) findViewById(R.id.btn_main_login);
        register = (Button) findViewById(R.id.btn_main_register);
        email = (EditText) findViewById(R.id.et_login_email);
        pwd = (EditText) findViewById(R.id.et_login_pwd);

        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_main_login:
                et_email = email.getText().toString();
                et_pwd = pwd.getText().toString();

                server_connection = Server_Connection.retrofit.create(Server_Connection.class);
                HashMap<String, String> map = new HashMap<>();
                map.put("email", et_email);
                map.put("password", et_pwd);
                Call<ArrayList<User>> call = server_connection.login(map);

                call.enqueue(new Callback<ArrayList<User>>() {
                    @Override
                    public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                        ArrayList<User> userList = response.body();

                        if (userList.size() == 0) {
                            Toast.makeText(LoginActivity.this, "ID / 패스워드를 확인해주세요.", Toast.LENGTH_LONG).show();
                        } else {
                            loginInfo = getSharedPreferences("loginInfo", Activity.MODE_PRIVATE);
                            editor = loginInfo.edit();
                            editor.putInt("user_id", userList.get(0).getId());
                            editor.putString("user_name",  userList.get(0).getName());
                            editor.putString("user_email",  userList.get(0).getEmail());
                            editor.putString("user_birth",  userList.get(0).getBirth());
                            editor.putString("user_avatar",  userList.get(0).getAvatar());
                            editor.putString("user_phone",  userList.get(0).getPhone());
                            editor.putInt("user_level",  userList.get(0).getLevel());
                            editor.commit();


                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("user_id" , userList.get(0).getId());
                            intent.putExtra("user_email",  userList.get(0).getEmail());
                            intent.putExtra("user_birth", userList.get(0).getBirth());
                            intent.putExtra("user_phone", userList.get(0).getPhone());
                            intent.putExtra("user_name",  userList.get(0).getName());
                            intent.putExtra("user_level",  userList.get(0).getLevel());
                            intent.putExtra("user_avatar", userList.get(0).getAvatar());
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case R.id.btn_main_register:
                Intent intent = new Intent(v.getContext(), AgreementActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        for(int i = 0; i < finishList.size(); i++) {
            finishList.get(i).finish();
        }

        super.onBackPressed();
    }
}
