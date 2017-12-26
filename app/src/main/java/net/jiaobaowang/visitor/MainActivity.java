package net.jiaobaowang.visitor;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.telpo.tps550.api.util.StringUtil;
import com.telpo.tps550.api.util.SystemUtil;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int deviceType = SystemUtil.getDeviceType();
        StringUtil.DeviceModelEnum[] values = StringUtil.DeviceModelEnum.values();
        String deviceName = "设备型号:" + values[deviceType];
        Log.i(TAG, deviceName);
    }
}
