package com.demand.well_family.well_family.notification;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.connection.Server_Connection;
import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.Notification;
import com.demand.well_family.well_family.dto.NotificationInfo;
import com.demand.well_family.well_family.family.FamilyActivity;
import com.demand.well_family.well_family.log.LogFlag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ㅇㅇ on 2017-03-07.
 */

public class NotificationActivity extends Activity {
    private RecyclerView rv_notification;
    private NotificationAdapter notificationAdapter;
    private SharedPreferences loginInfo;

    private int user_id;
    private String user_email;
    private String user_name;
    private String user_birth;
    private String user_phone;
    private int user_level;
    private String user_avatar;

    private Server_Connection server_connection;
    private ArrayList<Notification> notiList;

    private static final Logger logger = LoggerFactory.getLogger(NotificationActivity.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        setUserInfo();
        init();
    }

    private void setUserInfo() {
        loginInfo = getSharedPreferences("loginInfo", Activity.MODE_PRIVATE);
        user_id = loginInfo.getInt("user_id", 0);
        user_level = loginInfo.getInt("user_level", 0);
        user_name = loginInfo.getString("user_name", null);
        user_email = loginInfo.getString("user_email", null);
        user_birth = loginInfo.getString("user_birth", null);
        user_avatar = loginInfo.getString("user_avatar", null);
        user_phone = loginInfo.getString("user_phone", null);
        setToolbar(getWindow().getDecorView());
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

    private void init() {
        rv_notification = (RecyclerView) findViewById(R.id.rv_notification);

        server_connection = Server_Connection.retrofit.create(Server_Connection.class);
        Call<ArrayList<Notification>> call_notifications = server_connection.notifications(user_id);
        call_notifications.enqueue(new Callback<ArrayList<Notification>>() {
            @Override
            public void onResponse(Call<ArrayList<Notification>> call, Response<ArrayList<Notification>> response) {
                notiList = response.body();

                rv_notification.setLayoutManager(new LinearLayoutManager(NotificationActivity.this, LinearLayoutManager.VERTICAL, false));
                notificationAdapter = new NotificationAdapter(NotificationActivity.this, notiList, R.layout.item_notification);
                rv_notification.setAdapter(notificationAdapter);
            }

            @Override
            public void onFailure(Call<ArrayList<Notification>> call, Throwable t) {
                log(t);
                Toast.makeText(NotificationActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
            }
        });


    }

    private class NotificationViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout ll_notification;
        private TextView tv_notification_content;
        private TextView tv_notification_date;
        private ImageView iv_notification_avatar;
        private LinearLayout ll_noti_photo;
        private ImageView iv_noti_photo;

        public NotificationViewHolder(final View itemView) {
            super(itemView);

            ll_notification = (LinearLayout) itemView.findViewById(R.id.ll_notification);
            tv_notification_content = (TextView) itemView.findViewById(R.id.tv_notification_content);
            tv_notification_date = (TextView) itemView.findViewById(R.id.tv_notification_date);
            iv_notification_avatar = (ImageView) itemView.findViewById(R.id.iv_notification_avatar);
            ll_noti_photo = (LinearLayout) itemView.findViewById(R.id.ll_noti_photo);
            iv_noti_photo = (ImageView) itemView.findViewById(R.id.iv_noti_photo);

        }
    }

    private class NotificationAdapter extends RecyclerView.Adapter<NotificationViewHolder> {
        private Context context;
        private ArrayList<Notification> notificationList;
        private Server_Connection server_connection;
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

            if (notificationList.get(position).getBehavior_id() == 1) {
                server_connection = Server_Connection.retrofit.create(Server_Connection.class);
                Call<NotificationInfo> call_notificationInfoForCreatingFamily = server_connection.NotificationForCreatingFamily(notificationList.get(position).getId());
                call_notificationInfoForCreatingFamily.enqueue(new Callback<NotificationInfo>() {
                    @Override
                    public void onResponse(Call<NotificationInfo> call, Response<NotificationInfo> response) {
                        NotificationInfo notificationInfo = response.body();
                        Glide.with(NotificationActivity.this).load(getString(R.string.cloud_front_user_avatar) + notificationInfo.getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_notification_avatar);
                        holder.tv_notification_content.setText(notificationInfo.getName() + "님! <" + notificationInfo.getContent() + "> 가족 페이지 개설을 축하합니다.^^");
                        holder.ll_noti_photo.setVisibility(View.VISIBLE);

                        Glide.with(NotificationActivity.this).load(getString(R.string.cloud_front_family_avatar) + notificationInfo.getPhoto()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_noti_photo);
                    }

                    @Override
                    public void onFailure(Call<NotificationInfo> call, Throwable t) {
                        log(t);
                        Toast.makeText(NotificationActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                    }
                });
            }
            if (notificationList.get(position).getChecked() == 1) {
                holder.ll_notification.setBackgroundColor(Color.parseColor("#ffffff"));
            }

            holder.ll_notification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(notificationList.get(position).getIntent_flag()==1){
                        //familyActivity
                        server_connection = Server_Connection.retrofit.create(Server_Connection.class);
                        final Call<ArrayList<Family>> call_family  = server_connection.family_info_by_creator(notificationList.get(position).getIntent_id());

                        call_family.enqueue(new Callback<ArrayList<Family>>() {
                            @Override
                            public void onResponse(Call<ArrayList<Family>> call, Response<ArrayList<Family>> response) {
                                final ArrayList<Family> familyList = response.body();

                                server_connection = Server_Connection.retrofit.create(Server_Connection.class);

                                HashMap<String,String> map= new HashMap<String, String>();
                                map.put("notification_id",String.valueOf(notificationList.get(position).getId()));

                                Call<ResponseBody> call_update_check = server_connection.update_notification_check(map);
                                call_update_check.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        Intent intent = new Intent(NotificationActivity.this, FamilyActivity.class);
                                        //family info
                                        intent.putExtra("family_id", familyList.get(0).getId());
                                        intent.putExtra("family_name", familyList.get(0).getName());
                                        intent.putExtra("family_content", familyList.get(0).getContent());
                                        intent.putExtra("family_avatar", familyList.get(0).getAvatar());
                                        intent.putExtra("family_user_id", familyList.get(0).getUser_id());
                                        intent.putExtra("family_created_at", familyList.get(0).getCreated_at());
                                        intent.putExtra("notification_flag", 1);

                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        log(t);
                                        Toast.makeText(NotificationActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Call<ArrayList<Family>> call, Throwable t) {
                                log(t);
                                Toast.makeText(NotificationActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });

            holder.tv_notification_date.setText(calculateTime(notificationList.get(position).getCreated_at()));

        }

        public String calculateTime(String dateTime) {
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = transFormat.parse(dateTime);
            } catch (ParseException e) {
                log(e);
            }

            long curTime = System.currentTimeMillis();
            long regTime = date.getTime();
            long diffTime = (curTime - regTime) / 1000;

            String msg = null;

            if (diffTime < 60) {
                msg = diffTime + "초전";
            } else if ((diffTime /= 60) < 60) {
                System.out.println(diffTime);
                msg = diffTime + "분전";
            } else if ((diffTime /= 60) < 24) {
                msg = (diffTime) + "시간전";
            } else if ((diffTime /= 24) < 7) {
                msg = (diffTime) + "일전";
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yy.M.d aa h:mm");
                msg = sdf.format(date);
            }

            return msg;
        }

        @Override
        public int getItemCount() {
            return notificationList.size();
        }
    }


    private static void log(Throwable throwable) {
        StackTraceElement[] ste = throwable.getStackTrace();
        String className = ste[0].getClassName();
        String methodName = ste[0].getMethodName();
        int lineNumber = ste[0].getLineNumber();
        String fileName = ste[0].getFileName();

        if (LogFlag.printFlag) {
            if (logger.isInfoEnabled()) {
                logger.info("Exception: " + throwable.getMessage());
                logger.info(className + "." + methodName + " " + fileName + " " + lineNumber + " " + "line");
            }
        }
    }

}
