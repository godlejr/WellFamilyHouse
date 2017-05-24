package com.demand.well_family.well_family.dialog.popup.comment.edit.view;

import android.view.View;

import com.demand.well_family.well_family.dto.Comment;

/**
 * Created by ㅇㅇ on 2017-05-22.
 */

public interface CommentEditView {
    void init();

    void setToolbar(View decorView);
    View getDecorView();
    void showToolbarTitle(String title);

    void showMessage(String message);

    void navigateToBack();
    void navigateToBackWithComment(Comment commentAfterEdited);
}
