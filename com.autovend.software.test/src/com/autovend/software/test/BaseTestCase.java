package com.autovend.software.test;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.autovend.devices.SelfCheckoutStation;
import com.autovend.software.SelfCheckoutSystemLogic;
/**
Desmond O'Brien: 30064340
Matthew Cusanelli: 30145324
Saadman Rahman: 30153482
Tanvir Ahamed Himel: 30148868
Victor campos: 30106934
Sean Tan: 30094560
Sahaj Malhotra: 30144405 
Caleb Cavilla: 30145972
Muhtadi Alam: 30150910
Omar Tejada: 31052626
Jose Perales: 30143354
Anna Lee: 30137463
 */
public class BaseTestCase {
	protected SelfCheckoutStation station;
	protected SelfCheckoutSystemLogic system;
	protected int weightLimit = 100; 

	protected void initializeStation() {
		Currency curr = Currency.getInstance(Locale.CANADA);
		
		int[] billDenoms = {5,10,20,50,100};
		
		double[] coinDenomFloats = {0.01f, 0.5f, 0.10f, 0.25f, 1f, 2f};
		BigDecimal[] coinDenoms = new BigDecimal[coinDenomFloats.length];
		for (int i = 0; i < coinDenomFloats.length; ++i) {
			coinDenoms[i] = BigDecimal.valueOf(coinDenomFloats[i]);
		}
		
		int scaleMax = 100;
		int scaleSensitivity = 1;
		
		this.station = new SelfCheckoutStation(curr, billDenoms, coinDenoms, scaleMax, scaleSensitivity);
		this.system = new SelfCheckoutSystemLogic(this.station);
	}

	
	protected void deinitializeStation() {
		this.station = null;
	}



}
