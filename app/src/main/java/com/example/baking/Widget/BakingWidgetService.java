package com.example.baking.Widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.baking.Classes.Ingredient;
import com.example.baking.Classes.Recipe;
import com.example.baking.R;

import com.google.gson.Gson;

import java.util.List;

public class BakingWidgetService extends IntentService {

    private static final String LOG_TAG = BakingWidgetService.class.getSimpleName();

    private static final String ACTION_OPEN_RECIPE =
            "com.example.baking.Widget.baking_widget_service";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public BakingWidgetService(String name) {
        super(name);
    }

    public BakingWidgetService() {
        super("BakingWidgetService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_OPEN_RECIPE.equals(action)) {
                handleActionOpenRecipe();
            }
        }
    }

    private void handleActionOpenRecipe() {
        SharedPreferences sharedPreferences = getSharedPreferences("recipe", MODE_PRIVATE);
        String json = sharedPreferences.getString("recipe", "");
        Gson gson = new Gson();
        Recipe recipe = gson.fromJson(json, Recipe.class);

        int num = recipe.getId();
        int imgResId = 0;

        switch (num) {
            case 1:
                imgResId = R.drawable.chocolate;
                break;
            case 2:
                imgResId = R.drawable.brownie;
                break;
            case 3:
                imgResId = R.drawable.yellowcake;
                break;
            case 4:
                imgResId = R.drawable.cheesecake;
                break;
        }

        List<Ingredient> ingredients = recipe.getIngredients();
        StringBuilder stringBuilder = new StringBuilder();

        for (Ingredient ingredient : ingredients) {
            String quantity = String.valueOf(ingredient.getQuantity());
            String measure  = String.valueOf(ingredient.getMeasure());
            String name = ingredient.getIngredient();
            String line = quantity + " " + measure + " " + name;
            stringBuilder.append(line).append("\n");
        }

        String ingredientList = stringBuilder.toString();
        Log.d(LOG_TAG, ingredientList);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int [] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingWidget.class));
        BakingWidget.updateWidgetRecipe(this, ingredientList, imgResId, appWidgetManager, appWidgetIds);
    }

    public static void startActionOpenRecipe(Context context) {
        Intent intent = new Intent(context, BakingWidgetService.class);
        intent.setAction(ACTION_OPEN_RECIPE);
        context.startService(intent);
    }

    public static void startActionOpenRecipeO(Context context) {
        Intent intent = new Intent(context, BakingWidgetService.class);
        intent.setAction(ACTION_OPEN_RECIPE);
        ContextCompat.startForegroundService(context, intent);
    }
}
