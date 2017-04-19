package com.demand.well_family.well_family.dialog.list.comment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demand.well_family.well_family.R;

import com.demand.well_family.well_family.dialog.list.comment.adapter.holder.CommentDialogViewHolder;
import com.demand.well_family.well_family.dialog.list.comment.presenter.CommentDialogPresenter;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-19.
 */

public class CommentDialogAdapter extends RecyclerView.Adapter<CommentDialogViewHolder> {
    private CommentDialogPresenter commentDialogPresenter;
    private Context context;
    private ArrayList<String> commentDialogList;

    public CommentDialogAdapter(Context context, ArrayList<String> commentDialogList, CommentDialogPresenter commentDialogPresenter) {
        this.context = context;
        this.commentDialogList = commentDialogList;
        this.commentDialogPresenter = commentDialogPresenter;
    }

    @Override
    public CommentDialogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommentDialogViewHolder commentDialogViewHolder = new CommentDialogViewHolder(LayoutInflater.from(context).inflate(R.layout.item_textview, parent, false));
        return commentDialogViewHolder;
    }

    @Override
    public void onBindViewHolder(CommentDialogViewHolder holder, final int position) {
        holder.tv_popup_comment_text.setText(commentDialogList.get(position));
        holder.tv_popup_comment_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentDialogPresenter.onClickCommentDialog(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentDialogList.size();
    }

}

