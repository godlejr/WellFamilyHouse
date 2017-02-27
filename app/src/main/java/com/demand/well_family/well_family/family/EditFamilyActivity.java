package com.demand.well_family.well_family.family;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.Family;
import com.demand.well_family.well_family.dto.Identification;
import com.demand.well_family.well_family.log.LogFlag;
import com.demand.well_family.well_family.util.RealPathUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ㅇㅇ on 2017-02-27.
 */

public class EditFamilyActivity extends Activity {
    private EditText et_create_family_introduce;
    private EditText et_create_family_name;
    private ImageView iv_create_family_img;
    private ImageButton ib_create_family;
    private Uri family_photo_uri;
    private String path;
    private RealPathUtil realPathUtil;
    private static final Logger logger = LoggerFactory.getLogger(EditFamilyActivity.class);

    // family_info
    private int family_id;
    private String family_content;
    private String family_avatar;
    private String family_name;
    private int family_user_id;
    private String family_created_at;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_create_family);
        getWindow().setLayout(android.view.WindowManager.LayoutParams.MATCH_PARENT, android.view.WindowManager.LayoutParams.MATCH_PARENT);

        init();
        setToolbar(getWindow().getDecorView());
    }

    private void init(){
        family_id = getIntent().getIntExtra("family_id", 0);
        family_name = getIntent().getStringExtra("family_name");
        family_content = getIntent().getStringExtra("family_content");
        family_avatar = getIntent().getStringExtra("family_avatar");
        family_user_id = getIntent().getIntExtra("family_user_id", 0);
        family_created_at = getIntent().getStringExtra("family_created_at");

        et_create_family_introduce = (EditText) findViewById(R.id.et_create_family_introduce);
        et_create_family_name = (EditText) findViewById(R.id.et_create_family_name);
        iv_create_family_img = (ImageView) findViewById(R.id.iv_create_family_img);
        ib_create_family = (ImageButton)findViewById(R.id.ib_create_family);

        Glide.with(this).load(getString(R.string.cloud_front_family_avatar) +  family_avatar).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_create_family_img);
        et_create_family_introduce.setText(family_content);
        et_create_family_name.setText(family_name);

        realPathUtil = new RealPathUtil();
        ib_create_family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, 7777);
            }
        });
    }


    public void setToolbar(View view) {
        // toolbar_main
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolBar);
        ImageView toolbar_back = (ImageView) toolbar.findViewById(R.id.toolbar_back);
        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();           }
        });

        TextView toolbar_title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText("가족 수정");

        Button toolbar_complete = (Button) toolbar.findViewById(R.id.toolbar_complete);
        toolbar_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 7777) {
                if (data != null) {
                    family_photo_uri = data.getData();
                    try {
                        path = realPathUtil.getRealPathFromURI_API19(this, family_photo_uri);
                    } catch (Exception e) {
                        log(e);
                        path = realPathUtil.getRealPathFromURI_API11to18(this, family_photo_uri);
                    }
                    Glide.with(EditFamilyActivity.this).load(family_photo_uri).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_create_family_img);
                } else {
                    Log.e("갤러리 이미지 선택 오류", "");
                }
            }
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
