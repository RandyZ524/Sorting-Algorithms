import java.util.ArrayList;
import java.util.Arrays;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;

public class DataGraph {
	private int barWidth;
	private int maxBarHeight;
	private int numOfAccesses;
	private int numOfComparisons;
	private int numOfSwaps;
	private boolean isSorting;
	
	private int[] backingArray;
	private Bar[] barArray;
	
	private Algorithm currentAlgorithm;
	
	static int xPadding;
	static int yPadding;
	static int[] lastSwapped;
	static Label sizeLabel;
	static Label accessesLabel;
	static Label comparisonsLabel;
	static Label swapsLabel;
	static Rectangle graphBorder;
	static ComboBox<String> algorithmSelect;
	static SwapBracket swappedInTick;
	static DataOptions optionsDisplay;
	
	private static DataGraph singleton;
	
	static {
		xPadding = 150;
		yPadding = 100;
		lastSwapped = new int[2];
		
		sizeLabel = new Label("Size: ");
		accessesLabel = new Label("Accesses: ");
		comparisonsLabel = new Label("Comparisons: ");
		swapsLabel = new Label("Swaps: ");
		setLabels(sizeLabel, accessesLabel, comparisonsLabel, swapsLabel);
		
		graphBorder = new Rectangle(xPadding, yPadding,
									Main.stageWidth - (2 * xPadding),
									Main.stageHeight - (2 * yPadding));
		graphBorder.setStroke(Color.LIGHTGRAY);
		graphBorder.setStrokeWidth(150);
		graphBorder.setStrokeType(StrokeType.OUTSIDE);
		graphBorder.setFill(Color.TRANSPARENT);
		
		algorithmSelect = new ComboBox<String>();
		algorithmSelect.getItems().addAll(Algorithm.stringValues());
		algorithmSelect.setPrefWidth(120);
		algorithmSelect.setLayoutX((Main.stageWidth / 2) - algorithmSelect.getPrefWidth() - 5);
		algorithmSelect.setLayoutY(Main.stageHeight - ((DataGraph.yPadding + 25) / 2));
		
		swappedInTick = new SwapBracket();
		optionsDisplay = new DataOptions();
		
		singleton = null;
	}
	
	public int getNumOfAccesses() { return numOfAccesses; }
	public void setNumOfAccesses(int noa) {
		numOfAccesses = noa;
	}
	public int getNumOfComparisons() { return numOfComparisons; }
	public void setNumOfComparisons(int noc) {
		numOfComparisons = noc;
	}
	public int getNumOfSwaps() { return numOfSwaps; }
	public void setNumOfSwaps(int nos) {
		numOfSwaps = nos;
	}
	public boolean getIsSorting() { return isSorting; }
	public void setIsSorting(boolean is) {
		isSorting = is;
	}
	public int[] getBackingArray() { return backingArray; }
	public void setBackingArray(int[] ba) {
		barWidth = (Main.stageWidth - (2 * xPadding)) / ba.length;
		maxBarHeight = Main.stageHeight - (2 * yPadding);
		numOfAccesses = 0;
		numOfComparisons = 0;
		numOfSwaps = 0;
		isSorting = false;
		backingArray = ba;
		barArray = new Bar[backingArray.length];
		
		for (int i = 0; i < backingArray.length; i++) {
			barArray[i] = new Bar(backingArray[i], i);
		}
		
		sizeLabel.setText("Size: "  + backingArray.length);
		accessesLabel.setText("Accesses: 0");
		comparisonsLabel.setText("Comparisons: 0");
		swapsLabel.setText("Swaps: 0");
	}
	public Bar[] getBarArray() { return barArray; }
	public void setBarArray(Bar[] ba) {
		barArray = ba;
	}
	public Algorithm getCurrentAlgorithm() { return currentAlgorithm; }
	public void setCurrentAlgorithm(String ca) {
		currentAlgorithm = Algorithm.stringToAlgorithm(ca);
	}
	
	public boolean compare(int firstIndex, int secondIndex) {
		numOfAccesses += 2;
		accessesLabel.setText("Accesses: " + numOfAccesses);
		numOfComparisons++;
		comparisonsLabel.setText("Comparisons: " + numOfComparisons);
		return backingArray[firstIndex] > backingArray[secondIndex];
	}
	
	public void swap(int firstIndex, int secondIndex, boolean incrementAccesses) {
		int tempElement = backingArray[firstIndex];
		backingArray[firstIndex] = backingArray[secondIndex];
		backingArray[secondIndex] = tempElement;
		
		Bar tempBar = barArray[firstIndex];
		barArray[firstIndex] = barArray[secondIndex];
		barArray[secondIndex] = tempBar;
		
		int maxValue = backingArray.length;
		barArray[firstIndex].getBarRect().setX(((double) firstIndex / maxValue) * (Main.stageWidth - (2 * xPadding)) + xPadding);
		barArray[secondIndex].getBarRect().setX(((double) secondIndex / maxValue) * (Main.stageWidth - (2 * xPadding)) + xPadding);
		
		if (incrementAccesses) {
			numOfAccesses += 2;
			accessesLabel.setText("Accesses: " + numOfAccesses);
		}
		
		numOfSwaps++;
		swapsLabel.setText("Swaps: " + numOfSwaps);
		lastSwapped[0] = firstIndex;
		lastSwapped[1] = secondIndex;
	}
	
	public void set(int index, int value) {
		backingArray[index] = value;
		
		barArray[index].setElementValue(value);
		double hue = ((double) value / backingArray.length) * 360;
		
		barArray[index].setBarShade(hue, 1.0, 1.0);
		barArray[index].getBarRect().setHeight(((double) value / backingArray.length) * maxBarHeight);
		barArray[index].getBarRect().setY(Main.stageHeight - yPadding - (((double) value / backingArray.length) * maxBarHeight));
	}
	
	public static void setLabels(Label... labels) {
		for (int i = 0; i < labels.length; i++) {
			labels[i].setLayoutX(5);
			labels[i].setLayoutY((i * 18) + 3);
			labels[i].setFont(Font.font(16));
		}
	}
	
	public static Node[] getNodes() {
		ArrayList<Node> allNodes = new ArrayList<Node>();
		allNodes.addAll(Arrays.asList(graphBorder, sizeLabel, accessesLabel, comparisonsLabel, swapsLabel, algorithmSelect));
		allNodes.addAll(Arrays.asList(swappedInTick.getNodes()));
		allNodes.addAll(Arrays.asList(optionsDisplay.getNodes()));
		
		return allNodes.toArray(new Node[allNodes.size()]);
	}
	
	public static DataGraph DataGraph() {
		if (singleton == null) {
			singleton = new DataGraph();
		}
		
		return singleton;
	}
	
	public class Bar {
		private int elementValue;
		private Color barShade;
		private Rectangle barRect;
		
		public Bar(int ev, int index) {
			int maxValue = backingArray.length;
			elementValue = ev;
			barShade = Color.hsb(((double) elementValue / maxValue) * 360, 1.0, 1.0);
			barRect = new Rectangle(barWidth, ((double) elementValue / maxValue) * maxBarHeight, barShade);
			barRect.setX(((double) index / maxValue) * (Main.stageWidth - (2 * xPadding)) + xPadding);
			barRect.setY(Main.stageHeight - yPadding - (((double) elementValue / maxValue) * maxBarHeight));
		}
		
		public int getElementValue() { return elementValue; }
		public void setElementValue(int ev) {
			if (ev >= 0) {
				elementValue = ev;
			}
		}
		public Color getBarShade() { return barShade; }
		public void setBarShade(double hue, double saturation, double brightness) {
			if (hue < 0 || hue >= 360) {
				return;
			}
			
			if (saturation < 0 || saturation > 1.0) {
				return;
			}
			
			if (brightness < 0 || brightness > 1.0) {
				return;
			}
			
			barShade = Color.hsb(hue, saturation, brightness);
			barRect.setFill(barShade);
		}
		public Rectangle getBarRect() { return barRect; }
		public void setBarRect(Rectangle br) {
			barRect = br;
		}
	}
	
	static class SwapBracket {
		private Line firstIndexLine;
		private Line secondIndexLine;
		private Line connectingLine;
		
		public SwapBracket() {
			firstIndexLine = new Line();
			firstIndexLine.setStartY(Main.stageHeight - yPadding + 10);
			firstIndexLine.setEndY(Main.stageHeight - yPadding + 20);
			
			secondIndexLine = new Line();
			secondIndexLine.setStartY(Main.stageHeight - yPadding + 10);
			secondIndexLine.setEndY(Main.stageHeight - yPadding + 20);
			
			connectingLine = new Line();
			connectingLine.setStartY(Main.stageHeight - yPadding + 20);
			connectingLine.setEndY(Main.stageHeight - yPadding + 20);
		}
		
		public Line getFirstIndexLine() { return firstIndexLine; }
		public void setFirstIndexLine(Line fil) {
			firstIndexLine = fil;
		}
		public Line getSecondIndexLine() { return secondIndexLine; }
		public void setSecondIndexLine(Line sil) {
			secondIndexLine = sil;
		}
		public Line getConnectingLine() { return connectingLine; }
		public void setConnectingLine(Line cl) {
			connectingLine = cl;
		}
		
		public void updateLines() {
			Rectangle firstRect = singleton.getBarArray()[lastSwapped[0]].getBarRect();
			firstIndexLine.setStartX(firstRect.getX() + (0.5 * firstRect.getWidth()));
			firstIndexLine.setEndX(firstIndexLine.getStartX());
			
			Rectangle secondRect = singleton.getBarArray()[lastSwapped[1]].getBarRect();
			secondIndexLine.setStartX(secondRect.getX() + (0.5 * secondRect.getWidth()));
			secondIndexLine.setEndX(secondIndexLine.getStartX());
			
			connectingLine.setStartX(firstIndexLine.getEndX());
			connectingLine.setEndX(secondIndexLine.getEndX());
		}
		
		public void setLines(Line... lines) {
			for (Line l : lines) {
				l.setStroke(Color.RED);
				l.setStrokeWidth(3);
				l.setStrokeLineCap(StrokeLineCap.ROUND);
			}
		}
		
		public Node[] getNodes() {
			return new Node[]{firstIndexLine, secondIndexLine, connectingLine};
		}
	}
	
	static class DataOptions {
		private boolean lockedListener;
		private TextField sizeBox;
		private TextField randomInitialSwapsBox;
		private CheckBox startSortedCheck;
		
		public DataOptions() {
			lockedListener = false;
			
			sizeBox = new TextField();
			sizeBox.setLayoutX(40);
			sizeBox.setLayoutY(3);
			sizeBox.setMaxWidth(60);
			sizeBox.setPromptText("Set Size");
			sizeBox.setText("700");
			
			randomInitialSwapsBox = new TextField();
			randomInitialSwapsBox.setLayoutX(5);
			randomInitialSwapsBox.setLayoutY(DataGraph.yPadding);
			randomInitialSwapsBox.setMaxWidth(DataGraph.xPadding - 10);
			randomInitialSwapsBox.setPromptText("Random Initial Swaps");
			randomInitialSwapsBox.setDisable(true);
			
			startSortedCheck = new CheckBox("Start Sorted");
			startSortedCheck.setLayoutX(5);

			Platform.runLater(() -> {
				startSortedCheck.setLayoutY(DataGraph.yPadding + randomInitialSwapsBox.getHeight() + 5);
			});
			
			sizeBox.textProperty().addListener((obs, oldValue, newValue) -> {
				if (!lockedListener && !(newValue.isEmpty() || newValue.matches("(0|[1-9]\\d*)"))) {
					lockedListener = true;
					sizeBox.setText(oldValue);
					lockedListener = false;
				}
			});
			
			randomInitialSwapsBox.textProperty().addListener((obs, oldValue, newValue) -> {
				if (!lockedListener && !(newValue.isEmpty() || newValue.matches("(0|[1-9]\\d*)"))) {
					lockedListener = true;
					randomInitialSwapsBox.setText(oldValue);
					lockedListener = false;
				}
			});
			
			startSortedCheck.selectedProperty().addListener((obs, oldValue, newValue) -> {
				randomInitialSwapsBox.setDisable(!newValue);
			});
		}
		
		public TextField getSizeBox() { return sizeBox; }
		public void setSizeBox(TextField sb) {
			sizeBox = sb;
		}
		public TextField getRandomInitialSwapsBox() { return randomInitialSwapsBox; }
		public void setRandomInitialSwapsBox(TextField risb) {
			randomInitialSwapsBox = risb;
		}
		public CheckBox getStartSortedCheck() { return startSortedCheck; }
		public void setStartSortedCheck(CheckBox ssc) {
			startSortedCheck = ssc;
		}
		
		public Node[] getNodes() {
			return new Node[]{ sizeBox, randomInitialSwapsBox, startSortedCheck };
		}
	}
}