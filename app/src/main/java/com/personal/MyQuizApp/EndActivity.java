package com.personal.MyQuizApp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EndActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        int finalScore = getIntent().getIntExtra("FinalScore", 0);
        int correctAnswers = getIntent().getIntExtra("CorrectAnswers", 0);

        TextView scoreView = findViewById(R.id.finalScore);
        scoreView.setText("Final Score: " + finalScore);

        TextView correctAnswersView = findViewById(R.id.percentage);
        correctAnswersView.setText(String.format("%.1f%% Correct", ((float) correctAnswers / 20) * 100));

    }
}