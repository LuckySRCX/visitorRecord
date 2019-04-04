package net.jiaobaowang.visitor.manage;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import net.jiaobaowang.visitor.R;
import net.jiaobaowang.visitor.common.VisitorConfig;
import net.jiaobaowang.visitor.utils.SharePreferencesUtil;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NavigationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NavigationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavigationFragment extends Fragment implements View.OnClickListener {
    private OnFragmentInteractionListener mListener;
    private final static String BC_CHECKED_ID = "checkedId";
    private boolean hasQuery;
    private boolean hasSign;

    public NavigationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters
     *
     * @return A new instance of fragment NavigationFragment.
     */
    public static NavigationFragment newInstance(int checkedId) {
        Bundle bundle = new Bundle();
        bundle.putInt(BC_CHECKED_ID, checkedId);
        NavigationFragment navigationFragment = new NavigationFragment();
        navigationFragment.setArguments(bundle);
        return navigationFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_navigation, container, false);
        SharePreferencesUtil util = new SharePreferencesUtil(getActivity(), VisitorConfig.VISIT_LOCAL_STORAGE, false);
        hasQuery = util.getBoolean(VisitorConfig.VISIT_LOCAL_USER_QUERY);
        hasSign = util.getBoolean(VisitorConfig.VISIT_LOCAL_USER_SIGN);
        RadioButton query = v.findViewById(R.id.btn_query);
        RadioButton signOff = v.findViewById(R.id.btn_sign_off);
        RadioButton signIn = v.findViewById(R.id.btn_sign_in);
        v.findViewById(R.id.btn_home).setOnClickListener(this);
        if (hasSign&&hasQuery) {
            signIn.setVisibility(View.VISIBLE);
            query.setVisibility(View.VISIBLE);
            signOff.setVisibility(View.VISIBLE);
            signIn.setOnClickListener(this);
            query.setOnClickListener(this);
            signOff.setOnClickListener(this);
        } else if(hasSign&&!hasQuery){
            signIn.setVisibility(View.VISIBLE);
            query.setVisibility(View.GONE);
            signOff.setVisibility(View.VISIBLE);
            signIn.setOnClickListener(this);
            signOff.setOnClickListener(this);
        }else {
            signIn.setVisibility(View.GONE);
            query.setVisibility(View.VISIBLE);
            signOff.setVisibility(View.GONE);
            query.setOnClickListener(this);
        }
        int checkId = 1;
        RadioButton rb;
        Bundle bundle = getArguments();
        if (bundle != null) {
            checkId = bundle.getInt(BC_CHECKED_ID);
        }
        switch (checkId) {
            case 1://SignIn
                rb = v.findViewById(R.id.btn_sign_in);
                break;
            case 2://query
                rb = v.findViewById(R.id.btn_query);
                break;
            case 3://signOff
                rb = v.findViewById(R.id.btn_sign_off);
                break;
            default:
                rb = v.findViewById(R.id.btn_sign_in);
                break;
        }
        rb.setChecked(true);
        return v;
    }

    public void onButtonPressed(int id) {
        if (mListener != null) {
            mListener.onFragmentInteraction(id);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int id);

    }

    @Override
    public void onClick(View v) {
        int id;
        switch (v.getId()) {
            case R.id.btn_home://首页
                id = 0;
                break;
            case R.id.btn_sign_in://签到
                id = 1;
                break;
            case R.id.btn_query://查询
                id = 2;
                break;
            case R.id.btn_sign_off://签离
                id = 3;
                break;
            default:
                id = 1;
                break;
        }
        onButtonPressed(id);
    }
}
