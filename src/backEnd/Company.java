package backEnd;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.function.Function;
import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.*;

import application.BikeSceneController;
import application.BreakdownSceneController;
import application.MainSceneController;
import application.ScooterSceneController;
import application.VehiclesSceneController;
import javafx.application.Platform;

public class Company {

	public static String absPathToRentingData = Paths.get("rents.csv").toAbsolutePath().toString();
	public static String absPathToVehicles = Paths.get("vehicles.csv").toAbsolutePath().toString();
	public static LinkedList<Vehicle> listOfAllVehicles = new LinkedList<>();
	public static LinkedList<Vehicle> rentedVehicles = new LinkedList<>();
	private static LinkedList<String[]> rentingData = new LinkedList<>();
	public static LinkedList<Rent> rents = new LinkedList<>();
	private static Map<LocalDate,List<Rent>> mappedRents = new HashMap<>();
	public static LocalDate cd;
	public static int numOfRents;
	public static CountDownLatch cdl;
	public static String pathToDailyReportFolder = Paths.get("dailyReport").toAbsolutePath().toString();
	public static String receiptsDir;
	public static volatile double executionScale = 1;
	public static String pathToSerFolder = Paths.get("worstVehicles").toAbsolutePath().toString();
	
	public static int getAllRentsWithSameTime(LocalTime lt) {
		int rentsCount = 0;
		LinkedList<Rent> copyOfRents = new LinkedList<>(Company.rents);
		try{
			while(copyOfRents.pop().getTime().compareTo(lt) == 0) rentsCount++;
		}catch(NoSuchElementException nse) {
			Company.rents.stream().forEach(rent->System.out.println("Vehicle id:" + rent.getVehicleId()));
			
		}
		return rentsCount;
	}

	public static void loadAndSortRentingData() {
		try (BufferedReader br = new BufferedReader(new FileReader(Company.absPathToRentingData))) {
			String line = br.readLine();
			while((line = br.readLine())!= null) {
				String[] listOfArguments = null;
				try {
					listOfArguments = line.split(",");
				}catch(Exception e) {
					continue;
				}
				if(listOfArguments.length!=10) continue;
				listOfArguments[3] = listOfArguments[3] + "," + listOfArguments[4];
				listOfArguments[4] = listOfArguments[5] + "," + listOfArguments[6];
				listOfArguments[5] = listOfArguments[7];
				listOfArguments[6] = listOfArguments[8];
				listOfArguments[7] = listOfArguments[9];
				if(!Company.isValid(listOfArguments)) continue;
				String[] newListOfArguments = Arrays.copyOf(listOfArguments,8);
				Company.rentingData.add(newListOfArguments);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public static void generateRents(MainSceneController msc,BreakdownSceneController bdsc) {
		for (String[] rent : Company.rentingData) {
			Company.rents.add(new Rent(rent,msc,bdsc));
		}
	}
	
	private static boolean isValid(String[] rent) {
		LocalTime timeOfRent = null;
		LocalDate dateOfRent = null;
		rent[rent.length-1] = rent[rent.length-1].substring(0, rent[rent.length-1].length()-1); 
		try {
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d.M.yyyy");
			DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
			String[] dateAndTime = rent[0].substring(1).split(" ");
			dateOfRent = LocalDate.parse(dateAndTime[0],dateFormatter);
			timeOfRent = LocalTime.parse(dateAndTime[1],timeFormatter);
		}
		catch (Exception e) {
		    System.out.println("Problem with splitting date&time, skipping this line.");
		    return false;
		}
		try {
			String vehicleId = rent[2];
			if(!Company.checkIfExists(vehicleId)) throw new Exception("No such vehicle."); 
			Vehicle v = Company.listOfAllVehicles.stream()
			.filter(vehicle->vehicle.getUniqueId().equals(vehicleId))
			.findFirst()
			.orElseThrow();
		}catch(Exception e) {
			System.out.println("Problem with vehicle, skipping.");
			return false;
		}
		try {
			String[] coordinates = rent[3].substring(2, rent[3].length()-2).split(",");
			int a = Integer.parseInt(coordinates[0]);
			int b = Integer.parseInt(coordinates[1]);
			if(!(a <= 20 && a >= 0  && b <=20 && b >= 0)) throw new Exception("Invalid coordinates.");
			String[] coordinates2 = rent[4].substring(2, rent[4].length()-2).split(",");
			int a1 = Integer.parseInt(coordinates2[0]);
			int b1 = Integer.parseInt(coordinates2[1]);
			if(!(a1 <= 20 && a1 >= 0  && b1 <=20 && b1 >= 0)) throw new Exception("Invalid coordinates.");
		}catch(Exception e) {
			System.out.println("Skipping.");
			return false;
		}
		try {
			int tor = Integer.parseInt(rent[5]);
		}catch(Exception e) {
			System.out.println("Skipping.");
			return false;
		}
		return true;
	}

	public static void mapRents(BreakdownSceneController bdsc) {
		Company.mappedRents = rents.stream()
	            .collect(Collectors.groupingBy(
	                    Rent::getDate, // Group by date
	                    Collectors.collectingAndThen(
	                        Collectors.toList(), // Collect to list
	                        list -> {
	                            list.sort(Comparator.comparing(Rent::getTime)); // Sort by time
	                            return list;
	                        }
	                    )
	                ));
		Company.rents.clear();
		for (var entry : Company.mappedRents.entrySet()) {
			Company.rents.addAll(0,entry.getValue());
		}
		Company.rents = Company.rents.stream()
		.collect(Collectors
				.collectingAndThen(Collectors
						.toMap(Rent::getDateTimeID, rent->rent,(existing,replacement)->existing,LinkedHashMap::new), map->new LinkedList<Rent>(map.values())));
		Company.rents.stream().forEach(rent->System.out.println(rent.getDate() + " " + rent.getTime() + " " + rent.getVehicleId()));
		Company.rents.stream().forEach(rent->{
			if (rent.getIsBroken()) {
				Platform.runLater(()->bdsc.addBreakdownModel(rent));
			}
		});
	}
	
	public static void loadAllVehicles(VehiclesSceneController vsc,ScooterSceneController ssc, BikeSceneController bsc) {
		System.out.println(Company.absPathToVehicles);
		try (BufferedReader br = new BufferedReader(new FileReader(Company.absPathToVehicles))) {
			String line = br.readLine();
			while((line = br.readLine())!= null) {
				String[] listOfParameters = line.split(",");
				Vehicle vehicle = null;
				try {
					if(listOfParameters[listOfParameters.length-1].equals("automobil")) {
						vehicle = new Car(listOfParameters[0],listOfParameters[1],listOfParameters[2],listOfParameters[3],Integer.parseInt(listOfParameters[4]),listOfParameters[7]);
						
					}
					if(listOfParameters[listOfParameters.length-1].equals("trotinet")) {
						vehicle = new Scooter(listOfParameters[0],listOfParameters[1],listOfParameters[2],Integer.parseInt(listOfParameters[4]),Integer.parseInt(listOfParameters[6]),listOfParameters[7]);
						
					}
					if(listOfParameters[listOfParameters.length-1].equals("bicikl")) {
						vehicle = new Bycicle(listOfParameters[0],listOfParameters[1],listOfParameters[2],Integer.parseInt(listOfParameters[4]),Integer.parseInt(listOfParameters[5]),listOfParameters[7]);
						
					}
					if(vehicle ==null ) System.out.println(listOfParameters[listOfParameters.length-1]);
				}catch(Exception e) {
					continue;
				}
				Company.listOfAllVehicles.add(vehicle);
			}
				List<Vehicle> vehiclesWODuplicates = Company.listOfAllVehicles.stream()
						.collect(Collectors.toMap(Vehicle::GetId, v->v,(existing,duplicate)->existing))
						.values()
						.stream()
						.collect(Collectors.toList());
				Company.listOfAllVehicles.clear();
				Company.listOfAllVehicles.addAll(vehiclesWODuplicates);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean checkIfExists(String id) {
		return Company.listOfAllVehicles.stream()
				.anyMatch(vehicle -> vehicle.getUniqueId().equals(id));
	}
	
	public static void anualReport() {
		try{
			double totalIncome = Company.calculateTotalIncome(Company.receiptsDir);
			double totalDiscount = Company.calculateTotalDiscount(Company.receiptsDir);
			double totalPromotion = Company.calculateTotalPromoDiscount(Company.receiptsDir);
			double incomeFromDowntownRides = Company.calculateIncomeOfDowntownRides(Company.receiptsDir);
			double incomeFromOutskirtsRides = Company.calculateIncomeOfOutskirtsRides(Company.receiptsDir);
			double maintenanceCosts = Company.calculateMaintenanceCosts(Company.receiptsDir);
			double lossForRepairs = Company.calculateRepairLoss(Company.receiptsDir);
			double companyLosses = Company.calculateCompanyLosses(Company.receiptsDir);
			double totalTaxFee = Company.calculateTax(Company.receiptsDir);
			System.out.println("Total income:" + totalIncome + "\nTotal discount:" + totalDiscount + "\nTotal promotion:" + totalPromotion + 
					"\nIncome from dt rides:" + incomeFromDowntownRides + "\nIncome from os rides:" + incomeFromOutskirtsRides + "\nMaintenance costs:" + maintenanceCosts 
					+ "\nLoss for repairs:" + lossForRepairs + "\nCompany losses:" + companyLosses + "\nTax fee:" + totalTaxFee);
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public static double calculateMaintenanceCosts(String directory) throws IOException{
		return 0.2 * Company.calculateTotalIncome(directory);
	}
	
	public static double calculateCompanyLosses(String directory) throws IOException{
		return 0.2 * Company.calculateTotalIncome(directory);
	}
	
	public static double calculateTax(String directory) throws IOException{
		return 0.1 * (Company.calculateTotalIncome(directory)  - Company.calculateRepairLoss(directory) - Company.calculateCompanyLosses(directory)
				- Company.calculateMaintenanceCosts(directory));
	}
	
	
	public static double calculateTotalPromoDiscount(String directory) throws IOException{
		Path dir = Paths.get(directory);
		if(!Files.isDirectory(dir)) {
			return Company.readPromoDiscountAndCalcValue(dir);
		}
		return Files.list(dir)
				.filter(path->path.toString().endsWith(".txt"))
				.mapToDouble(Company::readPromoDiscountAndCalcValue)
				.sum();
	}
	
	public static double readPromoDiscountAndCalcValue(Path path) {
		double discountPerFile = 0;
		try(BufferedReader br = new BufferedReader(new FileReader(path.toString()))){
			String line;
			int lineNum = 0;
			double totalDiscount = 0;
			double totalPromotionDiscount = 0;
			while((line = br.readLine()) != null) {
				lineNum++;
				if(lineNum == 11 || (lineNum-11)%14 ==0) {
					totalDiscount = Double.parseDouble(line.split(":")[1]);
				}
				if(lineNum == 12 || (lineNum-12)%14 ==0)
					totalPromotionDiscount = Double.parseDouble(line.split(":")[1]);
				if(lineNum%14 == 0)
					discountPerFile += Company.calculatePromoDiscountValue(totalDiscount,totalPromotionDiscount,Double.parseDouble(line.split(":")[1]));
			}
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
		return discountPerFile;
	}
	
	public static double calculatePromoDiscountValue(double totalDiscount, double totalPromotionDiscount,
			double double1) {
		double basePrice =  double1 / (100-(totalDiscount+totalPromotionDiscount));
		return basePrice*(totalPromotionDiscount/100.00);
	}

	public static double calculateTotalDiscount(String directory) throws IOException {
		Path path = Paths.get(directory);
		if(!Files.isDirectory(path)) {
			return Company.readDiscountAndCalculateValue(path);
		}
		return Files.list(path)
				.filter(p->p.toString().endsWith(".txt"))
				.mapToDouble(Company::readDiscountAndCalculateValue)
				.sum();
	}
	
	public static double readDiscountAndCalculateValue(Path path){
		double discountPerFile = 0;
		try(BufferedReader br = new BufferedReader(new FileReader(path.toString()))){
			String line;
			int lineNum = 0;
			double totalDiscount = 0;
			double totalPromotionDiscount = 0;
			while((line = br.readLine()) != null) {
				lineNum++;
				if(lineNum == 11 || (lineNum-11)%14 ==0) {
					totalDiscount = Double.parseDouble(line.split(":")[1]);
				}
				if(lineNum == 12 || (lineNum-12)%14 ==0)
					totalPromotionDiscount = Double.parseDouble(line.split(":")[1]);
				if(lineNum%14 == 0)
					discountPerFile += Company.calculateDiscountValue(totalDiscount,totalPromotionDiscount,Double.parseDouble(line.split(":")[1]));
				
			}
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
		return discountPerFile;
	}

	public static double calculateDiscountValue(double discount,double promoDiscount,double totalValue) {
		double basePrice =  totalValue / (100-(discount+promoDiscount));
		return basePrice*(discount/100.00);
	}

	public static double calculateTotalIncome(String directory) {
		Path dir = Paths.get(directory);
		if(!Files.isDirectory(dir)) {
			LinkedList<String> readPriceLines = Company.readPriceLines(dir);
			return Company.splitAndParse(readPriceLines);
		}
		double priceToBePaid = 0;
		try{
			priceToBePaid = Files.list(dir)
				.filter(path->path.toString().endsWith(".txt"))
				.map(Company::readPriceLines)
				.mapToDouble(Company::splitAndParse)
				.sum();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
		return priceToBePaid;
	}
	
	public static double splitAndParse(LinkedList<String> lines) {
		return lines.stream()
				.mapToDouble(line->{
					return Double.parseDouble(line.split(":")[1]);
				})
				.sum();
	}
	
	public static LinkedList<String> readPriceLines(Path path) {
		LinkedList<String> returnResult = new LinkedList<>();
		try(BufferedReader br = new BufferedReader(new FileReader(path.toString()))){
			String line;
			int lineNum = 0;
			while((line = br.readLine()) != null) {
				lineNum++;
				if(lineNum%14 == 0)
					returnResult.add(line);
			}
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
		return returnResult;
	}
	
	public static double calculateIncomeOfDowntownRides(String directory) throws IOException {
		Path dir = Paths.get(directory);
		if(!Files.isDirectory(dir)) {
			return Company.readCoordinatesAndTakePricesOfDT(dir);
		}
		return Files.list(dir)
				.filter(path->path.toString().endsWith(".txt"))
				.mapToDouble(Company::readCoordinatesAndTakePricesOfDT)
				.sum();
	}
	
	public static double calculateIncomeOfOutskirtsRides(String directory) throws IOException{
		Path dir = Paths.get(directory);
		if(!Files.isDirectory(dir)) {
			return Company.readCoordinatesAndTakePricesOfOS(dir);
		}
		return Files.list(dir)
				.filter(path->path.toString().endsWith(".txt"))
				.mapToDouble(Company::readCoordinatesAndTakePricesOfOS)
				.sum();
	}
	
	public static double readCoordinatesAndTakePricesOfOS(Path path) {
		double totalPrice = 0;
		try(BufferedReader br = new BufferedReader(new FileReader(path.toString()))){
			String line;
			boolean flag = false;
			int lineNum = 0;
			while((line = br.readLine()) != null) {
				lineNum++;
				if((lineNum==7 || (lineNum-7) % 14 ==0) || (lineNum==8 || (lineNum-8) % 14 ==0))
					{
					int a = Integer.parseInt(line.split(":")[1].split(",")[0]);
					int b = Integer.parseInt(line.split(":")[1].split(",")[1]);
					if(a < 5 || a >= 15 || b < 5 || b >= 15) {
						flag = true;
					}
				}
				if(lineNum%14 == 0) {
					if(flag == true) {
						totalPrice += Double.parseDouble(line.split(":")[1]);
						flag = false;
					}
				}
			}
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
		return totalPrice;
	}
	
	public static double readCoordinatesAndTakePricesOfDT(Path path) {
		double totalPrice = 0;
		try(BufferedReader br = new BufferedReader(new FileReader(path.toString()))){
			String line;
			boolean flag1 = false;
			boolean flag2 = false;
			int lineNum = 0;
			while((line = br.readLine()) != null) {
				lineNum++;
				if((lineNum==7 || (lineNum-7) % 14 ==0))
					{
					int a = Integer.parseInt(line.split(":")[1].split(",")[0]);
					int b = Integer.parseInt(line.split(":")[1].split(",")[1]);
					if(a >= 5 && a < 15 && b >= 5 && b < 15) {
						flag1 = true;
					}
				}
				if((lineNum==8 || (lineNum-8) % 14 ==0)) {
					int a = Integer.parseInt(line.split(":")[1].split(",")[0]);
					int b = Integer.parseInt(line.split(":")[1].split(",")[1]);
					if(a >= 5 && a < 15 && b >= 5 && b < 15) {
						flag2 = true;
					}
				}
				if(lineNum%14 == 0) {
					if(flag1 == true && flag2 == true) {
						totalPrice += Double.parseDouble(line.split(":")[1]);
					}
					flag1 = false;
					flag2 = false;
				}
			}
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
		return totalPrice;
	}
	
	public static double calculateRepairLoss(String directory) throws IOException{
		Path path = Paths.get(directory);
		if(!Files.isDirectory(path)) {
			return Company.getRepairPrice(path);
		}
		return Files.list(path)
				.filter(s->s.toString().endsWith(".txt"))
				.mapToDouble(Company::getRepairPrice)
				.sum();
	}
	
	public static double getRepairPrice(Path path) {
		double repairPrice = 0;
		try(BufferedReader br = new BufferedReader(new FileReader(path.toString()))){
			System.out.println(path.toString());
			String line;
			int lineNumber = 0;
			Vehicle vehicle = null;
			String vehicleId = null;
			while((line = br.readLine()) != null) {
				lineNumber++;
				if(lineNumber==3 || (lineNumber-3)%14==0)
					vehicleId = line.split(":")[1];
				if(lineNumber==13 || (lineNumber-13)%14==0) {
					if("true".equals(line.split(":")[1])) {
						for(Vehicle v : Company.listOfAllVehicles) {
							if(v.getUniqueId().equals(vehicleId)) {
								vehicle = v;
								break;
							}
						}
						System.out.println("Broken vehicle detected:" + vehicle.getUniqueId());
						if(vehicle instanceof Car) 
							repairPrice += vehicle.getPriceOfAcq() * 0.07;
						if(vehicle instanceof Bycicle)
							repairPrice += vehicle.getPriceOfAcq() * 0.04;
						if(vehicle instanceof Scooter)
							repairPrice += vehicle.getPriceOfAcq() * 0.02;
						System.out.println("Print repair price:" + repairPrice);
					}
				}
			}
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
		return repairPrice;
	}
	
	public static double calculateTotalLossOnVehicle(Path path) {
		return 0.2 * Company.splitAndParse(Company.readPriceLines(path)) + Company.getRepairPrice(path);
	}
	public static void createDailyReportBase() throws IOException {
	    if (!Files.exists(Paths.get(Company.pathToDailyReportFolder))) {
	        Files.createDirectory(Paths.get(Company.pathToDailyReportFolder));
	    }

	    Files.list(Paths.get(Company.receiptsDir)).filter(Files::isRegularFile).forEach(filePath -> {
	        try {
	            List<String> allLines = Files.readAllLines(filePath);

	            for (int i = 0; i < allLines.size(); i += 14) {
	                String dateLine = allLines.get(i).substring(13);
	                
	                
	                String pathToFileInString = Paths.get(Company.pathToDailyReportFolder, dateLine + ".txt").toString();
	                
	                List<String> linesForDate = allLines.subList(i, Math.min(allLines.size(), i + 14));

	                Files.write(Paths.get(pathToFileInString), linesForDate, StandardOpenOption.CREATE, StandardOpenOption.APPEND);

	            }

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    });
	}

	
	public static List<Map<Vehicle,Double>> findTheWorstVehicleForEachType() throws IOException {
		Map<String,Double> mappedReceiptFiles = new HashMap<>();
		mappedReceiptFiles = Files.list(Paths.get(Company.receiptsDir))
			    .collect(Collectors.toMap(
			        Company::getVehicleId,  
			        Company::calculateTotalLossOnVehicle,
			        (existing, replacement) -> existing  
			    ));
		List<Map<String,Double>> totalLosses =  mappedReceiptFiles.entrySet().stream()
	            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))  
	            .map(entry -> {
	                Map<String, Double> singleEntryMap = new LinkedHashMap<>();
	                singleEntryMap.put(entry.getKey(), entry.getValue());
	                return singleEntryMap;
	            })
	            .collect(Collectors.toList());
		List<Map<Vehicle, Double>> mostExpensiveVehicles = totalLosses.stream()
	            .collect(Collectors.groupingBy(
	                vehicle -> vehicle.keySet().iterator().next().charAt(0),
	                Collectors.maxBy(Comparator.comparing(vehicle -> vehicle.values().iterator().next()))
	            ))
	            .values()
	            .stream()
	            .flatMap(Optional::stream)
	            .map(vehicleMap -> {
	                String vehicleId = vehicleMap.keySet().iterator().next().split("\\.")[0];
	                Vehicle vehicle = Company.findInVehicles(vehicleId);
	                Double price = vehicleMap.get(vehicleMap.keySet().iterator().next());	          
	                System.out.println(price);
	                return Map.of(vehicle, price);
	            })
	            .collect(Collectors.toList());
		try{
			System.out.println(Company.pathToSerFolder);
			if(Files.exists(Paths.get(Company.pathToSerFolder)))
			{
				try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(Company.pathToSerFolder))) {
	                for (Path entry : stream) {
	                    Files.delete(entry); 
	                }
	            }
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		for(var element: mostExpensiveVehicles) {
		Vehicle v = element.entrySet().stream().findFirst().orElseThrow().getKey();
		double value = element.entrySet().stream().findFirst().orElseThrow().getValue();
		if(!Files.exists(Paths.get(Company.pathToSerFolder)))
			Files.createDirectory(Paths.get(Company.pathToSerFolder));
			try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Company.pathToSerFolder + File.separator + v.getUniqueId() + ".ser",true))){
				oos.writeObject(v);
				oos.writeDouble(value);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return mostExpensiveVehicles;
	}
	
	
	private static Vehicle findInVehicles(String vehicleId) {
		return Company.listOfAllVehicles.stream().filter(vehicle->vehicle.getUniqueId().equals(vehicleId)).findFirst().orElseThrow();
	}

	private static String getVehicleId(Path v) {
		return v.getFileName().toString();
	}
	
	public void rentVehicles(int counter) {
		while(counter!=0) {
			// EXTRACT VEHICLE DATA FROM RENT STRING
			Rent rent;
			synchronized(Company.rents) {
				rent = Company.rents.pop();
			}
			
			try {
				rent.getRentedVehicle().semaphore.acquire();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			if (rent.getRentedVehicle() instanceof Car) {
				if (rent.getUserId().getIsDomestic())
					System.out.println("User is domestic citizen. Id: " + rent.getUserId().getIsDomestic());
				else 
					System.out.println("User is foreigner. Passport: " + rent.getUserId().getPassport());
				System.out.println("Driving licence: " + rent.getUserId().getDrivingLicence());
			}
			rent.start();
			rent.getRentedVehicle().semaphore.release();
			counter--;
		}
	}

	public static void readWorstVehicles(String pathToSerFolder,List<Map<Vehicle,Double>> list) throws IOException {
		Files.list(Paths.get(Company.pathToSerFolder)).forEach(file->{
			try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file.toString()))){
				Vehicle vehicle = (Vehicle)ois.readObject();
				Double cost = (Double)ois.readDouble();
				Map<Vehicle, Double> map = new HashMap<>();
		        map.put(vehicle, cost);
		        list.add(map);
			}catch(Exception e) {
				e.printStackTrace();
			}
		});
	}

	public static void vehiclesTableView(VehiclesSceneController vsc, ScooterSceneController ssc,
			BikeSceneController bsc) {
		for(Vehicle v : Company.listOfAllVehicles) {
			if(v instanceof Car) {
				Platform.runLater(()->vsc.addCarModel(v));
			}
			if(v instanceof Bycicle) {
				Platform.runLater(()->bsc.addBikeModel(v));
			}
			if(v instanceof Scooter) {
				Platform.runLater(()->ssc.addScooterModel(v));
			}
		}
		
	}

}