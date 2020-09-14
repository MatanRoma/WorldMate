package com.example.androidsecondproject.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidsecondproject.R;
import com.example.androidsecondproject.model.eViewModels;
import com.example.androidsecondproject.viewmodel.LoginViewModel;
import com.example.androidsecondproject.viewmodel.ViewModelFactory;

public class LoginFragment extends Fragment {

    private LoginViewModel mViewModel;
    private LoginFragmentInterface mListener;
    private EditText mEmailEt, mPasswordEt;
    private TextView mErrorTv;

    interface LoginFragmentInterface{
        void onClickMoveToRegister();
        void onLoginToApp();
    }

    public static LoginFragment newInstance()
    {
        LoginFragment loginFragment = new LoginFragment();
        return loginFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener =(LoginFragmentInterface)getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel=new ViewModelProvider(this,new ViewModelFactory(getContext(), eViewModels.Login)).get(LoginViewModel.class);
        final Observer<String> loginObserverSuccess = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String uid) {
                mListener.onLoginToApp();
            }
        };
        final Observer<String> loginObserverFailed = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String error) {
               mErrorTv.setVisibility(View.VISIBLE);
            }
        };

        mViewModel.getLoginDataSuccess().observe(this, loginObserverSuccess);
        mViewModel.getLoginDataFailed().observe(this, loginObserverFailed);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.signin_fragment,container,false);

        Button loginButton=rootView.findViewById(R.id.login_btn);
        Button signUpButton=rootView.findViewById(R.id.sign_up_button);
        mEmailEt =rootView.findViewById(R.id.email_et_signin);
        mPasswordEt =rootView.findViewById(R.id.password_et_singin);
        mErrorTv=rootView.findViewById(R.id.error_et_signin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLoginFields();
                mViewModel.loginUser();


            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickMoveToRegister();
                // dismiss();
            }
        });

        //setCancelable(false);
        return rootView;
    }
    private void setLoginFields(){
        mViewModel.setEmail(mEmailEt.getText().toString());
        mViewModel.setPassword(mPasswordEt.getText().toString());
    }

}