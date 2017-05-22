package com.demand.well_family.well_family.dialog.list.songstory.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demand.well_family.well_family.R;

import com.demand.well_family.well_family.dialog.list.songstory.presenter.SongStoryDialogPresenter;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-04-20.
 */

public class SongStoryDialogAdapter extends RecyclerView.Adapter<SongStoryDialogAdapter.SongStoryDialogViewHolder> {
    private Context context;
    private int layout;
    private ArrayList<String> songStoryDialogList;
    private SongStoryDialogPresenter songStoryDialogPresenter;

    public SongStoryDialogAdapter(Context context, ArrayList<String> popupList, int layout, SongStoryDialogPresenter songStoryDialogPresenter) {
        this.context = context;
        this.layout = layout;
        this.songStoryDialogList = popupList;
        this.songStoryDialogPresenter = songStoryDialogPresenter;
    }

    @Override
    public int getItemCount() {
        return songStoryDialogList.size();
    }

    @Override
    public SongStoryDialogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SongStoryDialogViewHolder songStoryDialogViewHolder = new SongStoryDialogViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
        return songStoryDialogViewHolder;
    }

    @Override
    public void onBindViewHolder(SongStoryDialogViewHolder holder, final int dialogPosition) {
        holder.tv_popup_comment_text.setText(songStoryDialogList.get(dialogPosition));
        holder.tv_popup_comment_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songStoryDialogPresenter.onClickSongStoryDialog(dialogPosition);
            }
        });
    }

    public class SongStoryDialogViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_popup_comment_text;

        public SongStoryDialogViewHolder(View itemView) {
            super(itemView);
            tv_popup_comment_text = (TextView) itemView.findViewById(R.id.tv_popup_comment_text);
        }
    }
}


