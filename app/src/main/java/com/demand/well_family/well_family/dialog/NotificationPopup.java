package com.demand.well_family.well_family.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.demand.well_family.well_family.LoginActivity;
import com.demand.well_family.well_family.R;

/**
 * Created by ㅇㅇ on 2017-04-11.
 */

public class NotificationPopup extends Activity {
    private TextView tv_popup_notification_content;
    private Button btn_popup_notification_close;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_notification);
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        getWindow().getAttributes().width = (int) (display.getWidth() * 0.9);
        getWindow().getAttributes().height = (int) (display.getHeight() * 0.4);

        init();
    }

    private void init() {
        tv_popup_notification_content = (TextView) findViewById(R.id.tv_popup_notification_content);
        btn_popup_notification_close = (Button) findViewById(R.id.btn_popup_notification_close);
        tv_popup_notification_content.setText(getIntent().getStringExtra("content"));

        btn_popup_notification_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
                finish();
            }
        });
    }

    private void logout() {
        SharedPreferences loginInfo = getSharedPreferences("loginInfo", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = loginInfo.edit();
        editor.remove("user_id");
        editor.remove("user_name");
        editor.remove("user_email");
        editor.remove("user_birth");
        editor.remove("user_avatar");
        editor.remove("user_phone");
        editor.remove("user_level");
        editor.remove("access_token");
        editor.commit();

        Intent logout_intent = new Intent(this, LoginActivity.class);
        startActivity(logout_intent);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return true;
    }
}
