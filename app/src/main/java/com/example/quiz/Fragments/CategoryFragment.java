package com.example.quiz.Fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quiz.Common.Common;
import com.example.quiz.Interface.ItemClicklistener;
import com.example.quiz.Model.Category;
import com.example.quiz.R;
import com.example.quiz.Activity.Start;
import com.example.quiz.ViewHolder.CategoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class CategoryFragment extends Fragment {

     View myFragment;

     RecyclerView listCategory;
     RecyclerView.LayoutManager layoutManager;
     FirebaseRecyclerAdapter<Category, CategoryViewHolder> adapter;

     FirebaseDatabase database;
     DatabaseReference categories;

     public static CategoryFragment newInstance(){
          CategoryFragment categoryFragment = new CategoryFragment();
          return categoryFragment;
     }

     @Override
     public void onCreate(@Nullable Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);

          database = FirebaseDatabase.getInstance();
          categories = database.getReference("Category");

     }

     @Nullable
     @Override
     public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
          myFragment = inflater.inflate(R.layout.fragment_category,container,false);


          listCategory = (RecyclerView)myFragment.findViewById(R.id.listCategory);
          listCategory.setHasFixedSize(true);
          layoutManager = new LinearLayoutManager(container.getContext());
          listCategory.setLayoutManager(layoutManager);

          loadCategories();

          return myFragment;
     }

     private void loadCategories() {
         adapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(
                 Category.class,
                 R.layout.category_layout,
                 CategoryViewHolder.class,
                 categories
         ) {
             @Override
             protected void populateViewHolder(CategoryViewHolder viewHolder, final Category model, int i) {
                 viewHolder.category_name.setText(model.getName());
                  Picasso.with((getActivity()))
                          .load(model.getImage())
                          .into(viewHolder.category_image);

                  viewHolder.setItemClicklistener(new ItemClicklistener() {
                       @Override
                       public void onClick(View view, int position, boolean isLongClick) {
                            //Toast.makeText(getActivity(), String.format("%s|%s",adapter.getRef(position).getKey(),model.getName()), Toast.LENGTH_SHORT).show();
                           Intent startGame = new Intent(getActivity(), Start.class);
                           Common.categoryId = adapter.getRef(position).getKey();
                           Common.categoryName = model.getName();
                           startActivity(startGame);
                       }
                  });
             }
         };
         adapter.notifyDataSetChanged();
         listCategory.setAdapter(adapter);
     }
}
