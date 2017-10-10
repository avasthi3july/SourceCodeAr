package com.augmentedrealityapp.retrofit;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.nio.charset.Charset;

import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by prasharma on 1/11/2017.
 */

public class ErrorUtils {


    public static APIError parseError(Response<ResponseBody> response, RetrofitManager retrofitManager) throws IOException {


        ResponseBody responseBody = response.body();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();
        String responseBodyString = buffer.clone().readString(Charset.forName("UTF-8"));
//        Log.d("TAG", responseBodyString);
        buffer.close();

        if (!checkStatusSeparate(responseBodyString)) {
            Converter<ResponseBody, APIError> converter =
                    RetrofitManager.retrofit
                            .responseBodyConverter(APIError.class, new Annotation[0]);

            APIError error;

            try {
                error = converter.convert(response.body());
            } catch (IOException e) {
                return new APIError();
            }

            return error;
        } else {
            return null;
        }


    }


    private static boolean checkStatusSeparate(String string) {

        try {
            JSONObject mJsonObject = new JSONObject(string);
            if (mJsonObject.has("status")) {
                return mJsonObject.optBoolean("status");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}

