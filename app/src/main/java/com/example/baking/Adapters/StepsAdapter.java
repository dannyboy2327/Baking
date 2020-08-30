package com.example.baking.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baking.Classes.Step;
import com.example.baking.R;

import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {

    private static final String LOG_TAG = StepsAdapter.class.getSimpleName();

    private Context mContext;
    private List<Step> mSteps;
    private OnStepClickListener mListener;

    public interface OnStepClickListener {
        void onStepClick(Step step);
    }


    public StepsAdapter(Context context, List<Step> steps, OnStepClickListener listener){
        mContext = context;
        mSteps = steps;
        mListener = listener;
    }

    public StepsAdapter(Context context, List<Step> steps){
        mContext = context;
        mSteps = steps;
    }


    @NonNull
    @Override
    public StepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.steps_list_item,
                parent,
                false);

        return new StepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsViewHolder holder, int position) {
        holder.mTextView.setText("Step " + mSteps.get(position).getId() + ": " + mSteps.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

    public class StepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTextView;

        public StepsViewHolder(@NonNull View itemView) {
            super(itemView);

            mTextView = itemView.findViewById(R.id.tv_recipe_Ingredients_Label);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Log.d(LOG_TAG, mSteps.get(position).getId().toString());
            Step step = mSteps.get(position);
            mListener.onStepClick(step);
        }
    }
}

