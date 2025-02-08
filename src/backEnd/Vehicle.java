package backEnd;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.Semaphore;

import application.MainSceneController;
import javafx.application.Platform;

public abstract class Vehicle implements Serializable{
	private static final long serialVersionUID = 1L;
	private String uniqueId;
	private int priceOfAcquisition;
	private String model;
	private int batteryLevel;
	private String manufacturer;
	private String breakdownDescription;
	private String breakdownDateTime;
	private String rentData;
    public Semaphore semaphore = new Semaphore(1,true);

    public String getUniqueId() {
    	return uniqueId;
    }
    public String getModel() {
    	return model;
    }
    public String getManufacturer() {
    	return manufacturer;
    }
    public String getBreakdownDateTime() {
    	return this.breakdownDateTime;
    }
    public String getRentData() {
    	return rentData;
    }
    public int getPriceOfAcq() {
    	return this.priceOfAcquisition;
    }
    public int getBatteryLevel() {
    	return batteryLevel;
    }
    public Vehicle(String id,String mn,String m,int priceOfAcq, String bd) {
    	this.uniqueId = id;
    	this.priceOfAcquisition = priceOfAcq;
    	this.model = m;
    	this.batteryLevel = 100;
    	this.manufacturer = mn;
    	this.breakdownDateTime = "";
    	this.breakdownDescription = bd;
    	this.rentData = "";
    }
    
    
    
    public String GetId() {
    	return uniqueId;
    }
    
    private void reduceBattery() {
    	this.batteryLevel-=1;
    }
    
    public void chargeBattery() {
    	this.batteryLevel = 100;
    	System.out.println("Current battery level: "+ this.batteryLevel);
    }
    
    private int calculateDistance(Coordinates c1, Coordinates c2) {
    	return Math.abs(c1.X - c2.X) + Math.abs(c1.Y - c2.Y); 
    }
    
    public void move(Coordinates c1, Coordinates c2, int timeOfUsage, MainSceneController msc) {
    	int totalDistance = calculateDistance(c1,c2);
    	int initialField = 0;
    	double timePerField = timeOfUsage/(double)totalDistance;
    	while (initialField < totalDistance) {
    		synchronized(msc) {
        		Platform.runLater(()->msc.moveLabel(uniqueId,this.batteryLevel,c2.X ,c2.Y));
        	}
    		
        	long timePerFieldInMillis = Math.round(timePerField * 1000 *Company.executionScale);
    		System.out.println("Vehicle id: " + this.GetId() + ", battery level: " + this.batteryLevel);
    		initialField++;
    		this.reduceBattery();
    		try{
    			Thread.sleep(timePerFieldInMillis);
    		}
    		catch(Exception e) {
    			e.printStackTrace();
    		}
    	}
    }

	public String getBreakdownDescription() {
		return this.breakdownDescription;
	}
    
}
