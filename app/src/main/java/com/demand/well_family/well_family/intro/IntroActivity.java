package com.demand.well_family.well_family.intro;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.demand.well_family.well_family.LoginActivity;
import com.demand.well_family.well_family.MainActivity;
import com.demand.well_family.well_family.R;

/**
 * Created by ㅇㅇ on 2017-01-25.
 */

public class IntroActivity extends Activity {
    private SharedPreferences loginInfo;
    private String email, name, birth, avatar,phone;
    private int id, level;


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
                name = loginInfo.getString("user_name", null);
                email = loginInfo.getString("user_email", null);
                birth = loginInfo.getString("user_birth", null);
                avatar = loginInfo.getString("user_avatar", null);
                phone = loginInfo.getString("user_phone", null);

                if (email != null && name != null) {
                    Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                    intent.putExtra("user_id", id);
                    intent.putExtra("user_email", email);
                    intent.putExtra("user_birth", birth);
                    intent.putExtra("user_phone", phone);
                    intent.putExtra("user_name", name);
                    intent.putExtra("user_level", level);
                    intent.putExtra("user_avatar", avatar);
                    startActivity(intent);
                    finish();
                } else {
                    Intent mainIntent = new Intent(IntroActivity.this, LoginActivity.class);
                    IntroActivity.this.startActivity(mainIntent);
                    IntroActivity.this.finish();
                }
            }
        }, 2000);
    }
}
