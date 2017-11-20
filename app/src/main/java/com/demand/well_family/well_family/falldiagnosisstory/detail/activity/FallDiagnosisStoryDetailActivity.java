package com.demand.well_family.well_family.falldiagnosisstory.detail.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dialog.list.comment.activity.CommentDialogActivity;
import com.demand.well_family.well_family.dialog.list.comment.flag.CommentActFlag;
import com.demand.well_family.well_family.dialog.list.comment.flag.CommentCodeFlag;
import com.demand.well_family.well_family.dialog.list.falldiagnosisstory.activity.FallDiagnosisStoryDialogActivity;
import com.demand.well_family.well_family.dialog.popup.photo.activity.PhotoPopupActivity;
import com.demand.well_family.well_family.dto.CommentInfo;
import com.demand.well_family.well_family.dto.EnvironmentEvaluationCategory;
import com.demand.well_family.well_family.dto.EnvironmentPhoto;
import com.demand.well_family.well_family.dto.FallDiagnosisContentCategory;
import com.demand.well_family.well_family.dto.FallDiagnosisStory;
import com.demand.well_family.well_family.dto.FallDiagnosisStoryInfo;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.dto.PhysicalEvaluationScore;
import com.demand.well_family.well_family.falldiagnosisstory.detail.adapter.comment.FallDiagnosisStoryDetailCommentAdapter;
import com.demand.well_family.well_family.falldiagnosisstory.detail.adapter.environment.content.FallDiagnosisStoryDetailEnvEvaluationCategoryAdapter;
import com.demand.well_family.well_family.falldiagnosisstory.detail.adapter.environment.photo.FallDiagnosisStoryDetailEnvEvaluationPhotoAdapter;
import com.demand.well_family.well_family.falldiagnosisstory.detail.adapter.selfdiagnosis.FallDiagnosisStoryDetailSelfDiagnosisAdapter;
import com.demand.well_family.well_family.falldiagnosisstory.detail.presenter.FallDiagnosisStoryDetailPresenter;
import com.demand.well_family.well_family.falldiagnosisstory.detail.presenter.impl.FallDiagnosisStoryDetailPresenterImpl;
import com.demand.well_family.well_family.falldiagnosisstory.detail.view.FallDiagnosisStoryDetailView;
import com.demand.well_family.well_family.falldiagnosisstory.flag.FallDiagnosisStoryCodeFlag;
import com.demand.well_family.well_family.flag.CommentINTENTFlag;
import com.demand.well_family.well_family.flag.PhotoPopupINTENTFlag;
import com.demand.well_family.well_family.util.CalculateDateUtil;

import java.util.ArrayList;


/**
 * Created by ㅇㅇ on 2017-06-07.
 */

public class FallDiagnosisStoryDetailActivity extends Activity implements FallDiagnosisStoryDetailView, View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private FallDiagnosisStoryDetailPresenter fallDiagnosisStoryDetailPresenter;

    private TextView toolbarTitle;
    private ImageView toolbarBack;
    private View decorView;
    private Toolbar toolbar;

    private TextView tv_falldiagnosisstorydetail_title;
    private TextView tv_falldiagnosisstorydetail_date;
    private TextView tv_falldiagnosisstorydetail_score;
    private TextView tv_falldiagnosisstorydetail_result;
    private TextView tv_falldiagnosisstorydetail_likecount;
    private TextView tv_falldiagnosisstorydetail_commentcount;
    private ImageView iv_falldiagnosisstorydetail_img;
    private ImageView iv_falldiagnosisstorydetail_sharebtn;
    private CheckBox cb_falldiagnosisstorydetail_likebtn;
    private RecyclerView rv_falldiagnosisstorydetail_comment;
    private EditText et_falldiagnosisstorydetail_comment;
    private Button btn_falldiagnosisstorydetail_comment;
    private LinearLayout ll_falldiagnosisstorydetail_container;
    private ImageView iv_falldiagnosisstory_menu;
    private NestedScrollView nsv_falldiagnosisstorydetail;
    private LayoutInflater inflater;

    private FallDiagnosisStoryDetailEnvEvaluationPhotoAdapter fallDiagnosisStoryDetailEnvEvaluationPhotoAdapter;
    private FallDiagnosisStoryDetailSelfDiagnosisAdapter fallDiagnosisStoryDetailSelfDiagnosisAdapter;
    private FallDiagnosisStoryDetailEnvEvaluationCategoryAdapter fallDiagnosisStoryDetailEnvEvaluationCategoryAdapter;
    private FallDiagnosisStoryDetailCommentAdapter fallDiagnosisStoryDetailCommentAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_falldiagnosisstorydetail);

        FallDiagnosisStoryInfo fallDiagnosisStoryInfo = (FallDiagnosisStoryInfo) getIntent().getSerializableExtra("fallDiagnosisStoryInfo");
        FallDiagnosisStory fallDiagnosisStory = (FallDiagnosisStory) getIntent().getSerializableExtra("fallDiagnosisStory");

        fallDiagnosisStoryDetailPresenter = new FallDiagnosisStoryDetailPresenterImpl(this);
        fallDiagnosisStoryDetailPresenter.onCreate(fallDiagnosisStory, fallDiagnosisStoryInfo);
    }

    @Override
    public void onBackPressed() {
        fallDiagnosisStoryDetailPresenter.onBackPressed();
    }

    @Override
    public void init(FallDiagnosisStory fallDiagnosisStory, FallDiagnosisStoryInfo fallDiagnosisStoryInfo) {
        tv_falldiagnosisstorydetail_title = (TextView) findViewById(R.id.tv_falldiagnosisstorydetail_title);
        tv_falldiagnosisstorydetail_date = (TextView) findViewById(R.id.tv_falldiagnosisstorydetail_date);
        tv_falldiagnosisstorydetail_score = (TextView) findViewById(R.id.tv_falldiagnosisstorydetail_score);
        tv_falldiagnosisstorydetail_result = (TextView) findViewById(R.id.tv_falldiagnosisstorydetail_result);
        tv_falldiagnosisstorydetail_likecount = (TextView) findViewById(R.id.tv_falldiagnosisstorydetail_likecount);
        tv_falldiagnosisstorydetail_commentcount = (TextView) findViewById(R.id.tv_falldiagnosisstorydetail_commentcount);
        iv_falldiagnosisstorydetail_img = (ImageView) findViewById(R.id.iv_falldiagnosisstorydetail_img);
        iv_falldiagnosisstorydetail_sharebtn = (ImageView) findViewById(R.id.iv_falldiagnosisstorydetail_sharebtn);
        cb_falldiagnosisstorydetail_likebtn = (CheckBox) findViewById(R.id.cb_falldiagnosisstorydetail_likebtn);
        ll_falldiagnosisstorydetail_container = (LinearLayout) findViewById(R.id.ll_falldiagnosisstorydetail_container);
        iv_falldiagnosisstory_menu = (ImageView) findViewById(R.id.iv_falldiagnosisstory_menu);
        nsv_falldiagnosisstorydetail = (NestedScrollView) findViewById(R.id.nsv_falldiagnosisstorydetail);

        rv_falldiagnosisstorydetail_comment = (RecyclerView) findViewById(R.id.rv_falldiagnosisstorydetail_comment);
        et_falldiagnosisstorydetail_comment = (EditText) findViewById(R.id.et_falldiagnosisstorydetail_comment);
        btn_falldiagnosisstorydetail_comment = (Button) findViewById(R.id.btn_falldiagnosisstorydetail_comment);
        inflater = getLayoutInflater();

        tv_falldiagnosisstorydetail_title.setText(fallDiagnosisStoryInfo.getTitle());
        tv_falldiagnosisstorydetail_date.setText(CalculateDateUtil.calculateDate(fallDiagnosisStory.getCreated_at()));

        tv_falldiagnosisstorydetail_result.setText(fallDiagnosisStoryInfo.getRisk_comment());
        Glide.with(this).load(getString(R.string.cloud_front_self_diagnosis) + fallDiagnosisStoryInfo.getAvatar()).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_falldiagnosisstorydetail_img);

        iv_falldiagnosisstorydetail_sharebtn.setOnClickListener(this);
        btn_falldiagnosisstorydetail_comment.setOnClickListener(this);
        iv_falldiagnosisstory_menu.setOnClickListener(this);
        cb_falldiagnosisstorydetail_likebtn.setOnCheckedChangeListener(this);

        fallDiagnosisStoryDetailPresenter.onLoadData();
    }

    @Override
    public View getDecorView() {
        if (decorView == null) {
            decorView = this.getWindow().getDecorView();
        }
        return decorView;
    }

    @Override
    public void setToolbar(View decorView) {
        toolbar = (Toolbar) decorView.findViewById(R.id.toolBar);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbarBack = (ImageView) toolbar.findViewById(R.id.toolbar_back);
        toolbarBack.setOnClickListener(this);
    }

    @Override
    public void showToolbarTitle(String message) {
        toolbarTitle.setText(message);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(FallDiagnosisStoryDetailActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showScore(String score) {
        tv_falldiagnosisstorydetail_score.setText(score);
    }

    @Override
    public void showScoreTextChangeColorWithSafe() {
        tv_falldiagnosisstorydetail_score.setTextColor(getResources().getColor(R.color.green));
    }

    @Override
    public void showScoreTextChangeColorWithCaution() {
        tv_falldiagnosisstorydetail_score.setTextColor(getResources().getColor(R.color.yellow));
    }

    @Override
    public void showScoreTextChangeColorWithRisk() {
        tv_falldiagnosisstorydetail_score.setTextColor(getResources().getColor(R.color.red));
    }

    @Override
    public void showCommentCount(String count) {
        tv_falldiagnosisstorydetail_commentcount.setText(count);
    }

    @Override
    public void showLikeCount(String count) {
        tv_falldiagnosisstorydetail_likecount.setText(count);
    }

    @Override
    public void setFallDiagnosisStoryLikeChecked() {
        cb_falldiagnosisstorydetail_likebtn.setChecked(true);
        int count = Integer.valueOf(tv_falldiagnosisstorydetail_likecount.getText().toString()) + 1;
        tv_falldiagnosisstorydetail_likecount.setText(String.valueOf(count));
    }

    @Override
    public void setFallDiagnosisStoryLikeUnChecked() {
        cb_falldiagnosisstorydetail_likebtn.setChecked(false);
        int count = Integer.valueOf(tv_falldiagnosisstorydetail_likecount.getText().toString()) - 1;
        tv_falldiagnosisstorydetail_likecount.setText(String.valueOf(count));
    }

    @Override
    public void setFallDiagnosisStoryLikeIsChecked() {
        cb_falldiagnosisstorydetail_likebtn.setChecked(true);
    }

    @Override
    public void setFallDiagnosisStoryLikeIsUnChecked() {
        cb_falldiagnosisstorydetail_likebtn.setChecked(false);
    }

    @Override
    public void navigateToBack(FallDiagnosisStory fallDiagnosisStory) {
        Intent intent = getIntent();
        intent.putExtra("fallDiagnosisStory", fallDiagnosisStory);
        setResult(FallDiagnosisStoryCodeFlag.RESULT_OK, intent);
        finish();
    }

    @Override
    public void setContentForSelfDiagnosis(ArrayList<FallDiagnosisContentCategory> categoryList) {
        ll_falldiagnosisstorydetail_container.removeAllViews();
        inflater.inflate(R.layout.item_falldiagnosisstorydetail_selfdiagnois_list, ll_falldiagnosisstorydetail_container, true);
        RecyclerView rv_item_falldiagnosisstory_selfdiagnosis = (RecyclerView) ll_falldiagnosisstorydetail_container.findViewById(R.id.rv_item_falldiagnosisstory_selfdiagnosis);

        fallDiagnosisStoryDetailSelfDiagnosisAdapter = new FallDiagnosisStoryDetailSelfDiagnosisAdapter(this, categoryList, fallDiagnosisStoryDetailPresenter);
        rv_item_falldiagnosisstory_selfdiagnosis.setAdapter(fallDiagnosisStoryDetailSelfDiagnosisAdapter);
        rv_item_falldiagnosisstory_selfdiagnosis.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void setContentForPhysicalEvaluation(PhysicalEvaluationScore physicalEvaluationScore) {
        ll_falldiagnosisstorydetail_container.removeAllViews();
        inflater.inflate(R.layout.item_falldiagnosisstorydetail_physicalevaluation, ll_falldiagnosisstorydetail_container, true);
        TextView tv_item_falldiagnosisstorydetail_balance = (TextView) ll_falldiagnosisstorydetail_container.findViewById(R.id.tv_item_falldiagnosisstorydetail_balance);
        TextView tv_item_falldiagnosisstorydetail_movement = (TextView) ll_falldiagnosisstorydetail_container.findViewById(R.id.tv_item_falldiagnosisstorydetail_movement);
        TextView tv_item_falldiagnosisstorydetail_leg = (TextView) ll_falldiagnosisstorydetail_container.findViewById(R.id.tv_item_falldiagnosisstorydetail_leg);

        String balanceScore = physicalEvaluationScore.getBalance_score() + "점";
        String movementScore = physicalEvaluationScore.getMovement_score() + "점";
        String legStrengthScore = physicalEvaluationScore.getLeg_strength_score() + "점";

        tv_item_falldiagnosisstorydetail_balance.setText(balanceScore);
        tv_item_falldiagnosisstorydetail_movement.setText(movementScore);
        tv_item_falldiagnosisstorydetail_leg.setText(legStrengthScore);
    }

    @Override
    public void setContentForRiskEvaluationPhoto(ArrayList<EnvironmentPhoto> environmentPhotoList) {
        ll_falldiagnosisstorydetail_container.removeAllViews();
        inflater.inflate(R.layout.item_falldiagnosisstorydetail_envenvironment_list, ll_falldiagnosisstorydetail_container, true);

        RecyclerView rv_item_falldiagnosisstorydetail_envevaluation_photo = (RecyclerView) ll_falldiagnosisstorydetail_container.findViewById(R.id.rv_item_falldiagnosisstorydetail_envevaluation_photo);
        fallDiagnosisStoryDetailEnvEvaluationPhotoAdapter = new FallDiagnosisStoryDetailEnvEvaluationPhotoAdapter(this, environmentPhotoList, fallDiagnosisStoryDetailPresenter);

        rv_item_falldiagnosisstorydetail_envevaluation_photo.setAdapter(fallDiagnosisStoryDetailEnvEvaluationPhotoAdapter);
        rv_item_falldiagnosisstorydetail_envevaluation_photo.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public void setContentForRiskEvaluationCategory(ArrayList<EnvironmentEvaluationCategory> environmentEvaluationCategoryList) {
        inflater.inflate(R.layout.item_falldiagnosisstorydetail_envenvironment_list, ll_falldiagnosisstorydetail_container, true);

        RecyclerView rv_item_falldiagnosisstorydetail_envevaluation_category = (RecyclerView) ll_falldiagnosisstorydetail_container.findViewById(R.id.rv_item_falldiagnosisstorydetail_envevaluation_category);
        fallDiagnosisStoryDetailEnvEvaluationCategoryAdapter = new FallDiagnosisStoryDetailEnvEvaluationCategoryAdapter(this, environmentEvaluationCategoryList, fallDiagnosisStoryDetailPresenter);
        rv_item_falldiagnosisstorydetail_envevaluation_category.setAdapter(fallDiagnosisStoryDetailEnvEvaluationCategoryAdapter);
        rv_item_falldiagnosisstorydetail_envevaluation_category.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void navigateToPhotoPopupActivity(ArrayList<Photo> photoList) {
        Intent intent = new Intent(this, PhotoPopupActivity.class);
        intent.putExtra("photoList", photoList);
        intent.putExtra("intent_flag", PhotoPopupINTENTFlag.FALLDIAGNOSISSTORYACTIVITY);
        startActivity(intent);
    }

    @Override
    public void navigateToCommentDialogActivityForOwner(CommentInfo commentInfo) {
        Intent intent = new Intent(this, CommentDialogActivity.class);
        intent.putExtra("comment_user_name", commentInfo.getUser_name());
        intent.putExtra("comment_category_id", CommentINTENTFlag.FALL_DIAGNOSIS_STORY);
        intent.putExtra("comment_content", commentInfo.getContent());
        intent.putExtra("comment_id", commentInfo.getComment_id());
        intent.putExtra("position", commentInfo.getPosition());
        intent.putExtra("act_flag", CommentActFlag.FALL_DIAGNOSIS_STORY);
        startActivityForResult(intent, FallDiagnosisStoryCodeFlag.COMMENT_POPUP_REQUEST);
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
        startActivityForResult(intent, FallDiagnosisStoryCodeFlag.COMMENT_POPUP_REQUEST);
    }

    @Override
    public void setFallDiagnosisStoryCommentAdapterList(ArrayList<CommentInfo> commentInfoList) {
        fallDiagnosisStoryDetailCommentAdapter = new FallDiagnosisStoryDetailCommentAdapter(this, commentInfoList, fallDiagnosisStoryDetailPresenter);
        rv_falldiagnosisstorydetail_comment.setAdapter(fallDiagnosisStoryDetailCommentAdapter);
        rv_falldiagnosisstorydetail_comment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void showCommentAdapterNotifyItemInserted() {
        if (fallDiagnosisStoryDetailCommentAdapter != null){
            fallDiagnosisStoryDetailCommentAdapter.notifyItemInserted(fallDiagnosisStoryDetailCommentAdapter.getItemCount() - 1);
        }
    }

    @Override
    public void showCommentEdit(String message) {
        et_falldiagnosisstorydetail_comment.setText(message);
    }

    @Override
    public void showCommentScrollDown() {
        nsv_falldiagnosisstorydetail.fullScroll(NestedScrollView.FOCUS_DOWN);
    }

    @Override
    public void setCommentAdapterAdded(CommentInfo commentInfo) {
        if(fallDiagnosisStoryDetailCommentAdapter != null) {
            fallDiagnosisStoryDetailCommentAdapter.setCommentAdded(commentInfo);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_back:
                finish();
                break;

            case R.id.iv_falldiagnosisstorydetail_sharebtn:

                break;

            case R.id.btn_falldiagnosisstorydetail_comment:
                String comment = et_falldiagnosisstorydetail_comment.getText().toString();
                CommentInfo commentInfo = new CommentInfo();
                commentInfo.setContent(comment);

                fallDiagnosisStoryDetailPresenter.setFalldiagnosisStoryComment(commentInfo);
                break;

            case R.id.iv_falldiagnosisstory_menu:
                fallDiagnosisStoryDetailPresenter.onClickMenuButton();

                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        fallDiagnosisStoryDetailPresenter.onCheckedChangeForLike(isChecked);
    }

    @Override
    public void showCommentAdapterNotifyItemDelete(int position) {
        if(fallDiagnosisStoryDetailCommentAdapter != null) {
            fallDiagnosisStoryDetailCommentAdapter.notifyItemRemoved(position);
            fallDiagnosisStoryDetailCommentAdapter.notifyItemRangeChanged(position, fallDiagnosisStoryDetailCommentAdapter.getItemCount());
        }
    }

    @Override
    public void setCommentAdapterDelete(int position) {
        if(fallDiagnosisStoryDetailCommentAdapter != null) {
            fallDiagnosisStoryDetailCommentAdapter.setCommentDelete(position);
        }
    }

    @Override
    public void setCommentAdapterSetContent(int position, String content) {
        if(fallDiagnosisStoryDetailCommentAdapter != null) {
            fallDiagnosisStoryDetailCommentAdapter.setCommentSetContent(position, content);
        }
    }

    @Override
    public void showCommentAdapterNotifyItemChanged(int position) {
        if(fallDiagnosisStoryDetailCommentAdapter != null) {
            fallDiagnosisStoryDetailCommentAdapter.notifyItemChanged(position);
        }
    }

    @Override
    public void goneMenuButton() {
        iv_falldiagnosisstory_menu.setVisibility(View.GONE);
    }

    @Override
    public void navigateToFallDiagnosisStoryDialogActivity(FallDiagnosisStory fallDiagnosisStory, FallDiagnosisStoryInfo fallDiagnosisStoryInfo) {
        Intent intent = new Intent(this, FallDiagnosisStoryDialogActivity.class);
        intent.putExtra("fallDiagnosisStory", fallDiagnosisStory);
        intent.putExtra("fallDiagnosisStoryInfo", fallDiagnosisStoryInfo);
        startActivityForResult(intent, FallDiagnosisStoryCodeFlag.POPUP_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FallDiagnosisStoryCodeFlag.POPUP_REQUEST:
                switch (resultCode) {
                    case FallDiagnosisStoryCodeFlag.RESULT_OK:
                        FallDiagnosisStory fallDiagnosisStory = (FallDiagnosisStory) data.getSerializableExtra("fallDiagnosisStory");
                        Intent intent = getIntent();
                        intent.putExtra("fallDiagnosisStory", fallDiagnosisStory);
                        setResult(FallDiagnosisStoryCodeFlag.RESULT_DELETE, intent);
                        finish();
                        break;
                }
                break;

            case FallDiagnosisStoryCodeFlag.COMMENT_POPUP_REQUEST:
                switch (resultCode) {
                    case FallDiagnosisStoryCodeFlag.RESULT_OK:

                        int position = data.getIntExtra("position", 0);
                        int commentCodeFlag = data.getIntExtra("flag", 0);
                        String content = data.getStringExtra("content");

                        switch (commentCodeFlag) {
                            case CommentCodeFlag.EDIT_REQUEST:
                                fallDiagnosisStoryDetailPresenter.onActivityResultForCommentEditResultOkModifyOrReport(position, content);
                                break;
                            case CommentCodeFlag.DELETE_REQUEST:
                                fallDiagnosisStoryDetailPresenter.onActivityResultForCommentEditResultOkDELETE(position);
                                break;
                        }
                        break;
                }
        }
    }
}
