package com.example.receptbanken;

import org.brickred.socialauth.AuthProvider;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.util.AccessGrant;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewRecipe extends Activity {

	RecipeManager recipeManager;
	Recipe recipe;
	int recipeId;
	private SocialAuthAdapter twitterAdapter;
	private SharedPreferences sharedPref; 
	public static Twitter twitter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_recipe);
		
		sharedPref = getPreferences(MODE_PRIVATE);
		
		
		recipeManager = new RecipeManager(getFilesDir());
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			recipeId = Integer.parseInt(extras.getString("RecipeId"));
		}
		else{
			recipeId = 0;
		}
		recipe = recipeManager.getRecipeAtId(recipeId);	
		setValues();
		
		
		twitterAdapter = new SocialAuthAdapter(new DialogListener(){
			@Override
			public void onError(SocialAuthError arg0) {
				sharedPref.edit().clear().commit();
			}
			
			@Override
			public void onComplete(Bundle bundle) {
				AuthProvider authProv = twitterAdapter.getCurrentProvider();
				AccessGrant accessGrant = authProv.getAccessGrant();
				new verifyAndGoToTwitter().execute(accessGrant.getKey(), accessGrant.getSecret());
			}
			
			@Override
			public void onCancel() {
				 Log.d("Custom-UI" , "Cancelled");
			}
			
			@Override
			public void onBack() {	
			}
		});
	}
	
	
	public void backToFirstPage(View view){
		Intent back = new Intent(this, RecipeList.class);
		startActivity(back);	
	}
	
	
	public void twitterButton(View view){
		twitterAdapter.authorize(this, Provider.TWITTER);
	}
	
	
	private class verifyAndGoToTwitter extends AsyncTask<String, Void, Void>{

		@Override
		protected Void doInBackground(String... params) {
			String accessToken = params[0];
			String accessSecret = params[1];
			
			ConfigurationBuilder confBuilder = new ConfigurationBuilder();
			confBuilder.setDebugEnabled(true)
			  .setOAuthConsumerKey("Y8i9GXLh9tTTfa3gPDIjVg")
			  .setOAuthConsumerSecret("PgnIyZhDrUbCHvJuzbsNzv3hWV2r1bnh6f6GxUO4v4")
			  .setOAuthAccessToken(accessToken)
			  .setOAuthAccessTokenSecret(accessSecret);
			TwitterFactory tf = new TwitterFactory(confBuilder.build());
			Twitter twitter = tf.getInstance();
			ViewRecipe.twitter = twitter;
			try {
				User user = twitter.verifyCredentials();
				if(user != null){				
					Editor edit = sharedPref.edit();
					edit.putString("access_token", accessToken);
					edit.putString("access_secret", accessSecret);
					edit.commit();
				}
			} catch (TwitterException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			Intent toTwitter = new Intent(ViewRecipe.this, TwitterPage.class);
			toTwitter.putExtra("RecipeId", recipeId);
			startActivity(toTwitter);
		}
	}
	
	
	public void shareButton(View view){		
		Intent toContacts = new Intent(this, ContactList.class);
		toContacts.putExtra("RecipeId",Integer.toString(recipeId));
		startActivity(toContacts);	
	}
	
	
	public void setValues(){
		TextView textViewName = (TextView)findViewById(R.id.textView); 	
		TextView textViewIngredients = (TextView)findViewById(R.id.textView6); 
		TextView textViewDescribe = (TextView)findViewById(R.id.textView7);
		ImageView imgView = (ImageView)findViewById(R.id.imageView1);
		
		String ingredients = "";
		for(int i = 0;i < recipe.getNumberOfIngredients();i++){
			ingredients += recipe.getIngredientsById(i) + "\n";
		}
		textViewIngredients.setText(ingredients);
		textViewName.setText(recipe.getName());
		textViewDescribe.setText(recipe.getDescription());
		
		imgView.setImageBitmap(BitmapFactory.decodeFile(recipe.getImagePath()));
	}	
}