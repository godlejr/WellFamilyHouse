package com.demand.well_family.well_family.dialog.list.comment.view;

import com.demand.well_family.well_family.dialog.list.comment.adapter.CommentDialogAdapter;
import com.demand.well_family.well_family.dto.Comment;
import com.demand.well_family.well_family.dto.Report;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-19.
 */

public interface CommentDialogView {
    void init();

    void setCommentDialogAdapterInit(ArrayList<String> commentDialogList);

    void setCommentDialogAdapter(CommentDialogAdapter commentDialogAdapter);

    void setCommentCopied(Comment comment);

    void navigateToCommentEditActivity(Comment comment, int actFlag);

    void navigateToCommentDeleteActivity(Comment comment, int actFlag);

    void navigateToReportActivity(Report report);

    void navigateToBack();

    void navigateToBackAfterEdit(Comment comment, int commentCodeFlag);

    void navigateToBackAfterDelete(Comment comment, int commentCodeFlag);

    void showMessage(String message);
}
