package backEnd;

import java.util.*;

public class Car extends Vehicle {
	private int numberOfPassengers;
	private String dateOfAcquistion;
	
	public int getNOP() {
		return numberOfPassengers;
	}
	
	public String getDOA(){
		return dateOfAcquistion;
	}
	
	public Car(String id,String mn,String m,String dateOfAcq, int priceOfAcq, String bd) {
		super(id,mn,m,priceOfAcq,bd);
		Random random = new Random();
		this.numberOfPassengers = random.nextInt(5);
		this.dateOfAcquistion = dateOfAcq;
	}
}
