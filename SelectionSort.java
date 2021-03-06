public abstract class SelectionSort extends SortingAlgorithm {
	public static void reset() {
		currentIndex = -1;
		maxIndexInSearch = -1;
		searching = false;
	}
	
	public static boolean iterateSort() {
		if (!searching) {
			currentIndex++;
			searchingIndex = currentIndex;
			maxIndexInSearch = -1;
			searching = true;
		}
		
		if (currentIndex == DataGraph.DataGraph().getBackingArray().length) {
			return true;
		} else if (searchingIndex == DataGraph.DataGraph().getBackingArray().length) {
			DataGraph.DataGraph().swap(currentIndex, maxIndexInSearch, true);
			searching = false;
		} else {
			if (maxIndexInSearch == -1 || !DataGraph.DataGraph().compare(searchingIndex, maxIndexInSearch)) {
				maxIndexInSearch = searchingIndex;
			}
			
			searchingIndex++;
		}
		
		return false;
	}
}