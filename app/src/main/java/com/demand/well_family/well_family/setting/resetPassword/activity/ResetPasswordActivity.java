package com.demand.well_family.well_family.setting.resetPassword.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.setting.resetPassword.presenter.ResetPasswordPresenter;
import com.demand.well_family.well_family.setting.resetPassword.presenter.impl.ResetPasswordPresenterImpl;
import com.demand.well_family.well_family.setting.resetPassword.view.ResetPasswordView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.demand.well_family.well_family.main.login.activity.LoginActivity.finishList;

/**
 * Created by ㅇㅇ on 2017-03-09.
 */

public class ResetPasswordActivity extends Activity implements ResetPasswordView, View.OnClickListener, TextWatcher {
    private ResetPasswordPresenter resetPasswordPresenter;

    private EditText et_reset_pwd1;
    private EditText et_reset_pwd2;
    private Button btn_reset_pwd;
    private Toolbar toolbar;
    private ImageView toolbar_back;
    private TextView toolbar_title;
    private View decorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        resetPasswordPresenter = new ResetPasswordPresenterImpl(this);
        resetPasswordPresenter.onCreate();
    }

    @Override
    public void init() {
        finishList.add(this);

        et_reset_pwd1 = (EditText) findViewById(R.id.et_reset_pwd1);
        et_reset_pwd2 = (EditText) findViewById(R.id.et_reset_pwd2);
        btn_reset_pwd = (Button) findViewById(R.id.btn_reset_pwd);

        btn_reset_pwd.setOnClickListener(this);
        et_reset_pwd2.addTextChangedListener(this);
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
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_back.setOnClickListener(this);
    }

    @Override
    public void showToolbarTitle(String message) {
        toolbar_title.setText(message);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back:
                finish();
                break;

            case R.id.btn_reset_pwd:
                String password1 = et_reset_pwd1.getText().toString();
                String password2 = et_reset_pwd2.getText().toString();
                resetPasswordPresenter.setPassword(password1, password2);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //none
    }

    @Override
    public void afterTextChanged(Editable s) {
        //none
    }

    @Override
    public void onTextChanged(CharSequence password, int start, int before, int count) {
        resetPasswordPresenter.onTextChanged(password);
    }

    @Override
    public void setButtonColorBrown() {
        btn_reset_pwd.setBackgroundResource(R.drawable.round_corner_btn_brown_r10);
    }

    @Override
    public void setButtonColorGray() {
        btn_reset_pwd.setBackgroundResource(R.drawable.round_corner_btn_gray_r10);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(ResetPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToBack() {
        finish();
    }
}
