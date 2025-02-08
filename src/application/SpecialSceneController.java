package application;

import backEnd.Scooter;
import backEnd.Vehicle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;

public class SpecialSceneController {
	@FXML
	public Pane startingPane;
	@FXML
	public Pane buttonPane;
	private ObservableList<SpecVehicleModel> specVehicleModels;
	
	@FXML
	public TableView<SpecVehicleModel> tableView;
	
	@FXML
	private TableColumn<SpecVehicleModel,String> id;

	@FXML
	private TableColumn<SpecVehicleModel,String> priceOfAcq;

	@FXML
	private TableColumn<SpecVehicleModel,String> model;

	@FXML
	private TableColumn<SpecVehicleModel,String> manufacturer;

	@FXML
	private TableColumn<SpecVehicleModel,String> batteryLevel;
	
	@FXML
	private TableColumn<SpecVehicleModel,String> losses;
	
	public void initialize() {
    	id.setCellValueFactory(cellData -> cellData.getValue().id);
        priceOfAcq.setCellValueFactory(cellData -> cellData.getValue().priceOfAcq);
        model.setCellValueFactory(cellData -> cellData.getValue().model);
        manufacturer.setCellValueFactory(cellData -> cellData.getValue().manufacturer);
        batteryLevel.setCellValueFactory(cellData -> cellData.getValue().batteryLevel);
        losses.setCellValueFactory(cellData -> cellData.getValue().losses);
        specVehicleModels = FXCollections.observableArrayList();
        tableView.setItems(specVehicleModels);
    }
	
    public void addSpecVehicleModel(Vehicle v,Double d) {
    	SpecVehicleModel cm = new SpecVehicleModel(v,d);
        specVehicleModels.add(cm);  // Add the CarModel to the ObservableList
    }
}
