package com.example.androidsecondproject.view;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.androidsecondproject.R;
import com.example.androidsecondproject.model.eViewModels;
import com.example.androidsecondproject.viewmodel.RegisterViewModel;
import com.example.androidsecondproject.viewmodel.ViewModelFactory;

public class RegisterFragment extends Fragment {

    private RegisterFragmentInterface listener;
    private RegisterViewModel mViewModel;
    private EditText mNicknameEt,mEmailEt,mPasswordEt;

    interface RegisterFragmentInterface{
        void onClickMoveToLogin();
        void onMoveToNameSetup(String uid);
    }

    public static RegisterFragment newInstance()
    {
        RegisterFragment registerFragment = new RegisterFragment();
            /*Bundle bundle = new Bundle();
            bundle.putString("user_name",username);
            loginFragment.setArguments(bundle);*/
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
                listener.onMoveToNameSetup(uid);
              //  setRegisterFields();
            }
        };
        final Observer<String> registerObserverFailed = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String error) {
               Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();

              //  setRegisterFields();
            }
        };

        mViewModel.getRegisterResultSuccess().observe(this, registerObserverSuccess);
        mViewModel.getRegisterResultFailed().observe(this, registerObserverFailed);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.signup_fragment,container,false);

        Button registerButton=rootView.findViewById(R.id.register_btn_signup);
        Button signInButton=rootView.findViewById(R.id.sign_in_button);
        mNicknameEt=rootView.findViewById(R.id.nickname_et_signup);
        mEmailEt=rootView.findViewById(R.id.email_et_signup);
        mPasswordEt=rootView.findViewById(R.id.password_et_signup);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    public void setRegisterFields(){
        String email=mEmailEt.getText().toString();
        String password=mPasswordEt.getText().toString();
        String nickname=mNicknameEt.getText().toString();

        mViewModel.setEmail(email);
        mViewModel.setPassword(password);
        mViewModel.setNickname(nickname);
    }
}
