package net.jiaobaowang.visitor.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import net.jiaobaowang.visitor.R;
import net.jiaobaowang.visitor.manage.ManageActivity;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        int selectId = 0;
        switch (v.getId()) {
            case R.id.visit_record:
                selectId = 0;
                break;
            case R.id.visit_query:
                selectId = 1;
                break;
            case R.id.visit_leave:
                selectId = 2;
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
