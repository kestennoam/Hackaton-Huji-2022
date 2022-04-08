package com.example.hujihackaton47.adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hujihackaton47.R;
import com.example.hujihackaton47.db.Database;
import com.example.hujihackaton47.db.IDataBase;
import com.example.hujihackaton47.interfaces.IMyItemAdapterListener;
import com.example.hujihackaton47.interfaces.IResultItemAdapterListener;
import com.example.hujihackaton47.models.Item;
import com.example.hujihackaton47.viewholders.MyItemViewHolder;
import com.example.hujihackaton47.viewholders.ResultItemViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MyItemAdapter extends RecyclerView.Adapter<MyItemViewHolder> {

    private final IDataBase db;
    private final IMyItemAdapterListener listener;
    private final List<Item> items = new ArrayList<>();

    public MyItemAdapter(@NonNull IMyItemAdapterListener listener) {
        this.db = Database.getInstance();
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_my_item, parent, false);
        return new MyItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyItemViewHolder holder, int position) {
        Item item = items.get(position);
        Log.d("MyItemAdapter", "item: " + item);
        holder.getNameValueTextView().setText(item.getName());
        holder.getPriceValueTextView().setText(String.valueOf(item.getPrice()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<Item> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }
}
