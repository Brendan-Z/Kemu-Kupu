package application;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ResultsController {

	private QuizGame quizGame;
	private QuizController controllerQuiz;
	private Scene rewardScene;
	private Scene mainScene;
	private RewardController controllerReward;

	@FXML private Button btnContinue;
	@FXML private Text wordOneText;
	@FXML private Text wordTwoText;
	@FXML private Text wordThreeText;
	@FXML private Text wordFourText;
	@FXML private Text wordFiveText;
	@FXML private ImageView tickOne;
	@FXML private ImageView crossOne;
	@FXML private ImageView tickTwo;
	@FXML private ImageView crossTwo;
	@FXML private ImageView tickThree;
	@FXML private ImageView crossThree;
	@FXML private ImageView tickFour;
	@FXML private ImageView crossFour;
	@FXML private ImageView tickFive;
	@FXML private ImageView crossFive;

	/**
	 * This function sets several unique instances of controllers and scenes as private fields to be
	 * stored as a link to these instances.
	 * 
	 * @param quizGame - a unique quizGame instance
	 * @param quiz - the quizController instance
	 * @param mainScene - the main menu scene
	 * @param rewardScene - the reward scene
	 * @param reward - the rewardController instance
	 */
	public void setSceneLinks(QuizGame quizGame, QuizController quiz, Scene mainScene, Scene rewardScene, RewardController reward) {
		this.controllerQuiz = quiz;
		this.rewardScene = rewardScene;
		this.controllerReward = reward;
		this.quizGame = quizGame;
		this.mainScene = mainScene;
	}

	/**
	 * This method handles when the continue button is pressed. It moves to the rewards scene if
	 * the last game was on quiz mode, otherwise if it was on practice mode it changes the scene to main
	 * menu, as no reward is given at all.
	 * 
	 * @param event - continue button is clicked.
	 */
	public void moveToNextScene(ActionEvent event) {
		try {
			Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

			// If the game mode is practice, return to main menu
			if (controllerQuiz.getIsPractice()) {
				currentStage.setScene(mainScene);

				// Otherwise game mode must be quiz mode, take to the reward scene
			} else {
				this.controllerReward.setScoreAndReward(quizGame.getScore());
				currentStage.setScene(rewardScene);

			}

			currentStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method sets the ticks and crosses in the results scene to indicate whether
	 * each tested word was spelt correctly or incorrectly.
	 * @param wordResults - Boolean array that indicates user's attempt for each word.
	 * 						True = correct, False = incorrect.
	 */
	public void setTicksAndCrosses(boolean[] wordResults) {

		crossOne.setVisible(!wordResults[0]);
		crossTwo.setVisible(!wordResults[1]);
		crossThree.setVisible(!wordResults[2]);
		crossFour.setVisible(!wordResults[3]);
		crossFive.setVisible(!wordResults[4]);

		tickOne.setVisible(wordResults[0]);
		tickTwo.setVisible(wordResults[1]);
		tickThree.setVisible(wordResults[2]);
		tickFour.setVisible(wordResults[3]);
		tickFive.setVisible(wordResults[4]);

	}

	/**
	 * This method sets each wordText to its respective tested word
	 * and displays them within the results scene.
	 * 
	 */
	public void setWords() {

		WordController currentWordController = this.quizGame.getWordController();
		ArrayList<String> testedWords = currentWordController.getWordList();

		for (int i=0; i < testedWords.size(); i++) {

			switch (i) {
			case 0:
				wordOneText.setText(testedWords.get(i));
				break;
			case 1:
				wordTwoText.setText(testedWords.get(i));
				break;
			case 2:
				wordThreeText.setText(testedWords.get(i));
				break;
			case 3:
				wordFourText.setText(testedWords.get(i));
				break;
			case 4:
				wordFiveText.setText(testedWords.get(i));
				break;
			}
		}


	}

}
