package com.demand.well_family.well_family.dialog.popup.notification.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.demand.well_family.well_family.dialog.popup.notification.presenter.NotificationPopupPresenter;
import com.demand.well_family.well_family.dialog.popup.notification.presenter.impl.NotificationPopupPresenterImpl;
import com.demand.well_family.well_family.dialog.popup.notification.view.NotificationPopupView;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.main.login.activity.LoginActivity;

/**
 * Created by ㅇㅇ on 2017-04-11.
 */

public class NotificationPopupActivity extends Activity implements NotificationPopupView, View.OnClickListener {
    private NotificationPopupPresenter notificationPopupPresenter;

    private TextView tv_popup_notification_content;
    private Button btn_popup_notification_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_notification);

        notificationPopupPresenter = new NotificationPopupPresenterImpl(this);
        notificationPopupPresenter.onCreate();
    }

    @Override
    public void setDisplay() {
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        getWindow().getAttributes().width = (int) (display.getWidth() * 0.9);
        getWindow().getAttributes().height = (int) (display.getHeight() * 0.4);
    }

    @Override
    public void init() {
        tv_popup_notification_content = (TextView) findViewById(R.id.tv_popup_notification_content);
        btn_popup_notification_close = (Button) findViewById(R.id.btn_popup_notification_close);

        btn_popup_notification_close.setOnClickListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_popup_notification_close) {
            notificationPopupPresenter.onClickLogout();
        }
    }

    @Override
    public void setNotificationContent() {
        String message = getIntent().getStringExtra("content");
        tv_popup_notification_content.setText(message);
    }

    @Override
    public void navigateToLoginActivity() {
        Intent login_intent = new Intent(NotificationPopupActivity.this, LoginActivity.class);
        startActivity(login_intent);
    }

    @Override
    public void navigateToBack() {
        finish();
    }
}
