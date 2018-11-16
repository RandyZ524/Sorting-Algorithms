public abstract class MergeSort extends SortingAlgorithm {
	public static void reset() {
		currentIndex = 0;
		upperBound = DataGraph.DataGraph().getBackingArray().length;
		totalMergeLevels = 31 - Integer.numberOfLeadingZeros(DataGraph.DataGraph().getBackingArray().length);
		currentMergeLevel = 0;
		lowSubListIndex = 0;
		highSubListIndex = 1;
		totalMergeCount = 0;
		currentWidth = 1;
		mergedSubList = new int[2];
	}
	
	public static boolean iterateSort() {
		if (currentMergeLevel == totalMergeLevels + 1) {
			return true;
		}
		
		if (currentIndex * 2 * currentWidth >= upperBound) { 
			currentIndex = 0;
			currentMergeLevel++;
			lowSubListIndex = 0;
			highSubListIndex = 2 * currentWidth;
			totalMergeCount = 0;
			currentWidth = (int) Math.pow(2, currentMergeLevel);
			mergedSubList = new int[2 * currentWidth];
		} else {
			if ((lowSubListIndex >= (currentIndex * 2 * currentWidth) + currentWidth || lowSubListIndex >= upperBound) &&
					(highSubListIndex >= (currentIndex * 2 * currentWidth) + (2 * currentWidth) || highSubListIndex >= upperBound)) {
				for (int i = 0; i < mergedSubList.length; i++) {
					if ((int) (currentIndex * 2 * currentWidth) + i < upperBound) {
						DataGraph.DataGraph().set((int) (currentIndex * 2 * currentWidth) + i,
					                              mergedSubList[i]);
						DataGraph.DataGraph().setNumOfSwaps(DataGraph.DataGraph().getNumOfSwaps() + 1);
						DataGraph.swapsLabel.setText("Swaps: " + DataGraph.DataGraph().getNumOfSwaps());
					}
				}
				
				currentIndex++;
				lowSubListIndex = currentIndex * 2 * currentWidth;
				highSubListIndex = lowSubListIndex + currentWidth;
				totalMergeCount = 0;
				
				int mergedSize;
				
				if ((currentIndex + 1) * 2 * currentWidth >= upperBound) {
					if (currentIndex * 2 * currentWidth >= upperBound) {
						return false;
					} else {
						mergedSize = currentWidth + (upperBound - highSubListIndex + 1);
					}
				} else {
					mergedSize = 2 * currentWidth;
				}
				
				mergedSubList = new int[mergedSize];
			} else {
				if (lowSubListIndex >= (currentIndex * 2 * currentWidth) + currentWidth ||
						lowSubListIndex >= upperBound) {
					mergedSubList[totalMergeCount++] = DataGraph.DataGraph().getBackingArray()[highSubListIndex++];
					DataGraph.DataGraph().setNumOfAccesses(DataGraph.DataGraph().getNumOfAccesses() + 1);
					DataGraph.accessesLabel.setText("Accesses: " + DataGraph.DataGraph().getNumOfAccesses());
				} else if (highSubListIndex >= (currentIndex * 2 * currentWidth) + (2 * currentWidth) ||
						highSubListIndex >= upperBound) {
					mergedSubList[totalMergeCount++] = DataGraph.DataGraph().getBackingArray()[lowSubListIndex++];
					DataGraph.DataGraph().setNumOfAccesses(DataGraph.DataGraph().getNumOfAccesses() + 1);
					DataGraph.accessesLabel.setText("Accesses: " + DataGraph.DataGraph().getNumOfAccesses());
				} else {
					if (DataGraph.DataGraph().compare(lowSubListIndex, highSubListIndex)) {
						mergedSubList[totalMergeCount++] = DataGraph.DataGraph().getBackingArray()[highSubListIndex++];
					} else {
						mergedSubList[totalMergeCount++] = DataGraph.DataGraph().getBackingArray()[lowSubListIndex++];
					}
				}
			}
		}
		
		return false;
	}
}