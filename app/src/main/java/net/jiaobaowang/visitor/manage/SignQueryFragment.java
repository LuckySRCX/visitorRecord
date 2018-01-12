package net.jiaobaowang.visitor.manage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.jiaobaowang.visitor.R;
import net.jiaobaowang.visitor.base.BaseFragment;
import net.jiaobaowang.visitor.custom_view.DatePickerFragment;
import net.jiaobaowang.visitor.entity.VisitRecord;
import net.jiaobaowang.visitor.entity.VisitRecordLab;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
    private TextView mSelectText;
    private final int REQUEST_SIBFGIN_CODE = 0;
    private final int REQUEST_SIOFF_CODE = 1;
    private static final String DIALOG_DATE = "DialogDate";
    private RecyclerView mRecyclerView;
    private QueryRecyclerAdapter mRecyclerAdapter;

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
        v.findViewById(R.id.back_up).setOnClickListener(this);
        mRecyclerView = v.findViewById(R.id.recycler_query);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return v;
    }

    private void updateUI() {
        VisitRecordLab recordLab = VisitRecordLab.get(getActivity());
        List<VisitRecord> records = recordLab.getVisitRecords();
        mRecyclerAdapter = new QueryRecyclerAdapter(records);
        mRecyclerView.setAdapter(mRecyclerAdapter);
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

        switch (v.getId()) {
            case R.id.sign_in_begin://签到开始时间
                mSelectText = (TextView) v;
                selectDate = mDateSIBegin;
                code = REQUEST_SIBFGIN_CODE;
                showDialog(code, selectDate, minDate);
                break;
            case R.id.sign_in_end://签到结束时间
                mSelectText = (TextView) v;
                selectDate = mDateSIEnd;
                code = REQUEST_SIOFF_CODE;
                minDate = mDateSIBegin;
                showDialog(code, selectDate, minDate);
                break;
            case R.id.back_up:
                getActivity().onBackPressed();
                break;
            default:
                break;
        }

    }

    /**
     * @param requestCode 请求代码
     * @param selectDate  已选日期
     * @param beginDate   最小日期
     */
    private void showDialog(int requestCode, Date selectDate, Date beginDate) {
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
            default:
                break;
        }
        mSelectText.setText(formatDate(resultDate));
    }

    /**
     * 格式化 日期
     *
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

    class QueryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private LinearLayout mCellConatiner;
        private TextView mVisitorName;
        private TextView mVisitorCounter;
        private TextView mVisitReason;
        private TextView mDepartName;
        private TextView mTeaName;
        private TextView mGradeName;
        private TextView mClassName;
        private TextView mStuName;
        private TextView mHeadTeaName;
        private TextView mInTime;
        private TextView mIsLeft;
        private TextView mLeaveTime;
        private ImageView mIconDetail;
        private ImageView mIconPrint;


        public QueryViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.visit_record_item, parent, false));
            mCellConatiner = itemView.findViewById(R.id.cell_container);
            mVisitorName = itemView.findViewById(R.id.visitor_name);
            mVisitorCounter = itemView.findViewById(R.id.visitor_counter);
            mVisitReason = itemView.findViewById(R.id.visit_reason);
            mDepartName = itemView.findViewById(R.id.depart_name);
            mTeaName = itemView.findViewById(R.id.tea_name);
            mGradeName = itemView.findViewById(R.id.grade_name);
            mClassName = itemView.findViewById(R.id.class_name);
            mStuName = itemView.findViewById(R.id.stu_name);
            mHeadTeaName = itemView.findViewById(R.id.headTea_name);
            mInTime = itemView.findViewById(R.id.in_time);
            mIsLeft = itemView.findViewById(R.id.is_left);
            mLeaveTime = itemView.findViewById(R.id.leave_time);
            mIconDetail = itemView.findViewById(R.id.icon_detail);
            mIconPrint = itemView.findViewById(R.id.icon_print);

        }

        public void bind(VisitRecord record, int position) {
            mVisitorName.setText(record.getVisitor_name());
//            mVisitorCounter.setText(record.getVisitor_counter());
//            mVisitReason.setText(record.getNote());
//            mDepartName.setText(record.getDepartment_name());
//            mTeaName.setText(record.getTeacher_name());
//            mGradeName.setText(record.getGrade_name());
//            mClassName.setText(record.getClass_name());
//            mStuName.setText(record.getStudent_name());
//            mHeadTeaName.setText(record.getHead_teacher_name());
//            mInTime.setText(record.getIn_time());
//            mLeaveTime.setText(record.getLeave_time());
            if (position % 2 == 1) {
                mCellConatiner.setBackground(getResources().getDrawable(R.drawable.visit_record_item_dark));
            } else {
                mCellConatiner.setBackground(getResources().getDrawable(R.drawable.visit_record_item));
            }
            mIconDetail.setTag(record);
            mIconPrint.setTag(record);
            mIconDetail.setOnClickListener(this);
            mIconPrint.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //访问记录
            VisitRecord record = (VisitRecord) v.getTag();
            switch (v.getId()) {
                case R.id.icon_detail://详情按钮点击事件
                    //todo 传递record并跳转至访问详情
                    break;
                case R.id.icon_print://打印按钮点击事件
                    //todo 传递record并跳转至打印界面
                    break;
                default:
                    break;
            }
        }
    }

    class QueryRecyclerAdapter extends RecyclerView.Adapter<QueryViewHolder> {
        List<VisitRecord> mVisitRecords;

        public QueryRecyclerAdapter(List<VisitRecord> records) {
            mVisitRecords = records;
        }

        @Override
        public QueryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new QueryViewHolder(LayoutInflater.from(getActivity()), parent);
        }

        @Override
        public void onBindViewHolder(QueryViewHolder holder, int position) {
            VisitRecord record = mVisitRecords.get(position);
            holder.bind(record, position);
        }

        @Override
        public int getItemCount() {
            return mVisitRecords.size();
        }
    }
}
