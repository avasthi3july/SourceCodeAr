package com.augmentedrealityapp.fragement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.augmentedrealityapp.MainActivity;
import com.augmentedrealityapp.R;
import com.augmentedrealityapp.activity.HomeActivity;
import com.augmentedrealityapp.activity.LoginActivity;
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
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kavasthi on 9/15/2017.
 */

public class LoginView extends Fragment implements RequestListener, View.OnClickListener {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    RetrofitManager retrofit;
    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.textInputLayoutPassword)
    TextInputLayout textInputLayoutPassword;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.link_signup)
    TextView _signupLink;
    @BindView(R.id.forgot_pw)
    TextView forgotPassword;
    @BindView(R.id.skip_login)
    TextView skipLogin;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    Subscription subscription;
    private SharedPreferences pref;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initViews(view);
    }

    private void initViews(View view) {
        retrofit = RetrofitManager.getInstance();
        pref = Util.getSharedPreferences(getActivity());
        _passwordText.addTextChangedListener(editText);
        textInputLayoutPassword.setPasswordVisibilityToggleEnabled(false);
        skipLogin.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        _signupLink.setOnClickListener(this);
        _loginButton.setOnClickListener(this);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_view, container, false);
    }

    private void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }
        showProgress();
        doLogin();
        //getStarredRepos();
        //_loginButton.setEnabled(false);

        /*final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);*/
    }


  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == getActivity().RESULT_OK) {
                getActivity().finish();
            }
        }
    }*/

    /* @Override
     public void onBackPressed() {
         moveTaskToBack(true);
     }
 */
    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        getActivity().finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getActivity().getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    private void getStarredRepos() {
        HashMap<String, String> hashmap = new HashMap<>();
        //mLoginObject.put("email", edtUserName.getText().toString());
        hashmap.put("email", _emailText.getText().toString());
        hashmap.put("password", _passwordText.getText().toString());
        hashmap.put("device_id", Constants.DEVICE_TOKEN);
        hashmap.put("device_type", Constants.DEVICE_TYPE);
        subscription = RetrofitManager.getInstance().getStarredRepos(hashmap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted");

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        System.out.println("onNext" + responseBody);
                    }
                });
    }

    private void doLogin() {
        try {
            HashMap<String, String> hashmap = new HashMap<>();
            hashmap.put("email", _emailText.getText().toString());
            hashmap.put("password", _passwordText.getText().toString());
            hashmap.put("device_id", Constants.DEVICE_TOKEN);
            hashmap.put("device_type", Constants.DEVICE_TYPE);
            retrofit.doLogin(hashmap, this, getActivity(), RetroService.GET_LOGIN, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void forgotPassword(String emailId) {
        try {
            showProgress();
            retrofit.forgotPassword(emailId, this, getActivity(), RetroService.GET_OTP, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* public void doLogin1(String type, int imageId, String tag) {
         BaseRequest baseRequest = new BaseRequest(this);
         baseRequest.setProgressShow(true);
         baseRequest.setRequestTag(1000);
         baseRequest.setMessage("Please wait...");
         baseRequest.setServiceCallBack(this);
         Api api = (Api) baseRequest.execute(Api.class);
         api.editImage(type, String.valueOf(imageId), tag, baseRequest.requestCallback());

     }*/
    @Override
    public void onSuccess(Response<ResponseBody> response, int tag) {
        try {
            String response1 = response.body().string();
            if (tag == RetroService.GET_OTP) {
                hideProgress();
                BaseResponse baseData = JsonDataParser.getInternalParser(response1, new TypeToken<BaseResponse<UserData>>() {
                }.getType());
                Util.showToast(getActivity(), baseData.getMessage());
                if (baseData.getStatus()) {
                    ForgetPasswordView mForgetPasswordView = new ForgetPasswordView();
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable("email", _emailText.getText().toString());
                    mForgetPasswordView.setArguments(mBundle);
                    ((LoginActivity) getActivity()).addFragementViewWithOutSatck(mForgetPasswordView);
                }

            } else if (tag == RetroService.GET_LOGIN) {


                hideProgress();

                BaseResponse<UserData> baseData = JsonDataParser.getInternalParser(response1, new TypeToken<BaseResponse<UserData>>() {
                }.getType());
                if (baseData.getStatus()) {
                    Intent i = new Intent(getActivity(), HomeActivity.class);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("isRegister", true);
                    editor.putString("email", baseData.getData().getEmail());
                    editor.putString("userId", baseData.getData().getId());
                    editor.putString("userName", baseData.getData().getName());
                    editor.putInt("cardCount", baseData.getCartCount());
                    editor.commit();
                    Constants.USER_ID = baseData.getData().getId();
                    Constants.CART_COUNT = baseData.getCartCount();
                    startActivity(i);
                    getActivity().finish();
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





    @Override
    public void onClick(View v) {
        if (v == forgotPassword) {
            String email = _emailText.getText().toString();
            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                _emailText.setError("enter a valid email address");
            } else {
                forgotPassword(email);
            }

        } else if (v == skipLogin) {
            Intent i = new Intent(getActivity(), HomeActivity.class);
            startActivity(i);
            getActivity().finish();
        } else if (v == _loginButton) {
            login();
        } else if (v == _signupLink) {
            SignUpView mSignUpView = new SignUpView();
            ((LoginActivity) getActivity()).addFragementView(mSignUpView);
            //Intent intent = new Intent(getActivity(), SignActivity.class);
            //startActivityForResult(intent, REQUEST_SIGNUP);
            //getActivity().finish();
            //getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
    }

    TextWatcher editText = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 0) {
                forgotPassword.setVisibility(View.INVISIBLE);
                textInputLayoutPassword.setPasswordVisibilityToggleEnabled(true);
            } else {
                forgotPassword.setVisibility(View.VISIBLE);
                textInputLayoutPassword.setPasswordVisibilityToggleEnabled(false);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}


