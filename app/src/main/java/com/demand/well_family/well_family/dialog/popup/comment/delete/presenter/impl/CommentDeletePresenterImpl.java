package com.demand.well_family.well_family.dialog.popup.comment.delete.presenter.impl;

import android.content.Context;

import com.demand.well_family.well_family.dialog.popup.comment.delete.interactor.CommentDeleteInteractor;
import com.demand.well_family.well_family.dialog.popup.comment.delete.interactor.impl.CommentDeleteInteractorImpl;
import com.demand.well_family.well_family.dialog.popup.comment.delete.presenter.CommentDeletePresenter;
import com.demand.well_family.well_family.dialog.popup.comment.delete.view.CommentDeleteView;
import com.demand.well_family.well_family.dto.Comment;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.PreferenceUtil;

/**
 * Created by ㅇㅇ on 2017-05-22.
 */

public class CommentDeletePresenterImpl implements CommentDeletePresenter{
    private CommentDeleteInteractor commentDeleteInteractor;
    private CommentDeleteView commentDeleteView;
    private PreferenceUtil preferenceUtil;

    @Override
    public void onCreate() {
        commentDeleteView.init();
    }


    @Override
    public void onSuccessSetCommentDeleted() {
        commentDeleteView.showMessage("댓글이 삭제되었습니다.");
        commentDeleteView.navigateToBackForResultOk();
    }

    public CommentDeletePresenterImpl(Context context) {
        this.commentDeleteInteractor = new CommentDeleteInteractorImpl(this);
        this.commentDeleteView = (CommentDeleteView)context;
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onClickDelete(Comment comment, int intentFlag) {
        User user = preferenceUtil.getUserInfo();
        commentDeleteInteractor.setCommentDeleted(user, comment, intentFlag);
    }


    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            commentDeleteView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            commentDeleteView.showMessage(apiErrorUtil.message());
        }
    }
}
