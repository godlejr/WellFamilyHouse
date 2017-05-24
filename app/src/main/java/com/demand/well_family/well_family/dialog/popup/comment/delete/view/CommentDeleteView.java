package com.demand.well_family.well_family.dialog.popup.comment.delete.view;

/**
 * Created by ㅇㅇ on 2017-05-22.
 */

public interface CommentDeleteView {
    void init();

    void navigateToBackForResultCanceled();
    void navigateToBackForResultOk();

    void showMessage(String message);
}
