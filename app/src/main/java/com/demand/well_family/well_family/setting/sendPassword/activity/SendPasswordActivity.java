package com.demand.well_family.well_family.setting.sendPassword.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.Toolbar;
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
import com.demand.well_family.well_family.setting.sendPassword.presenter.SendPasswordPresenter;
import com.demand.well_family.well_family.setting.sendPassword.presenter.impl.SendPasswordPresenterImpl;
import com.demand.well_family.well_family.setting.sendPassword.view.SendPasswordView;

import static com.demand.well_family.well_family.main.login.activity.LoginActivity.finishList;

/**
 * Created by ㅇㅇ on 2017-03-07.
 */

public class SendPasswordActivity extends Activity implements SendPasswordView, View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private SendPasswordPresenter findAccountPresenter;

    private TextView tv_confirm_account_name;
    private TextView tv_confirm_account_email;
    private ImageView iv_confirm_account_avatar;
    private RadioButton rb_confirm_account_email;
    private Button btn_confirm_account;
    private RadioGroup rg_confirm_account;
    private RadioButton rb_send_email;
    private Toolbar toolbar;
    private ImageView toolbar_back;
    private TextView toolbar_title;
    private View decorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_account);

        findAccountPresenter = new SendPasswordPresenterImpl(this);
        findAccountPresenter.onCreate();
    }

    @Override
    public View getDecorView() {
        if (decorView == null) {
            decorView = this.getWindow().getDecorView();
        }
        return decorView;
    }

    @Override
    public void setToolbar(View decorView) {
        toolbar = (Toolbar) decorView.findViewById(R.id.toolBar);
        toolbar_back = (ImageView) toolbar.findViewById(R.id.toolbar_back);
        toolbar_back.setOnClickListener(this);

        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
    }

    @Override
    public void showToolbarTitle(String message) {
        toolbar_title.setText(message);
    }

    @Override
    public void init() {
        finishList.add(this);

        tv_confirm_account_name = (TextView) findViewById(R.id.tv_confirm_account_name);
        tv_confirm_account_email = (TextView) findViewById(R.id.tv_confirm_account_email);
        iv_confirm_account_avatar = (ImageView) findViewById(R.id.iv_confirm_account_avatar);

        rg_confirm_account = (RadioGroup) findViewById(R.id.rg_confirm_account);
        rb_confirm_account_email = (RadioButton) findViewById(R.id.rb_confirm_account_email);
        btn_confirm_account = (Button) findViewById(R.id.btn_confirm_account);


        rg_confirm_account.setOnCheckedChangeListener(this);
    }

    @Override
    public void setUserInfo() {
        String name = getIntent().getStringExtra("name");
        String user_avatar = getIntent().getStringExtra("avatar");
        String email = getIntent().getStringExtra("email");

        tv_confirm_account_email.setText(email);
        tv_confirm_account_name.setText(name);
        Glide.with(SendPasswordActivity.this).load(getString(R.string.cloud_front_user_avatar) + user_avatar).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_confirm_account_avatar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back:
                finish();
                break;

            case R.id.btn_confirm_account:
                int user_id = getIntent().getIntExtra("user_id", 0);
                String name = getIntent().getStringExtra("name");
                String email = getIntent().getStringExtra("email");
                findAccountPresenter.sendEmail(user_id, name, email);
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        rb_send_email = (RadioButton) findViewById(checkedId);
        rb_send_email.setSelected(true);

        btn_confirm_account.setBackgroundResource(R.drawable.round_corner_btn_brown_r10);
        btn_confirm_account.setOnClickListener(this);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(SendPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToBack() {
        finish();
    }
}
