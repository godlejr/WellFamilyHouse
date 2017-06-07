package com.demand.well_family.well_family.falldiagnosistory.base.presenter;

import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.dto.FallDiagnosisStoryInfo;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.falldiagnosistory.base.adapter.FallDiagnosisStoryAdapter;
import com.demand.well_family.well_family.util.APIErrorUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-06-01.
 */

public interface FallDiagnosisStoryPresenter {
    void onCreate(User user);

    void onNetworkError(APIErrorUtil apiErrorUtil);

    void onLoadData();

    void onSuccessGetFallDiagnosisStory();

    void onSuccessSetContentLikeDown(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, int position);

    void onSuccessGetContentLikeCheck(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, int isChecked, int position);

    void onSuccessSetContentLikeUp(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, int position);

    void onSuccessGetFallDiagnosisStoryCommentCount(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, int count);

    void onSuccessGetFallDiagnosisStoryLikeCount(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, int count);

    void onSuccessGetFallDiagnosisStoryInfo(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, FallDiagnosisStoryInfo fallDiagnosisStoryInfo, FallDiagnosisStory fallDiagnosisStory);

    void onLoadContent(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, FallDiagnosisStory fallDiagnosisStory );

    void onClickContentBody(FallDiagnosisStory fallDiagnosisStory);


    void onScrollChange(int difference);

    void onGettingStoryDataAdded();

    void onSuccessGetStoryDataAdded(int i);

    void onSuccessThreadRun();

    void onCheckedChangeForLike(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, FallDiagnosisStory fallDiagnosisStory , boolean isChecked);

    void setResult(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder);

    void setScore(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, FallDiagnosisStory fallDiagnosisStory);
}
