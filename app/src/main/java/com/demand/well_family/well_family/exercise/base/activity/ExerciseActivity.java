package com.demand.well_family.well_family.exercise.base.activity;

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
import com.demand.well_family.well_family.dto.ExerciseCategory;
import com.demand.well_family.well_family.exercise.base.adapter.ExerciseAdapter;
import com.demand.well_family.well_family.exercise.base.presetner.ExercisePresenter;
import com.demand.well_family.well_family.exercise.base.presetner.impl.ExercisePresenterImpl;
import com.demand.well_family.well_family.exercise.base.view.ExerciseView;
import com.demand.well_family.well_family.exercise.create.activity.CreateExerciseActivity;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-06-23.
 */

public class ExerciseActivity extends Activity implements ExerciseView, View.OnClickListener {
    private ExercisePresenter exercisePresenter;

    private View decorView;
    private Toolbar toolbar;
    private ImageView toolbar_back;
    private TextView toolbar_title;

    private RecyclerView rv_exercise;
    private ExerciseAdapter exerciseAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        exercisePresenter = new ExercisePresenterImpl(this);
        exercisePresenter.onCreate();
    }

    @Override
    public void init() {
        rv_exercise = (RecyclerView) findViewById(R.id.rv_exercise);

        exercisePresenter.onLoadData();
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
    public void navigateToCreateExerciseActivity(ExerciseCategory exerciseCategory) {
        Intent intent = new Intent(this, CreateExerciseActivity.class);
        intent.putExtra("exerciseCategory", exerciseCategory);
        startActivity(intent);
    }

    @Override
    public void setExerciseAdapterItem(ArrayList<ExerciseCategory> exerciseCategoryList) {
        exerciseAdapter = new ExerciseAdapter(exercisePresenter, exerciseCategoryList, this);
        rv_exercise.setAdapter(exerciseAdapter);
        rv_exercise.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
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
