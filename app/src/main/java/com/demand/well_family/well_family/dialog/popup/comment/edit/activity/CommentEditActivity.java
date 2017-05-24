package com.demand.well_family.well_family.dialog.popup.comment.edit.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dialog.popup.comment.edit.presenter.CommentEditPresenter;
import com.demand.well_family.well_family.dialog.popup.comment.edit.presenter.impl.CommentEditPresenterImpl;
import com.demand.well_family.well_family.dialog.popup.comment.edit.view.CommentEditView;
import com.demand.well_family.well_family.dto.Comment;

/**
 * Created by Dev-0 on 2017-03-02.
 */

public class CommentEditActivity extends Activity implements CommentEditView, View.OnClickListener {
    private CommentEditPresenter commentEditPresenter;

    private View decorView;
    private TextView toolbarTitle;
    private EditText et_comment_modify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_modify);

        commentEditPresenter = new CommentEditPresenterImpl(this);
        commentEditPresenter.onCreate();
    }

    @Override
    public void navigateToBack() {
        finish();
    }

    @Override
    public void setToolbar(View decorView) {
        Toolbar toolbar = (Toolbar) decorView.findViewById(R.id.toolBar);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

        ImageView toolbarBack = (ImageView) toolbar.findViewById(R.id.toolbar_back);
        toolbarBack.setOnClickListener(this);

        Button toolbarComplete = (Button) toolbar.findViewById(R.id.toolbar_complete);
        toolbarComplete.setOnClickListener(this);
    }

    @Override
    public View getDecorView() {
        if (decorView == null) {
            decorView = this.getWindow().getDecorView();
        }
        return decorView;
    }

    @Override
    public void showToolbarTitle(String title) {
        toolbarTitle.setText(title);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(CommentEditActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToBackWithComment(Comment commentAfterEdited) {
        String commentContentAfterEdited = commentAfterEdited.getContent();

        Intent intent = getIntent();
        intent.putExtra("content", commentContentAfterEdited);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void init() {
        String commentContentBeforeEdited = getIntent().getStringExtra("comment_content");
        et_comment_modify = (EditText) findViewById(R.id.et_comment_modify);
        et_comment_modify.setText(commentContentBeforeEdited);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_complete:
                int intentFlag = getIntent().getIntExtra("act_flag", 0);
                int commentId = getIntent().getIntExtra("comment_id", 0);
                String commentContentBeforeEdited = getIntent().getStringExtra("comment_content");
                String commentContentAfterEdited = et_comment_modify.getText().toString();

                Comment commentBeforeEdited = new Comment();
                commentBeforeEdited.setContent(commentContentBeforeEdited);

                Comment commentAfterEdited = new Comment();
                commentAfterEdited.setContent(commentContentAfterEdited);
                commentAfterEdited.setId(commentId);

                commentEditPresenter.onClickEdit(commentBeforeEdited, commentAfterEdited, intentFlag);
                break;

            case R.id.toolbar_back:
                finish();
                break;
        }
    }
}
