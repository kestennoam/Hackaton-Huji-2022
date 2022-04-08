package com.example.hujihackaton47.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hujihackaton47.R;

public class MyItemViewHolder extends  RecyclerView.ViewHolder{

    private final View rootView;

    // Views
    private TextView nameValueTextView;
    private TextView priceValueTextView;


    public MyItemViewHolder(@NonNull View itemView) {
        super(itemView);
        rootView = itemView;

        nameValueTextView = (TextView) rootView.findViewById(R.id.item_name_value);
        priceValueTextView = (TextView) rootView.findViewById(R.id.item_price_value);

    }

    public TextView getNameValueTextView() {
        return nameValueTextView;
    }


    public TextView getPriceValueTextView() {
        return priceValueTextView;
    }

}
