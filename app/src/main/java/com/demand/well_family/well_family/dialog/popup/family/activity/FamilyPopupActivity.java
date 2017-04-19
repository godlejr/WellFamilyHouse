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

        int familyId = getIntent().getIntExtra("family_id", 0);
        int familyUserId = getIntent().getIntExtra("family_user_id", 0);
        String familyName = getIntent().getStringExtra("family_name");
        String familyContent = getIntent().getStringExtra("family_content");
        String familyAvatar = getIntent().getStringExtra("family_avatar");
        String familyCreatedAt = getIntent().getStringExtra("family_created_at");

        String joinerName = getIntent().getStringExtra("joiner_name");
        Boolean deleteFlag = getIntent().getBooleanExtra("delete_flag", false);
        int joinFlag = getIntent().getIntExtra("join_flag", 0);

        Family family = new Family();
        family.setId(familyId);
        family.setName(familyName);
        family.setContent(familyContent);
        family.setAvatar(familyAvatar);
        family.setUser_id(familyUserId);
        family.setCreated_at(familyCreatedAt);

        familyPopupPresenter = new FamilyPopupPresenterImpl(this);
        familyPopupPresenter.onCreate(joinFlag, deleteFlag, family, joinerName);
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

        familyPopupPresenter.setPopupContent();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_popup_family_cancel:
                setResult(RESULT_CANCELED);
                finish();
                break;

            case R.id.btn_popup_family_close:
                finish();
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
    public void setPopupFamilyAvatar(String familyAvatar) {
        Glide.with(FamilyPopupActivity.this).load(getString(R.string.cloud_front_family_avatar) + familyAvatar).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_popup_family_avatar);
    }

    @Override
    public void setPopupButtonText(String commit, String cancel) {
        btn_popup_family_commit.setText(commit);
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
    public void navigateToBackAfterAcceptInvitation() {
        int familyPosition = getIntent().getIntExtra("position", 0);

        Intent backIntent = getIntent();
        backIntent.putExtra("position", familyPosition);
        setResult(FamilyPopupCodeFlag.FAMILY_JOIN, backIntent);
        finish();
    }

    @Override
    public void navigateToBackAfterAcceptRequest() {
        int joinerPosition = getIntent().getIntExtra("joiner_position", 0);

        setResult(FamilyPopupCodeFlag.FAMILY_JOIN, getIntent().putExtra("position", joinerPosition));
        finish();
    }

    @Override
    public void navigateToBackAfterSecessionAndDelete() {
        int familyPosition = getIntent().getIntExtra("position", 0);

        Intent backIntent = getIntent();
        backIntent.putExtra("position", familyPosition);
        setResult(FamilyPopupCodeFlag.DELETE_USER_TO_FAMILY, backIntent);
        finish();
    }


}
