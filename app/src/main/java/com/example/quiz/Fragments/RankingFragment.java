package com.example.quiz.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quiz.Common.Common;
import com.example.quiz.Interface.ItemClicklistener;
import com.example.quiz.Interface.RankingCallBack;
import com.example.quiz.Model.QuestionScore;
import com.example.quiz.Model.Ranking;
import com.example.quiz.R;
import com.example.quiz.Activity.ScoreDetail;
import com.example.quiz.ViewHolder.RankingViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RankingFragment extends Fragment {
    View myFragment;

    RecyclerView rankingList;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<Ranking, RankingViewHolder> adapter;


    FirebaseDatabase database;
    DatabaseReference questionScore, rankingTbl;

    int sum=0;

    public static RankingFragment newInstance(){
        RankingFragment rankingFragment = new RankingFragment();
        return rankingFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        questionScore = database.getReference("Question_Score");
        rankingTbl = database.getReference("Ranking");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_ranking,container,false);

        //init view
        rankingList =(RecyclerView)myFragment.findViewById(R.id.rankingList);
        layoutManager = new LinearLayoutManager(getActivity());
        rankingList.setHasFixedSize(true);
        //karena orderbychild akan sort list secara ascending
        //maka dari itu butuh untuk membalikkan recycler data menggunakan layoutmanager
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rankingList.setLayoutManager(layoutManager);

        //implemen callback
        updateScore(Common.currentUser.getUserName(), new RankingCallBack<Ranking>() {
            @Override
            public void callBack(Ranking ranking) {
                //update ke ranking tabel
                rankingTbl.child(ranking.getUserName())
                        .setValue(ranking);
                //showRanking(); // setelah diupload kita akan menyaring dan menampilkan ranking dengan fungsi shwoRanking
            }
        });
        //set adapter
        adapter = new FirebaseRecyclerAdapter<Ranking, RankingViewHolder>(
                Ranking.class,
                R.layout.layout_ranking,
                RankingViewHolder.class,
                rankingTbl.orderByChild("score")
        ) {
            @Override
            protected void populateViewHolder(RankingViewHolder viewHolder, final Ranking model, int position) {

                viewHolder.txt_name.setText(model.getUserName());
                viewHolder.txt_score.setText(String.valueOf(model.getScore()));

                //memperbaiki crash ketika menekan item
                viewHolder.setItemClicklistener(new ItemClicklistener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent scoreDetail = new Intent(getActivity(), ScoreDetail.class);
                        scoreDetail.putExtra("viewUser",model.getUserName());
                        startActivity(scoreDetail);



                    }
                });
            }
        };

        adapter.notifyDataSetChanged();
        rankingList.setAdapter(adapter);

        return myFragment;
    }

    private void updateScore(final String userName, final RankingCallBack<Ranking> callBack){
        questionScore.orderByChild("user").equalTo(userName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot data:dataSnapshot.getChildren()){
                            QuestionScore ques = data.getValue(QuestionScore.class);
                            sum += Integer.parseInt(ques.getScore());
                        }
                        //setelah dirangkum semua, membutuhkan sum variabel disini
                        //karena firebase selalu asnync db, jadi jika diproses diluar,
                        //nilai sum akan ke reset
                        Ranking ranking = new Ranking(userName,sum);
                        callBack.callBack(ranking);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
