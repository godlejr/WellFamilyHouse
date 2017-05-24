package com.demand.well_family.well_family.falldiagnosis.environment.interactor.impl;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.Evaluation;
import com.demand.well_family.well_family.falldiagnosis.environment.interactor.EnvironmentEvaluationInteractor;
import com.demand.well_family.well_family.falldiagnosis.environment.presenter.EnvironmentEvaluationPresenter;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-24.
 */

public class EnvironmentEvaluationInteractorImpl implements EnvironmentEvaluationInteractor {
    private EnvironmentEvaluationPresenter dangerEvaluationPresenter;

    public EnvironmentEvaluationInteractorImpl(EnvironmentEvaluationPresenter dangerEvaluationPresenter) {
        this.dangerEvaluationPresenter = dangerEvaluationPresenter;
    }

    @Override
    public ArrayList<Evaluation> getDangerEvaluationList() {
        ArrayList<Evaluation> dangerEvaluationList = new ArrayList<>();
        dangerEvaluationList.add(new Evaluation("현관", R.drawable.danger_evaluation_door));
        dangerEvaluationList.add(new Evaluation("거실", R.drawable.danger_evaluation_living));
        dangerEvaluationList.add(new Evaluation("안방", R.drawable.danger_evaluation_room));
        dangerEvaluationList.add(new Evaluation("계단", R.drawable.danger_evaluation_stairs));
        dangerEvaluationList.add(new Evaluation("부엌", R.drawable.danger_evaluation_kitchen));
        dangerEvaluationList.add(new Evaluation("화장실", R.drawable.danger_evaluation_bathroom));
        dangerEvaluationList.add(new Evaluation("베란다", R.drawable.danger_evaluation_balcony));

        return dangerEvaluationList;
    }
}