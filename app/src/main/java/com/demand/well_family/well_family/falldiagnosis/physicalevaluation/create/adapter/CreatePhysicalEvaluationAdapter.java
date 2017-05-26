package com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;
import com.demand.well_family.well_family.dto.PhysicalEvaluationCategory;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.presenter.CreatePhysicalEvaluationPresenter;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-05-26.
 */

public class CreatePhysicalEvaluationAdapter extends PagerAdapter {
    private ArrayList<PhysicalEvaluationCategory> physicalEvaluationCategoryList;
    private CreatePhysicalEvaluationPresenter createPhysicalEvaluationPresenter;
    private LayoutInflater inflater;

    public CreatePhysicalEvaluationAdapter(LayoutInflater inflater, ArrayList<PhysicalEvaluationCategory> physicalEvaluationCategoryList, CreatePhysicalEvaluationPresenter createPhysicalEvaluationPresenter) {
        this.inflater = inflater;
        this.physicalEvaluationCategoryList = physicalEvaluationCategoryList;
        this.createPhysicalEvaluationPresenter = createPhysicalEvaluationPresenter;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.item_physicalevaluation, null);
        TextView tv_physicalevaluation_content = (TextView) view.findViewById(R.id.tv_physicalevaluation_content);
        ImageView iv_physicalevaluation_img = (ImageView) view.findViewById(R.id.iv_physicalevaluation_img);

        PhysicalEvaluationCategory physicalEvaluationCategory = physicalEvaluationCategoryList.get(position);

        Context context = inflater.getContext();
        Glide.with(context).load(context.getString(R.string.cloud_front_self_diagnosis) + physicalEvaluationCategory.getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_physicalevaluation_img);

        tv_physicalevaluation_content.setText(physicalEvaluationCategory.getContent());

        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return physicalEvaluationCategoryList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {((ViewPager) container).removeView((View) object); }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
