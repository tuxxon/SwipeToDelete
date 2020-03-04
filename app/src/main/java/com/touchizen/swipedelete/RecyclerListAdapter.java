package com.touchizen.swipedelete;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ItemViewHolder>{

    private List<String> mItems;

    public RecyclerListAdapter() {
        this.mItems = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerListAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerListAdapter.ItemViewHolder holder, int position) {
        holder.textView.setText(mItems.get(position));
    }

    public void removeAt(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(String _name) {
        mItems.add(_name+"-"+mItems.size());
        if (mItems.size() == 1) {
            notifyDataSetChanged();
        }
        else {
            notifyItemInserted(mItems.size());
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public final TextView textView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.rowName);
        }

        /*
        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
         */
    }
}
