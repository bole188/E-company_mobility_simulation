package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import backEnd.Company;
import backEnd.Vehicle;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScene.fxml"));
            FXMLLoader loader2 = new FXMLLoader(getClass().getResource("VehiclesScene.fxml"));
            FXMLLoader loader3 = new FXMLLoader(getClass().getResource("ScooterScene.fxml"));
            FXMLLoader loader4 = new FXMLLoader(getClass().getResource("BikeScene.fxml"));
            FXMLLoader loader5 = new FXMLLoader(getClass().getResource("BreakdownScene.fxml"));
            FXMLLoader loader6 = new FXMLLoader(getClass().getResource("DailyReportScene.fxml"));
            FXMLLoader loader7 = new FXMLLoader(getClass().getResource("SpecialScene.fxml"));
            Parent ssp = loader7.load();
            Parent drs = loader6.load();
            Parent bds = loader5.load();
            Parent bs = loader4.load();
            Parent vs = loader2.load();
            Parent ss = loader3.load();
            Parent root = loader.load();
            Scene scene7 = new Scene(ssp);
            Scene scene5 = new Scene(bds);
            Scene scene2 = new Scene(vs);
            Scene scene3 = new Scene(ss);
            Scene scene4 = new Scene(bs);
            Scene scene6 = new Scene(drs);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
            
            
            // Create a task for long-running operations
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() {
                    try {
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
                		String relativePath = parametersProps.getProperty("RENT_RECEIPTS_PATH");
                		Company.receiptsDir = Paths.get(relativePath).toAbsolutePath().toString();
                		if(Files.exists(Paths.get(Company.receiptsDir)))
                		{
                			Files.walk(Paths.get(Company.receiptsDir)).filter(path->Files.isRegularFile(path)).forEach(path->{
								try {
									Files.deleteIfExists(path);
								} catch (IOException e) {
									e.printStackTrace();
								}
							});
                		}
                    	MainSceneController controller =loader.getController();
                    	SpecialSceneController specController = loader7.getController();
                    	specController.initialize();
                    	Button specSceneButton = new Button("go to main scene");
                    	specSceneButton.setOnAction(event->{
                    		primaryStage.setScene(scene);
                    	});
                    	Platform.runLater(()->specController.buttonPane.getChildren().add(specSceneButton));
                    	Button b = new Button("goToCarScene");
                    	Button b2 = new Button("goToScootersScene");
                    	Button b4 = new Button("goToBreakdownScene");
                    	Button speedButton = new Button("increase speed");
                    	Button speedButton2 = new Button("decrease speed");
                    	speedButton.setOnAction(event->{
                    		Company.executionScale /= 10.0;
                    	});
                    	speedButton2.setOnAction(event->{
                    		Company.executionScale *= 10.0;
                    	});
                    	speedButton.setLayoutX(150);
                    	speedButton.setLayoutY(50);
                    	speedButton2.setLayoutX(150);
                    	speedButton2.setLayoutY(100);      
                    	Button specButton = new Button("read worst vehicles");
                    	specButton.setLayoutX(150);
                    	specButton.setLayoutY(150);
                    	specButton.setOnAction(event->{
                    		primaryStage.setScene(scene7);
                    		List<Map<Vehicle,Double>> list = new LinkedList<>();
                    		try {
								Company.readWorstVehicles(Company.pathToSerFolder,list);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                    		for(var element : list) {
                    			System.out.println("Writing from .ser file:");
                    			System.out.println("Id:" + element.entrySet().stream().findFirst().orElseThrow().getKey().getUniqueId() + ", price:" + element.entrySet().stream().findFirst().orElseThrow().getValue());
                    		}
                    	});
                    	b4.setOnAction(event->{
                    		primaryStage.setScene(scene5);
                    	});
                    	b4.setLayoutY(150);
                    	b2.setLayoutY(50);
                    	Button backFromCarScene = new Button("back to main scene");
                    	Button backFromBreakdownScene = new Button("back to main scene");
                    	backFromBreakdownScene.setOnAction(event->{
                    		primaryStage.setScene(scene);
                    	});
                    	backFromCarScene.setOnAction(event->{
                    		primaryStage.setScene(scene);
                    	});
                    	b2.setOnAction(event->{
                    		primaryStage.setScene(scene3);
                    	});
                    	b.setOnAction(event -> {
            	            primaryStage.setScene(scene2);
            	        });
                    	Button b3 = new Button("goToBikeScene");
                    	b3.setLayoutY(100);
                    	b3.setOnAction(event->{
                    		primaryStage.setScene(scene4);
                    	});
                    	Button b5 = new Button("goToDailyReportScene");
                    	b5.setOnAction(event->{
                    		primaryStage.setScene(scene6);
                    	});
                    	b5.setLayoutX(150);
                    	Platform.runLater(()->{
                    		controller.buttonPane.getChildren().add(b);
                    		controller.buttonPane.getChildren().add(b2);
                    		controller.buttonPane.getChildren().add(b3);
                    		controller.buttonPane.getChildren().add(b4);
                    		controller.buttonPane.getChildren().add(b5);
                    		controller.buttonPane.getChildren().add(speedButton2);
                    		controller.buttonPane.getChildren().add(speedButton);
                    		controller.buttonPane.getChildren().add(specButton);
                    	});
                    	VehiclesSceneController vsc = loader2.getController();
                    	ScooterSceneController ssc = loader3.getController();
                    	BikeSceneController bsc = loader4.getController();
                    	BreakdownSceneController bdsc = loader5.getController();
                    	DailyReportSceneController drsc = loader6.getController();
                    	
                    	Button backToMain3 = new Button("back to main scene");
                    	backToMain3.setOnAction(event->{
                    		primaryStage.setScene(scene);
                    	});
                    	drsc.buttonPane.getChildren().add(backToMain3);
                    	bdsc.buttonPane.getChildren().add(backFromBreakdownScene);
                    	Button backToMain2 = new Button("back to the main scene");
                    	backToMain2.setOnAction(event->{
                    	primaryStage.setScene(scene);
                    	});
                    	Button backToMain = new Button("back to the main scene");
                    	backToMain.setOnAction(event->{
                    		primaryStage.setScene(scene);
                    	});
                    	vsc.buttonPane.getChildren().add(backFromCarScene);
                    	ssc.buttonPane.getChildren().add(backToMain);
                    	bsc.buttonPane.getChildren().add(backToMain2);                    	
                    	ssc.initialize();
                    	vsc.initialize();                    	           
                    	bsc.initialize();
                    	bdsc.initialize();
                    	drsc.initialize();
                    	Company company = new Company();
                        Company.loadAllVehicles(vsc,ssc,bsc);
                        Company.loadAndSortRentingData();
                        Company.generateRents(controller,bdsc);
                        Company.mapRents(bdsc);
                        Company.vehiclesTableView(vsc,ssc,bsc);
                        Company.cdl = new CountDownLatch(Company.rents.size());
                        if (Files.isDirectory(Paths.get(Company.pathToDailyReportFolder))) {
                        	Files.walk(Paths.get(Company.pathToDailyReportFolder))
                            .sorted(Comparator.reverseOrder())
                            .map(Path::toFile)
                            .forEach(File::delete);
                        }
                        
                        Company.cd = Company.rents.get(0).getDate();
                        while (!Company.rents.isEmpty()) {
                            LocalTime lt = Company.rents.getFirst().getTime();
                            Company.numOfRents = Company.getAllRentsWithSameTime(lt);
                            Company.cdl = new CountDownLatch(Company.numOfRents);
                            System.out.println("Number of rents:" + Company.numOfRents + ", started at:" + lt.toString());
                            Platform.runLater(()->controller.clearPane());          
                            company.rentVehicles(Company.numOfRents);
                            try {
                                Company.cdl.await();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            try {
                            	long sleepTime = Math.round(5000*Company.executionScale);
                            	Thread.sleep(sleepTime);
                            }catch(Exception e) {
                            	e.printStackTrace();
                            }
                        }
                        Platform.runLater(()->controller.clearPane());
                        Platform.runLater(()->{
                        	try {
                        		Company.createDailyReportBase();
                            	Files.walk(Paths.get(Company.pathToDailyReportFolder)).forEach(path->{
                            		drsc.addDailyReportModel(path);
                            	});
                            	}catch(Exception e) {
                            		e.printStackTrace();
                            	}           
                        });
                        
                        Platform.runLater(()->{
                        	System.out.println("Anual report:");
                        	Company.anualReport();
                        	try {
                        		List<Map<Vehicle,Double>> list = Company.findTheWorstVehicleForEachType();           
								for(var pair : list) {
									Vehicle v = pair.entrySet().stream().findFirst().orElseThrow().getKey();
									Double d = pair.entrySet().stream().findFirst().orElseThrow().getValue();
									Platform.runLater(()->specController.addSpecVehicleModel(v, d));
									System.out.println("Vehicle id:" + pair.entrySet().stream().findFirst().orElseThrow().getKey().getUniqueId());
									System.out.println("Losses: " + pair.entrySet().stream().findFirst().orElseThrow().getValue());
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
                    });
                                 
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
                
                @Override
                protected void succeeded() {
                    // Handle post-completion tasks here if needed
                }
                
                @Override
                protected void failed() {
                    // Handle failures here
                    super.failed();
                }
            };

            // Run the task in a background thread
            new Thread(task).start();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
