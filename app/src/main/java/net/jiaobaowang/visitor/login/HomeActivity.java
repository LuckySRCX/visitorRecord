package net.jiaobaowang.visitor.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import net.jiaobaowang.visitor.R;
import net.jiaobaowang.visitor.common.VisitorConfig;
import net.jiaobaowang.visitor.manage.ManageActivity;
import net.jiaobaowang.visitor.utils.SharePreferencesUtil;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private boolean hasQueryPower;
    private boolean hasSignPower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SharePreferencesUtil util = new SharePreferencesUtil(HomeActivity.this, VisitorConfig.VISIT_LOCAL_STORAGE, false);
        hasQueryPower = util.getBoolean(VisitorConfig.VISIT_LOCAL_USER_QUERY);
        hasSignPower = util.getBoolean(VisitorConfig.VISIT_LOCAL_USER_SIGN);
        findViewById(R.id.visit_record).setOnClickListener(this);
        findViewById(R.id.visit_query).setOnClickListener(this);
        findViewById(R.id.visit_leave).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_personal_setting:
                return true;
            case R.id.menu_quit:
                quitSystem();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void quitSystem() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(LoginActivity.EXTRA_DATA, true);
        intent.setClass(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        int selectId = 0;
        switch (v.getId()) {
            case R.id.visit_record:
                if(hasSignPower){
                    selectId=1;
                }else {
                    Toast.makeText(HomeActivity.this,"您无此权限！",Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
            case R.id.visit_query:
                selectId = 2;
                break;
            case R.id.visit_leave:
                if(hasSignPower){
                    selectId = 3;
                }else {
                    Toast.makeText(HomeActivity.this,"您无此权限！",Toast.LENGTH_SHORT).show();
                    return;
                }

                break;
            default:
                break;
        }
        Intent intent = new Intent();
        intent.putExtra(ManageActivity.EXTRA_CODE, selectId);
        intent.setClass(this, ManageActivity.class);
        startActivity(intent);
    }
}
