package application;

import backEnd.Bycicle;
import backEnd.Scooter;
import backEnd.Vehicle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.TableView;

import javafx.scene.layout.Pane;

import javafx.scene.control.TableColumn;

public class BikeSceneController {
	@FXML
	public Pane startingPane;
	@FXML
	private TableView<BikeModel> tableView;
	private ObservableList<BikeModel> bikeModels;
	
	@FXML
	private TableColumn<BikeModel,String> id;
	@FXML
	private TableColumn<BikeModel,String> price;
	@FXML
	private TableColumn<BikeModel,String> model;
	@FXML
	private TableColumn<BikeModel,String> batteryLevel;
	@FXML
	private TableColumn<BikeModel,String> manufacturer;
	@FXML
	private TableColumn<BikeModel,String> reach;
	@FXML
	public Pane buttonPane;

	public void initialize() {
    	id.setCellValueFactory(cellData -> cellData.getValue().id);
        price.setCellValueFactory(cellData -> cellData.getValue().priceOfAcquisition);
        model.setCellValueFactory(cellData -> cellData.getValue().model);
        manufacturer.setCellValueFactory(cellData -> cellData.getValue().manufacturer);
        batteryLevel.setCellValueFactory(cellData -> cellData.getValue().batteryLevel);
        reach.setCellValueFactory(cellData -> cellData.getValue().maxReach);
        bikeModels = FXCollections.observableArrayList();
        tableView.setItems(bikeModels);
    }
	
    public void addBikeModel(Vehicle v) {
        BikeModel cm = new BikeModel((Bycicle)v);
        bikeModels.add(cm);  // Add the CarModel to the ObservableList
    }
}
