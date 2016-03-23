package sun.bob.leela.ui.activities;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import sun.bob.leela.R;
import sun.bob.leela.utils.DeviceUtil;

public class AddAccountActivity extends AppCompatActivity {

    private NestedScrollView scrollView;
    private TextInputLayout inputLayout;
    private CollapsingToolbarLayout toolbarLayout;
    private AppBarLayout appBarLayout;
    private int layoutTopMargin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layoutTopMargin = (int) (16 * DeviceUtil.getInstance(getApplicationContext()).getDP());

        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        inputLayout = (TextInputLayout) findViewById(R.id.toolbar_text_input);
        scrollView = (NestedScrollView) findViewById(R.id.scrollView);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
//        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                int dy = oldScrollY - scrollY;
//                CollapsingToolbarLayout.LayoutParams layoutParams = (CollapsingToolbarLayout.LayoutParams) inputLayout.getLayoutParams();
//                layoutParams.topMargin += dy;
//                if (layoutParams.topMargin <= layoutTopMargin) {
//                    layoutParams.topMargin = layoutTopMargin;
//                }
//                if (layoutParams.topMargin >= 56 + layoutTopMargin) {
//                    layoutParams.topMargin = 56 + layoutTopMargin;
//                }
//                inputLayout.requestLayout();
//            }
//        });

    }

}
