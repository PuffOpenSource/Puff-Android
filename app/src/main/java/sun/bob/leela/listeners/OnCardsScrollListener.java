package sun.bob.leela.listeners;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import de.greenrobot.event.EventBus;
import sun.bob.leela.adapters.AcctListAdapter;
import sun.bob.leela.events.ItemUIEvent;

/**
 * Created by bob.sun on 16/3/22.
 */
public class OnCardsScrollListener extends RecyclerView.OnScrollListener {

    private RecyclerView recyclerView;

    public OnCardsScrollListener(RecyclerView recyclerView){
        this.recyclerView = recyclerView;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState){
        int first;
        switch (newState){
            case RecyclerView.SCROLL_STATE_IDLE:
//                EventBus.getDefault()
//                        .post(new ItemUIEvent(ItemUIEvent.LIST_SCROLLED, recyclerView.getScrollY()));
                break;
            case RecyclerView.SCROLL_STATE_DRAGGING:
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:
                break;
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy){

    }
}
