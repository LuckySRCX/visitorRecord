package net.jiaobaowang.visitor.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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
import net.jiaobaowang.visitor.entity.SchInfoResult;
import net.jiaobaowang.visitor.entity.SchoolLoginResult;
import net.jiaobaowang.visitor.entity.ShakeHandData;
import net.jiaobaowang.visitor.entity.ShakeHandResult;
import net.jiaobaowang.visitor.utils.SharePreferencesUtil;
import net.jiaobaowang.visitor.utils.Tools;

import java.net.UnknownHostException;
import java.util.TreeMap;
import java.util.UUID;

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
    private static final int REQUEST_FLAG_LOGIN_QX = 2;
    public static final String EXTRA_DATA = "net.jiaobaowang.visitor.LoginActivity.extra_data";
    OkHttpClient okHttpClient = new OkHttpClient();
    String mUserName;
    String mPassword;
    private int mSchoolId;
    ShakeHandResult mShakeHandResult;
    SchInfoResult schInfoResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null && getIntent().getBooleanExtra(EXTRA_DATA, false)) {
            finish();
        }
        setContentView(R.layout.activity_login);

        SharePreferencesUtil util = new SharePreferencesUtil(LoginActivity.this, VisitorConfig.VISIT_LOCAL_STORAGE);
        util.putString(VisitorConfig.VISIT_LOCAL_STORAGE_UUID, UUID.randomUUID().toString().toUpperCase());
        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                new LoginTask(LoginActivity.this, REQUEST_FLAG_SHAKEHAND).execute();

            }
        });
        findViewById(R.id.menu_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSetDialog();
            }
        });
    }

    private ShakeHandResult shakeHand() {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("uuid", Tools.getDeviceId(LoginActivity.this));
        map.put("shaketype", "login");
        map.put("appid", Tools.getAppId(LoginActivity.this));
        RequestBody body;
        try {
            String a = Tools.getSign(map);
            map.put("sign", a);
            Gson gson = new Gson();
            String json = gson.toJson(map);
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            body = RequestBody.create(JSON, json);
            Request request = new Request.Builder().url(VisitorConfig.VISIT_SCHOOL_SHAKEHAND).post(body).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String result = response.body().string();
                Log.d(result, "");
                ShakeHandResult result1 = gson.fromJson(result, ShakeHandResult.class);
                Log.d("这事对的吗", result);
                result1.setFlag(REQUEST_FLAG_SHAKEHAND);
                return result1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof UnknownHostException) {
                ShakeHandResult result = new ShakeHandResult();
                result.setRspCode(String.valueOf(1099));
                result.setRspTxt("网络连接失败，请检查网络");
                result.setFlag(REQUEST_FLAG_SHAKEHAND);
                return result;
            }
        }
        ShakeHandResult result = new ShakeHandResult();
        result.setRspCode(String.valueOf(1099));
        result.setRspTxt("未知错误！");
        result.setFlag(REQUEST_FLAG_SHAKEHAND);
        return result;
    }

    private SchoolLoginResult schoolLogin(ShakeHandData shakeHandData) {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("uuid", Tools.getDeviceId(LoginActivity.this));
        map.put("shaketype", "login");
        map.put("appid", Tools.getAppId(LoginActivity.this));
        map.put("schid", String.valueOf(mSchoolId));
        map.put("utp", "0");
        String uid = null;
        try {
            uid = Tools.RSAEncrypt(mUserName, shakeHandData);
            map.put("uid", uid);
            String pw = Tools.RSAEncrypt(mPassword, shakeHandData);
            map.put("pw", pw);
            map.put("sign", Tools.getSign(map));
            Gson gson = new Gson();
            String json = gson.toJson(map);
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder().url(VisitorConfig.VISIT_SCHOOL_LOGIN).post(body).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String s = response.body().string();
                Log.i(TAG, s);
                SharePreferencesUtil preferencesUtil = new SharePreferencesUtil(LoginActivity.this, VisitorConfig.VISIT_LOCAL_STORAGE);
                preferencesUtil.putString(VisitorConfig.VISIT_LOCAL_USERINFO, s);
                SchoolLoginResult result = gson.fromJson(s, SchoolLoginResult.class);
                result.setFlag(REQUEST_FLAG_LOGIN);
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof UnknownHostException) {
                SchoolLoginResult result = new SchoolLoginResult();
                result.setRspCode(String.valueOf(1099));
                result.setRspTxt("网络连接失败，请检查网络");
                result.setFlag(REQUEST_FLAG_LOGIN);
                return result;
            }
        }
        SchoolLoginResult result = new SchoolLoginResult();
        result.setRspCode(String.valueOf(1099));
        result.setRspTxt("未知错误！");
        result.setFlag(REQUEST_FLAG_LOGIN);
        return result;
    }

    private SchInfoResult getQx() {
        TreeMap<String, String> map = new TreeMap<>();
        map.put("uuid", Tools.getDeviceId(LoginActivity.this));
        map.put("appid", Tools.getAppId(LoginActivity.this));
//        String uid = null;
        try {
//            uid = Tools.RSAEncrypt(mUserName, shakeHandData);
//            map.put("uid", uid);
            SharedPreferences sp = LoginActivity.this.getSharedPreferences(VisitorConfig.VISIT_LOCAL_STORAGE, MODE_PRIVATE);
            String utoken = sp.getString(VisitorConfig.VISIT_LOCAL_TOKEN, "");
            map.put("utoken", utoken);
            map.put("sign", Tools.getSign(map));
            Gson gson = new Gson();
            String json = gson.toJson(map);
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder().url(VisitorConfig.VISIT_SCHOOL_LOGIN_QX).post(body).build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String s = response.body().string();
                Log.i(TAG, s);
                SchInfoResult result = gson.fromJson(s, SchInfoResult.class);
                result.setFlag(REQUEST_FLAG_LOGIN_QX);
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof UnknownHostException) {
                SchInfoResult result = new SchInfoResult();
                result.setRspCode(String.valueOf(1099));
                result.setRspTxt("网络连接失败，请检查网络");
                result.setFlag(REQUEST_FLAG_LOGIN_QX);
                return result;
            }
        }
        SchInfoResult result = new SchInfoResult();
        result.setRspCode(String.valueOf(1099));
        result.setRspTxt("未知错误！");
        result.setFlag(REQUEST_FLAG_LOGIN_QX);
        return result;
    }



    private void showSetDialog() {
        final EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        SharePreferencesUtil preferencesUtil = new SharePreferencesUtil(LoginActivity.this, VisitorConfig.VISIT_LOCAL_STORAGE, false);
        int schoolId = preferencesUtil.getInt(VisitorConfig.VISIT_LOCAL_SCHOOL_ID);
        if (schoolId > 0) {
            editText.setText(String.valueOf(schoolId));
        }
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
                        SharePreferencesUtil util = new SharePreferencesUtil(LoginActivity.this, VisitorConfig.VISIT_LOCAL_STORAGE);
                        util.putInt(VisitorConfig.VISIT_LOCAL_SCHOOL_ID, Integer.valueOf(editText.getText().toString()));
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

    private ProgressDialog mDialog;

    class LoginTask extends AsyncTask<Void, Void, FlagObject> {
        private int flag;
        private Context mContext;

        LoginTask(Context context, int flag) {
            super();
            mContext = context;
            this.flag = flag;
            if (flag == REQUEST_FLAG_SHAKEHAND) {
                mDialog = new ProgressDialog(context);
            }

        }

        @Override
        protected void onPreExecute() {
            if (flag == REQUEST_FLAG_SHAKEHAND) {
                mDialog.setMessage("登录中...");
                mDialog.show();
            }
        }

        @Override
        protected void onPostExecute(FlagObject flagObject) {
            super.onPostExecute(flagObject);
            if (flagObject == null) {
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                return;
            }
            Log.d(TAG, "omPostExecute");
            switch (flagObject.getFlag()) {
                case REQUEST_FLAG_SHAKEHAND:
                    mShakeHandResult = (ShakeHandResult) flagObject;
                    if (mShakeHandResult.getRspCode().equals("0000")) {
                        new LoginTask(mContext, REQUEST_FLAG_LOGIN).execute();
                    } else {
                        if (mDialog.isShowing()) {
                            mDialog.dismiss();
                        }
                        Toast.makeText(mContext, mShakeHandResult.getRspTxt(), Toast.LENGTH_LONG).show();
                    }
                    break;
                case REQUEST_FLAG_LOGIN:
                    SchoolLoginResult data = (SchoolLoginResult) flagObject;
                    Log.d(TAG, "获取登录信息成功");
                    switch (data.getRspCode()) {
                        case "0000":
                            boolean hasQuery = hasPowerInPoint(10, data.getRspData().getUrolestr());
                            boolean hasSign = hasPowerInPoint(11, data.getRspData().getUrolestr());
                            if (!hasSign && !hasQuery) {
                                Toast.makeText(mContext, "此用户无系统使用权限！", Toast.LENGTH_LONG).show();
                                if (mDialog.isShowing()) {
                                    mDialog.dismiss();
                                }
                                return;
                            }
                            SharePreferencesUtil util = new SharePreferencesUtil(LoginActivity.this, VisitorConfig.VISIT_LOCAL_STORAGE);
                            util.putString(VisitorConfig.VISIT_LOCAL_TOKEN, data.getRspData().getUtoken());
                            util.putString(VisitorConfig.VISIT_LOCAL_USERINFO_UID, data.getRspData().getUid());
                            util.putInt(VisitorConfig.VISIT_LOCAL_USERINFO_UTID, data.getRspData().getUtid());
                            util.putString(VisitorConfig.VISIT_LOCAL_USERINFO_UTNAME, data.getRspData().getUtname());
                            util.putBoolean(VisitorConfig.VISIT_LOCAL_USER_QUERY, hasQuery);
                            util.putBoolean(VisitorConfig.VISIT_LOCAL_USER_SIGN, hasSign);
                            new LoginTask(LoginActivity.this, REQUEST_FLAG_LOGIN_QX).execute();
                            break;
                        case "0005":
                            if (mDialog.isShowing()) {
                                mDialog.dismiss();
                            }
                            Toast.makeText(mContext, "用户名、密码或学校id设置错误", Toast.LENGTH_LONG).show();
                            break;
                        default:
                            if (mDialog.isShowing()) {
                                mDialog.dismiss();
                            }
                            Toast.makeText(mContext, data.getRspTxt(), Toast.LENGTH_LONG).show();
                            break;
                    }
                    break;
                case REQUEST_FLAG_LOGIN_QX:
                    if (mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    schInfoResult = (SchInfoResult) flagObject;
                    if (schInfoResult.getRspCode().equals("0000")) {
                        if(schInfoResult.getRspData().getSourestat()==0){
                            Toast.makeText(mContext, "访客服务被屏蔽 不允许登录", Toast.LENGTH_LONG).show();
                        }else{
                            SharePreferencesUtil util = new SharePreferencesUtil(LoginActivity.this, VisitorConfig.VISIT_LOCAL_STORAGE);
                            util.putString(VisitorConfig.VISIT_LOCAL_BASEAPPS, schInfoResult.getRspData().getBaseapps());
                            Intent intent = new Intent();
                            intent.setClass(mContext, HomeActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(mContext, schInfoResult.getRspTxt(), Toast.LENGTH_LONG).show();
                    }

                    break;
                default:
                    break;
            }
            if (flag == REQUEST_FLAG_LOGIN && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        }

        private boolean hasPowerInPoint(int position, String text) {
            if (text == null) {
                return false;
            }
            Log.d(TAG, "" + text.charAt(position));
            return text.charAt(position) == '1';
        }

        @Override
        protected FlagObject doInBackground(Void... voids) {
            switch (flag) {
                case REQUEST_FLAG_SHAKEHAND:
                    return shakeHand();
                case REQUEST_FLAG_LOGIN:
                    try {
                        return schoolLogin(mShakeHandResult.getRspData());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                case REQUEST_FLAG_LOGIN_QX:
                    return getQx();
                default:
                    return null;
            }
        }
    }

}

