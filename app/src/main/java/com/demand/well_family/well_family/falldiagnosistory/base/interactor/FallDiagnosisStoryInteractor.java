package com.demand.well_family.well_family.falldiagnosistory.base.interactor;

import com.demand.well_family.well_family.dto.User;

/**
 * Created by ㅇㅇ on 2017-06-01.
 */

public interface FallDiagnosisStoryInteractor {
    void getFallDiagnosisStory ();
    User getUser();
    void setUser(User user);

    void getFallDiagnosisStoryCommentCount();
    void getFallDiagnosisStoryLikeCount();
}
