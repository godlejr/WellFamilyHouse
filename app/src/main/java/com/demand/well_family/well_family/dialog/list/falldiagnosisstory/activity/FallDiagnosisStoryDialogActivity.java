package com.demand.well_family.well_family.dialog.list.falldiagnosisstory.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.widget.Toast;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dialog.list.falldiagnosisstory.adapter.FallDiagnosisStoryDialogAdapter;
import com.demand.well_family.well_family.dialog.list.falldiagnosisstory.presenter.FallDiagnosisStoryDialogPresenter;
import com.demand.well_family.well_family.dialog.list.falldiagnosisstory.presenter.impl.FallDiagnosisStoryDialogPresenterImpl;
import com.demand.well_family.well_family.dialog.list.falldiagnosisstory.view.FallDiagnosisStoryDialogView;
import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.dto.FallDiagnosisStoryInfo;
import com.demand.well_family.well_family.falldiagnosisstory.flag.FallDiagnosisStoryCodeFlag;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-06-08.
 */

public class FallDiagnosisStoryDialogActivity extends Activity implements FallDiagnosisStoryDialogView {
    private FallDiagnosisStoryDialogPresenter fallDiagnosisStoryDialogPresenter;

    private RecyclerView rv_dialog_list;
    private FallDiagnosisStoryDialogAdapter fallDiagnosisStoryDialogAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_list);
        getWindow().setLayout(android.view.WindowManager.LayoutParams.WRAP_CONTENT, android.view.WindowManager.LayoutParams.WRAP_CONTENT);

        FallDiagnosisStoryInfo fallDiagnosisStoryInfo = (FallDiagnosisStoryInfo) getIntent().getSerializableExtra("fallDiagnosisStoryInfo");
        FallDiagnosisStory fallDiagnosisStory = (FallDiagnosisStory) getIntent().getSerializableExtra("fallDiagnosisStory");

        fallDiagnosisStoryDialogPresenter = new FallDiagnosisStoryDialogPresenterImpl(this);
        fallDiagnosisStoryDialogPresenter.onCreate(fallDiagnosisStory, fallDiagnosisStoryInfo);
    }

    @Override
    public void init() {
        rv_dialog_list = (RecyclerView) findViewById(R.id.rv_dialog_list);
        rv_dialog_list.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        fallDiagnosisStoryDialogPresenter.onLoadData();
    }

    @Override
    public void setFallDiagnosisStoryDialogAdapterList(ArrayList<String> dialogList) {
        fallDiagnosisStoryDialogAdapter = new FallDiagnosisStoryDialogAdapter(this, dialogList, fallDiagnosisStoryDialogPresenter);
        rv_dialog_list.setAdapter(fallDiagnosisStoryDialogAdapter);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToBackForResultOk(FallDiagnosisStory fallDiagnosisStory) {
        Intent intent = getIntent();
        intent.putExtra("fallDiagnosisStory", fallDiagnosisStory);
        setResult(FallDiagnosisStoryCodeFlag.RESULT_OK, intent);
        finish();
    }
}
