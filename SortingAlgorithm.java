public abstract class SortingAlgorithm {
	public static int currentIndex;
	public static int upperBound;
	public static int lowerBound;
	public static int searchingIndex;
	public static int maxIndexInSearch;
	public static int swapGap;
	public static int totalMergeLevels;
	public static int currentMergeLevel;
	public static int lowSubListIndex;
	public static int highSubListIndex;
	public static int totalMergeCount;
	public static int currentWidth;
	public static int[] mergedSubList;
	public static boolean swapPerformed;
	public static boolean searching;
	
	public static void reset(String typeOfSort) {
		DataGraph.DataGraph().setIsSorting(true);
		
		switch (typeOfSort) {
			case "Bubble Sort":
				BubbleSort.reset();
				break;
			case "Insertion Sort":
				InsertionSort.reset();
				break;
			case "Selection Sort":
				SelectionSort.reset();
				break;
			case "Comb Sort":
				CombSort.reset();
			case "Merge Sort":
				MergeSort.reset();
			default:
				break;
		}
	}
}