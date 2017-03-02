package com.demand.well_family.well_family.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.connection.Server_Connection;
import com.demand.well_family.well_family.log.LogFlag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dev-0 on 2017-03-02.
 */

public class CommentDeleteActivity extends Activity{
    private int comment_id;

    private TextView tv_comment_delete_confirm;
    private TextView tv_comment_delete_cancel;
    private Server_Connection server_connection;

    private static final Logger logger = LoggerFactory.getLogger(CommentDeleteActivity.class);
    private int act_flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_comment_delete);
        getWindow().setLayout(android.view.WindowManager.LayoutParams.WRAP_CONTENT, android.view.WindowManager.LayoutParams.WRAP_CONTENT);
        
        init();
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
        super.onBackPressed();
    }

    private void init() {
        act_flag = getIntent().getIntExtra("act_flag", 0);

        comment_id = getIntent().getIntExtra("comment_id",0);

        tv_comment_delete_confirm =  (TextView)findViewById(R.id.tv_comment_delete_confirm);
        tv_comment_delete_cancel = (TextView)findViewById(R.id.tv_comment_delete_cancel);

        tv_comment_delete_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });

        tv_comment_delete_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                server_connection = Server_Connection.retrofit.create(Server_Connection.class);
                HashMap<String, String> map = new HashMap<>();
                map.put("comment_id", String.valueOf(comment_id));
                map.put("flag",String.valueOf(act_flag));
                Call<ResponseBody> call_delete_comment = server_connection.delete_comment(map);
                call_delete_comment.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        //scss
                        Toast.makeText(CommentDeleteActivity.this, "댓글이 삭제되었습니다.", Toast.LENGTH_LONG).show();
                        Intent intent = getIntent();
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        log(t);
                        Toast.makeText(CommentDeleteActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
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
