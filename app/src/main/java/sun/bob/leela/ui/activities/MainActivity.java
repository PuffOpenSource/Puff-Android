package sun.bob.leela.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import java.util.HashMap;

import sun.bob.leela.R;
import sun.bob.leela.db.Category;
import sun.bob.leela.db.CategoryHelper;
import sun.bob.leela.ui.drawable.ColorSquare;
import sun.bob.leela.ui.fragments.AcctListFragment;
import sun.bob.leela.utils.AppConstants;

/**
 * Created by bob.sun on 16/3/19.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private AcctListFragment acctListFragment;
    private AcctListFragment currentFragment;
    private HashMap<Long, AcctListFragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragments = new HashMap<>();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, AddAccountActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        for (Category category : CategoryHelper.getInstance(null).getAllCategory()) {
            navigationView.getMenu().add(category.getName());
        }

        acctListFragment = AcctListFragment.newInstance(AppConstants.CAT_ID_DEFAULT);
        fragments.put(AppConstants.CAT_ID_DEFAULT, acctListFragment);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.id_fragment_container, acctListFragment, "AcctListFragment")
                .commit();
        currentFragment = acctListFragment;
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

//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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
        currentFragment = toShow;
    }
}
