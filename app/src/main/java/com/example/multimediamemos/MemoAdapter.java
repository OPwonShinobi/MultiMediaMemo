package com.example.multimediamemos;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.MemoEntryHolder> {
    private final List<Integer> mDataset;
    private final MainActivity mViewController;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MemoEntryHolder extends RecyclerView.ViewHolder {
        // v is MemoEntryView object
        public View v;
        public MemoEntryHolder(View v) {
            super(v);
            this.v = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MemoAdapter(List<Integer> dataset, MainActivity viewController) {
        mDataset = dataset;
        mViewController = viewController;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MemoEntryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MemoEntryView v = new MemoEntryView(parent.getContext(), mViewController);
        MemoEntryHolder vh = new MemoEntryHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MemoEntryHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        holder.textView.setText(mDataset.get(position));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
