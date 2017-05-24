package com.demand.well_family.well_family.falldiagnosis.fall.diagnosis.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.SelfDiagnosisCategory;
import com.demand.well_family.well_family.falldiagnosis.fall.diagnosis.presenter.FallDiagnosisPresenter;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-23.
 */

public class FallViewPagerAdapter extends PagerAdapter {
    private FallDiagnosisPresenter fallDiagnosisPresenter;

    private LayoutInflater inflater;
    private TextView tv_viewPager_indicator;
    private ArrayList<SelfDiagnosisCategory> diagnosisCategoryList;    // temp


    public FallViewPagerAdapter(LayoutInflater inflater, ArrayList<SelfDiagnosisCategory> diagnosisCategoryList, FallDiagnosisPresenter fallDiagnosisPresenter) {
        this.diagnosisCategoryList = diagnosisCategoryList;
        this.fallDiagnosisPresenter = fallDiagnosisPresenter;
        this.inflater = inflater;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = inflater.inflate(R.layout.item_fall_diagnosis, null);
        ImageView iv_falldiagnosis_question = (ImageView) view.findViewById(R.id.iv_falldiagnosis_question);
        TextView tv_falldiagnosis_question = (TextView) view.findViewById(R.id.tv_falldiagnosis_question);
        TextView tv_falldiagnosis_num = (TextView) view.findViewById(R.id.tv_falldiagnosis_num);
        Button btn_falldiagnosis_answer_ok = (Button) view.findViewById(R.id.btn_falldiagnosis_answer_ok);
        Button btn_falldiagnosis_answer_no = (Button) view.findViewById(R.id.btn_falldiagnosis_answer_no);

        SelfDiagnosisCategory category = diagnosisCategoryList.get(position);
        tv_falldiagnosis_num.setText(category.getId());
        tv_falldiagnosis_question.setText(category.getName());
        Glide.with(container.getContext()).load(category.getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_falldiagnosis_question);


        btn_falldiagnosis_answer_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fallDiagnosisPresenter.onClickNextView(position);
            }
        });

        btn_falldiagnosis_answer_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fallDiagnosisPresenter.onClickNextView(position);
            }
        });


        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return diagnosisCategoryList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
