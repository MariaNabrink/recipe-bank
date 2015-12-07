package com.example.receptbanken;

import java.io.IOException;
import java.io.InputStream;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NewRecipe extends ActionBarActivity {

	RecipeManager recipeManager;
	private static final int REQUEST_CODE = 1;
	private Bitmap recipeImage;
	ImageView imgView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_recipe);
		
		imgView = (ImageView) findViewById(R.id.imageView1);
		recipeManager = new RecipeManager(getFilesDir());
	}
	
	
	public void backToFirstPage(View view){
		Intent back = new Intent(this, StartPage.class);
		startActivity(back);	
	}
	
	
	public void getImage(View view){
		Intent getImage = new Intent();
		getImage.setType("image/*");
		getImage.setAction(Intent.ACTION_GET_CONTENT);
		getImage.addCategory(Intent.CATEGORY_OPENABLE);
		startActivityForResult(getImage, REQUEST_CODE);
	}
	
	
	private Bitmap getThumbNail(Uri uri) throws IOException {
		InputStream stream = getContentResolver().openInputStream(uri);
		BitmapFactory.Options bounds = new BitmapFactory.Options();
		bounds.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(stream, null, bounds);
		stream.close();
		
		if(bounds.outHeight == -1 && bounds.outWidth == -1){
			return null;
		}
			int originalSize = 0;
			
			if(bounds.outHeight > bounds.outWidth){
				originalSize = bounds.outHeight;
			}else{
				originalSize = bounds.outWidth;
			}
		stream = getContentResolver().openInputStream(uri);	
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inSampleSize = originalSize / 50;
		Bitmap tmp= BitmapFactory.decodeStream(stream, null, opts);
		stream.close();
		return tmp;
	}
	
		
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK)
                if (recipeImage != null) {
                    recipeImage.recycle();
                }
        		Uri imageUri = data.getData();
                 
                try {
					recipeImage = getThumbNail(imageUri);
					imgView.setImageBitmap(recipeImage);
					imgView.invalidate();
					
			} catch (IOException e) {
				e.printStackTrace();
			}
         super.onActivityResult(requestCode, resultCode, data);
    }
	
	
	public void saveToFile(View view){
		TextView textViewName = (TextView)findViewById(R.id.textViewName); 	
		TextView textViewIngredients = (TextView)findViewById(R.id.textViewIngredients); 
		TextView textViewDescribe = (TextView)findViewById(R.id.textViewDescribe);	
		String name = textViewName.getText().toString();
		String ingredients = textViewIngredients.getText().toString();
		String describe = textViewDescribe.getText().toString();
		
		Recipe recipe = new Recipe(name, describe);
		recipe.addIngredient(ingredients);
		
		if(recipeImage != null){
			String path = recipeManager.saveImage(recipeImage, recipeManager.getListSize());
			recipe.setImagePath(path);
		}
		recipeManager.createNewRecipe(recipe);
		
		recipeManager.saveRecipes();
		
		textViewName.setText("");
		textViewIngredients.setText("");
		textViewDescribe.setText("");
		imgView.setImageDrawable(null);
		Toast.makeText(NewRecipe.this, "Receptet sparades!", Toast.LENGTH_SHORT).show();
	}
}
