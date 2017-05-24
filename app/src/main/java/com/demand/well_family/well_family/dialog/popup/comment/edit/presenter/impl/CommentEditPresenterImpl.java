package com.demand.well_family.well_family.dialog.popup.comment.edit.presenter.impl;

import android.content.Context;
import android.view.View;

import com.demand.well_family.well_family.dialog.popup.comment.edit.interactor.CommentEditInteractor;
import com.demand.well_family.well_family.dialog.popup.comment.edit.interactor.impl.CommentEditInteractorImpl;
import com.demand.well_family.well_family.dialog.popup.comment.edit.presenter.CommentEditPresenter;
import com.demand.well_family.well_family.dialog.popup.comment.edit.view.CommentEditView;
import com.demand.well_family.well_family.dto.Comment;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.util.APIErrorUtil;
import com.demand.well_family.well_family.util.PreferenceUtil;

/**
 * Created by ㅇㅇ on 2017-05-22.
 */

public class CommentEditPresenterImpl implements CommentEditPresenter {
    private CommentEditInteractor commentEditInteractor;
    private CommentEditView commentEditView;
    private PreferenceUtil preferenceUtil;

    public CommentEditPresenterImpl(Context context) {
        this.commentEditInteractor = new CommentEditInteractorImpl(this);
        this.commentEditView = (CommentEditView) context;
        this.preferenceUtil = new PreferenceUtil(context);
    }

    @Override
    public void onSuccessSetCommentEdited(Comment commentAfterEdited) {
        commentEditView.showMessage("댓글이 수정되었습니다.");
        commentEditView.navigateToBackWithComment(commentAfterEdited);
    }

    @Override
    public void onCreate() {
        commentEditView.init();

        View decorView = commentEditView.getDecorView();
        commentEditView.setToolbar(decorView);
        commentEditView.showToolbarTitle("댓글 수정");
    }

    @Override
    public void onClickEdit(Comment commentBeforeEdited, Comment commentAfterEdited, int intentFlag) {
        String commentContentBeforeEdited = commentBeforeEdited.getContent();
        String commentContentAfterEdited = commentAfterEdited.getContent();

        if (commentContentBeforeEdited.equals(commentContentAfterEdited)) {
            commentEditView.navigateToBack();
        } else {
            User user = preferenceUtil.getUserInfo();
            commentEditInteractor.setCommentEdited(user, commentAfterEdited, intentFlag);
        }
    }

    @Override
    public void onNetworkError(APIErrorUtil apiErrorUtil) {
        if (apiErrorUtil == null) {
            commentEditView.showMessage("네트워크 불안정합니다. 다시 시도하세요.");
        } else {
            commentEditView.showMessage(apiErrorUtil.message());
        }
    }
}
