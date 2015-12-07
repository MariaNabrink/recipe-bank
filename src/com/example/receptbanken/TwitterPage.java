package com.example.receptbanken;

import java.io.File;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class TwitterPage extends Activity {
	
	RecipeManager recipeManager;
	Recipe recipe;
	int recipeId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_twitter_page);
		
		recipeManager = new RecipeManager(getFilesDir());
		recipeId = getIntent().getExtras().getInt("RecipeId",0);
		recipe = recipeManager.getRecipeAtId(recipeId);	
		setValues();
	}
	
	
	public void tweetButton(View view){
		EditText composeText = (EditText) findViewById(R.id.editText1);
		File imgFile = new File(recipeManager.getRecipeAtId(recipeId).getImagePath());
		StatusUpdate update = new StatusUpdate(composeText.getText().toString());
		update.media(imgFile);
		new sendTweets().execute(update);
	}
	
	
	private class sendTweets extends AsyncTask<StatusUpdate, Void, Boolean>{

		@Override
		protected Boolean doInBackground(StatusUpdate... params) {			
			StatusUpdate status = params[0];
			Twitter twitter = ViewRecipe.twitter;
			try{
				twitter.updateStatus(status);
			}catch(TwitterException e){
				e.printStackTrace();
				return false;
			}
			return true;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if(result){
				Toast.makeText(TwitterPage.this, "Skickat till Twitter!",Toast.LENGTH_LONG).show();
				Intent getBack = new Intent(TwitterPage.this, RecipeList.class);
				startActivity(getBack);
			}else{
				Toast.makeText(TwitterPage.this, "Går ej att twittra!",Toast.LENGTH_LONG).show();
			}
		}
	}
	
	
	public void backwardsButton(View view){
		Intent back = new Intent(this, RecipeList.class);
		startActivity(back);
	}
	
	
	public void setValues(){
		ImageView image = (ImageView)findViewById(R.id.imageView1);
		image.setImageBitmap(BitmapFactory.decodeFile(recipe.getImagePath()));
	}	
}
