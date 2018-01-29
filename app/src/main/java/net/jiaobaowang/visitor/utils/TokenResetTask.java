package net.jiaobaowang.visitor.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import net.jiaobaowang.visitor.common.VisitorConfig;
import net.jiaobaowang.visitor.entity.TokenResetResult;
import net.jiaobaowang.visitor.visitor_interface.TaskCallBack;

import java.io.IOException;
import java.util.TreeMap;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ShangLinMo on 2018/1/27.
 */

public class TokenResetTask extends AsyncTask<Void, Void, String[]> {
    private Context mContext;
    private OkHttpClient mOkHttpClient;
    private TaskCallBack mTaskCallBack;

    public TokenResetTask(Context context, OkHttpClient okHttpClient, TaskCallBack taskCallBack) {
        this.mContext = context;
        this.mOkHttpClient = okHttpClient;
        this.mTaskCallBack = taskCallBack;
    }

    @Override
    protected String[] doInBackground(Void... voids) {
        String[] result = new String[2];
        SharedPreferences sp = mContext.getSharedPreferences(VisitorConfig.VISIT_LOCAL_STORAGE, MODE_PRIVATE);
        TreeMap<String, String> map = new TreeMap<>();
        map.put("uuid", Tools.getDeviceId(mContext));
        map.put("appid", Tools.getAppId(mContext));
        map.put("schid", String.valueOf(sp.getInt(VisitorConfig.VISIT_LOCAL_SCHOOL_ID, 0)));
        map.put("utname", sp.getString(VisitorConfig.VISIT_LOCAL_USERINFO_UTNAME, ""));
        map.put("utid", String.valueOf(sp.getInt(VisitorConfig.VISIT_LOCAL_USERINFO_UTID, 0)));
        map.put("utoken", sp.getString(VisitorConfig.VISIT_LOCAL_TOKEN, ""));
        try {
            map.put("sign", Tools.getSign(map));
            Gson gson = new Gson();
            String json = gson.toJson(map);
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(VisitorConfig.VISITOR_API_TOKEN_RESET)
                    .post(body)
                    .build();
            Response response = mOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                result[0] = "1";
                result[1] = response.body().string();
                return result;
            } else {
                throw new IOException("Unexpected code " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result[0] = "0";
            result[1] = e.toString();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String[] result) {
        Log.i("TokenResetTask", "onPostExecute:" + result[0] + " " + result[1]);
        String[] callBack = new String[2];
        if ("1".equals(result[0])) {
            Gson gson = new Gson();
            TokenResetResult tokenResetResult = gson.fromJson(result[1], TokenResetResult.class);
            if ("0000".equals(tokenResetResult.getRspCode())) {
                Log.i("TokenResetTask", "更新令牌成功");
                SharePreferencesUtil spu = new SharePreferencesUtil(mContext, VisitorConfig.VISIT_LOCAL_STORAGE);
                spu.putString(VisitorConfig.VISIT_LOCAL_TOKEN, tokenResetResult.getRspData());
                callBack[0] = "1";
                callBack[1] = "成功";
            } else {
                Log.i("TokenResetTask", "更新令牌失败");
                callBack[0] = "0";
                callBack[1] = tokenResetResult.getRspTxt();
            }
        } else {
            Log.i("TokenResetTask", "更新令牌失败");
            callBack[0] = "0";
            callBack[1] = result[1];
        }
        mTaskCallBack.CallBack(callBack);
    }
}
