package net.jiaobaowang.visitor.registration;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.zxing.other.BeepManager;
import com.telpo.tps550.api.TelpoException;
import com.telpo.tps550.api.idcard.IdCard;
import com.telpo.tps550.api.idcard.IdentityInfo;

import net.jiaobaowang.visitor.R;

/**
 * 来访登记
 */
public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "RegistrationActivity";
    private IdentityInfo idCardInfo;//二代身份证信息
    private Bitmap headImage;//身份证头像
    private BeepManager beepManager;//bee声音

    private Context mContext;
    private Button idCardReadBtn;//读取身份证
    private Button idCardOCRBtn;//识别身份证
    private ImageView idCardHeadIv;//身份证头像
    private EditText nameEt;//姓名
    private RadioButton maleRb;//男
    private RadioButton femaleRb;//女
    private EditText dateOfBirthEt;//出生日期
    private Spinner credentialsSpinner;//证件类型
    private EditText idNumberEt;//证件号码
    private EditText addressEt;//地址
    private Spinner reasonSpinner;//事由类型
    private EditText phoneNumberEt;//电话号码
    private EditText visitorNumberEt;//访客人数
    private EditText belongingsEt;//随身物品
    private EditText organizationEt;//单位名称
    private EditText plateNumberEt;//车牌号
    private EditText remarksEt;//备注

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mContext = this;
        idCardReadBtn = findViewById(R.id.id_card_read_btn);
        idCardOCRBtn = findViewById(R.id.id_card_ocr_btn);
        idCardHeadIv = findViewById(R.id.id_card_head_iv);
        nameEt = findViewById(R.id.name_et);
        maleRb = findViewById(R.id.male_rb);
        femaleRb = findViewById(R.id.female_rb);
        dateOfBirthEt = findViewById(R.id.date_of_birth_et);
        credentialsSpinner = findViewById(R.id.credentials_spinner);
        idNumberEt = findViewById(R.id.id_number_et);
        addressEt = findViewById(R.id.address_et);
        reasonSpinner = findViewById(R.id.reason_spinner);
        phoneNumberEt = findViewById(R.id.phone_number_et);
        visitorNumberEt = findViewById(R.id.visitor_number_et);
        belongingsEt = findViewById(R.id.belongings_et);
        organizationEt = findViewById(R.id.organization_et);
        plateNumberEt = findViewById(R.id.plate_number_et);
        remarksEt = findViewById(R.id.remarks_et);
        idCardReadBtn.setOnClickListener(this);
        idCardOCRBtn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        beepManager = new BeepManager(this, R.raw.beep);
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    IdCard.open(RegistrationActivity.this);
                } catch (TelpoException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            idCardReadBtn.setEnabled(false);
                            Toast.makeText(RegistrationActivity.this, "连接身份证读卡器失败，无法读取身份证信息", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        beepManager.close();
        beepManager = null;
        IdCard.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_card_read_btn://读取身份证
                new GetIDInfoTask().execute();
                break;
            case R.id.id_card_ocr_btn://识别身份证
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
            idCardOCRBtn.setEnabled(false);
            dialog = new ProgressDialog(mContext);
            dialog.setTitle("操作中");
            dialog.setMessage("连接读卡器...");
            dialog.setCancelable(false);
            dialog.show();
            idCardInfo = null;
            headImage = null;
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
            idCardOCRBtn.setEnabled(true);
            if (result == null) {
                beepManager.playBeepSoundAndVibrate();
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
                credentialsSpinner.setSelection(0, true);
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
            } else {
                String resultStr = result.toString();
                if (resultStr.equals("com.telpo.tps550.api.TimeoutException")) {
                    resultStr = "超时，请重新尝试";
                } else if (resultStr.equals("com.telpo.tps550.api.DeviceNotOpenException")) {
                    resultStr = "读卡器未打开";
                }
                new AlertDialog.Builder(mContext)
                        .setTitle("身份证读取失败")
                        .setMessage(resultStr)
                        .setNegativeButton("返回", null)
                        .show();
                idCardHeadIv.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
                nameEt.setText("");
                dateOfBirthEt.setText("");
                idNumberEt.setText("");
                addressEt.setText("");
            }
        }
    }
}
