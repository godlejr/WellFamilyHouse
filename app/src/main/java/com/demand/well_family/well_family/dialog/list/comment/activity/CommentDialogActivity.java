package com.demand.well_family.well_family.dialog.list.comment.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.widget.Toast;

import com.demand.well_family.well_family.ReportActivity;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dialog.CommentDeleteActivity;
import com.demand.well_family.well_family.dialog.CommentModifyActivity;
import com.demand.well_family.well_family.dialog.list.comment.adapter.CommentDialogAdapter;
import com.demand.well_family.well_family.dialog.list.comment.presenter.CommentDialogPresenter;
import com.demand.well_family.well_family.dialog.list.comment.presenter.impl.CommentDialogPresenterImpl;
import com.demand.well_family.well_family.dialog.list.comment.view.CommentDialogView;
import com.demand.well_family.well_family.flag.CommentCodeFlag;
import com.demand.well_family.well_family.flag.CommentINTENTFlag;

import java.util.ArrayList;

/**
 * Created by Dev-0 on 2017-03-02.
 */

public class CommentDialogActivity extends Activity implements CommentDialogView {
    private CommentDialogPresenter commentDialogPresenter;


    private CommentDialogAdapter commentDialogAdapter;
    private RecyclerView rv_popup_comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_comment);
        getWindow().setLayout(android.view.WindowManager.LayoutParams.WRAP_CONTENT, android.view.WindowManager.LayoutParams.WRAP_CONTENT);

        commentDialogPresenter = new CommentDialogPresenterImpl(this);
        commentDialogPresenter.onCreate();
    }

    @Override
    public void init() {
        rv_popup_comment = (RecyclerView) findViewById(R.id.rv_popup_comment);
        rv_popup_comment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        int actFlag = getIntent().getIntExtra("act_flag", 0);
        ArrayList<String> commentDialogList = commentDialogPresenter.getCommentDialogList(actFlag);
        commentDialogAdapter = new CommentDialogAdapter(CommentDialogActivity.this, commentDialogList, commentDialogPresenter);
        commentDialogPresenter.setCommentDialogAdapter(commentDialogAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CommentCodeFlag.EDIT_REQUEST) {
            if (resultCode == RESULT_OK) {
                int commentPosition = getIntent().getIntExtra("position", 0);
                String commentContent = getIntent().getStringExtra("comment_content");
                commentDialogPresenter.onActivityResultForEditResultOk(commentPosition, commentContent, CommentCodeFlag.EDIT_REQUEST);
            }
        }

        if (requestCode == CommentCodeFlag.DELETE_REQUEST) {
            if (resultCode == RESULT_OK) {
                int commentPosition = getIntent().getIntExtra("position", 0);
                commentDialogPresenter.onActivityResultForDeleteResultOk(commentPosition, CommentCodeFlag.DELETE_REQUEST);
            }
            if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }

    @Override
    public void setCommentDialogAdapter(CommentDialogAdapter commentDialogAdapter) {
        rv_popup_comment.setAdapter(commentDialogAdapter);
    }

    @Override
    public String getCommentContent() {
        String commentContent = getIntent().getStringExtra("comment_content");
        return commentContent;
    }

    @Override
    public int getActFlag() {
        int actFlag = getIntent().getIntExtra("act_flag", 0);
        return actFlag;
    }

    @Override
    public void navigateToCommentModifyActivity() {
        int commentId = getIntent().getIntExtra("comment_id", 0);
        String commentContent = getIntent().getStringExtra("comment_content");
        int actFlag = getIntent().getIntExtra("act_flag", 0);

        Intent modify_intent = new Intent(CommentDialogActivity.this, CommentModifyActivity.class);
        modify_intent.putExtra("comment_id", commentId);
        modify_intent.putExtra("comment_content", commentContent);
        modify_intent.putExtra("act_flag", actFlag);
        startActivityForResult(modify_intent, CommentCodeFlag.EDIT_REQUEST);
    }

    @Override
    public void navigateToCommentDeleteActivity() {
        int commentId = getIntent().getIntExtra("comment_id", 0);
        int actFlag = getIntent().getIntExtra("act_flag", 0);

        Intent modify_intent = new Intent(CommentDialogActivity.this, CommentDeleteActivity.class);
        modify_intent.putExtra("comment_id", commentId);
        modify_intent.putExtra("act_flag", actFlag);
        startActivityForResult(modify_intent, CommentCodeFlag.DELETE_REQUEST);
    }

    @Override
    public void navigateToReportActivity() {
        int commentId = getIntent().getIntExtra("comment_id", 0);
        String commentContent = getIntent().getStringExtra("comment_content");
        String commentAuthor = getIntent().getStringExtra("comment_user_name");
        int commentCategoryId = getIntent().getIntExtra("comment_category_id", CommentINTENTFlag.STORY);

        Intent report_intent = new Intent(CommentDialogActivity.this, ReportActivity.class);
        report_intent.putExtra("comment_user_name", commentAuthor);
        report_intent.putExtra("comment_category_id", commentCategoryId);
        report_intent.putExtra("comment_id", commentId);
        report_intent.putExtra("comment_content", commentContent);
        startActivity(report_intent);
        finish();
    }

    @Override
    public void setCommentCopy() {
        commentDialogPresenter.onClickCommentCopy();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(CommentDialogActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToBack() {
        finish();
    }

    @Override
    public void navigateToBackAfterEdit(int commentPosition, String commentContent, int commentCodeFlag) {
        Intent intent = getIntent();
        intent.putExtra("flag", commentCodeFlag); //1: modify , 2:photo_delete
        intent.putExtra("content", commentContent);
        intent.putExtra("position", commentPosition);
        setResult(Activity.RESULT_OK, intent);

        finish();
    }

    @Override
    public void navigateToBackAfterDelete(int commentPosition, int commentCodeFlag) {
        Intent intent = getIntent();
        intent.putExtra("flag", commentCodeFlag); //1: modify , 2:photo_delete
        intent.putExtra("position", commentPosition);
        setResult(Activity.RESULT_OK, intent);

        finish();
    }
}
