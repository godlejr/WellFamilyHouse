package com.demand.well_family.well_family.falldiagnosis.fall.diagnosis.adapter;

import android.content.Context;
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
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;
import com.demand.well_family.well_family.falldiagnosis.fall.diagnosis.flag.SelfDiagnosisCodeFlag;
import com.demand.well_family.well_family.falldiagnosis.fall.diagnosis.presenter.SelfDiagnosisPresenter;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-23.
 */

public class SelfDiagnosisViewPagerAdapter extends PagerAdapter  {
    private SelfDiagnosisPresenter selfDiagnosisPresenter;

    private LayoutInflater inflater;
    private TextView tv_viewPager_indicator;
    private ArrayList<FallDiagnosisContentCategory> fallDiagnosisContentCategoryList;    // temp


    public SelfDiagnosisViewPagerAdapter(LayoutInflater inflater, ArrayList<FallDiagnosisContentCategory> fallDiagnosisContentCategoryList, SelfDiagnosisPresenter selfDiagnosisPresenter) {
        this.fallDiagnosisContentCategoryList = fallDiagnosisContentCategoryList;
        this.selfDiagnosisPresenter = selfDiagnosisPresenter;
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

        FallDiagnosisContentCategory category = fallDiagnosisContentCategoryList.get(position);
        tv_falldiagnosis_num.setText(String.valueOf(category.getId()));
        tv_falldiagnosis_question.setText(category.getName());

        Context context = inflater.getContext();

        Glide.with(context).load(context.getString(R.string.cloud_front_self_diagnosis) + category.getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_falldiagnosis_question);

        final int categorySize = getCount();
        btn_falldiagnosis_answer_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selfDiagnosisPresenter.onClickAnswer(position,categorySize, SelfDiagnosisCodeFlag.NO);
            }
        });
        btn_falldiagnosis_answer_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selfDiagnosisPresenter.onClickAnswer(position,categorySize, SelfDiagnosisCodeFlag.YES);
            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return fallDiagnosisContentCategoryList.size();
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
