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
import androidx.lifecycle.ViewModelProvider;

import com.example.androidsecondproject.R;
import com.example.androidsecondproject.model.eViewModels;
import com.example.androidsecondproject.viewmodel.AccountSetupViewModel;
import com.example.androidsecondproject.viewmodel.ProfileViewModel;
import com.example.androidsecondproject.viewmodel.RegisterViewModel;
import com.example.androidsecondproject.viewmodel.ViewModelFactory;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class AccountSetupFragment extends Fragment {

    private AccountSetupFragmentInterface mListener;
    private AccountSetupViewModel mViewModel;

    interface AccountSetupFragmentInterface
    {
        void OnClickContinueToPreferences();
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

        mViewModel=new ViewModelProvider(this,new ViewModelFactory(getActivity().getApplication(),eViewModels.Setup)).get(AccountSetupViewModel.class);
        final Button manBtn=rootView.findViewById(R.id.men_btn);
        final Button womanBtn=rootView.findViewById(R.id.women_btn);


        manBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manBtn.setSelected(true);
                womanBtn.setSelected(false);
                mViewModel.setGender("male");

            }
        });

        womanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manBtn.setSelected(false);
                womanBtn.setSelected(true);
                mViewModel.setGender("female");
            }
        });

        Button continueBtn = rootView.findViewById(R.id.continue_btn);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText firstNameEt = rootView.findViewById(R.id.first_name_et);
                EditText lastNameEt = rootView.findViewById(R.id.last_name_et);
                DatePicker calender = rootView.findViewById(R.id.date_picker);
                String firstName = firstNameEt.getText().toString();
                String lastName = lastNameEt.getText().toString();
                GregorianCalendar date=new GregorianCalendar(calender.getYear(),calender.getMonth(),calender.getDayOfMonth());

                mViewModel.setFirstName(firstName);
                mViewModel.setLastName(lastName);
                mViewModel.setDate(date);

                boolean fieldsValidated=true;
                if(firstName.trim().length()==0){
                    fieldsValidated=false;
                    //TODO
                }
                if(lastName.trim().length()==0){
                    fieldsValidated=false;
                    //TODO
                }
                if(fieldsValidated) {
                    mViewModel.writeProfileToDatabase();
                    mListener.OnClickContinueToPreferences();
                }
            }
        });
        return rootView;
    }
}
