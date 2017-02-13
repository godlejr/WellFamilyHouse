package com.demand.well_family.well_family.photos;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ㅇㅇ on 2017-01-23.
 */

public class PhotoPopupActivity extends Activity {
    private LinearLayout ll_popup_top;
    private ImageView iv_popup_image, iv_popup_close, iv_popup_download;
    private String imageURL;
    private final int WRITE_EXTERNAL_STORAGE_PERMISSION = 999;

    //photo info
    private int photo_id;
    private String photo_name;
    private int story_id;
    private int photo_type;
    private String photo_ext;

    //download
    private ImageDownload imageDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_photo);
        getWindow().setLayout(android.view.WindowManager.LayoutParams.MATCH_PARENT, android.view.WindowManager.LayoutParams.MATCH_PARENT);

        photo_id = getIntent().getIntExtra("photo_id",0);
        story_id = getIntent().getIntExtra("story_id",0);
        photo_name = getIntent().getStringExtra("photo_name");
        photo_type = getIntent().getIntExtra("photo_type",0);
        photo_ext = getIntent().getStringExtra("photo_ext");
        checkPermission();
        init();
    }

    private void checkPermission() {
        int writePermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_PERMISSION);

        if (writePermission == PackageManager.PERMISSION_DENIED) {
            Log.e("WRITE PERMISSION", "권한X");

        } else {
            Log.e("WRITE PERMISSION", "권한O");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == WRITE_EXTERNAL_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "권한을 허가해주세요.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void init() {
        ll_popup_top = (LinearLayout) findViewById(R.id.ll_popup_top);
        ll_popup_top.bringToFront();
        ll_popup_top.invalidate();

        iv_popup_close = (ImageView) findViewById(R.id.iv_popup_close);
        iv_popup_image = (ImageView) findViewById(R.id.iv_popup_image);
        iv_popup_download = (ImageView) findViewById(R.id.iv_popup_download);

        imageURL = getString(R.string.cloud_front_stories_images) + photo_name + "." + photo_ext;

        Glide.with(this).load(imageURL).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_popup_image);


        iv_popup_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoPopupActivity.this.finish();
            }
        });

        iv_popup_image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (ll_popup_top.getVisibility() == View.GONE) {
                        ll_popup_top.setVisibility(View.VISIBLE);
                    } else {
                        ll_popup_top.setVisibility(View.GONE);
                    }
                }

                return true;
            }
        });

        iv_popup_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageDownload = new ImageDownload(photo_name + "." + photo_ext);
                imageDownload.execute(imageURL);
            }
        });

    }

    private class ImageDownload extends AsyncTask<String, Void, Void> {
        //       파일 명
        private String fileName;

        public ImageDownload(String fileName) {
            this.fileName = fileName;
        }

        @Override
        protected Void doInBackground(String... params) {
            Log.e("tt", "tt3");

            try {
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                InputStream is = conn.getInputStream();
                File file = new File(Environment.getExternalStorageDirectory().toString(), fileName);
                FileOutputStream fos = new FileOutputStream(file);

                int len = conn.getContentLength();
                byte[] tmpByte = new byte[len];
                int read;

                for (; ; ) {
                    read = is.read(tmpByte);
                    if (read <= 0) {
                        break;
                    }

                    fos.write(tmpByte, 0, read);
                }

                is.close();
                fos.close();
                conn.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(PhotoPopupActivity.this, "다운로드가 완료되었습니다.", Toast.LENGTH_SHORT).show();
            getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment.getExternalStorageDirectory() + "/" + fileName)));
        }
    }
}
