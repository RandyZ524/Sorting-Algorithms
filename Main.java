import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
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
	public static int trialNum = 0;
	public static int alg = 0;
	public static int size = 0;
	public static boolean nextArray = false;
	public static String currentAlgorithm;
	public static int[][][][] sizeAlgOpTri = new int[5][5][3][100];
	
	public static Random randVar = new Random();
	public static AnimationTimer timer = null;
	
	public static ArrayList<Integer> multiplesOf700 = new ArrayList<Integer>();
	
	public void start(Stage primaryStage) throws Exception {
		DataArrayGeneration.setStartSorted(false);
		DataArrayGeneration.setRandomInitialSwaps(10);
		DataArrayGeneration.generateArray(10);
		
		Group root = new Group(DataGraph.getNodes());
		Scene scene = new Scene(root, stageWidth, stageHeight);
		
		DataGraph.DataGraph().setBackingArray(DataArrayGeneration.getGeneratedArray());
		
		for (int i = 0; i < DataGraph.DataGraph().getBarArray().length; i++) {
			root.getChildren().add(DataGraph.DataGraph().getBarArray()[i].getBarRect());
		}
		
		multiplesOf700.addAll(Arrays.asList(10, 25, 70, 175, 700));
		
		Button startButton = new Button("Start");
		startButton.setLayoutX((stageWidth / 2) + 5);
		startButton.setLayoutY(stageHeight - ((DataGraph.yPadding + 25) / 2));
		root.getChildren().add(startButton);
		
		startButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				if (startButton.getText().equals("Start")) {
					if (!DataGraph.DataGraph().getIsSorting()) {
						//currentAlgorithm = DataGraph.algorithmSelect.getValue();
						currentAlgorithm = Algorithm.algorithmToString(Algorithm.values()[alg]);
						SortingAlgorithm.reset(currentAlgorithm);
						DataGraph.DataGraph().setCurrentAlgorithm(currentAlgorithm);
						DataGraph.algorithmSelect.getSelectionModel().select(alg);
					}
					
					startButton.setText("Stop");
					timer.start();
				} else if (startButton.getText().equals("Stop")) {
					startButton.setText("Start");
					timer.stop();
				} else if (startButton.getText().equals("Reset")) {
					sizeAlgOpTri[size][alg][0][trialNum] = DataGraph.DataGraph().getNumOfAccesses();
					sizeAlgOpTri[size][alg][1][trialNum] = DataGraph.DataGraph().getNumOfComparisons();
					sizeAlgOpTri[size][alg][2][trialNum] = DataGraph.DataGraph().getNumOfSwaps();
					
					trialNum++;
					
					if (trialNum == 100) {
						trialNum = 0;
						alg++;
					}
					
					if (alg == 5) {
						alg = 0;
						size++;
					}
					
					if (size == 5) {
						try(PrintWriter writer = new PrintWriter("data_" + sizeAlgOpTri[0][0][0].length + "-trials.txt", "UTF-8")) {
							for (int i = 0; i < 5; i++) {
								writer.println("Size: " + multiplesOf700.get(i));
								
								for (int j = 0; j < 5; j++) {
									writer.println("\tAlgorithm: " + Algorithm.algorithmToString(Algorithm.values()[j]));
									
									for (int k = 0; k < 3; k++) {
										String temp = "";
										
										switch (k) {
											case 0:
												temp = "Accesses:";
												break;
											case 1:
												temp = "Comparisons:";
												break;
											case 2:
												temp = "Swaps:";
												break;
										}
										
										writer.println("\t\t" + temp);
										int sum = 0;
										
										for (int l = 0; l < 100; l++) {
											writer.println("\t\t\t" + (l + 1) + ": " + sizeAlgOpTri[i][j][k][l]);
											sum += sizeAlgOpTri[i][j][k][l];
										}
										
										writer.println("\t\t\tAverage: " + ((double) sum / 100));
									}
								}
							}
						} catch (IOException ioe) {
						}
						
						System.exit(0);
					}
					
					DataGraph.trialsLabel.setText("Trials: " + (trialNum + 1));
					
					for (DataGraph.Bar b : DataGraph.DataGraph().getBarArray()) {
						root.getChildren().remove(b.getBarRect());
					}
					
					String ris = DataGraph.DataGraph().optionsDisplay.getRandomInitialSwapsBox().getText();
					
					if (ris.isEmpty()) {
						ris = "0";
					}
					
					DataArrayGeneration.setStartSorted(DataGraph.DataGraph().optionsDisplay.getStartSortedCheck().isSelected());
					DataArrayGeneration.setRandomInitialSwaps(Integer.valueOf(ris));
					//DataArrayGeneration.generateArray(multiplesOf700.get(randVar.nextInt(multiplesOf700.size())));
					//DataArrayGeneration.generateArray(Integer.valueOf(DataGraph.DataGraph().optionsDisplay.getSizeBox().getText()));
					DataArrayGeneration.generateArray(multiplesOf700.get(size));
					DataGraph.DataGraph().setBackingArray(DataArrayGeneration.getGeneratedArray());
					DataGraph.DataGraph().optionsDisplay.getSizeBox().setText(String.valueOf(multiplesOf700.get(size)));
					
					for (DataGraph.Bar b : DataGraph.DataGraph().getBarArray()) {
						root.getChildren().add(b.getBarRect());
					}
					
					setNodesVisible(true, DataGraph.swappedInTick.getNodes());
					startButton.setText("Start");
				}
			}
		});
		
		timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				final Algorithm currentAlg = DataGraph.DataGraph().getCurrentAlgorithm();
				boolean finished = false;
				
				double asymptoticBehavior = 0;
				int dataLength = DataGraph.DataGraph().getBackingArray().length;
				
				switch (currentAlg.getTimeComplexity()) {
					case "n^2":
						asymptoticBehavior = Math.pow(dataLength, 2);
						break;
					case "nlogn":
						asymptoticBehavior = dataLength * Math.log(dataLength);
						break;
					default:
						asymptoticBehavior = 750;
						break;
				}
				
				//for (int i = 0; i < asymptoticBehavior / /*750*/50; i++) {
				while (true) {
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
						case COMB_SORT:
							finished = CombSort.iterateSort();
							break;
						case MERGE_SORT:
							finished = MergeSort.iterateSort();
							break;
						default:
							break;
					}
					
					if (finished) {
						startButton.setText("Reset");
						nextArray = true;
						timer.stop();
						break;
					}
					
					finished = true;
				}
				
				if (DataGraph.lastSwapped[0] != -1) {
					DataGraph.swappedInTick.updateLines();
					DataGraph.lastSwapped[0] = -1;
					DataGraph.lastSwapped[1] = -1;
				}
				
				if (nextArray) {
					nextArray = false;
					setNodesVisible(false, DataGraph.swappedInTick.getNodes());
					
					//Platform.runLater(() -> {
						/*try {
							Thread.sleep(1000);
						} catch (InterruptedException ie) {
						}*/
						
						for (int j = 0; j < 2; j++) {
							Event.fireEvent(startButton, new ActionEvent());
						}
					//});
				}
			}
		};
		
		primaryStage.setTitle("Extended Sorting");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void setNodesVisible(boolean visible, Node... nodes) {
		for (Node n : nodes) {
			n.setVisible(visible);
		}
	}
}