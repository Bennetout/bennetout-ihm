package com.monier.bennetout.ihmclient;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyListViewAdapter extends RecyclerView.Adapter<MyListViewAdapter.ViewHolder> {

    public ArrayList<MyCustomHolder> mDataset;
    private MyListViewListener myListener;
    private double valueSelect = 0;
    private boolean customClickEnabled = true;
    private boolean deleteOnLongClickEnable = false;

    public interface MyListViewListener {
        void onNewPositionClicked(boolean state, double value);
    }

    public double getValueSelected() throws Exception {
        for (int i = 0; i < mDataset.size(); i++) {
            if (mDataset.get(i).isActive) {
                return mDataset.get(i).value;
            }
        }

        throw new Exception("No value selected");
    }

    public void setCustomLongClickEnabled(boolean deleteOnLongClickEnable) {
        this.deleteOnLongClickEnable = deleteOnLongClickEnable;
    }

    public void setCustomClickEnabled(boolean enabled) {
        this.customClickEnabled = enabled;
    }

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
    public MyListViewAdapter(ArrayList<MyCustomHolder> myDataset, MyListViewListener listener) {
        this.mDataset = myDataset;
//        this.myDefaultTextSize = defaultTextSize;
        this.myListener = listener;
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

        float textSize;
        if (holder.itemView.getResources().getBoolean(R.bool.isTablet)) {
            textSize = 50;
        } else {
            textSize = 20;
        }
        if (myCustomHolder.isActive) {
            holder.mTextView.setTextColor(myCustomHolder.activeColor);
            holder.mTextView.setText(Html.fromHtml("<b>" + myCustomHolder.textToShow + "</b>"));
            holder.mTextView.setTextSize(textSize +5);
        }
        else {
            holder.mTextView.setTextColor(Color.GRAY);
            holder.mTextView.setText(myCustomHolder.textToShow);
            holder.mTextView.setTextSize(textSize);
        }

        // Suppressin d'un item via un long click
        if (deleteOnLongClickEnable) {
            holder.mTextView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog.Builder alertDialogChoix = new AlertDialog.Builder(holder.itemView.getContext());
                    alertDialogChoix.setTitle("Que voulez vous faire ?");
                    alertDialogChoix.setItems(new CharSequence[]
                                    {"Décaler à gauche", "Décaler à droite", "Supprimer"},
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // The 'which' argument contains the index position
                                    // of the selected item
                                    switch (which) {
                                        case 0:
                                            if (holder.getAdapterPosition() != 0) {
                                                MyCustomHolder active = mDataset.get(holder.getAdapterPosition());
                                                MyCustomHolder nMoinsUn = mDataset.get(holder.getAdapterPosition() -1);
                                                mDataset.set(holder.getAdapterPosition(), nMoinsUn);
                                                mDataset.set(holder.getAdapterPosition() -1, active);
                                                notifyDataSetChanged();
                                            }
                                            break;

                                        case 1:
                                            if (holder.getAdapterPosition() != (mDataset.size() -1)) {
                                                MyCustomHolder active = mDataset.get(holder.getAdapterPosition());
                                                MyCustomHolder nPlusUn = mDataset.get(holder.getAdapterPosition() + 1);
                                                mDataset.set(holder.getAdapterPosition(), nPlusUn);
                                                mDataset.set(holder.getAdapterPosition() + 1, active);
                                                notifyDataSetChanged();
                                            }
                                            break;

                                        case 2:
                                            AlertDialog.Builder alertDialogSuppr = new AlertDialog.Builder(holder.itemView.getContext());
                                            alertDialogSuppr.setTitle("Supprimer une valeur");
                                            alertDialogSuppr.setMessage("Supprimer la valeur " + holder.mTextView.getText() + " ?");
                                            alertDialogSuppr.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    deleteItem(holder.getAdapterPosition());
                                                }
                                            });
                                            alertDialogSuppr.setNegativeButton("Non", null);
                                            alertDialogSuppr.show();
                                            break;
                                    }
                                }
                            });
                    alertDialogChoix.show();
                    return false;
                }
            });
        }

        if (customClickEnabled) {
            holder.mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchActiveItem(holder.getAdapterPosition());
                }
            });
        }
    }

    void removeSelectedValue() {
        for (MyCustomHolder index:mDataset) {
            if (index.value == valueSelect)
                index.isActive = false;
        }

        notifyDataSetChanged();
    }

//    public void resetAll() {
//        for (MyCustomHolder holder:mDataset) {
//            holder.isActive = false;
//        }
//        notifyDataSetChanged();
//    }

    public void switchActiveItem(int index) {
        if (mDataset.get(index).isActive) {
            mDataset.get(index).isActive = false;
        } else {
            for (MyCustomHolder holder:mDataset) {
                holder.isActive = false;
            }
            mDataset.get(index).isActive = true;
        }

        notifyDataSetChanged();

        valueSelect = mDataset.get(index).value;
        if (myListener != null)
            myListener.onNewPositionClicked(mDataset.get(index).isActive, valueSelect);
    }

    public double[] getAllValues() {

        double[] ret = new double[mDataset.size()];

        for (int i = 0; i < mDataset.size(); i++) {
            ret[i] = mDataset.get(i).value;
        }

        return ret;
    }

    public void addItem(MyCustomHolder holder) {
        this.mDataset.add(holder);
        notifyDataSetChanged();
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
