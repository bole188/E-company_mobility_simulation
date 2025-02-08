package application;

import backEnd.Scooter;
import javafx.beans.property.SimpleStringProperty;

public class ScooterModel {
	public final SimpleStringProperty id;
	public final SimpleStringProperty priceOfAcquisition;
	public final SimpleStringProperty model;
	public final SimpleStringProperty batteryLevel;
    public final SimpleStringProperty manufacturer;
    public final SimpleStringProperty speed;
	public ScooterModel(Scooter c) {
		super();
		id = new SimpleStringProperty(c.getUniqueId());
		this.priceOfAcquisition = new SimpleStringProperty(Integer.toString(c.getPriceOfAcq()));
		model = new SimpleStringProperty(c.getModel());
		this.batteryLevel =new SimpleStringProperty((String) Integer.toString(c.getBatteryLevel()));
		this.manufacturer = new SimpleStringProperty((String) c.getManufacturer());
		this.speed =new SimpleStringProperty((String) Integer.toString(c.getMaxSpeed()));
	}
}
