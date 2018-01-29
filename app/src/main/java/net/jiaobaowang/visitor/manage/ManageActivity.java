package net.jiaobaowang.visitor.manage;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.google.zxing.other.BeepManager;
import com.telpo.tps550.api.TelpoException;
import com.telpo.tps550.api.idcard.IdCard;
import com.telpo.tps550.api.idcard.IdentityInfo;

import net.jiaobaowang.visitor.R;
import net.jiaobaowang.visitor.base.BaseFragmentActivity;
import net.jiaobaowang.visitor.common.VisitorConstant;
import net.jiaobaowang.visitor.custom_view.VisitViewPager;
import net.jiaobaowang.visitor.utils.TianBoUtils;
import net.jiaobaowang.visitor.utils.ToastUtils;
import net.jiaobaowang.visitor.visitor_interface.OnGetIdentityInfoListener;
import net.jiaobaowang.visitor.visitor_interface.OnGetIdentityInfoResult;
import net.jiaobaowang.visitor.visitor_interface.OnGetQRCodeListener;
import net.jiaobaowang.visitor.visitor_interface.OnGetQRCodeResult;

/**
 * 访客管理界面
 */
public class ManageActivity extends BaseFragmentActivity implements NavigationFragment.OnFragmentInteractionListener, OnGetIdentityInfoListener, OnGetQRCodeListener {
    private static final String TAG = "ManageActivity";
    public static final String SIGN_IN_FRAGMENT = "SignInFragment";
    public static final String SIGN_QUERY_FRAGMENT = "SignQueryFragment";
    public static final String SIGN_OFF_FRAGMENT = "SignOffFragment";

    public static final String EXTRA_CODE = "net.jiaobaowang.visitor.extra.home_Code";
    private int curId;//当前fragment的id
    private IdentityInfo identityInfo;//身份证信息
    private Bitmap identityImage;//身份证头像
    private BeepManager beepManager;//bee声音
    private OnGetIdentityInfoResult onGetIdentityInfoResult;
    private OnGetQRCodeResult onGetQRCodeResult;
    private VisitViewPager mPager;
    ManageViewPagerAdapter adapter;
    private SignQueryFragment mQueryFragment;
    private SignOffFragment mOffFragment;
    private SignInFragment mInFragment;

    @Override
    public void onFragmentInteraction(int id) {
        if (id == 0) {
            finish();
        } else if (curId != id) {
            //隐藏键盘
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            try {
                inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            } catch (Exception e) {
                e.printStackTrace();
            }
            curId = id;
            setDetailFragment(id);
            if (id == 1 && mInFragment != null) {
                mInFragment.clearFragment();
            }
        }
    }

    private void setDetailFragment(int id) {
        Log.i(TAG, "setDetailFragment:" + id);
        if (id > 0) {
            mPager.setCurrentItem(id - 1);
        }
    }

    @Override
    public Fragment createFragment() {
        return NavigationFragment.newInstance(curId);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        curId = getIntent().getIntExtra(ManageActivity.EXTRA_CODE, 1);
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mInFragment = (SignInFragment) getSupportFragmentManager().getFragment(savedInstanceState, SIGN_IN_FRAGMENT);
            mQueryFragment = (SignQueryFragment) getSupportFragmentManager().getFragment(savedInstanceState, SIGN_QUERY_FRAGMENT);
            mOffFragment = (SignOffFragment) getSupportFragmentManager().getFragment(savedInstanceState, SIGN_OFF_FRAGMENT);
        }
        mPager = findViewById(R.id.fragment_detail);
        mPager.setOffscreenPageLimit(2);
        mPager.setCanScroll(false);
        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new ManageViewPagerAdapter(fragmentManager);
        mPager.setAdapter(adapter);
        setDetailFragment(curId);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mInFragment != null && mInFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, SIGN_IN_FRAGMENT, mInFragment);
        }
        if (mQueryFragment != null && mQueryFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, SIGN_QUERY_FRAGMENT, mQueryFragment);
        }
        if (mOffFragment != null && mOffFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, SIGN_OFF_FRAGMENT, mOffFragment);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        beepManager = new BeepManager(ManageActivity.this, R.raw.beep);
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    IdCard.open(ManageActivity.this);
                } catch (TelpoException e) {
                    e.printStackTrace();
                    ManageActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            ToastUtils.showMessage(ManageActivity.this, R.string.identify_read_fail);
                        }
                    });
                }
            }
        }).start();
    }

    class ManageViewPagerAdapter extends FragmentStatePagerAdapter {

        public ManageViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (mInFragment == null) {
                        mInFragment = SignInFragment.newInstance();
                    }
                    return mInFragment;
                case 1:
                    if (mQueryFragment == null) {
                        mQueryFragment = SignQueryFragment.newInstance();
                    }
                    return mQueryFragment;
                case 2:
                    if (mOffFragment == null) {
                        mOffFragment = SignOffFragment.newInstance();
                    }
                    return mOffFragment;
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        beepManager.close();
        beepManager = null;
        IdCard.close();
    }

    @Override
    public void getIdentityInfo() {
        initInterface();
        new GetIDInfoTask().execute();
    }

    @Override
    public void getQRCode() {
        initInterface();
        if (TianBoUtils.checkPackage(ManageActivity.this, "com.telpo.tps550.api")) {
            Intent intent = new Intent();
            intent.setClassName("com.telpo.tps550.api", "com.telpo.tps550.api.barcode.Capture");
            try {
                startActivityForResult(intent, VisitorConstant.ARC_VISITOR_SYSTEM_QRCODE);
            } catch (ActivityNotFoundException e) {
                onGetQRCodeResult.getQRCodeResult(0, getResources().getString(R.string.qrcode_fail), "");
            }
        } else {
            onGetQRCodeResult.getQRCodeResult(0, getResources().getString(R.string.qrcode_fail), "");
        }
    }

    /**
     * 初始化返回身份证信息，条码的页面
     */
    private void initInterface() {
        if (curId == 1) {//来访登记
            onGetIdentityInfoResult = mInFragment;
            onGetQRCodeResult = null;
        } else {//访客签离
            onGetIdentityInfoResult = mOffFragment;
            onGetQRCodeResult = mOffFragment;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult:" + requestCode + " " + resultCode);
        if (requestCode == VisitorConstant.ARC_VISITOR_SYSTEM_QRCODE) {
            if (resultCode == 0) {
                if (data != null) {
                    String qrCode = data.getStringExtra("qrCode");
                    onGetQRCodeResult.getQRCodeResult(1, "成功", qrCode);

                }
            } else {
                onGetQRCodeResult.getQRCodeResult(0, "条码/二维码：扫描失败", "");
            }
        }
    }

    private class GetIDInfoTask extends AsyncTask<Void, Integer, TelpoException> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            //在execute被调用后立即执行
            super.onPreExecute();
            dialog = new ProgressDialog(ManageActivity.this);
            dialog.setTitle("操作中");
            dialog.setMessage("连接读卡器...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected TelpoException doInBackground(Void... voids) {
            //在onPreExecute()完成后立即执行
            TelpoException result = null;
            try {
                publishProgress(1);
                identityInfo = IdCard.checkIdCard(1600);// luyq modify
                if (identityInfo != null) {
                    byte[] image = IdCard.getIdCardImage();
                    identityImage = IdCard.decodeIdCardImage(image);
                    Log.i(TAG, "---身份证---" + "\n"
                            + "姓名：" + identityInfo.getName() + "\n"
                            + "性别：" + identityInfo.getSex() + "\n"
                            + "民族：" + identityInfo.getNation() + "\n"
                            + "出生日期：" + identityInfo.getBorn() + "\n"
                            + "地址：" + identityInfo.getAddress() + "\n"
                            + "签发机关：" + identityInfo.getApartment() + "\n"
                            + "有效期限：" + identityInfo.getPeriod() + "\n"
                            + "身份证号码：" + identityInfo.getNo() + "\n"
                            + "国籍或所在地区代码：" + identityInfo.getCountry() + "\n"
                            + "中文姓名：" + identityInfo.getCn_name() + "\n"
                            + "证件类型：" + identityInfo.getCard_type() + "\n"
                            + "保留信息：" + identityInfo.getReserve());
                }
            } catch (TelpoException e) {
                e.printStackTrace();
                result = e;
            }
            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //在调用publishProgress时此方法被执行
            super.onProgressUpdate(values);
            if (values[0] == 1) {
                dialog.setMessage("获取身份证信息");
            }
        }

        @Override
        protected void onPostExecute(TelpoException result) {
            //当后台操作结束时，此方法将会被调用
            super.onPostExecute(result);
            dialog.dismiss();
            if (result == null) {
                beepManager.playBeepSoundAndVibrate();
                onGetIdentityInfoResult.getIdentityInfoResult(1, "成功", identityInfo, identityImage);
            } else {
                String errorStr = result.toString();
                if (errorStr.equals("com.telpo.tps550.api.TimeoutException")) {
                    errorStr = "超时，请重新尝试";
                } else if (errorStr.equals("com.telpo.tps550.api.DeviceNotOpenException")) {
                    errorStr = "读卡器未打开";
                }
                onGetIdentityInfoResult.getIdentityInfoResult(0, errorStr, null, null);
            }
            identityInfo = null;
            identityImage = null;
        }
    }
}
