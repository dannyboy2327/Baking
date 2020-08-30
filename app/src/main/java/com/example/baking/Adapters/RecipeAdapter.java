package com.example.baking.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baking.Classes.Recipe;
import com.example.baking.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    public interface RecipeViewClickListener {
        void onRecipeClick(Recipe recipe);
    }

    Context mContext;
    List<Recipe> mRecipes;
    RecipeViewClickListener mListener;

    public RecipeAdapter(Context context, List<Recipe> recipes, RecipeViewClickListener listener) {
        mContext = context;
        mRecipes = recipes;
        mListener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_item, parent, false);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {

        holder.mTextView.setText(mRecipes.get(position).getName());

        switch (position) {
            case 0:
                Picasso.get().load(R.drawable.chocolate).into(holder.mImageView);
                break;
            case 1:
                Picasso.get().load(R.drawable.brownie).into(holder.mImageView);
                break;
            case 2:
                Picasso.get().load(R.drawable.yellowcake).into(holder.mImageView);
                break;
            case 3:
                Picasso.get().load(R.drawable.cheesecake).into(holder.mImageView);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTextView;
        private ImageView mImageView;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            mTextView = itemView.findViewById(R.id.tv_recipeName);
            mImageView = itemView.findViewById(R.id.iv_recipeImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Recipe recipe = mRecipes.get(position);
            mListener.onRecipeClick(recipe);
        }
    }
}
