public enum Algorithm {
	BUBBLE_SORT("n^2"), INSERTION_SORT("n^2"), SELECTION_SORT("n^2"), COMB_SORT("n^2"), MERGE_SORT("nlogn");
	
	private String timeComplexity;
	
	public String getTimeComplexity() { return timeComplexity; }
	
	private Algorithm(String tc) {
		timeComplexity = tc;
	}
	
	public static Algorithm stringToAlgorithm(String str) {
		return Algorithm.valueOf(str.replace(" ", "_").toUpperCase());
	}
	
	public static String algorithmToString(Algorithm alg) {
		String firstWord = alg.toString().substring(0, alg.toString().indexOf("_"));
		String fullWord = firstWord.substring(0, 1) + firstWord.substring(1).toLowerCase() + " Sort";
		return fullWord;
	}
	
	public static String[] stringValues() {
		String[] stringArray = new String[Algorithm.values().length];
		
		for (int i = 0; i < stringArray.length; i++) {
			stringArray[i] = algorithmToString(Algorithm.values()[i]);
		}
		
		return stringArray;
	}
}