package com.demand.well_family.well_family.dialog.popup.photo.async;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by ㅇㅇ on 2017-04-19.
 */

public class ImageDownloadAsyncTask extends AsyncTask<String, Void, Void> {
    private String fileName;
    private Context context;

    public ImageDownloadAsyncTask(Context context, String fileName) {
        this.context = context;
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
            Log.e("photoPopup", e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Toast.makeText(context, "다운로드가 완료되었습니다.", Toast.LENGTH_SHORT).show();

        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment.getExternalStorageDirectory() + "/" + fileName)));
    }
}
