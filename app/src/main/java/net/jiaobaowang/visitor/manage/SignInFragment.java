package net.jiaobaowang.visitor.manage;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.google.zxing.other.BeepManager;
import com.telpo.tps550.api.TelpoException;
import com.telpo.tps550.api.idcard.IdCard;
import com.telpo.tps550.api.idcard.IdentityInfo;
import com.telpo.tps550.api.printer.UsbThermalPrinter;
import com.telpo.tps550.api.util.StringUtil;
import com.telpo.tps550.api.util.SystemUtil;

import net.jiaobaowang.visitor.R;
import net.jiaobaowang.visitor.entity.Guard;
import net.jiaobaowang.visitor.entity.User;
import net.jiaobaowang.visitor.entity.Visitor;
import net.jiaobaowang.visitor.entity.VisitorForm;
import net.jiaobaowang.visitor.printer.PrinterActivity;
import net.jiaobaowang.visitor.utils.DialogUtils;
import net.jiaobaowang.visitor.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 访客登记
 */
public class SignInFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "RegistrationFragment";

    private final int ID_REQ1 = 1;//身份证正面
    private final int ID_CAMERA = 2;//摄像头
    //打印机
    private final int NOPAPER = 1;//打印机缺纸
    private final int LOWBATTERY = 2;//打印机低电量
    private final int PRINTCONTENT = 3;//打印机打印内容
    private final int CANCELPROMPT = 4;//取消打印
    private final int OVERHEAT = 5;//打印机过热
    private final int PRINTERR = 6;//

    //身份证
    private IdentityInfo idCardInfo;//二代身份证信息
    private Bitmap headImage;//身份证头像
    private BeepManager beepManager;//bee声音
    //打印凭条
    private String tapeType;
    private String infoStr;
    private Bitmap barCodeBm;
    private String barCodeStr;
    private ProgressDialog progressDialog;
    private Boolean mLowBattery = false;//低电量
    private Boolean mNoPaper = false;//缺纸
    private UsbThermalPrinter mUsbThermalPrinter;
    private MyHandler handler;

    private Context mContext;
    private Button saveBtn;//保存
    private Button printTapeBtn;//打印凭条
    private Button idCardReadBtn;//读取身份证
    private Button idCardOCRBtn;//识别身份证
    private Button cameraBtn;//拍照
    private ImageView idCardHeadIv;//身份证头像
    private EditText nameEt;//姓名
    private EditText dateOfBirthEt;//出生日期
    private EditText idNumberEt;//证件号码
    private EditText addressEt;//地址
    private EditText phoneNumberEt;//电话号码
    private EditText visitorNumberEt;//访客人数
    private EditText belongingsEt;//随身物品
    private EditText organizationEt;//单位名称
    private EditText plateNumberEt;//车牌号
    private EditText remarksEt;//备注
    private RadioButton maleRb;//男
    private RadioButton femaleRb;//女
    private Spinner credentialsSpinner;//证件类型
    private Spinner reasonSpinner;//事由类型

    public SignInFragment() {
    }

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NOPAPER://缺纸
                    DialogUtils.showAlert(mContext, "打印缺纸，请放入纸后重试!");
                    break;
                case LOWBATTERY://低电量
                    DialogUtils.showAlert(mContext, "电池电量低，请连接充电器！");
                    break;
                case PRINTCONTENT:
                    new contentPrintThread().start();
                    break;
                case CANCELPROMPT://取消提示
                    if (progressDialog != null && !getActivity().isFinishing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                    break;
                case OVERHEAT://过热
                    DialogUtils.showAlert(mContext, "打印过热！");
                    break;
                default:
                    ToastUtils.showMessage(mContext, "Print Error!");
                    break;
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        mContext = getActivity();
        initView(view);
        initPrinter();
        return view;
    }

    public void initView(View view) {
        saveBtn = view.findViewById(R.id.save_btn);
        printTapeBtn = view.findViewById(R.id.print_tape_btn);
        idCardReadBtn = view.findViewById(R.id.id_card_read_btn);
        idCardOCRBtn = view.findViewById(R.id.id_card_ocr_btn);
        cameraBtn = view.findViewById(R.id.camera_btn);
        idCardHeadIv = view.findViewById(R.id.id_card_head_iv);
        nameEt = view.findViewById(R.id.name_et);
        maleRb = view.findViewById(R.id.male_rb);
        femaleRb = view.findViewById(R.id.female_rb);
        dateOfBirthEt = view.findViewById(R.id.date_of_birth_et);
        credentialsSpinner = view.findViewById(R.id.credentials_spinner);
        idNumberEt = view.findViewById(R.id.id_number_et);
        addressEt = view.findViewById(R.id.address_et);
        reasonSpinner = view.findViewById(R.id.reason_spinner);
        phoneNumberEt = view.findViewById(R.id.phone_number_et);
        visitorNumberEt = view.findViewById(R.id.visitor_number_et);
        belongingsEt = view.findViewById(R.id.belongings_et);
        organizationEt = view.findViewById(R.id.organization_et);
        plateNumberEt = view.findViewById(R.id.plate_number_et);
        remarksEt = view.findViewById(R.id.remarks_et);
        saveBtn.setOnClickListener(this);
        printTapeBtn.setOnClickListener(this);
        idCardReadBtn.setOnClickListener(this);
        idCardOCRBtn.setOnClickListener(this);
        cameraBtn.setOnClickListener(this);
    }

    public void initPrinter() {
        handler = new MyHandler();
        mUsbThermalPrinter = new UsbThermalPrinter(mContext);
        IntentFilter pIntentFilter = new IntentFilter();
        pIntentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        pIntentFilter.addAction("android.intent.action.BATTERY_CAPACITY_EVENT");
        mContext.registerReceiver(printReceive, pIntentFilter);
        final ProgressDialog getVersionDialog = new ProgressDialog(mContext);
        getVersionDialog.setTitle("操作中");
        getVersionDialog.setMessage("正在检测驱动版本，请稍候...");
        getVersionDialog.setCancelable(false);
        getVersionDialog.show();
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    mUsbThermalPrinter.start(0);
                    mUsbThermalPrinter.reset();
                    String printVersion = mUsbThermalPrinter.getVersion();
                    if (printVersion != null) {
                        Log.i(TAG, "打印机版本：" + printVersion);
                    }
                } catch (TelpoException e) {
                    e.printStackTrace();
                } finally {
                    getVersionDialog.dismiss();
                }
            }
        }).start();
    }

    /**
     * 电量的广播
     */
    BroadcastReceiver printReceive = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS,
                        BatteryManager.BATTERY_STATUS_NOT_CHARGING);
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
                // TPS390 can not print,while in low battery,whether is charging or not charging
                if (SystemUtil.getDeviceType() == StringUtil.DeviceModelEnum.TPS390.ordinal()) {
                    if (level * 5 <= scale) {
                        mLowBattery = true;
                    } else {
                        mLowBattery = false;
                    }
                } else {
                    if (status != BatteryManager.BATTERY_STATUS_CHARGING) {
                        if (level * 5 <= scale) {
                            mLowBattery = true;
                        } else {
                            mLowBattery = false;
                        }
                    } else {
                        mLowBattery = false;
                    }
                }
            }
            // Only use for TPS550MTK devices
            else if (action.equals("android.intent.action.BATTERY_CAPACITY_EVENT")) {
                int status = intent.getIntExtra("action", 0);
                int level = intent.getIntExtra("level", 0);
                if (status == 0) {
                    if (level < 1) {
                        mLowBattery = true;
                    } else {
                        mLowBattery = false;
                    }
                } else {
                    mLowBattery = false;
                }
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        beepManager = new BeepManager(getActivity(), R.raw.beep);
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    IdCard.open(getActivity());
                } catch (TelpoException e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            idCardReadBtn.setEnabled(false);
                            ToastUtils.showMessage(mContext, R.string.identify_read_fail);
                        }
                    });
                }
            }
        }).start();
        if (!checkPackage("com.telpo.tps550.api")) {
            ToastUtils.showMessage(mContext, R.string.identify_ocr_fail);
            idCardOCRBtn.setEnabled(false);
        } else {
            idCardOCRBtn.setEnabled(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        beepManager.close();
        beepManager = null;
        IdCard.close();
    }

    @Override
    public void onDestroy() {
        if (progressDialog != null && !getActivity().isFinishing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        getActivity().unregisterReceiver(printReceive);
        mUsbThermalPrinter.stop();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_btn://保存
                break;
            case R.id.print_tape_btn://显示凭条
                showPrintTape();
                break;
            case R.id.id_card_read_btn://读取身份证
                clearVisitorInfo();
                new GetIDInfoTask().execute();
                break;
            case R.id.id_card_ocr_btn://识别身份证
                idCardOCRBtn.setEnabled(false);
                clearVisitorInfo();
                Intent intent = new Intent();
                intent.setClassName("com.telpo.tps550.api",
                        "com.telpo.tps550.api.ocr.IdCardOcr");
                intent.putExtra("type", true);
                intent.putExtra("show_head_photo", true);

                //intent.putExtra("isKeepPicture", true);// 是否保存图片
                // true是，false:否，不传入时，默认为否
                //intent.putExtra("PictPath", "/sdcard/DCIM/Camera/003.png");// 图片路径，不传入时保存到默认路径/sdcard/OCRPict
                //intent.putExtra("PictFormat", "PNG");// 图片格式：JPEG，PNG，WEBP，不传入时默认为PNG格式
                try {
                    startActivityForResult(intent, ID_REQ1);
                } catch (ActivityNotFoundException exception) {
                    ToastUtils.showMessage(mContext, R.string.identify_ocr_fail);
                }
                break;
            case R.id.camera_btn:
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, ID_CAMERA);
                break;
        }
    }

    private class GetIDInfoTask extends AsyncTask<Void, Integer, TelpoException> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            //在execute被调用后立即执行
            super.onPreExecute();
            idCardReadBtn.setEnabled(false);
            dialog = new ProgressDialog(mContext);
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
                idCardInfo = IdCard.checkIdCard(1600);// luyq modify
                if (idCardInfo != null) {
                    byte[] image = IdCard.getIdCardImage();
                    headImage = IdCard.decodeIdCardImage(image);
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
            idCardReadBtn.setEnabled(true);
            if (result == null) {
                inputIdCardInfo();
            } else {
                String errorStr = result.toString();
                if (errorStr.equals("com.telpo.tps550.api.TimeoutException")) {
                    errorStr = "超时，请重新尝试";
                } else if (errorStr.equals("com.telpo.tps550.api.DeviceNotOpenException")) {
                    errorStr = "读卡器未打开";
                }
                DialogUtils.showAlert(mContext, errorStr);
            }
        }
    }

    private boolean checkPackage(String packageName) {
        PackageManager manager = getActivity().getPackageManager();
        Intent intent = new Intent().setPackage(packageName);
        @SuppressLint("WrongConstant") List<ResolveInfo> infos = manager.queryIntentActivities(intent,
                PackageManager.GET_INTENT_FILTERS);
        if (infos == null || infos.size() < 1) {
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.i(TAG, "requestCode:" + requestCode + "\n" + "resultCode:" + resultCode);
        if (requestCode == ID_REQ1) {
            //识别身份证
            if (resultCode == Activity.RESULT_CANCELED) {
                //成功
                try {
                    idCardOCRBtn.setEnabled(true);
                    if (data != null) {
                        idCardInfo = (IdentityInfo) data.getSerializableExtra("idInfo");
                        if (idCardInfo != null && idCardInfo.getName() != null && idCardInfo.getSex() != null && idCardInfo.getBorn() != null && idCardInfo.getNo() != null && idCardInfo.getAddress() != null && idCardInfo.getHead_photo() != null) {
                            //成功
                            headImage = BitmapFactory.decodeByteArray(idCardInfo.getHead_photo(), 0, idCardInfo.getHead_photo().length);
                            inputIdCardInfo();
                        } else {
                            DialogUtils.showAlert(mContext, "识别身份证失败，请重新尝试");
                        }
                    } else {
                        DialogUtils.showAlert(mContext, "识别身份证失败，请重新尝试");
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    String errorStr = e.toString();
                    DialogUtils.showAlert(mContext, errorStr);
                }
            } else {
                DialogUtils.showAlert(mContext, "识别身份证失败，请重新尝试");
            }
        } else if (requestCode == ID_CAMERA && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                idCardHeadIv.setImageBitmap(photo);
            }
        }
    }

    /**
     * 清空访客信息
     */
    public void clearVisitorInfo() {
        idCardInfo = null;
        headImage = null;
        idCardHeadIv.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        nameEt.setText("");
        dateOfBirthEt.setText("");
        idNumberEt.setText("");
        addressEt.setText("");
        phoneNumberEt.setText("");
        visitorNumberEt.setText("");
        belongingsEt.setText("");
        organizationEt.setText("");
        plateNumberEt.setText("");
        remarksEt.setText("");
    }

    /**
     * 输入身份证信息
     */
    public void inputIdCardInfo() {
        if (beepManager != null) {
            beepManager.playBeepSoundAndVibrate();
        }
        credentialsSpinner.setSelection(0, true);
        idCardHeadIv.setImageBitmap(headImage);
        nameEt.setText(idCardInfo.getName());
        String sex = idCardInfo.getSex();
        if ("男 / M".equals(sex)) {
            maleRb.setChecked(true);
            femaleRb.setChecked(false);
        } else if ("女 / F".equals(sex)) {
            maleRb.setChecked(false);
            femaleRb.setChecked(true);
        }
        dateOfBirthEt.setText(idCardInfo.getBorn());
        idNumberEt.setText(idCardInfo.getNo());
        addressEt.setText(idCardInfo.getAddress());
        Log.i(TAG, "---身份证---" + "\n"
                + "姓名：" + idCardInfo.getName() + "\n"
                + "性别：" + idCardInfo.getSex() + "\n"
                + "民族：" + idCardInfo.getNation() + "\n"
                + "出生日期：" + idCardInfo.getBorn() + "\n"
                + "地址：" + idCardInfo.getAddress() + "\n"
                + "签发机关：" + idCardInfo.getApartment() + "\n"
                + "有效期限：" + idCardInfo.getPeriod() + "\n"
                + "身份证号码：" + idCardInfo.getNo() + "\n"
                + "国籍或所在地区代码：" + idCardInfo.getCountry() + "\n"
                + "中文姓名：" + idCardInfo.getCn_name() + "\n"
                + "证件类型：" + idCardInfo.getCard_type() + "\n"
                + "保留信息：" + idCardInfo.getReserve());
    }

    /**
     * 显示打印凭条dialog
     */
    public void showPrintTape() {
        //用户
        User user = new User();
        user.setId("zhangsan");
        user.setName("张三");
        user.setType("学生");
        user.setGradeName("一年级");
        user.setClassName("1303班");
        user.setHeadMaster("王五");
        //访客
        Visitor visitor = new Visitor();
        visitor.setName(nameEt.getText().toString());
        if (maleRb.isChecked()) {
            visitor.setGender("男");
        } else {
            visitor.setGender("女");
        }
        visitor.setBorn(dateOfBirthEt.getText().toString());
        visitor.setAddress(addressEt.getText().toString());
        visitor.setCardNumber(idNumberEt.getText().toString());
        visitor.setCardType(credentialsSpinner.getSelectedItem().toString());
        visitor.setImage(headImage);
        //门卫
        Guard guard = new Guard();
        guard.setId("lisi");
        guard.setName("李四");
        //访客单
        VisitorForm visitorForm = new VisitorForm();
        visitorForm.setVisitor(visitor);
        visitorForm.setUser(user);
        visitorForm.setGuard(guard);
        visitorForm.setReason(reasonSpinner.getSelectedItem().toString());
        visitorForm.setPhoneNumber(phoneNumberEt.getText().toString());
        visitorForm.setVisitorNumber(visitorNumberEt.getText().toString());
        visitorForm.setBelongings(belongingsEt.getText().toString());
        visitorForm.setOrganization(organizationEt.getText().toString());
        visitorForm.setPlateNumber(plateNumberEt.getText().toString());
        visitorForm.setRemarks(remarksEt.getText().toString());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());
        String time = sdf.format(curDate);
        visitorForm.setRegisterTime(time);
        visitorForm.setEntryTime(time);
        Intent intent = new Intent(mContext, PrinterActivity.class);
        intent.putExtra("type", 0);
        intent.putExtra("data", visitorForm);
        startActivity(intent);
    }

    /**
     * 开始打印
     */
    private void initStartPrinting(int type) {
        if (mLowBattery) {
            handler.sendMessage(handler.obtainMessage(LOWBATTERY, 1, 0, null));
        } else {
            if (!mNoPaper) {
                progressDialog = ProgressDialog.show(mContext, "打印", "打印中，请稍候……");
                handler.sendMessage(handler.obtainMessage(type, 1, 0, null));
            } else {
                DialogUtils.showAlert(mContext, "打印机初始化中，请稍后再试");
            }
        }
    }

    /**
     * 文字打印
     */
    private class contentPrintThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                mUsbThermalPrinter.reset();
                mUsbThermalPrinter.setLeftIndent(0);
                mUsbThermalPrinter.setLineSpace(0);
                mUsbThermalPrinter.setGray(0);

                mUsbThermalPrinter.setAlgin(UsbThermalPrinter.ALGIN_MIDDLE);
                mUsbThermalPrinter.setTextSize(56);
                mUsbThermalPrinter.addString("访客单");
                mUsbThermalPrinter.addStringOffset(8, tapeType);
                mUsbThermalPrinter.printString();
                //访客单类型
                mUsbThermalPrinter.setTextSize(48);
                mUsbThermalPrinter.addString(tapeType);
                mUsbThermalPrinter.printString();
                //访客单基本信息
                mUsbThermalPrinter.setAlgin(UsbThermalPrinter.ALGIN_LEFT);
                mUsbThermalPrinter.setTextSize(24);
                mUsbThermalPrinter.addString(infoStr);
                mUsbThermalPrinter.printString();

                mUsbThermalPrinter.setAlgin(UsbThermalPrinter.ALGIN_MIDDLE);
//                if (headImage != null) {
//                    mUsbThermalPrinter.printLogo(headImage, true);
//                    mUsbThermalPrinter.printString();
//                }
                //打印条码
                if (barCodeBm != null) {
                    mUsbThermalPrinter.printLogo(barCodeBm, true);
                    mUsbThermalPrinter.printString();
                }
                //打印条码数字信息
                mUsbThermalPrinter.setTextSize(16);
                mUsbThermalPrinter.addString(barCodeStr);
                mUsbThermalPrinter.printString();
                //打印备注
                mUsbThermalPrinter.setTextSize(20);
                mUsbThermalPrinter.addString(getResources().getString(R.string.print_tape_remarks));
                mUsbThermalPrinter.printString();


                mUsbThermalPrinter.walkPaper(20);
            } catch (Exception e) {
                e.printStackTrace();
                printCatch(e.toString());
            } finally {
                printFinally();
            }
        }
    }

    /**
     * 打印异常
     */
    private void printCatch(String error) {
        switch (error) {
            case "com.telpo.tps550.api.printer.NoPaperException":
                //打印机缺纸
                mNoPaper = true;
                break;
            case "com.telpo.tps550.api.printer.OverHeatException":
                //打印机过热
                handler.sendMessage(handler.obtainMessage(OVERHEAT, 1, 0, null));
                break;
            default:
                handler.sendMessage(handler.obtainMessage(PRINTERR, 1, 0, null));
                break;
        }

    }

    /**
     * 打印异常
     */
    private void printFinally() {
        handler.sendMessage(handler.obtainMessage(CANCELPROMPT, 1, 0, null));
        if (mNoPaper) {
            handler.sendMessage(handler.obtainMessage(NOPAPER, 1, 0, null));
            mNoPaper = false;
        }
    }
}
