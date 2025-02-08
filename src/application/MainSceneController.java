package application;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class MainSceneController {
	@FXML
	public GridPane gridPane;
	
	@FXML
	public Pane startingPane;
	
	@FXML
	public Pane buttonPane;
	
	@FXML
	public Button goToCarScene;
	
	@FXML
	public Button goToScootersScene;
	
	@FXML
	public Button goToBikeScene;
	
	
	public GridPane getGridPane() {
		return gridPane;
	}
	
	public Button getCarSceneButton() {
		return goToCarScene;
	}
	
	public void addLabel(String id,int bl, int row,int column) {
		for (Node node : gridPane.getChildren()) {
	        Integer nodeRow = GridPane.getRowIndex(node);
	        Integer nodeColumn = GridPane.getColumnIndex(node);

	        // Handle null values for positions (sometimes they can be null if not set explicitly)
	        if (nodeRow == null) nodeRow = 0;
	        if (nodeColumn == null) nodeColumn = 0;
	        System.out.println("Node: " + node + " Row: " + nodeRow + " Column: " + nodeColumn);

	        if (nodeRow == row && nodeColumn == column && node instanceof Label) {
	            // If a label exists in the same position, append the id to its existing text
	            Label existingLabel = (Label) node;
	            existingLabel.setText(existingLabel.getText() + "," + id + ":"  + bl);
	            return; // No need to add a new label, just update and return
	        }
	    }
		Label label = new Label(id + ":"  + bl);
		if(id.startsWith("A"))
			label.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
		if(id.startsWith("T"))
			label.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
		if(id.startsWith("B"))
			label.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
		GridPane.setRowIndex(label, row);
		GridPane.setColumnIndex(label, column);
		gridPane.getChildren().add(label);
	}
	
	
	
	public void moveLabel(String id,int bl, int endRow, int endColumn) {
	    Label label = (Label) this.findLabelInPane(id, gridPane);
	    
	    Label secondLabel = null;
	    if (label == null) {
	        System.out.println("Label not found.");
	        return;
	    }
	    Integer currentRow = GridPane.getRowIndex(label);
	    Integer currentColumn = GridPane.getColumnIndex(label);

	    System.out.println("Current Position: (" + currentRow + ", " + currentColumn + 
	                        "), Target Position: (" + endRow + ", " + endColumn + ")");

	    if (!currentRow.equals(endRow)) {
	    	if(this.checkIfAnythingElseInLabel(label,id,bl) == true)
	    	{
	    		System.out.println("--------------------------------------------");
	    		secondLabel = new Label(id+":" + bl);
	    		System.out.println("Label text before:" + label.getText());
	    		String textToBeAdded;
	    		String[] list = label.getText().split(",");
	    		textToBeAdded = Stream.of(Arrays.stream(list).filter(string->string.charAt(0)!=id.charAt(0)).toArray(String[]::new)).collect(Collectors.joining());
	    		label.setText(textToBeAdded);
	    		
	    		System.out.println("Label text after:" + label.getText());
	    	}
	    	else {
	    		label.setText(id+":" + bl);
	    		gridPane.getChildren().remove(label);
	    	}
	        currentRow += (currentRow > endRow) ? -1 : 1;
	    }
	    
	    else if (!currentColumn.equals(endColumn)) {
	    	if(this.checkIfAnythingElseInLabel(label,id,bl) == true)
	    	{
	    		System.out.println("--------------------------------------------");
	    		secondLabel = new Label(id+":" + bl);
	    		System.out.println("Label text before:" + label.getText());
	    		String textToBeAdded;
	    		String[] list = label.getText().split(",");
	    		textToBeAdded = Stream.of(Arrays.stream(list).filter(string->string.charAt(0)!=id.charAt(0)).toArray(String[]::new)).collect(Collectors.joining());
	    		label.setText(textToBeAdded);
	    	}
	    	else {
	    		label.setText(id+":" + bl);
	    		gridPane.getChildren().remove(label);
	    	}
	        currentColumn += (currentColumn > endColumn) ? -1 : 1;
	    }
	    Label existingLabel = this.findLabelAtPosition(currentRow, currentColumn);
	    
	    if (existingLabel != null) {
	        // If a label exists at the target position, update its text
	        existingLabel.setText(existingLabel.getText() + "," + id+":" + bl);
	        if(existingLabel.getText().contains(","))
	        	existingLabel.setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
	        System.out.println("Updated existing label at (" + endRow + ", " + endColumn + ")");
	    }
	    else {
	    	if(secondLabel != null) {
	    		if(secondLabel.getText().startsWith("A"))
	    			secondLabel.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
	    		if(secondLabel.getText().startsWith("T"))
	    			secondLabel.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
	    		if(secondLabel.getText().startsWith("B"))
	    			secondLabel.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
	    		if(secondLabel.getText().contains(","))
	    			secondLabel.setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
	    		GridPane.setRowIndex(secondLabel, currentRow);
		        GridPane.setColumnIndex(secondLabel, currentColumn);
		        gridPane.getChildren().add(secondLabel);
	    	}
	    	else {
	    		
	    		GridPane.setRowIndex(label, currentRow);
		        GridPane.setColumnIndex(label, currentColumn);
		        gridPane.getChildren().add(label);
	    	}
	    }
	    if(label.getText().startsWith("A"))
			label.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
		if(label.getText().startsWith("T"))
			label.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
		if(label.getText().startsWith("B"))
			label.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
		if(label.getText().contains(","))
			label.setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
	}
	private Label findLabelAtPosition(int row, int column) {
        for (Node node : gridPane.getChildren()) {
            Integer nodeRow = GridPane.getRowIndex(node);
            Integer nodeColumn = GridPane.getColumnIndex(node);
            
            // Handle null values (nodes with unspecified row/column default to 0)
            if (nodeRow == null) nodeRow = 0;
            if (nodeColumn == null) nodeColumn = 0;

            if (nodeRow == row && nodeColumn == column && node instanceof Label) {
                return (Label) node; // Return the label at the target position
            }
        }
        return null; // No label found at the target position
    }
	
	private boolean checkIfAnythingElseInLabel(Label l, String id,int bl) {
	    System.out.println("Label text from method: " + l.getText());
	    System.out.println("Checking with: " + id + ":" + bl);
	 	    return l.getText().contains(",");
	}
	
	private Node findLabelInPane(String id, GridPane gridPane) {
	    for (Node node : gridPane.getChildren()) {
	        if (node instanceof Label && ((Label) node).getText().contains(id)) {
	            return node;
	        }
	    }
	    System.out.println("Label with ID " + id + " not found.");
	    return null;
	}
	


	public void clearPane() {
		gridPane.getChildren().clear();
		
	}

}
