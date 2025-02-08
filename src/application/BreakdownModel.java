package application;

import backEnd.Rent;
import javafx.beans.property.SimpleStringProperty;

public class BreakdownModel {
	public final SimpleStringProperty type;
	public final SimpleStringProperty id;
	public final SimpleStringProperty dateAndTime;
	public final SimpleStringProperty description;
	public BreakdownModel(Rent r) {
		type = new SimpleStringProperty(r.getRentedVehicle().getClass().getName().substring(8));
		id = new SimpleStringProperty(r.getVehicleId());
		dateAndTime = new SimpleStringProperty(r.getDateOfRent().toString()+ " " +r.getTimeOfRent().toString());
		description = new SimpleStringProperty(r.getRentedVehicle().getBreakdownDescription());
	}
}
