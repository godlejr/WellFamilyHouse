package com.demand.well_family.well_family.family.manage.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dialog.popup.family.activity.FamilyPopupActivity;
import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.FamilyInfoForFamilyJoin;
import com.demand.well_family.well_family.family.managedetail.activity.ManageFamilyDetailActivity;
import com.demand.well_family.well_family.family.manage.adapter.member.FamilyForMemberAdapter;
import com.demand.well_family.well_family.family.manage.adapter.owner.FamilyForOwnerAdapter;
import com.demand.well_family.well_family.family.manage.flag.ManageFamilyCodeFlag;
import com.demand.well_family.well_family.family.manage.presenter.ManageFamilyPresenter;
import com.demand.well_family.well_family.family.manage.presenter.impl.ManageFamilyPresenterImpl;
import com.demand.well_family.well_family.family.manage.view.ManageFamilyView;
import com.demand.well_family.well_family.search.SearchFamilyActivity;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-03.
 */

public class ManageFamilyActivity extends Activity implements ManageFamilyView, View.OnClickListener {
    private ManageFamilyPresenter manageFamilyPresenter;

    private TextView tv_find_family;
    private RecyclerView rv_manage_family_owner;
    private RecyclerView rv_manage_family_member;

    private FamilyForOwnerAdapter familyForOwnerAdapter;
    private FamilyForMemberAdapter familyForMemberAdapter;

    //toolbar
    private Toolbar toolbar;
    private ImageView toolbar_back;
    private TextView toolbar_title;
    private View decorView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_family);

        manageFamilyPresenter = new ManageFamilyPresenterImpl(this);
        manageFamilyPresenter.onCreate();
    }

    @Override
    public void init() {
        tv_find_family = (TextView) findViewById(R.id.tv_find_family);

        rv_manage_family_member = (RecyclerView) findViewById(R.id.rv_manage_family_member);
        rv_manage_family_owner = (RecyclerView) findViewById(R.id.rv_manage_family_owner);

        tv_find_family.setOnClickListener(this);
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
    public void setFamilyForOwnerItem(ArrayList<Family> familyList) {
        familyForOwnerAdapter = new FamilyForOwnerAdapter(familyList, R.layout.item_manage_family_owner, this, manageFamilyPresenter);
        rv_manage_family_owner.setAdapter(familyForOwnerAdapter);
        rv_manage_family_owner.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

    }

    @Override
    public void setFamilyForMemberItem(ArrayList<FamilyInfoForFamilyJoin> familyInfoForFamilyJoinList) {
        familyForMemberAdapter = new FamilyForMemberAdapter(familyInfoForFamilyJoinList, R.layout.item_manage_family_member, this, manageFamilyPresenter);
        rv_manage_family_member.setAdapter(familyForMemberAdapter);
        rv_manage_family_member.setLayoutManager(new LinearLayoutManager(ManageFamilyActivity.this, LinearLayoutManager.HORIZONTAL, false));

    }

    @Override
    public void setFamilyForMemberAdapterSecession(FamilyForMemberAdapter.FamilyForMemberViewHolder holder, String message) {
        familyForMemberAdapter.setFamilyForMemberSecession(holder, message);
    }

    @Override
    public void setFamilyForMemberAdapterHold(FamilyForMemberAdapter.FamilyForMemberViewHolder holder, String message) {
        familyForMemberAdapter.setFamilyForMemberHold(holder, message);
    }

    @Override
    public void setFamilyForMemberAdapterAgree(FamilyForMemberAdapter.FamilyForMemberViewHolder holder, String message) {
        familyForMemberAdapter.setFamilyForMemberAgree(holder, message);
    }

    @Override
    public void showFamilyForOwnerAdapterNotifyItemDelete(int position) {
        familyForOwnerAdapter.notifyItemRemoved(position);
        familyForOwnerAdapter.notifyItemRangeChanged(position, familyForOwnerAdapter.getItemCount());
    }

    @Override
    public void showFamilyForMemberAdapterNotifyItemDelete(int position) {
        familyForMemberAdapter.notifyItemRemoved(position);
        familyForMemberAdapter.notifyItemRangeChanged(position, familyForMemberAdapter.getItemCount());
    }

    @Override
    public void showFamilyForMemberAdapterNotifyItemChange(int position) {
        familyForMemberAdapter.notifyItemChanged(position);
    }

    @Override
    public void setFamilyForOwnerAdapterDelete(int position) {
        familyForOwnerAdapter.setFamilyForOwnerDelete(position);
    }

    @Override
    public void setFamilyForMemberAdapterDelete(int position) {
        familyForMemberAdapter.setFamilyForMemberDelete(position);
    }

    @Override
    public void setFamilyForMemberAdapterChangeForJoinFlag(int position, int joinFlag) {
        familyForMemberAdapter.setFamilyForMemberChangeForJoinFlag(position, joinFlag);
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
    public void navigateToSearchFamilyActivity() {
        Intent intent = new Intent(this, SearchFamilyActivity.class);
        startActivity(intent);
    }

    @Override
    public void navigateToSearchFamilyPopupActivity(FamilyInfoForFamilyJoin familyInfoForFamilyJoin) {
        Intent intent = new Intent(this, FamilyPopupActivity.class);

        intent.putExtra("family_id", familyInfoForFamilyJoin.getId());
        intent.putExtra("family_user_id", familyInfoForFamilyJoin.getUser_id());
        intent.putExtra("family_name", familyInfoForFamilyJoin.getName());
        intent.putExtra("family_content", familyInfoForFamilyJoin.getContent());
        intent.putExtra("family_avatar", familyInfoForFamilyJoin.getAvatar());
        intent.putExtra("family_created_at", familyInfoForFamilyJoin.getCreated_at());
        intent.putExtra("join_flag", familyInfoForFamilyJoin.getJoin_flag());
        intent.putExtra("position", familyInfoForFamilyJoin.getPosition());

        startActivityForResult(intent, ManageFamilyCodeFlag.POPUP_REQUEST);
    }

    @Override
    public void navigateToManageFamilyDetailActivity(Family family) {
        Intent manage_intent = new Intent(this, ManageFamilyDetailActivity.class);

        manage_intent.putExtra("family_id", family.getId());
        manage_intent.putExtra("family_name", family.getName());
        manage_intent.putExtra("family_content", family.getContent());
        manage_intent.putExtra("family_avatar", family.getAvatar());
        manage_intent.putExtra("family_user_id", family.getUser_id());
        manage_intent.putExtra("family_created_at", family.getCreated_at());
        manage_intent.putExtra("position", family.getPosition());

        startActivityForResult(manage_intent, ManageFamilyCodeFlag.DELETE_FAMILY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        int position;

        switch (requestCode) {
            case ManageFamilyCodeFlag.POPUP_REQUEST:

                switch (resultCode) {
                    case ManageFamilyCodeFlag.DELETE_USER_TO_FAMILY:
                        position = data.getIntExtra("position", 0);
                        manageFamilyPresenter.onActivityResultForPopupDeleteUserToFamily(position);
                        break;

                    case ManageFamilyCodeFlag.JOIN:
                        position = data.getIntExtra("position", 0);
                        manageFamilyPresenter.onActivityResultForPopupJoin(position);
                        break;
                }
                break;

            case ManageFamilyCodeFlag.DELETE_FAMILY_REQUEST:
                switch (resultCode) {
                    case ManageFamilyCodeFlag.DELETE_FAMILY:
                        position = data.getIntExtra("position", 0);
                        manageFamilyPresenter.onActivityResultForDeleteFamilyResult(position);
                        break;
                }

                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back:
                navigateToBack();
                break;
            case R.id.tv_find_family:
                navigateToSearchFamilyActivity();
                break;
        }
    }
}
