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
		} else if (searchingIndex == 0 || DataGraph.DataGraph().getBackingArray()[searchingIndex] >
										  DataGraph.DataGraph().getBackingArray()[searchingIndex - 1]) {
			searching = false;
			DataGraph.DataGraph().setNumOfAccesses(DataGraph.DataGraph().getNumOfAccesses() + 2);
			DataGraph.accessesLabel.setText("Accesses: " + DataGraph.DataGraph().getNumOfAccesses());
		} else {
			DataGraph.DataGraph().swap(searchingIndex, searchingIndex - 1);
			searchingIndex--;
		}
		
		return false;
	}
}