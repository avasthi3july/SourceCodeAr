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
 * Created by kavasthi on 8/9/2017.
 */

public class SignUpView extends Fragment implements RequestListener {
    @BindView(R.id.name)
    EditText nameText;
    @BindView(R.id.email)
    EditText emailId;
    @BindView(R.id.phone)
    EditText phoneNumber;
    @BindView(R.id.password)
    EditText userPassword;
    @BindView(R.id.btn_signup)
    Button signupButton;
    @BindView(R.id.login)
    TextView loginLink;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    private RetrofitManager retrofit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sign_up, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initViews(view);
    }

    private void initViews(View view) {
        retrofit = RetrofitManager.getInstance();
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // VerifyOtpView mVerifyOtpView = new VerifyOtpView();
                //((SignActivity) getActivity()).addFragementView(mVerifyOtpView);
                signup();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
               /* Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();*/
                LoginView mLoginView = new LoginView();

                ((LoginActivity) getActivity()).clearBackStackInclusive(mLoginView.getTag());
                ((LoginActivity) getActivity()).addFragementViewWithOutSatck(mLoginView);
                //getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        String name = nameText.getText().toString();
        String email = emailId.getText().toString();
        String mobile = phoneNumber.getText().toString();
        String password = userPassword.getText().toString();

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

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            userPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            userPassword.setError(null);
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
        doSignUp();
       /* final ProgressDialog progressDialog = new ProgressDialog(SignActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();*/

        String name = nameText.getText().toString();
        String email = emailId.getText().toString();
        String mobile = phoneNumber.getText().toString();
        String password = userPassword.getText().toString();

        // TODO: Implement your own signup logic here.

       /* new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);*/
    }

    private void doSignUp() {
        try {
            HashMap<String, String> mLoginObject = new HashMap();
            //mLoginObject.put("email", edtUserName.getText().toString());
            mLoginObject.put("name", nameText.getText().toString());
            mLoginObject.put("email", emailId.getText().toString());
            mLoginObject.put("password", userPassword.getText().toString());
            mLoginObject.put("phone_number", phoneNumber.getText().toString());
            mLoginObject.put("device_id", Constants.DEVICE_TOKEN);
            mLoginObject.put("device_type", Constants.DEVICE_TYPE);
            Constants.EMAIL_ID = emailId.getText().toString();
            retrofit.doSignup(mLoginObject, this, getActivity(), RetroService.GET_SIGNUP, false);

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
            BaseResponse baseData = JsonDataParser.getInternalParser(response1, new TypeToken<BaseResponse>() {
            }.getType());
            if (baseData.getStatus()) {
                // Intent i = new Intent(getActivity(), HomeActivity.class);
                //startActivity(i);
                VerifyOtpView mVerifyOtpView = new VerifyOtpView();
                ((LoginActivity) getActivity()).addFragementViewWithOutSatck(mVerifyOtpView);
                Util.showToast(getActivity(), baseData.getMessage());

            } else Util.showToast(getActivity(), baseData.getMessage());

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
