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
        Fragment detailFragment;
        if (curId == id) {
            return;
        }
        curId = id;
        switch (id) {
            case 0://SignIn
                detailFragment = SignInFragment.newInstance();
                break;
            case 1://query
                detailFragment = SignQueryFragment.newInstance();
                break;
            case 2://signOff
                detailFragment = SignOffFragment.newInstance();
                break;
            default:
                detailFragment = SignInFragment.newInstance();
                break;
        }
        setDetailFragment(detailFragment);

    }

    private void setDetailFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fg = fm.findFragmentById(R.id.fragment_detail);
        if (fg != null) {
            fm.beginTransaction().remove(fg).commit();
        }
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_detail, fragment).commit();
    }

    @Override
    public Fragment createFragment() {
        return NavigationFragment.newInstance();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDetailFragment(SignInFragment.newInstance());
    }
}
