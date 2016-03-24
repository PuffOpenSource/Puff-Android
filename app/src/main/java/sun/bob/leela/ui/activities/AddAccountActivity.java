package sun.bob.leela.ui.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import fr.ganfra.materialspinner.MaterialSpinner;
import sun.bob.leela.R;
import sun.bob.leela.adapters.CategorySpinnerAdapter;
import sun.bob.leela.adapters.TypeSpinnerAdapter;
import sun.bob.leela.db.AcctType;
import sun.bob.leela.db.Category;
import sun.bob.leela.db.CategoryHelper;
import sun.bob.leela.db.TypeHelper;

public class AddAccountActivity extends AppCompatActivity {

    private AppCompatSpinner spinnerCategory, spinnerType;
    private Long type;
    private Long category;

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

        spinnerType = (AppCompatSpinner) findViewById(R.id.spinner_type);
        spinnerType.setPrompt("Type");
        spinnerType.setAdapter(new TypeSpinnerAdapter(this, android.R.layout.simple_dropdown_item_1line));
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position < 0 || !(parent.getAdapter() instanceof TypeSpinnerAdapter)) {
                    return;
                }
                AcctType type = (AcctType) (parent.getAdapter()).getItem(position);
                AddAccountActivity.this.type = type.getId();
                CategoryHelper helper = CategoryHelper.getInstance(null);
                int catePos = helper.getAllCategory().indexOf(helper.getCategoryById(type.getCategory()));
                AddAccountActivity.this.spinnerCategory.setSelection(catePos);
                AddAccountActivity.this.category = type.getCategory();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                type = Long.valueOf(-1);
            }
        });

        spinnerCategory = (AppCompatSpinner) findViewById(R.id.spinner_category);
        spinnerCategory.setAdapter(new CategorySpinnerAdapter(this, android.R.layout.simple_dropdown_item_1line));
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position < 0) {
                    return;
                }
                Category category = (Category) parent.getAdapter().getItem(position);
                AddAccountActivity.this.category = category.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                category = Long.valueOf(-1);
            }
        });
    }

}
