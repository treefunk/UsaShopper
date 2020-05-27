package com.myoptimind.usashopper.features.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.myoptimind.usashopper.features.searchorder.SearchActivity;
import com.myoptimind.usashopper.R;
import com.myoptimind.usashopper.models.User;

public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";

    LoginViewModel mLoginViewModel;
    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_login,container,false);

        mLoginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        initLogin();

        return v;
    }

    private void initLogin() {

        Button btnLogin              = v.findViewById(R.id.btn_login);
        TextInputEditText etUsername = v.findViewById(R.id.et_username);
        TextInputEditText etPassword = v.findViewById(R.id.et_password);
        TextInputLayout   tlUsername = v.findViewById(R.id.txtinput_username);
        TextInputLayout   tlPassword = v.findViewById(R.id.txtinput_password);



        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mLoginViewModel.setUsername(s.toString());
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mLoginViewModel.setPassword(s.toString());
            }
        });

        LoginListener loginListener = new LoginListener() {
            @Override
            public void onSuccessLogin(User user) {
                Log.d(TAG,"Login Success");
                startActivity(SearchActivity.createIntent(getActivity()));
                getActivity().finish();
            }
            @Override
            public void onErrorLogin(String message, String code) {
                Log.d(TAG,"error login: " + message);
                Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete() {
                btnLogin.setEnabled(true);
            }
        };

        mLoginViewModel.getUsername().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s.isEmpty()){
                    tlUsername.setError("Username field is required.");
                }else{
                    tlUsername.setErrorEnabled(false);
                }
            }
        });

        mLoginViewModel.getPassword().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s.isEmpty()){
                    tlPassword.setError("Password field is required.");
                }else{
                    tlPassword.setErrorEnabled(false);
                }
            }
        });


        btnLogin.setOnClickListener(v -> {



            if(TextUtils.isEmpty(etUsername.getText().toString())){
                tlUsername.setError("Username field is required.");
                return;
            }
            if(TextUtils.isEmpty(etPassword.getText().toString())){
                tlPassword.setError("Password field is required");
                return;
            }

            btnLogin.setEnabled(false);
            mLoginViewModel.authenticateUser(
                    etUsername.getText().toString(),
                    etPassword.getText().toString(),
                    loginListener
            );
        });
    }


}
