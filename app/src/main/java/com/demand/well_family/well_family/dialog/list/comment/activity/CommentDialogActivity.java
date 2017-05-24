package com.demand.well_family.well_family.dialog.list.comment.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.widget.Toast;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.main.report.activity.ReportActivity;
import com.demand.well_family.well_family.dialog.popup.comment.delete.activity.CommentDeleteActivity;
import com.demand.well_family.well_family.dialog.popup.comment.edit.activity.CommentEditActivity;
import com.demand.well_family.well_family.dialog.list.comment.adapter.CommentDialogAdapter;
import com.demand.well_family.well_family.dialog.list.comment.flag.CommentCodeFlag;
import com.demand.well_family.well_family.dialog.list.comment.presenter.CommentDialogPresenter;
import com.demand.well_family.well_family.dialog.list.comment.presenter.impl.CommentDialogPresenterImpl;
import com.demand.well_family.well_family.dialog.list.comment.view.CommentDialogView;
import com.demand.well_family.well_family.dto.Comment;
import com.demand.well_family.well_family.dto.Report;
import com.demand.well_family.well_family.flag.CommentINTENTFlag;
import com.demand.well_family.well_family.flag.ReportINTETNFlag;

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

        Report report = new Report();
        report.setAuthor_name(getIntent().getStringExtra("comment_user_name"));
        report.setWriting_position(getIntent().getIntExtra("position", 0));
        report.setWriting_category_id(getIntent().getIntExtra("comment_category_id", CommentINTENTFlag.STORY));
        report.setWriting_content(getIntent().getStringExtra("comment_content"));
        report.setWriting_id(getIntent().getIntExtra("comment_id", 0));

        Comment comment = new Comment();
        comment.setPosition(getIntent().getIntExtra("position", 0));
        comment.setContent(getIntent().getStringExtra("comment_content"));
        comment.setId(getIntent().getIntExtra("comment_id", 0));
        int authorIsMe = getIntent().getIntExtra("act_flag", 0);

        commentDialogPresenter = new CommentDialogPresenterImpl(this);
        commentDialogPresenter.onCreate(report, comment, authorIsMe);
    }

    @Override
    public void init() {
        rv_popup_comment = (RecyclerView) findViewById(R.id.rv_popup_comment);
        commentDialogPresenter.setCommentDialogAdapterInit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CommentCodeFlag.EDIT_REQUEST:
                switch (resultCode) {
                    case CommentCodeFlag.RESULT_OK:
                        String editedCommentContent = data.getStringExtra("content");
                        commentDialogPresenter.onActivityResultForEditResultOk(CommentCodeFlag.EDIT_REQUEST, editedCommentContent);
                        break;
                }
                break;

            case CommentCodeFlag.DELETE_REQUEST:
                switch (resultCode) {
                    case CommentCodeFlag.RESULT_OK:
                        commentDialogPresenter.onActivityResultForDeleteResultOk(CommentCodeFlag.DELETE_REQUEST);
                        break;

                    case CommentCodeFlag.RESULT_CANCELED:
                        finish();
                        break;
                }
                break;
        }

    }

    @Override
    public void setCommentDialogAdapterInit(ArrayList<String> commentDialogList) {
        commentDialogAdapter = new CommentDialogAdapter(CommentDialogActivity.this, commentDialogList, commentDialogPresenter);
        commentDialogPresenter.setCommentDialogAdapter(commentDialogAdapter);
    }

    @Override
    public void setCommentDialogAdapter(CommentDialogAdapter commentDialogAdapter) {
        rv_popup_comment.setAdapter(commentDialogAdapter);
        rv_popup_comment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void setCommentModifiedOrReported() {
        commentDialogPresenter.onClickCommentModifyOrReport();
    }

    @Override
    public void navigateToCommentEditActivity(Comment comment, int actFlag) {
        Intent modify_intent = new Intent(CommentDialogActivity.this, CommentEditActivity.class);
        modify_intent.putExtra("comment_id", comment.getId());
        modify_intent.putExtra("comment_content", comment.getContent());
        modify_intent.putExtra("act_flag", actFlag);
        startActivityForResult(modify_intent, CommentCodeFlag.EDIT_REQUEST);
    }

    @Override
    public void navigateToCommentDeleteActivity(Comment comment, int actFlag) {
        Intent modify_intent = new Intent(CommentDialogActivity.this, CommentDeleteActivity.class);
        modify_intent.putExtra("comment_id", comment.getId());
        modify_intent.putExtra("act_flag", actFlag);
        startActivityForResult(modify_intent, CommentCodeFlag.DELETE_REQUEST);
    }

    @Override
    public void navigateToReportActivity(Report report) {
        Intent report_intent = new Intent(CommentDialogActivity.this, ReportActivity.class);
        report_intent.putExtra("intent_flag", ReportINTETNFlag.COMMENT);
        report_intent.putExtra("author_name", report.getAuthor_name());
        report_intent.putExtra("writing_id", report.getWriting_id());
        report_intent.putExtra("writing_category_id", report.getWriting_category_id());
        report_intent.putExtra("writing_content", report.getWriting_content());
        startActivity(report_intent);
        finish();
    }

    @Override
    public void setCommentCopied(Comment comment) {
        ClipboardManager clipboardManager = (ClipboardManager) this.getSystemService(this.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("label", comment.getContent());
        clipboardManager.setPrimaryClip(clipData);

        commentDialogPresenter.onClickCommentCopy();
    }

    @Override
    public void navigateToBackAfterEdit(Comment comment, int commentCodeFlag) {
        Intent intent = getIntent();
        intent.putExtra("flag", commentCodeFlag);
        intent.putExtra("content", comment.getContent());
        intent.putExtra("position", comment.getPosition());
        setResult(CommentCodeFlag.RESULT_OK, intent);

        finish();
    }

    @Override
    public void navigateToBackAfterDelete(Comment comment, int commentCodeFlag) {
        Intent intent = getIntent();
        intent.putExtra("flag", commentCodeFlag);
        intent.putExtra("position", comment.getPosition());
        setResult(Activity.RESULT_OK, intent);

        finish();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(CommentDialogActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToBack() {
        finish();
    }
}
