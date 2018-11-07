package com.monier.bennetout.ihmclient;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MyListViewAdapter extends RecyclerView.Adapter<MyListViewAdapter.ViewHolder> {

    private ArrayList<MyCustomHolder> mDataset;
    private float myDefaultTextSize;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView mTextView;
        ViewHolder(View view) {
            super(view);
            mTextView = view.findViewById(R.id.textViewValue);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    MyListViewAdapter(ArrayList<MyCustomHolder> myDataset, float defaultTextSize) {
        mDataset = myDataset;
        myDefaultTextSize = defaultTextSize;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public MyListViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
//        TextView v = (TextView) LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.main, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.config_view, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final MyListViewAdapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        MyCustomHolder myCustomHolder = mDataset.get(position);

//        holder.mTextView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        if (myCustomHolder.isActive) {
            holder.mTextView.setTextColor(myCustomHolder.activeColor);
            holder.mTextView.setText(Html.fromHtml("<b>" + myCustomHolder.textToShow + "</b>"));
            holder.mTextView.setTextSize(myDefaultTextSize +5);
        }
        else {
            holder.mTextView.setTextColor(Color.GRAY);
            holder.mTextView.setText(myCustomHolder.textToShow);
            holder.mTextView.setTextSize(myDefaultTextSize);
        }

        // Suppressin d'un item via un long click
//        holder.mTextView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                deleteItem(holder.getAdapterPosition());
//                return false;
//            }
//        });

        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchActiveItem(holder.getAdapterPosition());
            }
        });
    }

    private void switchActiveItem(int index) {
        if (mDataset.get(index).isActive) {
            mDataset.get(index).isActive = false;
        } else {
            for (MyCustomHolder holder:mDataset) {
                holder.isActive = false;
            }
            mDataset.get(index).isActive = true;
        }

        notifyDataSetChanged();
    }

    private void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
