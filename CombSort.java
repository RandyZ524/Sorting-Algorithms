public abstract class CombSort extends SortingAlgorithm {
	public static void reset() {
		currentIndex = 0;
		upperBound = DataGraph.DataGraph().getBackingArray().length;
		swapGap = (int) Math.round((double) upperBound / 1.3);
		swapPerformed = false;
	}
	
	public static boolean iterateSort() {
		if (currentIndex + swapGap == upperBound) {
			if (swapGap != 1) {
				currentIndex = 0;
				swapGap--;
				swapPerformed = false;
			} else {
				if (swapPerformed) {
					currentIndex = 0;
					swapPerformed = false;
				} else {
					return true;
				}
			}
		}
		
		if (DataGraph.DataGraph().compare(currentIndex, currentIndex + swapGap)) {
			DataGraph.DataGraph().swap(currentIndex, currentIndex + swapGap, false);
			swapPerformed = true;
		}
		
		currentIndex++;
		return false;
	}
}