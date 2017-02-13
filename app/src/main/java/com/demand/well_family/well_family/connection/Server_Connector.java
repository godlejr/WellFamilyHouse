package com.demand.well_family.well_family.connection;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Human on 2016-03-15.
 */
public class Server_Connector extends AsyncTask<String, Integer, String> {
    ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }


    @Override
    protected String doInBackground(String... urls) {

        StringBuilder json = new StringBuilder();

        try {
            String postURL = new URL(urls[0]).toString();
            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost(postURL);
            UrlEncodedFormEntity entity;

            entity = new UrlEncodedFormEntity(params, "UTF-8");
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

    public void addVariable(String name, String value) {
        params.add(new BasicNameValuePair(name, value));
    }


}
