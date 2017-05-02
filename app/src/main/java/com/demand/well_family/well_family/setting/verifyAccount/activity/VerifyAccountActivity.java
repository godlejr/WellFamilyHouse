package com.demand.well_family.well_family.setting.verifyAccount.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.setting.searchAccount.activity.SearchAccountActivity;
import com.demand.well_family.well_family.setting.resetPassword.activity.ResetPasswordActivity;
import com.demand.well_family.well_family.setting.verifyAccount.presenter.VerifyAccountPresenter;
import com.demand.well_family.well_family.setting.verifyAccount.presenter.impl.VerifyAccountPresenterImpl;
import com.demand.well_family.well_family.setting.verifyAccount.view.VerifyAccountView;

import static com.demand.well_family.well_family.main.login.activity.LoginActivity.finishList;

/**
 * Created by ㅇㅇ on 2017-03-06.
 */

public class VerifyAccountActivity extends Activity implements VerifyAccountView, View.OnClickListener, TextWatcher {
    private VerifyAccountPresenter verifyAccountPresenter;

    private View decorView;
    private Toolbar toolbar;
    private ImageView toolbar_back;
    private TextView toolbar_title;

    private EditText et_verify_email;
    private EditText et_verify_pwd;
    private Button btn_verify_pwd;
    private LinearLayout ll_search_pwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_password);

        verifyAccountPresenter = new VerifyAccountPresenterImpl(this);
        verifyAccountPresenter.onCreate();
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
    public void init() {
        finishList.add(this);

        et_verify_email = (EditText) findViewById(R.id.et_confirm_email);
        et_verify_pwd = (EditText) findViewById(R.id.et_confirm_pwd);
        btn_verify_pwd = (Button) findViewById(R.id.btn_confirm_pwd);
        ll_search_pwd = (LinearLayout) findViewById(R.id.ll_search_pwd);

        btn_verify_pwd.setOnClickListener(this);
        ll_search_pwd.setOnClickListener(this);
        et_verify_pwd.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back:
                finish();
                break;

            case R.id.btn_confirm_pwd:
                User user = new User();
                user.setEmail(et_verify_email.getText().toString());
                user.setPassword( et_verify_pwd.getText().toString());

                verifyAccountPresenter.getUserInfo(user);
                break;

            case R.id.ll_search_pwd:
                navigateToFindAccountActivity();
                break;
        }
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(VerifyAccountActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToBack() {
        finish();
    }

    @Override
    public void navigateToFindAccountActivity() {
        Intent intent = new Intent(VerifyAccountActivity.this, SearchAccountActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToResetPasswordActivity() {
        Intent intent = new Intent(VerifyAccountActivity.this, ResetPasswordActivity.class);
        startActivity(intent);
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
    public void onTextChanged(CharSequence email, int start, int before, int count) {
        verifyAccountPresenter.onTextChanged(email);
    }

    @Override
    public void setButtonColorBrown() {
        btn_verify_pwd.setBackgroundResource(R.drawable.round_corner_brown_r10);
    }

    @Override
    public void setButtonColorGray() {
        btn_verify_pwd.setBackgroundResource(R.drawable.round_corner_gray_r10);
    }


}
