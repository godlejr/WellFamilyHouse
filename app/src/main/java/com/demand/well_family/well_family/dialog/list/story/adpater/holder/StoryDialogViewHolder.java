package com.demand.well_family.well_family.dialog.list.story.adpater.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.demand.well_family.well_family.R;

/**
 * Created by ㅇㅇ on 2017-04-20.
 */

public class StoryDialogViewHolder extends RecyclerView.ViewHolder{
    public TextView tv_popup_comment_text;

    public StoryDialogViewHolder(View itemView) {
        super(itemView);
        tv_popup_comment_text = (TextView) itemView.findViewById(R.id.tv_popup_comment_text);
    }
}
