package sun.bob.leela.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import sun.bob.leela.R;
import sun.bob.leela.db.AccountHelper;
import sun.bob.leela.db.Category;
import sun.bob.leela.db.CategoryHelper;
import sun.bob.leela.ui.fragments.AcctListFragment;
import sun.bob.leela.utils.AppConstants;
import sun.bob.leela.utils.ResUtil;

/**
 * Created by bob.sun on 16/3/19.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private AcctListFragment acctListFragment;
    private AcctListFragment currentFragment;
    private Toolbar toolbar;
    private HashMap<Long, AcctListFragment> fragments;
    private SubMenu categoriesMenu;
    private MenuItem lastChecked;
    private NavigationView navigationView;
    private ImageView headerImageView;
    private TextView headerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragments = new HashMap<>();

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View reveal = findViewById(R.id.reveal_background);
                // get the center for the clipping circle
                int centerX = (fab.getLeft() + fab.getRight()) / 2;
                int centerY = (fab.getTop() + fab.getBottom()) / 2;

                int startRadius = 0;
                // get the final radius for the clipping circle
                int endRadius = Math.max(reveal.getWidth(), reveal.getHeight());

                // create the animator for this view (the start radius is zero)
                SupportAnimator anim =
                        ViewAnimationUtils.createCircularReveal(reveal, centerX, centerY, startRadius, endRadius);

                // make the view visible and start the animation
                reveal.setVisibility(View.VISIBLE);
                anim.start();
                anim.addListener(new SupportAnimator.AnimatorListener() {
                    @Override
                    public void onAnimationStart() {

                    }

                    @Override
                    public void onAnimationEnd() {
                        Intent intent = new Intent(MainActivity.this, AddAccountActivity.class);
                        intent.putExtra("showMode", AddAccountActivity.AddAccountShowMode.ShowModeAdd);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
//                        reveal.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationCancel() {

                    }

                    @Override
                    public void onAnimationRepeat() {

                    }
                });
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        headerImageView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.header_image_view);
        headerTextView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.header_category);

        categoriesMenu = navigationView.getMenu().addSubMenu(R.string.categories);

        loadCategoriesInNavigation();

        acctListFragment = AcctListFragment.newInstance(AppConstants.CAT_ID_RECENT);
        fragments.put(AppConstants.CAT_ID_RECENT, acctListFragment);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.id_fragment_container, acctListFragment, "AcctListFragment")
                .commit();
        currentFragment = acctListFragment;
        loadAccountByCategory(CategoryHelper.getInstance(this).getCategoryById(AppConstants.CAT_ID_RECENT));


        if (!AccountHelper.getInstance(this).hasMasterPassword()) {
            Intent intent = new Intent(this, SetMasterPasswordActivity.class);
            intent.putExtra("showMode", SetMasterPasswordActivity.ShowMode.ShowModeAdd);
            startActivity(intent);
        }
    }

    private void loadCategoriesInNavigation() {
        categoriesMenu.clear();
        for (Category category : CategoryHelper.getInstance(null).getAllCategory()) {
            try {
                categoriesMenu.add(category.getName()).setIcon(new BitmapDrawable(getResources(),
                        ResUtil.getInstance(null).getBmp(category.getIcon())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        findViewById(R.id.reveal_background).setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        }

        if (id == R.id.action_generator) {
            startActivity(new Intent(MainActivity.this, PasswordGenActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Category category = CategoryHelper.getInstance(getApplicationContext())
                .getCategoryByName(String.valueOf(item.getTitle()));
        loadAccountByCategory(category);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (headerImageView != null)
            Picasso.with(this).load(ResUtil.getInstance(null).getBmpUri(category.getIcon()))
                    .config(Bitmap.Config.RGB_565)
                    .fit()
                    .into(headerImageView);
        if (headerTextView != null)
            headerTextView.setText(category.getName());
        drawer.closeDrawer(GravityCompat.START);
        if (lastChecked != null)
            lastChecked.setChecked(false);
        item.setChecked(true);
        lastChecked = item;
        return true;
    }

    private void loadAccountByCategory(Category category) {
        AcctListFragment toShow = fragments.get(category.getId());
        if (toShow == null) {
            toShow = acctListFragment.newInstance(category.getId());
            fragments.put(category.getId(), toShow);
            getSupportFragmentManager().beginTransaction()
                    .hide(currentFragment)
                    .add(R.id.id_fragment_container, toShow, category.getName())
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .hide(currentFragment)
                    .show(toShow)
                    .commit();
        }
        getSupportActionBar().setTitle(category.getName());
        currentFragment = toShow;
    }
}
