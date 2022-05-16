package application;

import java.util.ArrayList;

public class WordController {

	private String listName;
	private ArrayList<String> words;
	private BashController controllerBash;

	public WordController(ExecutorController exec) {
		this.controllerBash = new BashController(exec);
	}

	/** T
	 * his function uses bash to obtain the words we will quiz the user on, taking words uniquely from a
	 * specified list type.
	 * 
	 * @param wordsType - String denoting the filename of the word category to be tested.
	 */
	public void loadWordList(String wordsType) {

		this.words = controllerBash.getLines("shuf -n 5 ./words/" + wordsType + " > ./words/.selection ; echo \"$(cat ./words/.selection)\"");

		this.listName = wordsType;

	}


	/**
	 * This is a getter method which returns the current round given the round of the game.
	 * 
	 * @param round - which round the word is allocated to in the game.
	 * @return string value of the tested word at the given round of the game.
	 */
	public String getWord(int round) {
		return this.words.get(round);
	}


	/**
	 * This is a getter method which returns the list of 5 words being tested in the current game.
	 * 
	 * @return ArrayList<String> of 5 strings representing the 5 words being tested.
	 */
	public ArrayList<String> getWordList() {
		return this.words;
	}


	/**
	 * A getter method returning the current category of words being tested.
	 * 
	 * @return String representing the category of the words being tested.
	 */
	public String getlistName() {
		return this.listName;
	}

}