package com.augmentedrealityapp.fragement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.augmentedrealityapp.R;
import com.augmentedrealityapp.activity.HomeActivity;
import com.augmentedrealityapp.ar.model3D.view.ModelActivity;
import com.augmentedrealityapp.dao.BaseResponse;
import com.augmentedrealityapp.dao.CartData;
import com.augmentedrealityapp.dao.ProductData;
import com.augmentedrealityapp.retrofit.APIError;
import com.augmentedrealityapp.retrofit.JsonDataParser;
import com.augmentedrealityapp.retrofit.RequestListener;
import com.augmentedrealityapp.retrofit.RetroService;
import com.augmentedrealityapp.retrofit.RetrofitManager;
import com.augmentedrealityapp.util.Constants;
import com.augmentedrealityapp.util.Util;
import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by kavasthi on 7/27/2017.
 */

public class ProductDetailView extends Fragment implements RequestListener, View.OnClickListener {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.expandedImage)
    ImageView exImageView;
    @BindView(R.id.product_name)
    TextView productName;
    @BindView(R.id.product_prize)
    TextView productPrize;
    @BindView(R.id.qty)
    TextView qty;
    @BindView(R.id.des)
    TextView description;
    @BindView(R.id.add_cart)
    Button addCart;
    @BindView(R.id.buy_now)
    Button buyNow;
    @BindView(R.id.plus_qty)
    ImageView plus;
    @BindView(R.id.minus_qty)
    ImageView minus;
    @BindView(R.id.ar_view)
    ImageView arView;
    private RetrofitManager retrofit;
    private SharedPreferences pref;
    private String userId;
    private ProductData mProductDetail;
    private int counter = 1;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.detail_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initViews(view);
        AppBarLayout appBarLayout = (AppBarLayout) view.findViewById(R.id.app_bar);
        appBarLayout.setExpanded(false, false);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
//              Util.showToast(getActivity(), "ProductDetailView Soon");
            }
        });

    }

    @Override
    public void onDestroyView() {
        ((HomeActivity) getActivity()).setUpToolbar();
        ((HomeActivity) getActivity()).showToolBar();
        super.onDestroyView();

    }

    @OnClick(R.id.add_cart)
    public void addCart() {

        addTOCart();
    }

    private void addTOCart() {
        try {
            showProgress();
            HashMap<String, String> mLoginObject = new HashMap();
            mLoginObject.put("user_id", Constants.USER_ID);
            mLoginObject.put("category_id", Constants.CAT_ID);
            mLoginObject.put("product_id", mProductDetail.getId());
            mLoginObject.put("quantity", qty.getText().toString());
            retrofit.addToCart(mLoginObject, this, getActivity(), RetroService.ADD_TO_CART, false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initViews(View view) {
        pref = Util.getSharedPreferences(getActivity());
        retrofit = RetrofitManager.getInstance();
        userId = pref.getString("id", "");
        plus.setOnClickListener(this);
        minus.setOnClickListener(this);
        arView.setOnClickListener(this);
        mProductDetail = (ProductData) getArguments().getSerializable("data");
        ((HomeActivity) getActivity()).hideToolBar();
        ((HomeActivity) getActivity()).setSupportActionBar(mToolbar);
        if (((HomeActivity) getActivity()).getSupportActionBar() != null)
            ((HomeActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle("Product Detail");
        Glide.with(getActivity())
                .load(mProductDetail.getImage())
                .placeholder(R.drawable.top_icon)
                .error(R.drawable.top_icon)
                .into(exImageView);
        productName.setText(mProductDetail.getName());
        productPrize.setText("Price : " + mProductDetail.getPrice() + " USD");
        description.setText(Html.fromHtml("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum"));
        /*
        getActivity().setSupportActionBar(myToolbar);

        getActivity().getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout ctl = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ctl.setTitle("Best Coupons Deals");


        TextView tv =(TextView)view.findViewById(R.id.coupons_lst);
        tv.setText(CouponStoreData.arrayOfCoupons.toString());
    */
    }

    private void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onSuccess(Response<ResponseBody> response, int apiType) {
        hideProgress();
        try {

            String response1 = response.body().string();
            BaseResponse baseData = JsonDataParser.getInternalParser(response1, new TypeToken<BaseResponse>() {
            }.getType());
            if (apiType == RetroService.ADD_TO_CART) {
                if (baseData.getStatus()) {
                    Util.showToast(getActivity(), baseData.getMessage());
                    Constants.CART_COUNT = baseData.getCartCount();
                    ((HomeActivity) getActivity()).updateCart(baseData.getCartCount());
                } else Util.showToast(getActivity(), baseData.getMessage());

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

    @Override
    public void onClick(View v) {
        if (v == plus) {
            counter = ++counter;
            qty.setText("" + counter);
        } else if (v == minus) {
            if (counter >= 2) {
                counter = --counter;
                qty.setText("" + counter);
            }
        } else if (v == arView) {
            Intent intent = new Intent(getActivity().getApplicationContext(), ModelActivity.class);
            Bundle b = new Bundle();
            b.putString("assetDir", "models");
            b.putString("assetFilename", "truck.obj");
            b.putString("immersiveMode", "true");
            intent.putExtras(b);
            getActivity().startActivity(intent);
        }
    }
}
