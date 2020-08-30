package com.example.baking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baking.Adapters.StepsAdapter;
import com.example.baking.Classes.Recipe;
import com.example.baking.Classes.Step;
import com.example.baking.Fragments.IngredientsFragment;
import com.example.baking.Fragments.RecipeDetailsListFragment;
import com.example.baking.Fragments.VideoPlayerFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RecipeDetails extends AppCompatActivity implements VideoPlayerFragment.OnPreviousButtonClickListener, VideoPlayerFragment.OnNextButtonClickListener {

    private static final String LOG_TAG = RecipeDetails.class.getSimpleName();

    private Recipe mRecipe;
    private List<Step> mSteps = new ArrayList<>();
    private int mVideoNumberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            fetchIntent();
        } else if (savedInstanceState != null) {
            mRecipe = (Recipe) savedInstanceState.getSerializable("recipes");
        }

        if (!getResources().getBoolean(R.bool.isTablet)) {
            Bundle stepsFragmentBundle = new Bundle();
            stepsFragmentBundle.putSerializable("recipeDetails", mRecipe);
            RecipeDetailsListFragment stepListFragment = new RecipeDetailsListFragment();
            stepListFragment.setArguments(stepsFragmentBundle);
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.recipe_detail_list_container, stepListFragment)
                    .commit();
        } else {
            Bundle stepsFragmentBundle = new Bundle();
            stepsFragmentBundle.putSerializable("recipeDetails", mRecipe);
            RecipeDetailsListFragment stepListFragment = new RecipeDetailsListFragment();
            stepListFragment.setArguments(stepsFragmentBundle);
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.recipe_detail_list_container, stepListFragment)
                    .commit();
        }


    }


    private void fetchIntent() {
        Intent recipeDetails = getIntent();
        Bundle bundle = recipeDetails.getExtras();

        assert bundle != null;
        mRecipe = (Recipe) bundle.getSerializable("details");

    }


    public void goToIngredients(View view) {

        if (!getResources().getBoolean(R.bool.isTablet)) {
            Bundle ingredientFragmentBundle = new Bundle();
            ingredientFragmentBundle.putSerializable("recipeIngredients", mRecipe);
            IngredientsFragment ingredientFragment = new IngredientsFragment();
            ingredientFragment.setArguments(ingredientFragmentBundle);
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_detail_list_container, ingredientFragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            Bundle ingredientFragmentBundle = new Bundle();
            ingredientFragmentBundle.putSerializable("recipeIngredients", mRecipe);
            IngredientsFragment ingredientFragment = new IngredientsFragment();
            ingredientFragment.setArguments(ingredientFragmentBundle);
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.child_container, ingredientFragment)
                    .addToBackStack(null)
                    .commit();
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPreviousButtonClicked(Step step) {
        mSteps = mRecipe.getSteps();
        mVideoNumberId = step.getId();

        mVideoNumberId--;

        if (mVideoNumberId < 0) {
            Toast.makeText(this, "No previous step. Please click next step", Toast.LENGTH_SHORT)
                    .show();
        }


        if (mVideoNumberId >= 0 && mVideoNumberId != mSteps.size() - 1) {

            step = mSteps.get(mVideoNumberId);

            if (!getResources().getBoolean(R.bool.isTablet)) {
                Bundle videoPlayerBundle = new Bundle();
                videoPlayerBundle.putSerializable("videoPlayer", step);

                VideoPlayerFragment videoPlayerFragment = new VideoPlayerFragment();
                videoPlayerFragment.setArguments(videoPlayerBundle);

                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .replace(R.id.recipe_detail_list_container, videoPlayerFragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                Bundle videoPlayerBundle = new Bundle();
                videoPlayerBundle.putSerializable("videoPlayer", step);

                VideoPlayerFragment videoPlayerFragment = new VideoPlayerFragment();
                videoPlayerFragment.setArguments(videoPlayerBundle);

                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .replace(R.id.child_container, videoPlayerFragment)
                        .addToBackStack(null)
                        .commit();
            }

        }

    }

    @Override
    public void onNextButtonClicked(Step step) {
        mSteps = mRecipe.getSteps();
        mVideoNumberId = step.getId();

        mVideoNumberId++;

        if (mVideoNumberId > mSteps.size() - 1) {
            Toast.makeText(this, "No next step. Please click previous step", Toast.LENGTH_SHORT)
                    .show();
        }

        if (mVideoNumberId >= 0 && mVideoNumberId <= mSteps.size() - 1) {
            step = mSteps.get(mVideoNumberId);

            if (!getResources().getBoolean(R.bool.isTablet)) {
                Bundle videoPlayerBundle = new Bundle();
                videoPlayerBundle.putSerializable("videoPlayer", step);

                VideoPlayerFragment videoPlayerFragment = new VideoPlayerFragment();
                videoPlayerFragment.setArguments(videoPlayerBundle);

                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .replace(R.id.recipe_detail_list_container, videoPlayerFragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                Bundle videoPlayerBundle = new Bundle();
                videoPlayerBundle.putSerializable("videoPlayer", step);

                VideoPlayerFragment videoPlayerFragment = new VideoPlayerFragment();
                videoPlayerFragment.setArguments(videoPlayerBundle);

                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .replace(R.id.child_container, videoPlayerFragment)
                        .addToBackStack(null)
                        .commit();
            }

        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("recipes", mRecipe);
    }
}