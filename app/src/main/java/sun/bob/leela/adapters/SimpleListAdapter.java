package sun.bob.leela.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import sun.bob.leela.R;
import sun.bob.leela.db.Account;
import sun.bob.leela.db.AccountHelper;
import sun.bob.leela.db.Category;
import sun.bob.leela.db.CategoryHelper;
import sun.bob.leela.events.DialogEvent;
import sun.bob.leela.runnable.CryptoRunnable;
import sun.bob.leela.services.IMEService;
import sun.bob.leela.utils.CryptoUtil;

/**
 * Created by bob.sun on 16/5/11.
 */
public class SimpleListAdapter extends RecyclerView.Adapter<SimpleListViewHolder> implements SimpleListViewHolder.SimpleListDelegate {

    private ArrayList data;
    private Context context;
    private SimpleListViewHolder.SimpleListViewType type;

    @Override
    public SimpleListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_list_item, parent, false);
        final SimpleListViewHolder ret = new SimpleListViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ret.onClick();
            }
        });
        return ret;
    }

    @Override
    public void onBindViewHolder(SimpleListViewHolder holder, int position) {
        Object item = data.get(position);
        if (item instanceof Account) {
            holder.configureWithAccount((Account) item, position);
            holder.delegate = this;
        } else {
            holder.configureWithCategory((Category) item, position);
            holder.delegate = this;
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void loadCategory(){
        type = SimpleListViewHolder.SimpleListViewType.SimpleListViewTypeCategory;
        data = CategoryHelper.getInstance(null).getAllCategory();
        this.notifyDataSetChanged();
    }

    public void loadAccountInCategory(Long category) {
        type = SimpleListViewHolder.SimpleListViewType.SimpleListViewTypeCategory;
        data = AccountHelper.getInstance(null).getAccountsByCategory(category);
        this.notifyDataSetChanged();
    }

    @Override
    public void onClick(SimpleListViewHolder.SimpleListViewType type, int index) {
        switch (type) {
            case SimpleListViewTypeAccount:
                Account account = (Account) data.get(index);
                new CryptoUtil(context, new CryptoUtil.OnDecryptedListener() {
                    @Override
                    public void onDecrypted(String account, String passwd, String addt) {
                        Intent intent = new Intent(context, IMEService.class);
                        intent.setAction("INIT");
                        intent.putExtra("account", account);
                        intent.putExtra("password", passwd);
                        intent.putExtra("additional", addt);
                        context.startService(intent);

                        // TODO: 16/5/15 Publish an event to tell activity I'm done here.
                        //               Dismiss that activity.
                        EventBus.getDefault().post(new DialogEvent());
                    }
                }).runDecrypt(account.getAccount(), account.getHash(), account.getAdditional(),
                        account.getAccount_salt(), account.getSalt(), account.getAdditional_salt());
                break;
            case SimpleListViewTypeCategory:
                this.loadAccountInCategory(((Category) data.get(index)).getId());
                this.type = SimpleListViewHolder.SimpleListViewType.SimpleListViewTypeAccount;
                break;
            default:
                break;
        }
    }

    public SimpleListViewHolder.SimpleListViewType getCurrentListType() {
        return type;
    }

    public void backToCategory() {
        loadCategory();
    }


}
