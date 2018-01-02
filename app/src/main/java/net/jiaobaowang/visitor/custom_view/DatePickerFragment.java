package net.jiaobaowang.visitor.custom_view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import net.jiaobaowang.visitor.R;

import java.util.Calendar;
import java.util.Date;

/**
 * 日期选择Fragment
 * Created by rocka on 2017/12/29.
 */

public class DatePickerFragment extends DialogFragment {
    public static final String EXTRA_DATE = "net.jiaobaowang.visitor.sign.date";//传值时的
    private static final String ARG_SELECT_DATE = "selectDate";//选择日期
    private static final String ARG_START_DATE = "start_date";//开始日期
    private DatePicker mDatePicker;
    private Date mSelectDate;//选择时间
    private Date mStartDate;//开始时间

    /**
     *
     * @param selectDate 所选日期
     * @param minDate 最小日期
     * @return DatePickerPFragment
     */
    public static DatePickerFragment newInstance( Date selectDate, Date minDate) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_SELECT_DATE, selectDate);
        args.putSerializable(ARG_START_DATE, minDate);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSelectDate = (Date) getArguments().getSerializable(ARG_SELECT_DATE);
            mStartDate = (Date) getArguments().getSerializable(ARG_START_DATE);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_date_picker, null);
        mDatePicker = v.findViewById(R.id.date_picker);
        setMinDate();
        initialDatePicker();
        return new AlertDialog.Builder(getActivity()).setView(v).setTitle("日期选择").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendResult(Activity.RESULT_OK, getSelectDate());
            }
        }).create();
    }

    private void initialDatePicker() {
        if (mSelectDate == null) {
            return;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mSelectDate);
        mDatePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            }
        });
    }
    private void setMinDate(){
        if(mStartDate==null){
            return;
        }
        mDatePicker.setMinDate(mStartDate.getTime());
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
