package net.jiaobaowang.visitor.manage;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import net.jiaobaowang.visitor.R;
import net.jiaobaowang.visitor.common.VisitorConfig;
import net.jiaobaowang.visitor.custom_view.DatePickerFragment;
import net.jiaobaowang.visitor.entity.SignOffResult;
import net.jiaobaowang.visitor.entity.VisitRecord;
import net.jiaobaowang.visitor.utils.TimeFormat;
import net.jiaobaowang.visitor.utils.TokenResetTask;
import net.jiaobaowang.visitor.utils.Tools;
import net.jiaobaowang.visitor.visitor_interface.TaskCallBack;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * 签离详情界面
 * Created by rocka on 2018/1/15.
 */

public class OffDetailFragment extends DialogFragment implements View.OnClickListener {
    private final static String EXTRA_ARGS_VISIT = "net.jiaobangwang.visitor.manage.OffDetailFragment.visit";
    public final static String EXTRA_IS_SIGN_OFF = "net.jiaobaowang.visitor.manage.offDetailFragment.isSignOff";
    private final static int REQUEST_OFF_CODE = 2;
    private final static String TAG_OFF_TIME_QUERY = "leaveTime";
    private VisitRecord mVisitRecord;
    private AlertDialog dialog;
    private TextView mOffTimeText;
    private Date mSelectLeaveTime;
    private OkHttpClient mOkHttpClient;
    private MyHandler mHandler;
    private ProgressDialog mDialog;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_off_detail, null, false);
        initialView(v);
        dialog = new AlertDialog.Builder(getActivity())
                .setView(v)
                .create();
        return dialog;
    }

    private void initialView(View v) {
        ((TextView) v.findViewById(R.id.off_detail_name)).setText(mVisitRecord.getVisitor_name());
        ((TextView) v.findViewById(R.id.off_detail_sex)).setText(mVisitRecord.getVisitor_sex() == 0 ? "女" : "男");
        ((TextView) v.findViewById(R.id.off_detail_cardType)).setText(mVisitRecord.getCertificate_type());
        ((TextView) v.findViewById(R.id.off_detail_cardNo)).setText(mVisitRecord.getCertificate_number());
        ((TextView) v.findViewById(R.id.off_detail_signIn)).setText(mVisitRecord.getIn_time());
        mOffTimeText = v.findViewById(R.id.off_detail_offTime);
        mSelectLeaveTime = new Date();
        mOffTimeText.setText(TimeFormat.formatTime(mSelectLeaveTime));
        ((TextView) v.findViewById(R.id.off_detail_checkTime)).setText(mVisitRecord.getCreate_time());
        setBeVisitor(v);
        new DownloadImageTask((ImageView) v.findViewById(R.id.off_detail_portrait)).execute(mVisitRecord.getImg_url());
        v.findViewById(R.id.off_detail_sure).setOnClickListener(this);
        v.findViewById(R.id.off_detail_cancel).setOnClickListener(this);
        v.findViewById(R.id.off_detail_offTime).setOnClickListener(this);
        v.findViewById(R.id.off_detail_offTimeContainer).setOnClickListener(this);
    }

    private void setBeVisitor(View v) {
        if (mVisitRecord.getInterviewee_type() == 0) {
            v.findViewById(R.id.tea_container).setVisibility(View.VISIBLE);
            v.findViewById(R.id.stu_container).setVisibility(View.GONE);
            ((TextView) v.findViewById(R.id.off_detail_beVisitor)).setText(mVisitRecord.getTeacher_name());
            ((TextView) v.findViewById(R.id.off_detail_beVisitorDepart)).setText(mVisitRecord.getDepartment_name());
        } else {
            v.findViewById(R.id.tea_container).setVisibility(View.GONE);
            v.findViewById(R.id.stu_container).setVisibility(View.VISIBLE);
            ((TextView) v.findViewById(R.id.off_detail_stuName)).setText(mVisitRecord.getStudent_name());
            ((TextView) v.findViewById(R.id.off_detail_stuClass)).setText(mVisitRecord.getClass_name());
            ((TextView) v.findViewById(R.id.off_detail_stuGrade)).setText(mVisitRecord.getGrade_name());
            ((TextView) v.findViewById(R.id.off_detail_stuHeaderTea)).setText(mVisitRecord.getHead_teacher_name());
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mVisitRecord = (VisitRecord) getArguments().getSerializable(EXTRA_ARGS_VISIT);
        }
        mOkHttpClient = new OkHttpClient();
        mHandler = new MyHandler(getActivity());
    }

    public static OffDetailFragment newInstance(VisitRecord visitRecord) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_ARGS_VISIT, visitRecord);
        OffDetailFragment fragment = new OffDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (dialog != null && dialog.getWindow() != null) {
            dialog.getWindow().setLayout(560, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.off_detail_sure:
                requestSignOff();
//                dialog.dismiss();
                break;
            case R.id.off_detail_cancel:
                dialog.dismiss();
                break;
            case R.id.off_detail_offTime:
                break;
            case R.id.off_detail_offTimeContainer:
//                dialog.dismiss();
                showTimePicker();
                break;
            default:
                break;
        }
    }

    private void requestSignOff() {
        mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage("加载中");
        mDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SharedPreferences sp = getActivity().getSharedPreferences(VisitorConfig.VISIT_LOCAL_STORAGE, MODE_PRIVATE);
                    FormBody body = new FormBody.Builder()
                            .add("token", sp.getString(VisitorConfig.VISIT_LOCAL_TOKEN, ""))
                            .add("uuid", Tools.getDeviceId(getActivity()))
                            .add("utid", String.valueOf(sp.getInt(VisitorConfig.VISIT_LOCAL_USERINFO_UTID, 0)))
                            .add("utname", sp.getString(VisitorConfig.VISIT_LOCAL_USERINFO_UTNAME, ""))
                            .add("schid", String.valueOf(sp.getInt(VisitorConfig.VISIT_LOCAL_SCHOOL_ID, 0)))
                            .add("id", mVisitRecord.getId() + "")
                            .add("leave_time", TimeFormat.formatTime(mSelectLeaveTime)).build();
                    Request request = new Request.Builder().url(VisitorConfig.VISITOR_API_LEAVE).post(body).build();
                    Response response = mOkHttpClient.newCall(request).execute();
                    if (!response.isSuccessful()) {
                        mHandler.sendEmptyMessage(-1);
                        throw new IOException("Exception" + response);
                    } else {
                        dealResult(response.body().string());
                    }
                } catch (Exception e) {
                    Log.e("ERROR", "获取信息错误", e);
                    mHandler.sendEmptyMessage(-1);
                }
            }
        }).start();
    }

    SignOffResult result;

    private void dealResult(String string) {
        Gson gson = new Gson();
        result = gson.fromJson(string, SignOffResult.class);
        switch (result.getCode()) {
            case "0000":
                mHandler.sendEmptyMessage(0);
                break;
            case "0006":
                new TokenResetTask(getActivity(), mOkHttpClient, new TaskCallBack() {
                    @Override
                    public void CallBack(String[] result) {
                        if (result[1].equals("1")) {
                            requestSignOff();
                        } else {
                            mHandler.sendEmptyMessage(6);
                        }
                    }
                });
                break;
            default:
                mHandler.sendEmptyMessage(-1);
                break;

        }
    }

    class MyHandler extends Handler {
        private Context mContext;

        MyHandler(Context context) {
            super();
            mContext = context;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    if (result != null && result.getMsg() != null) {
                        Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_LONG).show();
                    }
                    break;
                case 0:
                    sendResult(true);
                    Toast.makeText(getActivity(), "签离成功", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    break;
                case 1:
                    sendResult(false);
                    break;
                case 6://token续订错误
                    Toast.makeText(getActivity(), "服务器内部错误,请重新登录", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
        }
    }

    private void sendResult(boolean b) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_IS_SIGN_OFF, b);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }

    private void showTimePicker() {
        FragmentManager fragmentManager = getFragmentManager();
        DatePickerFragment fragment = (DatePickerFragment) fragmentManager.findFragmentByTag(TAG_OFF_TIME_QUERY);
        if (fragment != null) {
            return;
        }
        DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(1, mSelectLeaveTime, TimeFormat.getDateFromFormatTime(mVisitRecord.getIn_time()));
        datePickerFragment.setTargetFragment(OffDetailFragment.this, REQUEST_OFF_CODE);
        datePickerFragment.show(fragmentManager, TAG_OFF_TIME_QUERY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        mSelectLeaveTime = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
        switch (requestCode) {
            case REQUEST_OFF_CODE:
                mOffTimeText.setText(TimeFormat.formatTime(mSelectLeaveTime));
                break;
            default:
                break;
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", "获取图片错误", e);
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
