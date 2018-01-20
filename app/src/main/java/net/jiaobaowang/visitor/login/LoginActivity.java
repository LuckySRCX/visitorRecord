package net.jiaobaowang.visitor.login;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import net.jiaobaowang.visitor.R;
import net.jiaobaowang.visitor.base.FlagObject;
import net.jiaobaowang.visitor.common.VisitorConfig;
import net.jiaobaowang.visitor.entity.LoginResult;
import net.jiaobaowang.visitor.entity.ShakeHandData;
import net.jiaobaowang.visitor.entity.ShakeHandResult;
import net.jiaobaowang.visitor.utils.EncryptUtil;
import net.jiaobaowang.visitor.utils.Tools;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.TreeMap;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_FLAG_SHAKEHAND = 0;
    private static final int REQUEST_FLAG_LOGIN = 1;
    OkHttpClient okHttpClient = new OkHttpClient();
    String mUserName;
    String mPassword;
    MyHandler mHandler = new MyHandler(LoginActivity.this);
    private int mSchoolId;
    ShakeHandData shakeHandData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoginTask(REQUEST_FLAG_SHAKEHAND).execute();
                mUserName = ((EditText) findViewById(R.id.login_userName)).getText().toString();
                mPassword = ((EditText) findViewById(R.id.login_password)).getText().toString();
                mSchoolId = Tools.getSchoolId(LoginActivity.this);
                if (mSchoolId == -1) {
                    Toast.makeText(LoginActivity.this, "请设置学校id", Toast.LENGTH_LONG).show();
                    showSetDialog();
                    return;
                }
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
                            RequestBody body = new FormBody.Builder().add("username", mUserName).add("password", mPassword)
                                    .add("school_id", mSchoolId + "").build();
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
        findViewById(R.id.menu_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSetDialog();
            }
        });
    }

    private ShakeHandData shakeHand() {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("uuid", Tools.getDeviceId(LoginActivity.this));
        map.put("shaketype", "login");
        map.put("appid", Tools.getAppId(LoginActivity.this));
        RequestBody body = null;
        try {
            String a = Tools.getSign(map);
            map.put("sign", a);
            Gson gson = new Gson();
            String json = gson.toJson(map);
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            body = RequestBody.create(JSON, json);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Request request = new Request.Builder().url("https://jsypay.jiaobaowang.net/useradminwebapi/api/data/ShakeHand").post(body).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String result = response.body().string();
                Log.d(result, "");
                Gson gson = new Gson();
                ShakeHandResult result1 = gson.fromJson(result, ShakeHandResult.class);
                Log.d("这事对的吗", result);
                result1.getRspData().setFlag(REQUEST_FLAG_SHAKEHAND);
                return result1.getRspData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private FlagObject schoolLogin(ShakeHandData shakeHandData) throws Exception {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("uuid", Tools.getDeviceId(LoginActivity.this));
        map.put("shaketype", "login");
        map.put("appid", Tools.getAppId(LoginActivity.this));
        map.put("schid", 100005 + "");
        map.put("utp", "0");
        String uid=EncryptUtil.encryptByPublicKey("ceshi01", EncryptUtil.getPublicKey(shakeHandData.getModulus(), shakeHandData.getExponent()));
        map.put("uid",uid);
        String pw=EncryptUtil.encryptByPublicKey("123456", EncryptUtil.getPublicKey(shakeHandData.getModulus(), shakeHandData.getExponent()));
        map.put("pw",pw);
        map.put("sign", Tools.getSign(map));
        RequestBody body = null;

        Gson gson = new Gson();
        String json = gson.toJson(map);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url("https://jsypay.jiaobaowang.net/useradminwebapi/api/data/Login").post(body).build();
        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            String s = response.body().string();
            Log.d(TAG, s);
            return new FlagObject() {
            };
        }
        return new FlagObject() {
        };
    }


    private void showSetDialog() {
        final EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        new AlertDialog.Builder(LoginActivity.this)
                .setTitle("设置学校ID")
                .setView(editText)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (editText.getText().toString().equals("")) {
                            Toast.makeText(LoginActivity.this, "请输入学校ID", Toast.LENGTH_LONG).show();
                            return;
                        }
                        SharedPreferences preferences = getSharedPreferences(VisitorConfig.VISIT_LOCAL_STORAGE, MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt(VisitorConfig.VISIT_LOCAL_SCHOOL_ID, Integer.valueOf(editText.getText().toString()));
                        editor.apply();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }

    /**
     * MyHandler
     */
    private static class MyHandler extends Handler {
        private Context mContext;

        MyHandler(Context context) {
            mContext = context;
        }

        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "获取的信息为：" + String.valueOf(msg.what));
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

        void showToast() {
            Toast.makeText(mContext, "用户名或密码错误！", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * @param result 整理结果
     */
    private void resultDealt(String result) {
        Gson gson = new Gson();
        LoginResult result1 = gson.fromJson(result, LoginResult.class);
        if (result1.getCode().equals("0000")) {
            SharedPreferences preferences = getSharedPreferences(VisitorConfig.VISIT_LOCAL_STORAGE, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(VisitorConfig.VISIT_LOCAL_TOKEN, result1.getToken());
            editor.apply();
            Intent intent = new Intent();
            intent.setClass(this, HomeActivity.class);
            startActivity(intent);
        } else {
            mHandler.sendEmptyMessage(0);
        }
    }

    class LoginTask extends AsyncTask<Void, Void, FlagObject> {
        private int flag;

        public LoginTask(int flag) {
            super();
            this.flag = flag;
        }

        @Override
        protected void onPostExecute(FlagObject flagObject) {
            super.onPostExecute(flagObject);
            Log.d(TAG, "omPostExecute");
            switch (flagObject.getFlag()) {
                case REQUEST_FLAG_SHAKEHAND:
                    shakeHandData = (ShakeHandData) flagObject;
                    new LoginTask(REQUEST_FLAG_LOGIN).execute();
                    break;
                case REQUEST_FLAG_LOGIN:

                    break;
                default:
                    break;

            }
        }

        @Override
        protected FlagObject doInBackground(Void... voids) {
            switch (flag) {
                case REQUEST_FLAG_SHAKEHAND:
                    return shakeHand();
                case REQUEST_FLAG_LOGIN:
                    try {
                        return schoolLogin(shakeHandData);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                default:
                    return null;
            }
        }
    }

}

