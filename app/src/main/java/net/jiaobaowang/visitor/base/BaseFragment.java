package net.jiaobaowang.visitor.base;

import android.content.Context;
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
        TAG = getClass().getSimpleName();
        Log.e(TAG, "*******is created********");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "*****onResume******");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(TAG, "********onAttach*******");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(TAG, "********onDetach*******");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.e(TAG, "#####################isVisibleToUser:" + isVisibleToUser);
    }
}
