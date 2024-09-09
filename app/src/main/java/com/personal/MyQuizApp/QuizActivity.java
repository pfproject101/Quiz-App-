package com.personal.MyQuizApp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class QuizActivity extends AppCompatActivity {

    private TextView questionTextView;
    private TextView scoreView;
    private TextView questionNumberView;
    private Question [] questions;
    private RadioGroup choicesGroup;
    private String [] currChoices;
    private int currIdx = 0;
    private int selectedChoiceIdx = -1;
    private int score = 0;
    private int num=1;
    private int correctAnswers = 0;
    private long timeLeft = 300000;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

//        get views from layout
        scoreView = findViewById(R.id.score);
        choicesGroup = findViewById(R.id.choices);
        Button nextbtn = findViewById(R.id.next);
        Button prevbtn = findViewById(R.id.prev);
        Button revealbtn = findViewById(R.id.reveal);
        Button endbtn = findViewById(R.id.end);

//        get values defined in strings
        String scoreFormatted = getString(R.string.score, score);
        questions = new Question[20];
        for (int i = 0; i < 20; i++) {
            questions[i] = new Question(this, i);
        }

//        set values of views
        scoreView.setText(scoreFormatted);
        displayQuestion();

        prevbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currIdx!=0) {
                    currIdx--;
                    num--;
                }
                else {
                    Toast.makeText(QuizActivity.this, "No Previous Question", Toast.LENGTH_SHORT).show();
                }
                displayQuestion();
            }
        });
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!questions[currIdx].revealed) {
                    selectedChoiceIdx = choicesGroup.getCheckedRadioButtonId();
                    if (selectedChoiceIdx != -1) {
                        questions[currIdx].answered = true;
                        checkAnswer();
                    }
                }
                if (currIdx == 19){
                    nextbtn.setEnabled(false);
                }
                else{
                    currIdx++;
                    num++;
                }
                displayQuestion();
            }
        });
        revealbtn.setOnClickListener(v -> {
            if (!questions[currIdx].revealed && !questions[currIdx].answered) {
                int [] allOptionsIds = new int[] {
                        R.id.choice1, R.id.choice2, R.id.choice3, R.id.choice4
                };
                RadioButton answerbtn = (RadioButton)findViewById(allOptionsIds[questions[currIdx].getAnswerIdx()]);
                answerbtn.setChecked(true);
                questions[currIdx].revealed=true;
                questions[currIdx].selectedId = choicesGroup.getCheckedRadioButtonId();

//              don't allow user to select any option anymore
                for (int i=0; i<4; i++){
                    RadioButton choicebtn = (RadioButton) choicesGroup.getChildAt(i);
                    choicebtn.setEnabled(false);
                }

                score--;
                scoreView.setText(getString(R.string.score, score));
            }
        });
        endbtn.setOnClickListener(v->{
            Intent intent = new Intent(QuizActivity.this, EndActivity.class);
            intent.putExtra("FinalScore", score);
            intent.putExtra("CorrectAnswers", correctAnswers);
            startActivity(intent);
            finish();
        });

        setUpTimer();
    }

    private void displayQuestion(){
        questionNumberView = findViewById(R.id.questionNumber);
        String questionNum = getString(R.string.question_num, num);
        questionNumberView.setText(questionNum);

        questionTextView = findViewById(R.id.questionStatement);
        questionTextView.setText(questions[currIdx].getQuestionText());

        RadioGroup choicesGroup = findViewById(R.id.choices);
        choicesGroup.clearCheck();
        selectedChoiceIdx = -1;
        currChoices = questions[currIdx].getChoices();
        for (int i=0; i<4; i++){
            RadioButton choicebtn = (RadioButton) choicesGroup.getChildAt(i);
            choicebtn.setText(currChoices[i]);
            choicebtn.setEnabled(true);

            if(questions[currIdx].answered || questions[currIdx].revealed){
               choicebtn.setEnabled(false);
               choicesGroup.check(questions[currIdx].selectedId);
            }
        }
    }

    public void checkAnswer(){
        int selectedId = choicesGroup.getCheckedRadioButtonId();
        questions[currIdx].selectedId = selectedId;
        selectedChoiceIdx = choicesGroup.indexOfChild(findViewById(selectedId));
        if(selectedChoiceIdx == questions[currIdx].getAnswerIdx()){
            score+=5;
            correctAnswers++;
        }
        else{
            score-=1;
        }
        scoreView.setText(getString(R.string.score, score));
    }

    private void setUpTimer () {
        countDownTimer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                Intent intent = new Intent(QuizActivity.this, EndActivity.class);
                intent.putExtra("FinalScore", score);
                intent.putExtra("CorrectAnswers", correctAnswers);
                startActivity(intent);
                finish();
            }
        }.start();

    }
    private void updateTimer() {
        int minutes = (int) (timeLeft / 1000) / 60;
        int seconds = (int) (timeLeft / 1000) % 60;

        String timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
        TextView timerTextView = findViewById(R.id.time);
        timerTextView.setText("Time Left: " + timeLeftFormatted);
    }


}