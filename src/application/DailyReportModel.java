package application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import backEnd.Company;
import javafx.beans.property.SimpleStringProperty;

public class DailyReportModel {

	public  SimpleStringProperty date;
	public SimpleStringProperty income;
	public  SimpleStringProperty discount;
	public  SimpleStringProperty promoDiscount;
    public  SimpleStringProperty incomeFromDT;
    public  SimpleStringProperty incomeFromOS;
    public  SimpleStringProperty maintenance;
    public  SimpleStringProperty repair;

    public DailyReportModel(Path path) {
    	try {
    		String actualDate = path.toString().split("\\\\")[path.toString().split("\\\\").length-1].substring(0,10);
    		if(actualDate.startsWith("d"))
    			actualDate = "total";
			date = new SimpleStringProperty(actualDate);
    		income = new SimpleStringProperty(Double.toString(Company.calculateTotalIncome(path.toString())));
    		discount = new SimpleStringProperty(Double.toString(Company.calculateTotalDiscount(path.toString())));
    		promoDiscount = new SimpleStringProperty(Double.toString(Company.calculateTotalPromoDiscount(path.toString())));
    		incomeFromDT = new SimpleStringProperty(Double.toString(Company.calculateIncomeOfDowntownRides(path.toString())));
    		incomeFromOS = new SimpleStringProperty(Double.toString(Company.calculateIncomeOfOutskirtsRides(path.toString())));
    		maintenance = new SimpleStringProperty(Double.toString(Company.calculateMaintenanceCosts(path.toString())));
    		repair = new SimpleStringProperty(Double.toString(Company.calculateRepairLoss(path.toString())));
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
    }
    
    public DailyReportModel(){
    	try {
			Company.createDailyReportBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
