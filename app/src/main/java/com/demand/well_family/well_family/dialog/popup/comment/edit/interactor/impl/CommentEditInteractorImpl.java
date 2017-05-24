package com.demand.well_family.well_family.dialog.popup.comment.edit.interactor.impl;

import com.demand.well_family.well_family.dialog.popup.comment.edit.interactor.CommentEditInteractor;
import com.demand.well_family.well_family.dialog.popup.comment.edit.presenter.CommentEditPresenter;
import com.demand.well_family.well_family.dto.Comment;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.CommentServerConnection;
import com.demand.well_family.well_family.repository.interceptor.HeaderInterceptor;
import com.demand.well_family.well_family.util.ErrorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ㅇㅇ on 2017-05-22.
 */

public class CommentEditInteractorImpl implements CommentEditInteractor {
    private CommentEditPresenter commentEditPresenter;

    @Override
    public void setCommentEdited(User user, final Comment commentAfterEdited, int intentFlag) {
        String accessToken = user.getAccess_token();
        String commentContent = commentAfterEdited.getContent();
        int commentId = commentAfterEdited.getId();

        commentServerConnection = new HeaderInterceptor(accessToken).getClientForCommentServer().create(CommentServerConnection.class);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("content", commentContent);
        map.put("flag", String.valueOf(intentFlag));
        Call<ResponseBody> call_update_comment = commentServerConnection.update_comment(commentId, map);
        call_update_comment.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                   commentEditPresenter.onSuccessSetCommentEdited(commentAfterEdited);
                } else {
                    commentEditPresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log(t);
                commentEditPresenter.onNetworkError(null);
            }
        });

    }

    private CommentServerConnection commentServerConnection;
    private static final Logger logger = LoggerFactory.getLogger(CommentEditInteractorImpl.class);

    public CommentEditInteractorImpl(CommentEditPresenter commentEditPresenter) {
        this.commentEditPresenter = commentEditPresenter;
    }

    private static void log(Throwable throwable) {
        StackTraceElement[] ste = throwable.getStackTrace();
        String className = ste[0].getClassName();
        String methodName = ste[0].getMethodName();
        int lineNumber = ste[0].getLineNumber();
        String fileName = ste[0].getFileName();

        if (LogFlag.printFlag) {
            if (logger.isInfoEnabled()) {
                logger.error("Exception: " + throwable.getMessage());
                logger.error(className + "." + methodName + " " + fileName + " " + lineNumber + " " + "line");
            }
        }
    }
}
