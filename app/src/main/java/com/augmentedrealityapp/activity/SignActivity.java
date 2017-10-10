package com.augmentedrealityapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.augmentedrealityapp.R;
import com.augmentedrealityapp.dao.BaseResponse;
import com.augmentedrealityapp.fragement.CategoryView;
import com.augmentedrealityapp.fragement.SignUpView;
import com.augmentedrealityapp.retrofit.APIError;
import com.augmentedrealityapp.retrofit.JsonDataParser;
import com.augmentedrealityapp.retrofit.RequestListener;
import com.augmentedrealityapp.retrofit.RetrofitManager;
import com.augmentedrealityapp.util.Constants;
import com.augmentedrealityapp.util.Util;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class SignActivity extends BaseActivity   {
    private static final String TAG = "SignActivity";

   @BindView(R.id.container)
   FrameLayout frameLayout;
   private SignUpView mSignUpView;

    @Override
    protected int myView() {
        return R.layout.content_main2;
    }
    public void addFragementView(Fragment fragment) {
        replaceFragment(R.id.container, fragment, fragment.getClass().getName(), fragment.getClass().getName());
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mSignUpView = new SignUpView();
        addFragmentWithOutAddStack(R.id.container, mSignUpView, "");
       /* signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });*/
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.container);

            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                finish();
            } else {

                super.onBackPressed();
            }

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
    /*public void signup() {
        Log.d(TAG, "Signup");

        *//*if (!validate()) {
            onSignupFailed();
            return;
        }*//*

        // _signupButton.setEnabled(false);
        showProgress();
        doSignUp();
       *//* final ProgressDialog progressDialog = new ProgressDialog(SignActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();*//*

        String name = nameText.getText().toString();
        String email = emailId.getText().toString();
        String mobile = phoneNumber.getText().toString();
        String password = userPassword.getText().toString();

        // TODO: Implement your own signup logic here.

       *//* new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);*//*
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

            retrofit.doSignup(mLoginObject, SignActivity.this, SignActivity.this, Constants.API_TYPE.LOGIN, false);

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

    public void onSignupSuccess() {
        signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

   *//* public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        signupButton.setEnabled(true);
    }*//*

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

      *//*  if (address.isEmpty()) {
            addressText.setError("Enter Valid Address");
            valid = false;
        } else {
            _ddressText.setError(null);
        }*//*


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailId.setError("enter a valid email address");
            valid = false;
        } else {
            emailId.setError(null);
        }

        if (mobile.isEmpty() || mobile.length() != 10) {
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

    @Override
    public void onSuccess(Response<ResponseBody> response, Constants.API_TYPE apiType) {
        try {
            hideProgress();
            String response1 = response.body().string();
            BaseResponse baseData = JsonDataParser.getInternalParser(response1, new TypeToken<BaseResponse>() {
            }.getType());
            if (baseData.getStatus()) {
                Intent i = new Intent(this, HomeActivity.class);
                startActivity(i);

            } else Util.showToast(this, baseData.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onFailure(Response<ResponseBody> response, Constants.API_TYPE apiType) {

    }

    @Override
    public void onApiException(APIError error, Response<ResponseBody> response, Constants.API_TYPE apiType) {

    }*/
}