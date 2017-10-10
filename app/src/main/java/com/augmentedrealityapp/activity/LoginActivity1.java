package com.augmentedrealityapp.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.augmentedrealityapp.R;
import com.augmentedrealityapp.retrofit.APIError;
import com.augmentedrealityapp.retrofit.RequestListener;
import com.augmentedrealityapp.retrofit.RetrofitManager;
import com.augmentedrealityapp.util.Constants;
import com.augmentedrealityapp.util.Util;

import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Response;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity1 extends AppCompatActivity implements View.OnClickListener, RequestListener {

    @BindView(R.id.imgLogoLogin)
    ImageView imgLogoLogin;
    @BindView(R.id.edtEmail)
    AppCompatEditText edtEmail;
    @BindView(R.id.textInputEmail)
    TextInputLayout textInputEmail;
    @BindView(R.id.edtPassword)
    AppCompatEditText edtPassword;
    @BindView(R.id.textInputPassword)
    TextInputLayout textInputPassword;
    @BindView(R.id.btnSignIn)
    AppCompatButton btnSignIn;
    @BindView(R.id.btnCreateAccount)
    AppCompatButton btnCreateAccount;
    @BindView(R.id.txtForgotPass)
    TextView txtForgotPass;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.edtUserName)
    AppCompatEditText edtUserName;
    @BindView(R.id.textInputUserName)
    TextInputLayout textInputUserName;

    RetrofitManager retrofit;

    private Map<String, String> loginMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        ButterKnife.bind(this);
        retrofit = RetrofitManager.getInstance();
        initViews();
    }


    /**
     * Method to initialize the views
     */
    private void initViews() {
        btnCreateAccount.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
        txtForgotPass.setOnClickListener(this);

        edtEmail.addTextChangedListener(new MyTextWatcher(textInputEmail));
        edtPassword.addTextChangedListener(new MyTextWatcher(textInputPassword));
        edtUserName.addTextChangedListener(new MyTextWatcher(textInputUserName));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnCreateAccount:
                break;

            case R.id.btnSignIn:
                if (!validateUserName())
                    return;

                if (!validateEmail())
                    return;

                if (!validatePassword())
                    return;

                doLogin();
                break;

            case R.id.txtForgotPass:

                break;
        }
    }


    /**
     * Method to sign in the user
     */
    private void doLogin() {
        try {
            JSONObject mLoginObject = new JSONObject();
            //mLoginObject.put("email", edtUserName.getText().toString());
            mLoginObject.put("email", edtEmail.getText().toString());
            mLoginObject.put("password", edtPassword.getText().toString());
            mLoginObject.put("device_id", Constants.DEVICE_TOKEN);
            mLoginObject.put("device_type", Constants.DEVICE_TYPE);

            //retrofit.doLogin(mLoginObject, this, LoginActivity1.this, Constants.API_TYPE.LOGIN, false);
            showProgress();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Method to validate email address
     */
    private boolean validateEmail() {
        String email = edtEmail.getText().toString().trim();

        if (!Util.isValidEmail(email)) {
            textInputEmail.setErrorEnabled(true);
            textInputEmail.setError(getString(R.string.error_invalid_email));
            requestFocus(edtEmail);
            return false;

        } else {
            textInputEmail.setErrorEnabled(false);
            textInputEmail.setError(null);
        }
        return true;
    }

    /**
     * Method to validate password
     */
    private boolean validatePassword() {
        if (edtPassword.getText().toString().trim().isEmpty()) {
            textInputPassword.setErrorEnabled(true);
            textInputPassword.setError(getString(R.string.error_invalid_password));
            requestFocus(edtPassword);
            return false;
        } else {
            textInputPassword.setErrorEnabled(false);
            textInputPassword.setError(null);
        }

        return true;
    }


    /**
     * Method to validate password
     */
    private boolean validateUserName() {
        if (edtUserName.getText().toString().trim().isEmpty()) {
            textInputUserName.setErrorEnabled(true);
            textInputUserName.setError(getString(R.string.error_invalid_password));
            requestFocus(edtUserName);
            return false;
        } else {
            textInputUserName.setErrorEnabled(false);
            textInputUserName.setError(null);
        }

        return true;
    }

    /**
     * Method to set the focus on view
     *
     * @param view
     */
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        btnSignIn.setText("");
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
        btnSignIn.setText(this.getString(R.string.action_sign_in));
    }

    @Override
    public void onSuccess(Response<ResponseBody> response, int apiType) {

    }

    @Override
    public void onFailure(Response<ResponseBody> response, int apiType) {

    }

    @Override
    public void onApiException(APIError error, Response<ResponseBody> response, int apiType) {

    }


    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.textInputEmail:
                    if (editable.length() == 0)
                        return;
                    validateEmail();
                    break;

                case R.id.textInputUserName:
                    if (editable.length() == 0)
                        return;
                    validateUserName();
                    break;

                case R.id.textInputPassword:
                    if (editable.length() == 0) {
                        textInputPassword.setPasswordVisibilityToggleEnabled(false);
                        txtForgotPass.setVisibility(View.VISIBLE);
                        return;

                    } else {
                        txtForgotPass.setVisibility(View.GONE);
                        textInputPassword.setPasswordVisibilityToggleEnabled(true);
                    }

                    validatePassword();
                    break;
            }
        }

    }
}

