package application;

import backEnd.Scooter;
import backEnd.Vehicle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;

public class ScooterSceneController {
	@FXML
	public TableView<ScooterModel> tableView;
    private ObservableList<ScooterModel> scooterModels;
    
    @FXML
    public Pane startingPane;

    @FXML
    public Pane buttonPane;

    
    @FXML
    public TableColumn<ScooterModel, String> id;
    
    @FXML
    public TableColumn<ScooterModel, String> price;
    
    @FXML
    public TableColumn<ScooterModel, String> model;
    
    @FXML
    public TableColumn<ScooterModel, String> batteryLevel;
    
    @FXML
    public TableColumn<ScooterModel, String> manufacturer;

    @FXML
    public TableColumn<ScooterModel, String> speed;
    
    public void initialize() {
    	id.setCellValueFactory(cellData -> cellData.getValue().id);
        price.setCellValueFactory(cellData -> cellData.getValue().priceOfAcquisition);
        model.setCellValueFactory(cellData -> cellData.getValue().model);
        manufacturer.setCellValueFactory(cellData -> cellData.getValue().manufacturer);
        batteryLevel.setCellValueFactory(cellData -> cellData.getValue().batteryLevel);
        speed.setCellValueFactory(cellData -> cellData.getValue().speed);
        scooterModels = FXCollections.observableArrayList();
        tableView.setItems(scooterModels);
    }
	
    public void addScooterModel(Vehicle v) {
        ScooterModel cm = new ScooterModel((Scooter)v);
        scooterModels.add(cm);  // Add the CarModel to the ObservableList
    }

}
