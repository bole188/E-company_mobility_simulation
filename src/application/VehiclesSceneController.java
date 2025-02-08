package application;

import backEnd.Car;
import backEnd.Vehicle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;

public class VehiclesSceneController {
    @FXML
    public TableView<CarModel> tableView;
    private ObservableList<CarModel> carModels;

    @FXML
    public Pane buttonPane;
    
    @FXML
    public Pane startingPane;
    
    @FXML
    public TableColumn<CarModel, String> id;
    
    @FXML
    public TableColumn<CarModel, String> price;
    
    @FXML
    public TableColumn<CarModel, String> model;
    
    @FXML
    public TableColumn<CarModel, String> batteryLevel;
    
    @FXML
    public TableColumn<CarModel, String> manufacturer;

    @FXML
    public TableColumn<CarModel, String> numOfPassengers;

    @FXML
    public TableColumn<CarModel, String> timeOfAcq;

    @FXML
    public void initialize() {
    	id.setCellValueFactory(cellData -> cellData.getValue().id);
        price.setCellValueFactory(cellData -> cellData.getValue().priceOfAcquisition);
        model.setCellValueFactory(cellData -> cellData.getValue().model);
        manufacturer.setCellValueFactory(cellData -> cellData.getValue().manufacturer);
        batteryLevel.setCellValueFactory(cellData -> cellData.getValue().batteryLevel);
        numOfPassengers.setCellValueFactory(cellData -> cellData.getValue().numOfPassengers);
        timeOfAcq.setCellValueFactory(cellData -> cellData.getValue().dateOfAcq);
        carModels = FXCollections.observableArrayList();
        tableView.setItems(carModels);
    }
    
    public void addCarModel(Vehicle v) {
        CarModel cm = new CarModel((Car)v);
        carModels.add(cm);  // Add the CarModel to the ObservableList
    }
}
