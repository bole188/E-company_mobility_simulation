package application;

import backEnd.Vehicle;
import javafx.beans.property.SimpleStringProperty;

public class SpecVehicleModel {
	public final SimpleStringProperty id;
	public final SimpleStringProperty priceOfAcq;
	public final SimpleStringProperty model;
	public final SimpleStringProperty batteryLevel;
    public final SimpleStringProperty manufacturer;
    public final SimpleStringProperty losses;

	public SpecVehicleModel(Vehicle c,Double d) {
		super();
		id = new SimpleStringProperty(c.getUniqueId());
		this.priceOfAcq = new SimpleStringProperty(Integer.toString(c.getPriceOfAcq()));
		model = new SimpleStringProperty(c.getModel());
		this.batteryLevel =new SimpleStringProperty((String) Integer.toString(c.getBatteryLevel()));
		this.manufacturer = new SimpleStringProperty((String) c.getManufacturer());
		this.losses =new SimpleStringProperty((String) Double.toString(d));
	}
}
