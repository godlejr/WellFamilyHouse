package com.demand.well_family.well_family.dialog.list.comment.interactor.impl;

import com.demand.well_family.well_family.dialog.list.comment.interactor.CommentDialogInteractor;
import com.demand.well_family.well_family.dialog.list.comment.presenter.CommentDialogPresenter;

/**
 * Created by ㅇㅇ on 2017-04-19.
 */

public class CommentDialogInteractorImpl implements CommentDialogInteractor{
    private CommentDialogPresenter commentDialogPresenter;

    public CommentDialogInteractorImpl(CommentDialogPresenter commentDialogPresenter) {
        this.commentDialogPresenter = commentDialogPresenter;
    }
}
