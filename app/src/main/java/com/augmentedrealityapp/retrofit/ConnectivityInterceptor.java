package com.augmentedrealityapp.retrofit;

import android.content.Context;

import com.augmentedrealityapp.util.NetworkUtil;
import com.augmentedrealityapp.util.NoConnectivityException;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by prasharma on 3/24/2017.
 */

public class ConnectivityInterceptor implements Interceptor {

    private Context mContext;

    public ConnectivityInterceptor(Context context) {
        mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        //chain.addHeader("Content-Type", "application/x-www-form-urlencoded");
        //chain.addHeader("Accept", "application/json");
        if (!NetworkUtil.isOnline(mContext)) {
            throw new NoConnectivityException();
        }

        Request.Builder builder = chain.request().newBuilder();
        return chain.proceed(builder.build());


        //Request request = chain.request().newBuilder().addHeader("Content-Type", "application/x-www-form-urlencoded").build();
        //return chain.proceed(request);
    }

}