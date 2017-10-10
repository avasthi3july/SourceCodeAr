package com.augmentedrealityapp.retrofit;


import android.accounts.NetworkErrorException;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.augmentedrealityapp.BuildConfig;
import com.augmentedrealityapp.R;
import com.augmentedrealityapp.delegates.ApplicationAugmented;
import com.augmentedrealityapp.delegates.OnRetryCallback;
import com.augmentedrealityapp.util.Constants;
import com.augmentedrealityapp.util.NoConnectivityException;
import com.augmentedrealityapp.util.Util;

import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;


/**
 * Created by  on 3/24/2017.
 */

public class RetrofitManager implements OnRetryCallback {

    public static Retrofit retrofit = null;
    public static RetrofitManager retrofitManager = null;
    public static RetroService retroService = null;
    private Call<ResponseBody> call = null;
    private Callback<ResponseBody> mCallback = null;
    private final String format = "json";
    private String TAG = "RetrofitManager";
    AppCompatActivity activity = null;
    private OnRetryCallback mRetryCallback = this;
    private static String BASE_URL;

    static {
        if (BuildConfig.DEBUG) {
            BASE_URL = Constants.BASE_URL_TEST;
        } else {
            BASE_URL = Constants.BASE_URL_LIVE;
        }
    }

    private RetrofitManager() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.readTimeout(10, TimeUnit.SECONDS);
        httpClient.connectTimeout(10, TimeUnit.SECONDS);
        httpClient.addInterceptor(logging);
        httpClient.addInterceptor(new ConnectivityInterceptor(ApplicationAugmented.get()));
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        retroService = retrofit.create(RetroService.class);

    }

    public static RetrofitManager getInstance() {
        if (retrofitManager == null) {
            retrofitManager = new RetrofitManager();
        }
        return retrofitManager;
    }


    /**
     * Method to resolve the API callbacks
     *
     * @param mRequestListener
     * @param mContext
     * @param mApiType
     * @param showProgress
     */
    public void performCallback(
            final RequestListener mRequestListener,
            final Context mContext,
            final int mApiType,
            final boolean showProgress) {

        activity = (AppCompatActivity) mContext;

        if (mContext == null) {
            Util.DEBUG_LOG(1, TAG, "context is null");
            return;
        }

        if (showProgress) {
            Dialogs.showProgressDialog(mContext, "Please wait..");
        }

        mCallback = new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    APIError error = null;
                    try {
                        //error = ErrorUtils.parseError(response, retrofitManager);
                        //if (error == null) {
                            mRequestListener.onSuccess(response, mApiType);

                        /*} else {
                            mRequestListener.onApiException(error, response, mApiType);
                        }*/

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
//                    Util.DEBUG_LOG(2, TAG, response.errorBody().string());
                    mRequestListener.onFailure(response, mApiType);
                }

                Dialogs.hideProgressDialog(activity);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                try {
                    if (t instanceof SocketTimeoutException) {
                        Dialogs.showTryAgainDialog(mContext, mContext.getString(R.string.ERROR_SOCKET), mRetryCallback);

                    } else if (t instanceof NoConnectivityException) {
                        Dialogs.showAlert(mContext, mContext.getString(R.string.ERROR_INTERNET));

                    } else if (t instanceof NetworkErrorException) {
                        Dialogs.showAlert(mContext, t.getMessage());
                    }

                    Dialogs.hideProgressDialog(activity);
                } catch (Exception e) {
                    e.printStackTrace();
                }


//                mRequestListener.onFailure(t, mApiType);


            }
        };

        call.enqueue(mCallback);
    }


    @Override
    public void OnRetry(boolean isRetry) {
        if (isRetry) {
            Dialogs.showProgressDialog(activity, "Please wait..");
            call.clone().enqueue(mCallback);
        } else {

        }
    }

    /**
     * Method to cancel the call
     */
    public void cancelRequest() {
        call.cancel();
    }

    /**
     * Method to call the API for login
     *
     * @param mRequestListener
     * @param mContext
     * @param mApiType
     * @param showProgress
     */
    public void doLogin(
            HashMap<String ,String> mJsonObject, final RequestListener mRequestListener,
            final Context mContext,
            final int mApiType,
            final boolean showProgress) {

        call = retroService.doLogin(mJsonObject);
        performCallback(mRequestListener, mContext, mApiType, showProgress);

    }

    public void doSignup(
            HashMap<String,String> mJsonObject, final RequestListener mRequestListener,
            final Context mContext,
            final int mApiType,
            final boolean showProgress) {

        call = retroService.signUp(mJsonObject);
        performCallback(mRequestListener, mContext, mApiType, showProgress);

    }
    public void checkOut(
            HashMap<String,String> mJsonObject, final RequestListener mRequestListener,
            final Context mContext,
            final int mApiType,
            final boolean showProgress) {

        call = retroService.checkOut(mJsonObject);
        performCallback(mRequestListener, mContext, mApiType, showProgress);

    }
    public void paymentReceipt(
            HashMap<String,String> mJsonObject, final RequestListener mRequestListener,
            final Context mContext,
            final int mApiType,
            final boolean showProgress) {

        call = retroService.paymentReceipt(mJsonObject);
        performCallback(mRequestListener, mContext, mApiType, showProgress);

    }
    public void addToCart(
            HashMap<String,String> mJsonObject, final RequestListener mRequestListener,
            final Context mContext,
            final int mApiType,
            final boolean showProgress) {

        call = retroService.addToCart(mJsonObject);
        performCallback(mRequestListener, mContext, mApiType, showProgress);

    }
    public void removeToCart(
            HashMap<String,String> mJsonObject, final RequestListener mRequestListener,
            final Context mContext,
            final int mApiType,
            final boolean showProgress) {

        call = retroService.removeItem(mJsonObject);
        performCallback(mRequestListener, mContext, mApiType, showProgress);

    }
    public void editProfile(
            HashMap<String,String> mJsonObject, final RequestListener mRequestListener,
            final Context mContext,
            final int mApiType,
            final boolean showProgress) {

        call = retroService.editProfile(mJsonObject);
        performCallback(mRequestListener, mContext, mApiType, showProgress);

    }
    public void changePassword(
            HashMap<String,String> mJsonObject, final RequestListener mRequestListener,
            final Context mContext,
            final int mApiType,
            final boolean showProgress) {

        call = retroService.changePassword(mJsonObject);
        performCallback(mRequestListener, mContext, mApiType, showProgress);

    }
    public void verifyOtp(
            HashMap<String,String> mJsonObject, final RequestListener mRequestListener,
            final Context mContext,
            final int mApiType,
            final boolean showProgress) {

        call = retroService.verifyOtp(mJsonObject);
        performCallback(mRequestListener, mContext, mApiType, showProgress);

    }
    public void getProductList(
            String catId, final RequestListener mRequestListener,
            final Context mContext,
            final int mApiType,
            final boolean showProgress) {

        call = retroService.productList(catId);
        performCallback(mRequestListener, mContext, mApiType, showProgress);


    }
    public void getProductDetail(
            String pId, final RequestListener mRequestListener,
            final Context mContext,
            final int mApiType,
            final boolean showProgress) {

        call = retroService.proDetail(pId);
        performCallback(mRequestListener, mContext, mApiType, showProgress);

    }

    public void regenerateOtp(
            String pId, final RequestListener mRequestListener,
            final Context mContext,
            final int mApiType,
            final boolean showProgress) {

        call = retroService.regenerateOtp(pId);
        performCallback(mRequestListener, mContext, mApiType, showProgress);

    }
    public void forgotPassword(
            String email, final RequestListener mRequestListener,
            final Context mContext,
            final int mApiType,
            final boolean showProgress) {

        call = retroService.forgotPassword(email);
        performCallback(mRequestListener, mContext, mApiType, showProgress);

    }

    public void getProfile(
            String pId, final RequestListener mRequestListener,
            final Context mContext,
            final int mApiType,
            final boolean showProgress) {

        call = retroService.getUserProfile(pId);
        performCallback(mRequestListener, mContext, mApiType, showProgress);

    }
    public void getCategoryList(final RequestListener mRequestListener, final Context mContext, final int mApiType, final boolean showProgress) {

        call = retroService.getCategoryList();
        performCallback(mRequestListener, mContext, mApiType, showProgress);

    }
    public void getCartData(
            String uId, final RequestListener mRequestListener,
            final Context mContext,
            final int mApiType,
            final boolean showProgress) {

        call = retroService.getCartData(uId);
        performCallback(mRequestListener, mContext, mApiType, showProgress);

    }
    public Observable<ResponseBody> getStarredRepos(@NonNull HashMap<String ,String > userName) {
        return retroService.getStarredRepositories(userName);
    }
}
