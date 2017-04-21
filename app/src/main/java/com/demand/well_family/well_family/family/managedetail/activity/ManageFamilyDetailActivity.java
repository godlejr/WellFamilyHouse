package com.demand.well_family.well_family.family.managedetail.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dialog.popup.family.activity.FamilyPopupActivity;
import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.UserInfoForFamilyJoin;
import com.demand.well_family.well_family.family.managedetail.adapter.UserForFamilyJoinAdapter;
import com.demand.well_family.well_family.family.managedetail.flag.ManageFamilyDetailCodeFlag;
import com.demand.well_family.well_family.family.managedetail.presenter.ManageFamilyDetailPresenter;
import com.demand.well_family.well_family.family.managedetail.presenter.impl.ManageFamilyDetailPresenterImpl;
import com.demand.well_family.well_family.family.managedetail.view.ManageFamilyDetailView;
import com.demand.well_family.well_family.flag.FamilyJoinFlag;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ㅇㅇ on 2017-04-05.
 */

public class ManageFamilyDetailActivity extends Activity implements ManageFamilyDetailView, View.OnClickListener {
    private ManageFamilyDetailPresenter manageFamilyDetailPresenter;

    private CircleImageView iv_manage_family_avatar;
    private TextView tv_manage_family_name;
    private TextView tv_manage_family_content;
    private Button btn_manage_family_remove;
    private RecyclerView rv_manage_family_join;

    // adapter
    private UserForFamilyJoinAdapter userForFamilyJoinAdapter;
    //toolbar
    private Toolbar toolbar;
    private ImageView toolbar_back;
    private TextView toolbar_title;
    private View decorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_family_list);

        Intent intent = getIntent();
        Family family = new Family();

        family.setId(intent.getIntExtra("family_id", 0));
        family.setName(intent.getStringExtra("family_name"));
        family.setContent(intent.getStringExtra("family_content"));
        family.setUser_id(intent.getIntExtra("family_user_id", 0));
        family.setAvatar(intent.getStringExtra("family_avatar"));
        family.setCreated_at(intent.getStringExtra("family_created_at"));
        family.setPosition(intent.getIntExtra("position", 0));

        boolean notificationFlag = intent.getBooleanExtra("notification_flag", false);

        manageFamilyDetailPresenter = new ManageFamilyDetailPresenterImpl(this);
        manageFamilyDetailPresenter.onCreate(family, notificationFlag);
    }


    @Override
    public void init(Family family) {
        tv_manage_family_name = (TextView) findViewById(R.id.tv_manage_family_name);
        tv_manage_family_content = (TextView) findViewById(R.id.tv_manage_family_content);
        iv_manage_family_avatar = (CircleImageView) findViewById(R.id.iv_manage_family_avatar);
        btn_manage_family_remove = (Button) findViewById(R.id.btn_manage_family_remove);

        tv_manage_family_name.setText(family.getName());
        tv_manage_family_content.setText(family.getContent());
        Glide.with(this).load(getString(R.string.cloud_front_family_avatar) + family.getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_manage_family_avatar);

        rv_manage_family_join = (RecyclerView) findViewById(R.id.rv_manage_family_join);

        btn_manage_family_remove.setOnClickListener(this);
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
    public void setUserForFamilyJoinItem(ArrayList<UserInfoForFamilyJoin> userList) {
        userForFamilyJoinAdapter = new UserForFamilyJoinAdapter(userList, ManageFamilyDetailActivity.this, R.layout.item_manage_family_join, manageFamilyDetailPresenter);
        rv_manage_family_join.setAdapter(userForFamilyJoinAdapter);
        rv_manage_family_join.setLayoutManager(new LinearLayoutManager(ManageFamilyDetailActivity.this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void setUserForFamilyJoinAdapterAgree(UserForFamilyJoinAdapter.UserForFamilyJoinViewHolder holder, String message) {
        userForFamilyJoinAdapter.setUserForFamilyJoinAgree(holder, message);
    }

    @Override
    public void setUserForFamilyJoinAdapterHold(UserForFamilyJoinAdapter.UserForFamilyJoinViewHolder holder, String message) {
        userForFamilyJoinAdapter.setUserForFamilyJoinHold(holder, message);
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
    public void navigateToBackForResultCancel(int position) {
        Intent intent = getIntent();
        intent.putExtra("position", position);
        setResult(RESULT_CANCELED);
    }

    @Override
    public void navigateToBackForFamilyDelete(int position) {
        Intent intent = getIntent();
        intent.putExtra("position", position);
        intent.putExtra("photo_delete", true);
        setResult(ManageFamilyDetailCodeFlag.DELETE_FAMILY, intent);
        finish();
    }

    @Override
    public void setUserForFamilyJoinAdapterDelete(int position) {
        userForFamilyJoinAdapter.setUserForFamilyJoinDelete(position);
    }

    @Override
    public void showUserForFamilyJoinAdapterNotifyItemDelete(int position) {
        userForFamilyJoinAdapter.notifyItemRemoved(position);
        userForFamilyJoinAdapter.notifyItemRangeChanged(position, userForFamilyJoinAdapter.getItemCount());
    }

    @Override
    public void navigateToFamilyPopupActivityForFamilyDelete(Family family) {
        Intent intent = new Intent(ManageFamilyDetailActivity.this, FamilyPopupActivity.class);

        intent.putExtra("family_id", family.getId());
        intent.putExtra("family_user_id", family.getUser_id());
        intent.putExtra("family_name", family.getName());
        intent.putExtra("family_content", family.getContent());
        intent.putExtra("family_avatar", family.getAvatar());
        intent.putExtra("family_created_at", family.getCreated_at());
        intent.putExtra("position", family.getPosition());
        intent.putExtra("delete_flag", true);

        startActivityForResult(intent, ManageFamilyDetailCodeFlag.DELETE_FAMILY_REQUEST);
    }

    @Override
    public void navigateToFamilyPopupActivityForFamilyJoin(Family family, UserInfoForFamilyJoin userInfoForFamilyJoin) {
        Intent intent = new Intent(this, FamilyPopupActivity.class);

        intent.putExtra("family_id", family.getId());
        intent.putExtra("family_user_id", family.getUser_id());
        intent.putExtra("family_name", family.getName());
        intent.putExtra("family_content", family.getContent());
        intent.putExtra("family_avatar", family.getAvatar());
        intent.putExtra("family_created_at", family.getCreated_at());
        intent.putExtra("position", family.getPosition());


        intent.putExtra("joiner_id",userInfoForFamilyJoin.getId());
        intent.putExtra("joiner_name", userInfoForFamilyJoin.getName());
        intent.putExtra("joiner_avatar", userInfoForFamilyJoin.getAvatar());
        intent.putExtra("join_flag", FamilyJoinFlag.USER_TO_FAMILY);
        intent.putExtra("joiner_position", userInfoForFamilyJoin.getPosition());

        startActivityForResult(intent, ManageFamilyDetailCodeFlag.POPUP_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case ManageFamilyDetailCodeFlag.DELETE_FAMILY_REQUEST:
                switch (resultCode) {
                    case ManageFamilyDetailCodeFlag.DELETE_USER_TO_FAMILY:
                        int familyPosition = data.getIntExtra("position", 0);
                        manageFamilyDetailPresenter.onActivityResultForDeleteFamilyResult(familyPosition);
                        break;
                }
                break;

            case ManageFamilyDetailCodeFlag.POPUP_REQUEST:
                switch (resultCode) {
                    case ManageFamilyDetailCodeFlag.JOIN:
                        int userPosition = data.getIntExtra("joiner_position", 0);
                            manageFamilyDetailPresenter.onActivityResultForPopupJoin(userPosition);
                        break;
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        manageFamilyDetailPresenter.onBackPressed();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back:
                navigateToBack();
                break;

            case R.id.btn_manage_family_remove:
                manageFamilyDetailPresenter.onClickFamilyDelete();
                break;
        }
    }
}
