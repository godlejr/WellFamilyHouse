package com.demand.well_family.well_family.dialog.popup.comment.delete.interactor;

import com.demand.well_family.well_family.dto.Comment;
import com.demand.well_family.well_family.dto.User;

/**
 * Created by ㅇㅇ on 2017-05-22.
 */

public interface CommentDeleteInteractor {
    void setCommentDeleted(User user, Comment comment, int actFlag);
}
