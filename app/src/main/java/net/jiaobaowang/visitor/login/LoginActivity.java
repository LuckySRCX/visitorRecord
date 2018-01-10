package net.jiaobaowang.visitor.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import net.jiaobaowang.visitor.R;
import net.jiaobaowang.visitor.common.VisitorConfig;
import net.jiaobaowang.visitor.entity.LoginResult;
import net.jiaobaowang.visitor.manage.ManageActivity;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    OkHttpClient okHttpClient = new OkHttpClient();
    String mUserName;
    String mPassword;
    MyHandler mHandler = new MyHandler(LoginActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserName = ((EditText) findViewById(R.id.login_userName)).getText().toString();
                mPassword = ((EditText) findViewById(R.id.login_password)).getText().toString();
                if (mUserName.equals("")) {
                    Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_LONG).show();
                    return;
                }
                if (mPassword.equals("")) {
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_LONG).show();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            RequestBody body = new FormBody.Builder().add("username", mUserName).add("password", mPassword).build();
                            Request request = new Request.Builder().url(VisitorConfig.VISITOR_GET_TOKEN).post(body).build();
                            Response response = okHttpClient.newCall(request).execute();
                            if (!response.isSuccessful()) {
                                throw new IOException("unexpected code" + response);
                            } else {
//                                Log.e(TAG, response.body().string());
                                resultDealt(response.body().string());
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "获取token错误", e);
                        }
                    }
                }).start();
            }
        });

    }

    /**
     * MyHandler
     */
    private static class MyHandler extends Handler {
        private Context mContext;
        MyHandler(Context context){
            mContext=context;
        }
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "获取的信息为："+String.valueOf(msg.what));
            switch (msg.what) {
                case 0:
                   showToast();
                    break;
                case 1:
                    break;
                default:
                    break;
            }
        }
        void showToast(){
            Toast.makeText(mContext,"用户名或密码错误！",Toast.LENGTH_LONG).show();
        }
    }

    /**
     *
     * @param result 整理结果
     */
    private void resultDealt(String result) {
        Gson gson = new Gson();
        LoginResult result1 = gson.fromJson(result, LoginResult.class);
        if (result1.getCode().equals("0000")) {
            Intent intent = new Intent();
            intent.setClass(this, ManageActivity.class);
            startActivity(intent);
        } else {
            mHandler.sendEmptyMessage(0);
//
        }
    }

}

