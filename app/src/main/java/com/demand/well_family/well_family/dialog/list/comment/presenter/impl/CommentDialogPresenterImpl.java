package com.demand.well_family.well_family.dialog.list.comment.presenter.impl;

import android.content.Context;

import com.demand.well_family.well_family.dialog.list.comment.adapter.CommentDialogAdapter;
import com.demand.well_family.well_family.dialog.list.comment.flag.CommentActFlag;
import com.demand.well_family.well_family.dialog.list.comment.flag.CommentDialogFlag;
import com.demand.well_family.well_family.dialog.list.comment.interactor.CommentDialogInteractor;
import com.demand.well_family.well_family.dialog.list.comment.interactor.impl.CommentDialogInteractorImpl;
import com.demand.well_family.well_family.dialog.list.comment.presenter.CommentDialogPresenter;
import com.demand.well_family.well_family.dialog.list.comment.view.CommentDialogView;
import com.demand.well_family.well_family.dto.Comment;
import com.demand.well_family.well_family.dto.Report;
import com.demand.well_family.well_family.util.PreferenceUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-19.
 */

public class CommentDialogPresenterImpl implements CommentDialogPresenter {
    private CommentDialogView commentDialogView;
    private CommentDialogInteractor commentDialogInteractor;
    private PreferenceUtil preferenceUtil;

    public CommentDialogPresenterImpl(Context context) {
        this.commentDialogView = (CommentDialogView) context;
        this.commentDialogInteractor = new CommentDialogInteractorImpl(this);
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onCreate(Report report, Comment comment, int authorIsMe) {
        commentDialogInteractor.setReport(report);
        commentDialogInteractor.setComment(comment);
        commentDialogInteractor.setAuthorIsMe(authorIsMe);

        commentDialogView.init();
    }

    @Override
    public void onLoadData() {
        int authorIsMe = commentDialogInteractor.getAuthorIsMe();
        ArrayList<String> commentDialogList = commentDialogInteractor.getCommentDialog(authorIsMe);

        commentDialogView.setCommentDialogAdapterInit(commentDialogList);
    }

    @Override
    public void setCommentDialogAdapter(CommentDialogAdapter commentDialogAdapter) {
        commentDialogView.setCommentDialogAdapter(commentDialogAdapter);
    }

    @Override
    public void onClickCommentCopy() {
        commentDialogView.showMessage("클립보드에 복사되었습니다.");
        commentDialogView.navigateToBack();
    }

    @Override
    public void onClickCommentDialog(int commentDialogPosition) {
        Comment comment = commentDialogInteractor.getComment();

        if (commentDialogPosition == CommentDialogFlag.COPY) {
            commentDialogView.setCommentCopied(comment);
        }

        if (commentDialogPosition == CommentDialogFlag.MODIFY_OR_REPORT) {
            setCommentModifyOrReport();
        }

        if (commentDialogPosition == CommentDialogFlag.DELETE) {
            int authorIsMe = commentDialogInteractor.getAuthorIsMe();
            commentDialogView.navigateToCommentDeleteActivity(comment, authorIsMe);
        }
    }

    @Override
    public void setCommentModifyOrReport() {
        int authorIsMe = commentDialogInteractor.getAuthorIsMe();
        Report report = commentDialogInteractor.getReport();
        Comment comment = commentDialogInteractor.getComment();

        if (authorIsMe != CommentActFlag.PUBLIC) {
            commentDialogView.navigateToCommentEditActivity(comment, authorIsMe);
        } else {
            commentDialogView.navigateToReportActivity(report);
        }
    }

    @Override
    public void onActivityResultForEditResultOk(int commentCodeFlag, String editedCommentContent) {
        commentDialogInteractor.getComment().setContent(editedCommentContent);
        Comment comment = commentDialogInteractor.getComment();
        commentDialogView.navigateToBackAfterEdit(comment, commentCodeFlag);
    }

    @Override
    public void onActivityResultForDeleteResultOk(int commentCodeFlag) {
        Comment comment = commentDialogInteractor.getComment();
        commentDialogView.navigateToBackAfterDelete(comment, commentCodeFlag);
    }
}
