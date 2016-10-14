package sun.bob.leela.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import sun.bob.leela.R;
import sun.bob.leela.db.Account;
import sun.bob.leela.db.AccountHelper;
import sun.bob.leela.utils.AppConstants;
import sun.bob.leela.utils.ResUtil;
import sun.bob.leela.utils.StringUtil;

public class DetailActivity extends AppCompatActivity {

    private String acct, passwd, addt;
    private Account account;

    private AppCompatTextView tvTitle, tvAccount, tvPasswd, tvAdditional, tvWebSite, tvLastAccess,
            tvWebSiteTitle;
    private AppCompatImageView image;
    private LinearLayout webSiteContainer;

    private ArrayList<String> credentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        credentials = getIntent().getStringArrayListExtra("credentials");

        wireUpViews();
    }

    @Override
    public void onResume() {
        //Move loading to onResume for editing.
        super.onResume();
        Long id = getIntent().getLongExtra("account", -1);
        account = AccountHelper.getInstance(this).getAccount(id);

        acct = credentials.get(0);
        passwd = credentials.get(1);
        addt = credentials.get(2);

        tvTitle.setText(account.getName());
        tvAccount.setText(acct);
        tvPasswd.setText(passwd);
        tvAdditional.setText(addt);

        if (StringUtil.isNullOrEmpty(account.getWebsite())) {
            tvWebSiteTitle.setVisibility(View.GONE);
            webSiteContainer.setVisibility(View.GONE);
        } else {
            tvWebSite.setText(account.getWebsite());
        }

        if (account.getLast_access() != null)
            tvLastAccess.setText(StringUtil.timeStampToTime(account.getLast_access().toString()));

        Picasso.with(this)
                .load(ResUtil.getInstance(null).getBmpUri(account.getIcon()))
                .into(image);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    private void wireUpViews(){
        tvAccount = (AppCompatTextView) findViewById(R.id.account);
        tvPasswd = (AppCompatTextView) findViewById(R.id.password);
        tvAdditional = (AppCompatTextView) findViewById(R.id.additional);
        tvTitle = (AppCompatTextView) findViewById(R.id.id_name);
        image = (AppCompatImageView) findViewById(R.id.account_image);
        tvWebSite = (AppCompatTextView) findViewById(R.id.id_website_link);
        tvWebSiteTitle = (AppCompatTextView) findViewById(R.id.website_title);
        webSiteContainer = (LinearLayout) findViewById(R.id.website_container);
        tvLastAccess = (AppCompatTextView) findViewById(R.id.last_accessed);

        webSiteContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = account.getWebsite();
                if (!url.startsWith("http://") && !url.startsWith("https://"))
                    url = "http://" + url;

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AppConstants.REQUEST_CODE_EDIT && resultCode == RESULT_OK) {
            credentials = data.getStringArrayListExtra("credentials");
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        if (menuItem.getItemId() == R.id.action_edit) {
            Intent intent = new Intent(this, AddAccountActivity.class);
            intent.putExtra("acctId", account.getId());
            intent.putExtra("showMode", AddAccountActivity.AddAccountShowMode.ShowModeEdit);
            intent.putStringArrayListExtra("credentials", getIntent().getStringArrayListExtra("credentials"));
            startActivityForResult(intent, AppConstants.REQUEST_CODE_EDIT);
            return true;
        }
        if (menuItem.getItemId() == R.id.action_delete) {
            AccountHelper.getInstance(null).deleteAccount(account);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
