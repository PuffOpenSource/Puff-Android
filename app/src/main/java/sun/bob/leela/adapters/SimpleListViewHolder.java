package sun.bob.leela.adapters;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import sun.bob.leela.R;
import sun.bob.leela.db.Account;
import sun.bob.leela.db.Category;

/**
 * Created by bob.sun on 16/5/11.
 */
public class SimpleListViewHolder extends RecyclerView.ViewHolder {

    public enum SimpleListViewType {
        SimpleListViewTypeCategory,
        SimpleListViewTypeAccount,
    }

    private SimpleListViewType type;
    private View itemView;

    public SimpleListViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
    }

    public void configureWithAccount(Account account) {
        this.type = SimpleListViewType.SimpleListViewTypeAccount;
        ((AppCompatTextView) itemView.findViewById(R.id.account_name)).setText(account.getName());
    }

    public void configureWithCategory(Category category) {
        this.type = SimpleListViewType.SimpleListViewTypeCategory;
        ((AppCompatTextView) itemView.findViewById(R.id.account_name)).setText(category.getName());
    }

    public SimpleListViewType getType() {
        return type;
    }
}
