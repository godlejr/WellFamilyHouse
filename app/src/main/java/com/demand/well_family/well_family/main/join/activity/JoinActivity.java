package com.demand.well_family.well_family.main.join.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.demand.well_family.well_family.main.login.activity.LoginActivity;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.main.join.presenter.JoinPresenter;
import com.demand.well_family.well_family.main.join.presenter.impl.JoinPresenterImpl;
import com.demand.well_family.well_family.main.join.view.JoinView;

import java.util.Calendar;

/**
 * Created by ㅇㅇ on 2017-01-17.
 */

public class JoinActivity extends AppCompatActivity implements JoinView, View.OnClickListener {
    private JoinPresenter joinPresenter;

    private EditText et_join_email, et_join_password, et_join_password_check, et_join_name, et_join_birthday, et_join_phone;
    private Button btn_join, btn_join_email_check;
    private LinearLayout join_noti_email, join_noti_password, join_noti_password_check, join_noti_name, join_noti_birthday, join_noti_phone;


    //toolbar
    private Toolbar toolbar;
    private ImageView toolbar_back;
    private TextView toolbar_title;
    private View decorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        joinPresenter = new JoinPresenterImpl(this);
        joinPresenter.onCreate();

    }

    @Override
    public void init() {
        et_join_email = (EditText) findViewById(R.id.et_join_email);
        et_join_password = (EditText) findViewById(R.id.et_join_password);
        et_join_password_check = (EditText) findViewById(R.id.et_join_password_check);
        et_join_name = (EditText) findViewById(R.id.et_join_name);
        et_join_birthday = (EditText) findViewById(R.id.et_join_birthday);
        et_join_phone = (EditText) findViewById(R.id.et_join_phone);
        btn_join = (Button) findViewById(R.id.btn_join);
        btn_join_email_check = (Button) findViewById(R.id.btn_join_email_check);

        join_noti_email = (LinearLayout) findViewById(R.id.join_noti_email);
        join_noti_birthday = (LinearLayout) findViewById(R.id.join_noti_birthday);
        join_noti_name = (LinearLayout) findViewById(R.id.join_noti_name);
        join_noti_phone = (LinearLayout) findViewById(R.id.join_noti_phone);
        join_noti_password = (LinearLayout) findViewById(R.id.join_noti_password);
        join_noti_password_check = (LinearLayout) findViewById(R.id.join_noti_password_check);

        et_join_birthday.setOnClickListener(this);
        btn_join.setOnClickListener(this);
        btn_join_email_check.setOnClickListener(this);

        joinPresenter.setNameFilterCallback();
    }

    @Override
    public void setToolbar(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolBar);
        toolbar_back = (ImageView) toolbar.findViewById(R.id.toolbar_back);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);

        toolbar_back.setOnClickListener(this);
    }

    @Override
    public void setNameFilterCallback(InputFilter[] inputFilter) {
        et_join_name.setFilters(inputFilter);
    }

    @Override
    public View getDecorView() {
        if (decorView == null) {
            decorView = this.getWindow().getDecorView();
        }
        return decorView;
    }

    @Override
    public void showDatePicker(Calendar calendar) {
        DatePickerDialog datePicker = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String birth = String.valueOf(year) + "-" + String.valueOf((month + 1)) + "-" + String.valueOf(dayOfMonth);
                joinPresenter.setUserBirth(birth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH));

        datePicker.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        datePicker.show();
    }

    @Override
    public void showBirth(String birth) {
        et_join_birthday.setText(birth);
    }

    @Override
    public void showEmailCheckNotification() {
        join_noti_email.setVisibility(View.VISIBLE);
    }

    @Override
    public void goneEmailCheckNotification() {
        join_noti_email.setVisibility(View.GONE);
    }

    @Override
    public void showPasswordCheckNotification() {
        join_noti_password.setVisibility(View.VISIBLE);
    }

    @Override
    public void gonePasswordCheckNotification() {
        join_noti_password.setVisibility(View.GONE);
    }

    @Override
    public void showPasswordConfirmCheckNotification() {
        join_noti_password_check.setVisibility(View.VISIBLE);
    }

    @Override
    public void gonePasswordConfirmCheckNotification() {
        join_noti_password_check.setVisibility(View.GONE);
    }

    @Override
    public void showNameCheckNotification() {
        join_noti_name.setVisibility(View.VISIBLE);
    }

    @Override
    public void goneNameCheckNotification() {
        join_noti_name.setVisibility(View.GONE);
    }

    @Override
    public void showBirthCheckNotification() {
        join_noti_birthday.setVisibility(View.VISIBLE);
    }

    @Override
    public void goneBirthCheckNotification() {
        join_noti_birthday.setVisibility(View.GONE);
    }

    @Override
    public void showPhoneCheckNotification() {
        join_noti_phone.setVisibility(View.VISIBLE);
    }

    @Override
    public void gonePhoneCheckNotification() {
        join_noti_phone.setVisibility(View.GONE);
    }


    @Override
    public void showToolbarTitle(String message) {
        toolbar_title.setText(message);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToBack() {
        finish();
    }

    @Override
    public void navigateToLoginActivity() {
        Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_join_birthday:
                joinPresenter.setCalendar();
                break;
            case R.id.btn_join:
                String email = et_join_email.getText().toString();
                String password = et_join_password.getText().toString();
                String passwordConfirm = et_join_password_check.getText().toString();
                String name = et_join_name.getText().toString();
                String birth = et_join_birthday.getText().toString();
                String phone = et_join_phone.getText().toString();

                joinPresenter.onClickJoin(email, password, passwordConfirm, name, birth, phone);
                break;

            case R.id.btn_join_email_check:
                joinPresenter.onClickEmailCheck(et_join_email.getText().toString());
                break;

            case R.id.toolbar_back:
                navigateToBack();
                break;
        }
    }




}
