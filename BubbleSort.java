public abstract class BubbleSort extends SortingAlgorithm {
	public static void reset() {
		currentIndex = 0;
		upperBound = DataGraph.DataGraph().getBackingArray().length - 1;
		swapPerformed = false;
	}
	
	public static boolean iterateSort() {
		if (currentIndex == upperBound) {
			if (swapPerformed) {
				currentIndex = 0;
				upperBound--;
				swapPerformed = false;
			} else {
				return true;
			}
		}
		
		if (DataGraph.DataGraph().getBackingArray()[currentIndex] > DataGraph.DataGraph().getBackingArray()[currentIndex + 1]) {
			DataGraph.DataGraph().swap(currentIndex, currentIndex + 1);
			swapPerformed = true;
		}
		
		currentIndex++;
		return false;
	}
}