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
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by kavasthi on 8/9/2017.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private ArrayList<CatData> catData;
    private Context mContext;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_row, parent, false);
        return new ViewHolder(view);
    }

    public CategoryAdapter(Context mContext, ArrayList<CatData> cat) {
        this.mContext = mContext;
        this.catData = cat;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CatData mCatData = catData.get(position);
        holder.catName.setText(mCatData.getTaxCategory());
        Glide.with(mContext)
                .load(mCatData.getCatImage())
                .placeholder(R.drawable.top_icon)
                .error(R.drawable.top_icon)
                .into(holder.catImage);

    }

    @Override
    public int getItemCount() {
        return catData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView catImage;
        private TextView catName;

        public ViewHolder(View itemView) {
            super(itemView);
            catName = (TextView) itemView.findViewById(R.id.cat_name);
            catImage = (ImageView) itemView.findViewById(R.id.cat_image);
        }
    }
}
