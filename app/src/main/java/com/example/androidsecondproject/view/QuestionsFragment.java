package com.example.androidsecondproject.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidsecondproject.R;
import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.model.Question;
import com.example.androidsecondproject.model.QuestionRespond;
import com.example.androidsecondproject.model.eViewModels;
import com.example.androidsecondproject.viewmodel.QuestionsViewModel;
import com.example.androidsecondproject.viewmodel.ViewModelFactory;

import java.io.Serializable;
import java.util.List;

public class QuestionsFragment extends Fragment {
private QuestionsViewModel mViewModel;
private View mSportView;
    public static QuestionsFragment newInstance(Profile profile)
    {
        Bundle bundle=new Bundle();
        bundle.putSerializable("profile",profile);
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
        final RelativeLayout relativeLayoutQuestions =rootView.findViewById(R.id.relative_questions);


        mViewModel =new ViewModelProvider(this,new ViewModelFactory(getActivity().getApplication(), eViewModels.Questions)).get(QuestionsViewModel.class);
        mViewModel.setProfile((Profile)getArguments().getSerializable("profile"));

        Observer<List<Question>> questionsObserverSuccess=new Observer<List<Question>>() {
            @Override
            public void onChanged(List<Question> questions) {
                relativeLayoutQuestions.setVisibility(View.VISIBLE);
            }
        };
        mViewModel.readQuestions();

        mViewModel.getQuestionsResultSuccess().observe(this,questionsObserverSuccess);

        final CheckBox sportCb = rootView.findViewById(R.id.sport_rb);
        final CheckBox foodCb = rootView.findViewById(R.id.food_rb);

        final LinearLayout sportQuestionsLayout = rootView.findViewById(R.id.sport_questions_layout);
        final TextView sportTitleTv = rootView.findViewById(R.id.sport_title_tv);

        final LinearLayout foodQuestionsLayout = rootView.findViewById(R.id.food_questions_layout);
        final TextView foodTitleTv = rootView.findViewById(R.id.food_title_tv);

        sportCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sportCb.isChecked())
                {
                    sportTitleTv.setVisibility(View.VISIBLE);
                    sportQuestionsLayout.removeAllViews();
                    sportQuestionsLayout.addView(sportTitleTv);
                    for (final Question question:mViewModel.getQuestions()) {
                        if(question.getCategory().equals("sport"))
                        {
                            final View child = getLayoutInflater().inflate(R.layout.question_item_layout, sportQuestionsLayout, false);
                            TextView questionTitle = child.findViewById(R.id.question_title_tv);
                            RadioButton option1Rb = child.findViewById(R.id.option1_rb);
                            RadioButton option2Rb = child.findViewById(R.id.option2_rb);

                            questionTitle.setText(question.getSentence());
                            option1Rb.setText(question.getAnswers().getOption1());
                            option2Rb.setText(question.getAnswers().getOption2());
                            ImageButton closeBtn =child.findViewById(R.id.close_btn_iv);
                            for (QuestionRespond response: mViewModel.getProfile().getQuestionResponds()) {
                                if(response.getId() == question.getId())
                                {
                                    if(response.getResponse()==0)
                                    {
                                        option1Rb.setChecked(true);
                                        option2Rb.setChecked(false);
                                    }
                                    else
                                    {
                                        option1Rb.setChecked(false);
                                        option2Rb.setChecked(true);
                                    }
                                }
                            }
                            final RadioGroup answersRadioGroup = child.findViewById(R.id.answers_radio_group);
                            answersRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(RadioGroup group, int checkedId) {
                                    RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                                    boolean isChecked = checkedRadioButton.isChecked();
                                    if(isChecked)
                                    {
                                        int index = answersRadioGroup.indexOfChild(checkedRadioButton);
                                        Toast.makeText(getContext(), question.getCategory()+"", Toast.LENGTH_SHORT).show();
                                        QuestionRespond questionRespond = new QuestionRespond(question.getId(),index,question.getCategory());

                                        List<QuestionRespond> questionResponds = mViewModel.getProfile().getQuestionResponds();
                                        boolean exist = false;
                                        int existIndex = -1;
                                        for (int i = 0; i < questionResponds.size(); i++) {
                                            QuestionRespond respond = questionResponds.get(i);
                                            if(respond.getId()==question.getId())
                                            {
                                                exist = true;
                                                existIndex = questionResponds.indexOf(question);

                                                boolean tst =questionResponds.remove(respond);
                                                Toast.makeText(getContext(), tst+"", Toast.LENGTH_SHORT).show();
                                                //questionResponds.add(existIndex,questionRespond);
                                            }

                                        }
                                        mViewModel.getProfile().getQuestionResponds().add(questionRespond);
                                        mViewModel.writeProfile();
                                        Toast.makeText(getContext(), "size is "+questionResponds.size(), Toast.LENGTH_SHORT).show();


                                    }
                                }
                            });
                            //int index = answersRadioGroup.indexOfChild(child.findViewById(answersRadioGroup.getCheckedRadioButtonId()));



                            closeBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    sportQuestionsLayout.removeView(child);
                                    sportCb.setChecked(false);
                                    if(sportQuestionsLayout.getChildCount() == 1)
                                    {
                                        sportTitleTv.setVisibility(View.GONE);
                                    }
                                }
                            });

                            sportQuestionsLayout.addView(child);
                        }

                    }
                }
                else
                {
                    sportTitleTv.setVisibility(View.GONE);
                    sportQuestionsLayout.removeAllViews();
                }

            }

        });

        foodCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(foodCb.isChecked())
                {
                    foodTitleTv.setVisibility(View.VISIBLE);
                    foodQuestionsLayout.removeAllViews();
                    foodQuestionsLayout.addView(foodTitleTv);
                    for (final Question question:mViewModel.getQuestions()) {
                        if(question.getCategory().equals("food"))
                        {
                            final View child = getLayoutInflater().inflate(R.layout.question_item_layout, foodQuestionsLayout, false);
                            TextView questionTitle = child.findViewById(R.id.question_title_tv);
                            RadioButton option1Rb = child.findViewById(R.id.option1_rb);
                            RadioButton option2Rb = child.findViewById(R.id.option2_rb);

                            questionTitle.setText(question.getSentence());
                            option1Rb.setText(question.getAnswers().getOption1());
                            option2Rb.setText(question.getAnswers().getOption2());
                            ImageButton closeBtn =child.findViewById(R.id.close_btn_iv);
                            for (QuestionRespond response: mViewModel.getProfile().getQuestionResponds()) {
                                if(response.getId() == question.getId())
                                {
                                    if(response.getResponse()==0)
                                    {
                                        option1Rb.setChecked(true);
                                        option2Rb.setChecked(false);
                                    }
                                    else
                                    {
                                        option1Rb.setChecked(false);
                                        option2Rb.setChecked(true);
                                    }
                                }
                            }
                            final RadioGroup answersRadioGroup = child.findViewById(R.id.answers_radio_group);
                            answersRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(RadioGroup group, int checkedId) {
                                    RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                                    boolean isChecked = checkedRadioButton.isChecked();
                                    if(isChecked)
                                    {
                                        int index = answersRadioGroup.indexOfChild(checkedRadioButton);
                                        Toast.makeText(getContext(), question.getId()+"", Toast.LENGTH_SHORT).show();
                                        QuestionRespond questionRespond = new QuestionRespond(question.getId(),index,question.getCategory());
                                        List<QuestionRespond> questionResponds = mViewModel.getProfile().getQuestionResponds();
                                        boolean exist = false;
                                        int existIndex = -1;
                                        for (int i = 0; i < questionResponds.size(); i++) {
                                            QuestionRespond respond = questionResponds.get(i);
                                            if(respond.getId()==question.getId())
                                            {
                                                exist = true;
                                                existIndex = questionResponds.indexOf(question);

                                                boolean tst =questionResponds.remove(respond);
                                                Toast.makeText(getContext(), tst+"", Toast.LENGTH_SHORT).show();
                                                //questionResponds.add(existIndex,questionRespond);
                                            }

                                        }
                                        mViewModel.getProfile().getQuestionResponds().add(questionRespond);
                                        mViewModel.writeProfile();
                                        Toast.makeText(getContext(), "size is "+questionResponds.size(), Toast.LENGTH_SHORT).show();


                                    }
                                }
                            });
                            //int index = answersRadioGroup.indexOfChild(child.findViewById(answersRadioGroup.getCheckedRadioButtonId()));



                            closeBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    foodQuestionsLayout.removeView(child);
                                    foodCb.setChecked(false);
                                    if(foodQuestionsLayout.getChildCount() == 1)
                                    {
                                        foodTitleTv.setVisibility(View.GONE);
                                    }
                                }
                            });

                            foodQuestionsLayout.addView(child);
                        }

                    }
                }
                else
                {
                    foodTitleTv.setVisibility(View.GONE);
                    foodQuestionsLayout.removeAllViews();
                }

            }

        });




        return rootView;
    }
}
