package com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.interactor.impl;

import com.demand.well_family.well_family.dto.FallDiagnosisCategory;
import com.demand.well_family.well_family.dto.PhysicalEvaluation;
import com.demand.well_family.well_family.dto.PhysicalEvaluationCategory;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.interactor.CreatePhysicalEvaluationInteractor;
import com.demand.well_family.well_family.falldiagnosis.physicalevaluation.create.presenter.CreatePhysicalEvaluationPresenter;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.FallDiagnosisServerConnection;
import com.demand.well_family.well_family.repository.interceptor.NetworkInterceptor;
import com.demand.well_family.well_family.util.ErrorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ㅇㅇ on 2017-05-25.
 */

public class CreatePhysicalEvaluationInteractorImpl implements CreatePhysicalEvaluationInteractor {
    private CreatePhysicalEvaluationPresenter createPhysicalEvaluationPresenter;

    private Timer timer;
    private TimerTask timerTask;
    private int minute;
    private int second;
    private int millisecond;

    private FallDiagnosisServerConnection fallDiagnosisServerConnection;
    private FallDiagnosisCategory fallDiagnosisCategory;

    private ArrayList<PhysicalEvaluationCategory> physicalEvaluationCategoryList;
    private ArrayList<PhysicalEvaluation> physicalEvaluationList;

    private static final Logger logger = LoggerFactory.getLogger(CreatePhysicalEvaluationInteractorImpl.class);

    public CreatePhysicalEvaluationInteractorImpl(CreatePhysicalEvaluationPresenter createPhysicalEvaluationPresenter) {
        this.createPhysicalEvaluationPresenter = createPhysicalEvaluationPresenter;
        this.physicalEvaluationList = new ArrayList<>();
    }

    @Override
    public ArrayList<PhysicalEvaluationCategory> getPhysicalEvaluationCategoryList() {
        return physicalEvaluationCategoryList;
    }

    @Override
    public void setPhysicalEvaluationCategoryList(ArrayList<PhysicalEvaluationCategory> physicalEvaluationCategoryList) {
        this.physicalEvaluationCategoryList = physicalEvaluationCategoryList;
    }

    @Override
    public FallDiagnosisCategory getFallDiagnosisCategory() {
        return fallDiagnosisCategory;
    }

    @Override
    public void setFallDiagnosisCategory(FallDiagnosisCategory fallDiagnosisCategory) {
        this.fallDiagnosisCategory = fallDiagnosisCategory;
    }

    @Override
    public void getCountDownSecond() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 4; i > 0; i--) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        log(e);
                    }
                    createPhysicalEvaluationPresenter.onSuccessGetCountDownSecond(i);

                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    log(e);
                }
                createPhysicalEvaluationPresenter.onSuccessGetCountDownSecond(0);
            }
        }).start();
    }

    @Override
    public void getTimer() {
        minute = 0;
        second = 0;
        millisecond = 0;

        if (timerTask != null) {
            timerTask.cancel();
        }
        if (timer != null) {
            timer.cancel();
        }

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                millisecond++;
                if (millisecond % 100 == 0 && millisecond != 0) {
                    second++;
                    millisecond = 0;
                }

                if (second % 60 == 0 && second != 0) {
                    minute++;
                    second = 0;
                }
                createPhysicalEvaluationPresenter.onSuccessGetTimer(minute, second, millisecond);
            }
        };
        timer.schedule(timerTask, 10, 10);
    }

    @Override
    public void setTimerPause() {
        timer.cancel();
        timerTask.cancel();
    }

    @Override
    public void getPhysicalEvaluationCategories(User user) {
        String accessToken = user.getAccess_token();

        fallDiagnosisServerConnection = new NetworkInterceptor(accessToken).getFallDiagnosisServer().create(FallDiagnosisServerConnection.class);
        Call<ArrayList<PhysicalEvaluationCategory>> callGetPhysicalEvaluationCategories = fallDiagnosisServerConnection.getPhysicalEvaluationCategories(fallDiagnosisCategory.getId());
        callGetPhysicalEvaluationCategories.enqueue(new Callback<ArrayList<PhysicalEvaluationCategory>>() {
            @Override
            public void onResponse(Call<ArrayList<PhysicalEvaluationCategory>> call, Response<ArrayList<PhysicalEvaluationCategory>> response) {
                if (response.isSuccessful()) {
                    physicalEvaluationCategoryList = response.body();
                    createPhysicalEvaluationPresenter.onSuccessGetPhysicalEvaluationCategories(physicalEvaluationCategoryList);
                } else {
                    createPhysicalEvaluationPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PhysicalEvaluationCategory>> call, Throwable t) {
                log(t);
                createPhysicalEvaluationPresenter.onNetworkError(null);
            }
        });

    }


    private static void log(Throwable throwable) {
        StackTraceElement[] ste = throwable.getStackTrace();
        String className = ste[0].getClassName();
        String methodName = ste[0].getMethodName();
        int lineNumber = ste[0].getLineNumber();
        String fileName = ste[0].getFileName();

        if (LogFlag.printFlag) {
            if (logger.isInfoEnabled()) {
                logger.error("Exception: " + throwable.getMessage());
                logger.error(className + "." + methodName + " " + fileName + " " + lineNumber + " " + "line");
            }
        }
    }

    @Override
    public int getMinute() {
        return minute;
    }

    @Override
    public void setMinute(int minute) {
        this.minute = minute;
    }

    @Override
    public int getSecond() {
        return second;
    }

    @Override
    public void setSecond(int second) {
        this.second = second;
    }

    @Override
    public int getMillisecond() {
        return millisecond;
    }

    @Override
    public void setMillisecond(int millisecond) {
        this.millisecond = millisecond;
    }

    @Override
    public String getTimes(int time) {
        String times = null;
        if (time < 10) {
            times = "0" + time;
        } else {
            times = "" + time;
        }
        return times;
    }

    @Override
    public ArrayList<PhysicalEvaluation> getPhysicalEvaluationList() {
        return physicalEvaluationList;
    }

    @Override
    public void setPhysicalEvaluationList(ArrayList<PhysicalEvaluation> physicalEvaluationList) {
        this.physicalEvaluationList = physicalEvaluationList;
    }

    @Override
    public void setPhysicalEvaluationAdded(PhysicalEvaluation physicalEvaluation) {
        this.physicalEvaluationList.add(physicalEvaluation);
    }
}
