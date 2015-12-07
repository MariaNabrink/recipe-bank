package com.example.receptbanken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import android.graphics.Bitmap;
import android.util.Log;

public class RecipeManager {
	
	List<Recipe> recipeList = new ArrayList<Recipe>();
	File dir;
	
	public RecipeManager(File dir){
		this.dir = dir;
		loadRecipes();
	}
	
	
	public String saveImage(Bitmap recipeImage, int id){
		File imageDir = dir;
		File imageFile= new File(imageDir + "/image" + id + ".png");
		FileOutputStream outStream;
		try{
			outStream = new FileOutputStream(imageFile);
			recipeImage.compress(Bitmap.CompressFormat.PNG, 90, outStream);
			outStream.close();
		}catch(IOException e){
			Log.d("recipeImage save", e.getMessage());
		}
		
		return imageFile.getPath();
	}
	
	
	public void createNewRecipe(Recipe recipe){
		recipeList.add(recipe);
	}
	
	
	public void saveRecipes(){
		
		File file = dir;
		Log.d("saved", file.getAbsolutePath());
		File fileName = new File(file + "/savedFiles.txt");
		FileOutputStream fileOut;
		ObjectOutputStream objOut;
			try {
				fileOut = new FileOutputStream(fileName);
				objOut = new ObjectOutputStream(fileOut);
				objOut.writeObject(recipeList);
				objOut.close();
				fileOut.close();
			   }
			   catch (IOException e) {
			       Log.e("Exception", "Det gick inte att skriva till fil: " + e.toString());
			   } 
	}
	
	
	public int getListSize(){
		return recipeList.size();
	}
	
	
	public Recipe getRecipeAtId(int id){
		return recipeList.get(id);
	}
	
	
	@SuppressWarnings("unchecked")
	public void loadRecipes(){
		
		File file = dir;
		Log.d("saved", file.getAbsolutePath());
		File fileName = new File(file + "/savedFiles.txt");
		FileInputStream fileIn;
		ObjectInputStream objIn;
		try{
			fileIn = new FileInputStream(fileName);
			objIn = new ObjectInputStream(fileIn);
			recipeList = (ArrayList<Recipe>)objIn.readObject();
			objIn.close();
			fileIn.close();
		}
		catch (IOException e){
			Log.e("Exception", "Det gick inte att hämta filen: " + e.toString());
		}
		catch(ClassNotFoundException classEx){
			Log.e("Exception", "Ingen giltig class hittades: " + classEx.toString());
		}
	}
	

	public List<Recipe> getList(){
		return recipeList;
	}
}
