package com.demand.well_family.well_family.dialog.popup.family.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dialog.popup.family.flag.FamilyPopupCodeFlag;
import com.demand.well_family.well_family.dialog.popup.family.presenter.FamilyPopupPresenter;
import com.demand.well_family.well_family.dialog.popup.family.presenter.impl.FamilyPopupPresenterImpl;
import com.demand.well_family.well_family.dialog.popup.family.view.FamilyPopupView;
import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.UserInfoForFamilyJoin;


/**
 * Created by ㅇㅇ on 2017-04-04.
 */

public class FamilyPopupActivity extends Activity implements FamilyPopupView, View.OnClickListener {
    private FamilyPopupPresenter familyPopupPresenter;

    private TextView tv_popup_family_title;
    private TextView tv_popup_family_content;
    private ImageButton btn_popup_family_close;
    private ImageView iv_popup_family_avatar;
    private Button btn_popup_family_cancel;
    private Button btn_popup_family_commit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_family);

        Boolean deleteFlag = getIntent().getBooleanExtra("delete_flag", false);

        Family family = new Family();
        family.setId(getIntent().getIntExtra("family_id", 0));
        family.setName(getIntent().getStringExtra("family_name"));
        family.setContent(getIntent().getStringExtra("family_content"));
        family.setAvatar(getIntent().getStringExtra("family_avatar"));
        family.setUser_id(getIntent().getIntExtra("family_user_id", 0));
        family.setCreated_at(getIntent().getStringExtra("family_created_at"));
        family.setPosition(getIntent().getIntExtra("position", 0));

        UserInfoForFamilyJoin userInfoForFamilyJoin = new UserInfoForFamilyJoin();
        userInfoForFamilyJoin.setName(getIntent().getStringExtra("joiner_name"));
        userInfoForFamilyJoin.setJoin_flag(getIntent().getIntExtra("join_flag", 0));
        userInfoForFamilyJoin.setPosition(getIntent().getIntExtra("joiner_position", 0));
        userInfoForFamilyJoin.setId(getIntent().getIntExtra("joiner_id", 0));

        familyPopupPresenter = new FamilyPopupPresenterImpl(this);
        familyPopupPresenter.onCreate(family, userInfoForFamilyJoin, deleteFlag);
    }

    @Override
    public void setDisplay() {
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        getWindow().getAttributes().width = (int) (display.getWidth() * 0.9);
        getWindow().getAttributes().height = (int) (display.getHeight() * 0.8);
    }

    @Override
    public void init() {
        tv_popup_family_title = (TextView) findViewById(R.id.tv_popup_family_title);
        tv_popup_family_content = (TextView) findViewById(R.id.tv_popup_family_content);
        btn_popup_family_close = (ImageButton) findViewById(R.id.btn_popup_family_close);
        iv_popup_family_avatar = (ImageView) findViewById(R.id.iv_popup_family_avatar);
        btn_popup_family_cancel = (Button) findViewById(R.id.btn_popup_family_cancel);
        btn_popup_family_commit = (Button) findViewById(R.id.btn_popup_family_commit);

        btn_popup_family_cancel.setOnClickListener(this);
        btn_popup_family_close.setOnClickListener(this);
        btn_popup_family_commit.setOnClickListener(this);

        familyPopupPresenter.onLoadData();
    }

    @Override
    public void onBackPressed() {
        setResult(FamilyPopupCodeFlag.RESULT_CANCELED);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_popup_family_close:
                setResult(FamilyPopupCodeFlag.RESULT_CANCELED);
                finish();
                break;

            case R.id.btn_popup_family_cancel:
                familyPopupPresenter.onClickClose();
                break;

            case R.id.btn_popup_family_commit:
                familyPopupPresenter.onClickCommit();
                break;
        }
    }

    @Override
    public void setPopupTitle(String title) {
        tv_popup_family_title.setText(title);
    }

    @Override
    public void setPopupContent(String content) {
        tv_popup_family_content.setText(content);
    }

    @Override
    public void setPopupFamilyAvatar(Family family) {
        Glide.with(FamilyPopupActivity.this).load(getString(R.string.cloud_front_family_avatar) + family.getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_popup_family_avatar);
    }

    @Override
    public void setPopupButtonText(String conduct, String cancel) {
        btn_popup_family_commit.setText(conduct);
        btn_popup_family_cancel.setText(cancel);
    }

    @Override
    public void setPopupButtonBackground(int resId) {
        btn_popup_family_commit.setBackgroundResource(resId);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(FamilyPopupActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToBack() {
        finish();
    }

    @Override
    public void navigateToBackAfterAcceptInvitation(Family family) {
        Intent intent = getIntent();
        intent.putExtra("position", family.getPosition());
        setResult(FamilyPopupCodeFlag.FAMILY_JOIN, intent);
        finish();
    }

    @Override
    public void navigateToBackAfterAcceptRequest(UserInfoForFamilyJoin userInfoForFamilyJoin) {
        setResult(FamilyPopupCodeFlag.FAMILY_JOIN, getIntent().putExtra("position", userInfoForFamilyJoin.getPosition()));
        finish();
    }

    @Override
    public void navigateToBackAfterSecessionAndDelete(Family family) {
        Intent intent = getIntent();
        intent.putExtra("position", family.getPosition());
        setResult(FamilyPopupCodeFlag.DELETE_USER_TO_FAMILY, intent);
        finish();
    }

    @Override
    public void setButtonUnClickable() {
        btn_popup_family_commit.setClickable(false);
    }
}
