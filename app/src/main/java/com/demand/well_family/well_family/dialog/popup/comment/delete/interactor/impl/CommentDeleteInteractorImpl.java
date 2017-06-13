package com.demand.well_family.well_family.dialog.popup.comment.delete.interactor.impl;

import com.demand.well_family.well_family.dialog.popup.comment.delete.interactor.CommentDeleteInteractor;
import com.demand.well_family.well_family.dialog.popup.comment.delete.presenter.CommentDeletePresenter;
import com.demand.well_family.well_family.dto.Comment;
import com.demand.well_family.well_family.dto.User;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.repository.CommentServerConnection;
import com.demand.well_family.well_family.repository.interceptor.NetworkInterceptor;
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

public class CommentDeleteInteractorImpl implements CommentDeleteInteractor {
    private CommentDeletePresenter commentDeletePresenter;
    private CommentServerConnection commentServerConnection;
    private static final Logger logger = LoggerFactory.getLogger(CommentDeleteInteractorImpl.class);

    public CommentDeleteInteractorImpl(CommentDeletePresenter commentDeletePresenter) {
        this.commentDeletePresenter = commentDeletePresenter;
    }

    @Override
    public void setCommentDeleted(User user, Comment comment, int actFlag) {
        String accessToken = user.getAccess_token();
        int commentId = comment.getId();

        commentServerConnection = new NetworkInterceptor(accessToken).getClientForCommentServer().create(CommentServerConnection.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("flag",String.valueOf(actFlag));
        Call<ResponseBody> call_delete_comment = commentServerConnection.delete_comment(commentId, map);
        call_delete_comment.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                   commentDeletePresenter.onSuccessSetCommentDeleted();
                }else{
                    commentDeletePresenter.onNetworkError(new ErrorUtil(getClass()).parseError(response));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                log(t);
                commentDeletePresenter.onNetworkError(null);
            }
        });
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
