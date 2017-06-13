package com.demand.well_family.well_family.falldiagnosistory.base.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.dto.FallDiagnosisStoryInfo;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.falldiagnosistory.base.adapter.FallDiagnosisStoryAdapter;
import com.demand.well_family.well_family.falldiagnosistory.base.presenter.FallDiagnosisStoryPresenter;
import com.demand.well_family.well_family.falldiagnosistory.base.presenter.impl.FallDiagnosisStoryPresenterImpl;
import com.demand.well_family.well_family.falldiagnosistory.base.view.FallDiagnosisStoryView;
import com.demand.well_family.well_family.falldiagnosistory.detail.activity.FallDiagnosisStoryDetailActivity;
import com.demand.well_family.well_family.falldiagnosistory.flag.FallDiagnosisStoryCodeFlag;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-06-01.
 */

public class FallDiagnosisStoryActivity extends Activity implements FallDiagnosisStoryView, NestedScrollView.OnScrollChangeListener, View.OnClickListener {
    private FallDiagnosisStoryPresenter fallDiagnosisStoryPresenter;

    private Toolbar toolBar;
    private TextView toolbar_title;
    private ImageView toolbar_back;
    private View decorView;

    private RecyclerView rv_falldiagnosisstory;
    private FallDiagnosisStoryAdapter fallDiagnosisStoryAdapter;
    private ProgressDialog progressDialog;
    private NestedScrollView nsv_falldiagnosisstory;

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FallDiagnosisStoryCodeFlag.DETAIL_REQUEST:
                switch (resultCode) {
                    case FallDiagnosisStoryCodeFlag.RESULT_OK:
                        FallDiagnosisStory fallDiagnosisStory = (FallDiagnosisStory) data.getSerializableExtra("fallDiagnosisStory");
                        int position = fallDiagnosisStory.getPosition();

                        fallDiagnosisStoryAdapter.setLikeChange(position, fallDiagnosisStory.getChecked());
                        fallDiagnosisStoryAdapter.notifyItemChanged(position);
                        break;
                }
                break;
        }
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
    }

    @Override
    public void setFallDiagnosisStoryAdapterItem(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, FallDiagnosisStoryInfo fallDiagnosisStoryInfo, FallDiagnosisStory fallDiagnosisStory) {
        fallDiagnosisStoryAdapter.setFallDiagnosisStoryAdapterItem(holder, fallDiagnosisStoryInfo, fallDiagnosisStory);
    }

    @Override
    public void showFallDiagnosisStoryAdapterCommentCount(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, String count) {
        fallDiagnosisStoryAdapter.setFallDiagnosisStoryAdapterCommentCount(holder, count);
    }

    @Override
    public void showFallDiagnosisStoryAdapterLikeCount(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, String count) {
        fallDiagnosisStoryAdapter.setFallDiagnosisStoryAdapterLikeCount(holder, count);
    }

    public void setFallDiagnosisStoryAdapterLikeIsChecked(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, int position) {
        fallDiagnosisStoryAdapter.setFallDiagnosisStoryAdapterLikeIsChecked(holder, position);
    }

    public void setFallDiagnosisStoryAdapterLikeIsUnChecked(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, int position) {
        fallDiagnosisStoryAdapter.setFallDiagnosisStoryAdapterLikeIsUnChecked(holder, position);
    }


    @Override
    public void showMessage(String message) {
        Toast.makeText(FallDiagnosisStoryActivity.this, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void init() {
        rv_falldiagnosisstory = (RecyclerView) findViewById(R.id.rv_falldiagnosisstory);
        nsv_falldiagnosisstory = (NestedScrollView) findViewById(R.id.nsv_falldiagnosisstory);
        nsv_falldiagnosisstory.setOnScrollChangeListener(this);

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

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        View view = v.getChildAt(v.getChildCount() - 1);
        int difference = (view.getBottom() - (v.getHeight() + v.getScrollY()));
        fallDiagnosisStoryPresenter.onScrollChange(difference);
    }


    @Override
    public void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.progress_dialog);
    }

    @Override
    public void goneProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void showFallDiagnosisStoryAdapterNotifyItemChanged(int position) {
        fallDiagnosisStoryAdapter.notifyItemChanged(position);
    }

    @Override
    public void setFallDiagnosisStoryAdapter(FallDiagnosisStoryAdapter fallDiagnosisStoryAdapter) {
        rv_falldiagnosisstory.setAdapter(fallDiagnosisStoryAdapter);
        rv_falldiagnosisstory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public FallDiagnosisStoryAdapter getFallDiagnosisStoryAdapter() {
        return this.fallDiagnosisStoryAdapter;
    }

    @Override
    public void setFallDiagnosisStoryAdapterLikeDown(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, int position) {
        fallDiagnosisStoryAdapter.setFallDiagnosisStoryAdapterLikeUnChecked(holder, position);
    }

    @Override
    public void setFallDiagnosisStoryAdapterLikeUp(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, int position) {
        fallDiagnosisStoryAdapter.setFallDiagnosisStoryAdapterLikeChecked(holder, position);
    }

    @Override
    public void showFallDiagnosisStoryAdapterResult(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, String result) {
        fallDiagnosisStoryAdapter.setFallDiagnosisStoryAdapterResult(holder, result);
    }

    @Override
    public void showFallDiagnosisStoryAdapterScore(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, String score) {
        fallDiagnosisStoryAdapter.setFallDiagnosisStoryAdapterScore(holder, score);
    }

    @Override
    public void navigateToFallDiagnosisStoryDetailActivity(FallDiagnosisStory fallDiagnosisStory, FallDiagnosisStoryInfo fallDiagnosisStoryInfo) {
        fallDiagnosisStory.setFirstChecked(false);
        Intent intent = new Intent(this, FallDiagnosisStoryDetailActivity.class);
        intent.putExtra("fallDiagnosisStory", fallDiagnosisStory);
        intent.putExtra("fallDiagnosisStoryInfo", fallDiagnosisStoryInfo);
        startActivityForResult(intent, FallDiagnosisStoryCodeFlag.DETAIL_REQUEST);
    }

    @Override
    public void showScoreTextChangeColorWithSafe(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder) {
        fallDiagnosisStoryAdapter.showScoreTextChangeColorWithSafe(holder);
    }

    @Override
    public void showScoreTextChangeColorWithCaution(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder) {
        fallDiagnosisStoryAdapter.showScoreTextChangeColorWithCaution(holder);
    }

    @Override
    public void showScoreTextChangeColorWithRisk(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder) {
        fallDiagnosisStoryAdapter.showScoreTextChangeColorWithRisk(holder);
    }
}
