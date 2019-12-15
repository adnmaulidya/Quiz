package com.example.quiz.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quiz.Interface.ItemClicklistener;
import com.example.quiz.R;

public class RankingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txt_name,txt_score;

    private ItemClicklistener itemClicklistener;

    public RankingViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_name =(TextView)itemView.findViewById(R.id.txt_name);
        txt_score =(TextView)itemView.findViewById(R.id.txt_score);

        itemView.setOnClickListener(this);
    }

    public void setItemClicklistener(ItemClicklistener itemClicklistener) {
        this.itemClicklistener = itemClicklistener;
    }

    @Override
    public void onClick(View view) {
        itemClicklistener.onClick(view,getAdapterPosition(),false);

    }
}
