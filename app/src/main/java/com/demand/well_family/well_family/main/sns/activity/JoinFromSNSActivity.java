package com.demand.well_family.well_family.main.sns.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.flag.JoinFlag;
import com.demand.well_family.well_family.main.base.activity.MainActivity;
import com.demand.well_family.well_family.main.sns.presenter.JoinFromSNSPresenter;
import com.demand.well_family.well_family.main.sns.presenter.impl.JoinFromSNSPresenterImpl;
import com.demand.well_family.well_family.main.sns.view.JoinFromSNSView;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Calendar;

/**
 * Created by Dev-0 on 2017-02-23.
 */

public class JoinFromSNSActivity extends Activity implements JoinFromSNSView, View.OnClickListener {
    private JoinFromSNSPresenter joinFromSNSPresenter;

    private EditText et_sns_phone;
    private Button btn_sns_register;
    private EditText et_sns_birth;
    private LinearLayout ll_sns_noti_birth;
    private LinearLayout ll_sns_noti_phone;

    //toolbar
    private View decorView;
    private Toolbar toolbar;
    private ImageView toolbar_back;
    private TextView toolbar_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sns_register);

        joinFromSNSPresenter = new JoinFromSNSPresenterImpl(this);
        joinFromSNSPresenter.onCreate();
    }


    @Override
    public void init() {
        btn_sns_register = (Button) findViewById(R.id.btn_sns_register);
        et_sns_birth = (EditText) findViewById(R.id.et_sns_birth);
        et_sns_phone = (EditText) findViewById(R.id.et_sns_phone);

        ll_sns_noti_birth = (LinearLayout) findViewById(R.id.ll_sns_noti_birth);
        ll_sns_noti_phone = (LinearLayout) findViewById(R.id.ll_sns_noti_phone);

        et_sns_birth.setOnClickListener(this);
        btn_sns_register.setOnClickListener(this);
    }

    @Override
    public void setToolbar(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolBar);
        toolbar_back = (ImageView) toolbar.findViewById(R.id.toolbar_back);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_back.setOnClickListener(this);
    }

    @Override
    public View getDecorView() {
        if (decorView == null) {
            decorView = this.getWindow().getDecorView();
        }
        return decorView;
    }

    @Override
    public void showToolbarTitle(String message) {
        toolbar_title.setText(message);
    }

    @Override
    public void showDatePicker(Calendar calendar) {
        DatePickerDialog datePicker = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String birth = String.valueOf(year) + "-" + String.valueOf((month + 1)) + "-" + String.valueOf(dayOfMonth);
                joinFromSNSPresenter.setUserBirth(birth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH));

        datePicker.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        datePicker.show();
    }

    @Override
    public void showBrith(String birth) {
        et_sns_birth.setText(birth);
    }

    @Override
    public void showBirthCheckNotification() {
        ll_sns_noti_birth.setVisibility(View.VISIBLE);
    }

    @Override
    public void goneBirthCheckNotification() {
        ll_sns_noti_birth.setVisibility(View.GONE);
    }

    @Override
    public void showPhoneCheckNotification() {
        ll_sns_noti_phone.setVisibility(View.VISIBLE);
    }

    @Override
    public void gonePhoneCheckNotification() {
        ll_sns_noti_phone.setVisibility(View.GONE);
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
    public void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back:
                navigateToBack();
                break;

            case R.id.et_sns_birth:
                joinFromSNSPresenter.setCalendar();
                break;

            case R.id.btn_sns_register:
                String email = getIntent().getStringExtra("email");
                String password = getIntent().getStringExtra("password");
                String name = getIntent().getStringExtra("name");
                int loginCategoryId = getIntent().getIntExtra("login_category_id", JoinFlag.DEMAND);
                String phone = et_sns_phone.getText().toString();
                String birth = et_sns_birth.getText().toString();

                joinFromSNSPresenter.onClickJoin(email, password, name, loginCategoryId, phone, birth);
                break;
        }
    }

    @Override
    public String getDeviceId() {
        return android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID).toString();
    }

    @Override
    public String getFireBaseToken() {
        return FirebaseInstanceId.getInstance().getToken();
    }




}
