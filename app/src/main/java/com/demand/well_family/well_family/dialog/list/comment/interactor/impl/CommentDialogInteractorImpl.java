package com.demand.well_family.well_family.dialog.list.comment.interactor.impl;

import com.demand.well_family.well_family.dialog.list.comment.flag.CommentActFlag;
import com.demand.well_family.well_family.dialog.list.comment.interactor.CommentDialogInteractor;
import com.demand.well_family.well_family.dialog.list.comment.presenter.CommentDialogPresenter;
import com.demand.well_family.well_family.dto.Comment;
import com.demand.well_family.well_family.dto.Report;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-19.
 */

public class CommentDialogInteractorImpl implements CommentDialogInteractor {
    private CommentDialogPresenter commentDialogPresenter;

    private Report report;
    private Comment comment;
    private int authorIsMe;

    public CommentDialogInteractorImpl(CommentDialogPresenter commentDialogPresenter) {
        this.commentDialogPresenter = commentDialogPresenter;
    }

    @Override
    public Report getReport() {
        return this.report;
    }

    @Override
    public void setReport(Report report) {
        this.report = report;
    }

    @Override
    public Comment getComment() {
        return comment;
    }

    @Override
    public void setComment(Comment comment) {
        this.comment = comment;
    }

    @Override
    public int getAuthorIsMe() {
        return authorIsMe;
    }

    @Override
    public void setAuthorIsMe(int authorIsMe) {
        this.authorIsMe = authorIsMe;
    }

    @Override
    public ArrayList<String> setCommentDialogInit(int authorIsMe) {
        ArrayList<String> commentDialogList = new ArrayList<>();
        commentDialogList.add("댓글 복사");

        if (authorIsMe != CommentActFlag.PUBLIC) {
            commentDialogList.add("댓글 수정");
            commentDialogList.add("댓글 삭제");
        } else {
            commentDialogList.add("신고 하기");
        }

        return commentDialogList;
    }
}
