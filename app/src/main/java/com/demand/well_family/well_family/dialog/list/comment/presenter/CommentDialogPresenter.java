package com.demand.well_family.well_family.dialog.list.comment.presenter;

import com.demand.well_family.well_family.dialog.list.comment.adapter.CommentDialogAdapter;
import com.demand.well_family.well_family.dto.Comment;
import com.demand.well_family.well_family.dto.Report;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-19.
 */

public interface CommentDialogPresenter {
    void onCreate(Report report, Comment comment, int authorIsMe);

    void onLoadData();

    void setCommentDialogAdapter(CommentDialogAdapter commentDialogAdapter);

    void onClickCommentCopy();

    void onClickCommentDialog(int commentDialogPosition);

    void setCommentModifyOrReport();

    void onActivityResultForEditResultOk(int commentCodeFlag, String editedCommentContent);

    void onActivityResultForDeleteResultOk(int commentCodeFlag);
}
