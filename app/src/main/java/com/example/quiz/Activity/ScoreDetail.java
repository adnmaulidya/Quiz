package com.example.quiz.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.quiz.Model.QuestionScore;
import com.example.quiz.R;
import com.example.quiz.ViewHolder.ScoreDetailViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ScoreDetail extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference question_score;

    RecyclerView scoreList;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<QuestionScore, ScoreDetailViewHolder> adapter;

    String viewUser=" ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_detail);

        //firebase
        database = FirebaseDatabase.getInstance();
        question_score = database.getReference("Question_Score");

        //view
        scoreList =(RecyclerView)findViewById(R.id.scoreList);
        scoreList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        scoreList.setLayoutManager(layoutManager);

        if(getIntent() != null)
            viewUser = getIntent().getStringExtra("viewUser");
        if(!viewUser.isEmpty())
            loadDScoreDetail(viewUser);

    }

    private void loadDScoreDetail(String viewUser) {
        adapter = new FirebaseRecyclerAdapter<QuestionScore, ScoreDetailViewHolder>(
                QuestionScore.class,
                R.layout.score_detail_layout,
                ScoreDetailViewHolder.class,
                question_score.orderByChild("user").equalTo(viewUser)
        ) {
            @Override
            protected void populateViewHolder(ScoreDetailViewHolder viewHolder, QuestionScore model, int position) {
                viewHolder.txt_name.setText(model.getCategoryName());
                viewHolder.txt_score.setText(model.getScore());
            }
        };
        adapter.notifyDataSetChanged();
        scoreList.setAdapter(adapter);
    }
}
