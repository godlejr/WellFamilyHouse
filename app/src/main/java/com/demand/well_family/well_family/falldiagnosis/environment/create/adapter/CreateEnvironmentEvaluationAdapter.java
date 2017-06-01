package com.demand.well_family.well_family.falldiagnosis.environment.create.adapter;

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
import com.demand.well_family.well_family.dto.EnvironmentEvaluationCategory;
import com.demand.well_family.well_family.falldiagnosis.environment.create.flag.CreateEnvironmentEvaluationCodeFlag;
import com.demand.well_family.well_family.falldiagnosis.environment.create.presenter.CreateEnvironmentEvaluationPresenter;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-30.
 */

public class CreateEnvironmentEvaluationAdapter extends PagerAdapter {
    private CreateEnvironmentEvaluationPresenter createEnvironmentEvaluationPresenter;
    private ArrayList<EnvironmentEvaluationCategory> environmentEvaluationCategoryList;
    private final LayoutInflater inflater;

    public CreateEnvironmentEvaluationAdapter(LayoutInflater inflater, ArrayList<EnvironmentEvaluationCategory> environmentEvaluationCategoryList, CreateEnvironmentEvaluationPresenter createEnvironmentEvaluationPresenter) {
        this.createEnvironmentEvaluationPresenter = createEnvironmentEvaluationPresenter;
        this.environmentEvaluationCategoryList = environmentEvaluationCategoryList;
        this.inflater = inflater;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = inflater.inflate(R.layout.item_create_environment_evaluation, null);
        TextView tv_environment_evaluation_num = (TextView) view.findViewById(R.id.tv_environment_evaluation_num);
        TextView tv_environment_evaluation_question = (TextView) view.findViewById(R.id.tv_environment_evaluation_question);
        ImageView iv_environment_evaluation_question = (ImageView) view.findViewById(R.id.iv_environment_evaluation_question);
        Button btn_environment_evaluation_answer_ok = (Button) view.findViewById(R.id.btn_environment_evaluation_answer_ok);
        Button btn_environment_evaluation_answer_no = (Button) view.findViewById(R.id.btn_environment_evaluation_answer_no);

        final EnvironmentEvaluationCategory environmentEvaluationCategory = environmentEvaluationCategoryList.get(position);
        tv_environment_evaluation_num.setText(String.valueOf(position + 1));
        tv_environment_evaluation_question.setText(environmentEvaluationCategory.getName());
        Context context = inflater.getContext();
        Glide.with(context).load(context.getString(R.string.cloud_front_self_diagnosis) + environmentEvaluationCategory.getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_environment_evaluation_question);

        final int environmentEvaluationCategorySize = environmentEvaluationCategoryList.size();
        btn_environment_evaluation_answer_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEnvironmentEvaluationPresenter.onClickAnswer(position, environmentEvaluationCategorySize, environmentEvaluationCategory.getId() , CreateEnvironmentEvaluationCodeFlag.YES);
            }
        });

        btn_environment_evaluation_answer_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEnvironmentEvaluationPresenter.onClickAnswer(position, environmentEvaluationCategorySize,environmentEvaluationCategory.getId() , CreateEnvironmentEvaluationCodeFlag.NO);
            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return environmentEvaluationCategoryList.size();
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
