package net.jiaobaowang.visitor.printer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.jiaobaowang.visitor.R;
import net.jiaobaowang.visitor.common.VisitorConstant;
import net.jiaobaowang.visitor.entity.VisitRecord;

public class VisitorFormDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VisitRecord visitRecord = (VisitRecord) getIntent().getSerializableExtra(VisitorConstant.INTENT_PUT_EXTRA_DATA);
        setContentView(R.layout.activity_visitor_form_details);
    }
}
