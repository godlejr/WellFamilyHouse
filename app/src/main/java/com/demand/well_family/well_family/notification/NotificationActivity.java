package com.demand.well_family.well_family.notification;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.Notification;

import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-03-07.
 */

public class NotificationActivity extends Activity {
    private RecyclerView rv_notification;
    private NotificationAdapter notificationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        setToolbar(getWindow().getDecorView());
        init();
    }

    // toolbar_main & menu
    public void setToolbar(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolBar);
        ImageView toolbar_back = (ImageView) toolbar.findViewById(R.id.toolbar_back);
        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText("알림");

    }

    private void init(){
        ArrayList<Notification> notiList = new ArrayList<>();
        notiList.add(new Notification("con", "date", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTZcXRi3FUxjS2tSIDtmOSq3MsK9OeUrEpcy8QyJUlgV-bC090eltwQGOR6"));


        rv_notification = (RecyclerView)findViewById(R.id.rv_notification);
        rv_notification.setLayoutManager(new LinearLayoutManager(NotificationActivity.this, LinearLayoutManager.VERTICAL, false));
        notificationAdapter = new NotificationAdapter(this, notiList, R.layout.item_notification);

        rv_notification.setAdapter(notificationAdapter);
    }

    private class NotificationViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_notification_content;
        private TextView tv_notification_date;
        private ImageView iv_notification_avatar;

        public NotificationViewHolder(final View itemView) {
            super(itemView);
            tv_notification_content = (TextView) itemView.findViewById(R.id.tv_notification_content);
            tv_notification_date = (TextView) itemView.findViewById(R.id.tv_notification_date);
            iv_notification_avatar = (ImageView)itemView.findViewById(R.id.iv_notification_avatar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemView.setBackgroundColor(Color.parseColor("#fff9e8"));

                }
            });
        }
    }

    private class NotificationAdapter extends RecyclerView.Adapter<NotificationViewHolder> {
        private Context context;
        private ArrayList<Notification> notificationList;
        private int layout;

        public NotificationAdapter(Context context, ArrayList<Notification> notificationList, int layout) {
            this.context = context;
            this.notificationList = notificationList;
            this.layout = layout;
        }

        @Override
        public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            NotificationViewHolder profileOptionViewHolder = new NotificationViewHolder(LayoutInflater.from(NotificationActivity.this).inflate(layout, parent, false));
            return profileOptionViewHolder;
        }

        @Override
        public void onBindViewHolder(final NotificationViewHolder holder, final int position) {
            holder.tv_notification_content.setText(notificationList.get(position).getContent());
            holder.tv_notification_date.setText(notificationList.get(position).getDate());
            Glide.with(NotificationActivity.this).load(notificationList.get(position).getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_notification_avatar);

        }

        @Override
        public int getItemCount() {
            return notificationList.size();
        }
    }


}
