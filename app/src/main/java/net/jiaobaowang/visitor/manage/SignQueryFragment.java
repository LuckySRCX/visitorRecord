package net.jiaobaowang.visitor.manage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import net.jiaobaowang.visitor.Listener.OnLoadMoreListener;
import net.jiaobaowang.visitor.R;
import net.jiaobaowang.visitor.base.BaseFragment;
import net.jiaobaowang.visitor.common.VisitorConfig;
import net.jiaobaowang.visitor.common.VisitorConstant;
import net.jiaobaowang.visitor.custom_view.DatePickerFragment;
import net.jiaobaowang.visitor.entity.ListResult;
import net.jiaobaowang.visitor.entity.VisitRecord;
import net.jiaobaowang.visitor.entity.VisitRecordLab;
import net.jiaobaowang.visitor.printer.PrinterActivity;
import net.jiaobaowang.visitor.printer.VisitorFormDetailsActivity;
import net.jiaobaowang.visitor.utils.TokenResetTask;
import net.jiaobaowang.visitor.utils.Tools;
import net.jiaobaowang.visitor.visitor_interface.TaskCallBack;

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

import static android.content.Context.MODE_PRIVATE;

/**
 * 访客查询界面
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SignQueryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignQueryFragment extends BaseFragment implements View.OnClickListener {
    private static SignQueryFragment mQueryFragment;
    private static final String OUT_STAT_PAGE = "sign_query_fragment.page";
    private Date mDateSIBegin;//签到开始时间
    private Date mDateSIEnd;//签到结束时间
    private LinearLayout mQueryContainer;
    private TextView mSignInBegin;
    private TextView mSignInEnd;
    private TextView mSelectText;
    private EditText mText_keywords;
    private Spinner mSpinner_visitorState;
    private Spinner mSpinner_identity;
    private SwipeRefreshLayout mRefreshLayout;
    private final int REQUEST_SIBFGIN_CODE = 0;
    private final int REQUEST_SIOFF_CODE = 1;
    private static final String DIALOG_DATE = "DialogDate";

    private RecyclerView mRecyclerView;
    private QueryRecyclerAdapter mRecyclerAdapter;

    private int pageIndex = 1;
    private int pageSize = 20;
    private boolean isLastPage;
    OkHttpClient okHttpClient = new OkHttpClient();
    MyHandler mMyHandler;
    private ProgressDialog mDialog;
    private boolean mIsVisible;

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
        if (mQueryFragment == null) {
            mQueryFragment = new SignQueryFragment();
        }
        return mQueryFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMyHandler = new MyHandler(SignQueryFragment.this.getActivity());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(OUT_STAT_PAGE, pageIndex);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            pageIndex = 1;
            queryRecords();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_query, container, false);
        mQueryContainer = v.findViewById(R.id.sign_query_container);
        mSignInBegin = v.findViewById(R.id.sign_in_begin);
        mSignInEnd = v.findViewById(R.id.sign_in_end);
        setTextView(mSignInBegin, mDateSIBegin);
        setTextView(mSignInEnd, mDateSIEnd);
        v.findViewById(R.id.sign_in_beginContainer).setOnClickListener(this);
        v.findViewById(R.id.sign_in_endContainer).setOnClickListener(this);
        v.findViewById(R.id.back_up).setOnClickListener(this);
        v.findViewById(R.id.btn_query).setOnClickListener(this);
        mText_keywords = v.findViewById(R.id.edit_keywords);
        mSpinner_identity = v.findViewById(R.id.spinner_identity);
        mSpinner_visitorState = v.findViewById(R.id.spinner_visitorState);
        setSpinner(mSpinner_identity, R.array.person_identity);
        setSpinner(mSpinner_visitorState, R.array.is_leave_option);
        mRecyclerView = v.findViewById(R.id.recycler_query);
        mRefreshLayout = v.findViewById(R.id.swipe_refresh);
        setRefreshListener();
        queryRecords();
        return v;
    }

    private void setRefreshListener() {
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent,R.color.colorRed);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageIndex = 1;
                queryRecords();
            }
        });
    }

    private void setSpinner(Spinner spinner, int resId) {
        String[] options = getResources().getStringArray(resId);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.visit_spinner_item, options);
        adapter.setDropDownViewResource(R.layout.visit_drop_down_item);
        spinner.setAdapter(adapter);
    }


    private void updateUI() {
        VisitRecordLab recordLab = VisitRecordLab.get(getActivity());
        List<VisitRecord> records = recordLab.getVisitRecords();
        mRecyclerAdapter = new QueryRecyclerAdapter(mRecyclerView, records);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        setListener(records);
    }

    private void setListener(final List<VisitRecord> records) {
        mRecyclerAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                records.add(null);
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerAdapter.notifyItemInserted(records.size() - 1);
                    }
                });

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        records.remove(records.size() - 1);
                        queryRecords();
                    }
                });
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
    public void onClick(View v) {
        Date minDate = null;
        Date selectDate;
        int code;

        switch (v.getId()) {
            case R.id.sign_in_beginContainer:
            case R.id.sign_in_begin://签到开始时间
                mSelectText = mSignInBegin;
                selectDate = mDateSIBegin;
                code = REQUEST_SIBFGIN_CODE;
                showDialog(code, selectDate, minDate);
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
            default:
                break;
        }
        requireFocus(v);

    }

    private void requireFocus(View v) {
        v.setFocusable(true);
        v.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    private void queryRecords() {
        final String keywords = mText_keywords.getText().toString().trim();
        final int leaveFlag = mSpinner_visitorState.getSelectedItemPosition() - 1;
        final int identityType = mSpinner_identity.getSelectedItemPosition() - 1;
        mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage("加载中...");
        mDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    SharedPreferences sp = getActivity().getSharedPreferences(VisitorConfig.VISIT_LOCAL_STORAGE, MODE_PRIVATE);
                    RequestBody body = new FormBody.Builder()
                            .add("token", sp.getString(VisitorConfig.VISIT_LOCAL_TOKEN, ""))
                            .add("uuid", Tools.getDeviceId(getActivity()))
                            .add("utid", String.valueOf(sp.getInt(VisitorConfig.VISIT_LOCAL_USERINFO_UTID, 0)))
                            .add("utname", sp.getString(VisitorConfig.VISIT_LOCAL_USERINFO_UTNAME, ""))
                            .add("schid", String.valueOf(sp.getInt(VisitorConfig.VISIT_LOCAL_SCHOOL_ID, 0)))
                            .add("pageNumber", pageIndex + "")
                            .add("pageSize", pageSize + "")
                            .add("keyword", keywords)
                            .add("leave_flag", leaveFlag + "")
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
        switch (listResult.getCode()) {
            case "0000":
                isLastPage = listResult.getData().isLastPage();
                if (pageIndex == 1) {
                    VisitRecordLab.get(getActivity()).setVisitRecords(listResult.getData().getList());
                    mMyHandler.sendEmptyMessage(0);
                } else {
                    VisitRecordLab.get(getActivity()).addVisitRecords(listResult.getData().getList());
                    mMyHandler.sendEmptyMessage(1);
                }
                if (!isLastPage) {
                    pageIndex++;
                }
                break;
            case "0006":
                new TokenResetTask(getActivity(), okHttpClient, new TaskCallBack() {
                    @Override
                    public void CallBack(String[] result) {
                        if (result[1].equals("1")) {
                            queryRecords();
                        } else {
                            if (mIsVisible) {
                                mMyHandler.sendEmptyMessage(6);
                            }
                        }
                    }
                });
                break;
            case "0031":
                VisitRecordLab.get(getActivity()).clearVisitRecords();
                mMyHandler.sendEmptyMessage(2);
                break;
            default:
                mMyHandler.sendEmptyMessage(-1);
                break;
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
                    Log.d(TAG, VisitRecordLab.get(mContext).getVisitRecords().toString());
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    updateUI();
                    break;
                case 1:
                    mRecyclerAdapter.notifyDataSetChanged();
                    mRecyclerAdapter.setLoaded();
                    break;
                case 2:
                    if (mRecyclerAdapter != null) {
                        mRecyclerAdapter.notifyDataSetChanged();
                    }
                    if (mIsVisible) {
                        Toast.makeText(getActivity(), listResult.getMsg(), Toast.LENGTH_LONG).show();
                    }

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
            if (mRefreshLayout.isRefreshing()) {
                mRefreshLayout.setRefreshing(false);
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
        dialog.setTargetFragment(SignQueryFragment.this, requestCode);
        dialog.show(fragmentManager, DIALOG_DATE);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
//        pageIndex = 1;
//        queryRecords();
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

    /**
     * 重置数据
     */
    private void restoreData() {
        mText_keywords.setText("");
        mSpinner_visitorState.setSelection(0);
        mSpinner_identity.setSelection(0);
        mSignInBegin.setText("");
        mSignInEnd.setText("");
        mDateSIBegin = null;
        mDateSIEnd = null;
        pageIndex = 1;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        restoreData();
    }

    class QueryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
        private LinearLayout mManager;
        private ImageView mIconDetail;
        private ImageView mIconPrint;


        QueryViewHolder(LayoutInflater inflater, ViewGroup parent) {
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
            mManager = itemView.findViewById(R.id.manage);
            mIconDetail = itemView.findViewById(R.id.icon_detail);
            mIconPrint = itemView.findViewById(R.id.icon_print);
            mIconDetail.setVisibility(View.GONE);
        }

        void bind(VisitRecord record, int position) {
            mVisitorName.setText(record.getVisitor_name());
            mVisitorCounter.setText(record.getVisitor_counter());
            mVisitReason.setText(record.getVisitor_for());
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
            mManager.setTag(record);
            mManager.setOnClickListener(this);
            mCellContainer.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //访问记录
            VisitRecord record = (VisitRecord) v.getTag();
            switch (v.getId()) {
                case R.id.cell_container://详情按钮点击事件
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), VisitorFormDetailsActivity.class);
                    intent.putExtra(VisitorConstant.INTENT_PUT_EXTRA_DATA, record);
                    startActivity(intent);
                    break;
                case R.id.manage://打印按钮点击事件
                    Intent intent1 = new Intent();
                    intent1.putExtra(VisitorConstant.INTENT_PUT_EXTRA_DATA, record);
                    intent1.setClass(getActivity(), PrinterActivity.class);
                    startActivity(intent1);
                    break;
                default:
                    break;
            }
        }
    }

    private boolean isLoading;
    private int lastVisibleItem, totalItemCount;

    class LoadingMoreHolder extends RecyclerView.ViewHolder {
        ProgressBar mBar;

        LoadingMoreHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_loading, parent, false));
            mBar = itemView.findViewById(R.id.progress_bar);
        }

        void bind() {
            mBar.setIndeterminate(true);
        }
    }

    class QueryRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        List<VisitRecord> mVisitRecords;
        private final int VIEW_TYPE_ITEM = 0;
        private final int VIEW_TYPE_LOADING = 1;
        private OnLoadMoreListener onLoadMoreListener;

        void setOnLoadMoreListener(OnLoadMoreListener OnLoadMoreListener) {
            this.onLoadMoreListener = OnLoadMoreListener;
        }

        QueryRecyclerAdapter(RecyclerView recyclerView, List<VisitRecord> records) {
            mVisitRecords = records;
            final LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = manager.getItemCount();
                    lastVisibleItem = manager.findLastVisibleItemPosition();
                    if (!isLoading && totalItemCount <= (lastVisibleItem + 1)) {
                        if (isLastPage) {
                            return;
                        }
                        if (onLoadMoreListener != null) {
                            isLoading = true;
                            onLoadMoreListener.onLoadMore();
                        }
                    }
                }
            });
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == VIEW_TYPE_ITEM) {
                return new QueryViewHolder(LayoutInflater.from(getActivity()), parent);
            } else if (viewType == VIEW_TYPE_LOADING) {
                return new LoadingMoreHolder(LayoutInflater.from(getActivity()), parent);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            VisitRecord record = mVisitRecords.get(position);
            if (holder instanceof QueryViewHolder) {
                ((QueryViewHolder) holder).bind(record, position);
            } else if (holder instanceof LoadingMoreHolder) {
                ((LoadingMoreHolder) holder).bind();
            }
        }

        @Override
        public int getItemViewType(int position) {
            return mVisitRecords.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
        }

        @Override
        public int getItemCount() {
            return mVisitRecords.size();
        }

        public void setLoaded() {
            isLoading = false;
        }
    }
}
