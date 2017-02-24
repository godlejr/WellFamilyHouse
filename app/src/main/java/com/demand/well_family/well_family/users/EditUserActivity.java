package com.demand.well_family.well_family.users;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.log.LogFlag;
import com.demand.well_family.well_family.util.RealPathUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by ㅇㅇ on 2017-02-23.
 */

public class EditUserActivity extends Activity {
    private CircleImageView iv_edit_profile_avatar;
    private ImageButton ib_edit_profile_avatar;
    private TextView tv_edit_profile_email;
    private EditText et_edit_profile_name;
    private EditText et_edit_profile_birth;
    private EditText et_edit_profile_phone;
    private RadioButton rb_female;
    private RadioButton rb_man;

    private RecyclerView rv_profile_favorite;
    private RecyclerView rv_profile_song;
    private ProfileOptionAdapter profileOptionAdapter;
    private ProfileSongAdapter profileSongAdapter;

    private String dateStr, birthDate;

    // userInfo
    private int user_id;
    private String user_name;
    private String user_avatar;
    private String user_email;
    private String user_birth;
    private String user_phone;
    private int user_level;

    private static final Logger logger = LoggerFactory.getLogger(EditUserActivity.class);
    private static final int PICK_PHOTO = 777;
    private RealPathUtil realPathUtil;
    private String avatarPath;
    private int user_gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        init();
        setToolbar(getWindow().getDecorView());
    }


    private void init() {
        iv_edit_profile_avatar = (CircleImageView) findViewById(R.id.iv_edit_profile_avatar);
        ib_edit_profile_avatar = (ImageButton) findViewById(R.id.ib_edit_profile_avatar);
        tv_edit_profile_email = (TextView) findViewById(R.id.tv_edit_profile_email);
        et_edit_profile_name = (EditText) findViewById(R.id.et_edit_profile_name);
        et_edit_profile_birth = (EditText) findViewById(R.id.et_edit_profile_birth);
        et_edit_profile_phone = (EditText) findViewById(R.id.et_edit_profile_phone);
        rb_female = (RadioButton) findViewById(R.id.rb_female);
        rb_man = (RadioButton) findViewById(R.id.rb_man);
        rv_profile_favorite = (RecyclerView) findViewById(R.id.rv_profile_favorite);
        rv_profile_song = (RecyclerView) findViewById(R.id.rv_profile_song);
        realPathUtil = new RealPathUtil();

        user_id = getIntent().getIntExtra("user_id", 0);
        user_name = getIntent().getStringExtra("user_name");
        user_avatar = getIntent().getStringExtra("user_avatar");
        user_email = getIntent().getStringExtra("user_email");
        user_birth = getIntent().getStringExtra("user_birth");
        user_phone = getIntent().getStringExtra("user_phone");
        user_level = getIntent().getIntExtra("user_level", 0);

        tv_edit_profile_email.setText(user_email);
        et_edit_profile_name.setText(user_name);
        et_edit_profile_phone.setText(user_phone);
        Glide.with(EditUserActivity.this).load(getString(R.string.cloud_front_user_avatar) + user_avatar).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_edit_profile_avatar);

        et_edit_profile_birth.setText(user_birth);
        et_edit_profile_birth.setFocusable(false);
        et_edit_profile_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePicker = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        dateStr = String.valueOf(year) + "-" + String.valueOf((month + 1)) + "-" + String.valueOf(dayOfMonth);
                        try {
                            Date date = formatter.parse(dateStr);
                            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
                            birthDate = transFormat.format(date);
                            et_edit_profile_birth.setText(birthDate);
                        } catch (ParseException e) {
                            log(e);
                        }

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)
                        , calendar.get(Calendar.DAY_OF_MONTH));

                datePicker.show();
            }
        });


        rb_man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_gender = 1;
            }
        });

        rb_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_gender = 2;
            }
        });

        ib_edit_profile_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_PHOTO);
            }
        });

        ArrayList<String> favoriteList = new ArrayList<>();
        favoriteList.add("건강");
        favoriteList.add("건강");
        favoriteList.add("건강");
        favoriteList.add("건강");
        favoriteList.add("건강");
        favoriteList.add("건강");
        favoriteList.add("건강");
        favoriteList.add("건강");

        profileOptionAdapter = new ProfileOptionAdapter(this, favoriteList, R.layout.item_text);
        rv_profile_favorite.setAdapter(profileOptionAdapter);
        rv_profile_favorite.setLayoutManager(new GridLayoutManager(EditUserActivity.this, 3));

        ArrayList<String> songList = new ArrayList<>();
        songList.add("가요");
        songList.add("가요");
        songList.add("가요");
        songList.add("가요");
        songList.add("가요");
        songList.add("가요");

        profileSongAdapter = new ProfileSongAdapter(this, songList, R.layout.item_text);
        rv_profile_song.setAdapter(profileSongAdapter);
        rv_profile_song.setLayoutManager(new GridLayoutManager(EditUserActivity.this, 3));
    }

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
        toolbar_title.setText("프로필 설정");

        Button toolbar_complete = (Button) toolbar.findViewById(R.id.toolbar_complete);
        toolbar_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_name = et_edit_profile_name.getText().toString();
                user_birth = et_edit_profile_birth.getText().toString();
                user_phone = et_edit_profile_phone.getText().toString();
                user_email = tv_edit_profile_email.getText().toString();



            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_PHOTO) {
                Uri uri = data.getData();
                try {
                    avatarPath = realPathUtil.getRealPathFromURI_API19(this, uri);
                } catch (RuntimeException e) {
                    log(e);
                    avatarPath = realPathUtil.getRealPathFromURI_API11to18(this, uri);
                }

                Glide.with(EditUserActivity.this).load(uri).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_edit_profile_avatar);
            }
        }
    }

    private class ProfileOptionViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_option;

        public ProfileOptionViewHolder(View itemView) {
            super(itemView);
            tv_option = (TextView) itemView.findViewById(R.id.tv_option);
        }
    }
    private class ProfileOptionAdapter extends RecyclerView.Adapter<ProfileOptionViewHolder> {
        private Context context;
        private ArrayList<String> optionList;
        private int layout;

        public ProfileOptionAdapter(Context context, ArrayList<String> optionList, int layout) {
            this.context = context;
            this.optionList = optionList;
            this.layout = layout;
        }

        @Override
        public ProfileOptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ProfileOptionViewHolder profileOptionViewHolder = new ProfileOptionViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
            return profileOptionViewHolder;
        }

        @Override
        public void onBindViewHolder(final ProfileOptionViewHolder holder, int position) {
            holder.tv_option.setText(optionList.get(position));
            holder.tv_option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    holder.tv_option.setBackgroundResource(R.drawable.round_corner_border_brown);
                    holder.tv_option.setTextColor(Color.parseColor("#542920"));



                }
            });
        }

        @Override
        public int getItemCount() {
            return optionList.size();
        }
    }

    private class ProfileSongViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_option;

        public ProfileSongViewHolder(View itemView) {
            super(itemView);
            tv_option = (TextView) itemView.findViewById(R.id.tv_option);
        }
    }
    private class ProfileSongAdapter extends RecyclerView.Adapter<ProfileSongViewHolder> {
        private Context context;
        private ArrayList<String> optionList;
        private int layout;

        public ProfileSongAdapter(Context context, ArrayList<String> optionList, int layout) {
            this.context = context;
            this.optionList = optionList;
            this.layout = layout;
        }

        @Override
        public ProfileSongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ProfileSongViewHolder profileOptionViewHolder = new ProfileSongViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
            return profileOptionViewHolder;
        }

        @Override
        public void onBindViewHolder(ProfileSongViewHolder holder, int position) {
            holder.tv_option.setText(optionList.get(position));
        }

        @Override
        public int getItemCount() {
            return optionList.size();
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
