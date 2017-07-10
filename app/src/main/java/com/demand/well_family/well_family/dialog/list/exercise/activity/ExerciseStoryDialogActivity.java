package com.demand.well_family.well_family.dialog.list.exercise.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.widget.Toast;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dialog.list.exercise.adapter.ExerciseStoryDialogAdapter;
import com.demand.well_family.well_family.dialog.list.exercise.presenter.ExerciseStoryDialogPresenter;
import com.demand.well_family.well_family.dialog.list.exercise.presenter.impl.ExerciseStoryDialogPresenterImpl;
import com.demand.well_family.well_family.dialog.list.exercise.view.ExerciseStoryDialogView;
import com.demand.well_family.well_family.dto.ExerciseStory;
import com.demand.well_family.well_family.falldiagnosisstory.flag.FallDiagnosisStoryCodeFlag;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-07-07.
 */

public class ExerciseStoryDialogActivity extends Activity implements ExerciseStoryDialogView{
    private ExerciseStoryDialogPresenter exerciseStoryDialogPresenter;

    private RecyclerView rv_dialog_list;
    private ExerciseStoryDialogAdapter exerciseStoryDialogAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_list);
        getWindow().setLayout(android.view.WindowManager.LayoutParams.WRAP_CONTENT, android.view.WindowManager.LayoutParams.WRAP_CONTENT);

        ExerciseStory exerciseStory = (ExerciseStory)getIntent().getSerializableExtra("exerciseStory");

        exerciseStoryDialogPresenter = new ExerciseStoryDialogPresenterImpl(this);
        exerciseStoryDialogPresenter.onCreate(exerciseStory);
    }

    @Override
    public void init() {
        rv_dialog_list = (RecyclerView) findViewById(R.id.rv_dialog_list);
        rv_dialog_list.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        exerciseStoryDialogPresenter.onLoadData();
    }

    @Override
    public void setExerciseStoryDialogAdapterList(ArrayList<String> dialogList) {
        exerciseStoryDialogAdapter = new ExerciseStoryDialogAdapter(this, dialogList, exerciseStoryDialogPresenter);
        rv_dialog_list.setAdapter(exerciseStoryDialogAdapter);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToBackForResultOk(ExerciseStory fallDiagnosisStory) {
        Intent intent = getIntent();
        intent.putExtra("fallDiagnosisStory", fallDiagnosisStory);
        setResult(FallDiagnosisStoryCodeFlag.RESULT_OK, intent);
        finish();
    }
}
