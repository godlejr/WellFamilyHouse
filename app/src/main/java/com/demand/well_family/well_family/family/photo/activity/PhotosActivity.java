package com.demand.well_family.well_family.family.photo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dialog.popup.photo.activity.PhotoPopupActivity;
import com.demand.well_family.well_family.repository.FamilyServerConnection;
import com.demand.well_family.well_family.repository.interceptor.HeaderInterceptor;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.flag.LogFlag;
import com.demand.well_family.well_family.util.ErrorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.demand.well_family.well_family.main.login.activity.LoginActivity.finishList;

/**
 * Created by ㅇㅇ on 2017-01-19.
 */

public class PhotosActivity extends Activity {
    private RecyclerView rv_photos;
    private int family_id;
    private String family_name;
    private String family_content;
    private String family_avatar;
    private int family_user_id;
    private String family_created_at;

    private ArrayList<Photo> photoList;

    private int user_id;
    private String user_email;
    private String user_name;







    private String user_birth;
    private String user_phone;
    private int user_level;
    private String user_avatar;
    private String access_token;

    // Handler
    private MainHandler mainHandler;

    private ProgressDialog progressDialog;
    private Message msg;
    private FamilyServerConnection familyServerConnection;

    private static final Logger logger = LoggerFactory.getLogger(PhotosActivity.class);
    private SharedPreferences loginInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);

        //family info
        family_id = getIntent().getIntExtra("family_id", 0);
        family_name = getIntent().getStringExtra("family_name");
        family_content = getIntent().getStringExtra("family_content");
        family_avatar = getIntent().getStringExtra("family_avatar");
        family_user_id = getIntent().getIntExtra("family_user_id", 0);
        family_created_at = getIntent().getStringExtra("family_created_at");

        setUserInfo();

        finishList.add(this);
        getPhotoData();
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

        setToolbar(this.getWindow().getDecorView(), this.getApplicationContext(), family_name + " 사진첩");
    }

    // toolbar & menu
    public void setToolbar(final View view, Context context, String title) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolBar);
        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText(title);
        ImageView toolbar_back = (ImageView) toolbar.findViewById(R.id.toolbar_back);
        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBack();
            }
        });
    }

    public void setBack() {
        finish();
    }

    private void getPhotoData() {
        rv_photos = (RecyclerView) findViewById(R.id.rv_photos);

        mainHandler = new MainHandler();
        progressDialog = new ProgressDialog(PhotosActivity.this);
        progressDialog.show();
        progressDialog.getWindow().setBackgroundDrawable(new
                ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.progress_dialog);

        new Thread(new Runnable() {
            @Override
            public void run() {
                familyServerConnection = new HeaderInterceptor(access_token).getClientForFamilyServer().create(FamilyServerConnection.class);

                Call<ArrayList<Photo>> call_photo = familyServerConnection.family_photo_List(family_id);
                call_photo.enqueue(new Callback<ArrayList<Photo>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Photo>> call, Response<ArrayList<Photo>> response) {
                        if(response.isSuccessful()) {
                            photoList = response.body();

                            if (photoList.size() == 0) {
                                //사진이 없습니다.
                            } else {
                                msg = new Message();
                                mainHandler.sendMessage(msg);
                            }
                        } else {
                            Toast.makeText(PhotosActivity.this, new ErrorUtil(getClass()).parseError(response).message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Photo>> call, Throwable t) {
                        log(t);
                        Toast.makeText(PhotosActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                    }
                });

                mainHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            progressDialog.dismiss();
                        } catch (Exception e) {
                            log(e);
                        }
                    }
                }, 200);
            }
        }).start();

    }

    private class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_photo;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            iv_photo = (ImageView) itemView.findViewById(R.id.iv_item_photo);

            iv_photo.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PhotosActivity.this, PhotoPopupActivity.class);
//                    intent.putExtra("photo_id", photoList.get(getAdapterPosition()).getId());
//                    intent.putExtra("story_id", photoList.get(getAdapterPosition()).getStory_id());
//                    intent.putExtra("photo_type", photoList.get(getAdapterPosition()).getType());
//                    intent.putExtra("photo_name", photoList.get(getAdapterPosition()).getName());
//                    intent.putExtra("photo_ext", photoList.get(getAdapterPosition()).getExt());
                    intent.putExtra("from", "PhotosActivity");
                    intent.putExtra("photoList", photoList);
                    intent.putExtra("photo_position", getAdapterPosition());
                    startActivity(intent);
                }
            });
        }
    }

    private class PhotoViewAdapter extends RecyclerView.Adapter<PhotoViewHolder> {
        private Context context;
        private ArrayList<Photo> photoList;
        private int layout;

        public PhotoViewAdapter(Context context, ArrayList<Photo> photoList, int layout) {
            this.context = context;
            this.photoList = photoList;
            this.layout = layout;
        }

        @Override
        public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            PhotoViewHolder holder = new PhotoViewHolder(LayoutInflater.from(context).inflate(layout, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(PhotoViewHolder holder, int position) {
            Glide.with(context).load(getString(R.string.cloud_front_stories_images) + photoList.get(position).getName() + "." + photoList.get(position).getExt()).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(
                    holder.iv_photo);
        }

        @Override
        public int getItemCount() {
            return photoList.size();
        }

    }

    //추후검토
    private class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.bottom = space;
            outRect.right = space;
        }
    }

    class MainHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            rv_photos.setAdapter(new PhotoViewAdapter(PhotosActivity.this, photoList, R.layout.item_photo));
            rv_photos.setLayoutManager(new GridLayoutManager(PhotosActivity.this, 3));

            int spacing = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin) / 2;
            rv_photos.addItemDecoration(new SpaceItemDecoration(spacing));
            rv_photos.setHasFixedSize(true);
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


