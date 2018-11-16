public abstract class InsertionSort extends SortingAlgorithm {
	public static void reset() {
		currentIndex = 0;
		maxIndexInSearch = -1;
		searching = false;
	}
	
	public static boolean iterateSort() {
		if (!searching) {
			currentIndex++;
			searchingIndex = currentIndex;
			searching = true;
		}
		
		if (currentIndex == DataGraph.DataGraph().getBackingArray().length) {
			return true;
		} else {
			if (searchingIndex == 0 || DataGraph.DataGraph().compare(searchingIndex, searchingIndex - 1)) {
				searching = false;
			} else {
				DataGraph.DataGraph().swap(searchingIndex, searchingIndex - 1, true);
				searchingIndex--;
			}
		}
		
		return false;
	}
}