package com.augmentedrealityapp.fragement;

import android.content.Intent;
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

import com.augmentedrealityapp.R;
import com.augmentedrealityapp.activity.HomeActivity;
import com.augmentedrealityapp.activity.LoginActivity;
import com.augmentedrealityapp.dao.BaseResponse;
import com.augmentedrealityapp.dao.ProfileData;
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
 * Created by kavasthi on 9/15/2017.
 */

public class ForgetPasswordView extends Fragment implements RequestListener {
    @BindView(R.id.otp)
    EditText otpNumber;
    @BindView(R.id.passw)
    EditText password;
    @BindView(R.id.confirm_pas)
    EditText confirmPassword;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    private RetrofitManager retrofit;
    private String email;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.forgot_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initViews(view);
    }

    private void initViews(View view) {
        email = getArguments().getString("email");
        retrofit = RetrofitManager.getInstance();
    }

    @OnClick(R.id.submit)
    public void updateData() {
        update();
    }

    public boolean validate() {
        boolean valid = true;

        String passwordUser = password.getText().toString();
        String otpNum = otpNumber.getText().toString();
        String confirmPasswordUser = confirmPassword.getText().toString();


        if (otpNum.isEmpty() || otpNum.length() < 4 || otpNum.length() > 10) {
            otpNumber.setError("Please enter valid otp");
            valid = false;
        } else {
            otpNumber.setError(null);
        }

        if (confirmPasswordUser.isEmpty() || confirmPasswordUser.length() < 4 || confirmPasswordUser.length() > 10) {
            confirmPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            confirmPassword.setError(null);
        }

        if (!passwordUser.equalsIgnoreCase(confirmPasswordUser)) {
            confirmPassword.setError("Password and Confirm Password should same.");
            valid = false;
        } else {
            confirmPassword.setError(null);
        }
        return valid;
    }

    public void update() {
        Log.d("", "Signup");

        if (!validate()) {
            //onSignupFailed();
            return;
        }

        // _signupButton.setEnabled(false);
        showProgress();
        updatePassword("");
       /* final ProgressDialog progressDialog = new ProgressDialog(SignActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();*/


    }

    private void updatePassword(String type) {
        try {
            HashMap<String, String> mLoginObject = new HashMap();
            mLoginObject.put("request", "reset");
            mLoginObject.put("otp_number", otpNumber.getText().toString());
            mLoginObject.put("password", password.getText().toString());
            mLoginObject.put("email", email);

            retrofit.changePassword(mLoginObject, this, getActivity(), RetroService.PASSWORD_UPDATE, false);

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
            if (apiType == RetroService.PASSWORD_UPDATE) {
                hideProgress();
                String response1 = response.body().string();
                BaseResponse baseData = JsonDataParser.getInternalParser(response1, new TypeToken<BaseResponse>() {
                }.getType());
                if (baseData.getStatus()) {
                        LoginView mLoginView = new LoginView();
                        ((LoginActivity) getActivity()).addFragementView(mLoginView);
                        //nameText.setText(baseData.getData().getName());
                        //emailId.setText(baseData.getData().getEmail());
                        //phoneNumber.setText(baseData.getData().getPhoneNumber());
                } else Util.showToast(getActivity(), baseData.getMessage());
            }
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
        Util.showToast(getActivity(), error.getResult());
    }
}
