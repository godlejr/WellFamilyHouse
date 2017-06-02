package com.demand.well_family.well_family.falldiagnosistory.base.activity;

import android.app.Activity;
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
import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.falldiagnosistory.base.adapter.FallDiagnosisStoryAdapter;
import com.demand.well_family.well_family.falldiagnosistory.base.presenter.FallDiagnosisStoryPresenter;
import com.demand.well_family.well_family.falldiagnosistory.base.presenter.impl.FallDiagnosisStoryPresenterImpl;
import com.demand.well_family.well_family.falldiagnosistory.base.view.FallDiagnosisStoryView;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-06-01.
 */

public class FallDiagnosisStoryActivity extends Activity implements FallDiagnosisStoryView, View.OnClickListener {
    private FallDiagnosisStoryPresenter fallDiagnosisStoryPresenter;

    private Toolbar toolBar;
    private TextView toolbar_title;
    private ImageView toolbar_back;
    private View decorView;

    private RecyclerView rv_falldiagnosisstory;
    private FallDiagnosisStoryAdapter fallDiagnosisStoryAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_falldiagnosisstory);

        User user = new User();
        user.setId(getIntent().getIntExtra("story_user_id", 0));
        user.setEmail(getIntent().getStringExtra("story_user_email"));
        user.setBirth(getIntent().getStringExtra("story_user_birth"));
        user.setPhone(getIntent().getStringExtra("story_user_phone"));
        user.setName(getIntent().getStringExtra("story_user_name"));
        user.setLevel(getIntent().getIntExtra("story_user_level", 0));
        user.setAvatar(getIntent().getStringExtra("story_user_avatar"));

        fallDiagnosisStoryPresenter = new FallDiagnosisStoryPresenterImpl(this);
        fallDiagnosisStoryPresenter.onCreate(user);
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
        toolBar = (Toolbar) decorView.findViewById(R.id.toolBar);
        toolbar_title = (TextView) toolBar.findViewById(R.id.toolbar_title);
        toolbar_back = (ImageView) toolBar.findViewById(R.id.toolbar_back);
        toolbar_back.setOnClickListener(this);
    }

    @Override
    public void showToolbarTitle(String message) {
        toolbar_title.setText(message);
    }

    @Override
    public void setFallDiagnosisStoryAdapterInit(ArrayList<FallDiagnosisStory> fallDiagnosisStoryList) {
        fallDiagnosisStoryAdapter = new FallDiagnosisStoryAdapter(this, fallDiagnosisStoryList, fallDiagnosisStoryPresenter);
        rv_falldiagnosisstory.setAdapter(fallDiagnosisStoryAdapter);
        rv_falldiagnosisstory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void setFallDiagnosisStoryAdapterCommentCount(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, String count) {

    }

    @Override
    public void setFallDiagnosisStoryAdapterLikeCount(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, String count) {

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(FallDiagnosisStoryActivity.this, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void init() {
        rv_falldiagnosisstory = (RecyclerView) findViewById(R.id.rv_falldiagnosisstory);
        fallDiagnosisStoryPresenter.onLoadData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back:
                finish();
                break;


        }
    }
}
