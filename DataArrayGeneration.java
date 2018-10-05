import java.util.Random;
import java.util.ArrayList;

public abstract class DataArrayGeneration {
	private static int randomInitialSwaps;
	private static boolean startSorted;
	private static int[] generatedArray;
	private static Random randVar;
	
	static {
		randVar = new Random();
	}
	
	public static int getRandomInitialSwaps() { return randomInitialSwaps; }
	public static void setRandomInitialSwaps(int ris) {
		if (ris >= 0) {
			randomInitialSwaps = ris;
		}
	}
	public static int[] getGeneratedArray() { return generatedArray; }
	public static void setGeneratedArray(int[] ga) {
		
	}
	public static boolean getStartSorted() { return startSorted; }
	public static void setStartSorted(boolean ss) {
		startSorted = ss;
	}
	
	public static void generateArray(int size) {
		generatedArray = new int[size];
		
		if (startSorted) {
			for (int i = 0; i < size; i++) {
				generatedArray[i] = i + 1;
			}
			
			makeRandomInitialSwaps();
		} else {
			ArrayList<Integer> allValues = new ArrayList<Integer>();
			
			for (int i = 0; i < size; i++) {
				allValues.add(i + 1);
			}
			
			for (int i = 0; i < size; i++) {
				int randIndex = randVar.nextInt(allValues.size());
				generatedArray[i] = allValues.get(randIndex);
				allValues.remove(randIndex);
			}
		}
	}
	
	public static void makeRandomInitialSwaps() {
		int firstIndex = -1;
		int secondIndex = -1;
		
		for (int i = 0; i < randomInitialSwaps; i++) {
			firstIndex = randVar.nextInt(generatedArray.length);
			
			do {
				secondIndex = randVar.nextInt(generatedArray.length);
			} while (secondIndex == firstIndex);
			
			int tempElement = generatedArray[firstIndex];
			generatedArray[firstIndex] = generatedArray[secondIndex];
			generatedArray[secondIndex] = tempElement;
		}
	}
}