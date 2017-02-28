package com.demand.well_family.well_family.intro;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.demand.well_family.well_family.LoginActivity;
import com.demand.well_family.well_family.MainActivity;
import com.demand.well_family.well_family.R;
import com.demand.well_family.well_family.connection.Server_Connection;
import com.demand.well_family.well_family.dto.Check;
import com.demand.well_family.well_family.log.LogFlag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ㅇㅇ on 2017-01-25.
 */

public class IntroActivity extends Activity {
    private SharedPreferences loginInfo;
    private String email, name, birth, avatar, phone;
    private int id, level;
    private String device_id,token;

    private Server_Connection server_connection;

    private static final Logger logger = LoggerFactory.getLogger(IntroActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loginInfo = getSharedPreferences("loginInfo", Activity.MODE_PRIVATE);
                id = loginInfo.getInt("user_id", 0);
                level = loginInfo.getInt("user_level", 0);
                token = loginInfo.getString("token", null);
                device_id = loginInfo.getString("device_id", null);

                if (id != 0) {
                    server_connection = Server_Connection.retrofit.create(Server_Connection.class);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("device_id", device_id);

                    Call<ArrayList<Check>> call_device_check = server_connection.check_device_id(id, map);

                    call_device_check.enqueue(new Callback<ArrayList<Check>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Check>> call, Response<ArrayList<Check>> response) {

                            if (response.body().get(0).getChecked() > 0){
                                Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(IntroActivity.this,"다른 기기에서 접속중입니다. 로그인 시 다른기기에서 강제 로그아웃 됩니다.",Toast.LENGTH_LONG);
                                SharedPreferences.Editor editor = loginInfo.edit();
                                editor.remove("user_id");
                                editor.remove("user_name");
                                editor.remove("user_email");
                                editor.remove("user_birth");
                                editor.remove("user_avatar");
                                editor.remove("user_phone");
                                editor.remove("user_level");
                                editor.remove("device_id");
                                editor.commit();

                                Intent mainIntent = new Intent(IntroActivity.this, LoginActivity.class);
                                IntroActivity.this.startActivity(mainIntent);
                                IntroActivity.this.finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Check>> call, Throwable t) {
                            log(t);
                            Toast.makeText(IntroActivity.this, "네트워크 불안정합니다. 다시 시도하세요.", Toast.LENGTH_LONG).show();
                        }
                    });

                }else {
                    Intent mainIntent = new Intent(IntroActivity.this, LoginActivity.class);
                    IntroActivity.this.startActivity(mainIntent);
                    IntroActivity.this.finish();
                }
            }
        }, 2000);
    }

    private static void log(Throwable throwable){
        StackTraceElement[] ste =  throwable.getStackTrace();
        String className = ste[0].getClassName();
        String methodName = ste[0].getMethodName();
        int lineNumber = ste[0].getLineNumber();
        String fileName = ste[0].getFileName();

        if(LogFlag.printFlag){
            if(logger.isInfoEnabled()){
                logger.info("Exception: " + throwable.getMessage());
                logger.info(className + "."+ methodName+" "+ fileName +" "+ lineNumber +" "+ "line" );
            }
        }
    }
}
