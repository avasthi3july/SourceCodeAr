package com.augmentedrealityapp.fragement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.augmentedrealityapp.MainActivity;
import com.augmentedrealityapp.R;
import com.augmentedrealityapp.activity.HomeActivity;
import com.augmentedrealityapp.adapter.CategoryAdapter;
import com.augmentedrealityapp.dao.BaseResponse;
import com.augmentedrealityapp.dao.CatData;
import com.augmentedrealityapp.dao.ProductData;
import com.augmentedrealityapp.delegates.ClickListener;
import com.augmentedrealityapp.delegates.RecyclerTouchListener;
import com.augmentedrealityapp.retrofit.APIError;
import com.augmentedrealityapp.retrofit.JsonDataParser;
import com.augmentedrealityapp.retrofit.RequestListener;
import com.augmentedrealityapp.retrofit.RetroService;
import com.augmentedrealityapp.retrofit.RetrofitManager;
import com.augmentedrealityapp.util.Constants;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by kavasthi on 7/27/2017.
 */

public class CategoryView extends Fragment implements RequestListener {
    private RetrofitManager retrofit;
    @BindView(R.id.product_list_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    BaseResponse<ArrayList<CatData>> catData;
    BaseResponse<ArrayList<ProductData>> proData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.product_list_view, container, false);
        ((HomeActivity) getActivity()).showToolBar();
        ((HomeActivity) getActivity()).getSupportActionBar().setTitle("Category");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initViews(view);
    }

    private void initViews(View view) {
        retrofit = RetrofitManager.getInstance();
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                String catId = catData.getData().get(position).getId();
                Constants.CAT_ID = catId;
                getProductList(catId);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        getCatList();
    }

    private void getCatList() {
        try {
            showProgress();
            retrofit.getCategoryList(this, getActivity(), RetroService.GET_CAT_LIST, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getProductList(String id) {
        try {
            showProgress();
            retrofit.getProductList(id, this, getActivity(), RetroService.GET_PRO_LIST, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
        //btnSignIn.setText("");
    }

    private void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
        // btnSignIn.setText(this.getString(R.string.action_sign_in));
    }

    @Override
    public void onSuccess(Response<ResponseBody> response, int apiType) {
        try {
            if (apiType == RetroService.GET_CAT_LIST) {
                hideProgress();
                String response1 = response.body().string();
                catData = JsonDataParser.getInternalParser(response1, new TypeToken<BaseResponse<ArrayList<CatData>>>() {
                }.getType());
                if (catData.getStatus()) {
                    CategoryAdapter adapter = new CategoryAdapter(getActivity(), catData.getData());
                    mRecyclerView.setAdapter(adapter);

                }
            } else if (apiType == RetroService.GET_PRO_LIST) {

                hideProgress();
                String response1 = response.body().string();
                proData = JsonDataParser.getInternalParser(response1, new TypeToken<BaseResponse<ArrayList<ProductData>>>() {
                }.getType());
                if (proData.getStatus()) {
                    System.out.println(proData.getData().size());

                    ProductListView mProductListView = new ProductListView();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", proData.getData());
                    mProductListView.setArguments(bundle);
                    ((HomeActivity) getActivity()).addFragementView(mProductListView);

                }
            }
            //...
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Response<ResponseBody> response, int apiType) {
        hideProgress();
    }

    @Override
    public void onApiException(APIError error, Response<ResponseBody> response, int apiType) {
        hideProgress();
    }
}
