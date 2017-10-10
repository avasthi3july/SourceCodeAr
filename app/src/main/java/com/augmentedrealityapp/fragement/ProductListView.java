package com.augmentedrealityapp.fragement;

import android.content.RestrictionsManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.augmentedrealityapp.R;
import com.augmentedrealityapp.activity.HomeActivity;
import com.augmentedrealityapp.adapter.CategoryAdapter;
import com.augmentedrealityapp.adapter.ProductAdapter;
import com.augmentedrealityapp.dao.BaseResponse;
import com.augmentedrealityapp.dao.ProductData;
import com.augmentedrealityapp.delegates.ClickListener;
import com.augmentedrealityapp.delegates.NotifyActionBar;
import com.augmentedrealityapp.delegates.RecyclerTouchListener;
import com.augmentedrealityapp.retrofit.APIError;
import com.augmentedrealityapp.retrofit.JsonDataParser;
import com.augmentedrealityapp.retrofit.RequestListener;
import com.augmentedrealityapp.retrofit.RetroService;
import com.augmentedrealityapp.retrofit.RetrofitManager;
import com.augmentedrealityapp.util.Util;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by kavasthi on 7/27/2017.
 */

public class ProductListView extends Fragment implements View.OnClickListener, RequestListener {
    @BindView(R.id.product_list_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    private RetrofitManager retrofit;
    BaseResponse<ProductData> proData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_list_view, container, false);
        ((HomeActivity) getActivity()).getSupportActionBar().setTitle("Product");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initViews(view);
    }

    private void getProductDetail(String id) {
        try {
            showProgress();
            retrofit.getProductDetail(id, this, getActivity(), RetroService.GET_PRO_LIST, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initViews(View view) {
        retrofit = RetrofitManager.getInstance();
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        final ArrayList<ProductData> mProductData = (ArrayList<ProductData>) getArguments().getSerializable("data");
        ProductAdapter adapter = new ProductAdapter(getActivity(), mProductData);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                String catId = mProductData.get(position).getId();
                getProductDetail(catId);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    private void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onSuccess(Response<ResponseBody> response, int apiType) {
        try {

            hideProgress();
            String response1 = response.body().string();
            proData = JsonDataParser.getInternalParser(response1, new TypeToken<BaseResponse<ProductData>>() {
            }.getType());
            if (proData.getStatus()) {
                ProductDetailView mProductDetailView = new ProductDetailView();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", proData.getData());
                mProductDetailView.setArguments(bundle);
                ((HomeActivity) getActivity()).addFragementView(mProductDetailView);
            }
        } catch (Exception e) {

        }

    }

    @Override
    public void onFailure(Response<ResponseBody> response, int apiType) {

    }

    @Override
    public void onApiException(APIError error, Response<ResponseBody> response, int apiType) {

    }
}
