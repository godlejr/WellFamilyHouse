package com.demand.well_family.well_family.dialog.list.songstory.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.demand.well_family.well_family.R;

/**
 * Created by ㅇㅇ on 2017-04-20.
 */

public class SongStoryDialogViewHolder extends RecyclerView.ViewHolder {
    public TextView tv_popup_comment_text;

    public SongStoryDialogViewHolder(View itemView) {
        super(itemView);
        tv_popup_comment_text = (TextView) itemView.findViewById(R.id.tv_popup_comment_text);
    }
}
