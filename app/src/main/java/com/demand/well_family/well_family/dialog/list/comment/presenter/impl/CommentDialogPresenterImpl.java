package com.demand.well_family.well_family.dialog.list.comment.presenter.impl;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.demand.well_family.well_family.dialog.list.comment.adapter.CommentDialogAdapter;
import com.demand.well_family.well_family.dialog.list.comment.interactor.CommentDialogInteractor;
import com.demand.well_family.well_family.dialog.list.comment.interactor.impl.CommentDialogInteractorImpl;
import com.demand.well_family.well_family.dialog.list.comment.presenter.CommentDialogPresenter;
import com.demand.well_family.well_family.dialog.list.comment.view.CommentDialogView;
import com.demand.well_family.well_family.flag.CommentActFlag;
import com.demand.well_family.well_family.flag.CommentDialogFlag;
import com.demand.well_family.well_family.util.PreferenceUtil;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-19.
 */

public class CommentDialogPresenterImpl implements CommentDialogPresenter {
    private CommentDialogView commentDialogView;
    private CommentDialogInteractor commentDialogInteractor;
    private PreferenceUtil preferenceUtil;

    private Context context;

    public CommentDialogPresenterImpl(Context context) {
        this.commentDialogView = (CommentDialogView) context;
        this.commentDialogInteractor = new CommentDialogInteractorImpl(this);
        this.preferenceUtil = new PreferenceUtil(context);
        this.context = context;
    }

    @Override
    public void onCreate() {
        commentDialogView.init();
    }

    @Override
    public ArrayList<String> getCommentDialogList(int actFlag) {
        ArrayList<String> commentDialogList = new ArrayList<>();
        commentDialogList.add("댓글 복사");

        if (actFlag != CommentActFlag.PUBLIC) {
            commentDialogList.add("댓글 수정");
            commentDialogList.add("댓글 삭제");
        } else {
            commentDialogList.add("신고 하기");
        }

        return commentDialogList;
    }

    @Override
    public void setCommentDialogAdapter(CommentDialogAdapter commentDialogAdapter) {
        commentDialogView.setCommentDialogAdapter(commentDialogAdapter);
    }

    @Override
    public void onClickCommentCopy() {
        String commentContent = commentDialogView.getCommentContent();

        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("label", commentContent);
        clipboardManager.setPrimaryClip(clipData);

        commentDialogView.showMessage("클립보드에 복사되었습니다.");
        commentDialogView.navigateToBack();
    }


    @Override
    public void onClickCommentDialog(int commentDialogPosition) {
        if (commentDialogPosition == CommentDialogFlag.COPY) {
            commentDialogView.setCommentCopy();
        }

        if (commentDialogPosition == CommentDialogFlag.MODIFY_OR_REPORT) {
            int actFlag = commentDialogView.getActFlag();

            if (actFlag != CommentActFlag.PUBLIC) {
                commentDialogView.navigateToCommentModifyActivity();
            } else {
                commentDialogView.navigateToReportActivity();
            }
        }

        if (commentDialogPosition == CommentDialogFlag.DELETE) {
            commentDialogView.navigateToCommentDeleteActivity();
        }
    }

    @Override
    public void onActivityResultForEditResultOk(int commentPosition, String commentContent, int commentCodeFlag) {
        commentDialogView.navigateToBackAfterEdit(commentPosition, commentContent, commentCodeFlag);
    }

    @Override
    public void onActivityResultForDeleteResultOk(int commentPosition, int commentCodeFlag) {
        commentDialogView.navigateToBackAfterDelete(commentPosition, commentCodeFlag);
    }
}
