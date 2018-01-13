package net.jiaobaowang.visitor.printer;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.telpo.tps550.api.TelpoException;
import com.telpo.tps550.api.printer.UsbThermalPrinter;
import com.telpo.tps550.api.util.StringUtil;
import com.telpo.tps550.api.util.SystemUtil;

import net.jiaobaowang.visitor.R;
import net.jiaobaowang.visitor.entity.PrintForm;
import net.jiaobaowang.visitor.utils.DialogUtils;
import net.jiaobaowang.visitor.utils.PrinterCodeUtils;
import net.jiaobaowang.visitor.utils.ToastUtils;

public class PrinterActivity extends AppCompatActivity {
    private static final String TAG = "PrinterActivity";
    public static final String EXTRA_VISIT_RECORD="net.jiaobaowang.visitor.printer.visit_record";
    //打印机
    private final int NOPAPER = 1;//打印机缺纸
    private final int LOWBATTERY = 2;//打印机低电量
    private final int PRINTCONTENT = 3;//打印机打印内容
    private final int CANCELPROMPT = 4;//取消打印
    private final int OVERHEAT = 5;//打印机过热
    private final int PRINTERR = 6;//

    private String printContent;//打印的内容
    private Bitmap barCodeBm = null;
    private ProgressDialog progressDialog;
    private Boolean mLowBattery = false;//低电量
    private Boolean mNoPaper = false;//缺纸
    private UsbThermalPrinter mUsbThermalPrinter;
    private MyHandler handler;
    private PrintForm printForm;

    private Context mContext;
    private ImageView barCodeIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        printForm = (PrintForm) getIntent().getSerializableExtra("printForm");
        setContentView(R.layout.activity_prenter);
        mContext = this;
        initView();
        initPrinter();
    }


    @Override
    public void onDestroy() {
        if (progressDialog != null && !PrinterActivity.this.isFinishing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        unregisterReceiver(printReceive);
        try {
            mUsbThermalPrinter.stop();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    private void initView() {
        TextView tapeTypeTv = findViewById(R.id.tape_type_tv);
        TextView visNameTv = findViewById(R.id.vis_name_tv);
        TextView visGenderTv = findViewById(R.id.vis_gender_tv);
        TextView visOrgTv = findViewById(R.id.vis_org_tv);
        TextView visReasonTv = findViewById(R.id.vis_reason_tv);
        TextView userNameTv = findViewById(R.id.user_name_tv);
        TextView userDepartmentTv = findViewById(R.id.user_department_tv);
        LinearLayout studentLL = findViewById(R.id.student_ll);
        TextView gradeNameTv = findViewById(R.id.grade_name_tv);
        TextView classNameTv = findViewById(R.id.class_name_tv);
        TextView headmasterNameTv = findViewById(R.id.headmaster_name_tv);
        TextView entryTimeTv = findViewById(R.id.entry_time_tv);
        TextView registrantTv = findViewById(R.id.registrant_tv);
        barCodeIv = findViewById(R.id.bar_code_iv);
        TextView barCodeTv = findViewById(R.id.bar_code_tv);
        TextView remarksTv = findViewById(R.id.remarks_tv);
        Button confirmPrintBtn = findViewById(R.id.confirm_print_btn);
        Button cancelBtn = findViewById(R.id.cancel_btn);

        confirmPrintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initStartPrinting(PRINTCONTENT);
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //姓名
        String visName = getResources().getString(R.string.name) + printForm.getVisName();
        visNameTv.setText(visName);
        printContent = visName + "\n";
        //性别
        String gender = getResources().getString(R.string.gender);
        if (printForm.getVisGender() == 0) {
            gender = gender + "男";
        } else {
            gender = gender + "女";
        }
        visGenderTv.setText(gender);
        printContent = printContent + gender + "\n";
        //来访单位
        String visOrg = getResources().getString(R.string.visitor_org);
        if (printForm.getVisOrg() != null) {
            visOrg = visOrg + printForm.getVisOrg();
        }
        visOrgTv.setText(visOrg);
        printContent = printContent + visOrg + "\n";
        //访问事由
        String visReason = getResources().getString(R.string.visitor_reason);
        if (printForm.getVisReason() != null) {
            visReason = visReason + printForm.getVisReason();
        }
        visReasonTv.setText(visReason);
        printContent = printContent + visReason + "\n";
        //被访人
        String interviewee = getResources().getString(R.string.interviewee);
        if (printForm.getUserName() != null) {
            interviewee = interviewee + printForm.getUserName();
        }
        userNameTv.setText(interviewee);
        printContent = printContent + interviewee + "\n";
        //访客类型
        if (printForm.getUserType() == 0) {
            //教职工
            tapeTypeTv.setText(getResources().getString(R.string.teacher));
            //被访人的部门
            String userDepartment = getResources().getString(R.string.interviewee_department);
            if (printForm.getUserDepartment() != null) {
                userDepartment = userDepartment + printForm.getUserDepartment();
            }
            userDepartmentTv.setText(userDepartment);
            printContent = printContent + userDepartment + "\n";
        } else {
            //学生
            tapeTypeTv.setText(getResources().getString(R.string.student));
            userDepartmentTv.setVisibility(View.GONE);
            studentLL.setVisibility(View.VISIBLE);
            //年级
            String gradeName = getResources().getString(R.string.grade);
            if (printForm.getUserGradeName() != null) {
                gradeName = gradeName + printForm.getUserGradeName();
            }
            gradeNameTv.setText(gradeName);
            printContent = printContent + gradeName + "\n";
            //班级
            String className = getResources().getString(R.string.classes);
            if (printForm.getUserClassName() != null) {
                className = className + printForm.getUserClassName();
            }
            classNameTv.setText(className);
            printContent = printContent + className + "\n";
            //班主任
            String headMaster = getResources().getString(R.string.head_master);
            if (printForm.getUserHeadMaster() != null) {
                headMaster = headMaster + printForm.getUserHeadMaster();
            }
            headmasterNameTv.setText(headMaster);
            printContent = printContent + headMaster + "\n";
        }
        //进入时间
        String entryTime = getResources().getString(R.string.entry_time);
        if (printForm.getEntryTime() != null) {
            entryTime = entryTime + printForm.getEntryTime();
        }
        entryTimeTv.setText(entryTime);
        printContent = printContent + entryTime + "\n";
        //登记人
        String registrantName = getResources().getString(R.string.registrant);
        if (printForm.getRegisterName() != null) {
            registrantName = registrantName + printForm.getRegisterName();
        }
        registrantTv.setText(registrantName);
        printContent = printContent + registrantName + "\n";
        //条码
        //条码数字
        if (printForm.getFormId() != null) {
            String barCode = printForm.getFormId();
            try {
                barCodeBm = PrinterCodeUtils.CreateCode(barCode, BarcodeFormat.CODE_128, 300, 50);
            } catch (WriterException e) {
                e.printStackTrace();
                DialogUtils.showAlert(mContext, "条码生成失败：" + e.toString());
            }
            barCodeIv.setImageBitmap(barCodeBm);
            barCodeTv.setText(barCode);
        }
        //备注
        if (printForm.getRemarks() != null) {
            String remarks = "注：" + printForm.getRemarks();
            remarksTv.setText(remarks);
        }
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
                    new PrinterActivity.contentPrintThread().start();
                    break;
                case CANCELPROMPT://取消提示
                    if (progressDialog != null && !PrinterActivity.this.isFinishing()) {
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

    private void initPrinter() {
        handler = new MyHandler();
        mUsbThermalPrinter = new UsbThermalPrinter(mContext);
        IntentFilter pIntentFilter = new IntentFilter();
        pIntentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        pIntentFilter.addAction("android.intent.action.BATTERY_CAPACITY_EVENT");
        mContext.registerReceiver(printReceive, pIntentFilter);
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
                mUsbThermalPrinter.printString();
                //访客单类型
                mUsbThermalPrinter.setTextSize(24);
                if (printForm.getUserType() == 0) {
                    mUsbThermalPrinter.addString(getResources().getString(R.string.teacher));
                } else {
                    mUsbThermalPrinter.addString(getResources().getString(R.string.student));
                }
                mUsbThermalPrinter.printString();
                //访客单基本信息
                mUsbThermalPrinter.setAlgin(UsbThermalPrinter.ALGIN_LEFT);
                mUsbThermalPrinter.addString(printContent);
                mUsbThermalPrinter.printString();

                mUsbThermalPrinter.setAlgin(UsbThermalPrinter.ALGIN_MIDDLE);
                //打印条码
                if (barCodeBm != null) {
                    mUsbThermalPrinter.printLogo(barCodeBm, true);
                    mUsbThermalPrinter.printString();
                }
                if (printForm.getFormId() != null) {
                    //打印条码数字信息
                    mUsbThermalPrinter.setTextSize(16);
                    mUsbThermalPrinter.addString(printForm.getFormId());
                    mUsbThermalPrinter.printString();
                }
                if (printForm.getRemarks() != null) {
                    //打印备注
                    String remarks = "注：" + printForm.getRemarks();
                    mUsbThermalPrinter.setTextSize(20);
                    mUsbThermalPrinter.addString(remarks);
                    mUsbThermalPrinter.printString();
                }
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
        Log.e(TAG, "printCatch:" + error);
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
