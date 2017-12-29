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
import android.widget.Button;
import android.widget.TextView;

import net.jiaobaowang.visitor.R;
import net.jiaobaowang.visitor.base.BaseFragment;
import net.jiaobaowang.visitor.custom_view.DatePickerFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
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
        v.findViewById(R.id.sign_in_begin).setOnClickListener(this);
        v.findViewById(R.id.sign_in_end).setOnClickListener(this);
        v.findViewById(R.id.sign_off_begin).setOnClickListener(this);
        v.findViewById(R.id.sign_off_end).setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        int type = 0;
        Date beginDate = null;
        int code = 0;
        mSelectText = (TextView) v;
        switch (v.getId()) {
            case R.id.sign_in_begin://签到开始时间
                type = 0;
                code = REQUEST_SIBFGIN_CODE;
                break;
            case R.id.sign_in_end://签到结束时间
                type = 1;
                code = REQUEST_SIOFF_CODE;
                beginDate = mDateSIBegin;
                break;
            case R.id.sign_off_begin://签离开始时间
                type = 1;
                code = REQUEST_SOBFGIN_CODE;
                beginDate = mDateSIBegin;
                break;
            case R.id.sign_off_end://签离结束时间
                type = 1;
                code = REQUEST_SOOFF_CODE;
                beginDate = mDateSOBegin == null ? mDateSIBegin : mDateSOBegin;
                break;
            default:
                break;
        }
        showDialog(code, type, beginDate);
    }

    private void showDialog(int requestCode, int type, Date beginDate) {

        FragmentManager fragmentManager = getFragmentManager();
        DatePickerFragment dialog = DatePickerFragment.newInstance(type, beginDate);
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
