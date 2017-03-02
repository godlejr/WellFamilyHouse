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
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.dto.Photo;
import com.demand.well_family.well_family.dto.SongPhoto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ㅇㅇ on 2017-01-23.
 */

public class SongPhotoPopupActivity extends Activity {
    private LinearLayout ll_popup_top;
    private ImageView iv_popup_image, iv_popup_close, iv_popup_download;
    private String imageURL;
    private final int WRITE_EXTERNAL_STORAGE_PERMISSION = 999;
    private ViewPager photo_viewPager;
    private ArrayList<SongPhoto> photoList;
    private int photoListSize;
    private int current_photo_position;

    //photo info
    private int photo_id;
    private String photo_name;
    private int song_story_id;
    private int photo_type;
    private String photo_ext;

    //download
    private ImageDownload imageDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_photo);

//        photo_id = getIntent().getIntExtra("photo_id", 0);
//        song_story_id = getIntent().getIntExtra("story_id", 0);
//        photo_name = getIntent().getStringExtra("photo_name");
//        photo_type = getIntent().getIntExtra("photo_type", 0);
//        photo_ext = getIntent().getStringExtra("photo_ext");
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

        iv_popup_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SongPhotoPopupActivity.this.finish();
            }
        });

        photoList = (ArrayList<SongPhoto>) getIntent().getSerializableExtra("photoList");
        photoListSize = photoList.size();
        current_photo_position = getIntent().getIntExtra("photo_position", 0);

        photo_viewPager = (ViewPager) findViewById(R.id.photo_viewPager);
        photo_viewPager.setAdapter(new ViewPageAdapter(getLayoutInflater()));
        photo_viewPager.setCurrentItem(current_photo_position);
    }

    private class ImageDownload extends AsyncTask<String, Void, Void> {
        private String fileName;

        public ImageDownload(String fileName) {
            this.fileName = fileName;
        }

        @Override
        protected Void doInBackground(String... params) {
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
            Toast.makeText(SongPhotoPopupActivity.this, "다운로드가 완료되었습니다.", Toast.LENGTH_SHORT).show();
            getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment.getExternalStorageDirectory() + "/" + fileName)));
        }
    }

    private class ViewPageAdapter extends PagerAdapter {
        LayoutInflater inflater;

        public ViewPageAdapter(LayoutInflater inflater) {
            this.inflater = inflater;
        }

        @Override
        public int getCount() {
            return photoList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = inflater.inflate(R.layout.viewpager_childview, null);
            ImageView iv_viewPager_childView = (ImageView) view.findViewById(R.id.iv_viewPager_childView);
            TextView tv_viewPager_position = (TextView) view.findViewById(R.id.tv_viewPager_position);

            iv_viewPager_childView.setOnTouchListener(new View.OnTouchListener() {
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

            imageURL = getString(R.string.cloud_front_song_stories_images) + photoList.get(position).getName() + "." + photoList.get(position).getExt();
            Glide.with(SongPhotoPopupActivity.this).load(imageURL).thumbnail(0.5f).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(iv_viewPager_childView);

            String viewPager_position = (position + 1) + " / " + photoListSize;
            tv_viewPager_position.setText(viewPager_position);

            container.addView(view);

            iv_popup_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int viewpagerCurrentItem = photo_viewPager.getCurrentItem();
                    imageURL = getString(R.string.cloud_front_song_stories_images) + photoList.get(viewpagerCurrentItem).getName() + "." + photoList.get(viewpagerCurrentItem).getExt();
                    imageDownload = new ImageDownload(photoList.get(viewpagerCurrentItem).getName() + "." + photoList.get(viewpagerCurrentItem).getExt());
                    imageDownload.execute(imageURL);
                }
            });


            return view;
        }

        @Override
        public boolean isViewFromObject(View v, Object obj) {
            return v == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

}
