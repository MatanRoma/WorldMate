package com.example.androidsecondproject.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidsecondproject.R;
import com.example.androidsecondproject.model.Chat;
import com.example.androidsecondproject.model.Profile;
import com.example.androidsecondproject.model.Question;
import com.example.androidsecondproject.model.QuestionRespond;
import com.example.androidsecondproject.model.TranslateString;
import com.example.androidsecondproject.model.eViewModels;
import com.example.androidsecondproject.viewmodel.QuestionsViewModel;
import com.example.androidsecondproject.viewmodel.ViewModelFactory;

import java.util.ArrayList;
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
        final CheckBox cultureCb = rootView.findViewById(R.id.culture_rb);
        final CheckBox musicCb = rootView.findViewById(R.id.music_rb);
        final CheckBox religionCb = rootView.findViewById(R.id.religion_rb);
        final CheckBox travelCb = rootView.findViewById(R.id.travel_rb);

        final LinearLayout sportQuestionsLayout = rootView.findViewById(R.id.sport_questions_layout);
        final TextView sportTitleTv = rootView.findViewById(R.id.sport_title_tv);

        final LinearLayout foodQuestionsLayout = rootView.findViewById(R.id.food_questions_layout);
        final TextView foodTitleTv = rootView.findViewById(R.id.food_title_tv);

        final LinearLayout cultureQuestionsLayout = rootView.findViewById(R.id.culture_questions_layout);
        final TextView cultureTitleTv = rootView.findViewById(R.id.culture_title_tv);

        final LinearLayout musicQuestionsLayout = rootView.findViewById(R.id.music_questions_layout);
        final TextView musicTitleTv = rootView.findViewById(R.id.music_title_tv);

        final LinearLayout religionQuestionsLayout = rootView.findViewById(R.id.religion_questions_layout);
        final TextView religionTitleTv = rootView.findViewById(R.id.religion_title_tv);

        final LinearLayout travelQuestionsLayout = rootView.findViewById(R.id.travel_questions_layout);
        final TextView travelTitleTv = rootView.findViewById(R.id.travel_title_tv);

        sportCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleQuestion(sportCb,sportQuestionsLayout,sportTitleTv,getString(R.string.sport));
            }

        });

        foodCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleQuestion(foodCb,foodQuestionsLayout,foodTitleTv,getString(R.string.food));
            }

        });

        cultureCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleQuestion(cultureCb,cultureQuestionsLayout,cultureTitleTv,getString(R.string.culture));
            }

        });

        musicCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleQuestion(musicCb,musicQuestionsLayout,musicTitleTv,getString(R.string.music));
            }

        });

        religionCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleQuestion(religionCb,religionQuestionsLayout,religionTitleTv,getString(R.string.religion));
            }
        });

        travelCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleQuestion(travelCb,travelQuestionsLayout,travelTitleTv,getString(R.string.travel));
            }
        });




        return rootView;
    }

    private void handleQuestion(final CheckBox checkBox, final LinearLayout questionsLayout, final TextView titleTv, String titleStr)
    {
        if(checkBox.isChecked())
        {
            questionsLayout.setVisibility(View.VISIBLE);
            titleTv.setVisibility(View.VISIBLE);
            questionsLayout.removeAllViews();
            questionsLayout.addView(titleTv);
            titleStr = TranslateString.HebToEnglish(titleStr);
            for (final Question question:mViewModel.getQuestions()) {
                if(question.getCategory().equals(titleStr))
                {
                    final View child = getLayoutInflater().inflate(R.layout.question_item_layout, questionsLayout, false);
                    TextView questionTitle = child.findViewById(R.id.question_title_tv);
                    questionTitle.setText(question.getSentence());
                    ImageButton closeBtn =child.findViewById(R.id.close_btn_iv);
                    closeBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            questionsLayout.removeView(child);
                            if(questionsLayout.getChildCount() == 1)
                            {
                                checkBox.setChecked(false);
                                titleTv.setVisibility(View.GONE);
                                questionsLayout.setVisibility(View.GONE);
                            }
                        }
                    });
                    RadioGroup radioGroup = child.findViewById(R.id.answers_radio_group);
                    List<RadioButton> radioButtons = new ArrayList<>();
                    for (int i= 0; i <question.getAnswers().size();i++) {
                        RadioButton radioButton = new RadioButton(getContext());
                        radioButton.setHighlightColor(getResources().getColor(R.color.black));
                        radioButton.setTag(question.getId()+"");
                        radioButtons.add(radioButton);
                        radioButton.setText(question.getAnswers().get(i));
                        final int tmpI =i;
                        radioButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                boolean isChecked = ((RadioButton)v).isChecked();
                                if(isChecked)
                                {

                                    QuestionRespond questionRespond = new QuestionRespond(question.getId(),tmpI,question.getCategory(),question.getAnswers().size());
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

                                            //questionResponds.add(existIndex,questionRespond);
                                        }

                                    }
                                    mViewModel.getProfile().getQuestionResponds().add(questionRespond);
                                    mViewModel.updateQuestion();



                                }
                            }
                        });
                        radioGroup.addView(radioButton);
                    }

                    List<QuestionRespond> responds = mViewModel.getProfile().getQuestionResponds();
                    for (int i = 0; i < responds.size(); i++)
                    {
                        if(responds.get(i).getId() == question.getId())
                        {
                            for (RadioButton radioButton: radioButtons) {


                                if(radioButtons.indexOf(radioButton)==responds.get(i).getResponse())
                                {

                                    radioButton.setChecked(true);
                                }
                            }
                        }
                    }
                    questionsLayout.addView(child);
                }

            }
        }
        else
        {
            questionsLayout.setVisibility(View.GONE);
            titleTv.setVisibility(View.GONE);
            questionsLayout.removeAllViews();
        }
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.removeItem(R.id.filter_id);
    }
}
