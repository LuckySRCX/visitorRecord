package net.jiaobaowang.visitor.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import net.jiaobaowang.visitor.R;

/**
 * Created by rocka on 2017/12/26.
 * 这是基础Fragment界面
 */

public abstract class BaseFragmentActivity extends AppCompatActivity {
    private static String TAG;

    public abstract Fragment createFragment();

    protected int getLayoutResId() {
        return R.layout.activity_base_fragment;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getLocalClassName();
        Log.d(TAG, "-------created------");
        setContentView(getLayoutResId());
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragmentNav = fm.findFragmentById(R.id.fragment_navigation);
        if (fragmentNav == null) {
            fragmentNav = createFragment();
            fm.beginTransaction().add(R.id.fragment_navigation, fragmentNav).commit();
        }
    }
}
