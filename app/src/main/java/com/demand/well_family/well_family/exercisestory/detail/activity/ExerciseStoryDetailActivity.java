package com.demand.well_family.well_family.exercisestory.detail.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dialog.list.comment.activity.CommentDialogActivity;
import com.demand.well_family.well_family.dialog.list.comment.flag.CommentActFlag;
import com.demand.well_family.well_family.dialog.list.comment.flag.CommentCodeFlag;
import com.demand.well_family.well_family.dialog.list.exercise.activity.ExerciseStoryDialogActivity;
import com.demand.well_family.well_family.dto.CommentInfo;
import com.demand.well_family.well_family.dto.ExerciseStory;
import com.demand.well_family.well_family.exercise.flag.ExerciseIntentFlag;
import com.demand.well_family.well_family.exercisestory.detail.adapter.ExerciseStoryDetailCommentAdapter;
import com.demand.well_family.well_family.exercisestory.detail.presenter.ExerciseStoryDetailPresenter;
import com.demand.well_family.well_family.exercisestory.detail.presenter.impl.ExerciseStoryDetailPresenterImpl;
import com.demand.well_family.well_family.exercisestory.detail.view.ExerciseStoryDetailView;
import com.demand.well_family.well_family.flag.CommentINTENTFlag;

import java.util.ArrayList;


/**
 * Created by ㅇㅇ on 2017-06-27.
 */

public class ExerciseStoryDetailActivity extends Activity implements ExerciseStoryDetailView, View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private ExerciseStoryDetailPresenter exerciseStoryDetailPresenter;

    private TextView tv_exercisestorydetail_title;
    private TextView tv_exercisestorydetail_date;
    private TextView tv_exercisestorydetail_score;
    private TextView tv_exercisestorydetail_result;
    private TextView tv_exercisestorydetail_likecount;
    private TextView tv_exercisestorydetail_commentcount;
    private ImageView iv_exercisestorydetail_menu;
    private ImageView iv_exercisestorydetail_share;
    private NestedScrollView nsv_exercisestorydetail;
    private LinearLayout ll_exercisestorydetail;
    private RatingBar rb_exercisestorydetail;
    private CheckBox cb_exercisestorydetail_like;
    private RecyclerView rv_exercisestorydetail_comment;
    private EditText et_exercisestorydetail_comment;
    private Button btn_exercisestorydetail_comment;

    private View decorView;
    private TextView toolbar_title;
    private Toolbar toolbar;
    private ImageView toolbar_back;
    private ExerciseStoryDetailCommentAdapter exerciseStoryDetailCommentAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercisestorydetail);

       ExerciseStory exerciseStory = (ExerciseStory)getIntent().getSerializableExtra("exerciseStory");

        exerciseStoryDetailPresenter = new ExerciseStoryDetailPresenterImpl(this);
        exerciseStoryDetailPresenter.onCreate(exerciseStory);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public View getDecorView() {
        if (decorView == null) {
            decorView = this.getWindow().getDecorView();
        }
        return decorView;
    }

    @Override
    public void showToolbarTitle(String message) {
        toolbar_title.setText(message);
    }

    @Override
    public void setToolbar(View decorView) {
        toolbar = (Toolbar) decorView.findViewById(R.id.toolBar);
        toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_back = (ImageView) toolbar.findViewById(R.id.toolbar_back);
        toolbar_back.setOnClickListener(this);
    }

    @Override
    public void init() {
        tv_exercisestorydetail_title = (TextView) findViewById(R.id.tv_exercisestorydetail_title);
        tv_exercisestorydetail_date = (TextView) findViewById(R.id.tv_exercisestorydetail_date);
        tv_exercisestorydetail_score = (TextView) findViewById(R.id.tv_exercisestorydetail_score);
        tv_exercisestorydetail_result = (TextView) findViewById(R.id.tv_exercisestorydetail_result);
        tv_exercisestorydetail_likecount = (TextView) findViewById(R.id.tv_exercisestorydetail_likecount);
        tv_exercisestorydetail_commentcount = (TextView) findViewById(R.id.tv_exercisestorydetail_commentcount);

        nsv_exercisestorydetail = (NestedScrollView) findViewById(R.id.nsv_exercisestorydetail);
        ll_exercisestorydetail = (LinearLayout) findViewById(R.id.ll_exercisestorydetail);
        iv_exercisestorydetail_menu = (ImageView) findViewById(R.id.iv_exercisestorydetail_menu);
        iv_exercisestorydetail_share = (ImageView) findViewById(R.id.iv_exercisestorydetail_share);
        rb_exercisestorydetail = (RatingBar) findViewById(R.id.rb_exercisestorydetail);
        cb_exercisestorydetail_like = (CheckBox) findViewById(R.id.cb_exercisestorydetail_like);
        rv_exercisestorydetail_comment = (RecyclerView) findViewById(R.id.rv_exercisestorydetail_comment);
        btn_exercisestorydetail_comment = (Button) findViewById(R.id.btn_exercisestorydetail_comment);
        et_exercisestorydetail_comment = (EditText) findViewById(R.id.et_exercisestorydetail_comment);

        btn_exercisestorydetail_comment.setOnClickListener(this);
        iv_exercisestorydetail_menu.setOnClickListener(this);
        cb_exercisestorydetail_like.setOnCheckedChangeListener(this);

        exerciseStoryDetailPresenter.onLoadData();
    }

    @Override
    public void showTitle(String message) {
        tv_exercisestorydetail_title.setText(message);
    }

    @Override
    public void showDate(String message) {
        tv_exercisestorydetail_date.setText(message);
    }

    @Override
    public void showScoreText(String message) {
        tv_exercisestorydetail_score.setText(message);
    }

    @Override
    public void showScore(float score) {
        rb_exercisestorydetail.setRating(score);
    }

    @Override
    public void showResult(String message) {
        tv_exercisestorydetail_result.setText(message);
    }

    @Override
    public void showCommentCount(String message) {
        tv_exercisestorydetail_commentcount.setText(message);
    }

    @Override
    public void showLikeCount(String message) {
        tv_exercisestorydetail_likecount.setText(message);
    }

    @Override
    public void navigateToExerciseStoryDialogActivity(ExerciseStory exerciseStory) {
        Intent intent = new Intent(this, ExerciseStoryDialogActivity.class);
        intent.putExtra("exerciseStory", exerciseStory);
        startActivityForResult(intent, ExerciseIntentFlag.POPUP_REQUEST);
    }

    @Override
    public void setExerciseStoryCommentAdapterItem(ArrayList<CommentInfo> commentInfoList) {
        exerciseStoryDetailCommentAdapter = new ExerciseStoryDetailCommentAdapter(this, commentInfoList, exerciseStoryDetailPresenter);
        rv_exercisestorydetail_comment.setAdapter(exerciseStoryDetailCommentAdapter);
        rv_exercisestorydetail_comment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void showCommentEdit(String message) {
        et_exercisestorydetail_comment.setText(message);
    }

    @Override
    public void showCommentScrollDown() {
        nsv_exercisestorydetail.fullScroll(NestedScrollView.FOCUS_DOWN);
    }

    @Override
    public void showCommentAdapterNotifyItemInserted() {
        exerciseStoryDetailCommentAdapter.notifyItemInserted(exerciseStoryDetailCommentAdapter.getItemCount() - 1);
    }


    @Override
    public void setCommentAdapterAdded(CommentInfo commentInfo) {
        exerciseStoryDetailCommentAdapter.setCommentAdded(commentInfo);
    }

    @Override
    public void showCommentAdapterNotifyItemDelete(int position) {
        exerciseStoryDetailCommentAdapter.notifyItemRemoved(position);
        exerciseStoryDetailCommentAdapter.notifyItemRangeChanged(position, exerciseStoryDetailCommentAdapter.getItemCount());
    }

    @Override
    public void setCommentAdapterDelete(int position) {
        exerciseStoryDetailCommentAdapter.setCommentDelete(position);
    }

    @Override
    public void setCommentAdapterSetContent(int position, String content) {
        exerciseStoryDetailCommentAdapter.setCommentSetContent(position, content);
    }

    @Override
    public void showCommentAdapterNotifyItemChanged(int position) {
        exerciseStoryDetailCommentAdapter.notifyItemChanged(position);
    }

    @Override
    public void navigateToCommentDialogActivityForMember(CommentInfo commentInfo) {
        Intent intent = new Intent(this, CommentDialogActivity.class);
        intent.putExtra("comment_user_name", commentInfo.getUser_name());
        intent.putExtra("comment_category_id", CommentINTENTFlag.FALL_DIAGNOSIS_STORY);
        intent.putExtra("comment_content", commentInfo.getContent());
        intent.putExtra("comment_id", commentInfo.getComment_id());
        intent.putExtra("position", commentInfo.getPosition());
        intent.putExtra("act_flag", CommentActFlag.PUBLIC);
        startActivityForResult(intent, ExerciseIntentFlag.COMMENT_POPUP_REQUEST);
    }

    @Override
    public void navigateToCommentDialogActivityForOwner(CommentInfo commentInfo) {
        Intent intent = new Intent(this, CommentDialogActivity.class);
        intent.putExtra("comment_user_name", commentInfo.getUser_name());
        intent.putExtra("comment_category_id", CommentINTENTFlag.FALL_DIAGNOSIS_STORY);
        intent.putExtra("comment_content", commentInfo.getContent());
        intent.putExtra("comment_id", commentInfo.getComment_id());
        intent.putExtra("position", commentInfo.getPosition());
        intent.putExtra("act_flag", CommentActFlag.EXERCISE_STORY);
        startActivityForResult(intent, ExerciseIntentFlag.COMMENT_POPUP_REQUEST);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back:
                exerciseStoryDetailPresenter.onBackPressed();
                break;

            case R.id.btn_exercisestorydetail_comment:
                String comment = et_exercisestorydetail_comment.getText().toString();
                CommentInfo commentInfo = new CommentInfo();
                commentInfo.setContent(comment);

                exerciseStoryDetailPresenter.setExerciseStoryComment(commentInfo);
                break;

            case R.id.iv_exercisestorydetail_menu:
                exerciseStoryDetailPresenter.onClickMenuButton();
                break;
        }
    }

    @Override
    public void goneMenuButton() {
        iv_exercisestorydetail_menu.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ExerciseIntentFlag.POPUP_REQUEST:
                switch (resultCode) {
                    case ExerciseIntentFlag.RESULT_OK:
                        ExerciseStory exerciseStory = (ExerciseStory) data.getSerializableExtra("exerciseStory");
                        Intent intent = getIntent();
                        intent.putExtra("exerciseStory", exerciseStory);
                        setResult(ExerciseIntentFlag.RESULT_DELETE, intent);
                        finish();
                        break;
                }
                break;

            case ExerciseIntentFlag.COMMENT_POPUP_REQUEST:
                switch (resultCode) {
                    case ExerciseIntentFlag.RESULT_OK:

                        int position = data.getIntExtra("position", 0);
                        int commentCodeFlag = data.getIntExtra("flag", 0);
                        String content = data.getStringExtra("content");

                        switch (commentCodeFlag) {
                            case CommentCodeFlag.EDIT_REQUEST:
                                exerciseStoryDetailPresenter.onActivityResultForCommentEditResultOkModifyOrReport(position, content);
                                break;

                            case CommentCodeFlag.DELETE_REQUEST:
                                exerciseStoryDetailPresenter.onActivityResultForCommentEditResultOkDELETE(position);
                                break;
                        }
                        break;
                }
        }
    }

    @Override
    public void setExerciseStoryLikeChecked() {
        cb_exercisestorydetail_like.setChecked(true);
        int count = Integer.valueOf(tv_exercisestorydetail_likecount.getText().toString()) + 1;
        tv_exercisestorydetail_likecount.setText(String.valueOf(count));
    }

    @Override
    public void setExerciseStoryLikeUnChecked() {
        cb_exercisestorydetail_like.setChecked(false);
        int count = Integer.valueOf(tv_exercisestorydetail_likecount.getText().toString()) - 1;
        tv_exercisestorydetail_likecount.setText(String.valueOf(count));
    }

    @Override
    public void setExerciseStoryLikeIsChecked() {
        cb_exercisestorydetail_like.setChecked(true);
    }

    @Override
    public void setExerciseStoryLikeIsUnChecked() {
        cb_exercisestorydetail_like.setChecked(false);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        exerciseStoryDetailPresenter.onCheckedChangeForLike(isChecked);
    }

    @Override
    public void navigateToBack(ExerciseStory exerciseStory) {
        Intent intent = getIntent();
        intent.putExtra("exerciseStory", exerciseStory);
        setResult(ExerciseIntentFlag.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        exerciseStoryDetailPresenter.onBackPressed();
    }
}
