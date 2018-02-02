package net.jiaobaowang.visitor.entity;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rocka on 2018/1/17.
 */

public class OffRecordLab {
    private List<VisitRecord> mVisitRecords;
    private static OffRecordLab mOffRecordLab;
    private Context mContext;

    public OffRecordLab(Context context) {
        mContext = context.getApplicationContext();
        mVisitRecords = new ArrayList<>();
    }

    public static OffRecordLab get(Context context) {
        if (mOffRecordLab == null) {
            mOffRecordLab = new OffRecordLab(context);
        }
        return mOffRecordLab;
    }

    public List<VisitRecord> getVisitRecords() {
        return mVisitRecords;
    }


    public void setVisitRecords(List<VisitRecord> visitRecords) {
        mVisitRecords.clear();
        mVisitRecords.addAll(visitRecords);
    }

    public void addVisitRecords(List<VisitRecord> list) {
        mVisitRecords.addAll(list);
    }

    public void clearVisitRecords() {
        mVisitRecords.clear();
    }

    public void removeLastRecord() {
        if (mVisitRecords.size() > 0 && mVisitRecords.get(mVisitRecords.size() - 1) == null) {
            mVisitRecords.remove(mVisitRecords.size() - 1);
        }
    }
}
