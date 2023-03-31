/*
 * Anna Lee: 30137463
 * Caleb Cavilla: 30145972
 * Desmond O'Brien: 30064340
 * Jose Perales: 30143354
 * Matthew Cusanelli: 30145324
 * Muhtadi Alam: 30150910
 * Omar Tejada: 31052626
 * Saadman Rahman: 30153482
 * Sahaj Malhotra: 30144405
 * Sean Tan: 30094560
 * Tanvir Ahamed Himel: 30148868
 * Victor campos: 30106934
 */
package com.autovend.software;
import java.util.ArrayList;

public class MemberDatabase {

/**
 * Represents a cheap and dirty version of a set of databases that the
 * simulation can interact with.
 */
 
	/**
	 * Instances of this class are not needed, so the constructor is private.
	 */
	private MemberDatabase() {}
	
	/**
	 * The known membership numbers of users.
	 */
	public static ArrayList<String> MEMBERSHIP_DATABASE = new ArrayList<String>();
	
	
	public static boolean userExists(String number) {
		for (String key: MEMBERSHIP_DATABASE) {
			if (number.equals(key)) {
				return true;
			} 
		}
		return false;
	}
	

//	/**
//	 * A count of the items of the given product that are known to exist in the
//	 * store. Of course, this does not account for stolen items or items that were
//	 * not correctly recorded, but it helps management to track inventory.
//	 */
//	public static final Map<Product, Integer> INVENTORY = new HashMap<>();
	

}
