package sun.bob.leela.ui.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import sun.bob.leela.R;
import sun.bob.leela.db.Account;
import sun.bob.leela.db.AccountHelper;
import sun.bob.leela.utils.ResUtil;

public class DetailActivity extends AppCompatActivity {

    private String acct, passwd, addt;
    private Account account;

    private AppCompatTextView tvTitle, tvAccount, tvPasswd, tvAdditional;
    private AppCompatImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        wireUpViews();

        Long id = getIntent().getLongExtra("account", -1);
        account = AccountHelper.getInstance(this).getAccount(id);

        ArrayList<String> list = getIntent().getStringArrayListExtra("credentials");

        acct = list.get(0);
        passwd = list.get(1);
        addt = list.get(2);

        tvTitle.setText(account.getName());
        tvAccount.setText(acct);
        tvPasswd.setText(passwd);
        tvAdditional.setText(addt);

        Picasso.with(this)
                .load(ResUtil.getInstance(null).getBmpUri(account.getIcon()))
                .into(image);

    }

    private void wireUpViews(){
        tvAccount = (AppCompatTextView) findViewById(R.id.account);
        tvPasswd = (AppCompatTextView) findViewById(R.id.password);
        tvAdditional = (AppCompatTextView) findViewById(R.id.additional);
        tvTitle = (AppCompatTextView) findViewById(R.id.id_name);
        image = (AppCompatImageView) findViewById(R.id.account_image);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
