package sun.bob.leela.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import sun.bob.leela.R;
import sun.bob.leela.db.Account;
import sun.bob.leela.db.AccountHelper;
import sun.bob.leela.db.Category;
import sun.bob.leela.db.CategoryHelper;

/**
 * Created by bob.sun on 16/5/11.
 */
public class SimpleListAdapter extends RecyclerView.Adapter<SimpleListViewHolder> {

    private ArrayList data;
    @Override
    public SimpleListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_list_item, parent, false);
        SimpleListViewHolder ret = new SimpleListViewHolder(view);
        return ret;
    }

    @Override
    public void onBindViewHolder(SimpleListViewHolder holder, int position) {
        Object item = data.get(position);
        if (item instanceof Account) {
            holder.configureWithAccount((Account) item);
        } else {
            holder.configureWithCategory((Category) item);
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void loadCategory(){
        data = CategoryHelper.getInstance(null).getAllCategory();
        this.notifyDataSetChanged();
    }

    public void loadAccountInCategory(Long category) {
        data = AccountHelper.getInstance(null).getAccountsByCategory(category);
        this.notifyDataSetChanged();
    }
}
