package com.demand.well_family.well_family.users.base.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dialog.popup.photo.activity.PhotoPopupActivity;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.falldiagnosistory.base.activity.FallDiagnosisStoryActivity;
import com.demand.well_family.well_family.flag.PhotoPopupINTENTFlag;
import com.demand.well_family.well_family.users.EditUserActivity;
import com.demand.well_family.well_family.users.SongStoryActivity;
import com.demand.well_family.well_family.users.base.presenter.UserPresenter;
import com.demand.well_family.well_family.users.base.presenter.impl.UserPresenterImpl;
import com.demand.well_family.well_family.users.base.view.UserView;

import static com.demand.well_family.well_family.main.login.activity.LoginActivity.finishList;

/**
 * Created by Dev-0 on 2017-01-23.
 */

public class UserActivity extends Activity implements View.OnClickListener, UserView {
    private UserPresenter userPresenter;

    //toolbar
    private Toolbar toolbar;
    private ImageView toolbar_back;
    private TextView toolbar_title;
    private View decorView;

    private ImageView iv_family_activity_avatar;
    private TextView tv_family_activity_name;
    private TextView tv_family_activity_birth;
    private TextView tv_family_activity_phone2;
    private TextView tv_family_activity_email;
    private LinearLayout ll_memory_sound_story_list;
    private LinearLayout ll_user_edit;
    private LinearLayout ll_edit_profile;
    private RelativeLayout ll_user_phone_info;
    private ImageView iv_user_call;

    private LinearLayout ll_falldiagnosisstory_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        User storyUser = new User();

        storyUser.setId(getIntent().getIntExtra("story_user_id", 0));
        storyUser.setName(getIntent().getStringExtra("story_user_name"));
        storyUser.setEmail(getIntent().getStringExtra("story_user_email"));
        storyUser.setLevel( getIntent().getIntExtra("story_user_level", 0));
        storyUser.setAvatar(getIntent().getStringExtra("story_user_avatar"));
        storyUser.setBirth(getIntent().getStringExtra("story_user_birth"));
        storyUser.setPhone(getIntent().getStringExtra("story_user_phone"));

        userPresenter = new UserPresenterImpl(this);
        userPresenter.onCreate(storyUser);

    }

    @Override
    public void init(User user, User storyUser) {
        finishList.add(this);
        ll_edit_profile = (LinearLayout) findViewById(R.id.ll_edit_profile);
        tv_family_activity_phone2 = (TextView) findViewById(R.id.tv_family_activity_phone2);
        ll_memory_sound_story_list = (LinearLayout) findViewById(R.id.ll_memory_sound_story_list);
        iv_user_call = (ImageView) findViewById(R.id.iv_user_call);
        ll_user_phone_info = (RelativeLayout) findViewById(R.id.ll_user_phone_info);
        iv_family_activity_avatar = (ImageView) findViewById(R.id.iv_family_activity_avatar);
        tv_family_activity_email = (TextView) findViewById(R.id.tv_family_activity_email);
        tv_family_activity_name = (TextView) findViewById(R.id.tv_family_activity_name);
        tv_family_activity_birth = (TextView) findViewById(R.id.tv_family_activity_birth);
        ll_falldiagnosisstory_list=(LinearLayout)findViewById(R.id.ll_falldiagnosisstory_list);

        userPresenter.onLoadData();
        userPresenter.setUserPhone(storyUser.getPhone());
        userPresenter.setUserBirth(storyUser.getBirth());

        Glide.with(this).load(getString(R.string.cloud_front_user_avatar) + storyUser.getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_family_activity_avatar);
        tv_family_activity_name.setText(storyUser.getName());
        tv_family_activity_email.setText(storyUser.getEmail());

        ll_edit_profile.setOnClickListener(this);
        ll_memory_sound_story_list.setOnClickListener(this);
        iv_family_activity_avatar.setOnClickListener(this);
        ll_falldiagnosisstory_list.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_edit_profile:
                navigateToEditUserActivity();
                break;

            case R.id.toolbar_back:
                navigateToBack();
                break;

            case R.id.ll_falldiagnosisstory_list:
                userPresenter.onClickFallDiagnosisStory();
                break;

            case R.id.ll_memory_sound_story_list:
                userPresenter.onClickSongStory();
                break;

            case R.id.iv_family_activity_avatar:
                userPresenter.onClickAvatar();
                break;


            case R.id.tv_family_activity_phone2:
            case R.id.iv_user_call:
               userPresenter.onClickCall();
                break;
        }
    }



    @Override
    public void setToolbar(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolBar);
        toolbar_back = (ImageView) toolbar.findViewById(R.id.toolbar_back);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);

        toolbar_back.setOnClickListener(this);
    }

    @Override
    public void showToolbarTitle(String message) {
        toolbar_title.setText(message);
    }

    @Override
    public View getDecorView() {
        if (decorView == null) {
            decorView = this.getWindow().getDecorView();
        }
        return decorView;
    }

    @Override
    public void navigateToBack() {
        finish();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPhoneOnClickListener() {
        tv_family_activity_phone2.setOnClickListener(this);
        iv_user_call.setOnClickListener(this);
    }

    @Override
    public void goneUserEdit() {
        ll_user_edit = (LinearLayout) findViewById(R.id.ll_user_edit);
        ll_user_edit.setVisibility(View.GONE);
    }

    @Override
    public void showUserBirth(String date) {
        tv_family_activity_birth.setText(date);
    }

    @Override
    public void showUserPhone(String phone) {
        tv_family_activity_phone2.setText(phone);
    }

    @Override
    public void goneUserPhone() {
        ll_user_phone_info.setVisibility(View.GONE);
    }

    @Override
    public void navigateToCall(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        startActivity(intent);
    }

    @Override
    public void navigateToEditUserActivity() {
        Intent intent = new Intent(UserActivity.this, EditUserActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToSongStoryActivity(User storyUser) {
        Intent intent = new Intent(UserActivity.this, SongStoryActivity.class);
        intent.putExtra("story_user_id", storyUser.getId());
        intent.putExtra("story_user_email", storyUser.getEmail());
        intent.putExtra("story_user_birth", storyUser.getBirth());
        intent.putExtra("story_user_phone", storyUser.getPhone());
        intent.putExtra("story_user_name", storyUser.getName());
        intent.putExtra("story_user_level", storyUser.getLevel());
        intent.putExtra("story_user_avatar", storyUser.getAvatar());

        startActivity(intent);
    }

    @Override
    public void navigateToPhotoPopupActivity(User storyUser) {
        Intent intent = new Intent(this, PhotoPopupActivity.class);
        intent.putExtra("avatar", storyUser.getAvatar());
        intent.putExtra("intent_flag", PhotoPopupINTENTFlag.USERACTIVITY);
        startActivity(intent);
    }

    @Override
    public void navigateToFallDiagnosisStoryActivity(User storyUser) {
        Intent intent = new Intent(UserActivity.this, FallDiagnosisStoryActivity.class);
        intent.putExtra("story_user_id", storyUser.getId());
        intent.putExtra("story_user_email", storyUser.getEmail());
        intent.putExtra("story_user_birth", storyUser.getBirth());
        intent.putExtra("story_user_phone", storyUser.getPhone());
        intent.putExtra("story_user_name", storyUser.getName());
        intent.putExtra("story_user_level", storyUser.getLevel());
        intent.putExtra("story_user_avatar", storyUser.getAvatar());

        startActivity(intent);
    }
}
