package application;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class QuizController {

	private Scene resultsScene;
	private Scene gamesModuleScene;
	private ResultsController controllerResults;
	private WordController currentWords;
	private QuizGame quizGame;
	private boolean isPractice;
	private boolean isBusy = false;

	@FXML private Text headerText;
	@FXML private Text statusText;
	@FXML private Text displayText;
	@FXML private Text avatarText;
	@FXML private Text scoreText;
	@FXML private Text fastText;
	@FXML private Text slowText;
	@FXML private Text timerText;
	@FXML private Slider speedSlider;
	@FXML private TextField inputBox;
	@FXML private Button btnSubmit;
	@FXML private Button btnStart;
	@FXML private Button btnPlayback;
	@FXML private Button btnDontKnow;
	@FXML private Button btnMacronA;
	@FXML private Button btnMacronE;
	@FXML private Button btnMacronI;
	@FXML private Button btnMacronO;
	@FXML private Button btnMacronU;
	@FXML private Button btnNextWord;
	@FXML private Button btnReturn;




	public QuizController() {

	}

	/**
	 * This function sets several unique instances of controllers and scenes as private fields to be
	 * stored as a link to these instances.
	 * 
	 * @param quizGame - a unique quizGame instance
	 * @param resultsScene - the results scene
	 * @param gamesModuleScene - the gamesModule scene
	 * @param results - the resultsController instance
	 * @param timer - the timerController instance
	 */
	public void setSceneLinks(QuizGame quizGame, Scene resultsScene, Scene gamesModuleScene, ResultsController results, TimerController timer) {
		// Set controller reference for controller communication
		this.resultsScene = resultsScene;
		this.controllerResults = results;
		this.quizGame = quizGame;
		this.gamesModuleScene = gamesModuleScene;

	}

	/**
	 * This function handles the event when the submit answer button is clicked (or enter is pressed),
	 * taking the input within the input field box and processing it in quizGame for correctness.
	 * 
	 * @param event when button is clicked by user
	 */
	@FXML
	private void submitAnswer(ActionEvent event) {
		if (!isBusy) {
			try {
				String userInput = this.inputBox.getText().trim();
				inputBox.setText("");

				// Ignore the case where input is blank and return early
				if (userInput.isBlank()) {
					return;
				}

				// If true, then change UI appropriately, and increase score
				if (quizGame.checkAnswer(userInput)) {

					this.setAvatarMessage("Correct!\nGreat Job!");

					disableUserButtons(true);

					// Given they are wrong, if this is not a retry, prompt to try again
				} else if (quizGame.getRetry()){
					this.setAvatarMessage("Incorrect. Try once more!" + "\n");

					// Given they are wrong and not retry, end this round.
				} else {
					inputBox.setDisable(true);
					btnSubmit.setDisable(true);
					disableUserButtons(true);
					this.setAvatarMessage("Hang in there! Kia manawanui");

					// If it is practice mode, also show them the answer"
					if (isPractice) {
						displayText.setText("Correct Spelling: " + currentWords.getWord(quizGame.getRound()-1));
					}
				}

				// Display or remove hint depending on retry status
				if (quizGame.getRetry()) {
					this.showHint();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method sets the score of the quiz for the UI and adds it to the current score depending
	 * on the current round of the game
	 *  
	 * @param round - int value that determine the round of the quiz
	 */

	public void setScoreText(int round) {
		// Update current score text
		if (isPractice) {
			scoreText.setText("PRACTICE MODE");
		} else {
			scoreText.setText("Current Score: " + Integer.toString(quizGame.getScore()));
		}

		// Only display score earned if user gets word correct.
		if (quizGame.getWordResults()[round]) {
			this.setAvatarMessage("Correct! You have just earned " + Integer.toString(60 - (quizGame.getRoundTimes()[round])) + " points");
		} 
	}


	/**
	 * The functions toggles the input buttons to enable/disable the user's input 
	 * depending on the state of the game.
	 * 
	 * @param disableState - boolean value determining what section of buttons disable
	 */
	public void disableUserButtons (boolean disableState) {
		inputBox.setDisable(disableState);
		btnSubmit.setDisable(disableState);
		btnMacronA.setDisable(disableState);
		btnMacronE.setDisable(disableState);
		btnMacronI.setDisable(disableState);
		btnMacronO.setDisable(disableState);
		btnMacronU.setDisable(disableState);
		speedSlider.setDisable(disableState);
		btnPlayback.setVisible(!disableState);
		btnDontKnow.setVisible(!disableState);
		btnNextWord.setVisible(disableState);
	}


	/**
	 * This function handles user click on the next round button to move the quiz game
	 * to the next round (and thus next word), ending the game if appropriate.
	 * 
	 * @param event - User input click on next word button
	 */
	public void startNextRound(ActionEvent event) {

		// End game and move to reward screen if all words tested, else play next round.
		if (quizGame.getGameOver()) {
			this.moveToResults();
		} else {
			quizGame.startNewRound();
			btnNextWord.setVisible(false);
			btnPlayback.setVisible(true);
			btnDontKnow.setVisible(true);
			avatarText.setVisible(false);
			statusText.setVisible(false);

			// Let the user know how many letters in the word being spelled.
			inputBox.setPromptText(createUnderscores(0));


			displayText.setText("Spell Word " + Integer.toString(quizGame.getRound() + 1) + " Of " + 
					Integer.toString(currentWords.getWordList().size()) + ": ");
			disableUserButtons(false);

			// Reset timer if it is quiz mode
			if (!isPractice) {
				timerText.setVisible(true);
				quizGame.startTimer();
			}
		}
	}


	/**
	 * This function handles the clicking of all the macron buttons, and inserts the appropriate
	 * macronised character into the input field box on demand.
	 * 
	 * @param event - any macron button being clicked.
	 */
	public void inputMacron(ActionEvent event) {
		char buttonID = event.getTarget().toString().charAt(19);
		switch (buttonID) {
		case 'A': 
			inputBox.requestFocus();
			inputBox.appendText("ā");
			break;
		case 'E':
			inputBox.requestFocus();
			inputBox.appendText("ē");
			break;
		case 'I':
			inputBox.requestFocus();
			inputBox.appendText("ī");
			break;
		case 'O':
			inputBox.requestFocus();
			inputBox.appendText("ō");
			break;
		case 'U':
			inputBox.requestFocus();
			inputBox.appendText("ū");
			break;
		}
	}


	/**
	 * This function handles moving the window scene to the next scene in the game, which
	 * is the results scene, a summary of each round's outcome in the quiz.
	 * 
	 */
	public void moveToResults() {
		try {

			controllerResults.setWords();

			// Set up the display of the summary results screen
			controllerResults.setTicksAndCrosses(quizGame.getWordResults());

			Stage currentStage = (Stage) btnStart.getScene().getWindow();
			currentStage.setScene(resultsScene);
			currentStage.show();


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This is a getter method to return the boolean value of whether current quiz mode is practice or not
	 * 
	 * @return isPractice - boolean value
	 */
	public boolean getIsPractice() {
		return this.isPractice;
	}


	/**
	 * This is a function to handle when the return button is clicked on in the quiz window, and to
	 * then change the window scene to the main menu.
	 * 
	 * @param event - user clicks the return button
	 */
	@FXML
	private void returnClicked(ActionEvent event) {
		try {
			Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
			window.setScene(this.gamesModuleScene);
			window.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This is a function to handle when the don't know button is clicked in the quiz, skipping the
	 * current round and word. If it results in the game finishing, it automatically moves the player
	 * to the results summary.
	 * 
	 * @param event - user clicks on don't know button
	 */
	@FXML
	public void dontKnowClicked(ActionEvent event) {
		try {
			disableUserButtons(true);
			avatarText.setText("");
			inputBox.setText("");
			timerText.setVisible(false);

			this.setAvatarMessage("Don't worry, you will get it next time :)");

			// This will reveal the answer if it is a practice round. 
			if (this.isPractice) {
				displayText.setText("Correct Spelling: " + currentWords.getWord(quizGame.getRound()));
			}

			quizGame.skipRound();

			//reset speedSlider position
			speedSlider.setValue(1.0);

			Thread.sleep(125);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * This function listens to changes in the speed slider, and updates the game logic appropriately.
	 * Specifically, it sets the speed of the festival voice through a bash command parameter.
	 * 
	 */
	@FXML
	private void updateSpeed() {
		// Ensure we round the value to 2dp before converting to string
		quizGame.setSpeed(String.valueOf(Math.round(speedSlider.getValue()*100.0)/100.0));
	}


	/**
	 * This function handles th event when the start game button is clicked. It adjusts the visibility
	 * of several UI components as appropriate, and signals to the logic of the game in quizGame to
	 * begin the quiz.
	 * 
	 * @param event - start game button is clicked.
	 */
	@FXML
	private void startClicked(ActionEvent event) {
		if (!isBusy) {
			try {
				
				// Set appropriate buttons and text visible or not
				btnSubmit.setVisible(true);
				btnStart.setVisible(false);
				btnPlayback.setVisible(true);
				scoreText.setVisible(true);
				btnDontKnow.setVisible(true);
				btnMacronA.setVisible(true);
				btnMacronE.setVisible(true);
				btnMacronI.setVisible(true);
				btnMacronO.setVisible(true);
				btnMacronU.setVisible(true);
				btnReturn.setVisible(false);
				fastText.setVisible(true);
				slowText.setVisible(true);
				speedSlider.setVisible(true);

				// Display user input box and console text area
				inputBox.setVisible(true);
				displayText.setVisible(true);

				displayText.setText("Spell Word " + Integer.toString(quizGame.getRound() + 1) + " Of " + 
						Integer.toString(currentWords.getWordList().size()) + ": ");

				// Let the user know how many letters in the word being spelled.
				inputBox.setPromptText(createUnderscores(0));


				quizGame.startNewRound();
				btnSubmit.requestFocus();

				if (isPractice) {
					scoreText.setText("PRACTICE MODE");
				} else {
					// Reset timer if it is quiz mode and display it.
					scoreText.setText("Current Score: 0");
					timerText.setVisible(true);
					quizGame.startTimer();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * This function is a setter for the encouraging message given to users who skip/click don't know.
	 * 
	 * @param message - string to be set as the text value.
	 */
	private void setAvatarMessage(String message) {
		avatarText.setText(message);
		avatarText.setVisible(true);
	}


	/**
	 * This function handles displaying the appropriate hint to the user.
	 * If it is quiz mode, it reveals the second character of the round (as well as the length of the word as usual).
	 * If it is practice mode, it instead reveals half the word, specifically every odd character.
	 * 
	 */
	private void showHint() {
		// Gets second letter of current word and sets hint to inputBox if the game is quiz mode
		if (!isPractice) {

			inputBox.setPromptText("Hint: The word is _ " + currentWords.getWord(quizGame.getRound()).charAt(1) + 
					createUnderscores(2));

			// Else it must be practice mode give them a random amount of words.
		} else {
			inputBox.setPromptText("Hint: The word is " + createHalfWord());

		}
	}

	/**
	 * This function creates the practice mode half hidden word to be displayed as hint. It only
	 * displays the odd index characters (so an alternating string is created of underscores and characters).
	 * 
	 * @return string which is the current word but every even character is replaced by underscore
	 */
	private String createHalfWord() {
		String output = "";
		for (int i = 0; i < currentWords.getWord(quizGame.getRound()).length(); i++) {

			// If the character index is even, add underscore instead of character.
			if (i % 2 == 0) {

				//If the character is a space, add space instead of underline.
				if (currentWords.getWord(quizGame.getRound()).substring(i, i+1).equals(" ")) {
					output = output + "  ";
				} else {
					output = output + " _ ";
				}
			} else {
				output = output + " " + currentWords.getWord(quizGame.getRound()).charAt(i);
			}
		}

		return output;
	}

	/**
	 * This function returns a string of underscores for the hint, starting from a given character index.
	 * It is all underscores, and is used mainly to give the user the length of the word.
	 * 
	 */
	private String createUnderscores(int startPosition) {
		String output = "";
		for (int i = startPosition; i < currentWords.getWord(quizGame.getRound()).length(); i++) {

			//If the character is a space, add space instead of underline.
			if (currentWords.getWord(quizGame.getRound()).substring(i, i+1).equals(" ")) {
				output = output + "  ";
			} else {
				output = output + " _ ";
			}

		}
		return output;
	}


	/**
	 * This function handles the event when the user clicks the playback button. It produces the
	 * festival voice sounding out the current word at the selected speed, on demand.
	 * 
	 * @param event - user clicks the playback button.
	 */
	@FXML
	private void playbackClicked(ActionEvent event) {
		if (!isBusy) {
			quizGame.playBack();
		}
	}

	/**
	 * This function is for threading, to prevent multiple scheduled tasks on buttons being spammed, 
	 * by toggling a boolean that allows button input to be processed.
	 * 
	 * @param status - boolean value representing whether the threads are busy or not.
	 */
	public void setBusy(boolean status) {
		this.isBusy = status;
	}

	/**
	 * This function updates the value of the timer, which should be called from timerController.
	 * 
	 * @param newValue - The current timer count time as a string.
	 */
	public void updateTimer(String newValue) {
		timerText.setText("Time left: " + newValue);
	}

	/**
	 * This is an entry point to allow the scene to update when called from the game module controller.
	 * It sets the UI component visibility appropriately and resets the game logic to a clean state.
	 * 
	 * @param controllerWord - wordController instance that holds the current words being used.
	 * @param isPractice - boolean value which determines if quiz mode (false) or practice mode (true).
	 */
	public void entry(WordController controllerWord, boolean isPractice) {

		// Change the visibility of buttons and text so user can only interact with start
		// initially
		this.disableUserButtons(false);
		btnSubmit.setVisible(false);
		btnStart.setVisible(true);
		btnPlayback.setVisible(false);
		btnDontKnow.setVisible(false);
		scoreText.setVisible(false);
		statusText.setVisible(false);
		fastText.setVisible(false);
		slowText.setVisible(false);
		displayText.setVisible(true);
		displayText.setText("Press Start to Begin!");
		btnMacronA.setVisible(false);
		btnMacronE.setVisible(false);
		btnMacronI.setVisible(false);
		btnMacronO.setVisible(false);
		btnMacronU.setVisible(false);
		btnNextWord.setVisible(false);
		btnReturn.setVisible(true);
		avatarText.setVisible(false);
		speedSlider.setVisible(false);
		timerText.setVisible(false);

		// Determine the type of game mode it is
		this.isPractice = isPractice;

		// Set the visibility of the Text Input for pre-start
		inputBox.setVisible(false);

		// Set reference to the words controller in this class and the quiz class with input object
		this.currentWords = controllerWord;
		quizGame.setWords(controllerWord);

		headerText.setText(controllerWord.getlistName());

		// Reset quiz logic
		quizGame.resetGame();

	}

}
