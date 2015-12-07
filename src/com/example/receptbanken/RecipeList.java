package com.example.receptbanken;

import java.util.ArrayList;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RecipeList extends ActionBarActivity {

	RecipeManager recipeManager;
	ArrayAdapter<String> recipeAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipe_list);
		
		recipeManager = new RecipeManager(getFilesDir());
		updateList();
		
		ListView recipeList = (ListView) findViewById(R.id.recipeList);
		recipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id){
				Intent toViewRecipe = new Intent(getApplicationContext(), ViewRecipe.class);
				toViewRecipe.putExtra("RecipeId", Integer.toString(position));
				startActivity(toViewRecipe);	 
			}
		});
	}
	
	
	public void backToFirstPage(View view){
		Intent back = new Intent(this, StartPage.class);
		startActivity(back);	
	}
	
	
	public void updateList(){
		ArrayList<String> listOfRecipes = new ArrayList<String>(); 
		
		for(int i = 0;i < recipeManager.getListSize();i++){
			listOfRecipes.add(recipeManager.getRecipeAtId(i).getName());
		}
		recipeAdapter = new ArrayAdapter<String>(this,R.layout.recipe_list_row, listOfRecipes);
		ListView recipeList = (ListView) findViewById(R.id.recipeList);
		recipeList.setAdapter(recipeAdapter);
	}
}
