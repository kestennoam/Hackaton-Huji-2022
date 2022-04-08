package com.example.hujihackaton47.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hujihackaton47.R;

public class ResultItemViewHolder extends  RecyclerView.ViewHolder{

    private final View rootView;

    // Views
    private TextView nameValueTextView;
    private TextView ownerValueTextView;
    private TextView priceValueTextView;
    private TextView descriptionValueTextView;

    public ResultItemViewHolder(@NonNull View itemView) {
        super(itemView);
        rootView = itemView;

        nameValueTextView = (TextView) rootView.findViewById(R.id.item_name_value);
//        ownerValueTextView = (TextView) rootView.findViewById(R.id.item_owner_value);
        priceValueTextView = (TextView) rootView.findViewById(R.id.item_price_value);
//        descriptionValueTextView = (TextView) rootView.findViewById(R.id.item__description_value);

    }

    public TextView getNameValueTextView() {
        return nameValueTextView;
    }

//    public TextView getOwnerValueTextView() {
//        return ownerValueTextView;
//    }

    public TextView getPriceValueTextView() {
        return priceValueTextView;
    }

//    public TextView getDescriptionValueTextView() {
//        return descriptionValueTextView;
//    }
}
