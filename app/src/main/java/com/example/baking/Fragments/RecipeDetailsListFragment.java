package com.example.baking.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baking.Adapters.StepsAdapter;
import com.example.baking.Classes.Recipe;
import com.example.baking.Classes.Step;
import com.example.baking.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RecipeDetailsListFragment extends Fragment {

    private static final String LOG_TAG = RecipeDetailsListFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private Recipe mRecipe;
    private List<Step> steps = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipe_details_list, container, false);

        mRecyclerView = rootView.findViewById(R.id.rv_steps);


        fetchBundle();
        setupRecyclerView();
        fetchRecipeSteps();

        return rootView;
    }

    private void fetchRecipeSteps() {
        mRecyclerView.setAdapter(new StepsAdapter(getActivity(), steps, new StepsAdapter.OnStepClickListener() {
            @Override
            public void onStepClick(Step step) {

                if (!getResources().getBoolean(R.bool.isTablet)) {
                    Bundle videoBundle = new Bundle();
                    videoBundle.putSerializable("videoPlayer", step);

                    VideoPlayerFragment videoPlayerFragment = new VideoPlayerFragment();
                    videoPlayerFragment.setArguments(videoBundle);

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.recipe_detail_list_container, videoPlayerFragment)
                            .addToBackStack(null)
                            .commit();
                } else {
                    Bundle videoBundle = new Bundle();
                    videoBundle.putSerializable("videoPlayer", step);

                    VideoPlayerFragment videoPlayerFragment = new VideoPlayerFragment();
                    videoPlayerFragment.setArguments(videoBundle);

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.child_container, videoPlayerFragment)
                            .addToBackStack(null)
                            .commit();
                }

            }
        }));
    }


    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
    }

    private void fetchBundle() {
        Bundle bundle = this.getArguments();

        assert bundle != null;
        mRecipe = (Recipe) bundle.getSerializable("recipeDetails");
        Log.d(LOG_TAG, mRecipe.getName());

        steps = mRecipe.getSteps();
    }

}
