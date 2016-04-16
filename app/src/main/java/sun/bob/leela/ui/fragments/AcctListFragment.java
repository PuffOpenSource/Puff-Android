package sun.bob.leela.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import sun.bob.leela.R;
import sun.bob.leela.adapters.AcctListAdapter;
import sun.bob.leela.listeners.OnCardsScrollListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AcctListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AcctListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AcctListFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_CATEGORY = "category_param";

    private Long category;

    private OnFragmentInteractionListener mListener;

    private RecyclerView recyclerView;
    private CardView placeHolder;
    private AcctListAdapter adapter;

    public AcctListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param category Parameter 1.
     * @return A new instance of fragment AcctListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AcctListFragment newInstance(Long category) {
        AcctListFragment fragment = new AcctListFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = getArguments().getLong(ARG_CATEGORY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View ret = inflater.inflate(R.layout.fragment_acct_list, container, false);
        recyclerView = (RecyclerView) ret.findViewById(R.id.acct_list_recycler_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AcctListAdapter(getContext(), recyclerView);
        recyclerView.setAdapter(adapter);
        adapter.loadAccountsInCategory(category);
        recyclerView.addOnScrollListener(new OnCardsScrollListener(recyclerView));

        placeHolder = (CardView) ret.findViewById(R.id.placeholder);
        ViewCompat.setElevation(placeHolder, 10.0f);
        return ret;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();
        adapter.loadAccountsInCategory(category);
        if (recyclerView.getAdapter().getItemCount() == 0) {
            placeHolder.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        } else {
            placeHolder.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.notifyItemRangeChanged(0, adapter.getItemCount());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        adapter.loadAccountsInCategory(category);
        if (recyclerView.getAdapter().getItemCount() == 0) {
            placeHolder.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        } else {
            placeHolder.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
