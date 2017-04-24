package com.demand.well_family.well_family.dialog.list.story.adpater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demand.well_family.well_family.dialog.list.story.adpater.holder.StoryDialogViewHolder;
import com.demand.well_family.well_family.dialog.list.story.presenter.StoryDialogPresenter;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-20.
 */

public class StoryDialogAdapter extends RecyclerView.Adapter<StoryDialogViewHolder> {
    private Context context;
    private int layout;
    private ArrayList<String> popupList;
    private StoryDialogPresenter storyDialogPresenter;

    public StoryDialogAdapter(Context context, ArrayList<String> popupList, int layout, StoryDialogPresenter storyDialogPresenter) {
        this.context = context;
        this.layout = layout;
        this.popupList = popupList;
        this.storyDialogPresenter = storyDialogPresenter;
    }

    @Override
    public int getItemCount() {
        return popupList.size();
    }

    @Override
    public StoryDialogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        StoryDialogViewHolder storyDialogViewHolder = new StoryDialogViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
        return storyDialogViewHolder;
    }

    @Override
    public void onBindViewHolder(StoryDialogViewHolder holder, final int dialogPosition) {
        holder.tv_popup_comment_text.setText(popupList.get(dialogPosition));
        holder.tv_popup_comment_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storyDialogPresenter.onClickStoryDialog(dialogPosition);
            }
        });
    }
}
