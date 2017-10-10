package com.augmentedrealityapp.retrofit;


import com.augmentedrealityapp.util.Constants;

import okhttp3.ResponseBody;
import retrofit2.Response;


/**
 * Created by prasharma on 3/24/2017.
 */

public  interface RequestListener {

    void onSuccess(Response<ResponseBody> response, int apiType);

    void onFailure(Response<ResponseBody> response, int apiType);

    void onApiException(APIError error, Response<ResponseBody> response, int apiType);

}
