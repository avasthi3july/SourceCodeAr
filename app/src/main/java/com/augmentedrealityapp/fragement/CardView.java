package com.augmentedrealityapp.fragement;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.augmentedrealityapp.R;
import com.augmentedrealityapp.activity.HomeActivity;
import com.augmentedrealityapp.adapter.CartAdapter;
import com.augmentedrealityapp.adapter.CategoryAdapter;
import com.augmentedrealityapp.adapter.ProductAdapter;
import com.augmentedrealityapp.dao.BaseResponse;
import com.augmentedrealityapp.dao.CartData;
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
import com.augmentedrealityapp.util.Util;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by kavasthi on 9/4/2017.
 */

public class CardView extends Fragment implements RequestListener, View.OnClickListener {
    private RetrofitManager retrofit;
    @BindView(R.id.product_list_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.no_item)
    LinearLayout noItem;
    @BindView(R.id.main_layout)
    RelativeLayout mainLayout;
    @BindView(R.id.bottem_bar)
    RelativeLayout bottemBar;
    @BindView(R.id.total_price)
    TextView totalPrice;
    @BindView(R.id.checkOut)
    Button checkOut;
    ArrayList<CartData> cartData;
    BaseResponse removeData;
    private int price = 0;
    private CartAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.product_list_view, container, false);
        ((HomeActivity) getActivity()).showToolBar();
        ((HomeActivity) getActivity()).getSupportActionBar().setTitle("My Cart");
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
        bottemBar.setVisibility(View.VISIBLE);
        checkOut.setOnClickListener(this);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
       /* mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
               *//* String catId = catData.getData().get(position).getId();
                Constants.CAT_ID = catId;
                getProductList(catId);
                Toast.makeText(getActivity(), "Single Click on position        :" + position,
                        Toast.LENGTH_SHORT).show();*//*
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(getActivity(), "Single Click on position        :" + position,
                        Toast.LENGTH_SHORT).show();
            }
        }));*/

        getCardItem();
    }

    private void getCardItem() {
        try {
            showProgress();
            retrofit.getCartData(Constants.USER_ID, this, getActivity(), RetroService.GET_CART_DATA, false);
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
    }

    private void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    CartData mCartData;
    View.OnClickListener removeItem = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Are you sure want to remove this item?")
                    .setCancelable(false)
                    .setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("REMOVE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mCartData = (CartData) v.getTag();
                            String proId = mCartData.getProductId();
                            removeCartItem(proId);
                        }
                    });
            AlertDialog alert = builder.create();
            alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alert.show();
        }
    };

    private void removeCartItem(String proId) {
        try {
            showProgress();
            HashMap<String, String> mLoginObject = new HashMap();
            mLoginObject.put("user_id", Constants.USER_ID);
            mLoginObject.put("product_id", proId);
            retrofit.removeToCart(mLoginObject, this, getActivity(), RetroService.REMOVE_ITEM, false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(Response<ResponseBody> response, int apiType) {
        try {
            if (apiType == RetroService.GET_CART_DATA) {
                hideProgress();
                String response1 = response.body().string();
                BaseResponse<ArrayList<CartData>> baseData = JsonDataParser.getInternalParser(response1, new TypeToken<BaseResponse<ArrayList<CartData>>>() {
                }.getType());
                if (baseData.getStatus()) {
                    noItem.setVisibility(View.GONE);
                    mainLayout.setVisibility(View.VISIBLE);
                    cartData = new ArrayList<>();
                    cartData.addAll(baseData.getData());
                    adapter = new CartAdapter(getActivity(), cartData, removeItem);
                    mRecyclerView.setAdapter(adapter);
                    totalPriceCalculation();

                } else {
                    noItem.setVisibility(View.VISIBLE);
                    mainLayout.setVisibility(View.GONE);
                }
            } else if (apiType == RetroService.REMOVE_ITEM) {

                hideProgress();
                String response1 = response.body().string();

                removeData = JsonDataParser.getInternalParser(response1, new TypeToken<BaseResponse>() {
                }.getType());
                //((HomeActivity) getActivity()).updateCart(removeData.getCartCount());
                Constants.CART_COUNT = removeData.getCartCount();
                if (removeData.getStatus()) {
                    cartData.remove(mCartData);
                    adapter.notifyDataSetChanged();
                    if (removeData.getCartCount() == 0) {
                        noItem.setVisibility(View.VISIBLE);
                        mainLayout.setVisibility(View.GONE);
                    }

                    Util.showToast(getActivity(), removeData.getMessage());
                    upadItePrice();
                } else Util.showToast(getActivity(), removeData.getMessage());
            }
            //...
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void totalPriceCalculation() {
        for (CartData mCatData : cartData) {
            int qty = mCatData.getQty();
            int priceItem = mCatData.getPrice();
            int finalPrize = qty * priceItem;
            price = price + finalPrize;
        }
        totalPrice.setText("TOTAL $ " + price);
    }

    private void upadItePrice() {
        int qty = mCartData.getQty();
        int priceItem = mCartData.getPrice();
        int removeItemPrice = qty * priceItem;
        int updatedPrice = price - removeItemPrice;
        price = updatedPrice;
        totalPrice.setText("TOTAL $ " + updatedPrice);
    }

    @Override
    public void onFailure(Response<ResponseBody> response, int apiType) {
        hideProgress();
    }

    @Override
    public void onApiException(APIError error, Response<ResponseBody> response, int apiType) {
        hideProgress();
    }

    @Override
    public void onClick(View v) {
        if (v == checkOut) {
            CheckoutView mCheckoutView = new CheckoutView();
            Bundle mBundle = new Bundle();
            mBundle.putSerializable("cartData", cartData);
            mCheckoutView.setArguments(mBundle);
            ((HomeActivity) getActivity()).addFragementView(mCheckoutView);

        }

    }
}
