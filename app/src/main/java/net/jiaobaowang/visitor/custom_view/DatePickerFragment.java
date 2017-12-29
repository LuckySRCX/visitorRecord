package net.jiaobaowang.visitor.custom_view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import net.jiaobaowang.visitor.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by rocka on 2017/12/29.
 */

public class DatePickerFragment extends DialogFragment {
    public static final String EXTRA_DATE = "net.jiaobaowang.visitor.sign.date";
    private DatePicker mDatePicker;

    /**
     * 实例方法
     *
     * @param dateType  要获取的日期类型 0 不限制日期 1 限制日期
     * @param startDate 限制选取的最早时间
     * @return
     */
    public static DatePickerFragment newInstance(int dateType, Date startDate) {
        Bundle args = new Bundle();
        args.putSerializable("dateType", dateType);
        args.putSerializable("startDate", startDate);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_date_picker, null);
        mDatePicker = v.findViewById(R.id.date_picker);
        return new AlertDialog.Builder(getActivity()).setView(v).setTitle("日期选择").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendResult(Activity.RESULT_OK, getSelectDate());
            }
        }).create();
    }

    private Date getSelectDate() {
        int year = mDatePicker.getYear();
        int month = mDatePicker.getMonth();
        int day = mDatePicker.getDayOfMonth();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTime();
    }

    private void sendResult(int code, Date date) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), code, intent);
    }
}
