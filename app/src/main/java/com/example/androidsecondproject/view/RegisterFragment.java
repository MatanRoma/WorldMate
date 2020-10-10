package com.example.androidsecondproject.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidsecondproject.R;
import com.example.androidsecondproject.model.eViewModels;
import com.example.androidsecondproject.viewmodel.RegisterViewModel;
import com.example.androidsecondproject.viewmodel.ViewModelFactory;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterFragment extends Fragment {

    private RegisterFragmentInterface listener;
    private RegisterViewModel mViewModel;
    private EditText mEmailEt,mPasswordEt;
    private TextView mErrorTv;
    private View rootView;

    interface RegisterFragmentInterface{
        void onClickMoveToLogin();
        void onMoveToAccountSetup();
    }

    public static RegisterFragment newInstance()
    {
        RegisterFragment registerFragment = new RegisterFragment();
        return registerFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener=(RegisterFragmentInterface)getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel=new ViewModelProvider(this,new ViewModelFactory(getContext(), eViewModels.Register)).get(RegisterViewModel.class);
        final Observer<String> registerObserverSuccess = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String uid) {
        //        mErrorTv.setVisibility(View.INVISIBLE);
                listener.onMoveToAccountSetup();
                //  setRegisterFields();
            }
        };
        final Observer<String> registerObserverFailed = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String error) {
                mErrorTv.setText(error);
                mErrorTv.setVisibility(View.VISIBLE);
            }
        };

        mViewModel.getRegisterResultSuccess().observe(this, registerObserverSuccess);
        mViewModel.getRegisterResultFailed().observe(this, registerObserverFailed);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.signup_fragment,container,false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Button registerButton=rootView.findViewById(R.id.register_btn_signup);
        Button signInButton=rootView.findViewById(R.id.sign_in_button);
        mEmailEt=rootView.findViewById(R.id.email_et_signup);
        mPasswordEt=rootView.findViewById(R.id.password_et_signup);
        mErrorTv=rootView.findViewById(R.id.error_et_signup);



        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  validateFields();
                setRegisterFields();
                mViewModel.registerUser();
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickMoveToLogin();
            }
        });

        //setCancelable(false);
        return rootView;
    }

    void validateFields(){

        TextInputLayout emailInputLayout = rootView.findViewById(R.id.email_signup);
        if(mEmailEt.getText().toString().trim().length() == 0)
        {
            emailInputLayout.setDefaultHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red_stroke)));
        }
        else {
            emailInputLayout.setDefaultHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
        }
        TextInputLayout passwordInputLayout = rootView.findViewById(R.id.password_signup);
        if(mPasswordEt.getText().toString().trim().length() == 0)
        {
            passwordInputLayout.setDefaultHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.red_stroke)));
            mPasswordEt.setError("pw is empty");
        }
        else {
            passwordInputLayout.setDefaultHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.black)));
        }
    }

    public void setRegisterFields(){
        String email=mEmailEt.getText().toString();
        String password=mPasswordEt.getText().toString();

        mViewModel.setEmail(email);
        mViewModel.setPassword(password);
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }
    @Override
    public void onPause() {
        super.onPause();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
    }
}
