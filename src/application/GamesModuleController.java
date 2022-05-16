package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class GamesModuleController implements Initializable{
	
	private Scene quizScene;
	private Scene mainScene;
	private QuizController controllerQuiz;
	private String chosenList;
	private WordController controllerWords;
	private Boolean isPractice = false;
	
	//Array of spelling list names
	private String[] lists = {"Babies","Colours","CompassPoints","Feelings","Months","Software","Uni-life","Weather","Weeks","Work"};
	
	@FXML private ChoiceBox<String> choiceBoxLists;
	@FXML private Button btnSubmit;
	@FXML private Button btnReturn;
	@FXML private Button btnGameMode;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		//Adds all spelling list names within the given ChoiceBox
		choiceBoxLists.getItems().addAll(lists);
		
		//Links a reference of getList() to the ChoiceBox's onAcion method
		choiceBoxLists.setOnAction(this::getList);
	}
	
	/**
	 * This function sets several unique instances of controllers and scenes as private fields to be
	 * stored as a link to these instances.
	 * 
	 * @param quizScene - the quiz game scene 
	 * @param mainScene - the main menu scene
	 * @param quiz - the unique quizController instance
	 * @param executor - the unique executorController instance
	 */
	public void setSceneLinks(Scene quizScene, Scene mainScene, QuizController quiz, ExecutorController executor) {
		this.quizScene = quizScene;
		this.mainScene = mainScene;
		this.controllerQuiz = quiz;
		
		this.controllerWords = new WordController(executor);

	}
	
	
	/**
	 * This method handles user clicking on an option in the drop down. 
	 * It gets user's choice of spelling list and assigns it to the chosenList private instance field value.
	 * 
	 * @param event - user clicks on an option in the dropdown.
	 */
	public void getList(ActionEvent event) {
		
		String list = choiceBoxLists.getValue();
		
		this.chosenList = list;
	}
	
	/**
	 * Switches scene to the quizScene to start the quiz
	 * 
	 * @param event - user clicks on the start quiz button to begin playing.
	 */
	@FXML private void startQuiz(ActionEvent event) {
		try {
			
			//Only start the quiz if the user has picked a valid spelling list
			if (!(this.chosenList == null)) {
				
				Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
				window.setScene(this.quizScene);
				window.show();
				
				// Use the chosen spelling list to set up the words for the word controller
				controllerWords.loadWordList(chosenList);
				controllerQuiz.entry(controllerWords, isPractice);
				
				//Resets user's decision within the ChoiceBox
				choiceBoxLists.setValue(null);
		
			}
			
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		
	}
	
	/**
	 * This function is a button which toggles itself based on what game mode is selected, updating the
	 * boolean value which informs the game what mode we are.
	 * 
	 */
	@FXML
	private void switchGameMode(ActionEvent event) {
		// If it is a practice mode and the button is clicked, change to quiz mode and toggle color.
		if (isPractice) {
			btnGameMode.setText("Mode: Quiz");
			btnGameMode.setStyle("-fx-background-color: #D1F3C5;");
			isPractice = false;
			
		// Otherwise it is not practice mode and clicked, changing to practice mode.
		} else {
			btnGameMode.setText("Mode: Practice");
			btnGameMode.setStyle("-fx-background-color: #89CFF0;");
			isPractice = true;
		}
	}
	
	/**
	 * This method handles when the user clicks the return to main menu button. It changes the current
	 * scene to the main menu scene.
	 * 
	 * @param event - user clicks on the return to main menu button.
	 */
	@FXML private void returnClicked(ActionEvent event) {
		try {
			Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
			window.setScene(this.mainScene);
			window.show();
			
			//Resets user's decision within the ChoiceBox
			this.choiceBoxLists.setValue(null);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}