package com.demand.well_family.well_family.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class CommentModifyActivity extends Activity {
    private EditText et_comment_modify;

    private int comment_id;
    private String comment_content;
    private Server_Connection server_connection;

    private static final Logger logger = LoggerFactory.getLogger(CommentModifyActivity.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_modify);

        setToolbar(getWindow().getDecorView());
        init();

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
        toolbar_title.setText("댓글 수정");

        Button toolbar_complete = (Button) toolbar.findViewById(R.id.toolbar_complete);

        toolbar_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String content = et_comment_modify.getText().toString();

                if (comment_content.equals(content)) {
                    finish();
                } else {
                    server_connection = Server_Connection.retrofit.create(Server_Connection.class);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("content",content );
                    Call<ResponseBody> call_update_comment = server_connection.update_comment(comment_id, map);
                    call_update_comment.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            //scss
                            Toast.makeText(CommentModifyActivity.this, "댓글이 수정되었습니다.", Toast.LENGTH_LONG).show();
                            Intent intent = getIntent();
                            intent.putExtra("content",content);
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            log(t);
                            Toast.makeText(CommentModifyActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    private void init() {
        comment_id = getIntent().getIntExtra("comment_id", 0);
        comment_content = getIntent().getStringExtra("comment_content");

        et_comment_modify = (EditText) findViewById(R.id.et_comment_modify);
        et_comment_modify.setText(comment_content);

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
