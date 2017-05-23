package com.demand.well_family.well_family.story.edit.async;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.demand.well_family.well_family.flag.LogFlag;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.URL;


/**
 * Created by Dev-0 on 2017-05-23.
 */

public class UrlToBitmapAsyncTask extends AsyncTask<URL, Void, Bitmap> {
    private Bitmap bm = null;
    private HttpGet httpRequest = null;
    private static final Logger logger = LoggerFactory.getLogger(UrlToBitmapAsyncTask.class);

    @Override
    protected Bitmap doInBackground(URL... urls) {
        try {
            httpRequest = new HttpGet(urls[0].toURI());
            HttpClient httpclient = new DefaultHttpClient();

            HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);
            HttpEntity entity = response.getEntity();

            BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
            InputStream instream = bufHttpEntity.getContent();

            bm = BitmapFactory.decodeStream(instream);
        } catch (Exception e) {

        }
        return bm;
    }

    private static void log(Throwable throwable) {
        StackTraceElement[] ste = throwable.getStackTrace();
        String className = ste[0].getClassName();
        String methodName = ste[0].getMethodName();
        int lineNumber = ste[0].getLineNumber();
        String fileName = ste[0].getFileName();

        if (LogFlag.printFlag) {
            if (logger.isInfoEnabled()) {
                logger.error("Exception: " + throwable.getMessage());
                logger.error(className + "." + methodName + " " + fileName + " " + lineNumber + " " + "line");
            }
        }
    }
}
