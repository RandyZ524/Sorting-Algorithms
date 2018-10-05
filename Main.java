import java.util.ArrayList;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Main extends Application {
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	public static int stageWidth = 1000;
	public static int stageHeight = 600;
	public static int sortingSpeed = 0;
	public static String currentAlgorithm = "Insertion Sort";
	
	public static Random randVar = new Random();
	public static AnimationTimer timer = null;
	
	public static ArrayList<Integer> multiplesOf700 = new ArrayList<Integer>();
	
	public void start(Stage primaryStage) throws Exception {
		DataArrayGeneration.setStartSorted(false);
		DataArrayGeneration.setRandomInitialSwaps(10);
		DataArrayGeneration.generateArray(700);
		
		Group root = new Group(DataGraph.getNodes());
		Scene scene = new Scene(root, stageWidth, stageHeight);
		
		DataGraph.DataGraph().setBackingArray(DataArrayGeneration.getGeneratedArray());
		
		for (int i = 0; i < DataGraph.DataGraph().getBarArray().length; i++) {
			root.getChildren().add(DataGraph.DataGraph().getBarArray()[i].getBarRect());
		}
		
		Button startButton = new Button("Start");
		root.getChildren().add(startButton);
		
		for (int i = 10; i <= 700; i++) {
			if (700 % i == 0) {
				multiplesOf700.add(i);
			}
		}
		
		Platform.runLater(() -> {
			startButton.setLayoutX((stageWidth - startButton.getWidth()) / 2);
			startButton.setLayoutY(stageHeight - ((DataGraph.yPadding + 25) / 2));
		});
		
		startButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if (startButton.getText().equals("Start")) {
					if (!DataGraph.DataGraph().getIsSorting()) {
						currentAlgorithm = Algorithm.algorithmToString(Algorithm.values()[randVar.nextInt(3)]);
						SortingAlgorithm.reset(currentAlgorithm);
						DataGraph.DataGraph().setCurrentAlgorithm(currentAlgorithm);
					}
					
					startButton.setText("Stop");
					timer.start();
				} else if (startButton.getText().equals("Stop")) {
					startButton.setText("Start");
					timer.stop();
				} else if (startButton.getText().equals("Reset")) {
					for (DataGraph.Bar b : DataGraph.DataGraph().getBarArray()) {
						root.getChildren().remove(b.getBarRect());
					}
					
					DataArrayGeneration.generateArray(multiplesOf700.get(randVar.nextInt(multiplesOf700.size())));
					DataGraph.DataGraph().setBackingArray(DataArrayGeneration.getGeneratedArray());
					
					for (DataGraph.Bar b : DataGraph.DataGraph().getBarArray()) {
						root.getChildren().add(b.getBarRect());
					}
					
					sortingSpeed = 1;
					startButton.setText("Start");
				}
			}
		});
		
		timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				final Algorithm currentAlg = DataGraph.DataGraph().getCurrentAlgorithm();
				boolean finished = false;
				
				for (int i = 0; i < 2 * Math.pow(DataGraph.DataGraph().getBackingArray().length , 2) / 1500; i++) {
					switch (currentAlg) {
						case BUBBLE_SORT:
							finished = BubbleSort.iterateSort();
							break;
						case INSERTION_SORT:
							finished = InsertionSort.iterateSort();
							break;
						case SELECTION_SORT:
							finished = SelectionSort.iterateSort();
							break;
						default:
							break;
					}
					
					if (finished) {
						startButton.setText("Reset");
						
						Platform.runLater(() -> {
							reset(root);
						});
						
						timer.stop();
						break;
					}
					
					finished = true;
				}
				
				sortingSpeed += 2;
			}
		};
		
		primaryStage.setTitle("Extended Sorting");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	private void start() {
		if (!DataGraph.DataGraph().getIsSorting()) {
			currentAlgorithm = Algorithm.algorithmToString(Algorithm.values()[randVar.nextInt(3)]);
			SortingAlgorithm.reset(currentAlgorithm);
			DataGraph.DataGraph().setCurrentAlgorithm(currentAlgorithm);
		}
		
		//startButton.setText("Stop");
		timer.start();
	}
	
	private void reset(Group root) {
		for (DataGraph.Bar b : DataGraph.DataGraph().getBarArray()) {
			root.getChildren().remove(b.getBarRect());
		}
		
		DataArrayGeneration.generateArray(multiplesOf700.get(randVar.nextInt(multiplesOf700.size())));
		DataGraph.DataGraph().setBackingArray(DataArrayGeneration.getGeneratedArray());
		
		for (DataGraph.Bar b : DataGraph.DataGraph().getBarArray()) {
			root.getChildren().add(b.getBarRect());
		}
		
		sortingSpeed = 1;
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException ie) {
		}
		
		Platform.runLater(() -> {
			start();
		});
	}
}