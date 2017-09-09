package com.squalala.streamable.utils;

import android.text.TextUtils;

/**
 * Auteur : Fayçal Kaddouri
 * Nom du fichier : Check.java
 * Date : 24 juin 2014
 * 
 */
public class Check {

	public static boolean isNameValid(String name) {
		return name.length() >= 7;
	}

	public static boolean isPasswordValid(String password) {
		return password.length() >= 5;
	}
	
	public static boolean isValidEmail(CharSequence target) {
	  if (TextUtils.isEmpty(target))
		return false;
	  else
		return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	}
	


}
