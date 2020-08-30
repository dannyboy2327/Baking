package com.example.baking.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baking.Adapters.IngredientsAdapter;
import com.example.baking.Classes.Ingredient;
import com.example.baking.Classes.Recipe;
import com.example.baking.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IngredientsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private Recipe mRecipe;
    private List<Ingredient> ingredients = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_ingredients_list, container, false);

        if (savedInstanceState == null) {
            fetchBundle();
        } else {
            if (savedInstanceState.containsKey("ingredients")) {
                ingredients = (List<Ingredient>) savedInstanceState.getSerializable("ingredients");
            }
        }
        mRecyclerView = rootView.findViewById(R.id.rv_ingredients);

        setupRecyclerView();
        fetchRecipeSteps();

        return rootView;
    }

    private void fetchRecipeSteps() {
        mRecyclerView.setAdapter(new IngredientsAdapter(getActivity(), ingredients));
    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
    }

    private void fetchBundle() {
        Bundle bundle = this.getArguments();

        assert bundle != null;
        mRecipe = (Recipe) bundle.getSerializable("recipeIngredients");

        ingredients = mRecipe.getIngredients();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("ingredients", (Serializable) ingredients);
    }
}
