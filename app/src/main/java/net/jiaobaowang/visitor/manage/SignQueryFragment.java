package net.jiaobaowang.visitor.manage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.jiaobaowang.visitor.R;
import net.jiaobaowang.visitor.base.BaseFragment;
import net.jiaobaowang.visitor.custom_view.DatePickerFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 访客查询界面
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SignQueryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignQueryFragment extends BaseFragment implements View.OnClickListener {
    private Date mDateSIBegin;//签到开始时间
    private Date mDateSIEnd;//签到结束时间
    private Date mDateSOBegin;//签离开始时间
    private Date mDateSOEnd;//签离结束时间
    private TextView mSelectText;
    private final int REQUEST_SIBFGIN_CODE = 0;
    private final int REQUEST_SIOFF_CODE = 1;
    private final int REQUEST_SOBFGIN_CODE = 2;
    private final int REQUEST_SOOFF_CODE = 3;
    private static final String DIALOG_DATE = "DialogDate";

    public SignQueryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SignQueryFragment.
     */
    public static SignQueryFragment newInstance() {
        return new SignQueryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_query, container, false);
        setTextView((TextView) v.findViewById(R.id.sign_in_begin), mDateSIBegin);
        setTextView((TextView) v.findViewById(R.id.sign_in_end), mDateSIEnd);
        setTextView((TextView) v.findViewById(R.id.sign_off_begin), mDateSOBegin);
        setTextView((TextView) v.findViewById(R.id.sign_off_end), mDateSOEnd);
        return v;
    }

    private void setTextView(TextView view, Date date) {
        if (date != null) {
            view.setText(formatDate(date));
        }
        view.setOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        Date minDate = null;
        Date selectDate = null;
        int code = 0;
        mSelectText = (TextView) v;
        switch (v.getId()) {
            case R.id.sign_in_begin://签到开始时间
                selectDate = mDateSIBegin;
                code = REQUEST_SIBFGIN_CODE;
                break;
            case R.id.sign_in_end://签到结束时间

                selectDate = mDateSIEnd;
                code = REQUEST_SIOFF_CODE;
                minDate = mDateSIBegin;
                break;
            case R.id.sign_off_begin://签离开始时间

                selectDate = mDateSOBegin;
                code = REQUEST_SOBFGIN_CODE;
                minDate = mDateSIBegin;
                break;
            case R.id.sign_off_end://签离结束时间

                selectDate = mDateSOEnd;
                code = REQUEST_SOOFF_CODE;
                minDate = mDateSOBegin == null ? mDateSIBegin : mDateSOBegin;
                break;
            default:
                break;
        }
        showDialog(code, selectDate, minDate);
    }

    /**
     *
     * @param requestCode 请求代码
     * @param selectDate 已选日期
     * @param beginDate 最小日期
     */
    private void showDialog(int requestCode,  Date selectDate, Date beginDate) {
        FragmentManager fragmentManager = getFragmentManager();
        DatePickerFragment dialog = DatePickerFragment.newInstance(selectDate, beginDate);
        dialog.setTargetFragment(SignQueryFragment.this, requestCode);
        dialog.show(fragmentManager, DIALOG_DATE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        Log.d(TAG, "获取的日期:" + String.valueOf(data.getSerializableExtra(DatePickerFragment.EXTRA_DATE)));
        Date resultDate = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
        switch (requestCode) {
            case REQUEST_SIBFGIN_CODE:
                mDateSIBegin = resultDate;
                break;
            case REQUEST_SIOFF_CODE:
                mDateSIEnd = resultDate;
                break;
            case REQUEST_SOBFGIN_CODE:
                mDateSOBegin = resultDate;
                break;
            case REQUEST_SOOFF_CODE:
                mDateSOEnd = resultDate;
                break;
            default:
                break;
        }
        mSelectText.setText(formatDate(resultDate));
    }

    /**
     * 格式化 日期
     * @param date 日期
     * @return 返回 yyyy-MM-dd 格式的时间字符串
     */
    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(date);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
