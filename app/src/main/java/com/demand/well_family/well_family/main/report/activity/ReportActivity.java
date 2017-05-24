package com.demand.well_family.well_family.main.report.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.Category;
import com.demand.well_family.well_family.dto.Report;
import com.demand.well_family.well_family.main.report.adapter.ReportAdapter;
import com.demand.well_family.well_family.main.report.presenter.ReportPresenter;
import com.demand.well_family.well_family.main.report.presenter.impl.ReportPresenterImpl;
import com.demand.well_family.well_family.main.report.view.ReportView;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-03-03.
 */

public class ReportActivity extends Activity implements ReportView, View.OnClickListener {
    private ReportPresenter reportPresenter;
    private ReportAdapter reportAdapter;

    private View decorView;
    private TextView toolbarTitle;


    private TextView tv_report_content;
    private TextView tv_report_author_name;
    private RecyclerView rv_report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        int intentFlag = getIntent().getIntExtra("intent_flag", 0);

        Report report = new Report();
        report.setAuthor_name(getIntent().getStringExtra("author_name"));
        report.setWriting_content(getIntent().getStringExtra("writing_content"));
        report.setWriting_category_id(getIntent().getIntExtra("writing_category_id", 0));
        report.setWriting_id(getIntent().getIntExtra("writing_id", 0));


        reportPresenter = new ReportPresenterImpl(this);
        reportPresenter.onCreate(report, intentFlag);

    }

    @Override
    public void init() {
        tv_report_author_name = (TextView) findViewById(R.id.tv_report_author_name);
        tv_report_content = (TextView) findViewById(R.id.tv_report_content);
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
    public void showMessage(String message) {
        Toast.makeText(ReportActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToBack() {
        finish();
    }

    @Override
    public void setReportAdapterInit(ArrayList<Category> reportList) {
        reportAdapter = new ReportAdapter(ReportActivity.this, reportList, R.layout.item_report, reportPresenter);
        rv_report = (RecyclerView) findViewById(R.id.rv_report);
        rv_report.setAdapter(reportAdapter);
        rv_report.setLayoutManager(new LinearLayoutManager(ReportActivity.this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void setAuthorName(String authorName) {
        tv_report_author_name.setText(authorName);
    }

    @Override
    public void setReportContent(String content) {
        tv_report_content.setText(content);
    }

    @Override
    public void setToolbar(View decorView) {
        Toolbar toolbar = (Toolbar) decorView.findViewById(R.id.toolBar);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        ImageView toolbarBack = (ImageView) toolbar.findViewById(R.id.toolbar_back);
        toolbarBack.setOnClickListener(this);
    }

    @Override
    public View getDecorView() {
        if (decorView == null) {
            decorView = this.getWindow().getDecorView();
        }
        return decorView;
    }

    @Override
    public void showToolbarTitle(String title) {
        toolbarTitle.setText(title);
    }


}
