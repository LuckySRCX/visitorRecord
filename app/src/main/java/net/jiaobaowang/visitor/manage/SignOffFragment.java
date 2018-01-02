package net.jiaobaowang.visitor.manage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
 * 访客签离界面
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * <p>
 * to handle interaction events.
 * Use the {@link SignOffFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignOffFragment extends BaseFragment implements View.OnClickListener {
    private final int REQUEST_SIGN_BEGIN = 0;
    private final int REQUEST_SIGN_END = 1;
    private static final String DIALOG_DATE = "DialogDate";
    private Date mSartDate;
    private Date mEndDate;
    private TextView mSelectText;

    public SignOffFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SignOffFragment.
     */
    public static SignOffFragment newInstance() {
        return new SignOffFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sign_off, container, false);
        v.findViewById(R.id.sign_in_begin).setOnClickListener(this);
        v.findViewById(R.id.sign_in_end).setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        int requestCode = 0;
        Date selectDate = null;
        Date startDate = null;
        mSelectText = (TextView) v;
        switch (v.getId()) {
            case R.id.sign_in_begin:
                requestCode = REQUEST_SIGN_BEGIN;
                selectDate = mSartDate;
                break;
            case R.id.sign_in_end:
                requestCode = REQUEST_SIGN_END;
                selectDate = mEndDate;
                startDate = mSartDate;
                break;
            default:
                break;
        }
        showDialog(requestCode, selectDate, startDate);
    }

    private void showDialog(int requestCode, Date selectDate, Date startDate) {
        FragmentManager fragmentManager = getFragmentManager();
        DatePickerFragment dialog = DatePickerFragment.newInstance(selectDate, startDate);
        dialog.setTargetFragment(this, requestCode);
        dialog.show(fragmentManager, DIALOG_DATE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        Date resultDate = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
        switch (requestCode) {
            case REQUEST_SIGN_BEGIN:
                mSartDate = resultDate;
                break;
            case REQUEST_SIGN_END:
                mEndDate = resultDate;
                break;
            default:
                break;
        }
        mSelectText.setText(formatDate(resultDate));
    }

    private String formatDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return format.format(date);
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
