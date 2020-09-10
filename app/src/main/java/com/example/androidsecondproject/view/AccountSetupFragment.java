package com.example.androidsecondproject.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.androidsecondproject.R;

import java.util.ArrayList;

public class AccountSetupFragment extends Fragment {

    private AccountSetupFragmentInterface mListener;

    interface AccountSetupFragmentInterface
    {
        void OnClickContinueToPreferences(String firstName, String lastName, String gender, ArrayList<Integer> date);
    }



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
        mListener =(AccountSetupFragmentInterface) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.account_setup_fragment,container,false);

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

        Button continueBtn = rootView.findViewById(R.id.continue_btn);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText firstNameEt = rootView.findViewById(R.id.first_name_et);
                EditText lastNameEt = rootView.findViewById(R.id.last_name_et);
                String firstName = firstNameEt.getText().toString();
                String lastName = lastNameEt.getText().toString();
                String gender = " ";
                if(manBtn.isSelected())
                {
                    gender = "male";
                }
                else if(womanBtn.isSelected())
                {
                    gender = "female";
                }
                DatePicker calender = rootView.findViewById(R.id.date_picker);
                ArrayList<Integer> date = new ArrayList<Integer>();
                date.add(calender.getDayOfMonth());
                date.add(calender.getMonth());
                date.add(calender.getYear());
                mListener.OnClickContinueToPreferences(firstName,lastName,gender,date);
            }
        });
        return rootView;
    }
}
