package net.jiaobaowang.visitor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.jiaobaowang.visitor.login.LoginActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        int deviceType = SystemUtil.getDeviceType();
//        StringUtil.DeviceModelEnum[] values = StringUtil.DeviceModelEnum.values();
//        String deviceName = "设备型号:" + values[deviceType];
//        Log.i(TAG, deviceName);
        Intent intent=new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
