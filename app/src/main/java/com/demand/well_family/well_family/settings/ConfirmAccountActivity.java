package com.demand.well_family.well_family.settings;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;

/**
 * Created by ㅇㅇ on 2017-03-07.
 */

public class ConfirmAccountActivity extends Activity {
    //user_info
    private int user_id;
    private String user_name;
    private String user_avatar;
    private String user_email;
    private String user_birth;
    private String user_phone;
    private int user_level;
    private SharedPreferences loginInfo;

    private TextView tv_confirm_account_name;
    private TextView tv_confirm_account_email;
    private ImageView iv_confirm_account_avatar;
    private RadioButton rb_confirm_account_email;
    private Button btn_confirm_account;
    private RadioGroup rg_confirm_account;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_account);

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
        toolbar_title.setText("계정 확인");
    }

    private void init() {
        tv_confirm_account_name = (TextView) findViewById(R.id.tv_confirm_account_name);
        tv_confirm_account_email = (TextView) findViewById(R.id.tv_confirm_account_email);
        iv_confirm_account_avatar = (ImageView) findViewById(R.id.iv_confirm_account_avatar);

        rg_confirm_account = (RadioGroup) findViewById(R.id.rg_confirm_account);
        rb_confirm_account_email = (RadioButton) findViewById(R.id.rb_confirm_account_email);
        btn_confirm_account = (Button) findViewById(R.id.btn_confirm_account);

        tv_confirm_account_email.setText(user_email);
        tv_confirm_account_name.setText(user_name);
        Glide.with(ConfirmAccountActivity.this).load(getString(R.string.cloud_front_user_avatar) + user_avatar).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_confirm_account_avatar);


        rg_confirm_account.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton)findViewById(checkedId);
                rb.setSelected(true);

                if(rb.getId() == R.id.rb_confirm_account_email){
                    Toast.makeText(ConfirmAccountActivity.this, "이메일로 발송", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_confirm_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 임시 비밀번호 발송

            }
        });
    }
}
