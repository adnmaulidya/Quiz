package com.example.quiz.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.quiz.Common.Common;
import com.example.quiz.Model.QuestionScore;
import com.example.quiz.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Done extends AppCompatActivity {

    Button btnTryAgain;
    TextView txtReResultScore,getTxtResultQuestion;
    ProgressBar progressbar;

    FirebaseDatabase database;
    DatabaseReference question_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        database = FirebaseDatabase.getInstance();
        question_score = database.getReference("Question_Score");

        txtReResultScore = (TextView)findViewById(R.id.txtTotalScore);
        getTxtResultQuestion = (TextView)findViewById(R.id.txtTotalQuestion);
        progressbar = (ProgressBar)findViewById(R.id.doneProgressBar);
        btnTryAgain = (Button)findViewById(R.id.btnTryAgain);

        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Done.this,Home.class);
                startActivity(intent);
                finish();
            }
        });

        //mendapatkan data dari bundle dan ditampilkan pada view
        Bundle extra = getIntent().getExtras();
        if(extra != null){
            int score = extra .getInt("SCORE");
            int totalQuestion = extra .getInt("TOTAL");
            int correctAnswer = extra .getInt("CORRECT");

            txtReResultScore.setText(String.format("SCORE : %d",score));
            getTxtResultQuestion.setText(String.format("PASSED : %d / %d",correctAnswer,totalQuestion));

            progressbar.setMax(totalQuestion);
            progressbar.setProgress(correctAnswer);

            //upload point ke firebase
            question_score.child(String.format("%s_%s ", Common.currentUser.getUserName(),
                                    Common.categoryId))
                    .setValue(new QuestionScore(String.format("%s_%s ", Common.currentUser.getUserName(),
                            Common.categoryId),
                            Common.currentUser.getUserName(),
                            String.valueOf(score),
                            Common.categoryId,
                            Common.categoryName));
        }
    }
}
