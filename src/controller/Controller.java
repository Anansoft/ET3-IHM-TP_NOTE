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
	
	@FXML private AnchorPane anchor;
	@FXML private ColorPicker colorSelect;
	@FXML private RadioButton buttonSelect;
	@FXML private RadioButton buttonEllipse;
	@FXML private RadioButton buttonRect;
	@FXML private RadioButton buttonLine;
	@FXML private Button buttonDel;
	@FXML private Button buttonClone;
	@FXML private Pane pane;

	private ToggleGroup group = new ToggleGroup();
	private javafx.scene.paint.Color selectedColor;
	private String wantedShape;
	private Shape selectedShape;
	private double orgSceneX;
	private double orgSceneY;
	private double orgTranslateX;
	private double orgTranslateY;
	private int clickCount = 0;
	private double x1=0, x2=0, y1=0, y2=0;
	//1private ArrayList<Shape> selectionModel = new ArrayList<Shape>();
	
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
	
	public Controller(){

	}
	
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

	@FXML
	public void buttonDelCommand() {
		pane.getChildren().remove(selectedShape);
		disableButton(true);
		/*
		for(Node n:selectionModel) {
			pane.getChildren().remove(n);
		}
		*/
	}

	@FXML
	public void buttonSelectCommand() {
		wantedShape = null;
		//buttonDel.setDisable(false);
	}
	
	@FXML
	public void buttonRecCommand(){
		wantedShape = "Rectangle";
		disableButton(true);
	}
	
	public void buttonEllCommand() {
		wantedShape = "Ellipse";
		disableButton(true);
		//buttonDel.setDisable(false);
	}
	
	public void buttonLineCommand() {
		wantedShape = "Line";
		disableButton(true);
		//buttonDel.setDisable(false);
	}
	
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
		       
		        /*
		        if(wantedShape == "Rectangle") {
		        	updateColor();
		    		//mouse = MouseInfo.getPointerInfo().getLocation();
		    		drawRectangle(click.getX(), click.getY());
		    		/*
		    		Rectangle rect = new Rectangle(50,30, selectedColor);
		    		rect.relocate(click.getX(), click.getY());
		    		rect.setOnMousePressed(shapeMousePressedEventHandler);
		    		rect.setOnMouseDragged(shapeOnMouseDraggedEventHandler);
		    		pane.getChildren().add(rect);
		        }
		        */
		    }
		});
	}
	
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
	
	public void updateColor(){
		selectedColor = colorSelect.getValue();
	}
	
	public void drawRectangle(double x, double y){
		updateColor();
		//mouse = MouseInfo.getPointerInfo().getLocation();
		Rectangle rect = new Rectangle(50,30, selectedColor);
		rect.relocate(x,y);
		rect.setOnMousePressed(shapeMousePressedEventHandler);
		rect.setOnMouseDragged(shapeOnMouseDraggedEventHandler);
		pane.getChildren().add(rect);
	}
	
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
	
	public void disableButton(boolean bool) {
		buttonDel.setDisable(bool);
    	buttonClone.setDisable(bool);
	}
	
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
