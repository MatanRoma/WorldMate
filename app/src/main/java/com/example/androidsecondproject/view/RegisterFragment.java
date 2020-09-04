package com.example.androidsecondproject.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.androidsecondproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterFragment extends androidx.fragment.app.DialogFragment {

    public static RegisterFragment newInstance()
    {
        RegisterFragment registerFragment = new RegisterFragment();
            /*Bundle bundle = new Bundle();
            bundle.putString("user_name",username);
            loginFragment.setArguments(bundle);*/
        return registerFragment;
    }
    private FirebaseAuth mAuth;
    private RegisterFragmentInterface listener;
    interface RegisterFragmentInterface{
        void onClickMoveToLogin();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener=(RegisterFragmentInterface)getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.signup_fragment,container,false);

        Button registerButton=rootView.findViewById(R.id.register_btn_signup);
        Button signInButton=rootView.findViewById(R.id.sign_in_button);
        final EditText nicknameEt=rootView.findViewById(R.id.nickname_et_signup);
        final EditText emailEt=rootView.findViewById(R.id.email_et_signup);
        final EditText passwordEt=rootView.findViewById(R.id.password_et_signup);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=emailEt.getText().toString();
                String password=passwordEt.getText().toString();
                final String nickname=nicknameEt.getText().toString();

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    Toast.makeText(getContext(), user.getUid(), Toast.LENGTH_SHORT).show();

                                } else {
                                    // If sign in fails, display a message to the user.


                                }

                                // ...
                            }
                        });
            }
        });
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickMoveToLogin();
            }
        });

        return  rootView;
    }
}
