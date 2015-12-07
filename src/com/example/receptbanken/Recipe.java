package com.example.receptbanken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Recipe implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	String name; 
	String description;
	String imagePath;
	List<String> ingredients = new ArrayList<String>();
	
	public Recipe(String name, String description){
		setName(name);
		setDescription(description);
	}


	public String getImagePath() {
		return imagePath;
	}


	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}


	public void addIngredient(String ingredient){
		ingredients.add(ingredient);
	}
	
	
	public void removeIngredientAt(int id){
		ingredients.remove(id);
	}
	
	
	public int getNumberOfIngredients(){
		return ingredients.size();
	}
	
	
	public String getIngredientsById(int id){
		return ingredients.get(id);
	}
	
	
	public String getName() {
		return name;
	}

	
	public void setName(String name) {
		this.name = name;
	}

	
	public String getDescription() {
		return description;
	}

	
	public void setDescription(String description) {
		this.description = description;
	}	
}
