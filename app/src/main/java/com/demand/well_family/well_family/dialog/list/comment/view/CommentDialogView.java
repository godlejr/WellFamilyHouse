package com.demand.well_family.well_family.dialog.list.comment.view;

import com.demand.well_family.well_family.dialog.list.comment.adapter.CommentDialogAdapter;

/**
 * Created by ㅇㅇ on 2017-04-19.
 */

public interface CommentDialogView {
    void init();

    void setCommentDialogAdapter(CommentDialogAdapter commentDialogAdapter);
    void setCommentCopy();

    String getCommentContent();
    int getActFlag();

    void navigateToCommentModifyActivity();
    void navigateToCommentDeleteActivity();
    void navigateToReportActivity();

    void navigateToBackAfterEdit(int commentPosition, String commentContent, int commentCodeFlag);
    void navigateToBackAfterDelete(int commentPosition, int commentCodeFlag);

    void showMessage(String message);
    void navigateToBack();

}
