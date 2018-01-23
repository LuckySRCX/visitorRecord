package net.jiaobaowang.visitor.manage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.telpo.tps550.api.idcard.IdentityInfo;

import net.jiaobaowang.visitor.Listener.OnLoadMoreListener;
import net.jiaobaowang.visitor.R;
import net.jiaobaowang.visitor.base.BaseFragment;
import net.jiaobaowang.visitor.common.VisitorConfig;
import net.jiaobaowang.visitor.custom_view.DatePickerFragment;
import net.jiaobaowang.visitor.entity.ListResult;
import net.jiaobaowang.visitor.entity.OffRecordLab;
import net.jiaobaowang.visitor.entity.VisitRecord;
import net.jiaobaowang.visitor.utils.DialogUtils;
import net.jiaobaowang.visitor.visitor_interface.OnGetIdentityInfoListener;
import net.jiaobaowang.visitor.visitor_interface.OnGetIdentityInfoResult;
import net.jiaobaowang.visitor.visitor_interface.OnGetQRCodeListener;
import net.jiaobaowang.visitor.visitor_interface.OnGetQRCodeResult;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 访客签离界面
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * <p>
 * to handle interaction events.
 * Use the {@link SignOffFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignOffFragment extends BaseFragment implements View.OnClickListener, OnGetIdentityInfoResult, OnGetQRCodeResult {
    private Date mDateSIBegin;//签到开始时间
    private Date mDateSIEnd;//签到结束时间
    private TextView mSelectText;
    private TextView mSignInBegin;
    private TextView mSignInEnd;
    private final int REQUEST_SIBFGIN_CODE = 0;
    private final int REQUEST_SIOFF_CODE = 1;
    private final int REQUEST_SIGN_OFF_CODE = 3;
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_VISIT = "DialogVisit";
    private RecyclerView mRecyclerView;
    private OffRecyclerAdapter mRecyclerAdapter;
    private EditText mText_keywords;
    private Spinner mSpinner_identity;
    private String mToken;
    private int pageIndex = 1;
    private int pageSize = 20;
    private boolean isLastPage;
    private MyHandler mMyHandler;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private VisitRecord mVisitRecord;
    private OnGetIdentityInfoListener onGetIdentityInfoListener;
    private OnGetQRCodeListener onGetQRCodeListener;
    private ProgressDialog mDialog;

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
        mMyHandler = new MyHandler(SignOffFragment.this.getActivity());
        mToken = getActivity().getSharedPreferences(VisitorConfig.VISIT_LOCAL_STORAGE, Context.MODE_PRIVATE).getString(VisitorConfig.VISIT_LOCAL_TOKEN, "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_off, container, false);
        mSignInBegin = v.findViewById(R.id.sign_in_begin);
        mSignInEnd = v.findViewById(R.id.sign_in_end);
        setTextView(mSignInBegin, mDateSIBegin);
        setTextView(mSignInEnd, mDateSIEnd);
        v.findViewById(R.id.sign_in_beginContainer).setOnClickListener(this);
        v.findViewById(R.id.sign_in_endContainer).setOnClickListener(this);
        v.findViewById(R.id.back_up).setOnClickListener(this);
        v.findViewById(R.id.btn_query).setOnClickListener(this);
        v.findViewById(R.id.qrcode_btn).setOnClickListener(this);
        v.findViewById(R.id.id_card_read_btn).setOnClickListener(this);
        mRecyclerView = v.findViewById(R.id.recycler_query);
        v.findViewById(R.id.leave_time).setVisibility(View.GONE);
        mText_keywords = v.findViewById(R.id.edit_keywords);
        mSpinner_identity = v.findViewById(R.id.spinner_identity);
        setSpinner(mSpinner_identity, R.array.person_identity);
        onGetIdentityInfoListener = (OnGetIdentityInfoListener) getActivity();
        onGetQRCodeListener = (OnGetQRCodeListener) getActivity();
        queryRecords();
        return v;
    }

    private void setSpinner(Spinner spinner, int resId) {
        String[] options = getResources().getStringArray(resId);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.visit_spinner_item, options);
        adapter.setDropDownViewResource(R.layout.visit_drop_down_item);
        spinner.setAdapter(adapter);
    }

    private void updateUI() {
        OffRecordLab recordLab = OffRecordLab.get(getActivity());
        List<VisitRecord> records = recordLab.getVisitRecords();
        mRecyclerAdapter = new OffRecyclerAdapter(mRecyclerView, records);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        setListener(records);
    }

    private void setListener(final List<VisitRecord> records) {
        mRecyclerAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.d("获取的信息:", "loadMore");
                records.add(null);
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerAdapter.notifyItemInserted(records.size() - 1);
                    }
                });
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        records.remove(records.size() - 1);
                        queryRecords();
                    }
                }, 1000);
            }
        });
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
        Date minDate;
        Date selectDate;
        int code;

        switch (v.getId()) {
            case R.id.sign_in_beginContainer:
            case R.id.sign_in_begin://签到开始时间
                mSelectText = mSignInBegin;
                selectDate = mDateSIBegin;
                code = REQUEST_SIBFGIN_CODE;
                showDialog(code, selectDate, null);
                break;
            case R.id.sign_in_endContainer:
            case R.id.sign_in_end://签到结束时间
                mSelectText = mSignInEnd;
                selectDate = mDateSIEnd;
                code = REQUEST_SIOFF_CODE;
                minDate = mDateSIBegin;
                showDialog(code, selectDate, minDate);
                break;
            case R.id.back_up:
                getActivity().onBackPressed();
                break;
            case R.id.btn_query:
                pageIndex = 1;
                queryRecords();
                break;
            case R.id.qrcode_btn:
                onGetQRCodeListener.getQRCode();
                break;
            case R.id.id_card_read_btn:
                onGetIdentityInfoListener.getIdentityInfo();
                break;
            default:
                break;
        }

    }

    private void queryRecords() {
        mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage("加载中...");
        mDialog.show();
        final String keywords = mText_keywords.getText().toString().trim();
        final int identityType = mSpinner_identity.getSelectedItemPosition() - 1;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RequestBody body = new FormBody.Builder()
                            .add("token", mToken)
                            .add("pageNumber", pageIndex + "")
                            .add("pageSize", pageSize + "")
                            .add("keyword", keywords)
                            .add("leave_flag", 0 + "")
                            .add("interviewee_type", identityType + "")
                            .add("in_start_time", formatDate(mDateSIBegin) == null ? "" : formatDate(mDateSIBegin))
                            .add("in_end_time", formatDate(mDateSIEnd) == null ? "" : formatDate(mDateSIEnd)).build();
                    Request request = new Request.Builder().url(VisitorConfig.VISITOR_API_LIST).post(body).build();
                    Response response = okHttpClient.newCall(request).execute();
                    if (!response.isSuccessful()) {
                        mMyHandler.sendEmptyMessage(-1);
                        throw new IOException("Exception" + response);
                    } else {
                        resultDealt(response.body().string());
                    }
                } catch (Exception e) {
                    mMyHandler.sendEmptyMessage(-1);
                    Log.d("ERROR", "请求数据错误", e);
                }
            }
        }).start();
    }

    ListResult listResult;

    private void resultDealt(String string) {
        Log.d(TAG, string);
        Gson gson = new Gson();
        listResult = gson.fromJson(string, ListResult.class);
        if (listResult.getCode().equals("0000")) {
            isLastPage = listResult.getData().isLastPage();
            if (pageIndex == 1) {
                OffRecordLab.get(getActivity()).setVisitRecords(listResult.getData().getList());
                mMyHandler.sendEmptyMessage(0);
            } else {
                OffRecordLab.get(getActivity()).addVisitRecords(listResult.getData().getList());
                mMyHandler.sendEmptyMessage(1);
            }
            if (!isLastPage) {
                pageIndex++;
            }
        } else {
            mMyHandler.sendEmptyMessage(-1);
        }
    }

    /**
     * 返回身份证信息和身份证头像
     *
     * @param code          0，失败；1，成功
     * @param msg           失败信息
     * @param identityInfo  身份证信息
     * @param identityImage 身份证头像
     */
    @Override
    public void getIdentityInfoResult(int code, String msg, IdentityInfo identityInfo, Bitmap identityImage) {
        Log.i(TAG, "getIdentityInfoResult");
        if (code == 0) {
            //失败
            DialogUtils.showAlert(getActivity(), msg);
        } else {

        }
    }

    /**
     * 返回条码/二维码
     *
     * @param code   0，失败；1，成功
     * @param msg    失败信息
     * @param qrCode 条码/二维码
     */
    @Override
    public void getQRCodeResult(int code, String msg, String qrCode) {
        Log.i(TAG, "getIdentityInfoResult:" + qrCode);
        if (code == 0) {
            //失败
            DialogUtils.showAlert(getActivity(), msg);
        } else {

        }
    }

    /**
     * MyHandler
     */
    private class MyHandler extends Handler {
        private Activity mContext;

        MyHandler(Activity context) {
            mContext = context;
        }

        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG, "获取的信息为：" + String.valueOf(msg.what));
            switch (msg.what) {
                case -1:
                    if (listResult != null && listResult.getMsg() != null) {
                        Toast.makeText(getActivity(), listResult.getMsg(), Toast.LENGTH_LONG).show();
                    }
                    break;
                case 0:
                    Log.d(TAG, OffRecordLab.get(mContext).getVisitRecords().toString());
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    updateUI();
                    break;
                case 1:
                    mRecyclerAdapter.notifyDataSetChanged();
                    mRecyclerAdapter.setLoaded();
                    break;
                default:
                    break;
            }
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
        }
    }

    /**
     * @param requestCode 请求代码
     * @param selectDate  已选日期
     * @param beginDate   最小日期
     */
    private void showDialog(int requestCode, Date selectDate, Date beginDate) {
        FragmentManager fragmentManager = getFragmentManager();
        DatePickerFragment fragment = (DatePickerFragment) fragmentManager.findFragmentByTag(DIALOG_DATE);
        if (fragment != null) {
            return;
        }
        DatePickerFragment dialog = DatePickerFragment.newInstance(0, selectDate, beginDate);
        dialog.setTargetFragment(SignOffFragment.this, requestCode);
        dialog.show(fragmentManager, DIALOG_DATE);
    }

    private void showDetail(VisitRecord record) {

        FragmentManager fragmentManager = getFragmentManager();
        OffDetailFragment fragment = (OffDetailFragment) fragmentManager.findFragmentByTag(DIALOG_VISIT);
        if (fragment != null) {
            return;
        }
        OffDetailFragment offDetailFragment = OffDetailFragment.newInstance(record);
        offDetailFragment.setTargetFragment(SignOffFragment.this, REQUEST_SIGN_OFF_CODE);
        offDetailFragment.show(fragmentManager, DIALOG_VISIT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        Log.d(TAG, "获取的日期:" + String.valueOf(data.getSerializableExtra(DatePickerFragment.EXTRA_DATE)));

        switch (requestCode) {
            case REQUEST_SIBFGIN_CODE:
                setTime(mDateSIBegin, data);
                break;
            case REQUEST_SIOFF_CODE:
                setTime(mDateSIEnd, data);
                break;
            case REQUEST_SIGN_OFF_CODE:
                changeVisitorRecord(data);
                break;
            default:
                break;
        }

    }

    private void changeVisitorRecord(Intent data) {
        boolean isLeave = data.getBooleanExtra(OffDetailFragment.EXTRA_IS_SIGN_OFF, false);
        mVisitRecord.setLeave_flag(isLeave);
        mRecyclerAdapter.notifyDataSetChanged();
    }

    private void setTime(Date date, Intent data) {
        date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
        mSelectText.setText(formatDate(date));
    }


    /**
     * 格式化 日期
     *
     * @param date 日期
     * @return 返回 yyyy-MM-dd 格式的时间字符串
     */
    private String formatDate(Date date) {
        if (date == null) {
            return null;
        }
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

    class OffViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private LinearLayout mCellContainer;
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


        OffViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.visit_record_item, parent, false));
            mCellContainer = itemView.findViewById(R.id.cell_container);
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
            mLeaveTime.setVisibility(View.GONE);
            mIconPrint.setVisibility(View.GONE);
            mIconDetail.setImageResource(R.mipmap.ic_quit);
        }

        void bind(VisitRecord record, int position) {
            mVisitorName.setText(record.getVisitor_name());
            mVisitorCounter.setText(record.getVisitor_counter());
            mVisitReason.setText(record.getNote());
            mDepartName.setText(record.getDepartment_name());
            mTeaName.setText(record.getTeacher_name());
            mGradeName.setText(record.getGrade_name());
            mClassName.setText(record.getClass_name());
            mStuName.setText(record.getStudent_name());
            mHeadTeaName.setText(record.getHead_teacher_name());
            mInTime.setText(record.getIn_time());
            mLeaveTime.setText(record.getLeave_time());
            if (record.isLeave_flag()) {
                mIsLeft.setText("是");
                mIsLeft.setTextColor(getResources().getColor(R.color.is_leave));
            } else {
                mIsLeft.setText("否");
                mIsLeft.setTextColor(getResources().getColor(R.color.is_not_leave));
            }
            if (position % 2 == 1) {
                mCellContainer.setBackground(getResources().getDrawable(R.drawable.visit_record_item_dark));
            } else {
                mCellContainer.setBackground(getResources().getDrawable(R.drawable.visit_record_item));
            }
            mCellContainer.setTag(record);
            mCellContainer.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //访问记录
            VisitRecord record = (VisitRecord) v.getTag();
            switch (v.getId()) {
                case R.id.cell_container:
                    mVisitRecord = record;
                    showDetail(record);
                    break;
                default:
                    break;
            }
        }
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar mBar;

        LoadingViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_loading, parent, false));
            mBar = itemView.findViewById(R.id.progress_bar);
        }

        void bind() {
            mBar.setIndeterminate(true);
        }
    }

    private boolean isLoading;
    private int lastVisibleItem, totalItemCount;

    class OffRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<VisitRecord> mVisitRecords;
        private final int VIEW_TYPE_ITEM = 0;
        private final int VIEW_TYPE_LOADING = 1;
        private OnLoadMoreListener mLoadMoreListener;

        void setOnLoadMoreListener(OnLoadMoreListener listener) {
            mLoadMoreListener = listener;
        }

        OffRecyclerAdapter(RecyclerView recyclerView, List<VisitRecord> records) {
            mVisitRecords = records;
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    totalItemCount = manager.getItemCount();
                    lastVisibleItem = manager.findLastVisibleItemPosition();
                    if (!isLoading && totalItemCount <= (lastVisibleItem + 1)) {
                        if (isLastPage) {
                            return;
                        }
                        if (mLoadMoreListener != null) {
                            isLoading = true;
                            mLoadMoreListener.onLoadMore();
                        }

                    }

                }
            });
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == VIEW_TYPE_ITEM) {
                return new OffViewHolder(LayoutInflater.from(getActivity()), parent);
            } else if (viewType == VIEW_TYPE_LOADING) {
                return new LoadingViewHolder(LayoutInflater.from(getActivity()), parent);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            VisitRecord record = mVisitRecords.get(position);
            if (holder instanceof OffViewHolder) {
                ((OffViewHolder) holder).bind(record, position);
            } else if (holder instanceof LoadingViewHolder) {
                ((LoadingViewHolder) holder).bind();
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (mVisitRecords.get(position) == null) {
                return VIEW_TYPE_LOADING;
            } else {
                return VIEW_TYPE_ITEM;
            }
        }

        @Override
        public int getItemCount() {
            return mVisitRecords.size();
        }

        void setLoaded() {
            isLoading = false;
        }
    }

}
