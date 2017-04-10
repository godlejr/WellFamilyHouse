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
import com.demand.well_family.well_family.connection.FamilyServerConnection;
import com.demand.well_family.well_family.connection.NotificationServerConnection;
import com.demand.well_family.well_family.connection.UserServerConnection;
import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.Notification;
import com.demand.well_family.well_family.dto.NotificationInfo;
import com.demand.well_family.well_family.family.FamilyActivity;
import com.demand.well_family.well_family.family.ManageFamilyActivity;
import com.demand.well_family.well_family.family.ManageFamilyListActivity;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.flag.NotificationBEHAVIORFlag;
import com.demand.well_family.well_family.flag.NotificationINTENTFlag;
import com.demand.well_family.well_family.interceptor.HeaderInterceptor;
import com.demand.well_family.well_family.util.ErrorUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    private String access_token;

    private NotificationServerConnection notificationServerConnection;
    private FamilyServerConnection familyServerConnection;
    private UserServerConnection userServerConnection;
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
        access_token = loginInfo.getString("access_token", null);

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

        userServerConnection = new HeaderInterceptor(access_token).getClientForUserServer().create(UserServerConnection.class);
        Call<ArrayList<Notification>> call_notifications = userServerConnection.notifications(user_id);
        call_notifications.enqueue(new Callback<ArrayList<Notification>>() {
            @Override
            public void onResponse(Call<ArrayList<Notification>> call, Response<ArrayList<Notification>> response) {
                if (response.isSuccessful()) {
                    notiList = response.body();

                    rv_notification.setLayoutManager(new LinearLayoutManager(NotificationActivity.this, LinearLayoutManager.VERTICAL, false));
                    notificationAdapter = new NotificationAdapter(NotificationActivity.this, notiList, R.layout.item_notification);
                    rv_notification.setAdapter(notificationAdapter);
                } else {
                    Toast.makeText(NotificationActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                }
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
        private NotificationServerConnection notificationServerConnection;
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
            final int id = notificationList.get(position).getId();
            final int behavior_id = notificationList.get(position).getBehavior_id();
            final int intent_id = notificationList.get(position).getIntent_id();
            final int intent_flag = notificationList.get(position).getIntent_flag();
            final int checked = notificationList.get(position).getChecked();

            if (behavior_id == NotificationBEHAVIORFlag.CREATING_THE_FAMILY ||  behavior_id == NotificationBEHAVIORFlag.JOIN ||  behavior_id == NotificationBEHAVIORFlag.WANT_TO_JOIN ||  behavior_id == NotificationBEHAVIORFlag.INVITED ) {
                //creating family
                notificationServerConnection = new HeaderInterceptor(access_token).getClientForNotificationServer().create(NotificationServerConnection.class);
                Call<NotificationInfo> call_notificationInfoForCreatingFamily = notificationServerConnection.NotificationForCreatingFamilyAndJoinAndWantToJoin(id);
                call_notificationInfoForCreatingFamily.enqueue(new Callback<NotificationInfo>() {
                    @Override
                    public void onResponse(Call<NotificationInfo> call, Response<NotificationInfo> response) {
                        if (response.isSuccessful()) {




                            NotificationInfo notificationInfo = response.body();
                            String name = notificationInfo.getName();
                            String content = notificationInfo.getContent();

                            Glide.with(NotificationActivity.this).load(getString(R.string.cloud_front_user_avatar) + notificationInfo.getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_notification_avatar);


                            if( behavior_id == NotificationBEHAVIORFlag.CREATING_THE_FAMILY) {
                                holder.tv_notification_content.setText(name + "님! <" + content + "> 가족 페이지 개설을 축하합니다.");
                            }

                            if( behavior_id == NotificationBEHAVIORFlag.JOIN) {
                                holder.tv_notification_content.setText(name + "님이 <" + content + "> 에 가입되었습니다.");
                            }

                            if( behavior_id == NotificationBEHAVIORFlag.WANT_TO_JOIN) {
                                holder.tv_notification_content.setText(name + "님이 <" + content + "> 에 가입하고 싶어합니다.");
                            }

                            if( behavior_id == NotificationBEHAVIORFlag.INVITED) {
                                holder.tv_notification_content.setText(name + "님이 " + content + "님을 가족으로 초대하였습니다.");
                            }

                            holder.ll_noti_photo.setVisibility(View.VISIBLE);

                            Glide.with(NotificationActivity.this).load(getString(R.string.cloud_front_family_avatar) + notificationInfo.getPhoto()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_noti_photo);
                        } else {
                            Toast.makeText(NotificationActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<NotificationInfo> call, Throwable t) {
                        log(t);
                        Toast.makeText(NotificationActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                    }
                });
            }

            if (behavior_id == NotificationBEHAVIORFlag.WRITING_THE_COMMENT) {
                notificationServerConnection = new HeaderInterceptor(access_token).getClientForNotificationServer().create(NotificationServerConnection.class);
                Call<NotificationInfo> call_notificationInfoForWritingComment = notificationServerConnection.NotificationForWritingComment(id);
                call_notificationInfoForWritingComment.enqueue(new Callback<NotificationInfo>() {
                    @Override
                    public void onResponse(Call<NotificationInfo> call, Response<NotificationInfo> response) {
                        if (response.isSuccessful()) {
                            NotificationInfo notificationInfo = response.body();
                            Glide.with(NotificationActivity.this).load(getString(R.string.cloud_front_user_avatar) + notificationInfo.getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_notification_avatar);
                            holder.tv_notification_content.setText(notificationInfo.getName() + "님이 " + notificationInfo.getContent() + "에 댓글을 남겼습니다.");
                        } else {
                            Toast.makeText(NotificationActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<NotificationInfo> call, Throwable t) {
                        log(t);
                        Toast.makeText(NotificationActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                    }
                });
            }

            if (behavior_id == NotificationBEHAVIORFlag.LIKE) {
                notificationServerConnection = new HeaderInterceptor(access_token).getClientForNotificationServer().create(NotificationServerConnection.class);
                Call<NotificationInfo> call_notificationInfoForLike = notificationServerConnection.NotificationForLike(id);
                call_notificationInfoForLike.enqueue(new Callback<NotificationInfo>() {
                    @Override
                    public void onResponse(Call<NotificationInfo> call, Response<NotificationInfo> response) {
                        if (response.isSuccessful()) {
                            NotificationInfo notificationInfo = response.body();
                            Glide.with(NotificationActivity.this).load(getString(R.string.cloud_front_user_avatar) + notificationInfo.getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_notification_avatar);
                            holder.tv_notification_content.setText(notificationInfo.getName() + "님이 " + notificationInfo.getContent() + "을 좋아합니다.");
                        } else {
                            Toast.makeText(NotificationActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<NotificationInfo> call, Throwable t) {
                        log(t);
                        Toast.makeText(NotificationActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                    }
                });
            }


            if (behavior_id == NotificationBEHAVIORFlag.WRITING_THE_STORY) {
                //writing the story
                if (intent_flag == NotificationINTENTFlag.STORY_DETAIL) {
                    //family story
                    notificationServerConnection = new HeaderInterceptor(access_token).getClientForNotificationServer().create(NotificationServerConnection.class);
                    Call<NotificationInfo> call_notificationInfoForWritingStory = notificationServerConnection.NotificationForWritingStory(id);
                    call_notificationInfoForWritingStory.enqueue(new Callback<NotificationInfo>() {
                        @Override
                        public void onResponse(Call<NotificationInfo> call, Response<NotificationInfo> response) {
                            if (response.isSuccessful()) {
                                NotificationInfo notificationInfo = response.body();
                                Glide.with(NotificationActivity.this).load(getString(R.string.cloud_front_user_avatar) + notificationInfo.getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_notification_avatar);
                                holder.tv_notification_content.setText(notificationInfo.getName() + "님이 <" + notificationInfo.getContent() + "> 에 게시글을 남겼습니다. : \"" + notificationInfo.getTitle() + "\"");
                                if (notificationInfo.getPhoto() != null) {
                                    holder.ll_noti_photo.setVisibility(View.VISIBLE);
                                    Glide.with(NotificationActivity.this).load(getString(R.string.cloud_front_stories_images) + notificationInfo.getPhoto()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_noti_photo);
                                } else {
                                    holder.ll_noti_photo.setVisibility(View.GONE);
                                }
                            } else {
                                Toast.makeText(NotificationActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<NotificationInfo> call, Throwable t) {
                            log(t);
                            Toast.makeText(NotificationActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                if (intent_flag == NotificationINTENTFlag.SONG_STORY_DETAIL) {
                    //song story
                    notificationServerConnection = new HeaderInterceptor(access_token).getClientForNotificationServer().create(NotificationServerConnection.class);
                    Call<NotificationInfo> call_notificationInfoForWritingSongStory = notificationServerConnection.NotificationForWritingSongStory(id);
                    call_notificationInfoForWritingSongStory.enqueue(new Callback<NotificationInfo>() {
                        @Override
                        public void onResponse(Call<NotificationInfo> call, Response<NotificationInfo> response) {
                            if (response.isSuccessful()) {
                                NotificationInfo notificationInfo = response.body();
                                Glide.with(NotificationActivity.this).load(getString(R.string.cloud_front_user_avatar) + notificationInfo.getAvatar()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_notification_avatar);
                                holder.tv_notification_content.setText(notificationInfo.getName() + "님이 <" + notificationInfo.getContent() + "> 에 게시글을 남겼습니다. : \"" + notificationInfo.getTitle() + "\"");

                                if (notificationInfo.getPhoto() != null) {
                                    holder.ll_noti_photo.setVisibility(View.VISIBLE);
                                    Glide.with(NotificationActivity.this).load(getString(R.string.cloud_front_song_stories_images) + notificationInfo.getPhoto()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.iv_noti_photo);
                                } else {
                                    holder.ll_noti_photo.setVisibility(View.GONE);
                                }
                            } else {
                                Toast.makeText(NotificationActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<NotificationInfo> call, Throwable t) {
                            log(t);
                            Toast.makeText(NotificationActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }

            if (checked == 1) {
                holder.ll_notification.setBackgroundColor(Color.parseColor("#ffffff"));
            }

            holder.ll_notification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (intent_flag == NotificationINTENTFlag.FAMILY) {
                        //familyActivity
                        familyServerConnection = new HeaderInterceptor(access_token).getClientForFamilyServer().create(FamilyServerConnection.class);
                        final Call<Family> call_family = familyServerConnection.family(intent_id);

                        call_family.enqueue(new Callback<Family>() {
                            @Override
                            public void onResponse(Call<Family> call, Response<Family> response) {
                                if (response.isSuccessful()) {
                                    final Family familyInfo = response.body();
                                    notificationServerConnection = new HeaderInterceptor(access_token).getClientForNotificationServer().create(NotificationServerConnection.class);
                                    Call<ResponseBody> call_update_check = notificationServerConnection.notificationInfo(id);
                                    call_update_check.enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            if (response.isSuccessful()) {
                                                if (checked == 0) {
                                                    holder.ll_notification.setBackgroundColor(Color.parseColor("#ffffff"));
                                                }
                                                Intent intent = new Intent(NotificationActivity.this, FamilyActivity.class);
                                                //family info
                                                intent.putExtra("family_id", familyInfo.getId());
                                                intent.putExtra("family_name", familyInfo.getName());
                                                intent.putExtra("family_content", familyInfo.getContent());
                                                intent.putExtra("family_avatar", familyInfo.getAvatar());
                                                intent.putExtra("family_user_id", familyInfo.getUser_id());
                                                intent.putExtra("family_created_at", familyInfo.getCreated_at());
                                                intent.putExtra("notification_flag",true);

                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(NotificationActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            log(t);
                                            Toast.makeText(NotificationActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                } else {
                                    Toast.makeText(NotificationActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Family> call, Throwable t) {
                                log(t);
                                Toast.makeText(NotificationActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    if (intent_flag == NotificationINTENTFlag.STORY_DETAIL) {
                        notificationServerConnection = new HeaderInterceptor(access_token).getClientForNotificationServer().create(NotificationServerConnection.class);
                        Call<ResponseBody> call_update_check = notificationServerConnection.notificationInfo(id);
                        call_update_check.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    if (checked == 0) {
                                        holder.ll_notification.setBackgroundColor(Color.parseColor("#ffffff"));
                                    }
                                    Intent intent = new Intent(NotificationActivity.this, NotificationFamilyStoryDetail.class);
                                    intent.putExtra("story_id", intent_id);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(NotificationActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                log(t);
                                Toast.makeText(NotificationActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    if (intent_flag == NotificationINTENTFlag.SONG_STORY_DETAIL) {
                        notificationServerConnection = new HeaderInterceptor(access_token).getClientForNotificationServer().create(NotificationServerConnection.class);
                        Call<ResponseBody> call_update_check = notificationServerConnection.notificationInfo(id);
                        call_update_check.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    if (checked == 0) {
                                        holder.ll_notification.setBackgroundColor(Color.parseColor("#ffffff"));
                                    }
                                    Intent intent = new Intent(NotificationActivity.this, NotificationSongStoryDetail.class);
                                    intent.putExtra("story_id", intent_id);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(NotificationActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                log(t);
                                Toast.makeText(NotificationActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    if(intent_flag == NotificationINTENTFlag.MANAGE_FAMILY){
                        notificationServerConnection = new HeaderInterceptor(access_token).getClientForNotificationServer().create(NotificationServerConnection.class);
                        Call<ResponseBody> call_update_check = notificationServerConnection.notificationInfo(id);
                        call_update_check.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    if (checked == 0) {
                                        holder.ll_notification.setBackgroundColor(Color.parseColor("#ffffff"));
                                    }
                                    Intent intent = new Intent(NotificationActivity.this, ManageFamilyActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(NotificationActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                log(t);
                                Toast.makeText(NotificationActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    if(intent_flag == NotificationINTENTFlag.MANAGE_FAMILY_DETAIL){
                        familyServerConnection = new HeaderInterceptor(access_token).getClientForFamilyServer().create(FamilyServerConnection.class);
                        final Call<Family> call_family = familyServerConnection.family(intent_id);

                        call_family.enqueue(new Callback<Family>() {
                            @Override
                            public void onResponse(Call<Family> call, Response<Family> response) {
                                if (response.isSuccessful()) {
                                    final Family familyInfo = response.body();
                                    notificationServerConnection = new HeaderInterceptor(access_token).getClientForNotificationServer().create(NotificationServerConnection.class);
                                    Call<ResponseBody> call_update_check = notificationServerConnection.notificationInfo(id);
                                    call_update_check.enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            if (response.isSuccessful()) {
                                                if (checked == 0) {
                                                    holder.ll_notification.setBackgroundColor(Color.parseColor("#ffffff"));
                                                }
                                                Intent intent = new Intent(NotificationActivity.this, ManageFamilyListActivity.class);
                                                //family info
                                                intent.putExtra("family_id", familyInfo.getId());
                                                intent.putExtra("family_name", familyInfo.getName());
                                                intent.putExtra("family_content", familyInfo.getContent());
                                                intent.putExtra("family_avatar", familyInfo.getAvatar());
                                                intent.putExtra("family_user_id", familyInfo.getUser_id());
                                                intent.putExtra("family_created_at", familyInfo.getCreated_at());
                                                intent.putExtra("notification_flag", true);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(NotificationActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            log(t);
                                            Toast.makeText(NotificationActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                } else {
                                    Toast.makeText(NotificationActivity.this, new ErrorUtils(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Family> call, Throwable t) {
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
