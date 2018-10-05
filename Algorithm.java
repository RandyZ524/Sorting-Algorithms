public enum Algorithm {
	BUBBLE_SORT, INSERTION_SORT, SELECTION_SORT;
	
	public static Algorithm stringToAlgorithm(String str) {
		return Algorithm.valueOf(str.replace(" ", "_").toUpperCase());
	}
	
	public static String algorithmToString(Algorithm alg) {
		String firstWord = alg.toString().substring(0, alg.toString().indexOf("_"));
		String fullWord = firstWord.substring(0, 1) + firstWord.substring(1).toLowerCase() + " Sort";
		return fullWord;
	}
}