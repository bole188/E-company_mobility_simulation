package backEnd;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import application.BreakdownSceneController;
import application.MainSceneController;
import javafx.application.Platform;

public class Rent extends Thread {
	private LocalDate dateOfRent;
	private LocalTime timeOfRent;
	private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d.M.yyyy");
	private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
	private User userId;
	private Vehicle rentedVehicle;
	private String vehicleId;
	private Coordinates startingPoint;
	private Coordinates endingPoint;
	private int timeOfRide;
	private boolean promotion;
	private boolean isBroken;
	private MainSceneController controller;
	
	public User getUserId() {
		return this.userId;
	}
	
	public Coordinates getStartingPoint() {
		return startingPoint;
	}
	
	public Coordinates getEndingPoint() {
		return endingPoint;
	}
	
	public int getTimeOfRide() {
		return this.timeOfRide;
	}
	
	public boolean getPromotion() {
		return promotion;
	}
	 public boolean getIsBroken() {
	    return isBroken;
	}
	public void setIsBroken(boolean flag) {
		this.isBroken = flag;
	}
	public LocalDate getDateOfRent() {
		return dateOfRent;
	}
	public LocalTime getTimeOfRent() {
		return timeOfRent;
	}
	public Rent(String[] rentData,MainSceneController msc,BreakdownSceneController bdsc) {
			controller = msc;
			rentData[0] = rentData[0].substring(1);
			rentData[rentData.length-1] = rentData[rentData.length-1].substring(0, rentData[rentData.length-1].length()-1); 
			String[] dateAndTime = rentData[0].split(" ");
			this.dateOfRent = LocalDate.parse(dateAndTime[0],dateFormatter);
			this.timeOfRent = LocalTime.parse(dateAndTime[1],timeFormatter);
			this.userId = new User(rentData[1]);
			this.vehicleId = rentData[2];
			this.rentedVehicle = Company.listOfAllVehicles.stream()
					.filter(vehicle->vehicle.getUniqueId().equals(vehicleId))
					.findFirst()
					.orElseThrow();			
			Company.rentedVehicles.add(rentedVehicle);
			String[] coordinates = rentData[3].substring(2, rentData[3].length()-2).split(",");
			int a = Integer.parseInt(coordinates[0]);
			int b = Integer.parseInt(coordinates[1]);
			this.startingPoint = new Coordinates(a,b);
			String[] coordinates2 = rentData[4].substring(2, rentData[4].length()-2).split(",");
			a = Integer.parseInt(coordinates2[0]);
			b = Integer.parseInt(coordinates2[1]);
			this.endingPoint = new Coordinates(a,b);
			this.timeOfRide = Integer.parseInt(rentData[5]);
			boolean broken = "da".equals(rentData[6]);
			this.setIsBroken(broken);
			this.promotion = "da".equals(rentData[7]);
	}
	
	public LocalDate getDate() {
		return this.dateOfRent;
	}
	
	public LocalTime getTime() {
		return this.timeOfRent;
	}
	
	private void generateReceipt() {
		Properties parametersProps = new Properties();
		try {
			InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("parameters.properties"); 
			parametersProps.load(input);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int wideDistancePrice = Integer.parseInt(parametersProps.getProperty("DISTANCE_WIDE"));
		int narrowDistancePrice = Integer.parseInt(parametersProps.getProperty("DISTANCE_NARROW"));
		int discount = Integer.parseInt(parametersProps.getProperty("DISCOUNT"));
		int discountProm = Integer.parseInt(parametersProps.getProperty("DISCOUNT_PROM"));
		int unitPrice = 0;
		if (this.rentedVehicle instanceof Car)
			unitPrice = Integer.parseInt(parametersProps.getProperty("CAR_UNIT_PRICE"));
		if (this.rentedVehicle instanceof Bycicle)
			unitPrice = Integer.parseInt(parametersProps.getProperty("BIKE_UNIT_PRICE"));
		if (this.rentedVehicle instanceof Scooter)
			unitPrice = Integer.parseInt(parametersProps.getProperty("SCOOTER_UNIT_PRICE"));
		double finalPrice = calculateFinalPrice(wideDistancePrice,narrowDistancePrice,discount,discountProm,unitPrice);
		if (this.getIsBroken() == true) 
			{
			System.out.println(this.vehicleId + " broken.-------------------------------------------------------------------------");
			finalPrice = 0;
			}
		String relativePath = parametersProps.getProperty("RENT_RECEIPTS_PATH");
		Company.receiptsDir = Paths.get(relativePath).toAbsolutePath().toString(); 
		try {
			Semaphore sem = new Semaphore(1);
			sem.acquire();
			if(!Files.exists(Paths.get(Company.receiptsDir)))
				Files.createDirectory(Paths.get(Company.receiptsDir));
			sem.release();
			String pathToFile = Company.receiptsDir + File.separator + this.vehicleId + ".txt";
			this.writeData(pathToFile,finalPrice);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void writeData(String pathToFile, double finalPrice) {
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(pathToFile,true))){
			String data = "Date of rent:" + this.dateOfRent.format(DateTimeFormatter.ISO_LOCAL_DATE) + "\nTime of rent:"
					+ this.timeOfRent.format(DateTimeFormatter.ofPattern("HH:mm"))+ "\nVehicle id:" + 
					this.vehicleId + "\nBattery level:" + this.rentedVehicle.getBatteryLevel() + "\nUser passport:" +  
					this.userId.getPassport() + "\nUser personal id:" + this.userId.getPersonalId() + "\n" + 
					"Starting point:" + this.getStartingPoint().getX() + "," + this.getStartingPoint().getY() + "\n" + 
					"Ending point:" + this.getEndingPoint().getX() + "," + this.getEndingPoint().getY() + "\nTime of the ride:" + 
					this.getTimeOfRide() + "\nDriving licence:";
			if (this.rentedVehicle instanceof Car) 
				data+= this.userId.getDrivingLicence();
			Properties parametersProps = new Properties();
			try {
				InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("parameters.properties"); 
				parametersProps.load(input);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			data+= "\nDiscount:";
			if (this.userId.getRentCount()%10 == 0)				
				data+= parametersProps.getProperty("DISCOUNT") + "\n";
			else 
				data+="0\n";
			data+= "Promotion discount:" + parametersProps.getProperty("DISCOUNT_PROM")+ "\n";
			data+="Is broken:" + Boolean.toString(this.getIsBroken()) + "\n";
			data += "Price to be paid:" + finalPrice + "\n";
			bw.write(data);
			System.out.println("after writeData:" + this.vehicleId + " " + this.timeOfRent + " " + this.dateOfRent);
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public String getDateTimeID() {
		return this.dateOfRent.toString() + this.timeOfRent.toString() + this.vehicleId;
	}
	
	private double calculateFinalPrice(int wideDistancePrice, int narrowDistancePrice, int discount, int discountProm,
			int unitPrice) {
		double basePrice = (double)unitPrice * this.timeOfRide;
		
		double totalPrice;
		if(this.startingPoint.X > 4 && this.startingPoint.Y<15 && this.endingPoint.X > 4 && this.endingPoint.Y<15)
			totalPrice = basePrice * narrowDistancePrice;
		else
			totalPrice = basePrice * wideDistancePrice;
		
		double totalPriceAfterDiscounts = totalPrice;
		if (this.promotion == true)
			totalPriceAfterDiscounts -= totalPrice * (discountProm/100.00);
		if (this.userId.getRentCount()%10 == 0)
			totalPriceAfterDiscounts -= totalPrice * (discount/100.00);
		System.out.println("total price:" + totalPriceAfterDiscounts);
		return totalPriceAfterDiscounts;
	}
	
	public void run() {
		Platform.runLater(()->{
			controller.addLabel(vehicleId,this.rentedVehicle.getBatteryLevel(), startingPoint.X, startingPoint.Y);
		});
		System.out.println("Starting vehicle:"+this.vehicleId);
		this.rentedVehicle.move(this.startingPoint,this.endingPoint,this.timeOfRide,controller);
		this.generateReceipt();
		Company.cdl.countDown();
	}
	public Vehicle getRentedVehicle() {		
		return this.rentedVehicle;
	}
	public String getVehicleId() {
		return this.vehicleId;
	}
}
