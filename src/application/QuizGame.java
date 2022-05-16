package application;

public class QuizGame {

	private boolean is2ndAttempt;
	private boolean isGameOver;
	private int round;
	private int[] roundTimes;
	private Boolean isAnswerCorrect;
	private WordController controllerWord;
	private boolean[] wordResults;
	private BashController controllerBash;
	private String voiceSpeedCommand = "(Parameter.set 'Duration_Stretch 1)";

	/**
	 * This is a constructor for the quizGame class, which handles logic and computation for the quiz scene.
	 * @param Exec - An Executor Controller class to be used in constructing a unique bash controller instance.
	 * @param timer - A Timer Controller class to be used in constructing a unique bash controller instance.
	 */
	public QuizGame(ExecutorController Exec, TimerController timer) {
		timer.setQuizLogic(this);
		this.controllerBash = new BashController(Exec, timer);
		this.round = 0;
		this.is2ndAttempt = false;
		this.isAnswerCorrect = false;
		this.isGameOver = false;
		this.wordResults = new boolean[5];
		this.roundTimes = new int[5];

	}

	/**
	 * This function is used to perform the logic behind checking answers when submit button is clicked.
	 * It identifies if an answer is correct or not, whether it is the first attempt or not, and causes
	 * the appropriate response behaviour to occur.
	 * @param input - The string answer that user typed into the input field.
	 * @return boolean value of whether the answer was correct or not.
	 */
	public boolean checkAnswer(String input) {
		isAnswerCorrect = false;

		// If the input matches the word, they are correct, move on to next round.
		if (input.equalsIgnoreCase(this.controllerWord.getWord(this.round))) {
			isAnswerCorrect = true;
			controllerBash.runSilent("echo \"Correct\" | festival --tts");
			this.wordResults[this.round] = true;

			this.round++;
			this.is2ndAttempt = false;

			// Given they are incorrect, if this is the first attempt, allow a retry (set to true).
		} else if (!is2ndAttempt){
			controllerBash.runSilent("echo \"Incorrect, try once more\" | festival --tts");

			controllerBash.runSilent("touch commands.scm ; echo '(voice_akl_mi_pk06_cg)' >> commands.scm; echo \"" + 
					voiceSpeedCommand + "\" >> commands.scm ; echo \"(SayText \\\"" + this.controllerWord.getWord(this.round) +
					"\\\")\" >> commands.scm ; echo \"(SayText \\\"" + this.controllerWord.getWord(this.round) + "\\\")\" >> commands.scm; festival -b commands.scm; rm commands.scm");
			controllerBash.runSilent("festival -b commands.scm");
			controllerBash.runSilent("rm commands.scm");

			// Change retry condition to be true
			this.is2ndAttempt = true;

			// If they failed the answer on a retry attempt, move on to next round.
		} else {

			controllerBash.runSilent("echo \"Incorrect\" | festival --tts");
			this.round++;
			this.is2ndAttempt = false;
		}

		// Check if game is now over (no more words)
		if (this.round > this.controllerWord.getWordList().size() - 1) {
			this.isGameOver = true;
		}

		// Status is true only if they successfully spelled the word.
		return isAnswerCorrect;
	}

	/**
	 * This function starts a new round by enunciating to the user what the current word to be spelled is.
	 */
	public void startNewRound() {
		if (!isGameOver) {

			this.voiceSpeedCommand = "(Parameter.set 'Duration_Stretch 1)";

			controllerBash.runSilent("touch commands.scm ; echo '(voice_akl_mi_pk06_cg)' >> commands.scm; echo \"" + 
					voiceSpeedCommand + "\" >> commands.scm ; echo \"(SayText \\\"" + this.controllerWord.getWord(this.round) + "\\\")\" >> commands.scm");
			controllerBash.runSilent("festival -b commands.scm");
			controllerBash.runSilent("rm commands.scm");

		}

		// reset the retry condition in a new round.
		this.is2ndAttempt = false;

	}

	/**
	 * This function is a getter method for the wordResults boolean array, which details successful words
	 * @return boolean array of size 5 representing each round
	 */
	public boolean[] getWordResults() {
		return this.wordResults;
	}

	/**
	 * This function is a getter method for the wordController instance
	 * @return wordController instance used
	 */
	public WordController getWordController() {
		return this.controllerWord;
	}

	/**
	 * This function is a getter method for the score
	 * @return int score
	 */
	public int getScore() {
		int Score = 0;

		// Calculate the score of all successfully spelled words as 60 seconds - time taken.
		for (int i=0; i<this.round; i++) {
			if (wordResults[i]) {
				Score += (60 - roundTimes[i]);
			}
		}

		return Score;
	}


	/**
	 * This function is a getter method for the time taken on each round
	 * @return array of 5 int representing round times
	 */
	public int[] getRoundTimes() {
		return this.roundTimes;
	}

	/**
	 * This function is a getter method that returns the current status of the round, whether the user has 
	 * attempted once or not yet.
	 * @return Boolean on whether the user has attempted once or not.
	 */
	public boolean getRetry() {
		return this.is2ndAttempt;
	}

	/**
	 * This function is a getter method that returns the current round/word the game is on.
	 * @return int value of current round/word.
	 */
	public int getRound() {
		return this.round;
	}

	/**
	 * This function is a getter method that returns whether the game has finished or not.
	 * @return boolean value of whether game is over.
	 */
	public boolean getGameOver() {
		return this.isGameOver;
	}

	/**
	 * This function resets the quiz game, by setting the unique instance fields for each game to zero.
	 */
	public void resetGame() {
		this.round = 0;
		this.isGameOver = false;

		for (int i = 0; i<5; i++) {
			this.wordResults[i] = false;
		}

	}

	/**
	 * This function is used to skip the current round being played, marked as a failure.
	 */
	public void skipRound() {
		this.round++;
		this.is2ndAttempt = false;

		// Check if game is now over (no more words)
		if (this.round > this.controllerWord.getWordList().size() - 1) {
			this.isGameOver = true;
		}
	}

	/**
	 * This function is a setter method used to update the playback speed of festival based on given speed value.
	 * @param speedVal - A string representing the speed value as obtained from the slider on screen.
	 */
	public void setSpeed(String speedVal) {
		this.voiceSpeedCommand = "(Parameter.set 'Duration_Stretch " + speedVal + ")";
	}

	/**
	 * This function is a setter method of the word controller class that manages the words to be tested
	 * @param gameWords - The word controller instance that exists and manages all words in the game.
	 */
	public void setWords(WordController gameWords) {
		this.controllerWord = gameWords;
	}

	/**
	 * This function handles the logic to start processing a fresh timer in a separate thread.
	 */
	public void startTimer() {
		controllerBash.runTimer();
	}

	/**
	 * This function takes the time recorded by the timercontroller class and sets the field.
	 * @param time - time elapsed in the round recorded by timer controller.
	 * @param round - the round which was recorded.
	 */
	public void setRoundTime(int time, int round) {
		this.roundTimes[round] = time;
	}

	/**
	 * This function will return the status field, which is what the most recent round's outcome was. 
	 * @return Boolean status of whether the round was successful or a failure.
	 */
	public Boolean getStatus() {
		return this.isAnswerCorrect;
	}

	/**
	 * This function handles when the playback button is clicked, enunciating the current word.
	 */
	public void playBack() {
		try {
			controllerBash.runSilent("touch commands.scm ; echo '(voice_akl_mi_pk06_cg)' >> commands.scm; echo \"" + 
					voiceSpeedCommand + "\" >> commands.scm ; echo \"(SayText \\\"" + this.controllerWord.getWord(this.round) + "\\\")\" >> commands.scm");
			controllerBash.runSilent("festival -b commands.scm");
			controllerBash.runSilent("rm commands.scm");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}