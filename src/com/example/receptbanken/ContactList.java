package com.example.receptbanken;

import java.io.File;
import java.util.ArrayList;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ContactList extends Activity {
	
	RecipeManager recipeManager;
	Recipe recipe;
	int recipeId;
	public ListView text;
	ArrayAdapter<String> contactAdapter;
	ArrayList<String> listOfContacts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_list);
		
		recipeManager = new RecipeManager(getFilesDir());
		
		text = (ListView) findViewById(R.id.listView1);
		getContacts();
		text.setAdapter(contactAdapter);
		
		
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			recipeId = Integer.parseInt(extras.getString("RecipeId"));
		}
		else{
			recipeId = 0;
		}
		
		recipe = recipeManager.getRecipeAtId(recipeId);	
		
		text.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parentAdapter, View view, int position, long id){
				email(listOfContacts.get(position));
			}
		});
	}
	
	
	public void backToFirstPage(View view){
		Intent back = new Intent(this, ViewRecipe.class);
		startActivity(back);	
	}
	
	
	public void getContacts(){ 
		listOfContacts = new ArrayList<String>();
		
		String email = null;
		
		Uri uri = ContactsContract.Contacts.CONTENT_URI;
		String id = ContactsContract.Contacts._ID;
		String hasPhoneNumber = ContactsContract.Contacts.HAS_PHONE_NUMBER;

		Uri emailUri =  ContactsContract.CommonDataKinds.Email.CONTENT_URI;
		String emailId = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
		String data = ContactsContract.CommonDataKinds.Email.DATA;
				
		ContentResolver contentRes = getContentResolver();
		Cursor cursor = contentRes.query(uri, null,null, null, null);
		
		if(cursor.getCount() > 0){
			while(cursor.moveToNext()){
				String contactId = cursor.getString(cursor.getColumnIndex(id));
				int phoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(hasPhoneNumber)));
				
				if(phoneNumber > 0){	
					Cursor emailCursor = contentRes.query(emailUri, null, emailId + 
							" = ?", new String[] { contactId }, null);
					
					while(emailCursor.moveToNext()){
						email = emailCursor.getString(emailCursor.getColumnIndex(data));
						listOfContacts.add(email);
					}
					emailCursor.close();
				}
			}
		}
		contactAdapter = new ArrayAdapter<String>(this,R.layout.contact_list_row, listOfContacts);
	}
	
	
	public void email(String mailAddress){
		File imgFile = new File(recipeManager.getRecipeAtId(recipeId).getImagePath());
		Uri imageUri = Uri.fromFile(imgFile);
		
		Intent sendEmailIntent = new Intent(Intent.ACTION_SEND, Uri.parse("Skicka till:"));
		sendEmailIntent.setType("application/image");
		
		sendEmailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{mailAddress});
		sendEmailIntent.putExtra(Intent.EXTRA_SUBJECT, "Recept");
		sendEmailIntent.putExtra(Intent.EXTRA_TEXT, getMailContent());
		sendEmailIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
	
		startActivity(Intent.createChooser(sendEmailIntent, "Välj en E-mail: "));
	}
	

	public String getMailContent(){
		String mailContent = " ";
		mailContent += recipeManager.getRecipeAtId(recipeId).getName() + "\n";
		for(int i = 0; i < recipe.getNumberOfIngredients(); i++){
			mailContent += recipeManager.getRecipeAtId(recipeId).getIngredientsById(i) + "\n";
		}
		mailContent += recipeManager.getRecipeAtId(recipeId).getDescription();
		
		return mailContent;
	}
}
