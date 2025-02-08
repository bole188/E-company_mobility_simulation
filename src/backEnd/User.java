package backEnd;

import java.util.Random;

public class User {
	private String name;
	private boolean isDomestic;
	private String personalId = "";
	private String passport = "";
	private String drivingLicence;
	private int rentCount;
	
	public boolean getIsDomestic() {
		return isDomestic;
	}
	
	public int getRentCount() {
		return rentCount;
	}
	
	public String getDrivingLicence() {
		return drivingLicence;
	}
	
	public String getPassport() {
		return passport;
	}
	
	public String getPersonalId() {
		return this.personalId;
	}
	
	public String getName() {
		return this.name;
	}
	
	public User(String name) {
		this.name = name;
		Random random = new Random();
		this.isDomestic = 1 == random.nextInt(2) ? true : false; 
		if (this.isDomestic) {
			this.personalId = "ID" +  String.valueOf(random.nextInt(9999, 99999));
		}
		else {
			this.passport = "PP" + String.valueOf(random.nextInt(9999, 99999));
		}
		this.drivingLicence = "DL" + String.valueOf(random.nextInt(9999, 99999));
		this.rentCount = 0;
	}
}
