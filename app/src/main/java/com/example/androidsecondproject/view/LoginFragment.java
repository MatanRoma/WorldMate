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

public class LoginFragment extends androidx.fragment.app.DialogFragment {

        public static LoginFragment newInstance()
        {
            LoginFragment loginFragment = new LoginFragment();
            /*Bundle bundle = new Bundle();
            bundle.putString("user_name",username);
            loginFragment.setArguments(bundle);*/
            return loginFragment;
        }
        private FirebaseAuth mAuth;
        private LoginFragmentInterface listener;
        interface LoginFragmentInterface{
            void onClickMoveToRegister();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener=(LoginFragmentInterface)getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.signin_fragment,container,false);

        Button loginButton=rootView.findViewById(R.id.login_btn);
        Button signUpButton=rootView.findViewById(R.id.sign_up_button);
        final EditText emailEt=rootView.findViewById(R.id.email_et_signin);
        final EditText passwordEt=rootView.findViewById(R.id.password_et_singin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEt.getText().toString();
                String password = passwordEt.getText().toString();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(getContext(), ""+user.getUid(), Toast.LENGTH_SHORT).show();
                                    ;

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(getContext(), "fail", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

            }
        });


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickMoveToRegister();
            }
        });


        setCancelable(false);
        return rootView;
    }


}
