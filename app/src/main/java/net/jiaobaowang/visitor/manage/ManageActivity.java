package net.jiaobaowang.visitor.manage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import net.jiaobaowang.visitor.R;
import net.jiaobaowang.visitor.base.BaseFragmentActivity;

/**
 * 访客管理界面
 */
public class ManageActivity extends BaseFragmentActivity implements NavigationFragment.OnFragmentInteractionListener {
    public static final String EXTRA_CODE="net.jiaobaowang.visitor.extra.home_Code";
    private int curId;

    @Override
    public void onFragmentInteraction(int id) {
        if (id == 0) {
            finish();
        } else if (curId != id) {
            curId = id;
            setDetailFragment(id);
        }
    }

    private void setDetailFragment(int id) {
        Fragment fragment;
        switch (id) {
            case 1://SignIn
                fragment = SignInFragment.newInstance();
                break;
            case 2://query
                fragment = SignQueryFragment.newInstance();
                break;
            case 3://signOff
                fragment = SignOffFragment.newInstance();
                break;
            default:
                fragment = SignInFragment.newInstance();
                break;
        }
        FragmentManager fm = getSupportFragmentManager();
        Fragment fg_detail = fm.findFragmentById(R.id.fragment_detail);
        if (fg_detail != null) {
            fm.beginTransaction().remove(fg_detail).commit();
        }
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_detail, fragment).commit();
    }

    @Override
    public Fragment createFragment() {
        return NavigationFragment.newInstance(curId);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        curId = getIntent().getIntExtra(ManageActivity.EXTRA_CODE,1);
        super.onCreate(savedInstanceState);
        setDetailFragment(curId);
    }
}
