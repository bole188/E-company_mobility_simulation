package application;

import backEnd.Bycicle;
import backEnd.Rent;
import backEnd.Vehicle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.scene.layout.Pane;

public class BreakdownSceneController {
	@FXML
	public Pane startingPane;
	@FXML
	public TableView<BreakdownModel> tableView;
	private ObservableList<BreakdownModel> breakdownModels;
	
	@FXML
	private TableColumn<BreakdownModel,String> id;
	@FXML
	private TableColumn<BreakdownModel,String> dateAndTime;
	@FXML
	private TableColumn<BreakdownModel,String> description;
	@FXML
	private TableColumn<BreakdownModel,String> type;
	@FXML
	public Pane buttonPane;
	
	public void initialize() {
    	id.setCellValueFactory(cellData -> cellData.getValue().id);
        type.setCellValueFactory(cellData -> cellData.getValue().type);
        dateAndTime.setCellValueFactory(cellData -> cellData.getValue().dateAndTime);
        description.setCellValueFactory(cellData -> cellData.getValue().description);
        
        breakdownModels = FXCollections.observableArrayList();
        tableView.setItems(breakdownModels);
    }
	
    public void addBreakdownModel(Rent r) {
        BreakdownModel cm = new BreakdownModel(r);
        breakdownModels.add(cm);
    }
	
}
