package com.demand.well_family.well_family.setting.deactivationReason.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.demand.well_family.well_family.main.login.activity.LoginActivity;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dialog.popup.deactivation.activity.DeactivationPopupActivity;
import com.demand.well_family.well_family.setting.deactivationReason.presenter.DeactivationReasonPresenter;
import com.demand.well_family.well_family.setting.deactivationReason.presenter.impl.DeactivationReasonPresenterImpl;
import com.demand.well_family.well_family.setting.deactivationReason.view.DeactivationReasonView;

import static com.demand.well_family.well_family.main.login.activity.LoginActivity.finishList;

/**
 * Created by ㅇㅇ on 2017-03-22.
 */

public class DeactivationReasonActivity extends Activity implements DeactivationReasonView, View.OnClickListener, RadioGroup.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {
    private DeactivationReasonPresenter deactivationReasonPresenter;

    private Button btn_deactivation;
    private RadioGroup rg_deactivation;
    private FrameLayout[] frameLayouts;
    private Spinner sp_deactivation;
    private TextView tv_deactivation_logout;
    private TextView tv_deactivation_help1;
    private TextView tv_deactivation_help2;
    private TextView tv_deactivation_help3;
    private TextView tv_deactivation_setting1;
    private TextView tv_deactivation_setting2;
    private TextView tv_deactivation_invite;
    private Intent intent;
    private TextView toolbar_title;
    private ImageView toolbar_back;
    private Toolbar toolbar;
    private View decorView;


    // adapter
    private ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deactivation_reason);

        deactivationReasonPresenter = new DeactivationReasonPresenterImpl(this);
        deactivationReasonPresenter.onCreate();
    }

    @Override
    public void init() {
        finishList.add(this);

        frameLayouts = new FrameLayout[8];
        frameLayouts[0] = (FrameLayout) findViewById(R.id.fl_deactivation1);
        frameLayouts[1] = (FrameLayout) findViewById(R.id.fl_deactivation2);
        frameLayouts[2] = (FrameLayout) findViewById(R.id.fl_deactivation3);
        frameLayouts[3] = (FrameLayout) findViewById(R.id.fl_deactivation4);
        frameLayouts[4] = (FrameLayout) findViewById(R.id.fl_deactivation5);
        frameLayouts[5] = (FrameLayout) findViewById(R.id.fl_deactivation6);
        frameLayouts[6] = (FrameLayout) findViewById(R.id.fl_deactivation7);
        frameLayouts[7] = (FrameLayout) findViewById(R.id.fl_deactivation8);

        rg_deactivation = (RadioGroup) findViewById(R.id.rg_deactivation);
        btn_deactivation = (Button) findViewById(R.id.btn_deactivation);

        rg_deactivation.setOnCheckedChangeListener(this);
        btn_deactivation.setOnClickListener(this);
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
            case R.id.btn_deactivation:
                navigateToDeactivationPopupActivity();
                break;

            case R.id.tv_deactivation_logout:
                deactivationReasonPresenter.onClickLogout();
                break;

            case R.id.tv_deactivation_invite:   // 가족 초대하기
            case R.id.tv_deactivation_help1:    // 고객센터 문의
            case R.id.tv_deactivation_help2:    // 고객센터 문의
            case R.id.tv_deactivation_help3:    // 게시글 신고 방법
                showMessage("준비중입니다.");
                break;

            case R.id.toolbar_back:
            case R.id.tv_deactivation_setting1:
            case R.id.tv_deactivation_setting2:
                finish();
                break;
        }
    }

    // 다시 보기
    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        int position = 0;

        switch (checkedId) {
            case R.id.rb_deactivation1:
                position = 0;

                tv_deactivation_logout = (TextView) findViewById(R.id.tv_deactivation_logout);
                tv_deactivation_logout.setOnClickListener(this);
                break;

            case R.id.rb_deactivation2:
                position = 1;

                tv_deactivation_help1 = (TextView) findViewById(R.id.tv_deactivation_help1);
                tv_deactivation_help1.setOnClickListener(this);
                break;

            case R.id.rb_deactivation3:
                position = 2;

                tv_deactivation_setting1 = (TextView) findViewById(R.id.tv_deactivation_setting1);
                tv_deactivation_setting1.setOnClickListener(this);

                break;

            case R.id.rb_deactivation4:
                position = 3;

                tv_deactivation_help2 = (TextView) findViewById(R.id.tv_deactivation_help2);
                tv_deactivation_help2.setOnClickListener(this);
                break;

            case R.id.rb_deactivation5:
                position = 4;

                tv_deactivation_help3 = (TextView) findViewById(R.id.tv_deactivation_help3);
                tv_deactivation_help3.setOnClickListener(this);
                break;

            case R.id.rb_deactivation6:
                position = 5;

                tv_deactivation_setting2 = (TextView) findViewById(R.id.tv_deactivation_setting2);
                tv_deactivation_setting2.setOnClickListener(this);
                break;

            case R.id.rb_deactivation7:
                position = 6;

                tv_deactivation_invite = (TextView) findViewById(R.id.tv_deactivation_invite);
                tv_deactivation_invite.setOnClickListener(this);
                break;

            case R.id.rb_deactivation8:
                position = 8;
                break;

            case R.id.rb_deactivation9:
                position = 7;
                break;
        }

        deactivationReasonPresenter.setVisibleGuidance(position);
        btn_deactivation.setBackgroundResource(R.drawable.round_corner_brown_r10);
    }

    @Override
    public void showGuidance(int position) {
        frameLayouts[position].setVisibility(View.VISIBLE);
    }

    @Override
    public void goneGuidance(int position) {
        frameLayouts[position].setVisibility(View.GONE);
    }

    @Override
    public void navigateToDeactivationPopupActivity() {
        Intent intent = new Intent(DeactivationReasonActivity.this, DeactivationPopupActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToLoginActivity() {
        intent = new Intent(DeactivationReasonActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void setDeactivationPeriodSpinner(String[] spinnerArray) {
        sp_deactivation = (Spinner) findViewById(R.id.sp_deactivation);

        spinnerAdapter = new ArrayAdapter<String>(DeactivationReasonActivity.this, R.layout.custom_spinner_item, spinnerArray);
        spinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
        sp_deactivation.setAdapter(spinnerAdapter);
        sp_deactivation.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        showMessage(deactivationReasonPresenter.getDeactivationPeriodMessage(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // none
    }

    @Override
    public void navigateToBack() {
        finish();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


}
