import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;

public class DataGraph {
	private int barWidth;
	private int maxBarHeight;
	private int numOfAccesses;
	private int numOfSwaps;
	private boolean isSorting;
	
	private int[] backingArray;
	private Bar[] barArray;
	
	private Algorithm currentAlgorithm;
	
	static int xPadding;
	static int yPadding;
	static Label sizeLabel;
	static Label accessesLabel;
	static Label swapsLabel;
	static Rectangle graphBorder;
	
	private static DataGraph singleton;
	
	static {
		xPadding = 150;
		yPadding = 100;
		
		sizeLabel = new Label("Size: ");
		sizeLabel.setLayoutX(5);
		sizeLabel.setLayoutY(3);
		sizeLabel.setFont(Font.font(16));
		accessesLabel = new Label("Accesses: ");
		accessesLabel.setLayoutX(5);
		accessesLabel.setLayoutY(18);
		accessesLabel.setFont(Font.font(16));
		swapsLabel = new Label("Swaps: ");
		swapsLabel.setLayoutX(5);
		swapsLabel.setLayoutY(33);
		swapsLabel.setFont(Font.font(16));
		
		graphBorder = new Rectangle(xPadding, yPadding,
									Main.stageWidth - (2 * xPadding),
									Main.stageHeight - (2 * yPadding));
		graphBorder.setStroke(Color.LIGHTGRAY);
		graphBorder.setStrokeWidth(150);
		graphBorder.setStrokeType(StrokeType.OUTSIDE);
		graphBorder.setFill(Color.TRANSPARENT);
		
		singleton = null;
	}
	
	public int getNumOfAccesses() { return numOfAccesses; }
	public void setNumOfAccesses(int noa) {
		numOfAccesses = noa;
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
		numOfSwaps = 0;
		isSorting = false;
		backingArray = ba;
		barArray = new Bar[backingArray.length];
		
		for (int i = 0; i < backingArray.length; i++) {
			barArray[i] = new Bar(backingArray[i], i);
		}
		
		sizeLabel.setText("Size: "  + backingArray.length);
		accessesLabel.setText("Accesses: 0");
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
	
	public void swap(int firstIndex, int secondIndex) {
		int tempElement = backingArray[firstIndex];
		backingArray[firstIndex] = backingArray[secondIndex];
		backingArray[secondIndex] = tempElement;
		
		Bar tempBar = barArray[firstIndex];
		barArray[firstIndex] = barArray[secondIndex];
		barArray[secondIndex] = tempBar;
		
		int maxValue = backingArray.length;
		barArray[firstIndex].getBarRect().setX(((double) firstIndex / maxValue) * (Main.stageWidth - (2 * xPadding)) + xPadding);
		barArray[secondIndex].getBarRect().setX(((double) secondIndex / maxValue) * (Main.stageWidth - (2 * xPadding)) + xPadding);
		
		numOfSwaps++;
		swapsLabel.setText("Swaps: " + numOfSwaps);
		numOfAccesses += 2;
		accessesLabel.setText("Accesses: " + numOfAccesses);
	}
	
	public static Node[] getNodes() {
		return new Node[]{graphBorder, sizeLabel, accessesLabel, swapsLabel};
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
		}
		public Rectangle getBarRect() { return barRect; }
		public void setBarRect(Rectangle br) {
			barRect = br;
		}
	}
}