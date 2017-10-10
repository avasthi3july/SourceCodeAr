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
import android.widget.TextView;

import com.augmentedrealityapp.R;
import com.augmentedrealityapp.activity.HomeActivity;
import com.augmentedrealityapp.activity.LoginActivity;
import com.augmentedrealityapp.activity.SignActivity;
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
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by kavasthi on 9/1/2017.
 */

public class ProfileView extends Fragment implements RequestListener {
    @BindView(R.id.name)
    EditText nameText;
    @BindView(R.id.email)
    EditText emailId;
    @BindView(R.id.phone)
    EditText phoneNumber;
    @BindView(R.id.submit)
    Button updateDetail;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    private RetrofitManager retrofit;
    private Fragment fragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initViews(view);
    }

    private void initViews(View view) {
        fragment = new ProfileView();
        retrofit = RetrofitManager.getInstance();
        getProfile(Constants.USER_ID);
        updateDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

    }

    public boolean validate() {
        boolean valid = true;

        String name = nameText.getText().toString();
        String email = emailId.getText().toString();
        String mobile = phoneNumber.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            nameText.setError("at least 3 characters");
            valid = false;
        } else {
            nameText.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailId.setError("enter a valid email address");
            valid = false;
        } else {
            emailId.setError(null);
        }

        if (mobile.isEmpty()) {
            phoneNumber.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            phoneNumber.setError(null);
        }
        return valid;
    }

    public void signup() {
        Log.d("", "Signup");

        if (!validate()) {
            //onSignupFailed();
            return;
        }

        // _signupButton.setEnabled(false);
        showProgress();
        updateProfile();
       /* final ProgressDialog progressDialog = new ProgressDialog(SignActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();*/

        String name = nameText.getText().toString();
        String email = emailId.getText().toString();
        String mobile = phoneNumber.getText().toString();

    }

    private void updateProfile() {
        try {
            HashMap<String, String> mLoginObject = new HashMap();
            mLoginObject.put("email", emailId.getText().toString());
            mLoginObject.put("name", nameText.getText().toString());
            mLoginObject.put("phone_number", phoneNumber.getText().toString());
            mLoginObject.put("user_id", Constants.USER_ID);
            mLoginObject.put("device_id", Constants.DEVICE_TOKEN);
            mLoginObject.put("device_type", Constants.DEVICE_TYPE);

            retrofit.editProfile(mLoginObject, this, getActivity(), RetroService.EDIT_PROFILE, false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getProfile(String id) {
        try {
            showProgress();
            retrofit.getProfile(id, this, getActivity(), RetroService.GET_PROFILE, false);
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
            if (apiType == RetroService.GET_PROFILE) {
                hideProgress();
                String response1 = response.body().string();
                BaseResponse<ProfileData> baseData = JsonDataParser.getInternalParser(response1, new TypeToken<BaseResponse<ProfileData>>() {
                }.getType());
                if (baseData.getStatus()) {
                    if (baseData.getData() != null) {
                        nameText.setText(baseData.getData().getName());
                        emailId.setText(baseData.getData().getEmail());
                        phoneNumber.setText(baseData.getData().getPhoneNumber());
                    }

                } else Util.showToast(getActivity(), baseData.getMessage());
            } else if (apiType == RetroService.EDIT_PROFILE) {
                hideProgress();
                String response1 = response.body().string();
                BaseResponse baseData = JsonDataParser.getInternalParser(response1, new TypeToken<BaseResponse>() {
                }.getType());
                if (baseData.getStatus()) {
                    Util.showToast(getActivity(), baseData.getMessage());
                    ((HomeActivity) getActivity()).clearBackStackInclusive(fragment.getClass().getName());
                    CategoryView myPictureView = new CategoryView();
                    ((HomeActivity) getActivity()).addFragementView(myPictureView);
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
