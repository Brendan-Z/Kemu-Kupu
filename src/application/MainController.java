package application;

import java.io.IOException;
import java.net.URISyntaxException;
import javafx.application.Application;
import javafx.application.HostServices;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainController extends Application {

	private Scene gamesModuleScene;
	private ExecutorController controllerExec;
	private TimerController controllerTimer;

	@FXML private Button btnNewQuiz;
	@FXML private Button btnQuit;
	@FXML private Button btnHelp;

	/**
	 * This function sets several unique instances of controllers and scenes as private fields to be
	 * stored as a link to these instances.
	 * 
	 * @param gamesModuleScene - the games module scene
	 * @param executor - the unique executorController instance
	 * @param timer - the unique timerController instance
	 */
	public void setSceneLinks(Scene gamesModuleScene, ExecutorController executor, TimerController timer) {
		this.gamesModuleScene = gamesModuleScene;
		this.controllerExec = executor;
		this.controllerTimer = timer;
	}


	/**
	 * This method handles when the user clicks the new quiz button to enter the games module.
	 * It changes the scene displayed on the only existing stage/window to the games module scene.
	 * 
	 * @param event - user clicks the new quiz button
	 */
	@FXML private void clickNewQuiz(ActionEvent event) {
		try {
			Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
			window.setScene(this.gamesModuleScene);
			window.show();
		} catch (Exception e) {
			// Catch the error
			e.printStackTrace(); 
		}

	}	


	// This method will open the help manual for the user.
	@FXML private void showHelpManual(ActionEvent event) throws IOException, URISyntaxException {
		try {
			HostServices hostServices = getHostServices();
			hostServices.showDocument("userManual.pdf");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * This method handles exiting the game via clicking the quit button.
	 * 
	 * @param event - user clicks the quit button.
	 */

	// Quit the game
	@FXML private void clickQuit(ActionEvent event) {
		controllerExec.endExecutor();
		controllerTimer.endExecutor();
		Platform.exit();
	}

	/**
	 * This method is only needed for implementing Application abstract class, needed for host services.
	 */
	@Override
	public void start(Stage arg0) throws Exception {

	}

}
