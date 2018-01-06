package net.jiaobaowang.visitor.printer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import net.jiaobaowang.visitor.R;
import net.jiaobaowang.visitor.entity.VisitorForm;

public class PrinterActivity extends AppCompatActivity {
    private static final String TAG = "PrinterActivity";
    private int type;//页面类型
    private VisitorForm visitorForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        visitorForm = (VisitorForm) getIntent().getSerializableExtra("data");
        setContentView(R.layout.activity_prenter);
        initView();
    }

    public void initView() {
        TextView tapeTypeTv = findViewById(R.id.tape_type_tv);
        TextView name_tv = findViewById(R.id.name_tv);
    }
}
