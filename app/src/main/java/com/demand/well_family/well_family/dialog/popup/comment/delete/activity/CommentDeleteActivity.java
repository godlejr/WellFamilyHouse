package com.demand.well_family.well_family.dialog.popup.comment.delete.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dialog.popup.comment.delete.presenter.CommentDeletePresenter;
import com.demand.well_family.well_family.dialog.popup.comment.delete.presenter.impl.CommentDeletePresenterImpl;
import com.demand.well_family.well_family.dialog.popup.comment.delete.view.CommentDeleteView;
import com.demand.well_family.well_family.dto.Comment;

/**
 * Created by Dev-0 on 2017-03-02.
 */

public class CommentDeleteActivity extends Activity implements CommentDeleteView, View.OnClickListener {
    private CommentDeletePresenter commentDeletePresenter;

    private TextView tv_comment_delete_confirm;
    private TextView tv_comment_delete_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_comment_delete);
        getWindow().setLayout(android.view.WindowManager.LayoutParams.WRAP_CONTENT, android.view.WindowManager.LayoutParams.WRAP_CONTENT);


        commentDeletePresenter = new CommentDeletePresenterImpl(this);
        commentDeletePresenter.onCreate();
    }

    @Override
    public void navigateToBackForResultOk() {
        Intent intent = getIntent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
        super.onBackPressed();
    }

    @Override
    public void navigateToBackForResultCanceled() {
        Intent intent = getIntent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(CommentDeleteActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void init() {
        tv_comment_delete_confirm = (TextView) findViewById(R.id.tv_comment_delete_confirm);
        tv_comment_delete_cancel = (TextView) findViewById(R.id.tv_comment_delete_cancel);

        tv_comment_delete_cancel.setOnClickListener(this);
        tv_comment_delete_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_comment_delete_cancel:
                navigateToBackForResultCanceled();
                break;

            case R.id.tv_comment_delete_confirm:
                int intentFlag = getIntent().getIntExtra("act_flag", 0);
                Comment comment = new Comment();
                comment.setId(getIntent().getIntExtra("comment_id", 0));

                commentDeletePresenter.onClickDelete(comment, intentFlag);
                break;
        }
    }
}
