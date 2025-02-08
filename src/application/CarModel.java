package application;

import backEnd.Car;
import javafx.beans.property.SimpleStringProperty;

public class CarModel extends VehicleModel {
	public final SimpleStringProperty id;
	public final SimpleStringProperty priceOfAcquisition;
	public final SimpleStringProperty model;
	public final SimpleStringProperty batteryLevel;
    public final SimpleStringProperty manufacturer;
    public final SimpleStringProperty numOfPassengers;
    public final SimpleStringProperty dateOfAcq;
	public CarModel(Car c) {
		super();
		id = new SimpleStringProperty(c.getUniqueId());
		this.priceOfAcquisition = new SimpleStringProperty(Integer.toString(c.getPriceOfAcq()));
		this.dateOfAcq =new SimpleStringProperty(c.getDOA());
		model = new SimpleStringProperty(c.getModel());
		this.batteryLevel =new SimpleStringProperty((String) Integer.toString(c.getBatteryLevel()));
		this.manufacturer = new SimpleStringProperty((String) c.getManufacturer());
		this.numOfPassengers =new SimpleStringProperty((String) Integer.toString(c.getNOP()));
	}
}
