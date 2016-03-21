package sun.bob.leela.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import sun.bob.leela.R;
import sun.bob.leela.db.Account;
import sun.bob.leela.db.AccountHelper;

/**
 * Created by bob.sun on 16/3/19.
 */
public class AcctListAdapter extends RecyclerView.Adapter<AcctListViewHolder> {

    private Context context;
    private ArrayList<Account> data;

    public AcctListAdapter(Context context) {
        super();
        this.context = context;
//        data = AccountHelper.getInstance(context).getAllAccount();
        data = new ArrayList<>();
    }

    @Override
    public AcctListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AcctListViewHolder(
                LayoutInflater.from(context)
                        .inflate(R.layout.acct_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(AcctListViewHolder holder, int position) {
        holder.configureWithAccount(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void loadAccountsInCategory(Long category){
        data = AccountHelper.getInstance(context).getAccountsByCategory(category);
        this.notifyDataSetChanged();
    }
}
