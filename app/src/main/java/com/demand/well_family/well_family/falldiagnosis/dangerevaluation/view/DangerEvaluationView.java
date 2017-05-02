package com.demand.well_family.well_family.falldiagnosis.dangerevaluation.view;

import android.view.View;

import com.demand.well_family.well_family.dto.Evaluation;
import com.demand.well_family.well_family.falldiagnosis.dangerevaluation.adapter.DangerEvaluationAdapter;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-24.
 */

public interface DangerEvaluationView {
    void init();
    void setToolbar(View decorView);
    void showToolbarTitle(String title);
    View getDecorView();

    void setDangerEvaluationList(ArrayList<Evaluation> dangerEvaluationList);
    void setDangerEvaluationAdapter(DangerEvaluationAdapter dangerEvaluationAdapter);
}
