package com.augmentedrealityapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.augmentedrealityapp.R;
import com.augmentedrealityapp.dao.CatData;
import com.augmentedrealityapp.dao.ProductData;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by kavasthi on 8/21/2017.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private ArrayList<ProductData> catData;
    private Context mContext;

    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pro_list, parent, false);
        return new ProductAdapter.ViewHolder(view);
    }

    public


    ProductAdapter(Context mContext, ArrayList<ProductData> cat) {
        this.mContext = mContext;
        this.catData = cat;
    }

    @Override
    public void onBindViewHolder(ProductAdapter.ViewHolder holder, int position) {
        ProductData mProductData = catData.get(position);
        Glide.with(mContext)
                .load(mProductData.getImage())
                .placeholder(R.drawable.top_icon)
                .error(R.drawable.top_icon)
                .into(holder.product);
        holder.proName.setText(mProductData.getName());
        holder.proPrize.setText(mProductData.getPrice());

    }

    @Override
    public int getItemCount() {
        return catData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView product;
        private TextView proName, proPrize;

        public ViewHolder(View itemView) {
            super(itemView);
            proName = (TextView) itemView.findViewById(R.id.pro_name);
            proPrize = (TextView) itemView.findViewById(R.id.pro_price);
            product = (ImageView) itemView.findViewById(R.id.pro);
        }
    }
}
