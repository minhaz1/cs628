package com.example.googlemapdemo;
import java.util.ArrayList;


public class UtilityFunctions {

	public static float floatAverage(ArrayList<Float> values) {
		float total = 0;
		for (float fl : values)
			total += fl;
		return total / values.size();
	}
	
}
