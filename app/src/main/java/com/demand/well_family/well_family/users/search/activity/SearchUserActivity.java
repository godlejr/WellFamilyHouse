package com.demand.well_family.well_family.users.search.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.UserInfoForFamilyJoin;
import com.demand.well_family.well_family.family.base.activity.FamilyActivity;
import com.demand.well_family.well_family.users.search.adapter.UserAdapter;
import com.demand.well_family.well_family.users.search.presenter.SearchUserPresenter;
import com.demand.well_family.well_family.users.search.presenter.impl.SearchUserPresenterImpl;
import com.demand.well_family.well_family.users.search.view.SearchUserView;

import java.util.ArrayList;

import static com.demand.well_family.well_family.main.login.activity.LoginActivity.finishList;

/**
 * Created by ㅇㅇ on 2017-02-12.
 */

public class SearchUserActivity extends Activity implements SearchUserView, View.OnClickListener {
    private SearchUserPresenter searchUserPresenter;


    private View decorView;
    private TextView toolbarTitle;

    private Button btn_search_user;
    private EditText et_search_user;

    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);

        finishList.add(this);

        Family family = new Family();
        family.setId(getIntent().getIntExtra("family_id", 0));
        family.setName(getIntent().getStringExtra("family_name"));
        family.setContent(getIntent().getStringExtra("family_content"));
        family.setAvatar(getIntent().getStringExtra("family_avatar"));
        family.setUser_id(getIntent().getIntExtra("family_user_id", 0));
        family.setCreated_at(getIntent().getStringExtra("family_created_at"));


        searchUserPresenter = new SearchUserPresenterImpl(this);
        searchUserPresenter.onCreate(family);
    }


    @Override
    public void showMessage(String message) {
        Toast.makeText(SearchUserActivity.this, message, Toast.LENGTH_SHORT).show();
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
        Toolbar toolbar = (Toolbar) decorView.findViewById(R.id.toolBar);
        toolbar.setBackgroundColor(Color.WHITE);

        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        ImageView toolbarBack = (ImageView) toolbar.findViewById(R.id.toolbar_back);
        toolbarBack.setOnClickListener(this);
    }


    @Override
    public void init() {
        et_search_user = (EditText) findViewById(R.id.et_search_user);
        btn_search_user = (Button) findViewById(R.id.btn_search_user);
        btn_search_user.setOnClickListener(this);
    }

    @Override
    public void showToolbarTitle(String title) {
        toolbarTitle.setText(title);
    }

    @Override
    public void setUserAdapterInit(ArrayList<UserInfoForFamilyJoin> userList) {
        RecyclerView rv_search_user = (RecyclerView) findViewById(R.id.rv_search_user);
        userAdapter = new UserAdapter(userList, SearchUserActivity.this, R.layout.item_search_user, searchUserPresenter);
        rv_search_user.setAdapter(userAdapter);
        rv_search_user.setLayoutManager(new LinearLayoutManager(SearchUserActivity.this, LinearLayoutManager.VERTICAL, false));
    }


    @Override
    public void navigateToBackWithFamily(Family family) {
        Intent intent = new Intent(SearchUserActivity.this, FamilyActivity.class);

        intent.putExtra("family_id", family.getId());
        intent.putExtra("family_name", family.getName());
        intent.putExtra("family_content", family.getContent());
        intent.putExtra("family_avatar", family.getAvatar());
        intent.putExtra("family_user_id", family.getUser_id());
        intent.putExtra("family_created_at", family.getCreated_at());

        startActivity(intent);
        finish();
    }

    @Override
    public void setUserStateButtonForFamily(UserAdapter.UserViewHolder holder) {
        userAdapter.setUserStateButtonForFamily(holder);
    }

    @Override
    public void setUserStateButtonForJoin(UserAdapter.UserViewHolder holder, UserInfoForFamilyJoin userFound) {
        userAdapter.setUserStateButtonForJoin(holder, userFound);
    }

    @Override
    public void setUserStateButtonForStay(UserAdapter.UserViewHolder holder) {
        userAdapter.setUserStateButtonForStay(holder);
    }

    @Override
    public void setUserStateButtonForMe(UserAdapter.UserViewHolder holder) {
        userAdapter.setUserStateButtonForMe(holder);
    }

    @Override
    public void setUserStateButtonForInvite(UserAdapter.UserViewHolder holder, UserInfoForFamilyJoin userFound) {
        userAdapter.setUserStateButtonForInvite(holder, userFound);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back:
                searchUserPresenter.onClickBack();
                break;

            case R.id.btn_search_user:
                String searchKeyword = et_search_user.getText().toString();
                searchUserPresenter.onClickSetUserSearched(searchKeyword);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        searchUserPresenter.onClickBack();
    }


}
