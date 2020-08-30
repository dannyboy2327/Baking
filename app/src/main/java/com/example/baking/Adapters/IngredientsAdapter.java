package com.example.baking.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baking.Classes.Ingredient;
import com.example.baking.R;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder> {

    private Context mContext;
    private List<Ingredient> mIngredient;

    public IngredientsAdapter(Context context, List<Ingredient> ingredients){
        mContext = context;
        mIngredient = ingredients;
    }

    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.ingredient_list_item,
                parent,
                false);
        return new IngredientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsViewHolder holder, int position) {
        holder.mIngredientTextView.setText(mIngredient.get(position).getIngredient());
        holder.mQuantityTextView.setText(mIngredient.get(position).getQuantity().toString());
        holder.mMeasureTextView.setText(mIngredient.get(position).getMeasure());
    }

    @Override
    public int getItemCount() {
        return mIngredient.size();
    }

    public class IngredientsViewHolder extends RecyclerView.ViewHolder {

        private TextView mIngredientTextView;
        private TextView mQuantityTextView;
        private TextView mMeasureTextView;

        public IngredientsViewHolder(@NonNull View itemView) {
            super(itemView);

            mIngredientTextView = itemView.findViewById(R.id.tv_ingredient);
            mQuantityTextView = itemView.findViewById(R.id.tv_quantity);
            mMeasureTextView = itemView.findViewById(R.id.tv_measure);
        }
    }
}
