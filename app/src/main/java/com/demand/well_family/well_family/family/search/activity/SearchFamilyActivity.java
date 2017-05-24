package com.demand.well_family.well_family.family.search.activity;

import android.app.Activity;
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
import com.demand.well_family.well_family.dto.FamilyInfoForFamilyJoin;
import com.demand.well_family.well_family.family.search.adapter.FamilyAdapter;
import com.demand.well_family.well_family.family.search.presenter.SearchFamilyPresenter;
import com.demand.well_family.well_family.family.search.presenter.impl.SearchFamilyPresenterImpl;
import com.demand.well_family.well_family.family.search.view.SearchFamilyView;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-03.
 */

public class SearchFamilyActivity extends Activity implements SearchFamilyView, View.OnClickListener {
    private SearchFamilyPresenter searchFamilyPresenter;


    private Button btn_find_family;
    private EditText et_find_family;
    private FamilyAdapter familyAdapter;

    private View decorView;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_family);

        searchFamilyPresenter = new SearchFamilyPresenterImpl(this);
        searchFamilyPresenter.onCreate();
    }

    @Override
    public void init() {
        btn_find_family = (Button) findViewById(R.id.btn_find_family);
        et_find_family = (EditText) findViewById(R.id.et_find_family);

        btn_find_family.setOnClickListener(this);

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(SearchFamilyActivity.this, message, Toast.LENGTH_SHORT).show();
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
    public void showToolbarTitle(String title) {
        toolbarTitle.setText(title);
    }

    @Override
    public void setFamilyAdapter(ArrayList<FamilyInfoForFamilyJoin> familyList) {
        RecyclerView rv_find_family = (RecyclerView) findViewById(R.id.rv_find_family);
        familyAdapter = new FamilyAdapter(familyList, R.layout.item_find_family, SearchFamilyActivity.this, searchFamilyPresenter);
        rv_find_family.setAdapter(familyAdapter);
        rv_find_family.setLayoutManager(new LinearLayoutManager(SearchFamilyActivity.this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void setFamilyStateButtonForMe(FamilyAdapter.FamilyViewHolder holder) {
        familyAdapter.setFamilyStateButtonForMe(holder);
    }

    @Override
    public void setFamilyStateButtonForJoin(FamilyAdapter.FamilyViewHolder holder, FamilyInfoForFamilyJoin familyFound) {
        familyAdapter.setFamilyStateButtonForJoin(holder, familyFound);
    }

    @Override
    public void setFamilyStateButtonForStay(FamilyAdapter.FamilyViewHolder holder) {
        familyAdapter.setFamilyStateButtonForStay(holder);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_find_family:
                String keyword = et_find_family.getText().toString();
                searchFamilyPresenter.onClickSetFamilySearched(keyword);
                break;

            case R.id.toolbar_back:
                finish();
                break;
        }
    }


}
