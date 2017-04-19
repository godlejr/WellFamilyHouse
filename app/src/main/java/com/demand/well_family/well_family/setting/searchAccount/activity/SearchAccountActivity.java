package com.demand.well_family.well_family.setting.searchAccount.activity;

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
import android.widget.TextView;
import android.widget.Toast;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.setting.searchAccount.presenter.SearchAccountPresenter;
import com.demand.well_family.well_family.setting.searchAccount.presenter.impl.SearchAccountPresenterImpl;
import com.demand.well_family.well_family.setting.searchAccount.view.FindAccountView;
import com.demand.well_family.well_family.setting.sendPassword.activity.SendPasswordActivity;

import static com.demand.well_family.well_family.main.login.activity.LoginActivity.finishList;

/**
 * Created by ㅇㅇ on 2017-03-06.
 */

public class SearchAccountActivity extends Activity implements FindAccountView, View.OnClickListener, TextWatcher {
    private SearchAccountPresenter findAccountPresenter;

    private View decorView;
    private Toolbar toolbar;
    private ImageView toolbar_back;
    private TextView toolbar_title;
    private EditText et_reset_pwd;
    private Button btn_reset_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        findAccountPresenter = new SearchAccountPresenterImpl(this);
        findAccountPresenter.onCreate();
    }

    @Override
    public void init() {
        finishList.add(this);
        et_reset_pwd = (EditText) findViewById(R.id.et_reset_pwd);
        btn_reset_pwd = (Button) findViewById(R.id.btn_reset_pwd);

        et_reset_pwd.addTextChangedListener(this);
        btn_reset_pwd.setOnClickListener(this);
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
                String email = et_reset_pwd.getText().toString();
                findAccountPresenter.getUserInfoFromEmail(email);
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
        findAccountPresenter.onTextChanged(password);

    }

    @Override
    public void navigateToSendPasswordActivity(User user) {
        int userId = user.getId();
        String name = user.getName();
        String avatar = user.getAvatar();
        String email = user.getEmail();

        Intent intent = new Intent(SearchAccountActivity.this, SendPasswordActivity.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("name", name);
        intent.putExtra("avatar", avatar);
        intent.putExtra("email", email);
        startActivity(intent);
    }

    @Override
    public void navigateToBack() {
        finish();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(SearchAccountActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void gonePasswordResetButton() {
        btn_reset_pwd.setVisibility(View.GONE);
    }

    @Override
    public void showPasswordResetButton() {
        btn_reset_pwd.setVisibility(View.VISIBLE);
    }
}
