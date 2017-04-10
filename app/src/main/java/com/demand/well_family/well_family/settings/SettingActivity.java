package com.demand.well_family.well_family.settings;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.connection.UserServerConnection;

/**
 * Created by ㅇㅇ on 2017-03-06.
 */

public class SettingActivity extends Activity {
    private Switch sw_setting_family;
    private LinearLayout ll_setting_disable;
    private LinearLayout ll_setting_pwd;

    //user_info
    private int user_id;
    private String user_name;
    private String user_avatar;
    private String user_email;
    private String user_birth;
    private String user_phone;
    private int user_level;
    private int login_category_id;

    private SharedPreferences loginInfo;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

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
        login_category_id = loginInfo.getInt("login_category_id", 0);
        setToolbar(getWindow().getDecorView());
    }

    private void setToolbar(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolBar);
        ImageView toolbar_back = (ImageView) toolbar.findViewById(R.id.toolbar_back);
        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText("설정");
    }

    private void init() {
        sw_setting_family = (Switch) findViewById(R.id.sw_setting_family);
        ll_setting_disable = (LinearLayout) findViewById(R.id.ll_setting_disable);
        ll_setting_pwd = (LinearLayout) findViewById(R.id.ll_setting_pwd);

        loginInfo = getSharedPreferences("loginInfo", Activity.MODE_PRIVATE);
        if (loginInfo.getBoolean("notification", true)) {
            sw_setting_family.setChecked(true);
        } else {
            sw_setting_family.setChecked(false);
        }

        sw_setting_family.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor = loginInfo.edit();
                if (buttonView.isChecked()) {
                    editor.putBoolean("notification", true);
                } else {
                    editor.putBoolean("notification", false);
                }

                editor.apply();
            }
        });

        ll_setting_disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AccountDisableActivity.class);
                startActivity(intent);
            }
        });


        if (login_category_id != 1) {
            ll_setting_pwd.setVisibility(View.GONE);
        } else {
            ll_setting_pwd.setVisibility(View.VISIBLE);
            ll_setting_pwd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ConfirmPasswordActivity.class);
                    startActivity(intent);
                }
            });
        }

    }

}
