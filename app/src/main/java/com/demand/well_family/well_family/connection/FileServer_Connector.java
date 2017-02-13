package com.demand.well_family.well_family.connection;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by DongJoo on 2017-01-27.
 */

public class FileServer_Connector extends AsyncTask<String, Integer, String> {
    private String base64;

    @Override
    protected String doInBackground(String... urls) {
        StringBuilder json = new StringBuilder();

        try {
            String postURL = new URL(urls[0]).toString();
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(postURL);

            StringEntity entity = new StringEntity(base64);
            post.setEntity(entity);

            HttpResponse responsePOST = client.execute(post);
            HttpEntity resEntity = responsePOST.getEntity();

            if (resEntity != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(resEntity.getContent(), "UTF-8"));
                for (; ; ) {

                    String line = br.readLine();
                    Log.d("line", "line  : " + line);
                    if (line == null)
                        break;

                    json.append(line + "\n");
                }
                br.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return json.toString();

    }

    public void addBase64Bitmap(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] b = baos.toByteArray();
        base64 = Base64.encodeToString(b, Base64.NO_WRAP | Base64.URL_SAFE);
    }

    public void addBase64Audio(File file){
        byte[] bytes = convertFileToByteArray(file);
        base64 = Base64.encodeToString(bytes, 0);
    }

    public static byte[] convertFileToByteArray(File file)
    {
        byte[] byteArray = null;
        try
        {
            InputStream inputStream = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024*8];
            int bytesRead =0;

            while ((bytesRead = inputStream.read(b)) != -1)
            {
                bos.write(b, 0, bytesRead);
            }

            byteArray = bos.toByteArray();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return byteArray;
    }
}
