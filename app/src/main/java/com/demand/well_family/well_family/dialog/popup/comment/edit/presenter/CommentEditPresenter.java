package com.demand.well_family.well_family.dialog.popup.comment.edit.presenter;

import com.demand.well_family.well_family.dto.Comment;
import com.demand.well_family.well_family.util.APIErrorUtil;

/**
 * Created by ㅇㅇ on 2017-05-22.
 */

public interface CommentEditPresenter {
    void onCreate();

    void onClickEdit(Comment commentBeforeEdited, Comment commentAfterEdited, int actFlag);
    void onSuccessSetCommentEdited(Comment commentAfterEdited);
    void onNetworkError(APIErrorUtil apiErrorUtil);

}
