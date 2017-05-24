package com.demand.well_family.well_family.dialog.popup.comment.edit.interactor;

import com.demand.well_family.well_family.dto.Comment;
import com.demand.well_family.well_family.dto.User;

/**
 * Created by ㅇㅇ on 2017-05-22.
 */

public interface CommentEditInteractor {
    void setCommentEdited(User user, Comment comment, int actFlag);
}
