package sun.bob.leela.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import de.greenrobot.event.EventBus;
import sun.bob.leela.R;
import sun.bob.leela.adapters.SimpleListAdapter;
import sun.bob.leela.adapters.SimpleListViewHolder;
import sun.bob.leela.events.DialogEvent;
import sun.bob.leela.utils.ResUtil;

public class DialogAcctList extends AppCompatActivity {

    SimpleListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_acct_list);
        EventBus.getDefault().register(this);
        initUI();
        adapter = new SimpleListAdapter();
        adapter.loadCategory();
//        getSupportFragmentManager().beginTransaction()
//                .add()
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    public void onEventMainThread(Object event) {
        if (event instanceof DialogEvent) {
            finish();
        }
    }

    private void initUI(){
        getWindow().setLayout(ResUtil.getInstance(getApplicationContext()).pointToDp(400),
                ResUtil.getInstance(getApplicationContext()).pointToDp(600));
    }

    @Override
    public void onBackPressed() {
        if (adapter.getCurrentListType() == SimpleListViewHolder.SimpleListViewType.SimpleListViewTypeAccount) {
            adapter.backToCategory();
        } else {
            finish();
        }
    }
}
