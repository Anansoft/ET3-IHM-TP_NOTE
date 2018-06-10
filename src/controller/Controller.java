package controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Controller extends VBox{
	
	@FXML private AnchorPane anchor; //Main anchor Pane
	@FXML private ColorPicker colorSelect; //ColorPicker for the color of the shape
	@FXML private RadioButton buttonSelect; //Radio Button for selecting shapes
	@FXML private RadioButton buttonEllipse; //Radio Button to draw an Ellipse
	@FXML private RadioButton buttonRect; //Radio Button to draw a Rectangle
	@FXML private RadioButton buttonLine; //Radio Button to draw a Line
	@FXML private Button buttonDel; //Button to delete the selected shape
	@FXML private Button buttonClone; //Button to clone the selected shape
	@FXML private Pane pane; //Pane where the shapes are drawn

	private ToggleGroup group = new ToggleGroup(); //alows 
	private javafx.scene.paint.Color selectedColor; //the color selected for drawing the shape
	private String wantedShape; //string of the shape to be drawn
	private Shape selectedShape; //the shape to be selected
	private double orgSceneX; //X coordinate of the selected shape
	private double orgSceneY; //Y coordinate of the selected shape
	private double orgTranslateX; //X for the translation
	private double orgTranslateY; //Y for the translation
	private int clickCount = 0; //number of lick for drawing lines
	private double x1=0, x2=0, y1=0, y2=0; //coordinates for the drawing of lines
	
	/**
	 * EventHandler to select shape
	 */
	EventHandler<MouseEvent> shapeMousePressedEventHandler = new EventHandler<MouseEvent>() {
	 
	        @Override
	        public void handle(MouseEvent select) {
	        	if(buttonSelect.isSelected()) {
		        	selectedShape = (Shape) select.getSource();
		        	disableButton(false);
		        	orgSceneX = select.getSceneX();
		            orgSceneY = select.getSceneY();
		            orgTranslateX = ((Shape) select.getSource()).getTranslateX();
		            orgTranslateY = ((Shape) select.getSource()).getTranslateY();
	        	}
	        }
	    };
	    
	/**
	 * Eventhandler for click and drag
	 */
    EventHandler<MouseEvent> shapeOnMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
	     
            @Override
            public void handle(MouseEvent drag) {
            	if(buttonSelect.isSelected()) {
            		double offsetX = drag.getSceneX() - orgSceneX;
	                double offsetY = drag.getSceneY() - orgSceneY;
	                double newTranslateX = orgTranslateX + offsetX;
	                double newTranslateY = orgTranslateY + offsetY;
	                
	                ((Shape)(drag.getSource())).setTranslateX(newTranslateX);
	                ((Shape)(drag.getSource())).setTranslateY(newTranslateY);
            	}
            }
        };
	
    /**
     * Constructor of the class
     */
	public Controller(){

	}
	
	/**
	 * Controller for the clone button
	 */
	@FXML
	public void buttonCloneCommand() {
		if(selectedShape.getClass() == Rectangle.class) {
			drawRectangle(selectedShape.getLayoutX()+10, selectedShape.getLayoutY()+10);
		}
		else if(selectedShape.getClass() == Ellipse.class) {
			drawEllipse(selectedShape.getLayoutX()+1, selectedShape.getLayoutY()+1);
		}
		else if (selectedShape.getClass() == Line.class) {
			drawLine(((Line) selectedShape).getStartX()+ 20, ((Line) selectedShape).getStartY() + 20, ((Line) selectedShape).getEndX()+20, ((Line) selectedShape).getEndY()+20);
		}
	}

	/**
	 * Controller for the delete button
	 */
	@FXML
	public void buttonDelCommand() {
		pane.getChildren().remove(selectedShape);
		disableButton(true);
	}

	/**
	 * Controller for the select button
	 */
	@FXML
	public void buttonSelectCommand() {
		wantedShape = null;
		//buttonDel.setDisable(false);
	}
	
	/**
	 * Controller for the rectangle radio button
	 */
	@FXML
	public void buttonRecCommand(){
		wantedShape = "Rectangle";
		disableButton(true);
	}
	
	/**
	 * Controller for the ellipse radio button
	 */
	public void buttonEllCommand() {
		wantedShape = "Ellipse";
		disableButton(true);
		//buttonDel.setDisable(false);
	}
	
	/**
	 * Controller for the line radio button
	 */
	public void buttonLineCommand() {
		wantedShape = "Line";
		disableButton(true);
		//buttonDel.setDisable(false);
	}
	
	/**
	 * Controller for the pane
	 */
	@FXML
	public void paneCommand(){
		pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent click) {
		        if(wantedShape != null) {
		        	 switch(wantedShape) {
			        	case "Rectangle":
			        		//mouse = MouseInfo.getPointerInfo().getLocation();
			        		drawRectangle(click.getX(), click.getY());
			        		break;
			        	case "Ellipse":
			        		drawEllipse(click.getX(), click.getY());
			        		break;
			        	case "Line":
			        		if((clickCount==0)) {
			        			x1 = click.getX(); y1 = click.getY();
			        			clickCount = 1;
			        		}
			        		else{
			        			x2 = click.getX(); y2 = click.getY();
			        			clickCount = 0;
			        			drawLine(x1, y1, x2, y2);
			        		}
			        		break;
			        	default : 
			        		break;
			        }
		        }
		    }
		});
	}
	
	/**
	 * Controller for the color picker 
	 */
	public void colorSelectCommand() {
		if(buttonSelect.isSelected()) {
			if(selectedShape != null) {
				updateColor();
				if(selectedShape.getClass() != Line.class) {
					selectedShape.setFill(selectedColor);
				}
				else {
					selectedShape.setStroke(selectedColor);
				}
			}
			disableButton(true);
		}
	}
	
	/**
	 * Update the selected colors by colorPicker
	 */
	public void updateColor(){
		selectedColor = colorSelect.getValue();
	}
	
	/**
	 * Draw rectangle
	 * @param x x coordinate of the wanted position
	 * @param y y coordinate of the wanted position
	 */
	public void drawRectangle(double x, double y){
		updateColor();
		//mouse = MouseInfo.getPointerInfo().getLocation();
		Rectangle rect = new Rectangle(50,30, selectedColor);
		rect.relocate(x,y);
		rect.setOnMousePressed(shapeMousePressedEventHandler);
		rect.setOnMouseDragged(shapeOnMouseDraggedEventHandler);
		pane.getChildren().add(rect);
	}
	
	/**
	 * Draw elllipse
	 * @param x x coordinate of the wanted position
	 * @param y y coordinate of the wanted position
	 */
	public void drawEllipse(double x, double y){
		updateColor();
		//mouse = MouseInfo.getPointerInfo().getLocation();
		Ellipse elli = new Ellipse(10, 20);
		elli.relocate(x, y);
		elli.setFill(selectedColor);
		elli.setOnMousePressed(shapeMousePressedEventHandler);
		elli.setOnMouseDragged(shapeOnMouseDraggedEventHandler);
		pane.getChildren().add(elli);
	}
	
	/**
	 * Draw line 
	 * @param x1 x coordinate of the first point
	 * @param y1 y coordinate of the first point
	 * @param x2 x coordinate of the first point
	 * @param y2 y coordinate of the first point
	 */
	public void drawLine(double x1, double y1, double x2, double y2) {
		updateColor();
		Line line = new Line();
		line.setStartX(x1); line.setStartY(y1);
		line.setEndX(x2); line.setEndY(y2);
		line.setStrokeWidth(5);
		line.setStroke(selectedColor);
		line.setOnMousePressed(shapeMousePressedEventHandler);
		line.setOnMouseDragged(shapeOnMouseDraggedEventHandler);
		pane.getChildren().add(line);
	}
	
	/**
	 * Disables all the buttons
	 * @param bool Disables if true and enable if false
	 */
	public void disableButton(boolean bool) {
		buttonDel.setDisable(bool);
    	buttonClone.setDisable(bool);
	}
	
	/**
	 * Intializing function
	 * Allows to disable the right buttons
	 */
	@FXML
	public void initialize(){
		buttonSelect.setToggleGroup(group);
		buttonRect.setToggleGroup(group);
		buttonLine.setToggleGroup(group);
		buttonEllipse.setToggleGroup(group);
		
		disableButton(true);
		
		colorSelect.setValue(javafx.scene.paint.Color.BLACK);
	}
}
