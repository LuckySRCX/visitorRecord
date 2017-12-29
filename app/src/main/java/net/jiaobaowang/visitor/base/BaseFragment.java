package net.jiaobaowang.visitor.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Created by rocka on 2017/12/29.
 */

public abstract class BaseFragment extends Fragment {
    protected static String TAG;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG=getClass().getSimpleName();
        Log.d(TAG,"*******is created********");
    }
}
