package com.augmentedrealityapp.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.augmentedrealityapp.R;
import com.augmentedrealityapp.dao.BaseResponse;
import com.augmentedrealityapp.dao.UserData;
import com.augmentedrealityapp.fragement.LoginView;
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

public class LoginActivity extends BaseActivity {
    @BindView(R.id.container)
    FrameLayout frameLayout;

    @Override
    protected int myView() {
        return R.layout.content_main2;
    }

    public void addFragementViewWithOutSatck(Fragment fragment) {
        addFragmentWithOutAddStack(R.id.container, fragment, fragment.getClass().getName());
    }
    public void addFragementView(Fragment fragment) {
        addFragment(R.id.container, fragment, fragment.getClass().getName());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        LoginView mLoginView = new LoginView();
        addFragementViewWithOutSatck(mLoginView);
        /*initViews();
        retrofit = RetrofitManager.getInstance();
        skipLogin.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });*/
    }
    public void clearBackStackInclusive(String tag) {
        getSupportFragmentManager().popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
   /* private void initViews() {
        pref = Util.getSharedPreferences(this);
        _passwordText.addTextChangedListener(editText);
        textInputLayoutPassword.setPasswordVisibilityToggleEnabled(false);
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

        *//*final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
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
                }, 3000);*//*
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

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
            retrofit.doLogin(hashmap, this, LoginActivity.this, RetroService.GET_LOGIN, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void forgotPassword(String emailId) {
        try {
            showProgress();
            retrofit.forgotPassword(emailId, this, this, RetroService.GET_PRO_LIST, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    *//* public void doLogin1(String type, int imageId, String tag) {
         BaseRequest baseRequest = new BaseRequest(this);
         baseRequest.setProgressShow(true);
         baseRequest.setRequestTag(1000);
         baseRequest.setMessage("Please wait...");
         baseRequest.setServiceCallBack(this);
         Api api = (Api) baseRequest.execute(Api.class);
         api.editImage(type, String.valueOf(imageId), tag, baseRequest.requestCallback());

     }*//*
    @Override
    public void onSuccess(Response<ResponseBody> response, int tag) {

        try {
            hideProgress();
            String response1 = response.body().string();
            BaseResponse<UserData> baseData = JsonDataParser.getInternalParser(response1, new TypeToken<BaseResponse<UserData>>() {
            }.getType());
            if (baseData.getStatus()) {
                Intent i = new Intent(this, HomeActivity.class);
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
                finish();

            } else Util.showToast(this, baseData.getMessage());
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
        Util.showToast(this, error.getResult());
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
            Intent i = new Intent(this, HomeActivity.class);
            startActivity(i);
            finish();
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
    };*/
}
