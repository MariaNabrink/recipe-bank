package com.example.receptbanken;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StartPage extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_page);
	}
	
	
	public void savedRecipes(View view){
		Intent toSavedRecipes = new Intent(this, RecipeList.class);
		startActivity(toSavedRecipes);	
	}
	
	
	public void newRecipe(View view){
		Intent toNewRecipes = new Intent(this, NewRecipe.class);
		startActivity(toNewRecipes);
	}

}
