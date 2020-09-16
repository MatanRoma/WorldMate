package com.example.androidsecondproject.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.androidsecondproject.R;
import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.model.Question;

import java.io.Serializable;
import java.util.List;

public class QuestionsFragment extends Fragment {

    public static QuestionsFragment newInstance(List<Question> questions)
    {
        Bundle bundle=new Bundle();
        bundle.putSerializable("questions", (Serializable) questions);
        QuestionsFragment questionsFragment=new QuestionsFragment();
        questionsFragment.setArguments(bundle);
        return questionsFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.questions_fragment,container,false);




        return rootView;
    }
}
