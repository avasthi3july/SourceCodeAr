package com.augmentedrealityapp.fragement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.augmentedrealityapp.R;
import com.augmentedrealityapp.activity.HomeActivity;
import com.augmentedrealityapp.activity.SignActivity;
import com.augmentedrealityapp.dao.BaseResponse;
import com.augmentedrealityapp.dao.UserData;
import com.augmentedrealityapp.retrofit.APIError;
import com.augmentedrealityapp.retrofit.JsonDataParser;
import com.augmentedrealityapp.retrofit.RequestListener;
import com.augmentedrealityapp.retrofit.RetroService;
import com.augmentedrealityapp.retrofit.RetrofitManager;
import com.augmentedrealityapp.util.Constants;
import com.augmentedrealityapp.util.Util;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by kavasthi on 8/9/2017.
 */

public class VerifyOtpView extends Fragment implements RequestListener, View.OnClickListener {
    @BindView(R.id.password)
    EditText otp;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    private RetrofitManager retrofit;
    private SharedPreferences pref;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.verification_screen, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initViews(view);
    }

    @OnClick(R.id.resend_otp)
    public void resendOtp() {
        try {
            showProgress();
            retrofit.regenerateOtp(Constants.EMAIL_ID, this, getActivity(), RetroService.RESEND_OTP, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void verifyOtp() {
        try {
            if (otp.getText().toString() != null && otp.getText().toString().length() > 0) {
                showProgress();
                HashMap<String, String> mLoginObject = new HashMap();
                mLoginObject.put("otp_number", otp.getText().toString());
                retrofit.verifyOtp(mLoginObject, this, getActivity(), RetroService.OTP_VERIFY, false);
            } else Util.showToast(getActivity(), "Please enter otp.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initViews(View view) {
        retrofit = RetrofitManager.getInstance();
        pref = Util.getSharedPreferences(getActivity());
        submit.setOnClickListener(this);
    }

    private void showProgress() {
        if (mProgressBar != null)
            mProgressBar.setVisibility(View.VISIBLE);
        //btnSignIn.setText("");
    }

    private void hideProgress() {
        if (mProgressBar != null)
            mProgressBar.setVisibility(View.GONE);
        // btnSignIn.setText(this.getString(R.string.action_sign_in));
    }

    @Override
    public void onSuccess(Response<ResponseBody> response, int apiType) {
        try {


            hideProgress();
            String response1 = response.body().string();
            BaseResponse<UserData> baseData = JsonDataParser.getInternalParser(response1, new TypeToken<BaseResponse<UserData>>() {
            }.getType());
            if (apiType == RetroService.OTP_VERIFY) {
                if (baseData.getStatus()) {
                    Util.showToast(getActivity(), baseData.getMessage());
                    Intent i = new Intent(getActivity(), HomeActivity.class);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("isRegister", true);
                    editor.putString("email", baseData.getData().getEmail());
                    editor.putString("userId", baseData.getData().getId());
                    editor.putString("userName", baseData.getData().getName());
                    editor.putInt("cardCount", baseData.getData().getCartCount());
                    editor.commit();
                    Constants.USER_ID = baseData.getData().getId();
                    startActivity(i);
                    getActivity().finish();
                } else Util.showToast(getActivity(), baseData.getMessage());
            } else if (apiType == RetroService.RESEND_OTP) {
                Util.showToast(getActivity(), baseData.getMessage());
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onFailure(Response<ResponseBody> response, int apiType) {

    }

    @Override
    public void onApiException(APIError error, Response<ResponseBody> response, int apiType) {
        hideProgress();
    }

    @Override
    public void onClick(View v) {
        verifyOtp();
    }
}
