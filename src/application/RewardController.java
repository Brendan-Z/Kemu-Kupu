package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class RewardController {

	@FXML private Button btnPlayAgain;
	@FXML private Button btnMenu;
	@FXML private Scene mainScene;
	@FXML private Scene gamesModuleScene;
	@FXML private Text scoreText;
	@FXML private ImageView star1;
	@FXML private ImageView star2;
	@FXML private ImageView confetti;
	@FXML private ImageView kapaiKiwi;
	@FXML private ImageView mediumKiwi;
	@FXML private Text mediumText;

	/**
	 * This function sets several unique instances of controllers and scenes as private fields to be
	 * stored as a link to these instances.
	 * 
	 * @param mainScene - the main menu scene
	 * @param gamesModuleScene - the games module scene
	 */
	public void setSceneLinks(Scene mainScene, Scene gamesModuleScene) {
		this.mainScene = mainScene;
		this.gamesModuleScene = gamesModuleScene;
	}


	/**
	 * This method sets the scene to the appropriate display for the user depending on their score from the
	 * last game. It determines the exact reward that should be displayed, based on how well they have
	 * scored, which is dependent on their time taken and number of correctly spelled words.
	 * 
	 * @param quizScore - int value of the overall score from the last game.
	 */
	public void setScoreAndReward(int quizScore) {
		scoreText.setText("Score: " + Integer.toString(quizScore) + "/300");

		// Disable all images first and the medium text, so we can reset the "reward"
		this.disableAll();

		// Perfect kapai kiwi for 5/5, medium kiwi + effects for 3 or 4, medium  kiwi plus one effect for 2 or 3
		// medium kiwi only for 0 or 1. Each medium kiwi with a different text message.
		if (quizScore > 240) {
			kapaiKiwi.setVisible(true);
			confetti.setVisible(true);
			star1.setVisible(true);
			star2.setVisible(true);
		} else {
			mediumKiwi.setVisible(true);

			if (quizScore > 160 ) {
				mediumText.setText("Great job! Almost there!");
				confetti.setVisible(true);
				star2.setVisible(true);

			} else if (quizScore > 70) {
				mediumText.setText("Good work! Keep it up!");
				star2.setVisible(true);

			} else {
				mediumText.setText("Bad Luck! Keep trying!");
			}

			mediumText.setVisible(true);
		}
	}

	/**
	 * Disables all images visibility and sets mediumText to invisible
	 * 
	 */
	public void disableAll() {
		star1.setVisible(false);
		star2.setVisible(false);
		confetti.setVisible(false);
		kapaiKiwi.setVisible(false);
		mediumKiwi.setVisible(false);
		mediumText.setVisible(false);

	}

	/**
	 * This method handles when the return to menu button is clicked, changing the scene
	 * to the main menu scene as appropriate.
	 * 
	 * @param event - user clicks the return to menu button.
	 */
	@FXML
	private void returnToMenu(ActionEvent event) {
		try {
			Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
			window.setScene(this.mainScene);
			window.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method handles when the play again button is clicked, changing the scene to the
	 * games module scene, allowing users to pick another word category to quiz themselves on again.
	 * 
	 * @param event - user clicks the play again button.
	 */
	@FXML
	private void playAgain(ActionEvent event) {
		try {
			Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
			window.setScene(this.gamesModuleScene);
			window.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
