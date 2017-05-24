package com.demand.well_family.well_family.falldiagnosis.fall.diagnosis.presenter;

/**
 * Created by ㅇㅇ on 2017-05-23.
 */

public interface FallDiagnosisPresenter {
    void onCreate();

    void setNextView(int page);
    void setPreviousView(int page);

    void onClickNextView(int page);
}
