package com.augmentedrealityapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.augmentedrealityapp.R;
import com.augmentedrealityapp.dao.CartData;
import com.augmentedrealityapp.dao.ProductData;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by kavasthi on 8/21/2017.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private ArrayList<CartData> catData;
    private Context mContext;
    private View.OnClickListener removeClick;

    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_row, parent, false);
        return new CartAdapter.ViewHolder(view);
    }

    public CartAdapter(Context mContext, ArrayList<CartData> cat, View.OnClickListener onClickListener) {
        this.mContext = mContext;
        this.catData = cat;
        this.removeClick = onClickListener;
    }

    @Override
    public void onBindViewHolder(CartAdapter.ViewHolder holder, int position) {
        CartData mCartData = catData.get(position);
        Glide.with(mContext)
                .load(mCartData.getProductData().getImage())
                .placeholder(R.drawable.top_icon)
                .error(R.drawable.top_icon)
                .into(holder.product);
        holder.proName.setText(mCartData.getProductData().getProductName());
        holder.proPrize.setText("$ " + mCartData.getPrice());
        holder.quantity.setText("Qty " + mCartData.getQty());
        holder.remove.setOnClickListener(removeClick);
        holder.remove.setTag(mCartData);

    }

    @Override
    public int getItemCount() {
        return catData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView product;
        private TextView proName, proPrize, quantity;
        private Button remove;

        public ViewHolder(View itemView) {
            super(itemView);
            proName = (TextView) itemView.findViewById(R.id.pro_name);
            proPrize = (TextView) itemView.findViewById(R.id.pro_price);
            quantity = (TextView) itemView.findViewById(R.id.qty);
            product = (ImageView) itemView.findViewById(R.id.pro);
            remove = (Button) itemView.findViewById(R.id.remove);
        }
    }
}
