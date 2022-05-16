package application;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;


public class Main extends Application {

	private Scene mainScene;
	private Scene gamesModuleScene;
	private Scene quizScene;
	private Scene resultsScene;
	private Scene rewardScene;
	private MainController controllerMain;
	private GamesModuleController controllerGamesModule;
	private QuizController controllerQuiz;
	private ResultsController controllerResults;
	private RewardController controllerReward;

	@Override
	public void start(Stage primaryStage) {
		try {

			// We create three FXML Loaders and then the respective scene from the FXML
			// Then we obtain the controller instance object for each scene 
			FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("MainMenuScene.fxml"));
			this.mainScene = new Scene(mainLoader.load());
			this.controllerMain = mainLoader.getController();

			FXMLLoader gamesModuleLoader = new FXMLLoader(getClass().getResource("GamesModuleScene.fxml"));
			this.gamesModuleScene = new Scene(gamesModuleLoader.load());
			this.controllerGamesModule = gamesModuleLoader.getController();

			FXMLLoader quizLoader = new FXMLLoader(getClass().getResource("QuizScene.fxml"));
			this.quizScene = new Scene(quizLoader.load());
			this.controllerQuiz = quizLoader.getController();

			FXMLLoader resultsLoader = new FXMLLoader(getClass().getResource("ResultsScene.fxml"));
			this.resultsScene = new Scene(resultsLoader.load());
			this.controllerResults= resultsLoader.getController();

			FXMLLoader rewardLoader = new FXMLLoader(getClass().getResource("RewardScene.fxml"));
			this.rewardScene = new Scene(rewardLoader.load());
			this.controllerReward = rewardLoader.getController();

			// Create an executor service to manage multithreading (cap at 1 only) and its controller
			ExecutorService executor = Executors.newFixedThreadPool(1);
			ExecutorController controllerExec = new ExecutorController(executor);
			controllerExec.setQuiz(controllerQuiz);

			// Create another executor service exclusively for timing
			ExecutorService timerExecutor = Executors.newFixedThreadPool(1);
			TimerController controllerTimer = new TimerController(timerExecutor);
			controllerTimer.setQuiz(controllerQuiz);

			// Create a quiz and pass the executor controller into it
			QuizGame quizLogic = new QuizGame(controllerExec, controllerTimer);

			// We then set the private fields of each instance object to the other relevant scenes
			this.controllerMain.setSceneLinks(this.gamesModuleScene, controllerExec, controllerTimer);
			this.controllerGamesModule.setSceneLinks(this.quizScene, this.mainScene, this.controllerQuiz, controllerExec);
			this.controllerQuiz.setSceneLinks(quizLogic, this.resultsScene, this.gamesModuleScene, this.controllerResults, controllerTimer);
			this.controllerResults.setSceneLinks(quizLogic, this.controllerQuiz, this.mainScene, this.rewardScene, this.controllerReward);
			this.controllerReward.setSceneLinks(this.mainScene, this.gamesModuleScene);

			// Display the main menu on launch
			primaryStage.setScene(this.mainScene);
			primaryStage.setTitle("Kēmu Kupu");
			primaryStage.setResizable(false);

			// Set the window close button to also end background threads using platform exit
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent close) {
					controllerExec.endExecutor();
					controllerTimer.endExecutor();
					Platform.exit();
				}
			});
			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace(); 
		}
	} 

	public static void main(String[] args) {
		launch(args);
	}

}
