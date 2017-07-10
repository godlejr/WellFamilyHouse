package com.demand.well_family.well_family.exercisestory.base.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import com.demand.well_family.well_family.dto.ExerciseStory;
import com.demand.well_family.well_family.exercise.flag.ExerciseIntentFlag;
import com.demand.well_family.well_family.exercisestory.base.adapter.ExerciseStoryAdapter;
import com.demand.well_family.well_family.exercisestory.base.presenter.ExerciseStoryPresenter;
import com.demand.well_family.well_family.exercisestory.base.presenter.impl.ExerciseStoryPresenterImpl;
import com.demand.well_family.well_family.exercisestory.base.view.ExerciseStoryView;
import com.demand.well_family.well_family.exercisestory.detail.activity.ExerciseStoryDetailActivity;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-06-27.
 */

public class ExerciseStoryActivity extends Activity implements ExerciseStoryView, NestedScrollView.OnScrollChangeListener, View.OnClickListener {
    private ExerciseStoryPresenter exerciseStoryPresenter;

    private NestedScrollView nsv_exercisestory;
    private View decorView;
    private Toolbar toolbar;
    private TextView toolbar_title;
    private ImageView toolbar_back;

    private RecyclerView rv_exercisestory;
    private ExerciseStoryAdapter exerciseStoryAdapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercisestory);

        int storyUserId = getIntent().getIntExtra("story_user_id", 0);
        exerciseStoryPresenter = new ExerciseStoryPresenterImpl(this);
        exerciseStoryPresenter.onCreate(storyUserId);
    }

    @Override
    public void init() {
        nsv_exercisestory = (NestedScrollView) findViewById(R.id.nsv_exercisestory);
        rv_exercisestory = (RecyclerView) findViewById(R.id.rv_exercisestory);

        nsv_exercisestory.setOnScrollChangeListener(this);
        exerciseStoryPresenter.onLoadData();
    }

    @Override
    public void setExerciseStoryAdapterItem(ArrayList<ExerciseStory> exerciseStoryList) {
        exerciseStoryAdapter = new ExerciseStoryAdapter(exerciseStoryPresenter, exerciseStoryList, this, R.layout.item_exercisestory);
    }


    @Override
    public void showExerciseStoryAdapterCommentCount(ExerciseStoryAdapter.ExerciseStoryViewHolder holder, String message) {
        exerciseStoryAdapter.showExerciseStoryAdapterCommentCount(holder, message);
    }

    @Override
    public void showExerciseStoryAdapterLikeCount(ExerciseStoryAdapter.ExerciseStoryViewHolder holder, String message) {
        exerciseStoryAdapter.showExerciseStoryAdapterLikeCount(holder, message);
    }


    @Override
    public void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.progress_dialog);
    }

    @Override
    public void showExerciseStoryAdapterNotifyItemChanged(int position) {
        exerciseStoryAdapter.notifyItemChanged(position);
    }

    @Override
    public void goneProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void setExerciseStoryAdapter(ExerciseStoryAdapter fallDiagnosisStoryAdapter) {
        rv_exercisestory.setAdapter(exerciseStoryAdapter);
        rv_exercisestory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public ExerciseStoryAdapter getExerciseStoryAdapter() {
        return this.exerciseStoryAdapter;
    }

    @Override
    public void navigateToExerciseStoryDetailActivity(ExerciseStory exerciseStory) {
        Intent intent = new Intent(this, ExerciseStoryDetailActivity.class);
        intent.putExtra("exerciseStory", exerciseStory);
        startActivityForResult(intent, ExerciseIntentFlag.DETAIL_REQUEST);
    }

    @Override
    public void setExerciseStoryAdapterLikeDown(ExerciseStoryAdapter.ExerciseStoryViewHolder holder, int position) {
        exerciseStoryAdapter.setExerciseStoryAdapterLikeUnChecked(holder, position);
    }

    @Override
    public void setExerciseStoryAdapterLikeUp(ExerciseStoryAdapter.ExerciseStoryViewHolder holder, int position) {
        exerciseStoryAdapter.setExerciseStoryAdapterLikeChecked(holder, position);
    }

    @Override
    public void setExerciseStoryAdapterLikeIsChecked(ExerciseStoryAdapter.ExerciseStoryViewHolder holder, int position) {
        exerciseStoryAdapter.setExerciseStoryAdapterLikeIsChecked(holder, position);
    }

    @Override
    public void setExerciseStoryAdapterLikeIsUnChecked(ExerciseStoryAdapter.ExerciseStoryViewHolder holder, int position) {
        exerciseStoryAdapter.setExerciseStoryAdapterLikeIsUnChecked(holder, position);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
    public void setToolbar(View decorView) {
        toolbar = (Toolbar) decorView.findViewById(R.id.toolBar);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_back = (ImageView) toolbar.findViewById(R.id.toolbar_back);
        toolbar_back.setOnClickListener(this);
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
        exerciseStoryPresenter.onScrollChange(difference);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ExerciseIntentFlag.DETAIL_REQUEST:
                if (data != null) {
                    ExerciseStory exerciseStory = (ExerciseStory) data.getSerializableExtra("exerciseStory");
                    int position = exerciseStory.getPosition();

                    switch (resultCode) {
                        case ExerciseIntentFlag.RESULT_OK:
                            exerciseStoryAdapter.setLikeChange(position, exerciseStory.getChecked());
                            exerciseStoryAdapter.notifyItemChanged(position);
                            break;

                        case ExerciseIntentFlag.RESULT_DELETE:
                            exerciseStoryAdapter.setContentDelete(position);
                            exerciseStoryAdapter.notifyItemRemoved(position);
                            exerciseStoryAdapter.notifyItemRangeChanged(position, exerciseStoryAdapter.getItemCount());

                            break;
                    }
                    break;
                }
        }
    }

}
