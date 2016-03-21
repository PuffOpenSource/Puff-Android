package sun.bob.leela.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sun.bob.leela.R;
import sun.bob.leela.db.Account;

/**
 * Created by bob.sun on 16/3/19.
 */
public class AcctListViewHolder extends RecyclerView.ViewHolder {
    private View itemView;
    public AcctListViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        itemView.setMinimumHeight(60);
    }

    public void configureWithAccount(Account account) {
        ((TextView) itemView.findViewById(R.id.list_account_name)).setText(account.getName());
    }
}
