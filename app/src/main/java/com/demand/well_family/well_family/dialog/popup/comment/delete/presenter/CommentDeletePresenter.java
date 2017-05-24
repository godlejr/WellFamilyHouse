package com.demand.well_family.well_family.dialog.popup.comment.delete.presenter;

import com.demand.well_family.well_family.dto.Comment;
import com.demand.well_family.well_family.util.APIErrorUtil;

/**
 * Created by ㅇㅇ on 2017-05-22.
 */

public interface CommentDeletePresenter {
    void onCreate();

    void onClickDelete(Comment comment, int intentFlag);
    void onSuccessSetCommentDeleted();

    void onNetworkError(APIErrorUtil apiErrorUtil);
}
