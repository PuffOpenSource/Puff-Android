package sun.bob.leela.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import sun.bob.leela.R;
import sun.bob.leela.db.Account;
import sun.bob.leela.db.AccountHelper;
import sun.bob.leela.utils.AppConstants;

/**
 * Created by bob.sun on 16/3/19.
 */
public class AcctListAdapter extends RecyclerView.Adapter<AcctListViewHolder> {

    private static final int VIEW_TYPE_EMPTY  = 0x23;
    private static final int VIEW_TYPE_NORMAL = 0x24;

    private Context context;
    private RecyclerView recyclerView;
    private ArrayList<Account> data;

    public AcctListAdapter(Context context, RecyclerView recyclerView) {
        super();
        this.context = context;
        this.recyclerView = recyclerView;
        data = new ArrayList<>();
        this.setHasStableIds(true);
    }

    @Override
    public AcctListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View normal = LayoutInflater.from(context)
                .inflate(R.layout.acct_list_item, parent, false);
        AcctListViewHolder viewHolder = new AcctListViewHolder(normal);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AcctListViewHolder holder, int position) {
        holder.configureWithAccount(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public long getItemId (int position) {
        return data.get(position).getId();
    }

    public void loadAccountsInCategory(Long category){
        if (category == AppConstants.CAT_ID_RECENT) {
            data = AccountHelper.getInstance(context).getRecentUsed(10);
            this.notifyDataSetChanged();
            return;
        }
        data = AccountHelper.getInstance(context).getAccountsByCategory(category);
        this.notifyDataSetChanged();
    }

}
