package com.muhammadyaseenfatimamazharsarfarz.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.muhammadyaseenfatimamazharsarfarz.quizapp.databinding.FragmentHomeBinding;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    FragmentHomeBinding binding;
    FirebaseFirestore database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        database = FirebaseFirestore.getInstance();
        final ArrayList<CategoryModel> categories = new ArrayList<>();

        final CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), categories);
        database.collection("categories").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                categories.clear();
                for (DocumentSnapshot snapshot : value.getDocuments()) {
                    CategoryModel model = snapshot.toObject(CategoryModel.class);
                    model.setCategoryId(snapshot.getId());
                    categories.add(model);
                }
                categoryAdapter.notifyDataSetChanged();
            }
        });
        binding.categoryList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        binding.categoryList.setAdapter(categoryAdapter);

        binding.spinwheel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),SpinnerActivity.class));
              
            }
        });
        return binding.getRoot();
    }
}