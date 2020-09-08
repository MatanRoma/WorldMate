package com.example.androidsecondproject.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.androidsecondproject.R;

public class AccountSetupFragment extends Fragment {

    public static AccountSetupFragment newInstance()
    {
        AccountSetupFragment accountSetupFragment=new AccountSetupFragment();
            /*Bundle bundle = new Bundle();
            bundle.putString("user_name",username);
            loginFragment.setArguments(bundle);*/
        return accountSetupFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.account_setup_fragment,container,false);

        final Button manBtn=rootView.findViewById(R.id.men_btn);
        final Button womanBtn=rootView.findViewById(R.id.women_btn);


        manBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manBtn.setSelected(true);
                womanBtn.setSelected(false);
            }
        });

        womanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manBtn.setSelected(false);
                womanBtn.setSelected(true);
            }
        });
        return rootView;
    }
}
