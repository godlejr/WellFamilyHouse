package com.demand.well_family.well_family.dialog.list.comment.presenter;

import com.demand.well_family.well_family.dialog.list.comment.adapter.CommentDialogAdapter;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-19.
 */

public interface CommentDialogPresenter {
    void onCreate();

    ArrayList<String> getCommentDialogList(int actFlag);

    void setCommentDialogAdapter(CommentDialogAdapter commentDialogAdapter);

    void onClickCommentCopy();
    void onClickCommentDialog(int commentDialogPosition);

    void onActivityResultForEditResultOk(int commentPosition, String commentContent, int commentCodeFlag);
    void onActivityResultForDeleteResultOk(int commentPosition, int commentCodeFlag);
}
