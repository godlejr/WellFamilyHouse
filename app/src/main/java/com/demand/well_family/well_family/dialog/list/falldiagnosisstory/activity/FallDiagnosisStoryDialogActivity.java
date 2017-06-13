package com.demand.well_family.well_family.dialog.list.falldiagnosisstory.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dialog.list.falldiagnosisstory.adapter.FallDiagnosisStoryDialogAdapter;
import com.demand.well_family.well_family.dialog.list.falldiagnosisstory.presenter.FallDiagnosisStoryDialogPresenter;
import com.demand.well_family.well_family.dialog.list.falldiagnosisstory.presenter.impl.FallDiagnosisStoryDialogPresenterImpl;
import com.demand.well_family.well_family.dialog.list.falldiagnosisstory.view.FallDiagnosisStoryDialogView;

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

        fallDiagnosisStoryDialogPresenter = new FallDiagnosisStoryDialogPresenterImpl(this);
        fallDiagnosisStoryDialogPresenter.onCreate();
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
}
