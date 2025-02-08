package application;

import java.nio.file.Path;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.scene.layout.Pane;

public class DailyReportSceneController {
	@FXML
	private Pane startingPane;
	@FXML
	private TableView<DailyReportModel> tableView;

	@FXML
	public Pane buttonPane;
	
	private ObservableList<DailyReportModel> DailyReportModels;
	
	@FXML
	private TableColumn<DailyReportModel,String> date;
	@FXML
	private TableColumn<DailyReportModel,String> income;
	@FXML
	private TableColumn<DailyReportModel,String> discount;
	@FXML
	private TableColumn<DailyReportModel,String> promoDiscount;
	@FXML
	private TableColumn<DailyReportModel,String> incomeFromDT;
	@FXML
	private TableColumn<DailyReportModel,String> incomeFromOS;
	@FXML
	private TableColumn<DailyReportModel,String> maintenance;
	@FXML
	private TableColumn<DailyReportModel,String> repair;
	
	public void initialize() {
    	date.setCellValueFactory(cellData -> cellData.getValue().date);
        income.setCellValueFactory(cellData -> cellData.getValue().income);
        discount.setCellValueFactory(cellData -> cellData.getValue().discount);
        promoDiscount.setCellValueFactory(cellData -> cellData.getValue().promoDiscount);
        incomeFromDT.setCellValueFactory(cellData -> cellData.getValue().incomeFromDT);
        incomeFromOS.setCellValueFactory(cellData -> cellData.getValue().incomeFromOS);
        maintenance.setCellValueFactory(cellData -> cellData.getValue().maintenance);
        repair.setCellValueFactory(cellData -> cellData.getValue().repair);
        DailyReportModels = FXCollections.observableArrayList();
        tableView.setItems(DailyReportModels);
    }
	public void addDailyReportModel(Path path) {
        DailyReportModel cm = new DailyReportModel(path);
        DailyReportModels.add(cm);
    }
	
}
