package com.augmentedrealityapp.fragement;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.augmentedrealityapp.R;
import com.augmentedrealityapp.activity.HomeActivity;
import com.augmentedrealityapp.activity.LoginActivity;
import com.augmentedrealityapp.activity.SignActivity;
import com.augmentedrealityapp.dao.BaseResponse;
import com.augmentedrealityapp.dao.CartData;
import com.augmentedrealityapp.dao.CheckOutData;
import com.augmentedrealityapp.delegates.PaymentStatus;
import com.augmentedrealityapp.retrofit.APIError;
import com.augmentedrealityapp.retrofit.JsonDataParser;
import com.augmentedrealityapp.retrofit.RequestListener;
import com.augmentedrealityapp.retrofit.RetroService;
import com.augmentedrealityapp.retrofit.RetrofitManager;
import com.augmentedrealityapp.util.Constants;
import com.augmentedrealityapp.util.Util;
import com.google.gson.reflect.TypeToken;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringJoiner;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by kavasthi on 9/6/2017.
 */

public class CheckoutView extends Fragment implements RequestListener, PaymentStatus {
    @BindView(R.id.name)
    EditText nameText;
    @BindView(R.id.address)
    EditText address;
    @BindView(R.id.city)
    EditText city;
    @BindView(R.id.pincode)
    EditText pincode;
    @BindView(R.id.phoneNumber)
    EditText phoneNumber;
    @BindView(R.id.btn_submit)
    Button submit;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    private RetrofitManager retrofit;
    private ArrayList<CartData> cartData;
    private String quantity, productId;
    private static final String TAG = "paymentExample";
    /**
     * - Set to PayPalConfiguration.ENVIRONMENT_PRODUCTION to move real money.
     * <p>
     * - Set to PayPalConfiguration.ENVIRONMENT_SANDBOX to use your test credentials
     * from https://developer.paypal.com
     * <p>
     * - Set to PayPalConfiguration.ENVIRONMENT_NO_NETWORK to kick the tires
     * without communicating to PayPal's servers.
     */
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;

    // note that these credentials will differ between live & sandbox environments.
    private static final String CONFIG_CLIENT_ID = "AeIM4MyCTpVT1avncFqlhgwXtwB_rgZcUcMPqxGJE8R34R4fWpLe2x81q1NobCjH7v86zT_FUc58yBw6";

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final int REQUEST_CODE_PROFILE_SHARING = 3;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
            // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("Example Merchant")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.checkout_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((HomeActivity) getActivity()).showToolBar();
        ((HomeActivity) getActivity()).getSupportActionBar().setTitle("CheckOut");
        ButterKnife.bind(this, view);
        initViews(view);
    }

    private void initViews(View view) {
        Intent intent = new Intent(getActivity(), PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        getActivity().startService(intent);
        retrofit = RetrofitManager.getInstance();
        cartData = new ArrayList<>();
        cartData = (ArrayList<CartData>) getArguments().getSerializable("cartData");
        getItem();
    }

    private void getItem() {
        StringBuilder proId = new StringBuilder();
        StringBuilder qty = new StringBuilder();
        for (CartData mCatData : cartData) {
            proId.append(mCatData.getProductId());
            proId.append(",");
            qty.append(mCatData.getQty());
            qty.append(",");
        }
        productId = proId.length() > 0 ? proId.substring(0, proId.length() - 1) : "";
        quantity = qty.length() > 0 ? qty.substring(0, qty.length() - 1) : "";

    }

    @OnClick(R.id.btn_submit)
    public void submit() {
        checkout();

    }

    public boolean validate() {
        boolean valid = true;
        String name = nameText.getText().toString();
        String userAddress = address.getText().toString();
        String mobile = phoneNumber.getText().toString();
        String pinCode = pincode.getText().toString();
        String cityName = city.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            nameText.setError("at least 3 characters");
            valid = false;
        } else {
            nameText.setError(null);
        }

        if (userAddress.isEmpty()) {
            address.setError("Enter Address");
            valid = false;
        } else {
            address.setError(null);
        }

        if (mobile.isEmpty()) {
            phoneNumber.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            phoneNumber.setError(null);
        }

        if (pinCode.isEmpty()) {
            pincode.setError("Enter Valid Pincode");
            valid = false;
        } else {
            pincode.setError(null);
        }

        if (cityName.isEmpty()) {
            city.setError("Enter Valid City");
            valid = false;
        } else {
            city.setError(null);
        }


        return valid;
    }

    public void checkout() {
        if (!validate()) {
            return;
        }
        showProgress();
        checkOut();
    }

    private void checkOut() {
        try {
            HashMap<String, String> mCheckoutObject = new HashMap();
            mCheckoutObject.put("user_id", Constants.USER_ID);
            mCheckoutObject.put("product_ids", productId);
            mCheckoutObject.put("quantity", quantity);
            mCheckoutObject.put("address", address.getText().toString());
            mCheckoutObject.put("pin_code", pincode.getText().toString());
            mCheckoutObject.put("phone_number", phoneNumber.getText().toString());
            retrofit.checkOut(mCheckoutObject, this, getActivity(), RetroService.GET_CHECKOUT, false);

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

    @Override
    public void onSuccess(Response<ResponseBody> response, int apiType) {
        try {
            hideProgress();
            String response1 = response.body().string();
            if (apiType == RetroService.GET_CHECKOUT) {
                BaseResponse<CheckOutData> baseData = JsonDataParser.getInternalParser(response1, new TypeToken<BaseResponse<CheckOutData>>() {
                }.getType());
                if (baseData.getStatus()) {
                    Constants.ORDER_ID = baseData.getData().getOrderId();
                    PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);

        /*
         * See getStuffToBuy(..) for examples of some available payment options.
         */

                    Intent intent = new Intent(getActivity(), PaymentActivity.class);

                    // send the same configuration for restart resiliency
                    intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

                    intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

                    getActivity().startActivityForResult(intent, REQUEST_CODE_PAYMENT);

                } else Util.showToast(getActivity(), baseData.getMessage());
            } else if (apiType == RetroService.CHECK_STATUS) {
                BaseResponse baseData = JsonDataParser.getInternalParser(response1, new TypeToken<BaseResponse>() {
                }.getType());
                if (baseData.getStatus()) {
                    Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.container);
                    ((HomeActivity) getActivity()).clearBackStackInclusive(f.getTag());
                    CategoryView mCategoryView = new CategoryView();
                    ((HomeActivity) getActivity()).addFragementView(mCategoryView);
                    Util.showToast(getActivity(), baseData.getMessage());
                } else Util.showToast(getActivity(), baseData.getMessage());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PayPalPayment getThingToBuy(String paymentIntent) {
        return new PayPalPayment(new BigDecimal("0.01"), "USD", "sample item",
                paymentIntent);
    }

    @Override
    public void onFailure(Response<ResponseBody> response, int apiType) {
        hideProgress();
    }

    @Override
    public void onApiException(APIError error, Response<ResponseBody> response, int apiType) {
        hideProgress();
        Util.showToast(getActivity(), error.getResult());
    }

    @Override
    public void onDestroy() {
        // Stop service when done
        getActivity().stopService(new Intent(getActivity(), PayPalService.class));
        super.onDestroy();
    }

    @Override
    public void checkPayment(String orderId, String state, String amount, String des) {
        paymentReceipt(orderId, state, amount, des);
    }

    private void paymentReceipt(String orderId, String state, String amount, String des) {
        try {
            HashMap<String, String> mCheckoutObject = new HashMap();
            mCheckoutObject.put("user_id", Constants.USER_ID);
            mCheckoutObject.put("order_id", Constants.ORDER_ID);
            mCheckoutObject.put("paypal_order_id", orderId);
            mCheckoutObject.put("platform", "Android");
            mCheckoutObject.put("payment_state", state);
            mCheckoutObject.put("amount", amount);
            mCheckoutObject.put("currency_code", "USD");
            mCheckoutObject.put("short_description", des);
            retrofit.paymentReceipt(mCheckoutObject, this, getActivity(), RetroService.CHECK_STATUS, false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
