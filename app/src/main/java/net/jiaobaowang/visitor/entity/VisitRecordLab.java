package net.jiaobaowang.visitor.entity;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rocka on 2018/1/12.
 */

public class VisitRecordLab {
    private List<VisitRecord> mVisitRecords;
    private static VisitRecordLab mVisitRecordLab;
    private Context mContext;

    public VisitRecordLab(Context context) {
        mContext = context.getApplicationContext();
        mVisitRecords=new ArrayList<>();
    }

    public static VisitRecordLab get(Context context) {
        if (mVisitRecordLab == null) {
            mVisitRecordLab = new VisitRecordLab(context);
        }
        return mVisitRecordLab;
    }

    public List<VisitRecord> getVisitRecords() {
        for (int i = 0; i < 100; i++) {
            addVisit(new VisitRecord());
        }
        return mVisitRecords;
    }

    private void addVisit(VisitRecord record) {
        mVisitRecords.add(record);
    }

    public void setVisitRecords(List<VisitRecord> visitRecords) {
        mVisitRecords = visitRecords;
    }
}
