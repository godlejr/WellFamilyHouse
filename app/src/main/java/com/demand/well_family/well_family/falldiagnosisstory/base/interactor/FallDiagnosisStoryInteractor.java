package com.demand.well_family.well_family.falldiagnosisstory.base.interactor;

import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.dto.FallDiagnosisStoryInfo;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.falldiagnosisstory.base.adapter.FallDiagnosisStoryAdapter;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-06-01.
 */

public interface FallDiagnosisStoryInteractor {
    void getFallDiagnosisStoryData();

    void setFallDiagnosisStory(FallDiagnosisStory fallDiagnosisStory);

    ArrayList<FallDiagnosisStory> getFallDiagnosisStoryInfoList();

    void setFallDiagnosisStoryInfoList(ArrayList<FallDiagnosisStory> fallDiagnosisStoryInfoList);

    ArrayList<FallDiagnosisStory> getFallDiagnosisStoryList();

    void setFallDiagnosisStoryList(ArrayList<FallDiagnosisStory> fallDiagnosisStoryList);

    FallDiagnosisStory getFallDiagnosisStory();

    User getStoryUser();

    void setStoryUser(User user);

    User getUser();

    void setUser(User user);

    FallDiagnosisStoryInfo getFallDiagnosisStoryInfo();

    void setFallDiagnosisStoryInfo(FallDiagnosisStoryInfo fallDiagnosisStoryInfo);

    void getFallDiagnosisStoryCommentCount(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, FallDiagnosisStory fallDiagnosisStory);

    void getFallDiagnosisStoryLikeCount(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, FallDiagnosisStory fallDiagnosisStory);

    ArrayList<FallDiagnosisStory> getFallDiagnosisStoryListWithOffset();

    void getStoryDataAdded();


    void getFallDiagnosisStoryInfoData(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, FallDiagnosisStory fallDiagnosisStory);

    void getContentLikeCheck(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, FallDiagnosisStory fallDiagnosisStory);

    void setContentLikeUp(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, FallDiagnosisStory fallDiagnosisStory);

    void setContentLikeDown(FallDiagnosisStoryAdapter.FallDiagnosisStoryViewHolder holder, FallDiagnosisStory fallDiagnosisStory);

    void setFallDiagnosisStoryHit(FallDiagnosisStory fallDiagnosisStory);
}
