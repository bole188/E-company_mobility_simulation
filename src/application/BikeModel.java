package application;

import backEnd.Bycicle;
import javafx.beans.property.SimpleStringProperty;

public class BikeModel {
	public final SimpleStringProperty id;
	public final SimpleStringProperty priceOfAcquisition;
	public final SimpleStringProperty model;
	public final SimpleStringProperty batteryLevel;
    public final SimpleStringProperty manufacturer;
    public final SimpleStringProperty maxReach;
	public BikeModel(Bycicle c) {
		super();
		id = new SimpleStringProperty(c.getUniqueId());
		this.priceOfAcquisition = new SimpleStringProperty(Integer.toString(c.getPriceOfAcq()));
		model = new SimpleStringProperty(c.getModel());
		this.batteryLevel =new SimpleStringProperty((String) Integer.toString(c.getBatteryLevel()));
		this.manufacturer = new SimpleStringProperty((String) c.getManufacturer());
		this.maxReach =new SimpleStringProperty((String) Integer.toString(c.getReach()));
	}
}
	
